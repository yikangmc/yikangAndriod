package com.yikang.app.yikangserver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/6/3.
 */
public class HpWonderfulContent extends BannerBean implements Serializable {

    @SerializedName("userName")
    private String userName;

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    @SerializedName("createUsername")
    private String createUsername;

    public QuestionAnswers getQuestion() {
        return question;
    }

    public void setQuestion(QuestionAnswers question) {
        this.question = question;
    }

    @SerializedName("question")
    private QuestionAnswers question;

    public String getForumPostHtmlDetailContent() {
        return forumPostHtmlDetailContent;
    }

    public void setForumPostHtmlDetailContent(String forumPostHtmlDetailContent) {
        this.forumPostHtmlDetailContent = forumPostHtmlDetailContent;
    }

    @SerializedName("forumPostHtmlDetailContent")
    private String forumPostHtmlDetailContent;
    @SerializedName("photoUrl")
    private String photoUrl;
    @SerializedName("forumPostId")
    private int forumPostId;

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }
    @SerializedName("userPosition")
    private int userPosition;

    @SerializedName("designationName")
    private String designationName;

    public String getCreateUserDesignationName() {
        return createUserDesignationName;
    }

    public void setCreateUserDesignationName(String createUserDesignationName) {
        this.createUserDesignationName = createUserDesignationName;
    }

    public int getCreateUserPosition() {
        return createUserPosition;
    }

    public void setCreateUserPosition(int createUserPosition) {
        this.createUserPosition = createUserPosition;
    }

    @SerializedName("createUserDesignationName")
    private String createUserDesignationName;
    @SerializedName("createUserPosition")
    private int createUserPosition;

    @SerializedName("reportComplaintsStatus")
    private int reportComplaintsStatus;
    @SerializedName("isEssence")
    private int isEssence;
    @SerializedName("createTime")
    private Long createTime;
    @SerializedName("updateTime")
    private Long updateTime;
    @SerializedName("stars")
    private int stars;
    @SerializedName("isStar")
    private int isStar;

    public int getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(int userPosition) {
        this.userPosition = userPosition;
    }



    public int getIsStore() {
        return isStore;
    }

    public void setIsStore(int isStore) {
        this.isStore = isStore;
    }

    @SerializedName("isStore")
    private int isStore;

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    @SerializedName("shareNum")
    private int shareNum;
    @SerializedName("createUserId")
    private int createUserId;
    @SerializedName("shareUrl")
    private String shareUrl;
    @SerializedName("recommendPicUrl")
    private String recommendPicUrl;
    @SerializedName("startNums")
    private int startNums;
    @SerializedName("answerNum")
    private int answersNum;

    public int getAnswersNum() {
        return answersNum;
    }

    public void setAnswersNum(int answersNum) {
        this.answersNum = answersNum;
    }

    @SerializedName("answersNums")
    private int answersNums;
    @SerializedName("taglibs")
    private List<Taglibs> taglibs;

    private HpWonderfulContent formPosts;

    public HpWonderfulContent getFormPosts() {
        return formPosts;
    }

    public void setFormPosts(HpWonderfulContent formPosts) {
        this.formPosts = formPosts;
    }

    @SerializedName("forumPostsAnswers")

    private List<ForumPostsAnswers> forumPostsAnswers;
    @SerializedName("questionAnswers")
    private List<QuestionAnswers> questionAnswers;
    @SerializedName("forumPostsImage")
    private List<ForumPostsImage> forumPostsImage;
    @SerializedName("questionImages")

    private List<QuestionImages> questionImages;


    public List<FormPostsStar> getFormPostsStarLists() {
        return formPostsStarLists;
    }

    public void setFormPostsStarLists(List<FormPostsStar> formPostsStarLists) {
        this.formPostsStarLists = formPostsStarLists;
    }

    @SerializedName("formPostsStarLists")

    private List<FormPostsStar> formPostsStarLists;

    public List<QuestionImages> getQuestionImages() {
        return questionImages;
    }

    public void setQuestionImages(List<QuestionImages> questionImages) {
        this.questionImages = questionImages;
    }


    public List<QuestionAnswers> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswers> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

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


    public List<ForumPostsAnswers> getForumPostsAnswers() {
        return forumPostsAnswers;
    }

    public void setForumPostsAnswers(List<ForumPostsAnswers> forumPostsAnswers) {
        this.forumPostsAnswers = forumPostsAnswers;
    }

    public List<ForumPostsImage> getForumPostsImage() {
        return forumPostsImage;
    }

    public void setForumPostsImage(List<ForumPostsImage> forumPostsImage) {
        this.forumPostsImage = forumPostsImage;
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

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
