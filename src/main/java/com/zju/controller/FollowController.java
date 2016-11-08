package com.zju.controller;

import com.zju.async.EventModel;
import com.zju.async.EventProducer;
import com.zju.async.EventType;
import com.zju.model.*;
import com.zju.service.CommentService;
import com.zju.service.FollowService;
import com.zju.service.QuestionService;
import com.zju.service.UserService;
import com.zju.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/9.
 * 这个是问题发布相关的类
 */
@Controller
public class FollowController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    HostHolder hostHolder;//当前用户
    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;

    @RequestMapping(path = "/followUser")
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        //1、判断是否在线
        if (hostHolder==null){
            return WendaUtil.getJSONString(999);//跳到重新登录去了
        }
        //2、关注业务，操作粉丝和关注对象的两个数据库
        boolean ret=followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        //3、发送点赞的现场到消息队列，即将触发事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        //4、返回你关注的对象的个数
        return  WendaUtil.getJSONString(ret?0:1,String.valueOf(
                followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }

    //下面写取消关注业务
    @RequestMapping(path = "/unfollowUser")
    @ResponseBody
    public String unFollowUser(@RequestParam("userId") int userId){
        if (hostHolder==null){
            return WendaUtil.getJSONString(999);//跳到重新登录去了
        }
        boolean ret=followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        //返回你关注的对象的个数
        return  WendaUtil.getJSONString(ret?0:1,String.valueOf(
                followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }





    @RequestMapping(path = {"/followQuestion"},method = RequestMethod.POST)
    public String followQuestion(@RequestParam("questionId") int questionId) {
        //判断用户是否登陆
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }
        //选出关注的问题
        Question q = questionService.selectById(questionId);
        if (q == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }
        //调用关注业务操作底层数据
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        //添加现场数据到消息队列
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));
        //返回渲染的数据
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        //return WendaUtil.getJSONString(ret ? 0 : 1, info);
        return "redirect:/";
    }


    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        Question q = questionService.selectById(questionId);
        if (q == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtil.getJSONString(ret ? 0 : 1, info);
    }

    //某个用户的关注对象
    @RequestMapping(path = {"/user/{uid}/followees"})
    public String followees(Model model, @PathVariable("uid")int userId) {
        //uid的关注对象从数据库中提取出来
        List<Integer> followeeIds=followService.getFollowees(userId,EntityType.ENTITY_USER,10);
        if (hostHolder!=null){
            //把提取到数据包装起来加到view层中
            model.addAttribute("followees",getUserInfo(hostHolder.getUser().getId(),followeeIds));
        }else{
            model.addAttribute("followees",getUserInfo(WendaUtil.ANONYMOUS_USERID,followeeIds));
        }
        model.addAttribute("curUser",hostHolder.getUser());
        model.addAttribute("curFolloweeCount",followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER));
        return "followees";
    }

    @RequestMapping(path = {"/user/{uid}/followers"})
    public String followers(Model model,@PathVariable("uid") int userId) {
        List<Integer> followerIds=followService.getFollowers(EntityType.ENTITY_USER,userId,10);
        if (hostHolder!=null){
            model.addAttribute("followers",getUserInfo(hostHolder.getUser().getId(),followerIds));
        }else{
            model.addAttribute("followers",getUserInfo(WendaUtil.ANONYMOUS_USERID,followerIds));
        }
        model.addAttribute("curUser",hostHolder.getUser());
        model.addAttribute("curFollowerCount",followService.getFollowerCount(EntityType.ENTITY_USER,hostHolder.getUser().getId()));
        return "followers";

    }

    //写一个公共的函数用来获取粉丝或者关注的对象
    //并包装以便于view渲染
    private List<ViewOfObject> getUserInfo(int localUserId,List<Integer> userIds){
        List<ViewOfObject> userInfos=new ArrayList<>();
        for (Integer uid:userIds){
            User user=userService.getUser(uid);
            if (user==null){
                continue;
            }
            ViewOfObject vo =new ViewOfObject();
            vo.set("user",user);
            vo.set("commentCount",commentService.getUserCommentCount(uid));
            vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,uid));
            vo.set("followeeCount",followService.getFolloweeCount(uid,EntityType.ENTITY_USER));
            if (localUserId!=0){
                vo.set("followed",followService.isFollower(localUserId,EntityType.ENTITY_USER,uid));
            }else{
                vo.set("followed",false);
            }

            userInfos.add(vo);
            System.out.println("打印用户"+user);

        }
        return  userInfos;
    }


}
