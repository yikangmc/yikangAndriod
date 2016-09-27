package com.yikang.app.yikangserver.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 郝晓东 on 2016/5/26.
 */
public class BannerBean {
    @SerializedName( "actionUrl")
    private String actionUrl;
    @SerializedName( "banerId")
    private String banerId;
    @SerializedName( "banerPic")
    private String  banerPic;
    private String  title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String  content;

    public String getForumPostDetailContent() {
        return forumPostDetailContent;
    }

    public void setForumPostDetailContent(String forumPostDetailContent) {
        this.forumPostDetailContent = forumPostDetailContent;
    }

    private String  forumPostDetailContent;

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getBanerId() {
        return banerId;
    }

    public void setBanerId(String banerId) {
        this.banerId = banerId;
    }

    public String  getBanerPic() {
        return banerPic;
    }

    public void setBanerPic(String  banerPic) {
        this.banerPic = banerPic;
    }
}
