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
}