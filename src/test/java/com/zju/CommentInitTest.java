package com.zju;

import com.zju.dao.CommentDao;
import com.zju.model.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)

@Sql("/comment_table.sql")
public class CommentInitTest {

	@Autowired
	CommentDao commentDao;


	@Test
	public void testCommentInitData(){
		for (int i=0;i<10;i++){
			Comment comment=new Comment();
			comment.setContent("hello--test--content"+i*3+2);
			comment.setCreatedDate(new Date());
			comment.setEntityId(0);

			commentDao.addComment(comment);
		}
	}





	@Test
	public void testLoginInitData(){
		System.out.println("Hello");
	}


}
