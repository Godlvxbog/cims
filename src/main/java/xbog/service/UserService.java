package xbog.service;

import xbog.dao.LoginTicketDao;
import xbog.dao.UserDao;
import xbog.model.LoginTicket;
import xbog.model.User;
import xbog.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2016/10/4.
 */
@Service
public class UserService {

    //引入UserDao
    @Autowired
    UserDao userDao;

    //引入LoginTicketDao
    @Autowired
    LoginTicketDao loginTicketDao;

    //获取user数据
    public User getUser(int uid){
        return  userDao.selectById(uid);
    }

    //通过名字获取user
    public User selectByName(String name){
        return userDao.selectByName(name);
    }



    //业务逻辑：注册。返回的是注册相关的信息，用Map集合来
    public HashMap<String,String>  register(String username,String password){
        HashMap<String,String> msgMap=new HashMap<>();
        if (StringUtils.isBlank(username)){
            msgMap.put("msg","用户名不能为空");
            return msgMap;
        }
        if (StringUtils.isBlank(password)){
            msgMap.put("msg","密码不能为空");
            return msgMap;
        }
        //你说为什么要乱码
        //下面有一个业务，需要判断你当前注册的user在数据库中时候存在
//        所以需要把数据库中user提取出来 getUser(String username)
       User userDB=userDao.selectByName(username);
        if (userDB!=null){//这里表示user在数据库中已经存在了
            msgMap.put("msg","当前用户名已经存在了");
            return msgMap;
        }

        //上面是检测合法性，下面开始真正的注册，也就是开始加入user到一个数据库分钟
        User userReg=new User();
        userReg.setName(username);
        //随机生成一段盐
        userReg.setSalt(UUID.randomUUID().toString().substring(0,5));
        userReg.setPassword(WendaUtil.MD5(password+userReg.getSalt()));
        userReg.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));

        userDao.addUser(userReg);


        String ticket=addLoginTicket(userReg.getId());
        //需要下发到浏览器中
        msgMap.put("ticket",ticket);
        return msgMap;

    }



    //下面写登陆的service方法
    //业务逻辑：注册。返回的是注册相关的信息，用Map集合来
    public HashMap<String,Object>  login(String username,String password) {
        HashMap<String, Object> msgMap = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            msgMap.put("msg", "用户名不能为空");
            return msgMap;
        }
        if (StringUtils.isBlank(password)) {
            msgMap.put("msg", "密码不能为空");
            return msgMap;
        }
        //如果用户名不存在
        User userDB = userDao.selectByName(username);
        if (userDB == null) {//这里表示user在数据库中不出来
            msgMap.put("msg", "用户名不存在");
            return msgMap;
        }
        //验证提交的密码与数据库中密码是否可以对的上,下面表示已经对得上了
        if (!WendaUtil.MD5(password+userDB.getSalt()).equals(userDB.getPassword())){
            msgMap.put("msg", "密码错误");
            return msgMap;
        }

//        登陆验证
        String ticket2=addLoginTicket(userDB.getId());
        msgMap.put("ticket",ticket2);
        msgMap.put("userId",userDB.getId());

        //以下是更改ticket状态来设置登陆与登出
//        if (ticket!=null){
//            //需要下发到浏览器中
//            loginTicketDao.updateStatus(ticket,0);
//            LoginTicket loginTicket=loginTicketDao.selectByTicket(ticket);
//            String newticket=loginTicket.getTicket();
//            msgMap.put("ticket",newticket);
//        }


        return msgMap;
    }


    //增加LoginTicket()
    public String addLoginTicket(int userId){
        //典型的增加DAO
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);

        Date now=new Date();
        now.setTime(now.getTime()+1000*3600*24);
        loginTicket.setExpired(now);

        loginTicket.setStatus(0);
        //ticket
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));

        loginTicketDao.addTicket(loginTicket);

        //这个函数的目的是为了增加一个ticket
        return loginTicket.getTicket();

    }

    //添加一个logout方法
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }








    }
