package com.zju;

import com.zju.dao.CommentDao;
import com.zju.dao.MessageDao;
import com.zju.model.Comment;
import com.zju.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)


public class DaoTests {
	//引入到本类的成员变量
	@Autowired
	CommentDao commentDao;
	@Autowired
	MessageDao messageDao;

	@Test
	public void testCommentSelect(){
		List<Comment> clist= commentDao.selectByEntity(0,0);
		for (Comment comment:clist){
			System.out.println(comment.getContent()+" : "+comment.getCreatedDate());
		}
	}

	@Test
	public void testCount(){
		int cnt= commentDao.getCommentCount(0,0);
		System.out.println("==========现在的评论条数有：\n"+cnt);
	}

	@Test
	public void testMessage(){
		for (int i=0;i<10;i++){
			Message message = new Message();
			message.setContent("Hello" + i * i + "brrere\n");
			message.setCreatedDate(new Date());
			message.setFormId(2 * i + 1);
			message.setToId((i + 1) * 2);

			messageDao.addMeassage(message);
		}

	}


	@Test
	public void testGetConversation(){

		List<Message> mlist = messageDao.getConversationDetail("1_2",1,5);
		for (Message message : mlist){
			System.out.println(message.getContent()+"====="+message.getCreatedDate());

		}
	}

	@Test
	public void testGetConversationList(){
		List<Message> msglist= messageDao.getConversationList(24,0,10);
		for (Message mess:msglist){
			System.out.println("========"+mess.getContent()+"\n");
		}
	}











}
