package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Big_Home_Data_List_Item implements Serializable {


    @Expose
    private String fullImage;
    @Expose
    private String displayImage;
    @Expose
    private String bgColor;
    @Expose
    private List<Big_Home_Data_Item> data;


    @Expose
    private String isBorder;
    @Expose
    private String jsonImage;
    @Expose
    private String label;

    private String isViewAll;
    @Expose
    private String screenNo;
    @Expose
    private String title;
    @Expose
    private String subTitle;
    @Expose
    private String type;
    @Expose
    private String pointBackgroundColor;
    @Expose
    private String pointTextColor;
    @Expose
    private String buttonText;
    @Expose
    private String isActive;
    @Expose
    private String notActiveMessage;
    @Expose
    private String icon;
    @Expose
    private String id;
    @Expose
    private String taskId;
    @Expose
    private String url;
    @Expose
    private String image;
    @Expose
    private String dailyRewardTodayDate;
    @Expose
    private String dailyRewardEndDate;
    @Expose
    private String dailyRewardPoints;
    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String iconBGColor;
    @Expose
    private String taskCount;
    @Expose
    private String note;
    @Expose
    private String isBlink;
    @Expose
    private String lottieBgUrl;
    @Expose
    private String winningPoints;
    @Expose
    private String leaderboardRank;

    public String getLeaderboardRank() {
        return leaderboardRank;
    }

    public void setLeaderboardRank(String leaderboardRank) {
        this.leaderboardRank = leaderboardRank;
    }

    public String getWinningPoints() {
        return winningPoints;
    }

    public void setWinningPoints(String winningPoints) {
        this.winningPoints = winningPoints;
    }

    public String getLottieBgUrl() {
        return lottieBgUrl;
    }

    public void setLottieBgUrl(String lottieBgUrl) {
        this.lottieBgUrl = lottieBgUrl;
    }

    public String getIsBlink() {
        return isBlink;
    }

    public void setIsBlink(String isBlink) {
        this.isBlink = isBlink;
    }

    @SerializedName("milestoneData")
    private List<Big_MilestoneTargetDataItem> milestoneData;
    @Expose
    private List<Big_MilestoneTargetDataItem> dailyTargetList;

    public List<Big_MilestoneTargetDataItem> getDailyTargetList() {
        return dailyTargetList;
    }

    public void setDailyTargetList(List<Big_MilestoneTargetDataItem> dailyTargetList) {
        this.dailyTargetList = dailyTargetList;
    }

    public List<Big_MilestoneTargetDataItem> getMilestoneData() {
        return milestoneData;
    }

    public void setMilestoneData(List<Big_MilestoneTargetDataItem> milestoneData) {
        this.milestoneData = milestoneData;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public String getIconBGColor() {
        return iconBGColor;
    }

    public void setIconBGColor(String iconBGColor) {
        this.iconBGColor = iconBGColor;
    }

    public String getIsTodayTaskCompleted() {
        return isTodayTaskCompleted;
    }

    public void setIsTodayTaskCompleted(String isTodayTaskCompleted) {
        this.isTodayTaskCompleted = isTodayTaskCompleted;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public List<Big_Home_Data_Item> getData() {
        return data;
    }

    public void setData(List<Big_Home_Data_Item> data) {
        this.data = data;
    }

    public String getIsBorder() {
        return isBorder;
    }

    public void setIsBorder(String isBorder) {
        this.isBorder = isBorder;
    }

    public String getIsViewAll() {
        return isViewAll;
    }

    public void setIsViewAll(String isViewAll) {
        this.isViewAll = isViewAll;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJsonImage() {
        return jsonImage;
    }

    public void setJsonImage(String jsonImage) {
        this.jsonImage = jsonImage;
    }

    public String getPointBackgroundColor() {
        return pointBackgroundColor;
    }

    public void setPointBackgroundColor(String pointBackgroundColor) {
        this.pointBackgroundColor = pointBackgroundColor;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPointTextColor() {
        return pointTextColor;
    }

    public void setPointTextColor(String pointTextColor) {
        this.pointTextColor = pointTextColor;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getNotActiveMessage() {
        return notActiveMessage;
    }

    public void setNotActiveMessage(String notActiveMessage) {
        this.notActiveMessage = notActiveMessage;
    }


    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDailyRewardTodayDate() {
        return dailyRewardTodayDate;
    }

    public String getDailyRewardEndDate() {
        return dailyRewardEndDate;
    }

    public String getDailyRewardPoints() {
        return dailyRewardPoints;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public void setDailyRewardTodayDate(String dailyRewardTodayDate) {
        this.dailyRewardTodayDate = dailyRewardTodayDate;
    }

    public void setDailyRewardEndDate(String dailyRewardEndDate) {
        this.dailyRewardEndDate = dailyRewardEndDate;
    }

    public void setDailyRewardPoints(String dailyRewardPoints) {
        this.dailyRewardPoints = dailyRewardPoints;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }
}

