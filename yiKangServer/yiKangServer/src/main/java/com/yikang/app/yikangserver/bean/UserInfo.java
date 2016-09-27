package com.yikang.app.yikangserver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int INFO_STATUS_COMPLETE =1;
	public static final int INFO_STATUS_INCOMPLETE =0;


	public static final int CHECK_STATUS_UNCHECKED =0;
	public static final int  CHECK_STATUS_CHECKING =1;
	public static final int CHECK_STATUS_PASSED =2;
	public static final int  CHECK_STATUS_REJECT =3;

	@SerializedName( "userName")
	public String name;
	@SerializedName( "userSex")
	public String userSex;


	@SerializedName( "userId")
	public String userId;

	@SerializedName( "userPosition")
	public int profession = -1; // 职业

	@SerializedName( "jobCategory")
	public int jobType = -1; // 职业类型 全职和兼职

	@SerializedName( "nums")
	public int paintsNums = 0;// 病患个数

	public int getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(int isFollow) {
		this.isFollow = isFollow;
	}

	@SerializedName( "isFollow")
	public int isFollow;// 病患个数

	@SerializedName( "followUserNumber")
	public int followUserNumber = 0;// 病患个数

	public int getWatchUserNumber() {
		return watchUserNumber;
	}

	public void setWatchUserNumber(int watchUserNumber) {
		this.watchUserNumber = watchUserNumber;
	}

	@SerializedName( "watchUserNumber")
	public int watchUserNumber = 0;// 病患个数

	public int getQuestionAnswerNumber() {
		return questionAnswerNumber;
	}

	public void setQuestionAnswerNumber(int questionAnswerNumber) {
		this.questionAnswerNumber = questionAnswerNumber;
	}

	public int questionAnswerNumber = 0;// 病患个数

	@SerializedName( "forumPostsTznumber")
	public int forumPostsTznumber = 0;// 病患个数

	public int getFollowUserNumber() {
		return followUserNumber;
	}

	public void setFollowUserNumber(int followUserNumber) {
		this.followUserNumber = followUserNumber;
	}

	public int getForumPostsTznumber() {
		return forumPostsTznumber;
	}

	public void setForumPostsTznumber(int forumPostsTznumber) {
		this.forumPostsTznumber = forumPostsTznumber;
	}

	public int getWatchTaglibNumber() {
		return watchTaglibNumber;
	}

	public void setWatchTaglibNumber(int watchTaglibNumber) {
		this.watchTaglibNumber = watchTaglibNumber;
	}

	@SerializedName( "watchTaglibNumber")
	public int watchTaglibNumber = 0;// 病患个数


	@SerializedName( "hospital")
	public String hospital;

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	@SerializedName( "designationName")
	public String designationName;

	@SerializedName( "birthday")
	public String birthday;

	public String getUserIntroduce() {
		return userIntroduce;
	}

	public void setUserIntroduce(String userIntroduce) {
		this.userIntroduce = userIntroduce;
	}

	@SerializedName( "userIntroduce")
	public String userIntroduce;

	@SerializedName( "officeObj")
	public Department department;


	@SerializedName( "invitationCode")
	public String inviteCode;

	@SerializedName( "photoUrl")
	public String avatarImg;

	@SerializedName( "mapPositionAddress")
	public String mapPositionAddress;

	@SerializedName( "addressDetail")
	public String addressDetail;

	@SerializedName( "districtCode")
	public String districtCode;

	public String consumerTime;
	
	@SerializedName("invitationUrl")
	public String invitationUrl;

	@SerializedName("infoWrite")
	public int infoStatus = INFO_STATUS_INCOMPLETE;

	@SerializedName("positionAuditStatus")
	public int  professionCheckStatus= CHECK_STATUS_UNCHECKED;

	@SerializedName("adepts")
    public ArrayList<Expert> special;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getProfession() {
		return profession;
	}

	public void setProfession(int profession) {
		this.profession = profession;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public int getPaintsNums() {
		return paintsNums;
	}

	public void setPaintsNums(int paintsNums) {
		this.paintsNums = paintsNums;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getAvatarImg() {
		return avatarImg;
	}

	public void setAvatarImg(String avatarImg) {
		this.avatarImg = avatarImg;
	}

	public String getMapPositionAddress() {
		return mapPositionAddress;
	}

	public void setMapPositionAddress(String mapPositionAddress) {
		this.mapPositionAddress = mapPositionAddress;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getConsumerTime() {
		return consumerTime;
	}

	public void setConsumerTime(String consumerTime) {
		this.consumerTime = consumerTime;
	}

	public String getInvitationUrl() {
		return invitationUrl;
	}

	public void setInvitationUrl(String invitationUrl) {
		this.invitationUrl = invitationUrl;
	}

	public int getInfoStatus() {
		return infoStatus;
	}

	public void setInfoStatus(int infoStatus) {
		this.infoStatus = infoStatus;
	}

	public int getProfessionCheckStatus() {
		return professionCheckStatus;
	}

	public void setProfessionCheckStatus(int professionCheckStatus) {
		this.professionCheckStatus = professionCheckStatus;
	}

	public ArrayList<Expert> getSpecial() {
		return special;
	}

	public void setSpecial(ArrayList<Expert> special) {
		this.special = special;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", userSex='" + userSex + '\'' +
				", userId='" + userId + '\'' +
				", profession=" + profession +
				", jobType=" + jobType +
				", paintsNums=" + paintsNums +
				", hospital='" + hospital + '\'' +
				", birthday='" + birthday + '\'' +
				", department=" + department +
				", inviteCode='" + inviteCode + '\'' +
				", avatarImg='" + avatarImg + '\'' +
				", mapPositionAddress='" + mapPositionAddress + '\'' +
				", addressDetail='" + addressDetail + '\'' +
				", districtCode='" + districtCode + '\'' +
				", consumerTime='" + consumerTime + '\'' +
				", invitationUrl='" + invitationUrl + '\'' +
				", infoStatus=" + infoStatus +
				", professionCheckStatus=" + professionCheckStatus +
				", special=" + special +
				'}';
	}
}
