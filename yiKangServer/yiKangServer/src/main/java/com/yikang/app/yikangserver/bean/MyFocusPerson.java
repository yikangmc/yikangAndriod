package com.yikang.app.yikangserver.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class MyFocusPerson {
    private String photoUrl;
    private Long userId;
    private String userName;
    private String userServiceName;
    private List<Taglibs> taglibs;

    public List<Expert> getAdepts() {
        return adepts;
    }

    public void setAdepts(List<Expert> adepts) {
        this.adepts = adepts;
    }

    private List<Expert> adepts;
    private int followCount;

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public String getUserServiceName() {
        return userServiceName;
    }

    public void setUserServiceName(String userServiceName) {
        this.userServiceName = userServiceName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Taglibs> getTaglibs() {
        return taglibs;
    }

    public void setTaglibs(List<Taglibs> taglibs) {
        this.taglibs = taglibs;
    }

}
