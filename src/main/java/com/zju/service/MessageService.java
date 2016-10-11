package com.zju.service;

import com.zju.dao.MessageDao;
import com.zju.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    public  int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
       return messageDao.addMeassage(message) >0 ? message.getId() : 0;
    }

    public List<Message> getConversationDetail(String convstId,int offset, int limit){
        return messageDao.getConversationDetail(convstId,offset,limit);
    }


    public List<Message> getConversationList(int userId,int offset, int limit){
        return messageDao.getConversationList(userId,offset,limit);
    }

    public int getConversationUnreadCount(String conversationId,int userId){
        return messageDao.getConversationUnreadCount(conversationId, userId);
    }




}
