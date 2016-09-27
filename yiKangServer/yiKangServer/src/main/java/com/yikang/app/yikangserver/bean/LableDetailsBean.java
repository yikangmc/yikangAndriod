package com.yikang.app.yikangserver.bean;

import java.io.Serializable;
import java.util.List;

public class LableDetailsBean implements Serializable{

	private String  headIvUrl;
	private String  headTvName;
	private String  HeadTvLable;
    private int  taglibId;

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    private int isStar;;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    private Long  questionId;

    public long getToquestionId() {
        return toquestionId;
    }

    public void setToquestionId(long toquestionId) {
        this.toquestionId = toquestionId;
    }

    private Long  toquestionId;
    private String isHasPic;
    private Expert expert;
    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    private String createUserDesignationName;

    public String getIsHasPic() {
        return isHasPic;
    }

    public void setIsHasPic(String isHasPic) {
        this.isHasPic = isHasPic;
    }

    public String getCreateUserDesignationName() {
        return createUserDesignationName;
    }

    public void setCreateUserDesignationName(String createUserDesignationName) {
        this.createUserDesignationName = createUserDesignationName;
    }

    public int getCreateUserPosition() {
        return createUserPosition;
    }

    public void setCreateUserPosition(int createUserPosition) {
        this.createUserPosition = createUserPosition;
    }

    private int createUserPosition;
    public String getHasPic() {
        return isHasPic;
    }

    public void setHasPic(String hasPic) {
        isHasPic = hasPic;
    }

    public int getIsStore() {
        return isStore;
    }

    public void setIsStore(int isStore) {
        this.isStore = isStore;
    }

    private int  isStore;

    public int getUserPositions() {
        return userPositions;
    }

    public void setUserPositions(int userPositions) {
        this.userPositions = userPositions;
    }

    private int  userPositions;
	private String  releaseTime;
	private List<ForumPostsImage> detailIvUrls;
	private List<Taglibs> detailTaglibs;
    private List<Childs> childs;

    public List<Childs> getChilds() {
        return childs;
    }

    public void setChilds(List<Childs> childs) {
        this.childs = childs;
    }

    private String  detailTv;
    private List<Expert> adepts;

    public void setAdepts(List<Expert> adepts) {
        this.adepts = adepts;
    }

    public List<Taglibs> getDetailTaglibs() {
        return detailTaglibs;
    }

    public void setDetailTaglibs(List<Taglibs> detailTaglibs) {
        this.detailTaglibs = detailTaglibs;
    }

    private String  detailLable;
    private String  detailSupport;
    private String  detailDiscuss;
    private String  answersNums;

    public String getAnswersNums() {
        return answersNums;
    }

    public void setAnswersNums(String answersNums) {
        this.answersNums = answersNums;
    }

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

    public List<ForumPostsImage> getDetailIvUrls() {
        return detailIvUrls;
    }

    public void setDetailIvUrls(List<ForumPostsImage> detailIvUrls) {
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

    public List<Expert> getAdepts() {
        return adepts;
    }
}
