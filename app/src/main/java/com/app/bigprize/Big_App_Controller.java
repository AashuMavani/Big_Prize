package com.app.bigprize;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.utils.BIG_Activity_Manager;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.appsamurai.appsprize.AppReward;
import com.appsamurai.appsprize.AppsPrize;
import com.appsamurai.appsprize.AppsPrizeListener;
import com.appsamurai.appsprize.config.AppsPrizeConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.leadmint.adjump.adjump;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.pubscale.sdkone.offerwall.OfferWall;
import com.pubscale.sdkone.offerwall.OfferWallConfig;
import com.pubscale.sdkone.offerwall.models.OfferWallInitListener;
import com.pubscale.sdkone.offerwall.models.OfferWallListener;
import com.pubscale.sdkone.offerwall.models.errors.InitError;

import org.json.JSONArray;

import java.util.List;
import java.util.Locale;

import io.adjoe.sdk.Adjoe;

public class Big_App_Controller extends Application {

    public static Context mContext;
    private static final String ONESIGNAL_APP_ID = "ff05e043-dec7-4b46-b45f-f97fc9295c6f";
    public static BroadcastReceiver packageInstallBroadcast;


    private static String l;

    static {
        System.loadLibrary("bigprize");
    }

    private adjump adjump;


    @Override
    public void onCreate() {
        super.onCreate();
        if (Adjoe.isAdjoeProcess()) {
            // the method is executed on the adjoe process
            return;
        }
        mContext = this;


        BIG_Activity_Manager activityManager = new BIG_Activity_Manager();
        registerActivityLifecycleCallbacks(activityManager);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(activityManager);

        FirebaseMessaging.getInstance().subscribeToTopic("global");
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            FirebaseMessaging.getInstance().subscribeToTopic("globalV" + version);
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.AppVersion, version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.getDebug().setLogLevel(LogLevel.NONE);
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);
    }

    public static Context getContext() {

        return mContext;
    }


    public void initPubScale() {
        String userID = !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? "GU_" + BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000) : BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId);
        Bitmap bg = Bitmap.createBitmap(600, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        canvas.drawColor(getColor(R.color.pink));
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.big_app_icon);
        OfferWallConfig offerWallConfig =
                new OfferWallConfig.Builder(getContext(), "57565492")
                        .setUniqueId(userID) //optional, used to represent the user of your application
                        .setLoaderBackgroundBitmap(bg)//optional
                        .setLoaderForegroundBitmap(icon)//optional
                        .setFullscreenEnabled(false)//optional
                        .build();
        try {
            OfferWall.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OfferWall.init(offerWallConfig, new OfferWallInitListener() {
            @Override
            public void onInitSuccess() {
                BIG_AppLogger.getInstance().e("INIT", "PUBSCALE SUCCESS==========");
            }

            @Override
            public void onInitFailed(InitError initError) {
                BIG_AppLogger.getInstance().e("INIT", "PUBSCALE FAIL==========");
            }
        });
    }

    public void initAdjump() {
        String userID = !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? "GU_" + BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000) : BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId);
        adjump = new adjump(mContext, "1036", "1017", userID);
    }

    public adjump getAdjump() {
        return adjump;
    }

    public void initAppPrizeSDK() {
        String APPS_PRIZE_APP_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjM2fQ.b8v6DvL-oDtYCrGpMzzFxyuET_uyGLBGBqahLVQM8rQ";
        String userID = !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? "GU_" + BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000) : BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId);
        Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        AppsPrizeConfig config;
        if (responseMain != null && !BIG_Common_Utils.isStringNullOrEmpty(responseMain.getAppPrizeTargetCountryLocale())) {
            Locale locale = null;
            try {
                locale = new Locale("", responseMain.getAppPrizeTargetCountryLocale());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (locale != null) {
                config = new AppsPrizeConfig.Builder()
                        .setUserId(userID)
                        .setTestMode(false)
                        .setCountry(locale)
                        .build(APPS_PRIZE_APP_TOKEN, BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            } else {
                config = new AppsPrizeConfig.Builder()
                        .setUserId(userID)
                        .setTestMode(false)
                        .build(APPS_PRIZE_APP_TOKEN, BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            }
        } else {
            config = new AppsPrizeConfig.Builder()
                    .setUserId(userID)
                    .setTestMode(false)
                    .build(APPS_PRIZE_APP_TOKEN, BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
        }
        AppsPrize.initialize(getApplicationContext(), config, new AppsPrizeListener() {
            @Override
            public void onInitialize() {
//                KGVD_AppLogger.getInstance().d("[AppsPrize]", "AppsPrize:onInitialize");
            }

            @Override
            public void onInitializeFailed(@NonNull String errorMessage) {
//                KGVD_AppLogger.getInstance().d("[AppsPrize]", "AppsPrize:onInitializeFailed: err: " + errorMessage);
            }

            @Override
            public void onRewardUpdate(@NonNull List<AppReward> rewards) {
//                KGVD_AppLogger.getInstance().d("[AppsPrize]", "AppsPrize:onRewardUpdate: " + rewards);
            }
        });
    }
    public void initOfferToro() {
        String secretKey = "1c9eccd5b64f4e6ef87524e100b3f882";
        String appId = "15492";
        String userID = !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? "GU_" + BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000) : BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId);

        // initialize offerwall
        com.torox.sdk.OfferWall.getInstance().setConfig(appId, secretKey, userID);
        com.torox.sdk.OfferWall.getInstance().setOfferWallListener(new com.torox.sdk.OfferWallListener() {
            @Override
            public void onOfferWallInitSuccess() {

            }

            @Override
            public void onOfferWallInitFail(String s) {

            }

            @Override
            public void onOfferWallOpened() {
                BIG_Activity_Manager.isShowAppOpenAd = false;
                BIG_Common_Utils.logFirebaseEvent(getContext(), "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Bigprize_OfferToro", "Offerwall Opened");
            }

            @Override
            public void onOfferWallOfferClicked(String s) {

            }

            @Override
            public void onOfferWallCredited(double v, double v1) {

            }

            @Override
            public void onOfferLoadFail(String s) {

            }

            @Override
            public void onOfferWallClosed() {

            }

            @Override
            public void onOfferWallGetUserCredits(JSONArray jsonArray) {

            }

            @Override
            public void onOfferWallGetUserCreditsError(String s) {

            }

            @Override
            public void onOfferWallMissingCreditsError() {

            }
        });
    }
}
