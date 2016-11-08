package com.zju.controller;

import com.zju.async.EventModel;
import com.zju.async.EventProducer;
import com.zju.async.EventType;
import com.zju.model.*;
import com.zju.service.CommentService;
import com.zju.service.LikeService;
import com.zju.service.QuestionService;
import com.zju.service.UserService;
import com.zju.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/10/9.
 * 这个是问题发布相关的类
 */
@Controller
public class QuestionController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;//当前用户

    @Autowired
    LikeService likeService;

    @RequestMapping("/questionForm")
    public String questionForm(){
        return "questionForm";
    }

    @RequestMapping(value = "/question/add1")//页面进来需要的处理
    @ResponseBody
    public String addQuestion1(@RequestParam(value="title",required = false,defaultValue = "title1") String title,
                               @RequestParam(value="content",required = false,defaultValue = "content1") String content){
        try {

            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreateDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                //question.setUserId(WendaUtil.ANNOYMOUS_USERID);//设置匿名用户，在utils包中谈价匿名用户的id
                return WendaUtil.getJSONString(999);

            } else {
                question.setUserId(hostHolder.getUser().getId());
            }

            if (questionService.addQuestion(question) > 0) {
                //问题发布成功时候发一个异步事件
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                        .setActorId(question.getUserId())
                        .setEntityId(question.getId())
                        .setExts("title", question.getTitle())
                        .setExts("content", question.getContent()));
                return WendaUtil.getJSONString(0);
            }
        }
        catch (Exception e){
            logger.error("增加文章失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"添加文章失败");

//        questionService.addQuestion(question);
//        return "redirect:/";
    }


    //下面写问答的详细页面
    @RequestMapping("/question/{qid}")
    public String questionDetail(@PathVariable("qid") int qid, Model model){
        Question question= questionService.selectById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));

        List<Comment> commentList=commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        //这里不仅需要评论的东西，还需要评论用户的头像等东西
        List<ViewOfObject> comments=new ArrayList<>();
        for (Comment comment:commentList){
            ViewOfObject vo=new ViewOfObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            //添加赞与踩相关的功能；

            //判断当前用户是否喜欢
            if (hostHolder.getUser()==null ){
                vo.set("liked",0);
            }else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId() ));
            comments.add(vo);

        }
        model.addAttribute("comments",comments);

        return "detail";
    }


    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        //提示，@param常见形式有：<input>标签提交请求参数
        try{
            Question questionNew=new Question();
            questionNew.setTitle(title);
            questionNew.setContent(content);
            questionNew.setCreateDate(new Date());
            Random random=new Random();
            questionNew.setCommentCount(random.nextInt(1000));

            if (hostHolder.getUser()==null){
                questionNew.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else{

                questionNew.setUserId(hostHolder.getUser().getId());
            }

            if (questionService.addQuestion(questionNew)>0){
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("增加文章失败"+e.getMessage());
        }

        return WendaUtil.getJSONString(1,"添加文章失败");
    }



}
