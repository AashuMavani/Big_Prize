package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Big_Giveaway_Model {
    @Expose
    private String earningPoint;
    @Expose
    private Big_Top_Ads topAds;
    @Expose
    private String userToken;

    @Expose
    private String minPayAmountForCharges;
    @Expose
    private String homeNote;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String note;
    @Expose
    private String couponPoints;
    @Expose
    private String helpVideoUrl;

    @Expose
    private List<Big_Icon_ListItem> socialMedia;
    @Expose
    private String adFailUrl;

    @SerializedName("tigerInApp")
    private String tigerInApp;
    @Expose
    private String screen_no;
    private String btn_name;
    @Expose
    private String upiId;
    @Expose
    private String recipientName;
    @Expose
    private String extraCharge;
    @Expose
    private String upiImage;
    @Expose
    private String minPayAmount;
    @Expose
    private String paymentAmount;

    public String getScreen_no() {
        return screen_no;
    }

    public void setScreen_no(String screen_no) {
        this.screen_no = screen_no;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(String extraCharge) {
        this.extraCharge = extraCharge;
    }

    public String getUpiImage() {
        return upiImage;
    }

    public void setUpiImage(String upiImage) {
        this.upiImage = upiImage;
    }

    public String getMinPayAmount() {
        return minPayAmount;
    }

    public void setMinPayAmount(String minPayAmount) {
        this.minPayAmount = minPayAmount;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getBtn_name() {
        return btn_name;
    }

    public void setBtn_name(String btn_name) {
        this.btn_name = btn_name;
    }

    public String getScreenNo() {
        return screen_no;
    }

    public void setScreenNo(String screenNo) {
        screen_no = screenNo;
    }

    public List<Big_Home_Data_Item> getGiveawayCodeList() {
        return giveawayCodeList;
    }

    public void setGiveawayCodeList(List<Big_Home_Data_Item> giveawayCodeList) {
        this.giveawayCodeList = giveawayCodeList;
    }

    private List<Big_Home_Data_Item> giveawayCodeList;

    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Big_Icon_ListItem> getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(List<Big_Icon_ListItem> socialPlatforms) {
        this.socialMedia = socialPlatforms;
    }

    public String getCouponPoints() {
        return couponPoints;
    }

    public void setCouponPoints(String couponPoints) {
        this.couponPoints = couponPoints;
    }

    public String getHelpVideoUrl() {
        return helpVideoUrl;
    }

    public void setHelpVideoUrl(String helpVideoUrl) {
        this.helpVideoUrl = helpVideoUrl;
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

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public String getMinPayAmountForCharges() {
        return minPayAmountForCharges;
    }

    public void setMinPayAmountForCharges(String minPayAmountForCharges) {
        this.minPayAmountForCharges = minPayAmountForCharges;
    }
}

