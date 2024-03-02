package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Big_Login_Response_Model implements Serializable {
    @Expose
    private String userToken;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private Big_User_Details userDetails;
    @Expose
    private String adFailUrl;

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

    public Big_User_Details getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Big_User_Details userDetails) {
        this.userDetails = userDetails;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}

