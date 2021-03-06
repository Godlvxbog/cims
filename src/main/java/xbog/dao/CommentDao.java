package xbog.dao;

import xbog.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2016/10/2.
 */

@Mapper
public interface CommentDao {
    String TABLE_NAME =" comment ";
    String INSERT_FIELDS=" content,user_id,entity_type,entity_id,created_date,status ";
    String SELECT_FIELDS =" id, "+INSERT_FIELDS;

    //使用mapper写法
    @Insert({"insert into " +TABLE_NAME+" ( " +INSERT_FIELDS +" ) "+
            "values( #{content} , #{userId} , #{entityType} , #{entityId}, #{createdDate}, #{status} )"})
    int addComment(Comment comment);

    @Select({"select "+ SELECT_FIELDS+" from "+TABLE_NAME +" where entity_id= #{entityId} and entity_type= #{entityType} order by created_date desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Select({"select count(id) from "+TABLE_NAME +" where entity_id= #{entityId} and entity_type= #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Update("update comment set status=#{status} where id=#{id}")
    int updateStatus(@Param("id") int id,@Param("status") int status);

    //添加一个返回Comment的方法
    @Select({"select "+ SELECT_FIELDS+" from "+TABLE_NAME +" where id=#{id}"})
    Comment getCommentById(int id);

    //某个用户的的评论数
    @Select({"select count(id) from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);

}
