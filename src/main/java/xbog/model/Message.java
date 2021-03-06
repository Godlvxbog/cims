package xbog.model;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/10.
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private Date createdDate;
    private int hasRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFormId() {
        return fromId;
    }

    public void setFormId(int formId) {
        this.fromId = formId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getConversationId() {
        //保证两个A-->B 或者B-->A中的对话id是一致的
        if (fromId < toId){
            return String.format("%d_%d",fromId,toId);
        }else{
            return String.format("%d_%d",toId,fromId);
        }
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }
}
