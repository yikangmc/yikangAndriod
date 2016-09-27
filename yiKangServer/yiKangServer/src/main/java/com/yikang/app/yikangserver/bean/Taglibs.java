package com.yikang.app.yikangserver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yudong on 2016/5/6.
 */
public class Taglibs implements Serializable{
    @SerializedName( "taglibId")
    private int taglibId;

    public int getIsStore() {
        return isStore;
    }

    public void setIsStore(int isStore) {
        this.isStore = isStore;
    }

    @SerializedName( "isStore")
    private int isStore;

    public int getForumPostId() {
        return forumPostId;
    }

    public void setForumPostId(int forumPostId) {
        this.forumPostId = forumPostId;
    }

    @SerializedName( "forumPostId")
    private int forumPostId;
    @SerializedName( "tagName")
    private String tagName;
    @SerializedName( "tagPic")
    private String tagPic;
    @SerializedName( "parentId")
    private String parentId;
    @SerializedName( "orders")
    private String orders;
    @SerializedName( "isDelete")
    private String isDelete;
    @SerializedName( "createTime")
    private String createTime;
    @SerializedName( "updateTime")
    private String updateTime;
    @SerializedName( "createUserId")
    private String createUserId;
    @SerializedName( "isRecommend")
    private String isRecommend;

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

    public String getTagPic() {
        return tagPic;
    }

    public void setTagPic(String tagPic) {
        this.tagPic = tagPic;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

//    public int getTaglibId() {
//        return taglibId;
//    }
//
//    public void setTaglibId(int taglibId) {
//        this.taglibId = taglibId;
//    }
//
//    public String getTagName() {
//        return tagName;
//    }
//
//    public void setTagName(String tagName) {
//        this.tagName = tagName;
//    }
//
//    public String getTagPic() {
//        return tagPic;
//    }
//
//    public void setTagPic(String tagPic) {
//        this.tagPic = tagPic;
//    }
//
//    public String getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(String parentId) {
//        this.parentId = parentId;
//    }
//
//    public String getOrders() {
//        return orders;
//    }
//
//    public void setOrders(String orders) {
//        this.orders = orders;
//    }
//
//    public String getIsDelete() {
//        return isDelete;
//    }
//
//    public void setIsDelete(String isDelete) {
//        this.isDelete = isDelete;
//    }
//
//    public String getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(String updateTime) {
//        this.updateTime = updateTime;
//    }
//
//    public String getCreateUserId() {
//        return createUserId;
//    }
//
//    public void setCreateUserId(String createUserId) {
//        this.createUserId = createUserId;
//    }
//
//    public String getIsRecommend() {
//        return isRecommend;
//    }
//
//    public void setIsRecommend(String isRecommend) {
//        this.isRecommend = isRecommend;
//    }
}
