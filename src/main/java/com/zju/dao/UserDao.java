package com.zju.dao;

import com.zju.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2016/10/2.
 */

@Mapper
public interface UserDao {
    String TABLE_NAME =" user ";
    String INSERT_FIELDS=" name,password,salt,head_url ";
    String SELECT_FIELDS =" id, "+INSERT_FIELDS;

    //使用mapper写法
    @Insert({"insert into " +TABLE_NAME+" ( " +INSERT_FIELDS +" ) "+
            "values( #{name} , #{password} , #{salt} , #{headUrl} )"})
    int addUser(User user);

    @Select({"select "+ SELECT_FIELDS+" from "+TABLE_NAME +" where id= {#id}"})
    User selectById(int id);

}
