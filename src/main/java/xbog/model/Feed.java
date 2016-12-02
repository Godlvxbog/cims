package xbog.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/23.
 * 新鲜事
 */
public class Feed {
    private int id;
    private int type;//类型不同，渲染不同
    private int userId;
    private Date createdDate;
    private String data;//是json格式
    //辅助变量
    private JSONObject dataJSON=null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON=JSONObject.parseObject(data);
    }

    //类似于一个ViewObject,方便于模板进行调用vo.userName vo.questionId等等
    public String get(String key){
        return dataJSON==null?null:dataJSON.getString(key);
    }
}
