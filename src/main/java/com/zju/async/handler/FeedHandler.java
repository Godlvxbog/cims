package com.zju.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.zju.async.EventHandler;
import com.zju.async.EventModel;
import com.zju.async.EventType;
import com.zju.model.EntityType;
import com.zju.model.Feed;
import com.zju.model.Question;
import com.zju.model.User;
import com.zju.service.*;
import com.zju.util.JedisAdapter;
import com.zju.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**

 */
@Component//让其生成一个对象
public class FeedHandler implements EventHandler{

    @Autowired
    MessageService messageService;//当收到点赞时候，就要
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    FeedService feedService;
    //把核心数据给创建出来
    private String buildFeedData(EventModel model){
        Map<String,String> map=new HashMap<>();
        User actor=userService.getUser(model.getActorId());
        if (actor==null){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());


        if (model.getType()==EventType.COMMENT ||
                (model.getType()==EventType.FOLLOW&&model.getEntityType()== EntityType.ENTITY_QUESTION)){
            Question question=questionService.selectById(model.getEntityId());
            if (question==null){
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null ;




    }

    //这是一个拉取的模式
    @Override
    public void doHandle(EventModel model) {
        Random random=new Random();
        model.setActorId(1+random.nextInt(10));

        Feed feed=new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData()==null){
            return;
        }
        feedService.addFeed(feed);

        //给事件的粉丝进行推
        List<Integer> followers=followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),10);
        followers.add(0);
        for (int follower:followers){
            String timelinekey= RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelinekey,String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventType() {

        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}



