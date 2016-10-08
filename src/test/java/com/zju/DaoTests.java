package com.zju;

import com.zju.dao.QuestionDao;
import com.zju.dao.UserDao;
import com.zju.model.Question;
import com.zju.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)

@Sql("/init_table.sql")
public class DaoTests {
	//引入到本类的成员变量
	@Autowired
	UserDao userDao;

	@Autowired
	QuestionDao questionDao;
//
	@Test
	public void initData(){
		Random random=new Random();

		for (int i=0;i<10;++i){
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER--%d",i));
			user.setPassword(String.format("PASSWORD--%d",i*i));
			user.setSalt(String.format("SALT %d HelloWorld",i*5+20));
			userDao.addUser(user);

			//测试修改
			user.setPassword(String.format("XXXX"));
			userDao.updatePassword(user);

			//测试QuestionDao
			Question question=new Question();
			question.setTitle(String.format("谈论你幸福吗===%d",i));
			question.setUserId(i+1);
			question.setCommentCount(i*5);

			Date date =new Date();
			date.setTime(date.getTime()+1000*3600*24*i);
			question.setCreateDate(date);
			question.setContent(String.format("XXXXXXXXX--%d",i*i+3));

			questionDao.addQuestion(question);




		}
		//测试取出一个用户
		Assert.assertEquals("XXXX",userDao.selectById(2).getPassword());
//		userDao.deleteById(2);
//		Assert.assertNull(userDao.selectById(2));

		System.out.println(questionDao.selectLatestQuestions(0,2,6));


	}

}
