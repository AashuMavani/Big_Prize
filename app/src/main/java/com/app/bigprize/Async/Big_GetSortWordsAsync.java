package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_SortWordsActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_SortWordResponseModel;
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

public class Big_GetSortWordsAsync {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher cipher;

    public Big_GetSortWordsAsync(final Activity activity) {
        this.activity = activity;
        cipher = new BIG_AES_Cipher();
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("E4FYGGUBG", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("GYDRDDR", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken));
            jObject.put("BVDXDSZ", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.FCMregId));
            jObject.put("CXAWEX", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("NBCFXERSE", Build.MODEL);
            jObject.put("VCDXEBB", Build.VERSION.RELEASE);
            jObject.put("FCRDECF", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("BVDFXXS", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("BDSZBCF", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("VCDXX", BIG_Common_Utils.verifyInstallerId(activity));
            jObject.put("VGVGHBHB", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            BIG_AppLogger.getInstance().e("Get Word Sorting ORIGINAL ==>", jObject.toString());
            BIG_AppLogger.getInstance().e("Get Word Sorting ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            Call<Big_Api_Response> call = apiService.getWordSorting(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
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
            BIG_Common_Utils.dismissProgressLoader();
            e.printStackTrace();
        }
    }

    private void onPostExecute(Big_Api_Response response) {
        try {
            BIG_Common_Utils.dismissProgressLoader();
            Big_SortWordResponseModel responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Big_SortWordResponseModel.class);
            BIG_AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS) || responseModel.getStatus().equals("2")) {
                    ((Big_SortWordsActivity) activity).setData(responseModel);
                } else {
                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), true);
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
