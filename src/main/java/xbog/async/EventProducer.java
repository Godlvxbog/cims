package xbog.async;

import com.alibaba.fastjson.JSONObject;
import xbog.util.JedisAdapter;
import xbog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 参考生产者，消费者模式：这里数据结构是l，《Stack》lpush
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;//可以保存队列的信息

    public boolean fireEvent(EventModel eventModel){
        //也就是把传入的EventModel保存到队列里面
        try{
            //通过json序列化
            String json= JSONObject.toJSONString(eventModel);//序列化
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;

        }catch (Exception e){
            return false;
        }
    }
}
