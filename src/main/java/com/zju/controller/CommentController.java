package com.zju.controller;

import com.zju.model.Comment;
import com.zju.model.EntityType;
import com.zju.model.HostHolder;
import com.zju.service.CommentService;
import com.zju.service.QuestionService;
import com.zju.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/10.
 */
@Controller
public class CommentController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = "/addComment")
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        //添加会后经常需要加一个try--catch
        try{
            Comment comment=new Comment();
            //根据用户是否在线，设置comment的uerId
            if (hostHolder!=null){
                comment.setUserId(hostHolder.getUser().getId());
            }else{
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            //当添加评论数成功时候，应该更新评论数，这里有两条SQL语句的发生，需要用事务。
            commentService.addComment(comment);
            int comCount=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),comCount);


        }catch (Exception e){
            logger.error("增加评论失败："+e.getMessage());
        }
        //这时候添加变量到model是没有任何作用的，因为根本不会到达view层
        return "redirect:/question/"+questionId;//这里跳转到的是Question中的Controller，而非是直接的网页，记住！！！！

    }

}
