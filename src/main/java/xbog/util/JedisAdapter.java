package xbog.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xbog.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/16.
 */
@Service//包装成一个服务,并且实现初始化implements InitializingBean属性pool
public class JedisAdapter implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;
    public static void print(int index,Object obj){
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }

    public static void main(String[] args){
        Jedis jedis=new Jedis("redis://localhost:6379/1");//默认会连接你的6379端口
        jedis.flushDB();//删除这个数据库的数据
        //get set操作
        jedis.set("name","Hello world");
        print(1,jedis.get("name"));
        jedis.rename("name","new name");
        print(2,jedis.get("new name"));
        jedis.setex("hello2",15,"world");

        //阅读数； 1从数据库，redis，秒杀活动，等等都是放在内存中去的
        jedis.set("pv","100");
        jedis.incr("pv");//加1；
        jedis.incrBy("pv",5);
        print(3,jedis.get("pv"));
        jedis.decrBy("pv",3);
        print(4,jedis.get("pv"));

        print(5,jedis.keys("p*"));//获取所有的keys

        //使用list进行存取数据
        String listname="list";
        jedis.del(listname);
        for (int i=0;i<10;i++){
            jedis.lpush(listname,"a"+String.valueOf(i));
        }
        //取出数据,类似于一个栈的情况
        print(6,jedis.lrange(listname,3,12));
        print(7,jedis.llen(listname));
        print(8,jedis.lpop(listname));
        print(9,jedis.llen(listname));
        print(10,jedis.lindex(listname,0));
        print(11,jedis.linsert(listname, BinaryClient.LIST_POSITION.AFTER,"a4","nnnnnn"));
        print(12,jedis.linsert(listname, BinaryClient.LIST_POSITION.BEFORE,"a4","mmmmmm"));
        print(13,jedis.lrange(listname,0,12));

        //HASH
        String userKey="userXX";
        //随意定义一个表的属性
        jedis.hset(userKey,"name","javaers");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","12891221");
        print(14,jedis.hget(userKey,"name"));
        print(15,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(16,jedis.hgetAll(userKey));
        jedis.hset(userKey,"email","80210534@qq.com");
        print(17,jedis.hexists(userKey,"email"));
        print(18,jedis.hkeys(userKey));
        print(19,jedis.hvals(userKey));//其实是一种遍历
        jedis.hsetnx(userKey,"school","zju");
        jedis.hsetnx(userKey,"name","xioaming");//如果不存在才设置，
        print(20,jedis.hgetAll(userKey));

        //下面演示集合；

        String likekeys1="like1";//点赞
        String likekeys2="like2";//点赞
        for (int i=0;i<12;i++){
            jedis.sadd(likekeys1,String.valueOf(i));
            jedis.sadd(likekeys2,String.valueOf(i*i));
        }

        print(21,jedis.smembers(likekeys1));
        print(22,jedis.smembers(likekeys2));
        print(23,jedis.sunion(likekeys1,likekeys2));
        print(24,jedis.sdiff(likekeys1,likekeys2));//我有，你没有的
        print(25,jedis.sinter(likekeys1,likekeys2));
        print(26,jedis.sismember(likekeys1,"2"));
       jedis.srem(likekeys1,"5");
        print(27,jedis.smembers(likekeys1));
        jedis.smove(likekeys2,likekeys1,"25");//从likekeys2中25把元素移动到likekeys1
        print(28,jedis.smembers(likekeys1));
        print(29,jedis.smembers(likekeys2));
        print(30,jedis.scard(likekeys1));


        //演示优先队列sorts set
        String rankKey ="rankKey";
        jedis.zadd(rankKey,15,"jim");
        jedis.zadd(rankKey,60,"Ben");
        jedis.zadd(rankKey,86,"Lee");
        jedis.zadd(rankKey,90,"Huang");
        jedis.zadd(rankKey,99,"Hans");
        print(31,jedis.zcard(rankKey));
        print(32,jedis.zcount(rankKey,60,99));
        print(33,jedis.zscore(rankKey,"Huang"));//某个key的分数是多少
        jedis.zincrby(rankKey,3,"Le");
        print(34,jedis.zscore(rankKey,"Lee"));//某个key的分数是多少
        print(35,jedis.zscore(rankKey,"Le"));//某个key的分数是多少
        print(36,jedis.zrange(rankKey,0,100));

        //排行相关的功能；这里默认是以升ming来排序的
        print(37,jedis.zrange(rankKey,1,3));//按照人数取
        print(38,jedis.zrevrange(rankKey,0,2));

        //下面遍历:根据分数来挑选，//按照分数取
        for (Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,60,100)){
            print(40,tuple.getElement()+":" +String.valueOf(tuple.getScore()));
        }
        print(41,jedis.zrank(rankKey,"Ben"));//他的排名；
        print(41,jedis.zrevrank(rankKey,"Ben"));

        //下面根据字母排序
        String setKey="zset";//当所有人的分值都是一样的
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"e");
        jedis.zadd(setKey,1,"f");
        print(44,jedis.zlexcount(setKey,"-","+"));//从负无穷到正无穷
        print(45,jedis.zlexcount(setKey,"(b","[e"));
        print(46,jedis.zrange(setKey,0,12));
        jedis.zremrangeByLex(setKey,"(c","+");
        print(47,jedis.zrange(setKey,0,2));

        //连接池




//        JedisPool pool = new JedisPool();
//        for (int i = 0; i < 100; ++i) {
//            Jedis j = pool.getResource();
//            print(45, j.get("pv2"));
//            j.close();
//        }

        User user=new User();
        user.setName("张三");
        user.setPassword("123456");
        user.setSalt("hdhdad");
        user.setHeadUrl("dsdsd");
        user.setId(1);

        print(50,JSONObject.toJSONString(user));
        jedis.set("user1", JSONObject.toJSONString(user));

        String value1=jedis.get("user1");
        User user2 = JSON.parseObject(value1,User.class);
        System.out.println(user2.getName()+user2.getPassword());



















    }

    //初始化pool，相当有了一个集合中的数据
    @Override
    public void afterPropertiesSet() throws Exception {
        pool=new JedisPool("redis://localhost:6379/10");
    }

    //增加一个连接池的添加；在这这个连接池中增加一个数据
    public long sadd(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }


    public long sremove(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    //求这个连接池集合中的数量
    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }


    //判断是不是在里面
    public boolean sismember(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    //获取一个jedis
    public Jedis getJedis(){
        return pool.getResource();
    }

    //事务的开始
    public Transaction multi(Jedis jedis){
        try{
            return jedis.multi();

        }catch (Exception e){
            logger.error("事务失败："+e.getMessage());
        }
        return null ;
    }

    //执行的执行
    public List<Object> exec(Transaction tx,Jedis jedis){
        try{
            return tx.exec();

        }catch (Exception e){
            logger.error("事务执行失败："+e.getMessage());
        }finally {
            if (tx!=null){
                try {
                    tx.close();
                } catch (IOException ioe) {
                    logger.error("事务关闭失败："+ioe.getMessage());
                }

            }

            if (jedis!=null){
                jedis.close();
            }
        }
        return null ;
    }

    //添加用户到zsort中
    public long zadd(String key,double score,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key,score, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //获取列表中的数据
    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key,start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    ///获取有多少数据
    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    ///获取有多少数据
    public double zscore(String key,String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key,member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }



}
