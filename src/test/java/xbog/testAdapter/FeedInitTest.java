package xbog.testAdapter;

import xbog.CimsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)//
//spring环境的默认配置
@SpringApplicationConfiguration(classes = CimsApplication.class)

@Sql("/feed_table.sql")
public class FeedInitTest {


	@Test
	public void testFeedInitData(){
		System.out.println("Hello");
	}


}
