package com.app.bigprize.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Activity.Big_ScanAndPayDetails_Activity;
import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_Point_History_Model;
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

public class Big_PaymentDetails_Async {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher pocAesCipher;

    public Big_PaymentDetails_Async(final Activity activity, String id) {
        this.activity = activity;
        pocAesCipher = new BIG_AES_Cipher();
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("DGSUHDUADD", id);
            jObject.put("BSXBYUSUSD", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("DFGDDVC", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken));
            jObject.put("GFDEFGH", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("CRDRD", Build.MODEL);
            jObject.put("XZSWAA", Build.VERSION.RELEASE);
            jObject.put("SAFAADFD", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("DWAAEWSD", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("XZSASZ", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("VDXDGSDX", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);

            BIG_AppLogger.getInstance().e("Get Payment Details ORIGINAL ==>", jObject.toString());

            Call<Big_Api_Response> call = apiService.paymentDetails(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken), String.valueOf(n), jObject.toString());
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

            BIG_AppLogger.getInstance().e("Get Payments Details Response ", "" + new Gson().toJson(responseModel));

            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                    if (activity instanceof Big_ScanAndPayDetails_Activity) {
                        ((Big_ScanAndPayDetails_Activity) activity).showData(responseModel);
                    }
                } else
                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
                FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
