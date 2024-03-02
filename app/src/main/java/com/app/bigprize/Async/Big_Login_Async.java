package com.app.bigprize.Async;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_MainActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Login_Response_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Big_App_Controller;
import com.app.bigprize.Network.Big_ApiClient;
import com.app.bigprize.Network.Big_ApiInterface;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.Value.BIG_Request_Model;
import com.app.bigprize.utils.BIG_AES_Cipher;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Big_Login_Async {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher pocAesCipher;

    public Big_Login_Async(final Activity activity, BIG_Request_Model requestModel) {
        this.activity = activity;

         pocAesCipher=new BIG_AES_Cipher();
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();

            jObject.put("7376MQ", requestModel.getFirstName().trim());
            jObject.put("F3UOHD", requestModel.getLastName().trim());
            jObject.put("EQNMGQ", requestModel.getEmailId().trim());
            jObject.put("5ZOLGQ", requestModel.getProfileImage().trim());
            jObject.put("UWF03H", requestModel.getReferralCode().trim());
            jObject.put("JPM3XJ", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("W7BDS2", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.FCMregId));
            jObject.put("ADCXGV", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));

            if (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.ReferData).length() > 0) {
                jObject.put("3JAFZZ", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.ReferData));
            } else {
                jObject.put("3JAFZZ", "");
            }
            jObject.put("JKSDGH", "1");// 1 = android
            jObject.put("XG2Z5J", Build.MODEL);
            jObject.put("MNQXHQ", Build.VERSION.RELEASE);
            jObject.put("H7GWQT", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("3NG19B", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("DDOTD6", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("AW8HDW", BIG_Common_Utils.verifyInstallerId(activity));
            jObject.put("GHTREH", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            BIG_AppLogger.getInstance().e("Login DATA ORIGINAL==", "" + jObject.toString());
            BIG_AppLogger.getInstance().e("LOGIN DATA ENCRYPTED==", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);


            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            Call<Big_Api_Response> call = apiService.loginUser(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<Big_Api_Response>() {
                @Override
                public void onResponse(Call<Big_Api_Response> call, Response<Big_Api_Response> response) {
                    BIG_AppLogger.getInstance().e("login",""+response.body());
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<Big_Api_Response> call, Throwable t) {
                    BIG_Common_Utils.dismissProgressLoader();
                    if (!call.isCanceled()) {
                        BIG_AppLogger.getInstance().e("login",""+call.isCanceled());
                        BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), BIG_Constants.msg_Service_Error, false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            BIG_Common_Utils.dismissProgressLoader();
        }
    }

    private void onPostExecute(Big_Api_Response response) {
        try {
            BIG_Common_Utils.dismissProgressLoader();
            Big_Login_Response_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_Login_Response_Model.class);
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();

                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {

                    BIG_AppLogger.getInstance().e("login",""+responseModel.getStatus());

                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.User_Details, new Gson().toJson(responseModel.getUserDetails()));
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userId, responseModel.getUserDetails().getUserId());
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserDetails().getUserToken());
                    BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_LOGIN, true);
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getUserDetails().getEarningPoint());
                    Intent in = new Intent(activity, Big_MainActivity.class).putExtra("isFromLogin", true);
                    try {
                        Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowSurvey()) && responseMain.getIsShowSurvey().matches("1")) {
                            Big_App_Controller app = (Big_App_Controller) activity.getApplication();

                        }
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAdjump()) && responseMain.getIsShowAdjump().matches("1")) {
                            Big_App_Controller app = (Big_App_Controller)  activity.getApplication();
                            app.initAdjump();
                        }
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAppPrize()) && responseMain.getIsShowAppPrize().matches("1")) {
                            Big_App_Controller app = (Big_App_Controller) activity.getApplication();
                            app.initAppPrizeSDK();
                        }
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowPubScale()) && responseMain.getIsShowPubScale().matches("1")) {
                            Big_App_Controller app = (Big_App_Controller)activity.getApplication();
                            app.initPubScale();
                        }
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowOfferToro()) && responseMain.getIsShowOfferToro().matches("1")) {
                            Big_App_Controller app = (Big_App_Controller) activity.getApplication();
                            app.initOfferToro();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    activity.startActivity(in);
                    activity.finishAffinity();

                } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
//                String msg = "ORIGINAL: \n\n" + jObject.toString() + "\n\n\nCAPTCHA:\n\n" + captcha + "\n\n\nENCRYPTED: \n\n" + AES_Cipher.encrypt(POC_Constants.getMKEY(),POC_Constants.getMIV(), jObject.toString());
                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) {
                    BIG_AppLogger.getInstance().e("login",""+responseModel.getStatus());
                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

