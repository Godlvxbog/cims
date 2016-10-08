package com.zju.service;

import com.zju.dao.QuestionDao;
import com.zju.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public List<Question> getLatestQuestion(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }
}
