package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Big_Task_OfferList_Response_Model {
    @Expose
    private String userToken;
    @Expose
    private String adFailUrl;
    @Expose
    private String bgColor;
    @Expose
    private String currentPage;
    @Expose
    private String homeNote;

    @Expose
    private Big_Top_Ads topAds;
    @Expose
    private List<Big_Home_Slider_Item> homeSlider;
    @Expose
    private List<Big_TaskOffer> horizontalTaskList;
    @Expose
    private String message;
    @Expose
    private String screenNo;
    @Expose
    private String status;
    @Expose
    private List<Big_TaskOffer> taskOffers;
    @Expose
    private String tigerInApp;
    @Expose
    private Long totalIteam;

    @Expose
    private Long highPoinCount;
    @Expose
    private Long totalPage;
    @Expose
    private String url;

    @Expose
    private String earningPoint;

    @Expose
    private List<Big_Task_Category> taskCategoryList;
    @Expose
    private String horizontalTaskLabel;



    @Expose
    private String topBannerImage;
    @Expose
    private String topBannerImageScreenNo;

    public String getTopBannerImageScreenNo() {
        return topBannerImageScreenNo;
    }

    public void setTopBannerImageScreenNo(String topBannerImageScreenNo) {
        this.topBannerImageScreenNo = topBannerImageScreenNo;
    }

    public String getTopBannerImage() {
        return topBannerImage;
    }

    public void setTopBannerImage(String topBannerImage) {
        this.topBannerImage = topBannerImage;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public List<Big_Home_Slider_Item> getHomeSlider() {
        return homeSlider;
    }

    public void setHomeSlider(List<Big_Home_Slider_Item> homeSlider) {
        this.homeSlider = homeSlider;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Big_TaskOffer> getTaskOffers() {
        return taskOffers;
    }

    public void setTaskOffers(List<Big_TaskOffer> taskOffers) {
        this.taskOffers = taskOffers;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public List<Big_Task_Category> getTaskCategoryList() {
        return taskCategoryList;
    }

    public void setTaskCategoryList(List<Big_Task_Category> taskCategoryList) {
        this.taskCategoryList = taskCategoryList;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<Big_TaskOffer> getHorizontalTaskList() {
        return horizontalTaskList;
    }

    public void setHorizontalTaskList(List<Big_TaskOffer> horizontalTaskList) {
        this.horizontalTaskList = horizontalTaskList;
    }

    public String getHorizontalTaskLabel() {
        return horizontalTaskLabel;
    }

    public void setHorizontalTaskLabel(String horizontalTaskLabel) {
        this.horizontalTaskLabel = horizontalTaskLabel;
    }

    public Long getHighPoinCount() {
        return highPoinCount;
    }

    public void setHighPoinCount(Long highPoinCount) {
        this.highPoinCount = highPoinCount;
    }
}
