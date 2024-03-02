package com.app.bigprize.Activity;

import static android.view.View.GONE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.app.bigprize.Async.Big_GetTicketDetailsAsync;
import com.app.bigprize.Async.Big_Submit_Feedback_Async;
import com.app.bigprize.Async.Models.Big_FAQ_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_User_Details;
import com.app.bigprize.R;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.io.File;

public class Big_FeedbackSubmitActivity extends AppCompatActivity {
    private ImageView ivFAQ, ivImage;
    private AppCompatButton btnSubmit, btnCloseTicket;
    private Big_Response_Model responseMain;
    private LinearLayout layoutImage, layoutAds;
    private LinearLayout layoutReply;
    private FrameLayout frameLayoutNativeAd;
    private Big_User_Details userDetails;
    private MaxNativeAdLoader nativeAdLoader;
    private EditText etFeedback;
    private TextView tvImage;
    private TextView tvReply;
    private String selectedImage;
    private boolean isCheckTicketStatus = false;
    private TextView lblLoadingAds;
    private MaxAd nativeAd;
    public final int GALLERY_REQUEST_CODE = 105, Request_Storage_resize = 200;
    private Big_FAQ_Model objData;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_FeedbackSubmitActivity.this);
        setContentView(R.layout.big_activity_feedback_submit2);


        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        userDetails = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.User_Details), Big_User_Details.class);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getIntent().getStringExtra("title"));
        etFeedback = findViewById(R.id.etFeedback);
        ivImage = findViewById(R.id.ivImage);
        LinearLayout layoutTransaction = findViewById(R.id.layoutTransaction);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("withdrawId")) {
            layoutTransaction.setVisibility(View.VISIBLE);
            TextView txtTxn = findViewById(R.id.txtTxn);
            txtTxn.setText(getIntent().getExtras().getString("transactionId"));
            etFeedback.setHint("Please enter your ticket details here*");
            isCheckTicketStatus = getIntent().getExtras().containsKey("ticketId");
            layoutReply = findViewById(R.id.layoutReply);
            tvReply = findViewById(R.id.tvReply);
            btnCloseTicket = findViewById(R.id.btnCloseTicket);
            btnCloseTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_FeedbackSubmitActivity.this, new BIG_Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            new Big_Submit_Feedback_Async(Big_FeedbackSubmitActivity.this,
                                    userDetails.getEmailId(), etFeedback.getText().toString().trim(), userDetails.getMobileNumber(), selectedImage, getIntent().getExtras().getString("withdrawId"), getIntent().getExtras().getString("transactionId"), getIntent().getExtras().getString("ticketId"), "1");
                        }
                    });
                }
            });
        }
        tvImage = findViewById(R.id.tvImage);
        tvReply = findViewById(R.id.tvReply);
        layoutReply = findViewById(R.id.layoutReply);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        btnCloseTicket = findViewById(R.id.btnCloseTicket);
        btnCloseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_FeedbackSubmitActivity.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        new Big_Submit_Feedback_Async(Big_FeedbackSubmitActivity.this,
                                userDetails.getEmailId(), etFeedback.getText().toString().trim(), userDetails.getMobileNumber(), selectedImage, getIntent().getExtras().getString("withdrawId"), getIntent().getExtras().getString("transactionId"), getIntent().getExtras().getString("ticketId"), "1");
                    }
                });
            }
        });

