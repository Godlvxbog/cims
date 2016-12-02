package xbog.controller;

import xbog.async.EventModel;
import xbog.async.EventProducer;
import xbog.async.EventType;
import xbog.model.Comment;
import xbog.model.EntityType;
import xbog.model.HostHolder;
import xbog.service.CommentService;
import xbog.service.LikeService;
import xbog.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2016/10/17.
 */
@Controller
public class LikeController {

    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;
    @Autowired
    CommentService commentService;//由于需要questionId，所以需要在comment表当中找

    //设置是否，就调用like，但是显示是否就要分到questionController中
    @RequestMapping(path = "/like" ,method = RequestMethod.POST)
    public String like(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        //获取点赞的对象id，
        Comment comment=commentService.getCommentById(commentId);
//        comment.getEntityId()指的是评论的问题的id
        eventProducer.fireEvent(new EventModel(EventType.LIKE).
                setActorId(hostHolder.getUser().getId())
                .setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setEntityOwnerId(comment.getUserId())
                .setExts("questionId",String.valueOf(comment.getEntityId())));
        long likeCount=likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
//        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
        return "redirect:/";
    }

    @RequestMapping(path = "/dislike")
    public String dislike(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount=likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
//        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
        return "redirect:/";

    }
}
