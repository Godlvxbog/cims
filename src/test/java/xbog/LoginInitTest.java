package xbog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CimsApplication.class)

@Sql("/login_table.sql")
public class LoginInitTest {
	@Test
	public void testLoginInitData(){
		System.out.println("Hello");
	}


}
