package com.yikang.app.yikangserver.bean;

/**
 * Created by Administrator on 2016/6/4.
 */
public class Childs {
    private Long answerNumber;
    private Long followNumber;
    private Long fornumPostsNumber;

    public int getForumPostsTznumber() {
        return forumPostsTznumber;
    }

    public void setForumPostsTznumber(int forumPostsTznumber) {
        this.forumPostsTznumber = forumPostsTznumber;
    }

    private int forumPostsTznumber;
    private Long parentId;
    private Long taglibId;
    public boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    private Long createTime;

    public Long getForumPostsTzUpdateTime() {
        return forumPostsTzUpdateTime;
    }

    public void setForumPostsTzUpdateTime(Long forumPostsTzUpdateTime) {
        this.forumPostsTzUpdateTime = forumPostsTzUpdateTime;
    }

    private Long forumPostsTzUpdateTime;
    private int isStore;

    public int getIsStore() {
        return isStore;
    }

    public void setIsStore(int isStore) {
        this.isStore = isStore;
    }

    private String tagName;

    public String getTagPic() {
        return tagPic;
    }

    public void setTagPic(String tagPic) {
        this.tagPic = tagPic;
    }

    public Long getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(Long answerNumber) {
        this.answerNumber = answerNumber;
    }

    public Long getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(Long followNumber) {
        this.followNumber = followNumber;
    }

    public Long getFornumPostsNumber() {
        return fornumPostsNumber;
    }

    public void setFornumPostsNumber(Long fornumPostsNumber) {
        this.fornumPostsNumber = fornumPostsNumber;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTaglibId() {
        return taglibId;
    }

    public void setTaglibId(Long taglibId) {
        this.taglibId = taglibId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    private String tagPic;
}
