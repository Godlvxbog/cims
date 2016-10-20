package com.zju.service;

import com.zju.util.JedisAdapter;
import com.zju.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/20.
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;//只有引入jedis才可以操作数据

    //关注功能：关注一个人需要做两件事情：1follower列表中添加我成为粉丝，，  2我的列表中添加我关注的对象
    public boolean follow(int userId,int entityType,int entityId){
        String followerKey= RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date=new Date();
        //下面是开启事务
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        //对zset进行添加数据
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long)ret.get(1)>0;

    }

    //取消关注一个人
    public boolean unfollow(int userId,int entityType,int entityId){
        String followerKey= RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        //下面是开启事务
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        //对zset进行添加数据
        tx.zrem(followerKey,String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long)ret.get(1)>0;

    }

    public List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids=new ArrayList<>();
        for (String str:idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    //获取所有的关注着
    //由于zrange中返回的是set，需要转换成list类型的函数，所以需要一个helper函数
    public List<Integer> getFollowers(int entityType ,int entityId,int count){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        Set<String> idset= jedisAdapter.zrevrange(followerKey,0,count);
        return getIdsFromSet(idset);

    }
    //带有offset
    public List<Integer> getFollowers(int entityType ,int entityId,int offset,int count){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        Set<String> idset= jedisAdapter.zrevrange(followerKey,offset,count);
        return getIdsFromSet(idset);

    }

    //下面是获取我的关注对象
    public List<Integer> getFollowees(int userId, int entityType ,int count){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Set<String> idset= jedisAdapter.zrevrange(followeeKey,0,count);
        return getIdsFromSet(idset);

    }
    //带有offset
    public List<Integer> getFollowees(int userId, int entityType ,int offset,int count){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Set<String> idset= jedisAdapter.zrevrange(followeeKey,offset,count);
        return getIdsFromSet(idset);

    }

    //判断我有多少个粉丝，主动者是实体
    public long getFollowerCount(int entityType ,int entityId){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);

    }
    public long getFolloweeCount(int userId ,int entityType){
        String followerKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisAdapter.zcard(followerKey);

    }

    //判断是不是这个粉丝，也就是判断该用户是不是某个实体结合中的粉丝
    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId))!=0;
    }



}
