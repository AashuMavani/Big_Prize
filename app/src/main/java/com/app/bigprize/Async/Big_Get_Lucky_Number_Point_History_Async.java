package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_LuckyNumberDrawHistoryActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Point_History_Model;
import com.app.bigprize.Network.Big_ApiClient;
import com.app.bigprize.Network.Big_ApiInterface;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_AES_Cipher;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Big_Get_Lucky_Number_Point_History_Async {
    private Activity activity;
    private JSONObject jObject;
   private BIG_AES_Cipher pocAesCipher;

    private String type;

    public Big_Get_Lucky_Number_Point_History_Async(final Activity activity, String type, String pageNo) {
        this.activity = activity;
         pocAesCipher = new BIG_AES_Cipher();

        this.type = type;
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("HLOICPL", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("HBVVU", BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN());
            jObject.put("JCBCCJSLI", pageNo);
            jObject.put("GHSWSGSQT", type);
            jObject.put("VGVVV", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("HVJVGG", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("HVGGKK", Build.MODEL);
            jObject.put("GGCHBH", Build.VERSION.RELEASE);
            jObject.put("HJVGHV", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("HVGVGHG", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("FFGXC", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("HJVGVG", BIG_Common_Utils.verifyInstallerId(activity));
            jObject.put("GCXDXDD", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            //AppLogger.getInstance().e("Get Lucky Number Point History ORIGINAL ==>", jObject.toString());
            //AppLogger.getInstance().e("Get Lucky Number Point History ENCRYPTED --)", "" + AES_Cipher.encrypt(POC_Constants.getMKEY(),POC_Constants.getMIV(), jObject.toString()));
            Call<Big_Api_Response> call = apiService.getLuckyNumberHistory(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            Big_Point_History_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_Point_History_Model.class);
            //AppLogger.getInstance().e("RESPONSE: " + type, "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                }
                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                    ((Big_LuckyNumberDrawHistoryActivity) activity).setData(type, responseModel);
                } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) {
                    ((Big_LuckyNumberDrawHistoryActivity) activity).setData(type, responseModel);
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