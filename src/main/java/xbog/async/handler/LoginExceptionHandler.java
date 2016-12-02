package xbog.async.handler;

import xbog.async.EventHandler;
import xbog.async.EventModel;
import xbog.async.EventType;
import xbog.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/18.
 */
@Component
public class LoginExceptionHandler implements EventHandler{
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //CCCC发现登陆异常
        Map<String,Object> map=new HashMap<>();
        map.put("username",model.getExts("username"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"),"登陆异常","mails/login_exception.html",map);

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
