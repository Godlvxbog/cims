package com.zju.async.handler;

import com.zju.async.EventHandler;
import com.zju.async.EventModel;
import com.zju.async.EventType;
import com.zju.model.Message;
import com.zju.model.User;
import com.zju.service.MessageService;
import com.zju.service.UserService;
import com.zju.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * //只有继承EventHandler才能被
 * applicationContext.getBeansOfType(EventHandler.class)发现
 * 这个方法就是处理like事务的方法，和关心的时间
 */
@Component//让其生成一个对象
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;//当收到点赞时候，就要
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        message.setFormId(WendaUtil.SYSTEM_USERID);
//        message.setToId(model.getEntityOwnerId());
        message.setToId(model.getActorId());
        message.setCreatedDate(new Date());

        User user=userService.getUser(model.getActorId());
        message.setContent("用户："+user.getName()+"赞了你的评论,http://127.0.0.1:8080/question/"+model.getExts("questionId"));


        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
