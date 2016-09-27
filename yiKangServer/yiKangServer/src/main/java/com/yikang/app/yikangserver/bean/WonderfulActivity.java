package com.yikang.app.yikangserver.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/6/3.
 */
public class WonderfulActivity implements Serializable {
    private String content;
    private String title;
    private Double cost;
    private String mapPsitionAddress;
    private String detailAddress;
    private Long startTime;
    private String endTime;
    private String entryStartTime;
    private String entryEndTime;
    private String recommendPicUrl;
    private String createUsername;
    private String photoUrl;
    private int createUserId;
    private int isFollow;

    public int getCreateActivetyNumber() {
        return createActivetyNumber;
    }

    public void setCreateActivetyNumber(int createActivetyNumber) {
        this.createActivetyNumber = createActivetyNumber;
    }

    private int createActivetyNumber;

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(int userPosition) {
        this.userPosition = userPosition;
    }

    private int userPosition;
    private Long activetyId;
    private Long createTime;
    private Long updateTime;
    private Long personNumber;
    private Long checkStatus;
    private Long activetyMode;
    private Long partakeNums;
    private Integer activetyStatus;
    private Long isPartake;


    public Long getIsPartake() {
        return isPartake;
    }

    public void setIsPartake(Long isPartake) {
        this.isPartake = isPartake;
    }



    public Integer getActivetyStatus() {
        return activetyStatus;
    }

    public void setActivetyStatus(Integer activetyStatus) {
        this.activetyStatus = activetyStatus;
    }


    public Long getPartakeNums() {
        return partakeNums;
    }

    public void setPartakeNums(Long partakeNums) {
        this.partakeNums = partakeNums;
    }


    public List<FollowUsers> getFollowUsers() {
        return followUsers;
    }

    public void setFollowUsers(List<FollowUsers> followUsers) {
        this.followUsers = followUsers;
    }

    private List<FollowUsers> followUsers;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }


    public String getRecommendPicUrl() {
        return recommendPicUrl;
    }

    public void setRecommendPicUrl(String recommendPicUrl) {
        this.recommendPicUrl = recommendPicUrl;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getMapPsitionAddress() {
        return mapPsitionAddress;
    }

    public void setMapPsitionAddress(String mapPsitionAddress) {
        this.mapPsitionAddress = mapPsitionAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEntryStartTime() {
        return entryStartTime;
    }

    public void setEntryStartTime(String entryStartTime) {
        this.entryStartTime = entryStartTime;
    }

    public String getEntryEndTime() {
        return entryEndTime;
    }

    public void setEntryEndTime(String entryEndTime) {
        this.entryEndTime = entryEndTime;
    }

    public Long getActivetyMode() {
        return activetyMode;
    }

    public void setActivetyMode(Long activetyMode) {
        this.activetyMode = activetyMode;
    }

    public Long getActivetyId() {
        return activetyId;
    }

    public void setActivetyId(Long activetyId) {
        this.activetyId = activetyId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Long personNumber) {
        this.personNumber = personNumber;
    }

    public Long getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Long checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }
}
