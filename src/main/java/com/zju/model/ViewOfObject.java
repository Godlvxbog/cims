package com.zju.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/4.
 * 这里面用到的是父类容器可以接受子类的多态，子类也可以是一个容器
 */
public class ViewOfObject {

    HashMap<String,Object> vos=new HashMap<>();

    public void set(String key,Object object){
        vos.put(key,object);
    }

    public Object get(String key){
       return  vos.get(key);
    }
}
