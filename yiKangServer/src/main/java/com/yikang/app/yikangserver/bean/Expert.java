package com.yikang.app.yikangserver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liu on 16/3/16.
 */
public class Expert  implements Serializable{
    @SerializedName("adeptName")
    public String name;
    @SerializedName("adeptId")
    public String id;
    @SerializedName("isCheck")
    public boolean isChecked;

    public Expert(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return "Expert{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
