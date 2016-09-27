package com.yikang.app.yikangserver.bean;

import java.util.Date;
import java.util.List;

public class Question {
    private Long questionId;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    private List<Taglibs> taglibs;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Taglibs> getTaglibs() {
        return taglibs;
    }

    public void setTaglibs(List<Taglibs> taglibs) {
        this.taglibs = taglibs;
    }

    private String title;

    private String content;

    private Long createUserId;

    private Date createTime;

    private Byte isDelete;
    
    private Integer star;

    private Date updateTime;
    
    //用户名称
    private String userName;
    //头像地址
    private String photoUrl;
    
    //回复数量
    private Integer answerNum;
    

}