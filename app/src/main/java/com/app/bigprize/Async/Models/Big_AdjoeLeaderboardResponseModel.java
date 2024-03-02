package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Big_AdjoeLeaderboardResponseModel implements Serializable {
	@SerializedName("homeNote")
	private String homeNote;

	@SerializedName("userToken")
	private String userToken;
	@SerializedName("winningNote")
	private String winningNote;
	@SerializedName("data")
	private List<Big_AdjoeLeaderboardItem> data;

	@SerializedName("historyData")
	private List<Big_AdjoeLeaderboardHistoryItem> historyData;

	@SerializedName("isShowInterstitial")
	private String isShowInterstitial;
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
	@SerializedName("miniAds")
	private Big_Mini_Ads miniAds;

	@SerializedName("todayDate")
	private String todayDate;

	@SerializedName("endDate")
	private String endDate;
	@Expose
	private Long totalIteam;
	@Expose
	private Long totalPage;
	@Expose
	private String currentPage;

	@Expose
	private String btnColor;
	@Expose
	private String btnName;
	@Expose
	private String btnTextColor;
	@Expose
	private String winPoint1;
	@Expose
	private String winPoint2;
	@Expose
	private String winPoint3;

	public String getWinPoint1() {
		return winPoint1;
	}

	public String getWinPoint2() {
		return winPoint2;
	}

	public String getWinPoint3() {
		return winPoint3;
	}

	public String getBtnTextColor() {
		return btnTextColor;
	}

	public String getBtnColor() {
		return btnColor;
	}

	public String getBtnName() {
		return btnName;
	}

	public Long getTotalIteam() {
		return totalIteam;
	}

	public Long getTotalPage() {
		return totalPage;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public String getIsShowInterstitial() {
		return isShowInterstitial;
	}

	public String getTodayDate() {
		return todayDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Big_Mini_Ads getMiniAds() {
		return miniAds;
	}

	public String getHomeNote() {
		return homeNote;
	}

	public String getUserToken() {
		return userToken;
	}

	public List<Big_AdjoeLeaderboardItem> getData() {
		return data;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public Big_Top_Ads getTopAds() {
		return topAds;
	}

	public String getAdFailUrl() {
		return adFailUrl;
	}

	public String getTigerInApp() {
		return tigerInApp;
	}

	public String getWinningNote() {
		return winningNote;
	}

	public List<Big_AdjoeLeaderboardHistoryItem> getHistoryData() {
		return historyData;
	}
}