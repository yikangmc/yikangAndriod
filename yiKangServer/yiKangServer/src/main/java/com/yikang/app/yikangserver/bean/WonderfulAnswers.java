package com.yikang.app.yikangserver.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class WonderfulAnswers {
    private String content;

    public int getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(int userPosition) {
        this.userPosition = userPosition;
    }

    private int userPosition;
    private Long createUserId;
    private Long questionAnswerId;
    private Long questionId;
    private List<QuestionImages> questionImages;

    public List<QuestionImages> getQuestionImages() {
        return questionImages;
    }

    public void setQuestionImages(List<QuestionImages> questionImages) {
        this.questionImages = questionImages;
    }

    public Long getStarNum() {
        return starNum;
    }

    public void setStarNum(Long starNum) {
        this.starNum = starNum;
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

    private Long starNum;
    private String userName;
    private String photoUrl;

    public QuestionAnswers getQuestion() {
        return question;
    }

    public void setQuestion(QuestionAnswers question) {
        this.question = question;
    }

    private QuestionAnswers question;
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    private Long createTime;
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(Long questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }


}
