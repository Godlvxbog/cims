package xbog.dao;

import xbog.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2016/10/2.
 */

@Mapper
public interface LoginTicketDao {
    String TABLE_NAME =" login_ticket ";
    String INSERT_FIELDS=" user_id,status,ticket,expired ";
    String SELECT_FIELDS =" id, "+INSERT_FIELDS;

    //使用mapper写法
    @Insert({"insert into " +TABLE_NAME+" ( " +INSERT_FIELDS +" ) "+
            "values( #{userId} , #{status} , #{ticket} , #{expired} )"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select "+ SELECT_FIELDS+" from "+TABLE_NAME +" where ticket= #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    //登出
    @Update("update " + TABLE_NAME+" set status=#{status} where ticket=#{ticket}")
    void updateStatus(@Param("ticket") String ticket,
                      @Param("status") int status);



}
