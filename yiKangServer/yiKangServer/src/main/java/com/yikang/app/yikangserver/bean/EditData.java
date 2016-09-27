package com.yikang.app.yikangserver.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/6/28.
 */
public class EditData {
    public String inputStr;
    public String imagePath;
    public Bitmap bitmap;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
