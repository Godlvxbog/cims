package com.zju.controller;

import com.zju.model.HostHolder;
import com.zju.model.Question;
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

import java.util.Date;
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
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;//当前用户

    @RequestMapping("/questionForm")
    public String questionForm(){
        return "questionForm";
    }

    @RequestMapping(value = "/question/add1")//页面进来需要的处理
    public String addQuestion1(@RequestParam(value="title",required = false,defaultValue = "title1") String title,
                               @RequestParam(value="content",required = false,defaultValue = "content1") String content){
        Question question=new Question();
        question.setTitle(title);
        question.setContent(content);
        question.setCreateDate(new Date());
        question.setCommentCount(0);
        if (hostHolder.getUser()==null){
            //question.setUserId(WendaUtil.ANNOYMOUS_USERID);//设置匿名用户，在utils包中谈价匿名用户的id
            return WendaUtil.getJSONString(999);

        }else{
            question.setUserId(hostHolder.getUser().getId());
        }
        questionService.addQuestion(question);

        return "redirect:/";
    }


    //下面写问答的详细页面
    @RequestMapping("/question/{qid}")
    public String questionDetail(@PathVariable("qid") int qid, Model model){
        Question question= questionService.selectById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));
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
