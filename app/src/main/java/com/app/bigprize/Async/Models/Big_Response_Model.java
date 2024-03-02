package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Big_Response_Model implements Serializable {

    @SerializedName("lovinInterstitialID")
    private List<String> lovinInterstitialID;
    @SerializedName("appVersion")
    private String appVersion;
    @SerializedName("userToken")
    private String userToken;

    @SerializedName("bottomGridTitle")
    @Expose
    private String bottomGridTitle;

    @SerializedName("bottomGridDesc")
    @Expose
    private String bottomGridDesc;
    @SerializedName("fakeEarningPoint")
    private String fakeEarningPoint;
    @SerializedName("pointValue")
    private String pointValue;
    @SerializedName("Menu_Banner")
    private Big_Menu_Banner menuBanner;
    @SerializedName("homeDialog")
    private Big_Home_Dialog homeDialog;
    @SerializedName("lovinRewardID")
    private List<String> lovinRewardID;

    @SerializedName("celebrationLottieUrl")
    private String celebrationLottieUrl;
    @SerializedName("homeDataList")
    private List<Big_Home_Data_List_Item> homeDataList;
    @SerializedName("exitDialog")
    private Big_Exit_Dialog exitDialog;
    @SerializedName("updateMessage")
    private String updateMessage;
    @SerializedName("status")
    private String status;
    @SerializedName("lovinNativeID")
    private List<String> lovinNativeID;
    @SerializedName("isShowNativeAdsOnAppExit")
    private String isShowNativeAdsOnAppExit;
    @SerializedName("storyView")
    private List<Big_Story_View_Item> storyView;
    @SerializedName("aboutUsUrl")
    private String aboutUsUrl;
    @SerializedName("homeSlider")
    private List<Big_Home_Slider_Item> homeSlider;
    @SerializedName("footerTaskIcon")
    private String footerTaskIcon;
    @SerializedName("isBackAdsInterstitial")
    private String isBackAdsInterstitial;
    @SerializedName("sideMenuList")
    private List<Big_Menu_ListItem> sideMenuList;
    @SerializedName("lovinBannerID")
    private List<String> lovinBannerID;
    @SerializedName("isAppLovinAdShow")
    private String isAppLovinAdShow;
    @SerializedName("privacyPolicy")
    private String privacyPolicy;
    @SerializedName("isForceUpdate")
    private String isForceUpdate;
    @SerializedName("appUrl")
    private String appUrl;
    @SerializedName("lovinAppOpenID")
    private List<String> lovinAppOpenID;
    @SerializedName("message")
    private String message;
    @SerializedName("termsConditionUrl")
    private String termsConditionUrl;
    @SerializedName("telegramUrl")
    private String telegramUrl;

    @SerializedName("youtubeUrl")
    private String youtubeUrl;

    @SerializedName("instagramUrl")
    private String instagramUrl;

    @SerializedName("homeNote")
    private String homeNote;

    @SerializedName("topAds")
    private Big_Top_Ads topAds;
    @SerializedName("packageInstallTrackingUrl")
    private String packageInstallTrackingUrl;
    @SerializedName("pid")
    private String pid;
    @SerializedName("offer_id")
    private String offer_id;
    @SerializedName("earningPoint")
    private String earningPoint;
    @SerializedName("hotOffersScreenNo")
    private String hotOffersScreenNo;
    @SerializedName("isShowHotOffers")
    private String isShowHotOffers;
    @SerializedName("nextWithdrawAmount")
    private String nextWithdrawAmount;
    @SerializedName("isShowWelcomeBonusPopup")
    private String isShowWelcomeBonusPopup;
    @SerializedName("welcomeBonus")
    private String welcomeBonus;
    @SerializedName("top_offers")
    private List<Big_TaskOffer> top_offers;
    @SerializedName("isShowPubScale")
    private String isShowPubScale;
    @SerializedName("isShowFooterTaskIcon")
    private String isShowFooterTaskIcon;
    @SerializedName("isShowAdjump")
    private String isShowAdjump;
    @SerializedName("isScanAndPayShow")
    private String isScanAndPayShow;
    @SerializedName("poweredByScanAndImage")
    private String poweredByScanAndImage;

    @SerializedName("withdrawLottie")
    private String withdrawLottie;

    @SerializedName("isShowOfferToro")
    private String isShowOfferToro;

    public String getIsShowOfferToro() {
        return isShowOfferToro;
    }
    public String getWithdrawLottie() {
        return withdrawLottie;
    }

    public void setWithdrawLottie(String withdrawLottie) {
        this.withdrawLottie = withdrawLottie;
    }

    public String getPoweredByScanAndImage() {
        return poweredByScanAndImage;
    }

    public void setPoweredByScanAndImage(String poweredByScanAndImage) {
        this.poweredByScanAndImage = poweredByScanAndImage;
    }

    public String getIsScanAndPayShow() {
        return isScanAndPayShow;
    }

    public void setIsScanAndPayShow(String isScanAndPayShow) {
        this.isScanAndPayShow = isScanAndPayShow;
    }

    public String getIsShowAdjump() {
        return isShowAdjump;
    }

    public void setIsShowAdjump(String isShowAdjump) {
        this.isShowAdjump = isShowAdjump;
    }

    public String getIsShowFooterTaskIcon() {
        return isShowFooterTaskIcon;
    }

    public void setIsShowFooterTaskIcon(String isShowFooterTaskIcon) {
        this.isShowFooterTaskIcon = isShowFooterTaskIcon;
    }

    public String getIsShowPubScale() {
        return isShowPubScale;
    }

    public void setIsShowPubScale(String isShowPubScale) {
        this.isShowPubScale = isShowPubScale;
    }


    private String totalGameCount;

    public String getTotalGameCount() {
        return totalGameCount;
    }

    public void setTotalGameCount(String totalGameCount) {
        this.totalGameCount = totalGameCount;
    }

    public List<Big_TaskOffer> getTop_offers() {
        return top_offers;
    }

    public void setTop_offers(List<Big_TaskOffer> top_offers) {
        this.top_offers = top_offers;
    }

    public String getWelcomeBonus() {
        return welcomeBonus;
    }

    public void setWelcomeBonus(String welcomeBonus) {
        this.welcomeBonus = welcomeBonus;
    }

    public String getIsShowWelcomeBonusPopup() {
        return isShowWelcomeBonusPopup;
    }

    public void setIsShowWelcomeBonusPopup(String isShowWelcomeBonusPopup) {
        this.isShowWelcomeBonusPopup = isShowWelcomeBonusPopup;
    }

    public String getNextWithdrawAmount() {
        return nextWithdrawAmount;
    }

    public void setNextWithdrawAmount(String nextWithdrawAmount) {
        this.nextWithdrawAmount = nextWithdrawAmount;
    }

    public String getIsShowHotOffers() {
        return isShowHotOffers;
    }

    public void setIsShowHotOffers(String isShowHotOffers) {
        this.isShowHotOffers = isShowHotOffers;
    }

    public String getHotOffersScreenNo() {
        return hotOffersScreenNo;
    }

    public void setHotOffersScreenNo(String hotOffersScreenNo) {
        this.hotOffersScreenNo = hotOffersScreenNo;
    }

    public String getIsShowAdjoeLeaderboardIcon() {
        return isShowAdjoeLeaderboardIcon;
    }

    public void setIsShowAdjoeLeaderboardIcon(String isShowAdjoeLeaderboardIcon) {
        this.isShowAdjoeLeaderboardIcon = isShowAdjoeLeaderboardIcon;
    }

    private String isShowAdjoeLeaderboardIcon;
    private String isShowSurvey;
    @Expose
    private String points;
    @Expose
    private String adType;
    @Expose
    private String mainNote;
    private String remainGameCount;
    @Expose
    private String todayDate;
    @Expose
    private String lastDate;
    @Expose
    private String nextGameTimer;
    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;
    @Expose
    private String taskButton;
    @Expose
    private String taskId;
    @Expose
    private String helpVideoUrl;

    @Expose
    private String winningPoints;
    @Expose
    private List<Big_MinesFinderDataItem> data;
    @SerializedName("lovinSmallNativeID")
    private List<String> lovinSmallNativeID;

    public List<String> getLovinSmallNativeID() {
        return lovinSmallNativeID;
    }

    public void setLovinSmallNativeID(List<String> lovinSmallNativeID) {
        this.lovinSmallNativeID = lovinSmallNativeID;
    }

    public List<Big_MinesFinderDataItem> getData() {
        return data;
    }

    public void setData(List<Big_MinesFinderDataItem> data) {
        this.data = data;
    }

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskButton() {
        return taskButton;
    }

    public void setTaskButton(String taskButton) {
        this.taskButton = taskButton;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public String getIsTodayTaskCompleted() {
        return isTodayTaskCompleted;
    }

    public void setIsTodayTaskCompleted(String isTodayTaskCompleted) {
        this.isTodayTaskCompleted = isTodayTaskCompleted;
    }

    public String getNextGameTimer() {
        return nextGameTimer;
    }

    public void setNextGameTimer(String nextGameTimer) {
        this.nextGameTimer = nextGameTimer;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public String getRemainGameCount() {
        return remainGameCount;
    }

    public void setRemainGameCount(String remainGameCount) {
        this.remainGameCount = remainGameCount;
    }

    public String getMainNote() {
        return mainNote;
    }

    public void setMainNote(String mainNote) {
        this.mainNote = mainNote;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIsShowSurvey() {
        return isShowSurvey;
    }

    public void setIsShowSurvey(String isShowSurvey) {
        this.isShowSurvey = isShowSurvey;
    }

    @SerializedName("adFailUrl")
    private String adFailUrl;

    @SerializedName("loginSlider")
    private List<Big_Home_Slider_Item> loginSlider;
    @SerializedName("loginSliderWhatsApp")
    private List<Big_Home_Slider_Item> loginSliderWhatsApp;
    @SerializedName("giveawayCode")
    private String giveawayCode;
    @SerializedName("isShowGiveawayCode")
    private String isShowGiveawayCode;

    @SerializedName("tigerInApp")
    private String tigerInApp;

    @SerializedName("isShowWhatsAppAuth")
    private String isShowWhatsAppAuth;

    @SerializedName("rewardLabel")
    private String rewardLabel;

    @SerializedName("isShowAccountDeleteOption")
    private String isShowAccountDeleteOption;

    @SerializedName("adjoeKeyHash")
    private String adjoeKeyHash;

    public String getAdjoeIcon() {
        return adjoeIcon;
    }

    public void setAdjoeIcon(String adjoeIcon) {
        this.adjoeIcon = adjoeIcon;
    }

    @SerializedName("adjoeIcon")
    private String adjoeIcon;

    @SerializedName("imageAdjoeIcone")
    private String imageAdjoeIcone;

    @SerializedName("isshowPlaytimeIcone")
    private String isshowPlaytimeIcone;

    public String getImageAdjoeIcone() {
        return imageAdjoeIcone;
    }

    public String getIsshowPlaytimeIcone() {
        return isshowPlaytimeIcone;
    }

    public void setAppPrizeTargetCountryLocale(String appPrizeTargetCountryLocale) {
        this.appPrizeTargetCountryLocale = appPrizeTargetCountryLocale;
    }

    @SerializedName("appPrizeTargetCountryLocale")
    private String appPrizeTargetCountryLocale;
    @SerializedName("isShowAppPrize")
    private String isShowAppPrize;
    @SerializedName("isShowHomeBottomSheet")
    private String isShowHomeBottomSheet;
    @SerializedName("bottomGrid")
    @Expose
    private List<BottomGrid> bottomGrid;

    @SerializedName("bottomGridSpan")
    private String bottomGridSpan;

    public List<BottomGrid> getBottomGrid() {
        return bottomGrid;
    }

    public void setBottomGrid(List<BottomGrid> bottomGrid) {
        this.bottomGrid = bottomGrid;
    }

    public String getIsShowHomeBottomSheet() {
        return isShowHomeBottomSheet;
    }

    public void setIsShowHomeBottomSheet(String isShowHomeBottomSheet) {
        this.isShowHomeBottomSheet = isShowHomeBottomSheet;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Big_Menu_Banner getMenuBanner() {
        return menuBanner;
    }

    public Big_Home_Dialog getHomeDialog() {
        return homeDialog;
    }

    public List<String> getLovinRewardID() {
        return lovinRewardID;
    }

    public Big_Exit_Dialog getExitDialog() {
        return exitDialog;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public String getStatus() {
        return status;
    }

    public String getIsShowNativeAdsOnAppExit() {
        return isShowNativeAdsOnAppExit;
    }


    public List<Big_Story_View_Item> getStoryView() {
        return storyView;
    }

    public String getAboutUsUrl() {
        return aboutUsUrl;
    }

    public List<Big_Home_Slider_Item> getHomeSlider() {
        return homeSlider;
    }

    public String getIsBackAdsInterstitial() {
        return isBackAdsInterstitial;
    }

    public List<Big_Menu_ListItem> getSideMenuList() {
        return sideMenuList;
    }


    public String getIsAppLovinAdShow() {
        return isAppLovinAdShow;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public String getIsForceUpdate() {
        return isForceUpdate;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getTermsConditionUrl() {
        return termsConditionUrl;
    }

    public List<String> getLovinInterstitialID() {
        return lovinInterstitialID;
    }

    public List<String> getLovinNativeID() {
        return lovinNativeID;
    }

    public List<String> getLovinBannerID() {
        return lovinBannerID;
    }

    public List<String> getLovinAppOpenID() {
        return lovinAppOpenID;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public List<Big_Home_Data_List_Item> getHomeDataList() {
        return homeDataList;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public Big_Top_Ads getTopAds() {
        return topAds;
    }

    public String getFakeEarningPoint() {
        return fakeEarningPoint;
    }

    public String getPointValue() {
        return pointValue;
    }

    public String getPackageInstallTrackingUrl() {
        return packageInstallTrackingUrl;
    }

    public String getPid() {
        return pid;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public List<Big_Home_Slider_Item> getLoginSlider() {
        return loginSlider;
    }

    public String getIsShowWhatsAppAuth() {
        return isShowWhatsAppAuth;
    }

    public List<Big_Home_Slider_Item> getLoginSliderWhatsApp() {
        return loginSliderWhatsApp;
    }

    public void setLovinNativeID(List<String> lovinNativeID) {
        this.lovinNativeID = lovinNativeID;
    }

    public void setIsAppLovinAdShow(String isAppLovinAdShow) {
        this.isAppLovinAdShow = isAppLovinAdShow;
    }

    public void setLovinInterstitialID(List<String> lovinInterstitialID) {
        this.lovinInterstitialID = lovinInterstitialID;
    }

    public void setLovinRewardID(List<String> lovinRewardID) {
        this.lovinRewardID = lovinRewardID;
    }

    public void setLovinBannerID(List<String> lovinBannerID) {
        this.lovinBannerID = lovinBannerID;
    }

    public void setLovinAppOpenID(List<String> lovinAppOpenID) {
        this.lovinAppOpenID = lovinAppOpenID;
    }

    public void setLoginSlider(List<Big_Home_Slider_Item> loginSlider) {
        this.loginSlider = loginSlider;
    }

    public void setLoginSliderWhatsApp(List<Big_Home_Slider_Item> loginSliderWhatsApp) {
        this.loginSliderWhatsApp = loginSliderWhatsApp;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getCelebrationLottieUrl() {
        return celebrationLottieUrl;
    }

    public String getRewardLabel() {
        return rewardLabel;
    }

    public String getIsShowAccountDeleteOption() {
        return isShowAccountDeleteOption;
    }

    public String getAdjoeKeyHash() {
        return adjoeKeyHash;
    }

    public String getImageAdjoeIcon() {
        return imageAdjoeIcone;
    }

    public String getIsShowPlaytimeIcone() {
        return isshowPlaytimeIcone;
    }

    public String getFooterTaskIcon() {

        return footerTaskIcon;
    }

    public String getIsShowPlaytimeIcon() {
        return isshowPlaytimeIcone;
    }

    public String getFooterImage() {
        return imageAdjoeIcone;
    }

    public String getIsShowGiveawayCode() {
        return isShowGiveawayCode;
    }

    public String getGiveawayCode() {
        return giveawayCode;
    }


    public String getAppPrizeTargetCountryLocale() {
        return appPrizeTargetCountryLocale;
    }


    public String getIsShowAppPrize() {
        return isShowAppPrize;
    }

    public void setIsShowAppPrize(String isShowAppPrize) {
        this.isShowAppPrize = isShowAppPrize;
    }


    public String getBottomGridSpan() {
        return bottomGridSpan;
    }

    public void setBottomGridSpan(String bottomGridSpan) {
        this.bottomGridSpan = bottomGridSpan;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getBottomGridTitle() {
        return bottomGridTitle;
    }

    public void setBottomGridTitle(String bottomGridTitle) {
        this.bottomGridTitle = bottomGridTitle;
    }

    public void setFakeEarningPoint(String fakeEarningPoint) {
        this.fakeEarningPoint = fakeEarningPoint;
    }

    public void setPointValue(String pointValue) {
        this.pointValue = pointValue;
    }

    public void setMenuBanner(Big_Menu_Banner menuBanner) {
        this.menuBanner = menuBanner;
    }

    public void setHomeDialog(Big_Home_Dialog homeDialog) {
        this.homeDialog = homeDialog;
    }

    public void setCelebrationLottieUrl(String celebrationLottieUrl) {
        this.celebrationLottieUrl = celebrationLottieUrl;
    }

    public void setHomeDataList(List<Big_Home_Data_List_Item> homeDataList) {
        this.homeDataList = homeDataList;
    }

    public void setExitDialog(Big_Exit_Dialog exitDialog) {
        this.exitDialog = exitDialog;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIsShowNativeAdsOnAppExit(String isShowNativeAdsOnAppExit) {
        this.isShowNativeAdsOnAppExit = isShowNativeAdsOnAppExit;
    }

    public void setStoryView(List<Big_Story_View_Item> storyView) {
        this.storyView = storyView;
    }

    public void setAboutUsUrl(String aboutUsUrl) {
        this.aboutUsUrl = aboutUsUrl;
    }

    public void setHomeSlider(List<Big_Home_Slider_Item> homeSlider) {
        this.homeSlider = homeSlider;
    }

    public void setFooterTaskIcon(String footerTaskIcon) {
        this.footerTaskIcon = footerTaskIcon;
    }

    public void setIsBackAdsInterstitial(String isBackAdsInterstitial) {
        this.isBackAdsInterstitial = isBackAdsInterstitial;
    }

    public void setSideMenuList(List<Big_Menu_ListItem> sideMenuList) {
        this.sideMenuList = sideMenuList;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public void setIsForceUpdate(String isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTermsConditionUrl(String termsConditionUrl) {
        this.termsConditionUrl = termsConditionUrl;
    }

    public void setTelegramUrl(String telegramUrl) {
        this.telegramUrl = telegramUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public void setTopAds(Big_Top_Ads topAds) {
        this.topAds = topAds;
    }

    public void setPackageInstallTrackingUrl(String packageInstallTrackingUrl) {
        this.packageInstallTrackingUrl = packageInstallTrackingUrl;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public void setGiveawayCode(String giveawayCode) {
        this.giveawayCode = giveawayCode;
    }

    public void setIsShowGiveawayCode(String isShowGiveawayCode) {
        this.isShowGiveawayCode = isShowGiveawayCode;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public void setIsShowWhatsAppAuth(String isShowWhatsAppAuth) {
        this.isShowWhatsAppAuth = isShowWhatsAppAuth;
    }

    public void setRewardLabel(String rewardLabel) {
        this.rewardLabel = rewardLabel;
    }

    public void setIsShowAccountDeleteOption(String isShowAccountDeleteOption) {
        this.isShowAccountDeleteOption = isShowAccountDeleteOption;
    }

    public void setAdjoeKeyHash(String adjoeKeyHash) {
        this.adjoeKeyHash = adjoeKeyHash;
    }

    public void setImageAdjoeIcone(String imageAdjoeIcone) {
        this.imageAdjoeIcone = imageAdjoeIcone;
    }

    public void setIsshowPlaytimeIcone(String isshowPlaytimeIcone) {
        this.isshowPlaytimeIcone = isshowPlaytimeIcone;
    }

    public String getBottomGridDesc() {
        return bottomGridDesc;
    }

    public void setBottomGridDesc(String bottomGridDesc) {
        this.bottomGridDesc = bottomGridDesc;
    }
}