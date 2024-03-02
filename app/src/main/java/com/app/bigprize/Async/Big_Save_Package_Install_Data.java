package com.app.bigprize.Async;

import android.provider.Settings;

import com.app.bigprize.Big_App_Controller;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Network.Big_ApiClient;
import com.app.bigprize.Network.Big_ApiInterface;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_AES_Cipher;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Big_Save_Package_Install_Data {
        private JSONObject jObject;
   private BIG_AES_Cipher pocAesCipher;


    public Big_Save_Package_Install_Data(String packageId) {
        try {
            jObject = new JSONObject();

            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            String url = responseMain.getPackageInstallTrackingUrl()
                    + "?pid=" + responseMain.getPid()
                    + "&offer_id=" + responseMain.getOffer_id()
                    + "&sub1=" + packageId
                    + "&sub2=" + Big_App_Controller.getContext().getPackageName()
                    + "&sub3=" + BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID);
            jObject.put("NKGDFJGSDFG", url);
            jObject.put("HSDFNS", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId).length() == 0 ? "0" : BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId));
            jObject.put("BASTGBE", BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN());
            jObject.put("AVTYRN", BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID));
            jObject.put("AEBYER", packageId);
            try {
                jObject.put("QVERYQ", Settings.Secure.getString(Big_App_Controller.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                jObject.put("WUERYYJ", Settings.Secure.getString(Big_App_Controller.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
            int n = BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            Big_ApiInterface apiService = Big_ApiClient.getClient().create(Big_ApiInterface.class);
            Call<JsonObject> call = apiService.savePackageTracking(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userToken) : BIG_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostExecute(JsonObject responseModel) {
        try {
            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

