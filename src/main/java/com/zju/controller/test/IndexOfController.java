package com.zju.controller.test;

import com.zju.model.test.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/30.
 */

@Controller
public class IndexOfController {

    @RequestMapping(value={"/index1","/"},method = RequestMethod.GET)
    @ResponseBody
    public String index(HttpSession session){
        System.out.println("你访问了一次");

        return "Hello,xbog" +session.getAttribute("mss");
    }

    @RequestMapping(value={"/profile/{group}/{userId}"},method = {RequestMethod.GET})
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("group") String group,
                          @RequestParam(value = "key",defaultValue = "zzzzz") String key,
                          @RequestParam(value = "type",required = false) int type){

        System.out.println(String.format("您的状态是%s。 \n第%d页.key=%s,type=%d",
                group,userId,key,type));
        return "index";
    }

    @RequestMapping(value={"/xingming"})
    @ResponseBody
    public String profile(@RequestParam String inputStr, HttpServletRequest request,
                          HttpServletResponse response,
                          Cookie cookie){
        StringBuffer sb=new StringBuffer();

        cookie.setMaxAge(200);

        sb.append(request.getRequestURL());
        sb.append(request.getCookies());
        sb.append(request.getQueryString());
        sb.append(request.getSession());
        sb.append(String.valueOf(cookie.getMaxAge()));

        response.setHeader("头文件","zheshasda");
        response.addCookie(new Cookie("cokkkkkkk","This is a cookie"));


        System.out.println(sb.toString().toCharArray());
        return String.format("inputStr=%s,inputInt=%s",inputStr,request.getParameter("inputInt"));
    }



    @RequestMapping(value={"/testmodel"})
    public String profile(Model model) {

        ArrayList list=new ArrayList();
        list.add("红色");
        list.add("黄色");
        list.add("白色");

        HashMap<String,String> colors=new HashMap<>();
        colors.put("一","huang");
        colors.put("二","henong");

        HashMap<String,String> nums=new HashMap<>();
        for (int i=0;i<10;i++){
            nums.put(String.valueOf(i),String.valueOf(i*i));
        }


        //测试对象类
        User user=new User("liyang",100);

        model.addAttribute("user",user);
        model.addAttribute("nums",nums);
        model.addAttribute("colors",colors);
        model.addAttribute("Id","20016232");
        model.addAttribute("list",list);

        return "testmodel";
    }


}
