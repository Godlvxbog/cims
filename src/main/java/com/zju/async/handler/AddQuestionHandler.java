package com.zju.async.handler;

import com.zju.async.EventHandler;
import com.zju.async.EventModel;
import com.zju.async.EventType;
import com.zju.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);


    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(),
                    model.getExts("title"),model.getExts("content"));

        }catch (Exception e){
            logger.error("添加问题索引失败"+e.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
