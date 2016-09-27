package com.yikang.app.yikangserver.bean;

import java.util.List;

public class LablesBean {

    private String lableName;
    private String lableTime;
    private String lableTiezi;
    private String lableSee;
    private int answersNums;

    public int getForumPostId() {
        return forumPostId;
    }

    public void setForumPostId(int forumPostId) {
        this.forumPostId = forumPostId;
    }

    private int forumPostId;
    public boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private List<QuestionImages> questionImages;

    public List<QuestionImages> getQuestionImages() {
        return questionImages;
    }

    public void setQuestionImages(List<QuestionImages> questionImages) {
        this.questionImages = questionImages;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getAnswersNums() {
        return answersNums;
    }

    public void setAnswersNums(int answersNums) {
        this.answersNums = answersNums;
    }

    private int stars;
    private List<Childs> childs;

    public String getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }

    private String answerNum;
    private int taglibId;

    public int getIsStore() {
        return isStore;
    }

    public void setIsStore(int isStore) {
        this.isStore = isStore;
    }

    private int isStore;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    private int questionId;
    private String tagName;
    private String fornumPostsNumber;
    private String followNumber;
    private long updateTime;

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    private long createTime;
    private String tagPic;
    private String recommendPicUrl;
    private String title;
    private String userName;
    private String content;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRecommendPicUrl() {
        return recommendPicUrl;
    }

    public void setRecommendPicUrl(String recommendPicUrl) {
        this.recommendPicUrl = recommendPicUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String photoUrl;

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTagPic() {
        return tagPic;
    }

    public void setTagPic(String tagPic) {
        this.tagPic = tagPic;
    }

    private int orders;

    public String getFornumPostsNumber() {
        return fornumPostsNumber;
    }

    public void setFornumPostsNumber(String fornumPostsNumber) {
        this.fornumPostsNumber = fornumPostsNumber;
    }

    public String getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(String followNumber) {
        this.followNumber = followNumber;
    }

    public int getTaglibId() {
        return taglibId;
    }

    public void setTaglibId(int taglibId) {
        this.taglibId = taglibId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }

    public String getLableTime() {
        return lableTime;
    }

    public void setLableTime(String lableTime) {
        this.lableTime = lableTime;
    }

    public String getLableTiezi() {
        return lableTiezi;
    }

    public void setLableTiezi(String lableTiezi) {
        this.lableTiezi = lableTiezi;
    }

    public String getLableSee() {
        return lableSee;
    }

    public void setLableSee(String lableSee) {
        this.lableSee = lableSee;
    }

    public List<Childs> getChilds() {
        return childs;
    }

    public void setChilds(List<Childs> childs) {
        this.childs = childs;
    }
}
