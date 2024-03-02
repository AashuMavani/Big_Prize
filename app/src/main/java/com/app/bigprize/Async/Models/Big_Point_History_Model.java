package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Big_Point_History_Model implements Serializable {

    @Expose
    private String adFailUrl;
    @Expose
    private String userToken;
    @Expose
    private String currentPage;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private Long totalIteam;
    @Expose
    private Long totalPage;
    @Expose
    private List<Big_Wallet_ListItem> walletList;

    @Expose
    private List<Big_Wallet_ListItem> data;

    @Expose
    private List<Big_Lucky_Number_MyHistory_Item> luckyNumberMyHistoryList;

    @Expose
    private List<Big_Lucky_Number_AllHistory_Item> luckyNumberAllHistoryList;

    @Expose
    private List<Big_Quiz_My_History_Item> quizMyHistoryList;

    @Expose
    private List<Big_Quiz_All_History_Item> quizAllHistoryList;

    @Expose
    private String earningPoint;

    @Expose
    private Big_Top_Ads topAds;

    @Expose
    private String homeNote;

    @Expose
    private Big_Mini_Ads miniAds;

    @Expose
    private String isShowInterstitial;


    @SerializedName("tigerInApp")
    private String tigerInApp;


    @Expose
    private Big_Wallet_ListItem payment;
    @Expose
    private String shareText;
    @Expose
    private String upiImage;

    public String getUpiImage() {
        return upiImage;
    }

    public void setUpiImage(String upiImage) {
        this.upiImage = upiImage;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public Big_Wallet_ListItem getPayment() {
        return payment;
    }

    public void setPayment(Big_Wallet_ListItem payment) {
        this.payment = payment;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
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

    public Long getTotalIteam() {
        return totalIteam;
    }

    public void setTotalIteam(Long totalIteam) {
        this.totalIteam = totalIteam;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List<Big_Wallet_ListItem> getWalletList() {
        return walletList;
    }

    public void setWalletList(List<Big_Wallet_ListItem> walletList) {
        this.walletList = walletList;
    }

    public List<Big_Wallet_ListItem> getData() {
        return data;
    }

    public void setData(List<Big_Wallet_ListItem> data) {
        this.data = data;
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

    public Big_Mini_Ads getMiniAds() {
        return miniAds;
    }

    public void setMiniAds(Big_Mini_Ads miniAds) {
        this.miniAds = miniAds;
    }

    public String getIsShowInterstitial() {
        return isShowInterstitial;
    }

    public void setIsShowInterstitial(String isShowInterstitial) {
        this.isShowInterstitial = isShowInterstitial;
    }

    public List<Big_Lucky_Number_MyHistory_Item> getLuckyNumberMyHistoryList() {
        return luckyNumberMyHistoryList;
    }

    public void setLuckyNumberMyHistoryList(List<Big_Lucky_Number_MyHistory_Item> luckyNumberMyHistoryList) {
        this.luckyNumberMyHistoryList = luckyNumberMyHistoryList;
    }

    public List<Big_Lucky_Number_AllHistory_Item> getLuckyNumberAllHistoryList() {
        return luckyNumberAllHistoryList;
    }

    public void setLuckyNumberAllHistoryList(List<Big_Lucky_Number_AllHistory_Item> luckyNumberAllHistoryList) {
        this.luckyNumberAllHistoryList = luckyNumberAllHistoryList;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<Big_Quiz_My_History_Item> getQuizMyHistoryList() {
        return quizMyHistoryList;
    }

    public void setQuizMyHistoryList(List<Big_Quiz_My_History_Item> quizMyHistoryList) {
        this.quizMyHistoryList = quizMyHistoryList;
    }

    public List<Big_Quiz_All_History_Item> getQuizAllHistoryList() {
        return quizAllHistoryList;
    }

    public void setQuizAllHistoryList(List<Big_Quiz_All_History_Item> quizAllHistoryList) {
        this.quizAllHistoryList = quizAllHistoryList;
    }
}
