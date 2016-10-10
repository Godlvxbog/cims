package com.zju.service;

import com.zju.dao.QuestionDao;
import com.zju.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public int addQuestion(Question question){
        //需要做一个敏感词顾虑
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        int num= questionDao.addQuestion(question);
        if (num>0){
            return  question.getId();
        }else {
            return 0;
        }
    }

    public List<Question> getLatestQuestion(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }

    //获取question
    public Question selectById(int id){
        return  questionDao.SelectById(id);
    }

    public int updateCommentCount(int id, int count) {
        return questionDao.updateCommentCount(id, count);
    }



}
