package com.app.bigprize.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.app.bigprize.Activity.Big_SplashScreenActivity;
import com.appsamurai.appsprize.ui.AppsPrizeActivity;
import com.leadmint.adjump.WebViewActivity;
import com.pubscale.sdkone.offerwall.ui.OfferWallActivity;

import io.adjoe.sdk.AdjoeActivity;

public class BIG_Activity_Manager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private int numberActivitiesStart = 0;
    public static int timeToWatchInSeconds;
    public static boolean isStartTimer = false;
    private String TAG = this.getClass().getSimpleName();
    public static boolean appInForeground, isShowAppOpenAd = true;

    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        try {
            //AppLogger.getInstance().e(TAG, "onMoveToForeground===  app went to foreground");
//            if (!(BIG_Ads_Utils.getCurrentActivity() instanceof Big_SplashScreenActivity)
//                    && !(BIG_Ads_Utils.getCurrentActivity() instanceof AdjoeActivity) && isShowAppOpenAd)
//                BIG_Ads_Utils.showAppOpenAdd(BIG_Ads_Utils.getCurrentActivity(), null);
//            isShowAppOpenAd = true;
            if (!(BIG_Ads_Utils.getCurrentActivity() instanceof AppsPrizeActivity) && !(BIG_Ads_Utils.getCurrentActivity() instanceof WebViewActivity) && !(BIG_Ads_Utils.getCurrentActivity() instanceof OfferWallActivity) && !(BIG_Ads_Utils.getCurrentActivity() instanceof Big_SplashScreenActivity) && !(BIG_Ads_Utils.getCurrentActivity() instanceof AdjoeActivity) && isShowAppOpenAd)
                BIG_Ads_Utils.showAppOpenAdd(BIG_Ads_Utils.getCurrentActivity(), null);
            isShowAppOpenAd = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        try {
            BIG_Ads_Utils.setCurrentActivity(activity);
            if (numberActivitiesStart == 0) {
                // The application come from background to foreground
                //AppLogger.getInstance().e(TAG, "App_Controller status > onActivityStarted:  app went to foreground");
                if (!appInForeground) {
                    appInForeground = true;
                }
            }
            numberActivitiesStart++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        BIG_Ads_Utils.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        try {
            numberActivitiesStart--;
            if (numberActivitiesStart == 0) {
                // The application go from foreground to background
                appInForeground = false;
                //AppLogger.getInstance().e(TAG, "App_Controller status > onActivityStopped: app went to background");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

}
