package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_EarningOptionsActivity;
import com.app.bigprize.Activity.Big_MainActivity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Model_MinesweeperData;
import com.app.bigprize.Network.Big_ApiClient;
import com.app.bigprize.Network.Big_ApiInterface;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_AES_Cipher;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.gson.Gson;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Big_GetWalletBalanceAsync {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher cipher;

    public Big_GetWalletBalanceAsync(final Activity activity) {
        this.activity = activity;
        cipher = new BIG_AES_Cipher();
        try {
            jObject = new JSONObject();
            jObject.put("SDFSDFSDFS", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("VCDXVX", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken));
            jObject.put("RDRRG", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.FCMregId));
            jObject.put("GXESWE", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("GFDERSS", Build.MODEL);
            jObject.put("GFTURDU", Build.VERSION.RELEASE);
            jObject.put("GCXRWSW", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("FGTDESE", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("ZSWWWS", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("HGFDRE", BIG_Common_Utils.verifyInstallerId(activity));
            jObject.put("ESWECHG", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            BIG_AppLogger.getInstance().e("Get Wallet Balance ORIGINAL ==>", jObject.toString());
            BIG_AppLogger.getInstance().e("Get Wallet Balance ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            Call<Big_Api_Response> call = apiService.getWalletBalance(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<Big_Api_Response>() {
                @Override
                public void onResponse(Call<Big_Api_Response> call, Response<Big_Api_Response> response) {
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<Big_Api_Response> call, Throwable t) {
                    if (!call.isCanceled()) {
                        BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), BIG_Constants.msg_Service_Error, false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostExecute(Big_Api_Response response) {
        try {
            Big_Model_MinesweeperData responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Big_Model_MinesweeperData.class);
            BIG_AppLogger.getInstance().e("RESPONSE11", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                if (activity instanceof Big_MainActivity) {
                    ((Big_MainActivity) activity).onUpdateWalletBalance();
                }
                if (activity instanceof Big_EarningOptionsActivity) {
                    ((Big_EarningOptionsActivity) activity).onUpdateWalletBalance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
