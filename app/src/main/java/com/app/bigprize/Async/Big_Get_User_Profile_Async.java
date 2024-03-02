package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_UserProfileActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_User_Profile_Model;
import com.app.bigprize.Network.Big_ApiClient;
import com.app.bigprize.Network.Big_ApiInterface;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_AES_Cipher;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Big_Get_User_Profile_Async {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher pocAesCipher;

    public Big_Get_User_Profile_Async(final Activity activity) {
        this.activity = activity;
        pocAesCipher = new BIG_AES_Cipher();

        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("OE1WR1", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("U5T2EL", BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN());
            jObject.put("1U4N64", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("KG8HYT", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("WL9GXT", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("Z55CWE", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("J7AW8Q", Build.MODEL);
            jObject.put("Q3Q5OX", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
//            jObject.put("Q3Q5OX", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            //AppLogger.getInstance().e("Get User Profile ORIGINAL ==>", jObject.toString());

            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);

            BIG_AppLogger.getInstance().e("HomeDataObject ORIGINAL--)", "" + jObject.toString());
            BIG_AppLogger.getInstance().e("HomeDataObject ENCRYPTED--)", "" + pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            Call<Big_Api_Response> call = apiService.getUserProfile(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<Big_Api_Response>() {
                @Override
                public void onResponse(Call<Big_Api_Response> call, Response<Big_Api_Response> response) {
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<Big_Api_Response> call, Throwable t) {
                    BIG_Common_Utils.dismissProgressLoader();
                    if (!call.isCanceled()) {
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
            Big_User_Profile_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_User_Profile_Model.class);
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.User_Details, new Gson().toJson(responseModel.getUserDetails()));
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getUserDetails().getEarningPoint());
                    if (activity instanceof Big_UserProfileActivity) {
                        ((Big_UserProfileActivity) activity).onProfileDataChanged(responseModel);
                    }
                } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) {
                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

