package com.yikang.app.yikangserver.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class FundBean implements Serializable {
    private Long myScore;
    private Long todayScore;
    private Long score;
    public List<Job> onceJobs;
    public List<Job> usualJobs;

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public List<Job> getUsualJobs() {
        return usualJobs;
    }

    public void setUsualJobs(List<Job> usualJobs) {
        this.usualJobs = usualJobs;
    }

    public Long getMyScore() {
        return myScore;
    }

    public void setMyScore(Long myScore) {
        this.myScore = myScore;
    }

    public Long getTodayScore() {
        return todayScore;
    }

    public void setTodayScore(Long todayScore) {
        this.todayScore = todayScore;
    }

    public List<Job> getOnceJobs() {
        return onceJobs;
    }

    public void setOnceJobs(List<Job> onceJobs) {
        this.onceJobs = onceJobs;
    }


}
