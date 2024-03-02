package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Big_Task_Details_Response_Model {

    @Expose
    private String adFailUrl;
    @Expose
    private Big_Top_Ads topAds;
    @Expose
    private String homeNote;
    @Expose
    private String userToken;
    @Expose
    private String status;
    @Expose
    private String isShowInterstitial;
    @Expose
    private String message;
    @Expose
    private Big_Task_Details taskDetails;
    @SerializedName("tigerInApp")
    private String tigerInApp;



    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Big_Task_Details getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(Big_Task_Details taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }

    public String getIsShowInterstitial() {
        return isShowInterstitial;
    }

    public void setIsShowInterstitial(String isShowInterstitial) {
        this.isShowInterstitial = isShowInterstitial;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }
}

