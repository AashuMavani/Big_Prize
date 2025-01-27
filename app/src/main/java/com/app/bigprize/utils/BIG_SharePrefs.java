package com.app.bigprize.utils;

import android.content.Context;

import com.app.bigprize.Big_App_Controller;
import com.github.rtoshiro.secure.SecureSharedPreferences;



public class BIG_SharePrefs {
    public static final String rewardDialogShownDate = "rewardDialogShownDate";
    public static String RATE_POPUP_SHOWN_COUNT = "RatePopupShownCount";
    private SecureSharedPreferences sharedPreferences;
    public static String IS_WELCOME_POPUP_SHOWN = "isWelcomePopupShown";
    private static BIG_SharePrefs instance = null;
    public static String RATE_POPUP_MOVE_TO_PLAY_STORE_COUNT = "ratePopupMoveToPlayStoreCount";
    public static String RATE_POPUP_LAST_SHOW_DATE = "ratePopupLastShowDate";
    public static String IS_USER_CONSENT_ACCEPTED = "isUserConsentAccepted";

    public static String IS_LOGIN = "isLogin";
    public static String IS_REVIEW_GIVEN = "isReviewGiven";
    public static String IS_SKIPPED_LOGIN = "isSkippedLogin";
    public static String IS_FIRST_LOGIN = "isFirstLogin";
    public static final String ReferData = "ReferData";
    public static final String FLASH = "FLASH";
    public static final String CAM_ID ="CAM_ID" ;

    public static final String User_Details = "User_Details";
    public static final String userId = "userId";
    public static final String userToken = "userToken";
    public static final String AdID = "AdID";
    public static final String EarnedPoints = "EarnedPoints";
    public static final String isShowWhatsAppAuth = "isShowWhatsAppAuth";
    public static final String fakeEarningPoint = "fakeEarningPoint";
    public static final String LastSpinIndex = "LastSpinIndex";
    public static final String FCMregId = "FCMregId";
    public static final String AppVersion = "AppVersion";
    public static final String HomeData = "HomeData";
    public static final String totalOpen = "totalOpen";
    public static final String todayOpen = "todayOpen";
    public static final String appOpenDate = "appOpenDate";
    public static final String homeDialogShownDate = "homeDialogShownDate";
    public static final String pointHistoryMiniAdShownDate = "pointHistoryMiniAdShownDate";
    public static final String isFromNotification = "isFromNotification";
    public static final String notificationData = "notificationData";

    public static final String isReferralChecked = "isReferralChecked";
    public static final String IS_SHOW_SCAN_PAY_INFO = "isShowScanAndPayInfo";

    /**
     * Saving data in shared preferences which will store life time of Application
     */
    public BIG_SharePrefs(Context context) {
        this.sharedPreferences = new SecureSharedPreferences(Big_App_Controller.mContext);
    }

    public static BIG_SharePrefs getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return new BIG_SharePrefs(Big_App_Controller.mContext);
        }
    }

    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public String getEarningPointString() {
        return sharedPreferences.getString(BIG_SharePrefs.EarnedPoints, sharedPreferences.getString(BIG_SharePrefs.fakeEarningPoint, "0"));
    }

    public void putInt(String key, Integer val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    public void putBoolean(String key, Boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void clearSharePrefs() {
        sharedPreferences.edit().clear().apply();
    }

    public void putFloat(String key, float val) {
        sharedPreferences.edit().putFloat(key, val).apply();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0.0f);
    }


}
