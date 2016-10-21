package com.zju.service;

import com.zju.dao.CommentDao;
import com.zju.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
@Service
public class CommentService {

    @Autowired
    CommentDao commentDao;

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        //敏感词过滤还没有完成
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        return commentDao.addComment(comment) >0 ?comment.getId():0;
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }

    public boolean deleteComment(int commentId){
        return  commentDao.updateStatus(commentId,1)>0;
    }

    public Comment getCommentById(int id){
        return commentDao.getCommentById(id);
    }





}
