package com.zju.dao;

import com.zju.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2016/10/2.
 */

@Mapper
public interface MessageDao {
    String TABLE_NAME =" message ";
    String INSERT_FIELDS=" from_id,to_id,conversation_id,content,created_date,has_read ";
    String SELECT_FIELDS =" id, "+INSERT_FIELDS;

    //使用mapper写法
    @Insert({"insert into " +TABLE_NAME+" ( " +INSERT_FIELDS +" ) "+
            "values( #{fromId} , #{toId} , #{conversationId} , #{content}, #{createdDate} , #{hasRead})"})
    int addMeassage(Message meassage);


    @Select({"select "+ SELECT_FIELDS+" from "+TABLE_NAME +" where conversation_id= #{conversationId} order by created_date asc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);


//    select *,count(id) as cnt from
//    (select * from message order by created_date desc) tt
//    group by conversation_id order by created_date desc
//    limit 0,3;
//    这里的as cnt 需要一个变量来保存，所以用id来保存
    @Select({"select "+ INSERT_FIELDS +",count(id) as id from " +
            " (select * from "+TABLE_NAME
            +" where from_id=#{userId} or to_id=#{userId} "
            + " order by created_date desc) tt "
            +" group by conversation_id order by created_date desc "+
            " limit #{offset},#{limit} "})
    List<Message> getConversationList(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);





}
