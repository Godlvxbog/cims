package com.zju.dao;

import com.zju.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2016/10/2.
 */

@Mapper
public interface QuestionDao {
    String TABLE_NAME =" question ";
    String INSERT_FIELDS=" title,content,user_id,create_date,comment_count ";
    String SELECT_FIELDS =" id, "+INSERT_FIELDS;

    //使用mapper写法
    @Insert({"insert into " +TABLE_NAME+" ( " +INSERT_FIELDS +" ) "+
            "values( #{title} , #{content} , #{userId} , #{createDate}, #{commentCount} )"})
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    @Select({"select "+ SELECT_FIELDS+" from "+TABLE_NAME +" where id= #{id}"})
    Question SelectById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    //获取总的数目
    //某个用户的的评论数
    @Select({"select count(id) from ", TABLE_NAME})
    int getQuestionCount();
}
