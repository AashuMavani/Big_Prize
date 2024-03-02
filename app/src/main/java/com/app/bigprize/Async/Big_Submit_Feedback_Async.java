package com.app.bigprize.Async;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.app.bigprize.Async.Models.Big_Api_Response;
import com.app.bigprize.Async.Models.Big_FAQ_Model;
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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Big_Submit_Feedback_Async {
    private Activity activity;
    private JSONObject jObject;
    private BIG_AES_Cipher pocAesCipher;
    private String ticketId;
    public Big_Submit_Feedback_Async(final Activity activity, String emailId, String message, String mobileNumber, String image, String withdrawId, String transactionId, String ticketId, String isCloseTicket) {
        this.activity = activity;
        pocAesCipher = new BIG_AES_Cipher();
        this.ticketId = ticketId;
        try {
            BIG_Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("V65VS5G6A", withdrawId);
            jObject.put("QEWE4D5QGS", transactionId);
            jObject.put("K7H5FS3AA", ticketId);
            jObject.put("SV5G6SVT5", isCloseTicket);
            jObject.put("NWHVRI", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.totalOpen));
            jObject.put("TJGNM2", BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.todayOpen));
            jObject.put("SWCRJT", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("5VLZ61", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AppVersion));
            jObject.put("QPTZGX", Build.MODEL);
            jObject.put("OY4YW6", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("OWO92W", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken));
            jObject.put("KOL4XU", emailId);
            jObject.put("VYQ72N", mobileNumber);
            jObject.put("WW5OS4", message);
            jObject.put("RGCQEQ", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("9RPNUP", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            MultipartBody.Part body = null;

            // Send extra params as part
            RequestBody requestBodyDetails =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), jObject.toString());
            try {
                if (image != null) {
                    File file = new File(image);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("image1", file.getName(), requestFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            Call<Big_Api_Response> call = apiService.submitFeedback(requestBodyDetails, body);
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
            Big_FAQ_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_FAQ_Model.class);
            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
                BIG_Common_Utils.doLogout(activity);
            } else {
                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                    if (BIG_Common_Utils.isStringNullOrEmpty(ticketId)) {
                        BIG_Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Feedback", "Submit Feedback -> Success");
                    }
                    Intent i = new Intent();
                    i.putExtra("ticketId", responseModel.getTicketId());
                    activity.setResult(Activity.RESULT_OK, i);
                    BIG_Common_Utils.NotifySuccess(activity, activity.getString(R.string.app_name), responseModel.getMessage(), true);
                } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
                    if (ticketId.length() == 0) {
                        BIG_Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Feedback", "Submit Feedback -> Fail");
                    }
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

//    private void onPostExecute(Big_Api_Response response) {
//        try {
//            BIG_Common_Utils.dismissProgressLoader();
//            Big_FAQ_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Big_FAQ_Model.class);
//            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
//            if (responseModel.getStatus().equals(BIG_Constants.STATUS_LOGOUT)) {
//                BIG_Common_Utils.doLogout(activity);
//            } else {
//                BIG_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
//                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
//                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.userToken, responseModel.getUserToken());
//                }
//                if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
//                    BIG_Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Feedback", "Submit Feedback -> Success");
//                    BIG_Common_Utils.NotifySuccess(activity, activity.getString(R.string.app_name), responseModel.getMessage(), true);
//                } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR)) {
//                    BIG_Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Feedback", "Submit Feedback -> Fail");
//                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
//                } else if (responseModel.getStatus().equals("2")) {
//                    BIG_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
//                }
//                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
//                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
