package com.zju.controller;

import com.zju.model.*;
import com.zju.service.CommentService;
import com.zju.service.FollowService;
import com.zju.service.QuestionService;
import com.zju.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 * 下面测试第一个正式的Controller
 */
@Controller
public class HomeController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    //引入Service层
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;




    //第一个主页
    @RequestMapping(value = {"/home","/"})
    public String home(Model model) {
        List<ViewOfObject> vos=getQuestions(0,0,10);
        model.addAttribute("vos", vos);

        return "home";
    }


    //第二个页面user，这里面的model指的是，没有那个页面的ui.model
    @RequestMapping("/user/{userId}")
    public String userHome(Model model, @PathVariable("userId") int userId) {
        List<ViewOfObject> vos= getQuestions(userId,0,10);

        model.addAttribute("vos", vos);
        return "home";
    }

    @RequestMapping("/profile/{userId}")
    public String userProfile(Model model, @PathVariable("userId") int userId) {
        List<ViewOfObject> vos= getQuestions(userId,0,10);

        User user = userService.getUser(userId);
        ViewOfObject vo = new ViewOfObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        model.addAttribute("vos",vos);
        return "profile";
    }

    //提取出公共的方法
    public List<ViewOfObject> getQuestions(int userId,int offset,int limit){
        List<Question> qlist = questionService.getLatestQuestion(userId, offset, limit);

        //直接用vo的数组；本身VO类是一个map
        ArrayList<ViewOfObject> vos = new ArrayList<>();
        for (Question question : qlist) {
            ViewOfObject vo = new ViewOfObject();
            //在这里question是由questionService获取list然后遍历得到；
            //而对应的question的user由question.getUerID得到
            //得到userId之后根据UserService中的getUser可以得到user
            vo.set("question", question);
            vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }

        return vos;
    }



}

