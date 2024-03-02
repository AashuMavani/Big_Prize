package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_TaskDetailsInfoActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_ReferResponseModel;
import com.app.bigprize.Network.Big_ApiClient;
import com.app.bigprize.Network.Big_ApiInterface;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.R;
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

public class Big_SaveShareTaskAsync {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher pocAesCipher;
    private String type;

    public Big_SaveShareTaskAsync(final Activity activity, String taskId, String type) {
        this.activity = activity;
        pocAesCipher = new BIG_AES_Cipher();
        this.type = type;
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("SG7HJSG", taskId);
            jObject.put("SDJ7BSW5", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("VHDSWWDR", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken));
            jObject.put("SEGFGHFHG", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.FCMregId));
            jObject.put("GDRWXEES", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("VCDSWWFC", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("VCESWA", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("BVXSZWAS", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("CDFXESW", Build.MODEL);
            jObject.put("VCSRSSFCF", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);

            BIG_AppLogger.getInstance().e("Get User Profile ORIGINAL ==>", jObject.toString());
            BIG_AppLogger.getInstance().e("Get User Profile ORIGINAL ==>", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            Call<Big_Api_Response> call = apiService.shareTaskOffer(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            Big_ReferResponseModel responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_ReferResponseModel.class);
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                    if (activity instanceof Big_TaskDetailsInfoActivity) {
                        responseModel.setType(type);
                        BIG_AppLogger.getInstance().e("type==",""+responseModel.getType());
                        ((Big_TaskDetailsInfoActivity) activity).saveShareTaskOffer(responseModel);
                    }
                } else {
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
