package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Big_Daily_Reward_Response_Model {

    @SerializedName("data")
    private List<Big_Daily_Reward_Data_Item> data;
    @Expose
    private String isShowInterstitial;

    @Expose
    private String isClaimedDailyReward;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String totalPoints;
    @Expose
    private String adFailUrl;

    @SerializedName("tigerInApp")
    private String tigerInApp;
    @Expose
    private Big_Top_Ads topAds;
    @Expose
    private String userToken;
    @Expose
    private String homeNote;
    @Expose
    private String earningPoint;
    @Expose
    private String btnColor;
    @Expose
    private String btnName;
    @Expose
    private String note;
    @Expose
    private String todayDate;
    @Expose
    private String endDate;



    public List<Big_Daily_Reward_Data_Item> getData() {
        return data;
    }

    public void setData(List<Big_Daily_Reward_Data_Item> dailyRewardData) {
        this.data = dailyRewardData;
    }

    public String getIsShowInterstitial() {
        return isShowInterstitial;
    }

    public void setIsShowInterstitial(String isShowInterstitial) {
        this.isShowInterstitial = isShowInterstitial;
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

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public String getBtnColor() {
        return btnColor;
    }

    public String getBtnName() {
        return btnName;
    }

    public String getNote() {
        return note;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getIsClaimedDailyReward() {
        return isClaimedDailyReward;
    }

    public void setIsClaimedDailyReward(String isClaimedDailyReward) {
        this.isClaimedDailyReward = isClaimedDailyReward;
    }
}

