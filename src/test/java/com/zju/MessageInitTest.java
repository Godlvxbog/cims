package com.zju;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)

@Sql("/message_table.sql")
public class MessageInitTest {


	@Test
	public void testMessageInitData(){
		System.out.println("Hello");
	}


}
