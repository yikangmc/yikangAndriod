package com.yikang.app.yikangserver.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/3.
 */
public class QuestionAnswers implements Serializable{
    private String content;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    private Question question;

    public String getHtmlDetailContent() {
        return htmlDetailContent;
    }

    public void setHtmlDetailContent(String htmlDetailContent) {
        this.htmlDetailContent = htmlDetailContent;
    }

    private String htmlDetailContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private String userName;
    private int userPosition;
    private List<QuestionImages> questionImages;

    public List<QuestionImages> getQuestionImages() {
        return questionImages;
    }

    public void setQuestionImages(List<QuestionImages> questionImages) {
        this.questionImages = questionImages;
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

    private String photoUrl;
    private Long createUserId;

    private Long dataSource;
    private Long isRecommend;

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    private int isStar;
    private List<Taglibs> taglibs;

    public List<Taglibs> getTaglibs() {
        return taglibs;
    }

    public void setTaglibs(List<Taglibs> taglibs) {
        this.taglibs = taglibs;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    private Long createTime;

    public Long getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(Long answerNum) {
        this.answerNum = answerNum;
    }

    private Long answerNum;

    public Long getStarNum() {
        return starNum;
    }

    public void setStarNum(Long starNum) {
        this.starNum = starNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDataSource() {
        return dataSource;
    }

    public void setDataSource(Long dataSource) {
        this.dataSource = dataSource;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Long isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(Long questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }

    private Long starNum;
    private Long questionId;
    private Long questionAnswerId;

    public int getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(int userPosition) {
        this.userPosition = userPosition;
    }
}
