package com.zju.util;

/**
 * 生成Redis的key的帮助类，所有的key都从这里生成就可以保证key不会重复了
 * 不同的业务有不同的前缀
 */
public class RedisKeyUtil {
    private static String SPLIT="-";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENTQUEUE="EVENT_QUEUE";


    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE+SPLIT +String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }
    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE+SPLIT +String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

}
