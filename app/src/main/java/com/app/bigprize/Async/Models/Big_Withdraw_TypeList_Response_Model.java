package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Big_Withdraw_TypeList_Response_Model {
    private String isRateus;
    @Expose
    private String userToken;
    @Expose
    private String adFailUrl;
    @Expose
    private Big_Exit_Dialog exitDialog;
    @Expose
    private String homeNote;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String tigerInApp;
    @Expose
    private Big_Top_Ads topAds;
    @Expose
    private String country;
    @Expose
    private List<Big_Withdraw_List> withdrawList;
    public String getIsRateus() {
        return isRateus;
    }






    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public Big_Exit_Dialog getExitDialog() {
        return exitDialog;
    }

    public void setExitDialog(Big_Exit_Dialog exitDialog) {
        this.exitDialog = exitDialog;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
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

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }

    public List<Big_Withdraw_List> getWithdrawList() {
        return withdrawList;
    }

    public void setWithdrawList(List<Big_Withdraw_List> withdrawList) {
        this.withdrawList = withdrawList;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}

