package com.app.bigprize.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Big_App_Controller;
import com.app.bigprize.Network.Big_ApiClient;
import com.app.bigprize.Network.Big_ApiInterface;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_AES_Cipher;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Big_SplashScreenActivity extends AppCompatActivity {
    private Handler handler;
    private ImageView btm, btm1;
    private BroadcastReceiver appOpenAddLoadedBroadcast;
    private IntentFilter intentFilterActivities;
    private InstallReferrerClient referrerClient;
    private String referrer = "";


    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onCreate(savedInstanceState);
        BIG_Common_Utils.setDayNightTheme(Big_SplashScreenActivity.this);
        setContentView(R.layout.big_activity_splash_screen);
        btm = findViewById(R.id.btm);
//        btm1 = findViewById(R.id.btm1);

        if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen) > 1) {
            btm.setVisibility(View.VISIBLE);
//            btm1.setVisibility(View.VISIBLE);
        } else {
            btm.setVisibility(View.GONE);
//            btm1.setVisibility(View.GONE);
        }

        logSentFriendRequestEvent();


        if (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.appOpenDate).length() == 0 || !BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.appOpenDate).equals(BIG_Common_Utils.getCurrentDate())) {
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.appOpenDate, BIG_Common_Utils.getCurrentDate());
            BIG_SharePrefs.getInstance().putInt(BIG_SharePrefs.todayOpen, 1);
            BIG_SharePrefs.getInstance().putInt(BIG_SharePrefs.totalOpen, (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen) + 1));
        } else {
            BIG_SharePrefs.getInstance().putInt(BIG_SharePrefs.todayOpen, (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen) + 1));
        }
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null && (extras.containsKey("bundle") && extras.getString("bundle").trim().length() > 0)) {
                BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.isFromNotification, true);
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.notificationData, getIntent().getExtras().getString("bundle"));
            } else {
                BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.isFromNotification, false);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.isFromNotification, false);
        }

        if (!BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.isReferralChecked)) {
            BIG_AppLogger.getInstance().e("REFERRAL_URL", "REFERRAL_URL:+++++++++++++++++++ ");
            BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.isReferralChecked, true);
            referrerClient = InstallReferrerClient.newBuilder(this).build();
            // on below line we are starting its connection.
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    // this method is called when install referrer setup is finished.
                    switch (responseCode) {
                        // we are using switch case to check the response.
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // this case is called when the status is OK and
                            ReferrerDetails response = null;
                            try {
                                // on below line we are getting referrer details
                                // by calling get install referrer.
                                response = referrerClient.getInstallReferrer();
                                if (response != null) {
                                    // on below line we are getting our apps install referrer.
                                    referrer = response.getInstallReferrer();
                                    if (referrer != null) {
//                                      AppLogger.getInstance().e("REFERRAL_URL", "REFERRAL_URL: " + referrer);
                                        String[] pairs = referrer.split("&");
                                        Map<String, String> map = new LinkedHashMap<String, String>();
                                        for (String pair : pairs) {
                                            int idx = pair.indexOf("=");
                                            map.put(pair.substring(0, idx), pair.substring(idx + 1));
                                        }
//                                        AppLogger.getInstance().e("REFERRAL_MAP", "REFERRAL_SOURCE: " + map.get("utm_source"));
//                                        AppLogger.getInstance().e("REFERRAL_MAP", "REFERRAL_CONTENT: " + map.get("utm_content"));
                                        if (map.get("utm_source").equals("app_referral")) {
                                            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.ReferData, map.get("utm_content"));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // handling error case.
                                e.printStackTrace();
                            }
                            referrerClient.endConnection();
                            if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen) == 1) {
                                new GetHomeDataAsync(Big_SplashScreenActivity.this);
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection couldn't be established.
//                            Toast.makeText(SplashScreenActivity.this, "Fail to establish connection", Toast.LENGTH_SHORT).show();
                            // API not available on the current Play Store app.
//                            Toast.makeText(SplashScreenActivity.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                            if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen) == 1) {
                                new GetHomeDataAsync(Big_SplashScreenActivity.this);
                            }
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            });
        } else {
            if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen) == 1) {
                new GetHomeDataAsync(Big_SplashScreenActivity.this);
            }
        }
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try {
                    advertId = idInfo.getId();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.AdID, advertId);
            }
        };
        task.execute();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.FCMregId, token);
                    }
                });
        if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen) != 1) {
            new GetHomeDataAsync(Big_SplashScreenActivity.this);
        }

    }
    public void logSentFriendRequestEvent () {
        AppEventsLogger.newLogger(Big_SplashScreenActivity.this).logEvent("sentFriendRequest");
    }
    public class GetHomeDataAsync {
        private Activity activity;
        private JSONObject jObject;
        private BIG_AES_Cipher pocAesCipher;
        private String userToken;

        public GetHomeDataAsync(final Activity activity) {
            this.activity = activity;
            pocAesCipher = new BIG_AES_Cipher();
            try {
                jObject = new JSONObject();
                jObject.put("XOR4FQ", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.FCMregId));
                jObject.put("0APGT6", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
                jObject.put("7C444N", Build.MODEL);
                jObject.put("59LSYR", Build.VERSION.RELEASE);
                jObject.put("U1STPH", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
                jObject.put("CXWQNV", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
                jObject.put("xOR4FQ", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
                jObject.put("7FWFMI", BIG_Common_Utils.verifyInstallerId(activity));
                jObject.put("4HDXTT", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                jObject.put("WSZSJW", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    jObject.put("W6CEUG", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
                    jObject.put("FJSJN5TG", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken));
                    userToken = BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken);

                } else {
                    userToken = (BIG_Constants.getUSERTOKEN());
                    jObject.put("KLOPIOH", userToken);
                }
                BIG_AppLogger.getInstance().e("#home", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
                BIG_AppLogger.getInstance().e("#home", jObject.toString());


                int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
                jObject.put("RANDOM", n);
                Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
                Call<Big_Api_Response> call = apiService.getHomeData(userToken, String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

//                Call<Api_Response> call = apiService.getHomeData(pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

                call.enqueue(new Callback<Big_Api_Response>() {
                    @Override
                    public void onResponse(Call<Big_Api_Response> call, Response<Big_Api_Response> response) {
                        onPostExecute(response.body());
                        BIG_AppLogger.getInstance().e("AAA", response.body().toString());

                    }

                    @Override
                    public void onFailure(Call<Big_Api_Response> call, Throwable t) {
                        if (!call.isCanceled()) {
                            BIG_AppLogger.getInstance().d("TTTTTTTTT", "onFailure: ===" + "fgdfgdfg");
                            BIG_Common_Utils.Notify(activity, getString(R.string.app_name), BIG_Constants.msg_Service_Error, true);
                        }
                    }
                });
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        private void onPostExecute(Big_Api_Response response1) {
            try {

                Big_Response_Model response = new Gson().fromJson(new String(pocAesCipher.decrypt(response1.getEncrypt())), Big_Response_Model.class);
                BIG_Ads_Utils.adFailUrl = response.getAdFailUrl();
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.HomeData, new Gson().toJson(response));
                if (!BIG_Common_Utils.isStringNullOrEmpty(response.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, response.getUserToken());
                }
                if (response.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                    BIG_Common_Utils.doLogout(activity);
                } else if (response.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(response.getEarningPoint())) {
                        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, response.getEarningPoint());
                    }
                    if (!BIG_Common_Utils.isStringNullOrEmpty(response.getIsShowAdjump()) && response.getIsShowAdjump().matches("1")) {
                        Big_App_Controller app = (Big_App_Controller) getApplication();
                        app.initAdjump();
                    }
                    if (!BIG_Common_Utils.isStringNullOrEmpty(response.getIsShowPubScale()) && response.getIsShowPubScale().matches("1")) {
                        Big_App_Controller app = (Big_App_Controller) getApplication();
                        app.initPubScale();
                    }
                    if (!BIG_Common_Utils.isStringNullOrEmpty(response.getIsShowOfferToro()) && response.getIsShowOfferToro().matches("1")) {
                        Big_App_Controller app = (Big_App_Controller) getApplication();
                        app.initOfferToro();
                    }

                    if (!BIG_Common_Utils.isStringNullOrEmpty(response.getIsShowPubScale()) && response.getIsShowPubScale().matches("1")) {
                        Big_App_Controller app = (Big_App_Controller) getApplication();
                        app.initAppPrizeSDK();
                    }
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.isShowWhatsAppAuth, response.getIsShowWhatsAppAuth());
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.fakeEarningPoint, response.getFakeEarningPoint());



                    handler = new Handler();


                    if (BIG_Common_Utils.isShowAppLovinAds() && !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.isFromNotification) && BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        BIG_Common_Utils.InitializeApplovinSDK();
                        handler.postDelayed(Big_SplashScreenActivity.this::gotoMainActivity, 2000);
                    } else {
                        handler.postDelayed(Big_SplashScreenActivity.this::moveToMainScreen, 1000);
                        BIG_AppLogger.getInstance().e("Log", "move to main");
                    }
                } else if (response.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
                    BIG_Common_Utils.Notify(activity, getString(R.string.app_name), response.getMessage(), true);
                }
            } catch (Exception e) {
                BIG_AppLogger.getInstance().d("TAG", "onPostExecute: ======" + e.getMessage());
                e.printStackTrace();
            }
        }

    }


    private void gotoMainActivity() {
        try {
            removeHandler();
            BIG_Ads_Utils.showAppOpenAdd(Big_SplashScreenActivity.this, new BIG_Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (appOpenAddLoadedBroadcast != null) {
                        moveToMainScreen();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToMainScreen() {
        try {
            unRegisterReceivers();
            if (!BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) && !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_SKIPPED_LOGIN)) {
                startActivity(new Intent(Big_SplashScreenActivity.this, Big_LoginUserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                startActivity(new Intent(Big_SplashScreenActivity.this, Big_MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
//        //AppLogger.getInstance().e("SplashActivity", "onStop");
            unRegisterReceivers();
            if (referrerClient != null) {
                referrerClient.endConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerBroadcast() {
        appOpenAddLoadedBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //AppLogger.getInstance().e("SplashScreenActivity", "Broadcast Received==" + intent.getAction());
                if (intent.getAction().equals(BIG_Constants.APP_OPEN_ADD_DISMISSED)) {
                    removeHandler();
                    moveToMainScreen();
                } else {
                    gotoMainActivity();
                }
            }
        };
        intentFilterActivities = new IntentFilter();
        intentFilterActivities.addAction(BIG_Constants.APP_OPEN_ADD_LOADED);
        intentFilterActivities.addAction(BIG_Constants.APP_OPEN_ADD_DISMISSED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(appOpenAddLoadedBroadcast, intentFilterActivities, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(appOpenAddLoadedBroadcast, intentFilterActivities);
        }
    }

    private void unRegisterReceivers() {
        if (appOpenAddLoadedBroadcast != null) {
//            //AppLogger.getInstance().e("SplashScreenActivity", "Unregister Broadcast");
            unregisterReceiver(appOpenAddLoadedBroadcast);
            appOpenAddLoadedBroadcast = null;
        }
    }

    @Override
    public void onBackPressed() {
//        //AppLogger.getInstance().e("SplashScreenActivity", "onBackPress");
        removeHandler();
        unRegisterReceivers();
        super.onBackPressed();
        System.exit(0);
    }

    private void removeHandler() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}

