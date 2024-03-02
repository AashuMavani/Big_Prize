package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Big_EarningOptionsScreenModel implements Serializable {

    @SerializedName("status")
    private String status;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("dailyBonus")
    private Big_EverydayBonus dailyBonus;

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
    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;
    @Expose
    private String taskButton;
    @Expose
    private String taskId;
    @Expose
    private String screenNo;
    @Expose
    private List<Big_Home_Data_Item> taskList;
    @SerializedName("isShowGiveawayCode")
    private String isShowGiveawayCode;

    @SerializedName("giveawayCode")
    private String giveawayCode;
    @SerializedName("homeDialog")
    private Big_Home_Dialog homeDialog;
    @SerializedName("footerImage")
    private String footerImage;
    @SerializedName("isShowAdjoeLeaderboardIcon")
    private String isShowAdjoeLeaderboardIcon;

    public String getIsShowAdjoeLeaderboardIcon() {
        return isShowAdjoeLeaderboardIcon;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public void setIsShowGiveawayCode(String isShowGiveawayCode) {
        this.isShowGiveawayCode = isShowGiveawayCode;
    }

    public void setFooterImage(String footerImage) {
        this.footerImage = footerImage;
    }

    public String getFooterImage() {
        return footerImage;
    }

    public String getIsShowGiveawayCode() {
        return isShowGiveawayCode;
    }
    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Big_EverydayBonus getDailyBonus() {
        return dailyBonus;
    }

    public void setDailyBonus(Big_EverydayBonus dailyBonus) {
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

    public void setUserToken(String userToken) {
        this.userToken = userToken;
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

    public String getTodayCompletedTask() {
        return todayCompletedTask;
    }

    public void setTodayCompletedTask(String todayCompletedTask) {
        this.todayCompletedTask = todayCompletedTask;
    }

    public List<Big_Home_Data_Item> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Big_Home_Data_Item> taskList) {
        this.taskList = taskList;
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

    public Big_Home_Dialog getHomeDialog() {
        return homeDialog;
    }

    public void setHomeDialog(Big_Home_Dialog homeDialog) {
        this.homeDialog = homeDialog;
    }

    public String getGiveawayCode() {
        return giveawayCode;
    }
}