package com.yikang.app.yikangserver.bean;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MessageState {
    private int systemNoReadNum;
    private int dynamicNoReadNum;
    private int systemAlert;
    private int dynamicAlert;

    public int getDynamicAlert() {
        return dynamicAlert;
    }

    public void setDynamicAlert(int dynamicAlert) {
        this.dynamicAlert = dynamicAlert;
    }

    public int getSystemAlert() {
        return systemAlert;
    }

    public void setSystemAlert(int systemAlert) {
        this.systemAlert = systemAlert;
    }



    public int getDynamicNoReadNum() {
        return dynamicNoReadNum;
    }

    public void setDynamicNoReadNum(int dynamicNoReadNum) {
        this.dynamicNoReadNum = dynamicNoReadNum;
    }

    public int getSystemNoReadNum() {
        return systemNoReadNum;
    }

    public void setSystemNoReadNum(int systemNoReadNum) {
        this.systemNoReadNum = systemNoReadNum;
    }


}
