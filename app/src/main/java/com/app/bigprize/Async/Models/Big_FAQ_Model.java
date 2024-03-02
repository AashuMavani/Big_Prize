package com.app.bigprize.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Big_FAQ_Model implements Serializable {
    @SerializedName("homeNote")
    private String homeNote;

    @SerializedName("userToken")
    private String userToken;

    @SerializedName("data")
    private List<Big_FAQ_ListItem> FAQList;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("topAds")
    private Big_Top_Ads topAds;

    @SerializedName("adFailUrl")
    private String adFailUrl;

    @SerializedName("tigerInApp")
    private String tigerInApp;
    @SerializedName("query")
    private String query;
    @SerializedName("reply")
    private String reply;
    @SerializedName("image")
    private String image;
    @SerializedName("ticketId")
    private String ticketId;

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public List<Big_FAQ_ListItem> getFAQList() {
        return FAQList;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public void setFAQList(List<Big_FAQ_ListItem> FAQList) {
        this.FAQList = FAQList;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public String getUserToken() {
        return userToken;
    }
}
