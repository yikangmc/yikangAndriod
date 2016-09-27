package com.yikang.app.yikangserver.bean;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ForumPostsAnswers {

    private int createUserPosition;


    public int getCreateUserPosition() {
        return createUserPosition;
    }

    public void setCreateUserPosition(int createUserPosition) {
        this.createUserPosition = createUserPosition;
    }

    public String getCreateUserDesignationName() {
        return createUserDesignationName;
    }

    public void setCreateUserDesignationName(String createUserDesignationName) {
        this.createUserDesignationName = createUserDesignationName;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    private String createUserDesignationName;
    private String content;
    private Long createTime;
    private Long createUserId;
    private String createUserName;
    private String createUserPhotoUrl;
    private Long formPostId;

    public Long getFormPostId() {
        return formPostId;
    }

    public void setFormPostId(Long formPostId) {
        this.formPostId = formPostId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserPhotoUrl() {
        return createUserPhotoUrl;
    }

    public void setCreateUserPhotoUrl(String createUserPhotoUrl) {
        this.createUserPhotoUrl = createUserPhotoUrl;
    }


}
