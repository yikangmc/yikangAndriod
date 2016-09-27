package com.yikang.app.yikangserver.bean;

/**
 * Created by Administrator on 2016/6/15.
 */
public class Message {
    private String title;
    private String content;
    private long messagesId;

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    private Long dataId;

    public int getContentGroup() {
        return contentGroup;
    }

    public void setContentGroup(int contentGroup) {
        this.contentGroup = contentGroup;
    }

    private Long createTime;
    private int contentGroup;

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    private int isRead;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMessagesId() {
        return messagesId;
    }

    public void setMessagesId(Long messagesId) {
        this.messagesId = messagesId;
    }


}
