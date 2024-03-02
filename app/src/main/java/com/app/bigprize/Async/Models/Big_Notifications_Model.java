package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Big_Notifications_Model implements Serializable {

    @SerializedName("homeNote")
    private String homeNote;
    @SerializedName("userToken")
    private String userToken;

    @SerializedName("notificationList")
    private List<Big_Notification_ListItem> notificationList;

    @Expose
    private Big_Top_Ads topAds;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @Expose
    private String isShowInterstitial;
    @SerializedName("adFailUrl")
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;


    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }

    public String getHomeNote(){
        return homeNote;
    }

    public List<Big_Notification_ListItem> getNotificationList(){
        return notificationList;
    }
    public String getStatus(){
        return status;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public void setNotificationList(List<Big_Notification_ListItem> notificationList) {
        this.notificationList = notificationList;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
