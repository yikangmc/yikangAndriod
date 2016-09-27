package com.yikang.app.yikangserver.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/3.
 */
public class QuestionImages implements Serializable {
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuestionImageId() {
        return questionImageId;
    }

    public void setQuestionImageId(Long questionImageId) {
        this.questionImageId = questionImageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private Long createTime;
    private Long questionId;
    private Long questionImageId;
    private String imageUrl;
}
