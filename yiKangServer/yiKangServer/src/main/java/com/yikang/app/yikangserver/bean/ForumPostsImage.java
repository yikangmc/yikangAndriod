package com.yikang.app.yikangserver.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ForumPostsImage implements Serializable {
    private String createTime;
    private Long forumPostsId;
    private Long forumPostsImagesId;
    private String imageUrl;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getForumPostsId() {
        return forumPostsId;
    }

    public void setForumPostsId(Long forumPostsId) {
        this.forumPostsId = forumPostsId;
    }

    public Long getForumPostsImagesId() {
        return forumPostsImagesId;
    }

    public void setForumPostsImagesId(Long forumPostsImagesId) {
        this.forumPostsImagesId = forumPostsImagesId;
    }



}
