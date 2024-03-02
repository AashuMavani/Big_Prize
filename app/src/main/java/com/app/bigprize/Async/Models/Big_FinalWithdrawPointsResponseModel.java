package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Big_FinalWithdrawPointsResponseModel implements Serializable {
    @Expose
    private String earningPoint;
    @Expose
    private String txnStatus;
    @Expose
    private String userToken;
    @Expose
    private String status;

    @Expose
    private String message;
    @Expose
    private String isMobileVerified;
    @SerializedName("adFailUrl")
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;
    @Expose
    private String nextWithdrawAmount;
    @Expose
    private String isRateus;
    @Expose
    private String txnID;
    @Expose
    private String deliveryDate;
    @Expose
    private String paymentPartner;
    @Expose
    private String shareText;



    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsMobileVerified() {
        return isMobileVerified;
    }

    public void setIsMobileVerified(String isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public String getNextWithdrawAmount() {
        return nextWithdrawAmount;
    }

    public void setNextWithdrawAmount(String nextWithdrawAmount) {
        this.nextWithdrawAmount = nextWithdrawAmount;
    }

    public String getIsRateus() {
        return isRateus;
    }

    public void setIsRateus(String isRateus) {
        this.isRateus = isRateus;
    }

    public String getTxnID() {
        return txnID;
    }

    public void setTxnID(String txnID) {
        this.txnID = txnID;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaymentPartner() {
        return paymentPartner;
    }

    public void setPaymentPartner(String paymentPartner) {
        this.paymentPartner = paymentPartner;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }
}
