package com.yikang.app.yikangserver.bean;

import java.io.Serializable;

/**
 * Created by 郝晓东 on 2016/6/4.
 */
public class FollowUsers implements Serializable {
    private String photoUrl;
    private String userName;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
