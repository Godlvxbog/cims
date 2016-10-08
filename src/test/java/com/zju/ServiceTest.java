package com.zju;

import com.zju.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)


public class ServiceTest {
	@Autowired
	UserService userService;

	@Test
	public void addUser(){
		HashMap<String,String> msg= userService.register("wubo","svwuodshj20138");
		for (String key :msg.keySet()){
			System.out.println(String.format("%s====%s",key,msg.get(key)));
		}
	}
}
