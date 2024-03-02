package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_WithdrawTypesListActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Redeem_Points;
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

public class Big_Redeem_Points_Async {
    private Activity activity;
    private JSONObject jObject;
   private BIG_AES_Cipher pocAesCipher;


    public Big_Redeem_Points_Async(final Activity activity, String id, String title, String withdrawType, String mobileNumber) {
        this.activity = activity;
         pocAesCipher = new BIG_AES_Cipher();
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("B6HX3H", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("MSUYMSJ", BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN());
            jObject.put("OB7NE2", id);
//            jObject.put("DB4D7C", title);
            jObject.put("BPUBSD", withdrawType);
            jObject.put("6V4JLT", mobileNumber);
            jObject.put("79ZOPB", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("DZ9BT6", Build.MODEL);
            jObject.put("FTDQ0F", Build.VERSION.RELEASE);
            jObject.put("533LNL", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("HWWVML", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("TY84X9", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("CVXPWH", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
//            jObject.put("CVXPWH", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            BIG_AppLogger.getInstance().e("BIG WITHD ORIGINAL ==>", jObject.toString());
            BIG_AppLogger.getInstance().e("BIG WITHD ORIGINAL11 ==>", title);
            BIG_AppLogger.getInstance().e("BIG WITHD ENCRYPTED 1111 ==>", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            Call<Big_Api_Response> call = apiService.redeemPoints(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

            call.enqueue(new Callback<Big_Api_Response>() {
                @Override
                public void onResponse(Call<Big_Api_Response> call, Response<Big_Api_Response> response) {
                    onPostExecute(response.body());
                    BIG_AppLogger.getInstance().e("phone1",""+response);

                }

                @Override
                public void onFailure(Call<Big_Api_Response> call, Throwable t) {
                    BIG_Common_Utils.dismissProgressLoader();
                    if (!call.isCanceled()) {
                        BIG_AppLogger.getInstance().e("phone2",""+call.isCanceled());
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
            Big_Redeem_Points responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_Redeem_Points.class);

            BIG_AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                ((Big_WithdrawTypesListActivity) activity).checkWithdraw(responseModel);
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

