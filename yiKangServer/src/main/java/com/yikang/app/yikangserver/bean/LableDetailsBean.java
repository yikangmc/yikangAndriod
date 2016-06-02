package com.yikang.app.yikangserver.bean;

import java.util.List;

public class LableDetailsBean {

	private String  headIvUrl;
	private String  headTvName;
	private String  HeadTvLable;
    private int  taglibId;
	private String  releaseTime;
	private List<String> detailIvUrls;
    private String  detailTv;
    private String  detailLable;
    private String  detailSupport;
    private String  detailDiscuss;
    public int getTaglibId() {
        return taglibId;
    }

    public void setTaglibId(int taglibId) {
        this.taglibId = taglibId;
    }

    public String getHeadIvUrl() {
        return headIvUrl;
    }

    public void setHeadIvUrl(String headIvUrl) {
        this.headIvUrl = headIvUrl;
    }

    public String getHeadTvName() {
        return headTvName;
    }

    public void setHeadTvName(String headTvName) {
        this.headTvName = headTvName;
    }

    public String getHeadTvLable() {
        return HeadTvLable;
    }

    public void setHeadTvLable(String headTvLable) {
        HeadTvLable = headTvLable;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public List<String> getDetailIvUrls() {
        return detailIvUrls;
    }

    public void setDetailIvUrls(List<String> detailIvUrls) {
        this.detailIvUrls = detailIvUrls;
    }

    public String getDetailTv() {
        return detailTv;
    }

    public void setDetailTv(String detailTv) {
        this.detailTv = detailTv;
    }

    public String getDetailLable() {
        return detailLable;
    }

    public void setDetailLable(String detailLable) {
        this.detailLable = detailLable;
    }

    public String getDetailSupport() {
        return detailSupport;
    }

    public void setDetailSupport(String detailSupport) {
        this.detailSupport = detailSupport;
    }

    public String getDetailDiscuss() {
        return detailDiscuss;
    }

    public void setDetailDiscuss(String detailDiscuss) {
        this.detailDiscuss = detailDiscuss;
    }
}
