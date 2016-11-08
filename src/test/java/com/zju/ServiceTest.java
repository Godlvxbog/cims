package com.zju;

import com.zju.dao.FeedDao;
import com.zju.dao.UserDao;
import com.zju.model.EntityType;
import com.zju.model.Feed;
import com.zju.model.User;
import com.zju.service.FollowService;
import com.zju.service.UserService;
import com.zju.util.JedisAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)

@Sql("/init_table.sql")
public class ServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;

	@Autowired
	JedisAdapter jedisAdapter;
	@Autowired
	FeedDao feedDao;

	@Test
	public void addUser(){
		HashMap<String,String> msg= userService.register("wubo","svwuodshj20138");
		for (String key :msg.keySet()){
			System.out.println(String.format("%s====%s",key,msg.get(key)));
		}
	}


	@Test
	public void addFeed(){
		Feed feed=new Feed();
		feed.setCreatedDate(new Date());
		feed.setData("nihao");
		feed.setType(EntityType.ENTITY_COMMENT );
		feedDao.addFeed(feed);
	}


	///初始化一些的关注数据
	@Autowired
	FollowService followService;

	@Test
	public void contextLoads() {
		Random random = new Random();
		jedisAdapter.getJedis().flushDB();
		for (int i = 0; i < 11; ++i) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i + 1));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);

			for (int j = 1; j < i; ++j) {
				followService.follow(j, EntityType.ENTITY_USER, i);
			}


		}
	}



}
