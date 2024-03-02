package com.app.bigprize.Activity;

import static android.view.View.GONE;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.bigprize.Async.Big_PaymentDetails_Async;
import com.app.bigprize.Async.Models.Big_Point_History_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Wallet_ListItem;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Activity_Manager;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class Big_ScanAndPayDetails_Activity extends AppCompatActivity {
    private TextView tvPoints, lblLoadingAds, tvNameSuccess, tvFromSuccess, tvSuccessMessage, tvTransactionIdSuccess, tvTransactionDateSuccess, tvAmountSuccess, tvPointsDeductedSuccess;
    private Big_Response_Model responseMain;
    private ImageView ivIconUpi;
    public final int Request_Storage_resize = 111;
    private String transactionStatus = "0", shareMessage = "";
    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private LinearLayout layoutAds, layoutPoints, layoutPaymentSuccess, layoutShare;
    private Big_Point_History_Model objData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ScanAndPayDetails_Activity.this);
        setContentView(R.layout.activity_big_scan_and_pay_details);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        layoutPaymentSuccess = findViewById(R.id.layoutPaymentSuccess);
        layoutPaymentSuccess.setVisibility(View.INVISIBLE);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutAds = findViewById(R.id.layoutAds);
        tvSuccessMessage = findViewById(R.id.tvSuccessMessage);
        tvTransactionDateSuccess = findViewById(R.id.tvTransactionDateSuccess);
        tvAmountSuccess = findViewById(R.id.tvAmountSuccess);
        tvPointsDeductedSuccess = findViewById(R.id.tvPointsDeductedSuccess);
        tvNameSuccess = findViewById(R.id.tvNameSuccess);
        tvFromSuccess = findViewById(R.id.tvFromSuccess);
        tvTransactionIdSuccess = findViewById(R.id.tvTransactionIdSuccess);
        ivIconUpi = findViewById(R.id.ivIconUpi);

        String withdrawID = getIntent().getStringExtra("withdrawID");

        tvTransactionIdSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (tvTransactionIdSuccess.getText().length() > 0) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", tvTransactionIdSuccess.getText());
                        clipboard.setPrimaryClip(clip);
                        BIG_Common_Utils.setToast(Big_ScanAndPayDetails_Activity.this, "Copied!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layoutShare = findViewById(R.id.layoutShare);
        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(ScanAndPayDetailsActivity.this, "CLicked---", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request_Storage_resize);
                } else {
                    shareImage();
                }
            }
        });

        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(GONE);
        }

        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ScanAndPayDetails_Activity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ScanAndPayDetails_Activity.this);
                }
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Big_PaymentDetails_Async(Big_ScanAndPayDetails_Activity.this, withdrawID);
    }

    private void shareImage() {
        ImageView ivPoweredBySS = findViewById(R.id.ivPoweredBySS);
        ImageView ivIconUpiSS = findViewById(R.id.ivIconUpiSS);
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getPoweredByScanAndImage())) {
                Glide.with(Big_ScanAndPayDetails_Activity.this)
                        .load(responseMain.getPoweredByScanAndImage())
                        .into(ivPoweredBySS);
            } else {
                ivPoweredBySS.setVisibility(View.GONE);
            }
            ivPoweredBySS.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ivIconUpiSS.setImageDrawable(ivIconUpi.getDrawable());
            ivIconUpiSS.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView tvTransactionDateSS = findViewById(R.id.tvTransactionDateSS);
        TextView tvTransactionIdSS = findViewById(R.id.tvTransactionIdSS);
        TextView tvFromSS = findViewById(R.id.tvFromSS);
        TextView tvUpiIdSS = findViewById(R.id.tvUpiIdSS);
        TextView tvNameSS = findViewById(R.id.tvNameSS);
        TextView tvAmountSS = findViewById(R.id.tvAmountSS);
        TextView tvSuccessMessageSS = findViewById(R.id.tvSuccessMessageSS);
        LinearLayout layoutScreenShot = findViewById(R.id.layoutScreenShot);
        ImageView ivPaymentStatusSS = findViewById(R.id.ivPaymentStatusSS);

        ivPaymentStatusSS.setImageResource(transactionStatus.equals(BIG_Constants.STATUS_SUCCESS) ? R.drawable.big_ic_success : R.drawable.big_ic_pending);

        tvNameSS.setText(tvNameSuccess.getText());
        tvFromSS.setText(tvFromSuccess.getText());
        tvTransactionIdSS.setText(tvTransactionIdSuccess.getText());
        tvUpiIdSS.setText(objData.getPayment().getMobileNo());

        tvAmountSS.setText(tvAmountSuccess.getText());
        tvTransactionDateSS.setText(tvTransactionDateSuccess.getText());
        tvSuccessMessageSS.setText(tvSuccessMessage.getText());
        tvSuccessMessageSS.setText(tvSuccessMessage.getText());

        BIG_Common_Utils.showProgressLoader(Big_ScanAndPayDetails_Activity.this);

        try {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BIG_Common_Utils.dismissProgressLoader();
                    BIG_Activity_Manager.isShowAppOpenAd = false;
                    Bitmap bitmap = BIG_Common_Utils.setViewToBitmapImage(Big_ScanAndPayDetails_Activity.this, layoutScreenShot, getColor(R.color.white));
                    String path = BIG_Common_Utils.saveImageInCacheDirBitmap(Big_ScanAndPayDetails_Activity.this, bitmap, "payment.jpg");
                    BIG_Common_Utils.mShare(Big_ScanAndPayDetails_Activity.this, path, shareMessage);
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_Storage_resize) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareImage();
            } else {
                BIG_Common_Utils.setToast(Big_ScanAndPayDetails_Activity.this, "Allow storage permission!");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Storage_resize) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    shareImage();
                } else {
                    BIG_Common_Utils.setToast(Big_ScanAndPayDetails_Activity.this, "Allow storage permission!");
                }
            }
        }
    }
    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ScanAndPayDetails_Activity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }
                    nativeAd = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayoutNativeAd.addView(nativeAdView);
                    layoutAds.setVisibility(View.VISIBLE);
                    lblLoadingAds.setVisibility(View.GONE);
                    //Earn_AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                    //Earn_AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoader.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showData(Big_Point_History_Model responseModel) {
        try {
            objData = responseModel;
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ScanAndPayDetails_Activity.this, null);
            layoutPaymentSuccess.setVisibility(View.VISIBLE);
            if (responseModel.getPayment().getIsDeliverd() != null) {
                if (responseModel.getPayment().getIsDeliverd().matches("1")) {
                    transactionStatus = BIG_Constants.STATUS_SUCCESS;
                    tvSuccessMessage.setText("Payment Successful!");
                } else if (responseModel.getPayment().getIsDeliverd().matches("0")) {
                    transactionStatus = BIG_Constants.STATUS_ERROR;
                    tvSuccessMessage.setText("Payment is Pending!");
                } else {
                    transactionStatus = "2";
                    tvSuccessMessage.setText("Payment Failed");
                }
            }
            shareMessage = responseModel.getShareText();
            Big_Wallet_ListItem p_walletListItem = responseModel.getPayment();
            if (responseModel.getPayment().getEmailID() != null) {
                tvNameSuccess.setText(p_walletListItem.getEmailID());
            }
            if (responseModel.getPayment().getPaymentFrom() != null) {
                tvFromSuccess.setText(p_walletListItem.getPaymentFrom());
            }
            if (responseModel.getPayment().getTxnID() != null) {
                tvTransactionIdSuccess.setText(p_walletListItem.getTxnID());
            }
            if (responseModel.getPayment().getEntryDate() != null) {
                tvTransactionDateSuccess.setText(BIG_Common_Utils.modifyDateLayout(responseModel.getPayment().getEntryDate()));
            }
            if (responseModel.getPayment().getPoints() != null) {
                tvPointsDeductedSuccess.setText(p_walletListItem.getPoints());
            }
            if (responseModel.getPayment().getAmount() != null) {
                tvAmountSuccess.setText("₹ " + p_walletListItem.getAmount());
            }

            try {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getUpiImage())) {
                    Glide.with(Big_ScanAndPayDetails_Activity.this)
                            .load(responseModel.getUpiImage())
                            .into(ivIconUpi);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}