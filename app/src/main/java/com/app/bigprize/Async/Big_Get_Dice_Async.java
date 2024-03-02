package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.app.bigprize.Activity.Big_Dice_Roll_Activity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Dice_Data_Model;
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

public class Big_Get_Dice_Async {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher pocAesCipher;
    public Big_Get_Dice_Async(Big_Dice_Roll_Activity diceRollActivity) {
        this.activity = diceRollActivity;
        pocAesCipher = new BIG_AES_Cipher();

        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("AAVCMILS", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("HGYTYT", BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN());
            jObject.put("GVYVY", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("JKJHIOHU", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.FCMregId));
            jObject.put("HDXDTYT", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("FXSZZA", Build.MODEL);
            jObject.put("HFJFXXX", Build.VERSION.RELEASE);
            jObject.put("HGCTRH", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("XZSZRCTR", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("KGUIFT", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("HGTRDEAW", BIG_Common_Utils.verifyInstallerId(activity));
            jObject.put("GHVHG", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            Log.e("Get Dice ORIGINAL ==>", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
//            Log.e("Get Dice ENCRYPTED ==>", POC_AES_Cipher.encrypt(POC_Constants.getMKEY(),POC_Constants.getMIV(), jObject.toString()));
            Call<Big_Api_Response> call = apiService.getDiceData(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            Big_Dice_Data_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_Dice_Data_Model.class);
            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                //AppLogger.getInstance().e("Logout", "" + Constants.STATUS_LOGOUT);
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                    //AppLogger.getInstance().e("Status", "" + Constants.STATUS_SUCCESS);
                    ((Big_Dice_Roll_Activity) activity).setData(responseModel);
                } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
                    ((Big_Dice_Roll_Activity) activity).setData(responseModel);
                } else if (responseModel.getStatus().equals("2")) {
                    ((Big_Dice_Roll_Activity) activity).setData(responseModel);
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