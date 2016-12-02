package xbog.util;

/**
 * 生成Redis的key的帮助类，所有的key都从这里生成就可以保证key不会重复了
 * 不同的业务有不同的前缀
 */
public class RedisKeyUtil {
    private static String SPLIT="-";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENTQUEUE="EVENT_QUEUE";
    //关注的数据列表key：粉丝和关注对象
    private static String BIZ_FOLLLOWER="FOLLLOWER";
    private static String BIZ_FOLLLOWEE="FOLLLOWEE";
    private static String BIZ_TIMELINE="TIMELINE";

    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

    public static void setBizTimeline(String bizTimeline) {
        BIZ_TIMELINE = bizTimeline;
    }

    //    要知道每个key下面都是一个容器，用来装映射的，
    //每个实体所有的粉丝的表的key，保证所有实体所有的粉丝列表
    public static String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLLOWER+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }
    //每一个实体关注某一类的表：用户我关注的所有的问题，或者人
    public static String getFolloweeKey(int userId,int entityType){
        return BIZ_FOLLLOWEE+SPLIT+String.valueOf(userId)+SPLIT+String.valueOf(entityType);
    }





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
