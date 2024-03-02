package com.app.bigprize.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Big_Withdraw_Types_Response_Model {
    @SerializedName("homeSlider")
    private List<Big_Home_Slider_Item> homeSlider;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private List<Big_Withdraw_Type> Type;
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

    public List<Big_Withdraw_Type> getType() {
        return Type;
    }

    public void setType(List<Big_Withdraw_Type> type) {
        Type = type;
    }

    public List<Big_Home_Slider_Item> getHomeSlider() {
        return homeSlider;
    }

    public void setHomeSlider(List<Big_Home_Slider_Item> homeSlider) {
        this.homeSlider = homeSlider;
    }

    public String getUserToken() {
        return userToken;
    }
}

