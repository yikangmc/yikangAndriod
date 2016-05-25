package com.yikang.app.yikangserver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yudong on 2016/5/3.
 */
public class HpWonderfulContent implements Serializable {

    @SerializedName( "userName")
    private String userName;
    @SerializedName( "photoUrl")
    private String photoUrl;
    @SerializedName( "forumPostId")
    private int forumPostId;
    @SerializedName( "title")
    private String title;
    @SerializedName( "content")
    private String content;
    @SerializedName( "reportComplaintsStatus")
    private int reportComplaintsStatus;
    @SerializedName( "isEssence")
    private int isEssence;
    @SerializedName( "createTime")
    private long createTime;
    @SerializedName( "updateTime")
    private long updateTime;
    @SerializedName( "stars")
    private int stars;
    @SerializedName( "shareNum")
    private int shareNum;
    @SerializedName( "createUserId")
    private int createUserId;
    @SerializedName( "shareUrl")
    private String shareUrl;
    @SerializedName( "recommendPicUrl")
    private String recommendPicUrl;
    @SerializedName( "startNums")
    private int startNums;
    @SerializedName( "answersNums")
    private int answersNums;
    @SerializedName( "formPostsStarLists")
    private String formPostsStarLists;
    @SerializedName( "forumPostsAnswers")
    private String forumPostsAnswers;
    @SerializedName( "taglibs")
    private List<Taglibs> taglibs;


    public int getForumPostId() {
        return forumPostId;
    }

    public void setForumPostId(int forumPostId) {
        this.forumPostId = forumPostId;
    }

    public int getReportComplaintsStatus() {
        return reportComplaintsStatus;
    }

    public void setReportComplaintsStatus(int reportComplaintsStatus) {
        this.reportComplaintsStatus = reportComplaintsStatus;
    }

    public int getIsEssence() {
        return isEssence;
    }

    public void setIsEssence(int isEssence) {
        this.isEssence = isEssence;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getStartNums() {
        return startNums;
    }

    public void setStartNums(int startNums) {
        this.startNums = startNums;
    }

    public int getAnswersNums() {
        return answersNums;
    }

    public void setAnswersNums(int answersNums) {
        this.answersNums = answersNums;
    }

    public String getFormPostsStarLists() {
        return formPostsStarLists;
    }

    public void setFormPostsStarLists(String formPostsStarLists) {
        this.formPostsStarLists = formPostsStarLists;
    }

    public String getForumPostsAnswers() {
        return forumPostsAnswers;
    }

    public void setForumPostsAnswers(String forumPostsAnswers) {
        this.forumPostsAnswers = forumPostsAnswers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getRecommendPicUrl() {
        return recommendPicUrl;
    }

    public void setRecommendPicUrl(String recommendPicUrl) {
        this.recommendPicUrl = recommendPicUrl;
    }


    public List<Taglibs> getTaglibs() {
        return taglibs;
    }

    public void setTaglibs(List<Taglibs> taglibs) {
        this.taglibs = taglibs;
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
}
