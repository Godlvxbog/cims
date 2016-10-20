package com.zju.service;

import com.zju.util.JedisAdapter;
import com.zju.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/10/17.
 */
@Service//包装一个点赞与点踩的类
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    //应该有一个参数表示当前有多少人喜欢的方法
    public long getLikeCount(int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityType, entityId);

        return jedisAdapter.scard(likeKey);//当前key有多少人喜欢
    }




    //用户userId,对某个评论或者回答进行了点赞
    public long like(int userId,int entityType,int entityId){
        //在喜欢中添加这个人
        String likeKey= RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        //从不喜欢中删除这个这个人
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sremove(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    //踩：不喜欢中添加userId，喜欢中删除userId;
    public long disLike(int userId,int entityType,int entityId){

        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        String likeKey= RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sremove(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    //用户的喜欢对于这个赞与踩的状态是怎么样的
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        if (jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(disLikeKey,String.valueOf(userId))){
            return -1;
        }else{
            return 0;
        }
    }



}
