package com.zju.controller;

import com.zju.model.HostHolder;
import com.zju.model.Message;
import com.zju.model.User;
import com.zju.model.ViewOfObject;
import com.zju.service.MessageService;
import com.zju.service.UserService;
import com.zju.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
@Controller
public class MessageController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;


    //发送消息,因为是弹窗所以用json数据@ResponseBody
    @RequestMapping(path = "/msg/addMessage1")
    @ResponseBody
    public String addMessage1(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        //添加会后经常需要加一个try--catch
        try {
            //根据用户是否在线，设置message的uerId
            if (hostHolder == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            User user = userService.selectByName(toName);//发送消息的对方
            if (user == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }

            //说明发送消息双方都存在的，下面开始构造消息
            Message message = new Message();
            message.setFormId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setConversationId(String.format("%d_%d", hostHolder.getUser().getId(), user.getId()));

            messageService.addMessage(message);

            //准备返回
            return WendaUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发送失败");
        }
    }


    //发送消息,因为是弹窗所以用json数据@ResponseBody
    @RequestMapping(path = "/messageSend")
    public String addMessageSend() {
        return "messageSend";
    }



    //发送消息,因为是弹窗所以用json数据@ResponseBody
    @RequestMapping(path = "/msg/addMessage")
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        //添加会后经常需要加一个try--catch
        try {
            //根据用户是否在线，设置message的uerId
            if (hostHolder == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            User user = userService.selectByName(toName);//发送消息的对方
            if (user == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }

            //说明发送消息双方都存在的，下面开始构造消息
            Message message = new Message();
            message.setFormId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setConversationId(String.format("%d_%d", hostHolder.getUser().getId(), user.getId()));

            messageService.addMessage(message);

            return WendaUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发送失败");
        }
    }


    //消息显示页
    @RequestMapping(path = "/msg/list")
    public String getConversationList(Model model) {
        if (hostHolder.getUser()==null){
            return "redirect:/reglogin";
        }
        int localUserId =hostHolder.getUser().getId();
        List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
        List<ViewOfObject> VOs=new ArrayList<>();
        for (Message conversation:conversationList){
            ViewOfObject vo=new ViewOfObject();
            vo.set("message",conversation);
            //这里只要别人发给你的消息
            int targetId=( localUserId==conversation.getFormId() ) ? conversation.getToId() :conversation.getFormId();

            vo.set("user",userService.getUser(targetId));
            vo.set("unread",messageService.getConversationUnreadCount(conversation.getConversationId(),localUserId));
            VOs.add(vo);
        }
        model.addAttribute("conversations",VOs);
        return "letter";
    }

    //消息显示页，实际上你要做的就是把消息属性展示出来
    @RequestMapping(path = "/msg/detail")
    public String getConversationDeatail(Model model,
                                         @RequestParam("conversationId") String conversationId) {
        try{
            List<Message> messageList=messageService.getConversationDetail(conversationId,0,10);
            //复杂的显示页就用viewOfObject
            List<ViewOfObject> msgVOs=new ArrayList<>();
            for (Message msg:messageList){
                ViewOfObject vo=new ViewOfObject();
                vo.set("message",msg);
                vo.set("user",userService.getUser(msg.getFormId()));
                msgVOs.add(vo);
            }
            model.addAttribute("messages",msgVOs);


        }catch (Exception e){
            logger.error("消息详情页显示失败"+e.getMessage());
        }


        return "letterDetail";
    }

}
