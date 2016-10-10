package com.zju;

import com.zju.dao.CommentDao;
import com.zju.model.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)


public class DaoTests {
	//引入到本类的成员变量
	@Autowired
	CommentDao commentDao;

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





}
