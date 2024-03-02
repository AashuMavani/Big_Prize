package com.app.bigprize.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Async.Big_CheckScanAndPayStatusAsync;
import com.app.bigprize.Async.Big_ScanAndPayAsync;
import com.app.bigprize.Async.Models.Big_FinalWithdrawPointsResponseModel;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Top_Ads;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Activity_Manager;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class Big_ScanAndPayActivity extends AppCompatActivity {
    private TextView tvPoints, tvPointsDeduction, tvUpiId, tvName, lblLoadingAds, tvPaymentProgress, tvNameSuccess, tvFromSuccess, tvSuccessMessage, tvTransactionIdSuccess, tvTransactionDateSuccess, tvAmountSuccess, tvPointsDeductedSuccess;
    private ImageView ivHistory, ivIconUpi, ivShareSuccess, ivPoweredBy;
    private LinearLayout layoutAds, layoutPoints, layoutContent, layoutIntroduction, layoutPayment, layoutPaymentSuccess;
    private RelativeLayout layoutMain;
    private EditText etAmount, etNote;
    private AppCompatButton btnLetsStart, btnPayNow, btnDone;
    private int charges = 0, totalDeduction, minPayAmount,paymentAmount = 0,paymentAmountforCharges = 0;
    private Big_Response_Model responseMain;
    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private ProgressBar progressBar;
    private LottieAnimationView animation_view;
    private CountDownTimer timer;
    private long mLastClickTime = 0;
    public final int Request_Storage_resize = 111, Request_Camera = 222;
    private boolean isPaymentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ScanAndPayActivity.this);
        setContentView(R.layout.activity_big_scan_and_pay);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        layoutMain = findViewById(R.id.layoutMain);

        layoutPayment = findViewById(R.id.layoutPayment);
        layoutPaymentSuccess = findViewById(R.id.layoutPaymentSuccess);

        layoutIntroduction = findViewById(R.id.layoutIntroduction);
        layoutContent = findViewById(R.id.layoutContent);

        ivIconUpi = findViewById(R.id.ivIconUpi);
        etAmount = findViewById(R.id.etAmount);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);


        tvPaymentProgress = findViewById(R.id.tvPaymentProgress);
        progressBar = findViewById(R.id.progressBarPay);
        animation_view = findViewById(R.id.animation_view);

        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutAds = findViewById(R.id.layoutAds);

        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_SHOW_SCAN_PAY_INFO)) {
            layoutIntroduction.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);
            layoutMain.setBackgroundColor(getColor(R.color.white));

            tvPointsDeduction = findViewById(R.id.tvPointsDeduction);
            tvNameSuccess = findViewById(R.id.tvNameSuccess);
            tvFromSuccess = findViewById(R.id.tvFromSuccess);
            tvTransactionIdSuccess = findViewById(R.id.tvTransactionIdSuccess);
            tvTransactionIdSuccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (tvTransactionIdSuccess.getText().length() > 0) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied Text", tvTransactionIdSuccess.getText());
                            clipboard.setPrimaryClip(clip);
                            BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "Copied!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            tvSuccessMessage = findViewById(R.id.tvSuccessMessage);
            tvTransactionDateSuccess = findViewById(R.id.tvTransactionDateSuccess);
            tvAmountSuccess = findViewById(R.id.tvAmountSuccess);
            tvPointsDeductedSuccess = findViewById(R.id.tvPointsDeductedSuccess);

            btnDone = findViewById(R.id.btnDone);
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BIG_Ads_Utils.showAppLovinRewardedAd(Big_ScanAndPayActivity.this, new BIG_Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            });
            ivShareSuccess = findViewById(R.id.ivShareSuccess);
            ivShareSuccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request_Storage_resize);
                        Log.d("Shaer", "onClick: "+view);
                    } else {

                        shareImage();
                        Log.d("Shaer1", "onClick: "+view);
                    }
                }
            });

            try {
                ivPoweredBy = findViewById(R.id.ivPoweredBy);
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getPoweredByScanAndImage())) {
                    LinearLayout layoutPoweredBy = findViewById(R.id.layoutPoweredBy);
                    Glide.with(Big_ScanAndPayActivity.this)
                            .load(responseMain.getPoweredByScanAndImage())
                            .into(ivPoweredBy);
                    layoutPoweredBy.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!BIG_Common_Utils.isStringNullOrEmpty(getIntent().getStringExtra("upiImage"))) {
                    Glide.with(Big_ScanAndPayActivity.this)
                            .load(getIntent().getStringExtra("upiImage"))
                            .into(ivIconUpi);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            etNote = findViewById(R.id.etNote);


            tvUpiId = findViewById(R.id.tvUpiId);
            tvUpiId.setText(getIntent().getStringExtra("upiId"));

            tvName = findViewById(R.id.tvName);
            tvName.setText(getIntent().getStringExtra("name"));

            charges = getIntent().getIntExtra("charges", 0);

            minPayAmount = getIntent().getIntExtra("minPayAmount", 0);
            paymentAmount = getIntent().getIntExtra("paymentAmount", 0);
            paymentAmountforCharges = getIntent().getIntExtra("minPayAmountForCharges", 0);


            Big_Top_Ads topAds = (Big_Top_Ads) getIntent().getSerializableExtra("topAds");
            String homeNote = getIntent().getStringExtra("homeNote");

            // Load home note webview top
            try {
                if (!BIG_Common_Utils.isStringNullOrEmpty(homeNote)) {
                    WebView webNote = findViewById(R.id.webNote);
                    webNote.getSettings().setJavaScriptEnabled(true);
                    webNote.setVisibility(View.VISIBLE);
                    webNote.loadDataWithBaseURL(null, homeNote, "text/html", "UTF-8", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load top ad
            try {
                if (topAds != null && !BIG_Common_Utils.isStringNullOrEmpty(topAds.getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    BIG_Common_Utils.loadTopBannerAd(Big_ScanAndPayActivity.this, layoutTopAds, topAds);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            btnPayNow = findViewById(R.id.btnPayNow);
            btnPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if (tvUpiId.getText().toString().trim().length() == 0) {
                            BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "UPI Id can not be blank");
                            return;
                        }
                        if (tvName.getText().toString().trim().length() == 0) {
                            BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "Recipient name can not be blank");
                            return;
                        }
                        if (etAmount.getText().toString().trim().length() > 0 && Integer.parseInt(etAmount.getText().toString().trim()) > 0) {
                            if (Integer.parseInt(etAmount.getText().toString().trim()) < minPayAmount) {
                                showErrorMessage("Minimum Amount", "Payment should be minimum " + minPayAmount + "Rs.");
                            } else if (Integer.parseInt(BIG_SharePrefs.getInstance().getEarningPointString()) - totalDeduction < 0) {
                                NotifyCoin(Big_ScanAndPayActivity.this);
                            } else {
                                showConfirmationPopup();
                            }
                        } else {
                            BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "Please Enter Amount");
                            etAmount.requestFocus();
//                            Log.d("Amount", "onClick: "+etAmount);
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_ScanAndPayActivity.this);
                    }
                }
            });
            etAmount = findViewById(R.id.etAmount);
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (etAmount.getText().toString().trim().length() > 0) {
                        tvPointsDeduction.setVisibility(View.VISIBLE);
                        int totalAmount = Integer.parseInt(etAmount.getText().toString().trim()) * Integer.parseInt(responseMain.getPointValue());
                        String str = totalAmount + " Rubies";

                        if (Integer.parseInt(etAmount.getText().toString()) < paymentAmountforCharges) {
                            totalDeduction = totalAmount + charges;
                            if (charges > 0) {
                                str += " + " + charges + " Rubies(Fees) = " + totalDeduction + " Ruby";
                            }
                        } else {
                            totalDeduction = totalAmount;
                        }
                        str += "\nwill be deducted from your wallet.";
                        tvPointsDeduction.setText(str);
                    } else {
                        tvPointsDeduction.setVisibility(View.GONE);
                    }

                }
            });
            if (paymentAmount > 0) {
                etAmount.setText("" + paymentAmount);
            }
            etAmount.requestFocus();
            etAmount.setSelection(etAmount.getText().length());
        } else {
            layoutIntroduction.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.GONE);
            layoutMain.setBackgroundColor(getColor(R.color.white));

            btnLetsStart = findViewById(R.id.btnLetsStart);
            btnLetsStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, Request_Camera);
                    } else {
                        openScanScreen();
                    }
                }
            });
        }


        ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ScanAndPayActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.SCAN_AND_PAY)
                            .putExtra("title", "Scan and Pay History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ScanAndPayActivity.this);
                }
            }
        });

        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ScanAndPayActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ScanAndPayActivity.this);
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
    }

    private void shareImage() {
        ImageView ivPoweredBySS = findViewById(R.id.ivPoweredBySS);
        ImageView ivIconUpiSS = findViewById(R.id.ivIconUpiSS);
        try {
//            if (!M_Win_CommonMethods.isStringNullOrEmpty(responseMain.getPoweredByScanAndImage())) {
//                Glide.with(M_Win_ScanAndPayActivity.this)
//                        .load(responseMain.getPoweredByScanAndImage())
//                        .into(ivPoweredBySS);
//            } else {
//                ivPoweredBySS.setVisibility(View.GONE);
//            }
            ivPoweredBySS.setImageDrawable(ivPoweredBy.getDrawable());
            ivPoweredBySS.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
//            if (!M_Win_CommonMethods.isStringNullOrEmpty(getIntent().getStringExtra("upiImage"))) {
//                Glide.with(M_Win_ScanAndPayActivity.this)
//                        .load(getIntent().getStringExtra("upiImage"))
//                        .into(ivIconUpiSS);
//            }
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

        tvNameSS.setText(tvName.getText());
        tvFromSS.setText(tvFromSuccess.getText());
        tvTransactionIdSS.setText(tvTransactionIdSuccess.getText());
        tvUpiIdSS.setText(tvUpiId.getText());
        tvAmountSS.setText("₹ " + etAmount.getText());
        tvTransactionDateSS.setText(tvTransactionDateSuccess.getText());
        tvSuccessMessageSS.setText(tvSuccessMessage.getText());
        BIG_Common_Utils.showProgressLoader(Big_ScanAndPayActivity.this);

        try {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BIG_Common_Utils.dismissProgressLoader();
                    BIG_Activity_Manager.isShowAppOpenAd = false;
                    Bitmap bitmap = BIG_Common_Utils.setViewToBitmapImage(Big_ScanAndPayActivity.this, layoutScreenShot, getColor(R.color.white));
                    String path = BIG_Common_Utils.saveImageInCacheDirBitmap(Big_ScanAndPayActivity.this, bitmap, "payment.jpg");
                    BIG_Common_Utils.mShare(Big_ScanAndPayActivity.this, path, shareMessage);
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

                BIG_AppLogger.getInstance().e("#onrequest","fgfg");
                shareImage();
            } else {
                BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "Allow storage permission!");
            }
        } else if (requestCode == Request_Camera) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openScanScreen();
            } else {
                BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "Allow camera permission!");
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
                    BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "Allow storage permission!");
                }
            }
        }
    }

    private void openScanScreen() {
        BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_SHOW_SCAN_PAY_INFO, true);
        startActivity(new Intent(Big_ScanAndPayActivity.this, Big_ScanActivity.class));
        finish();
    }

    private void showConfirmationPopup() {
        try {
            final Dialog dialog1 = new Dialog(Big_ScanAndPayActivity.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.big_popup_confirm_payment);
            dialog1.setCancelable(false);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);


            int totalAmount = Integer.parseInt(etAmount.getText().toString().trim()) * Integer.parseInt(responseMain.getPointValue());
//            PP_AppLogger.getInstance().e("Dialog Amount",""+totalAmount);
            String str = "<b>" + totalAmount + " Rubies";
//            PP_AppLogger.getInstance().e("paymentAmountforCharges",""+paymentAmountforCharges);

            if (Integer.parseInt(etAmount.getText().toString()) < paymentAmountforCharges) {
                str += " + " + charges + " Rubies(Fee) = " + (totalAmount + charges) + " Rubies";
            }else {
                str += " + " + 0 + " Rubies(Fees) = " + (totalAmount) + " Rubies";
            }
            str += "<br/>will be deducted from your wallet.";

            tvMessage.setText(Html.fromHtml(str));

            AppCompatButton btnNo = dialog1.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(v -> {
                dialog1.dismiss();
            });
            AppCompatButton btnYes = dialog1.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (BIG_Common_Utils.isNetworkAvailable(Big_ScanAndPayActivity.this)) {
                    if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                        loadAppLovinNativeAds();
                    } else {
                        layoutAds.setVisibility(View.GONE);
                    }
                    dialog1.dismiss();
                    etAmount.setEnabled(false);
                    etAmount.setFocusable(false);
                    etAmount.invalidate();
                    etNote.setEnabled(false);
                    etNote.setFocusable(false);
                    etNote.invalidate();
                    btnPayNow.setVisibility(View.GONE);
                    ivIconUpi.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.invalidate();
                    tvPaymentProgress.setVisibility(View.VISIBLE);
                    isPaymentInProgress = true;
                    new Big_ScanAndPayAsync(Big_ScanAndPayActivity.this, tvUpiId.getText().toString().trim(), etAmount.getText().toString().trim(), String.valueOf(totalDeduction), tvName.getText().toString().trim(), etNote.getText().toString().trim(), String.valueOf(charges));
                } else {
                    BIG_Common_Utils.setToast(Big_ScanAndPayActivity.this, "No internet connection");
                }
            });

            dialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void backToNormalView() {
        isPaymentInProgress = false;
        etAmount.setEnabled(true);
        etAmount.setFocusable(true);
        etAmount.setFocusableInTouchMode(true);
        etAmount.setClickable(true);
        etAmount.invalidate();
        etNote.setEnabled(true);
        etNote.setFocusable(true);
        etNote.setFocusableInTouchMode(true);
        etNote.setClickable(true);
        etNote.invalidate();
        btnPayNow.setVisibility(View.VISIBLE);
        ivIconUpi.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        tvPaymentProgress.setVisibility(View.GONE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                cancelTimer();
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                    frameLayoutNativeAd = null;
                }
                if (transactionStatus.equals(BIG_Constants.STATUS_ERROR)) {
                    new Big_CheckScanAndPayStatusAsync(Big_ScanAndPayActivity.this, tvTransactionIdSuccess.getText().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ScanAndPayActivity.this);
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
                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                    BIG_AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
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

    private int apiCallCounter = 0;
    private String transactionStatus = "0", shareMessage = "";

    public void updateStatus(Big_FinalWithdrawPointsResponseModel responseModel) {
        try {
            if (responseModel != null) {
                if (responseModel.getStatus().matches(BIG_Constants.STATUS_SUCCESS)) {
                    isPaymentInProgress = false;
                    transactionStatus = BIG_Constants.STATUS_SUCCESS;
                    BIG_Common_Utils.logFirebaseEvent(Big_ScanAndPayActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Scan_Pay_Success", "BigP_Scan_Pay_Success");
                    cancelTimer();
                    progressBar.setVisibility(View.INVISIBLE);
                    tvPaymentProgress.setVisibility(View.GONE);
                    ivIconUpi.setVisibility(View.INVISIBLE);
                    tvSuccessMessage.setText("Payment Successful!");
                    btnDone.setText("Done");
                    layoutPayment.setVisibility(View.GONE);
                    layoutPaymentSuccess.setVisibility(View.VISIBLE);

                    animation_view.setAnimation(R.raw.big_win);
                    animation_view.setVisibility(View.VISIBLE);
                    animation_view.playAnimation();
                } else if (apiCallCounter == 5) {
                    cancelTimer();
                    isPaymentInProgress = false;
                    tvSuccessMessage.setText("Payment is Pending");
                    btnDone.setText("Ok");

                    progressBar.setVisibility(View.INVISIBLE);
                    tvPaymentProgress.setVisibility(View.GONE);
                    ivIconUpi.setVisibility(View.INVISIBLE);

                    layoutPayment.setVisibility(View.GONE);
                    layoutPaymentSuccess.setVisibility(View.VISIBLE);

                    animation_view.setAnimation(R.raw.pending);
                    animation_view.setVisibility(View.VISIBLE);
                    animation_view.playAnimation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimer() {
        try {
            if (timer == null) {
                timer = new CountDownTimer(16 * 1000L, 3000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (apiCallCounter < 5) {
                            apiCallCounter += 1;
                            new Big_CheckScanAndPayStatusAsync(Big_ScanAndPayActivity.this, tvTransactionIdSuccess.getText().toString());
                        }
                    }

                    @Override
                    public void onFinish() {
                        cancelTimer();
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkWithdraw(Big_FinalWithdrawPointsResponseModel responseModel) {
        try {
            if (responseModel != null) {
                if (responseModel.getStatus().matches(BIG_Constants.STATUS_SUCCESS)) {
                    shareMessage = responseModel.getShareText();
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    tvNameSuccess.setText(tvName.getText());
                    tvFromSuccess.setText(responseModel.getPaymentPartner());
                    tvTransactionIdSuccess.setText(responseModel.getTxnID());
                    tvTransactionDateSuccess.setText(BIG_Common_Utils.modifyDateLayout(responseModel.getDeliveryDate()));
                    tvAmountSuccess.setText("₹ " + etAmount.getText());
                    tvPointsDeductedSuccess.setText("" + totalDeduction);
                    setTimer();
                } else if (responseModel.getStatus().matches(BIG_Constants.STATUS_ERROR)) {
                    backToNormalView();
                    showErrorMessage(getString(R.string.app_name), responseModel.getMessage());
                } else {
                    isPaymentInProgress = false;
                    ivIconUpi.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    tvPaymentProgress.setVisibility(View.GONE);
                    showErrorMessage(getString(R.string.app_name), responseModel.getMessage());
                }
            } else {
                backToNormalView();
                BIG_Common_Utils.Notify(Big_ScanAndPayActivity.this, getString(R.string.app_name), "Something went wrong, please try again later.", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String title, String message) {
        try {
            final Dialog dialog1 = new Dialog(Big_ScanAndPayActivity.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.big_dialog_notify);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ScanAndPayActivity.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        try {
                            if (dialog1 != null) {
                                dialog1.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            });
            if (!isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void NotifyCoin(Activity activity) {
        try {
            if (activity != null) {
                Dialog dialog1 = new Dialog(Big_ScanAndPayActivity.this, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setCancelable(true);
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.setContentView(R.layout.big_dialog_not_enough_coins);

                TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
                tvTitle.setTextColor(getColor(R.color.red));
                tvTitle.setText("Not Enough Rubies!");

                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
                tvMessage.setText("You don't have enough Rubies to make payment. Earn more Rubies and then try again.");

//                View viewShine = dialog1.findViewById(R.id.viewShine);
//                Animation animUpDown = AnimationUtils.loadAnimation(Big_ScanAndPayActivity.this, R.anim.big_left_right);
//                animUpDown.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        viewShine.startAnimation(animUpDown);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
                // start the animation
//                viewShine.startAnimation(animUpDown);

                Button btnEarnMore = dialog1.findViewById(R.id.btnEarnMore);
                btnEarnMore.setOnClickListener(v -> {
                    if (!activity.isFinishing()) {
                        dialog1.dismiss();
                    }
                    Intent intent = new Intent(Big_ScanAndPayActivity.this, Big_TasksCategoryTypeActivity.class);
                    startActivity(intent);
                });
                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (isPaymentInProgress) {
            Dialog dialog1 = new Dialog(Big_ScanAndPayActivity.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.big_dialog_notify);
            dialog1.setCancelable(true);

            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText("Please Wait");

            LottieAnimationView animation_view = dialog1.findViewById(R.id.animation_view);
            animation_view.setAnimation(R.raw.pending);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText("Your payment is in process...");

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(v -> {
                dialog1.dismiss();
            });
            dialog1.show();
        } else {
            super.onBackPressed();
        }
    }
}