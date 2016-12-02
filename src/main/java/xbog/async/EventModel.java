package xbog.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/17.
 */
public class EventModel {
    private EventType type;//事件类型：点赞
    private int actorId;//触发者，谁点赞
    private int entityType;//给那个东西点赞
    private int entityId;
    private int entityOwnerId;//给谁点赞
    //以上是公用的属性，方便使用所以用单独的字段储存
    //保留发生的各种字段,定义一个扩展字段，类似于一个ViewOfObject
//    比如这里的评论点赞，那么应该保存，你对那个问题的的评论进行点赞
    private Map<String,String> exts=new HashMap<>();

    public EventModel(){

    }



    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }



    public EventModel (EventType type){
        this.type=type;
    }

    //自己设置直接传入字段，和值就可以把这条是数据添加到EventModel中
    public EventModel setExts(String key,String value){
        exts.put(key,value);
        return this;
    }
    public String getExts(String key){
        return exts.get(key);
    }

//==========================
    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;//就可以实现xx.setXX().setXX().setXX()实现链状队列
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }
}
