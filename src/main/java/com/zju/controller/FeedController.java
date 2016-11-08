package com.zju.controller;

import com.zju.model.EntityType;
import com.zju.model.Feed;
import com.zju.model.HostHolder;
import com.zju.service.FeedService;
import com.zju.service.FollowService;
import com.zju.util.JedisAdapter;
import com.zju.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 *
 */
@Controller
public class FeedController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    //拉取模式
    @RequestMapping(path = "/pullfeeds")
    public String getPullFeeds(Model model){
        int localUserId=hostHolder.getUser()==null ? 0:hostHolder.getUser().getId();
        List<Integer> followees=new ArrayList<>();//默认就是一个null
        if (localUserId!=0){
            //取出所有的关注者
            followees=followService.getFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);


        }
        //然后找出所有的Feed
        List<Feed>  feeds=feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);


        return "feeds";
    }

    //推模式
    @RequestMapping(path = "/pushfeeds")
    public String getPushFeeds(Model model){
        int localUserId=hostHolder.getUser()==null ? 0:hostHolder.getUser().getId();
        List<String> feedIds=jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId),0,10);
        List<Feed> feeds=new ArrayList<>();
        for (String feedId:feedIds){
            Feed feed= feedService.getFeedById(Integer.parseInt(feedId));
            if (feed==null){
                continue;
            }
            feeds.add(feed);
        }
        model.addAttribute("feeds",feeds);
        return "feeds";
    }









}