//
//        ivFAQ = findViewById(R.id.ivFAQ);
//        ivFAQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Big_FeedbackSubmitActivity.this, Big_ActivityFAQ.class));
//            }
//        });

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidData()) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_FeedbackSubmitActivity.this, new BIG_Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {

                            if (userDetails == null) {
                                new Big_Submit_Feedback_Async(Big_FeedbackSubmitActivity.this,
                                        "", etFeedback.getText().toString().trim(), "", selectedImage, "", "", "", "");
                            } else if (getIntent().getExtras().containsKey("withdrawId")) {
                                new Big_Submit_Feedback_Async(Big_FeedbackSubmitActivity.this,
                                        userDetails.getEmailId(), etFeedback.getText().toString().trim(), userDetails.getMobileNumber(), selectedImage, getIntent().getExtras().getString("withdrawId"), getIntent().getExtras().getString("transactionId"), getIntent().getExtras().getString("ticketId"), "");
                            } else {
                                new Big_Submit_Feedback_Async(Big_FeedbackSubmitActivity.this,
                                        userDetails.getEmailId(), etFeedback.getText().toString().trim(), userDetails.getMobileNumber(), selectedImage, "", "", "", "");
                            }
                        }
                    });
                }

            }
        });

        layoutAds = findViewById(R.id.layoutAds);
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutImage = findViewById(R.id.layoutImage);
        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Big_FeedbackSubmitActivity.this.getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Big_FeedbackSubmitActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request_Storage_resize);
                } else {
                    if (isCheckTicketStatus) {
                        if (objData != null && !BIG_Common_Utils.isStringNullOrEmpty(objData.getImage())) {
                            BIG_Common_Utils.openUrl(Big_FeedbackSubmitActivity.this, objData.getImage());
                        }
                    } else {
                        selectImage(GALLERY_REQUEST_CODE);
                    }
                }
            }
        });
        if (isCheckTicketStatus) {
            new Big_GetTicketDetailsAsync(Big_FeedbackSubmitActivity.this, getIntent().getExtras().getString("ticketId"));
            btnCloseTicket.setVisibility(View.VISIBLE);
        }

        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(GONE);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_Storage_resize) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage(GALLERY_REQUEST_CODE);
            } else {
                BIG_Common_Utils.setToast(Big_FeedbackSubmitActivity.this, "Allow permission for storage access!");
            }
            return;
        }

    }

    private void selectImage(int requestCode) {
        BIG_Activity_Manager.isShowAppOpenAd = false;
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if (data != null) {
                    Uri uriforloadImage = data.getData();
                    if (uriforloadImage != null) {
                        selectedImage = BIG_Common_Utils.getPathFromURI(Big_FeedbackSubmitActivity.this, uriforloadImage);
                        Glide.with(Big_FeedbackSubmitActivity.this)
                                .load(new File(selectedImage))
                                .override(getResources().getDimensionPixelSize(R.dimen.dim_90), getResources().getDimensionPixelSize(R.dimen.dim_90))
                                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.dim_5))))
                                .into(ivImage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Request_Storage_resize) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    selectImage(GALLERY_REQUEST_CODE);
                } else {
                    BIG_Common_Utils.setToast(Big_FeedbackSubmitActivity.this, "Allow permission for storage access!");
                }
            }
        }
    }


    private boolean isValidData() {
        if (etFeedback.getText().toString().trim().length() == 0) {
            BIG_Common_Utils.setToast(Big_FeedbackSubmitActivity.this, "Please enter feedback");
            return false;
        }
        return true;
    }

    private void loadAppLovinNativeAds() {
        /* try {*/
        nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_FeedbackSubmitActivity.this);
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
                //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {

            }
        });
        nativeAdLoader.loadAd();
       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void updateData(Big_FAQ_Model responseModel) {
        try {
            objData = responseModel;
            if (!BIG_Common_Utils.isStringNullOrEmpty(objData.getImage())) {
                layoutImage.setVisibility(View.VISIBLE);
                tvImage.setText("Click to View Image");
                Glide.with(Big_FeedbackSubmitActivity.this)
                        .load(objData.getImage())
                        .override(getResources().getDimensionPixelSize(R.dimen.dim_90), getResources().getDimensionPixelSize(R.dimen.dim_90))
                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.dim_5))))
                        .into(ivImage);
            } else {
                layoutImage.setVisibility(View.GONE);
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(objData.getReply())) {
                layoutReply.setVisibility(View.VISIBLE);
                tvReply.setText(objData.getReply());
            } else {
                layoutReply.setVisibility(View.GONE);
            }
            etFeedback.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    btnSubmit.setEnabled(!etFeedback.getText().toString().trim().equals(objData.getQuery().trim()));
                    btnSubmit.setText(!etFeedback.getText().toString().trim().equals(objData.getQuery().trim()) ? "Submit Changes" : "Submit");
                }
            });
            etFeedback.setText(objData.getQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}