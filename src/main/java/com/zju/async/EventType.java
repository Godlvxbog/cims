package com.zju.async;

/**
 * Created by Administrator on 2016/10/17.
 */
public enum EventType {
    //枚举型
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5);
    private int value;
    EventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }

}
