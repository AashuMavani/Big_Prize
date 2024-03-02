package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Big_Reward_Screen_Model implements Serializable {

    @SerializedName("status")
    private String status;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("dailyBonus")
    private Big_Daily_Bonus dailyBonus;

    @Expose
    private List<Big_Home_Data_Item> taskList;
    @SerializedName("message")
    private String message;

    @SerializedName("homeNote")
    private String homeNote;

    @SerializedName("rewardDataList")
    private List<Big_Home_Data_List_Item> rewardDataList;

    @SerializedName("adFailUrl")
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;
    @SerializedName("todayCompletedTask")
    private String todayCompletedTask;



    public String getTodayCompletedTask() {
        return todayCompletedTask;
    }

    public void setTodayCompletedTask(String todayCompletedTask) {
        this.todayCompletedTask = todayCompletedTask;
    }

    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;

    @Expose
    private String screenNo;
    @Expose
    private String taskButton;
    @Expose
    private String taskId;

    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Big_Daily_Bonus getDailyBonus() {
        return dailyBonus;
    }

    public void setDailyBonus(Big_Daily_Bonus dailyBonus) {
        this.dailyBonus = dailyBonus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public List<Big_Home_Data_List_Item> getRewardDataList() {
        return rewardDataList;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getIsTodayTaskCompleted() {
        return isTodayTaskCompleted;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public String getTaskButton() {
        return taskButton;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<Big_Home_Data_Item> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Big_Home_Data_Item> taskList) {
        this.taskList = taskList;
    }

    public void setRewardDataList(List<Big_Home_Data_List_Item> rewardDataList) {
        this.rewardDataList = rewardDataList;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public void setIsTodayTaskCompleted(String isTodayTaskCompleted) {
        this.isTodayTaskCompleted = isTodayTaskCompleted;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public void setTaskButton(String taskButton) {
        this.taskButton = taskButton;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
