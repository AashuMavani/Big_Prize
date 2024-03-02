package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Big_EverydayBonus implements Serializable {

    @SerializedName("lastClaimedDay")
    private String lastClaimedDay;

    @SerializedName("isTodayClaimed")
    private String isTodayClaimed;

    @SerializedName("data")
    private List<Big_EverydayBonusItem> data;

    @Expose
    private Big_Top_Ads topAds;

    @SerializedName("homeNote")
    private String homeNote;

    @Expose
    private String isWatchWebsite;
    @Expose
    private String watchWebsiteUrl;
    @Expose
    private String watchWebsiteTime;

    public String getLastClaimedDay() {
        return lastClaimedDay;
    }

    public List<Big_EverydayBonusItem> getData() {
        return data;
    }

    public void setLastClaimedDay(String lastClaimedDay) {
        this.lastClaimedDay = lastClaimedDay;
    }

    public String getIsTodayClaimed() {
        return isTodayClaimed;
    }

    public void setIsTodayClaimed(String isTodayClaimed) {
        this.isTodayClaimed = isTodayClaimed;
    }

    public void setData(List<Big_EverydayBonusItem> data) {
        this.data = data;
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

    public String getIsWatchWebsite() {
        return isWatchWebsite;
    }

    public void setIsWatchWebsite(String isWatchWebsite) {
        this.isWatchWebsite = isWatchWebsite;
    }

    public String getWatchWebsiteUrl() {
        return watchWebsiteUrl;
    }

    public void setWatchWebsiteUrl(String watchWebsiteUrl) {
        this.watchWebsiteUrl = watchWebsiteUrl;
    }

    public String getWatchWebsiteTime() {
        return watchWebsiteTime;
    }

    public void setWatchWebsiteTime(String watchWebsiteTime) {
        this.watchWebsiteTime = watchWebsiteTime;
    }
}
