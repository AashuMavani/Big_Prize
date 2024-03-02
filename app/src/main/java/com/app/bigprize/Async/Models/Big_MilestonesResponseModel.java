package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Big_MilestonesResponseModel {

    @Expose
    private String adFailUrl;
    @Expose
    private String encrypt;
    @Expose
    private String message;
    @Expose
    private List<Big_MilestoneDataItem> milestoneData;
    @Expose
    private Big_MilestoneDataItem singleMilestoneData;
    @Expose
    private String status;
    @Expose
    private String tigerInApp;

    @Expose
    private Big_Top_Ads topAds;
    @Expose
    private String earningPoint;
    @Expose
    private String userToken;
    @Expose
    private String homeNote;
    @Expose
    private String helpVideoUrl;
    @Expose
    private String winningPoints;



    public String getWinningPoints() {
        return winningPoints;
    }

    public void setWinningPoints(String winningPoints) {
        this.winningPoints = winningPoints;
    }

    public String getHelpVideoUrl() {
        return helpVideoUrl;
    }

    public void setHelpVideoUrl(String helpVideoUrl) {
        this.helpVideoUrl = helpVideoUrl;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Big_MilestoneDataItem> getMilestoneData() {
        return milestoneData;
    }

    public void setMilestoneData(List<Big_MilestoneDataItem> milestoneData) {
        this.milestoneData = milestoneData;
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

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Big_MilestoneDataItem getSingleMilestoneData() {
        return singleMilestoneData;
    }

    public void setSingleMilestoneData(Big_MilestoneDataItem singleMilestoneData) {
        this.singleMilestoneData = singleMilestoneData;
    }
}
