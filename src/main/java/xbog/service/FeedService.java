package xbog.service;

import xbog.dao.FeedDao;
import xbog.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
@Service
public class FeedService {

    @Autowired
    FeedDao feedDao;

    //拉取模式
    public List<Feed> getUserFeeds(int maxId,List<Integer> userIds,int count){
        return feedDao.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed){
        feedDao.addFeed(feed);
        return feed.getId()>0;
    }

    //推模式
    public Feed getFeedById(int id){
        return feedDao.getFeedById(id);
    }


}
