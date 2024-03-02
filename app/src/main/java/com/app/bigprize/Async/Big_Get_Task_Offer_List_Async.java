package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_TasksCategoryTypeActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Task_OfferList_Response_Model;
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

public class Big_Get_Task_Offer_List_Async {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher pocAesCipher;
    private String type;

    public Big_Get_Task_Offer_List_Async(final Activity activity, String type, String pageNo) {
        this.activity = activity;
        pocAesCipher = new BIG_AES_Cipher();

        this.type = type;
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("JCHYYA", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("44OZGV", BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN());
            jObject.put("QHJBV9", pageNo);
            jObject.put("EAGZ0T", type);
            jObject.put("VKWROE", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("2BABDO", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("LV9HZA", Build.MODEL);
            jObject.put("XDA69X", Build.VERSION.RELEASE);
            jObject.put("FCLXGY", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("ORLBFB", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("749U3S", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("VKWROE", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);

          BIG_AppLogger.getInstance().e("RESPONSEsa: " + type, "" + type);
          BIG_AppLogger.getInstance().e("RESPONSE_stts: " + "", pageNo);
          BIG_AppLogger.getInstance().e("RESPONSE_stts: " + "", BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN());
          BIG_AppLogger.getInstance().e("RESPONSE_stts: " + "",pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            BIG_AppLogger.getInstance().e("DATA ORIGINAL)", "" + jObject.toString());



            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
//            Call<Task_OfferList_Response_Model> call = apiService.getTaskOfferList(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

            Call<Big_Api_Response> call = apiService.getTaskOfferList(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN(), String.valueOf(n), jObject.toString());
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
        Big_Task_OfferList_Response_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_Task_OfferList_Response_Model.class);
        BIG_AppLogger.getInstance().e("RESPONSEsa: " + type, "" + new Gson().toJson(responseModel));
        BIG_AppLogger.getInstance().e("RESPONSE_stts: " + "", responseModel.getStatus());
        if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
            BIG_Common_Utils.doLogout(activity);
        } else {
            BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
            }
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                if (activity instanceof Big_TasksCategoryTypeActivity) {
                    ((Big_TasksCategoryTypeActivity) activity).setData(responseModel);
                }
            } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
                BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
            } else if (responseModel.getStatus().equals("2")) {
                if (activity instanceof Big_TasksCategoryTypeActivity) {
                    ((Big_TasksCategoryTypeActivity) activity).setData(responseModel);
                }
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

