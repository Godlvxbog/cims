package com.zju.service.test;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/10/2.
 */

@Service
public class TestService {
    public String getMessage(int userId){
        return "Hello Message"+userId;
    }
}
