package xbog.async;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 * 专门负责处理事务的一个接口
 */
public interface EventHandler {
    void doHandle(EventModel eventModel);
    List<EventType> getSupportEventType();//比如这里关注的是点赞的，其实就是一个selectBy
    //上面告知这次额Event可以来调doHandler;
}
