package com.zju;

import com.zju.dao.UserDao;
import com.zju.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)

@Sql("/init-table.sql")
public class InitDataTests {
	//引入到本类的成员变量
	@Autowired
	UserDao userDao;

	@Test
	public void initData(){
		Random random=new Random();

		for (int i=0;i<10;++i){
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER--%d",i));
			user.setPassword(String.format("PASSWORD--%d",i*i));
			user.setSalt(String.format("SALT %d",i*5));
			userDao.addUser(user);
		}


	}

}
