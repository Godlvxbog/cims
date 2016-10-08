package com.zju.controller;

import com.zju.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/5.
 */

//专门负责登陆注册的一个Controller,这里面已经准备好了UserService和@RequestParam等参数，需要去处理
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;
    //这里是写入数据用Post:这车过来需要传入两个参数，username和password;
//    这里面的页面之间跳转就是你的业务逻辑，需要思考
    @RequestMapping(path = "/reg/")
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse response){
        //把可能出现的异常try起来
        try {
            HashMap<String,String> map=userService.register(username, password);
            if (map.containsKey("ticket")){
                //把的cookie写入到response之中
                Cookie cookie=new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return  "login";
            }
        }catch (Exception e){
            logger.error("注册异常： "+e.getMessage());
            return "login";
        }
    }

    //下面做登陆功能
    @RequestMapping(path = "/login/")
    public String login(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                        @RequestParam(value = "rememberme" ,defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        //把可能出现的异常try起来
        try {
            HashMap<String,String> map=userService.login(username, password);
            if (map.containsKey("ticket")){
                //把的cookie写入到response之中
                Cookie cookie=new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return  "login";
            }
        }catch (Exception e){
            logger.error("登陆异常： "+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = "/reglogin")
    public String reglogin(Model model){
        return "login";
    }

}