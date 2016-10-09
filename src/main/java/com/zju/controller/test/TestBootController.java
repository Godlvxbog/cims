package com.zju.controller.test;

import com.zju.model.test.User;
import com.zju.service.test.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/1.
 * //这是一个测试Controller中涉及到方法的测试类
 */

@Controller
public class TestBootController {
    private static final Logger logger= LoggerFactory.getLogger(TestBootController.class);

    //这里的@RequestMapping由六个参数可以掺入1 value 2method;
    @RequestMapping(value = {"/test","/t"},method = {RequestMethod.GET})
    @ResponseBody
    public String testIndex(){
        logger.info("访问了当前主页，LOGGER记录了");

        return "HELLO,this is a test ";
    }

    @RequestMapping(value = "/file/{group}/{id}")
    public String testParam(@PathVariable("group") String group,
                            @PathVariable("id") Integer id,
                            Model model,
                            @RequestParam(value = "type",defaultValue = "admin",required = false) String type){
        StringBuffer sb=new StringBuffer();
        sb.append(String.format("您现在是：%s,您的id号码是 %d",group ,id));
        sb.append(String.format("您传入的参数type=%s",type));
        model.addAttribute("sb",sb);
        return "testParm";
   }

    @RequestMapping(value = "/testmodel1")
    public String testParam(Model model){
        //List
        ArrayList list=new ArrayList();
        for (int i=0;i<4;i++){
            list.add(i,String.format("list %d",i));
        }

        //放入Map 以及类

        HashMap<String,User> stus=new HashMap<>();
        stus.put("wubo",new User("wubo",23));
        stus.put("liyang",new User("liyang",34));
        stus.put("fenkai",new User("fenkai",33));

        //测试Request的内容


        //添加到model
        model.addAttribute("list",list);
        model.addAttribute("stus",stus);
        return "testParm";
    }

    @RequestMapping(value = "/testreq")
    public String testParam(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(value = "type",defaultValue = "2") int type
                            ){
        StringBuffer sb=new StringBuffer();
        request.setAttribute("setAttribbute","这是一个setAttribbute");
        System.out.println("cookie"+request.getCookies());
        System.out.println(request.getAttribute("setAttribbute"));
        sb.append(request.getContextPath());
        System.out.println("requestion.contextpath+===="+request.getContextPath());


        //测试Response
        response.addCookie(new Cookie("helloCookie","This is a cookie"));
        response.setHeader("myHeader","HEader");
        Collection<String> list= response.getHeaders("myHeader");

        //通过参数来控制跳转不同的页面
        try {
            if (type>0)
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "index1";
    }

    @RequestMapping(value={"/testform"})
    @ResponseBody
    public String profile(@RequestParam String inputStr, HttpServletRequest request,
                          HttpServletResponse response
                          ){
        StringBuffer sb=new StringBuffer();



        sb.append(request.getRequestURL());
        sb.append(request.getCookies());
        sb.append(request.getQueryString());
        sb.append(request.getSession());


        response.setHeader("头文件","zheshasda");
        response.addCookie(new Cookie("cokkkkkkk","This is a cookie"));


        System.out.println(sb.toString().toCharArray());
        return String.format("inputStr=%s,inputInt=%s",inputStr,request.getParameter("inputInt"));
    }

    @RequestMapping(value = "/request")
    @ResponseBody
    public String testrequest(Model model,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session,
                              @CookieValue("JSESSIONID") String Jsession){
        StringBuffer sb=new StringBuffer();

        sb.append("SESSIONID:" +Jsession+"</br>" );
        sb.append(request.getMethod()+"</br>");
        sb.append(request.getSession()+"</br>");
        sb.append(request.getCookies()+"</br>");
        sb.append(request.getQueryString()+"</br>");
        sb.append(request.getPathInfo()+"</br>");
        sb.append(request.getRequestURI()+"</br>");
        sb.append(request.getContextPath()+"</br>");

        session.setAttribute("session","this is a session");
        sb.append(session.getAttribute("session"));

        Enumeration<String> headers=request.getHeaderNames();
        while(headers.hasMoreElements()){
            String name=headers.nextElement();
            sb.append(name +" :-----------" + request.getHeader(name)+"<br>");
        }

        if (request.getCookies()!=null){
            for (Cookie cookie: request.getCookies()){
                sb.append(cookie.getName()+"==="+cookie.getValue()+"</br>");
            }
        }
        //添加response的header
        response.setHeader("myHeaderID","I am handsome boy");
        //添加一个cookie
        response.addCookie(new Cookie("myCookie1","Heelo,this is my new Cookie"));
        return sb.toString();
    }

    @RequestMapping(value = "/redirect/{code}")
    public RedirectView testredirect(Model model,
                                     @PathVariable(value = "code") int code,
                                     HttpServletResponse response,
                                     HttpSession session){
        //跳转之前先给session放点东西
        session.setAttribute("mss","jump from the redirect");
        RedirectView red=new RedirectView("/",true);
        if (code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;

    }

    @RequestMapping(path = "/admin")
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if ("admin".equals(key)){
            return "hello admin";
        }else{
            throw new IllegalArgumentException("参数不对");
        }
    }

    //处理异常
    @ExceptionHandler()
    @ResponseBody
    public String except(Exception e){
        return "ERROR: "+e.getMessage();
    }

    //演示Ioc：
    //正常情况下应该这么写
    TestService testService=new TestService();

    //Ioc
    @Autowired
    TestService testService1;

    @RequestMapping("/testIoc")
    @ResponseBody
    public String testIoc(){
        return "This is a testService"+(testService1.getMessage(4));
    }








}
