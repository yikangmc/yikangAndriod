package com.yikang.app.yikangserver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 郝晓东 on 2016/5/28.
 */
public class TagHot implements Serializable {
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

    public String getTaglibId() {
        return taglibId;
    }

    public void setTaglibId(String taglibId) {
        this.taglibId = taglibId;
    }

    @SerializedName( "tagName")
    private String tagName;
    @SerializedName( "tagPic")
    private String tagPic;
    @SerializedName( "taglibId")
    private String taglibId;

    public String getIsStore() {
        return isStore;
    }

    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }

    @SerializedName( "isStore")
    private String isStore;
}
