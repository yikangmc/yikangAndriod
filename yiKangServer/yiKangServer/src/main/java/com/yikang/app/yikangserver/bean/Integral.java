package com.yikang.app.yikangserver.bean;

public class Integral {
    private Long integralId;

    private Long jobsId;

    private Integer score;

    private Long createDatetime;

    private Long updateDatetime;

    private Long createUserId;

    private Byte jobGroup;

    private Byte jobState;

    private Byte integralType;

    private Long ownerUserId;
    
    public Long getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(Long ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public Long getIntegralId() {
        return integralId;
    }

    public void setIntegralId(Long integralId) {
        this.integralId = integralId;
    }

    public Long getJobsId() {
        return jobsId;
    }

    public void setJobsId(Long jobsId) {
        this.jobsId = jobsId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Long updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Byte getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(Byte jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Byte getJobState() {
        return jobState;
    }

    public void setJobState(Byte jobState) {
        this.jobState = jobState;
    }

	public Byte getIntegralType() {
		return integralType;
	}

	public void setIntegralType(Byte integralType) {
		this.integralType = integralType;
	}

}