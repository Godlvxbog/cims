package com.zju.model.test;

/**
 * Created by Administrator on 2016/9/30.
 */
public class User {
    private String name;
    private Integer age;

    public String sayHello(){
        return String.format("Hello, my name is %s ,i am %s 说说你爱我吗",name,age);
    }

    @Override
    public String toString() {
        return String.format("Hello, my name is %s ,i am %s",name,age);
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
