package xbog.async;

import com.alibaba.fastjson.JSON;
import xbog.util.JedisAdapter;
import xbog.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费者：参考多线程中典型问题，生产者消费者模式
 * 队列,把event分发到各个eventHandler去处理，
 * 所以需要把event与各个Handler关系建立起来 Map<EventType,List<EventHandler>
 * 包装成Service并且，初始化这个映射
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);
    //建立eventType-->List<EventHandler>之间的映射
    private Map<EventType,List<EventHandler>> config=new HashMap<>();
    private ApplicationContext applicationContext;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //在容器中找到所有的EventHandler
        Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
        //从applicationContext工厂容器中得到EventHandler，并构造方便消息队列遍历的event--list<handle>的映射结构
        if (beans!=null){
            for (Map.Entry<String,EventHandler> enytry:beans.entrySet()){
                List<EventType> eventTypes=enytry.getValue().getSupportEventType();
                for (EventType type:eventTypes){
                    if (!config.containsKey(type)){
                        //初始化数据，把当前关心的事件1，以及需要处理的方法放入
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    //把当前事件方法，添加到前面初始好的List<Handler>
                    //其中handler由config中的enytry.getValue()来填充
                    config.get(type).add(enytry.getValue());
                }
            }
        }
        //创建线程,run()遍历每个event，并执行映射的handler中方法；
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key= RedisKeyUtil.getEventQueueKey();
                    //取出最后一个元素，若没有元素就一直卡着
                    List<String> events=jedisAdapter.brpop(0,key);
                    for (String message:events){
                        if (message.equals(key)){
                            continue;//实现过滤key
                        }
                        //反序列化，从json中解析出eventmodel
                        EventModel eventModel= JSON.parseObject(message,EventModel.class);
                        //应该找各种handler来处理
                        if (!config.containsKey(eventModel.getType())){
                            logger.error("这是不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler:config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
