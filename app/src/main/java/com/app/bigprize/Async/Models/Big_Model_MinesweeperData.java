package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Big_Model_MinesweeperData implements Serializable {

    @Expose
    private String adFailUrl;
    @Expose
    private String nextGameTimer;
    @Expose
    private String userToken;

    @Expose
    private String helpVideoUrl;

    @Expose
    private List<Big_MilestoneDataItem> data;

    @Expose
    private String adType;
    @Expose
    private Long checkSpinNum;
    //    @SerializedName("totalGameCount")
    private String totalGameCount;

    @Expose
    private Big_Floating_Ads floatingAds;
    @Expose
    private String lastDate;
    @Expose
    private Big_Exit_Dialog exitDialog;
    @Expose
    private String message;
    //    @SerializedName("remainGameCount")
    private String remainGameCount;
    @Expose
    private String gameTime;
    @Expose
    private String status;
    @Expose
    private String tigerInApp;
    @Expose
    private String todayDate;
    @Expose
    private Big_Top_Ads topAds;

    //    @SerializedName("homeNote")
    private String homeNote;
    @Expose
    private String buttonImage;

    @Expose
    private String buttonTextColor;

    @Expose
    private String spinImage;

    @Expose
    private String backgroundImage;

    @Expose
    private String leavesImage;

    //    @SerializedName("timerTextColor")
    private String timerTextColor;

    @Expose
    private String labelBackgroundImage;

    @Expose
    private String points;

    @Expose
    private String winningPoints;

    @Expose
    private String mainNote;

    @Expose
    private String earningPoint;

    @Expose
    private String creditPoint;
    @Expose
    private String isShowAds;

    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;
    @Expose
    private String taskButton;
    @Expose
    private String taskId;





    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public Long getCheckSpinNum() {
        return checkSpinNum;
    }

    public void setCheckSpinNum(Long checkSpinNum) {
        this.checkSpinNum = checkSpinNum;
    }

    public String getTotalGameCount() {
        return totalGameCount;
    }

    public void setTotalGameCount(String totalGameCount) {
        this.totalGameCount = totalGameCount;
    }

    public List<Big_MilestoneDataItem> getData() {
        return data;
    }

    public void setData(List<Big_MilestoneDataItem> data) {
        this.data = data;
    }

    public String getRemainGameCount() {
        return remainGameCount;
    }

    public void setRemainGameCount(String remainGameCount) {
        this.remainGameCount = remainGameCount;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getWinningPoints() {
        return winningPoints;
    }

    public void setWinningPoints(String winningPoints) {
        this.winningPoints = winningPoints;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getNextGameTimer() {
        return nextGameTimer;
    }

    public void setNextGameTimer(String nextGameTimer) {
        this.nextGameTimer = nextGameTimer;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Big_Floating_Ads getFloatingAds() {
        return floatingAds;
    }

    public void setFloatingAds(Big_Floating_Ads floatingAds) {
        this.floatingAds = floatingAds;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }

    public String getHelpVideoUrl() {
        return helpVideoUrl;
    }

    public void setHelpVideoUrl(String helpVideoUrl) {
        this.helpVideoUrl = helpVideoUrl;
    }

    public String getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(String buttonImage) {
        this.buttonImage = buttonImage;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public String getSpinImage() {
        return spinImage;
    }

    public void setSpinImage(String spinImage) {
        this.spinImage = spinImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getLabelBackgroundImage() {
        return labelBackgroundImage;
    }

    public void setLabelBackgroundImage(String labelBackgroundImage) {
        this.labelBackgroundImage = labelBackgroundImage;
    }

    public String getMainNote() {
        return mainNote;
    }

    public void setMainNote(String mainNote) {
        this.mainNote = mainNote;
    }

    public String getPoint() {
        return points;
    }

    public void setPoint(String point) {
        this.points = point;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public String getCreditPoint() {
        return creditPoint;
    }

    public void setCreditPoint(String creditPoint) {
        this.creditPoint = creditPoint;
    }

    public String getTimerTextColor() {
        return timerTextColor;
    }

    public void setTimerTextColor(String timerTextColor) {
        this.timerTextColor = timerTextColor;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public Big_Exit_Dialog getExitDialog() {
        return exitDialog;
    }

    public void setExitDialog(Big_Exit_Dialog exitDialog) {
        this.exitDialog = exitDialog;
    }

    public String getLeavesImage() {
        return leavesImage;
    }

    public void setLeavesImage(String leavesImage) {
        this.leavesImage = leavesImage;
    }

    public String getIsShowAds() {
        return isShowAds;
    }

    public void setIsShowAds(String isShowAds) {
        this.isShowAds = isShowAds;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getIsTodayTaskCompleted() {
        return isTodayTaskCompleted;
    }

    public void setIsTodayTaskCompleted(String isTodayTaskCompleted) {
        this.isTodayTaskCompleted = isTodayTaskCompleted;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public String getTaskButton() {
        return taskButton;
    }

    public void setTaskButton(String taskButton) {
        this.taskButton = taskButton;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
