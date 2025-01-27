package com.app.bigprize.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Adapter.Big_SimpleText_Adapter;
import com.app.bigprize.Async.Big_Download_Image_Share_Async;
import com.app.bigprize.Async.Big_Get_Task_Details_Async;
import com.app.bigprize.Async.Big_SaveShareTaskAsync;
import com.app.bigprize.Async.Big_Task_Image_Upload_Async;
import com.app.bigprize.Async.Models.Big_ReferResponseModel;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Task_Details_Response_Model;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

public class Big_TaskDetailsInfoActivity extends AppCompatActivity {
    private TextView tvPoints, tvScratchCard, lblLoadingAds;
    private ImageView ivBack, ivHistory, ivSmallIcon, ivBanner, ivVideoTutorial, ivGifFinger1, ivGifFinger2, ivGifFinger3, ivGifFinger4, loadSelectImage;
    private TextView tvTitle, txtPoints, txtTitle, txtSubtitle, txtNote, txtTitleUpload, txtFileName, tvReferTaskPoints, tvShareTitle;
    private LottieAnimationView ltSmallIcon, ivLottieView;
    private RelativeLayout layoutButton, layoutNote;
    private CardView cardPoint;
    private Button lInstallBtn, btnUpload;
    private LinearLayout lTaskMain, lWatch, layoutShareOther, layoutShareWA, layoutCopyLink;
    private WebView webTaskStep, webDisclaimer;
    private LinearLayout relVideoTutorial, lPickImage;
    private String imagePath;
    private RelativeLayout layoutYoutubeImage, layoutTaskBanner;
    private int PICK_IMAGE = 12;
    private String taskId;
    private LinearLayout layoutPoints, layoutAds;
    private Big_Task_Details_Response_Model objTask;
    private View viewShine, viewShineNote;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private Big_Response_Model responseMain;
    private FrameLayout frameLayoutNativeAd;
    private CardView cardDisclaimer, cardUploadImage, cardHowToClaim, cardWatchVideo, cardReferTask;
    private RecyclerView rvFootSteps, rvTnC;
    private Big_ReferResponseModel responseModelShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_TaskDetailsInfoActivity.this);
        setContentView(R.layout.big_activity_task_details_info2);

        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        taskId = getIntent().getStringExtra("taskId");
        initView();
        new Big_Get_Task_Details_Async(Big_TaskDetailsInfoActivity.this, taskId);
    }

    private void initView() {
        cardDisclaimer = findViewById(R.id.cardDisclaimer);
        cardUploadImage = findViewById(R.id.cardUploadImage);
        cardHowToClaim = findViewById(R.id.cardHowToClaim);
        cardWatchVideo = findViewById(R.id.cardWatchVideo);
        layoutTaskBanner = findViewById(R.id.layoutTaskBanner);
        rvFootSteps = findViewById(R.id.rvFootSteps);
        rvTnC = findViewById(R.id.rvTnC);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_TaskDetailsInfoActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_TaskDetailsInfoActivity.this);
                }
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        ivHistory = findViewById(R.id.ivHistory);
        //   Common_Utils.startRoundAnimation(TaskDetailsInfoActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_TaskDetailsInfoActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.TASK)
                            .putExtra("title", "Task History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_TaskDetailsInfoActivity.this);
                }
            }
        });
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutYoutubeImage = findViewById(R.id.layoutYoutubeImage);
        lInstallBtn = findViewById(R.id.lInstallBtn);
        //viewShine = findViewById(R.id.viewShine);
        layoutButton = findViewById(R.id.layoutButton);
        lTaskMain = findViewById(R.id.lTaskMain);
        tvTitle = findViewById(R.id.tvTitle);
        cardReferTask = findViewById(R.id.cardReferTask);
        tvReferTaskPoints = findViewById(R.id.tvReferTaskPoints);
        layoutShareOther = findViewById(R.id.layoutShareOther);
        layoutCopyLink = findViewById(R.id.layoutCopyLink);
        layoutShareWA = findViewById(R.id.layoutShareWA);
//        layoutPointsInner = findViewById(R.id.layoutPointsInner);
//        layoutReferTaskPoints = findViewById(R.id.layoutReferTaskPoints);
        txtFileName = findViewById(R.id.txtFileName);
        txtTitleUpload = findViewById(R.id.txtTitleUpload);
        loadSelectImage = findViewById(R.id.loadSelectImage);
        ivSmallIcon = findViewById(R.id.ivSmallIcon);
        btnUpload = findViewById(R.id.btnUpload);
        relVideoTutorial = findViewById(R.id.relVideoTutorial);
        txtPoints = findViewById(R.id.txtPoints);
        lPickImage = findViewById(R.id.lPickImage);
        ivGifFinger3 = findViewById(R.id.ivGifFinger3);
        ivGifFinger4 = findViewById(R.id.ivGifFinger4);
        lWatch = findViewById(R.id.lWatch);
        txtTitle = findViewById(R.id.txtTitle);
        ivVideoTutorial = findViewById(R.id.ivVideoTutorial);
        txtSubtitle = findViewById(R.id.txtSubtitle);
        layoutNote = findViewById(R.id.layoutNote);
        txtNote = findViewById(R.id.txtNote);
        ivBanner = findViewById(R.id.ivBanner);
        webTaskStep = findViewById(R.id.webTaskStep);
        webDisclaimer = findViewById(R.id.webDisclamier);
        ltSmallIcon = findViewById(R.id.ltSmallIcon);
        ivLottieView = findViewById(R.id.ivLottieView);
        cardUploadImage.setVisibility(View.GONE);
        lPickImage.setVisibility(View.GONE);
        lTaskMain.setVisibility(View.INVISIBLE);
        layoutButton.setVisibility(View.GONE);
        cardPoint=findViewById(R.id.cardPoint);


        lPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BIG_Common_Utils.setEnableDisable(Big_TaskDetailsInfoActivity.this, v);
                if (ContextCompat.checkSelfPermission(Big_TaskDetailsInfoActivity.this.getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Big_TaskDetailsInfoActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 74);
                } else {
                    BIG_Activity_Manager.isShowAppOpenAd = false;
                    txtFileName.setText("Click here to select image");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BIG_Common_Utils.setEnableDisable(Big_TaskDetailsInfoActivity.this, v);
                if (imagePath != null && !imagePath.isEmpty()) {
                    UploadImage();
                } else {
                    Toast.makeText(Big_TaskDetailsInfoActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    public void setData(Big_Task_Details_Response_Model responseModel) {
        if (responseModel.getTaskDetails() != null) {
            objTask = responseModel;
            if (responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(BIG_Constants.APPLOVIN_INTERSTITIAL)) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_TaskDetailsInfoActivity.this, null);
            } else if (responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(BIG_Constants.APPLOVIN_REWARD)) {
                BIG_Ads_Utils.showAppLovinRewardedAd(Big_TaskDetailsInfoActivity.this, null);
            }

            LinearLayout layoutBannerAdTop = findViewById(R.id.layoutBannerAdTop);
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (responseModel.getTaskDetails().getIsShowNativeAd() != null && responseModel.getTaskDetails().getIsShowNativeAd().equals("1")) {
                toolbar.setVisibility(View.VISIBLE);
                layoutTaskBanner.setVisibility(View.GONE);
                layoutBannerAdTop.setVisibility(View.GONE);
                lblLoadingAds = findViewById(R.id.lblLoadingAds);
                layoutAds = findViewById(R.id.layoutAds);
                frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                loadAppLovinNativeAds();
            } else {

                // Load top banner ad
                try {
                    if (BIG_Common_Utils.isShowAppLovinBannerAds()) {
                        toolbar.setVisibility(View.VISIBLE);
                        layoutBannerAdTop.setVisibility(View.VISIBLE);
                        TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceTop);
                        BIG_Common_Utils.loadBannerAds(Big_TaskDetailsInfoActivity.this, layoutBannerAdTop, lblAdSpaceBottom);
                    } else {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                    layoutTaskBanner.setVisibility(View.VISIBLE);
                    if (responseModel.getTaskDetails().getImages() != null) {
                        if (responseModel.getTaskDetails().getImages().contains(".json")) {
                            ivBanner.setVisibility(View.GONE);
                            ivLottieView.setVisibility(View.VISIBLE);
                            BIG_Common_Utils.setLottieAnimation(ivLottieView, responseModel.getTaskDetails().getImages());
                            ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                        } else {
                            ivBanner.setVisibility(View.VISIBLE);
                            ivLottieView.setVisibility(View.GONE);
                            Glide.with(getApplicationContext())
                                    .load(responseModel.getTaskDetails().getImages())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                            ivBanner.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.big_rectangle_white));
                                            return false;
                                        }
                                    })
                                    .into(ivBanner);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Load home note webview top
            try {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
                    WebView webNote = findViewById(R.id.webNote);
                    webNote.getSettings().setJavaScriptEnabled(true);
                    webNote.setVisibility(View.VISIBLE);
                    webNote.loadDataWithBaseURL(null, responseModel.getHomeNote(), "text/html", "UTF-8", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load top ad
            try {
                if (responseModel.getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTopAds().getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    BIG_Common_Utils.loadTopBannerAd(Big_TaskDetailsInfoActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseModel.getTaskDetails().getIcon() != null) {
                if (responseModel.getTaskDetails().getIcon().contains(".json")) {
                    ivSmallIcon.setVisibility(View.GONE);
                    ltSmallIcon.setVisibility(View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(ltSmallIcon, responseModel.getTaskDetails().getIcon());
                    ltSmallIcon.setRepeatCount(LottieDrawable.INFINITE);
                } else {
                    ivSmallIcon.setVisibility(View.VISIBLE);
                    ltSmallIcon.setVisibility(View.GONE);
                    Glide.with(getApplicationContext())
                            .load(responseModel.getTaskDetails().getIcon())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    ivSmallIcon.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.big_rectangle_white));
                                    return false;
                                }
                            })
                            .into(ivSmallIcon);
                }
            }

            lInstallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
//                        BIG_AppLogger.getInstance().e("onclick", "" + responseModel.getTaskDetails());
                        BIG_Common_Utils.logFirebaseEvent(Big_TaskDetailsInfoActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Task_Details_BigPrize", "Action Button Clicked");
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getScreenNo()) && responseModel.getTaskDetails().getScreenNo().equals("2")) { // open url in external browser
                            BIG_Activity_Manager.isShowAppOpenAd = false;
                        }
                        BIG_Common_Utils.Redirect(Big_TaskDetailsInfoActivity.this, responseModel.getTaskDetails().getScreenNo(), responseModel.getTaskDetails().getTitle(), responseModel.getTaskDetails().getUrl(), responseModel.getTaskDetails().getId(), responseModel.getTaskDetails().getId(), responseModel.getTaskDetails().getImages());
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_TaskDetailsInfoActivity.this);
                    }
                }
            });

            if (responseModel.getTaskDetails().getTitle() != null) {
                txtTitle.setText(responseModel.getTaskDetails().getTitle());
                tvTitle.setText(responseModel.getTaskDetails().getTitle());
            }

            if (responseModel.getTaskDetails().getDescription() != null) {
                txtSubtitle.setText(responseModel.getTaskDetails().getDescription());
            }

            if (responseModel.getTaskDetails().getIsImageUpload() != null) {
                if (responseModel.getTaskDetails().getIsImageUpload().matches("0")) {
                    lPickImage.setVisibility(View.GONE);
                    cardUploadImage.setVisibility(View.GONE);
                } else {
                    lPickImage.setVisibility(View.VISIBLE);
                    cardUploadImage.setVisibility(View.VISIBLE);
                }
            } else {
                lPickImage.setVisibility(View.GONE);
                cardUploadImage.setVisibility(View.GONE);
            }

            if (responseModel.getTaskDetails().getPoints() != null) {
                if (responseModel.getTaskDetails().getPoints().matches("0")) {
                    cardPoint.setVisibility(View.GONE);
                } else {
                    cardPoint.setVisibility(View.VISIBLE);
                }
            }

            if (responseModel.getTaskDetails().getPoints() != null) {
                try {
                    txtPoints.setText(responseModel.getTaskDetails().getPoints());
//                    TextView tvTaskPoints = findViewById(R.id.tvTaskPoints);
                    TextView tvTaskRupees = findViewById(R.id.tvTaskRupees);
//                    tvTaskPoints.setText(responseModel.getTaskDetails().getPoints());
                    tvTaskRupees.setText(BIG_Common_Utils.convertPointsInINR(responseModel.getTaskDetails().getPoints(), new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class).getPointValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (responseModel.getTaskDetails().getImageUploadTitle() != null) {
                txtTitleUpload.setText(responseModel.getTaskDetails().getImageUploadTitle());
            }

            if (responseModel.getTaskDetails().getYoutubeLink() != null && !responseModel.getTaskDetails().getYoutubeLink().isEmpty()) {
                cardWatchVideo.setVisibility(View.VISIBLE);
                if (responseModel.getTaskDetails().getYoutubeImage() != null && !responseModel.getTaskDetails().getYoutubeImage().isEmpty()) {
                    relVideoTutorial.setVisibility(View.GONE);
                    layoutYoutubeImage.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(responseModel.getTaskDetails().getYoutubeImage())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(ivVideoTutorial);
                } else {
                    layoutYoutubeImage.setVisibility(View.GONE);
                    relVideoTutorial.setVisibility(View.VISIBLE);
                }
                Glide.with(getApplicationContext())
                        .load(getResources().getDrawable(R.drawable.big_left_finger))
                        .into(ivGifFinger3);
                Glide.with(getApplicationContext())
                        .load(getResources().getDrawable(R.drawable.big_left_finger))
                        .into(ivGifFinger4);
            } else {
                cardWatchVideo.setVisibility(View.GONE);
            }

            lWatch.setOnClickListener(v -> {
                if (responseModel.getTaskDetails().getYoutubeLink() != null) {
                    BIG_Common_Utils.openUrl(Big_TaskDetailsInfoActivity.this, responseModel.getTaskDetails().getYoutubeLink());
                }
            });
            lTaskMain.setVisibility(View.VISIBLE);
            layoutButton.setVisibility(View.VISIBLE);

//            Animation animUpDown = AnimationUtils.loadAnimation(Big_TaskDetailsInfoActivity.this, R.anim.big_left_right);
//            animUpDown.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    viewShine.startAnimation(animUpDown);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            // start the animation
//            viewShine.startAnimation(animUpDown);
            if (responseModel.getTaskDetails().getIsShareTask() != null && responseModel.getTaskDetails().getIsShareTask().equals("1")) {
                cardReferTask.setVisibility(View.VISIBLE);
                tvReferTaskPoints.setText(responseModel.getTaskDetails().getShareTaskPoint());
//                if(!CommonMethodsUtils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareBtnText())){
//                    btnShareLink.setText(responseModel.getTaskDetails().getShareBtnText());
//                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareBtnNote())) {
                    TextView tvShareBtnNote = findViewById(R.id.tvShareBtnNote);
                    tvShareBtnNote.setText(responseModel.getTaskDetails().getShareBtnNote());
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareNote())) {
                    TextView tvShareNote = findViewById(R.id.tvShareNote);
                    tvShareNote.setText(responseModel.getTaskDetails().getShareNote());
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareTitle())) {
                    tvShareTitle = findViewById(R.id.tvShareTitle);
                    tvShareTitle.setText(responseModel.getTaskDetails().getShareTitle());
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareMessage())) {
                    TextView tvTopNote = findViewById(R.id.tvTopNote);
                    tvTopNote.setText(responseModel.getTaskDetails().getShareMessage());
                }
                layoutShareWA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {

                            if (BIG_Common_Utils.isNetworkAvailable(Big_TaskDetailsInfoActivity.this)) {
//
                                BIG_AppLogger.getInstance().e("#WPTASK--)", new Gson().toJson(responseModelShare));
                                if (responseModelShare == null) {
                                    new Big_SaveShareTaskAsync(Big_TaskDetailsInfoActivity.this, taskId, "1");

                                } else {
                                    responseModelShare.setType("1");
                                    saveShareTaskOffer(responseModelShare);
                                }
                            } else {
                                BIG_Common_Utils.setToast(Big_TaskDetailsInfoActivity.this, "No internet connection");

                            }
                        } else {
                            BIG_Common_Utils.NotifyLogin(Big_TaskDetailsInfoActivity.this);
                        }
                    }
                });
                layoutShareOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                            if (BIG_Common_Utils.isNetworkAvailable(Big_TaskDetailsInfoActivity.this)) {
                                if (responseModelShare == null) {
                                    new Big_SaveShareTaskAsync(Big_TaskDetailsInfoActivity.this, taskId, "2");
                                } else {
                                    responseModelShare.setType("2");
                                    saveShareTaskOffer(responseModelShare);
                                }
                            } else {
                                BIG_Common_Utils.setToast(Big_TaskDetailsInfoActivity.this, "No internet connection");
                            }
                        } else {
                            BIG_Common_Utils.NotifyLogin(Big_TaskDetailsInfoActivity.this);
                        }
                    }
                });
                layoutCopyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                            if (BIG_Common_Utils.isNetworkAvailable(Big_TaskDetailsInfoActivity.this)) {
                                if (responseModelShare == null) {
                                    new Big_SaveShareTaskAsync(Big_TaskDetailsInfoActivity.this, taskId, "3");
                                } else {
                                    responseModelShare.setType("3");
                                    saveShareTaskOffer(responseModelShare);
                                }
                            } else {
                                BIG_Common_Utils.setToast(Big_TaskDetailsInfoActivity.this, "No internet connection");
                            }
                        } else {
                            BIG_Common_Utils.NotifyLogin(Big_TaskDetailsInfoActivity.this);
                        }
                    }
                });
            } else {
                cardReferTask.setVisibility(View.GONE);
            }

//            if (responseModel.getTaskDetails().getIsScratchCard() != null && responseModel.getTaskDetails().getIsScratchCard().equals("1")) {
//                layoutScratchCard.setVisibility(View.VISIBLE);
//                tvScratchCard.setText(tvScratchCard.getText() + "\nEarn upto 10-" + responseModel.getTaskDetails().getPoints() + " Rubies.");
//                layoutScratchCard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent(Big_TaskDetailsInfoActivity.this, Big_ScratchCardGameActivity.class));
//                    }
//                });
//                Glide.with(getApplicationContext())
//                        .load(getResources().getDrawable(R.drawable.big_left_finger))
//                        .into(ivGifFinger1);
//                Glide.with(getApplicationContext())
//                        .load(getResources().getDrawable(R.drawable.big_left_finger))
//                        .into(ivGifFinger2);
//            } else {
//                layoutScratchCard.setVisibility(View.GONE);
//            }


            if (responseModel.getTaskDetails().getNote() != null && !responseModel.getTaskDetails().getNote().isEmpty()) {
                layoutNote.setVisibility(View.VISIBLE);
                txtNote.setText("Note: " + responseModel.getTaskDetails().getNote());
//                Animation animUpDown1 = AnimationUtils.loadAnimation(Big_TaskDetailsInfoActivity.this, R.anim.big_left_right_slow);
//                animUpDown1.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        viewShineNote.startAnimation(animUpDown1);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
//                        getResources().getDimensionPixelSize(R.dimen.dim_60), txtNote.getMeasuredHeight());
//                viewShineNote.setLayoutParams(rel_btn);
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // start the animation
//                        viewShineNote.startAnimation(animUpDown1);
//                    }
//                }, 500);
            } else {
//                View sepNote = findViewById(R.id.sepNote);
//                sepNote.setVisibility(View.VISIBLE);
                layoutNote.setVisibility(View.GONE);
            }

//            if (responseModel.getTaskDetails().getStapes() != null) {
//                String start = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/Medium.ttf\")}body {font-family: MyFont;font-size: small;text-align: justify;}</style></head><body>";
//                String end = "</body></html>";
//                String myHtmlString = start + responseModel.getTaskDetails().getStapes() + end;
//                    webTaskStep.loadData(responseModel.getTaskDetails().getStapes(), "text/html", "UTF-8");
//            }else {
//                cardHowToClaim.setVisibility(View.GONE);
//            }
//
//            if (responseModel.getTaskDetails().getTnc() != null) {
//                String start = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/Medium.ttf\")}body {font-family: MyFont;font-size: small;text-align: justify;}</style></head><body>";
//                String end = "</body></html>";
//                String myHtmlString = start + responseModel.getTaskDetails().getTnc() + end;
//                webDisclaimer.loadData(responseModel.getTaskDetails().getTnc(), "text/html", "UTF-8");
//            }
            if (responseModel.getTaskDetails().getFootstep() != null && responseModel.getTaskDetails().getFootstep().size() > 0) {
                rvFootSteps.setLayoutManager(new LinearLayoutManager(Big_TaskDetailsInfoActivity.this));
                rvFootSteps.setAdapter(new Big_SimpleText_Adapter(responseModel.getTaskDetails().getFootstep(), Big_TaskDetailsInfoActivity.this));
                rvFootSteps.setVisibility(View.VISIBLE);
                webTaskStep.setVisibility(View.GONE);
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getStapes())) {
                webTaskStep.loadData(responseModel.getTaskDetails().getStapes(), "text/html", "UTF-8");
            } else {
                cardHowToClaim.setVisibility(View.GONE);
            }
            if (responseModel.getTaskDetails().getTncList() != null) {
                //PP_Applogger.getInstance().e("tncLuist", "" + responseModel.getTaskDetails().getTncList().size());
                rvTnC.setLayoutManager(new LinearLayoutManager(Big_TaskDetailsInfoActivity.this));
                rvTnC.setAdapter(new Big_SimpleText_Adapter(responseModel.getTaskDetails().getTncList(), Big_TaskDetailsInfoActivity.this));
                rvTnC.setVisibility(View.VISIBLE);
                webDisclaimer.setVisibility(View.GONE);
            } else {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getTnc())) {
                    webDisclaimer.setVisibility(View.VISIBLE);
                    rvTnC.setVisibility(View.GONE);
                    webDisclaimer.loadData(responseModel.getTaskDetails().getTnc(), "text/html", "UTF-8");
                } else {
                    webDisclaimer.setVisibility(View.GONE);
                    cardDisclaimer.setVisibility(View.GONE);
                }
            }

//            if (responseModel.getTaskDetails().getBtnColor() != null && responseModel.getTaskDetails().getBtnColor().length() > 0) {
//                Drawable mDrawable = ContextCompat.getDrawable(Big_TaskDetailsInfoActivity.this, R.drawable.big_ic_btn_gradient_rounded_corner_rect_new);
//                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getTaskDetails().getBtnColor()), PorterDuff.Mode.SRC_IN));
//                lInstallBtn.setBackground(mDrawable);
//
////                Drawable mDrawable1 = ContextCompat.getDrawable(TaskDetailsInfoActivity.this, R.drawable.bg_points_conversion);
////                mDrawable1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getTaskDetails().getBtnColor()), PorterDuff.Mode.SRC_IN));
////                layoutPointss.setBackground(mDrawable1);
//            }
            if (responseModel.getTaskDetails().getBtnName() != null) {
                lInstallBtn.setText(responseModel.getTaskDetails().getBtnName());
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getNote())) {
                BIG_Common_Utils.NotifyMessage(Big_TaskDetailsInfoActivity.this, "Important Note!", responseModel.getTaskDetails().getNote(), false);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 74:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BIG_Activity_Manager.isShowAppOpenAd = false;
                    txtFileName.setText("Click here to select image");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                } else {
                    BIG_Common_Utils.setToast(Big_TaskDetailsInfoActivity.this, "Allow permission for storage access!");
                }
                break;
            case 774:
                try {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        shareImageData1();
                    } else {
                        BIG_Common_Utils.setToast(Big_TaskDetailsInfoActivity.this, "Allow permission for storage access!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void uploadTaskImageSuccess() {
        imagePath = null;
        txtFileName.setText("Click here to select image");
        loadSelectImage.setImageResource(R.drawable.big_ic_gallery);
        btnUpload.setVisibility(View.GONE);
    }

    public void UploadImage() {
        new Big_Task_Image_Upload_Async(Big_TaskDetailsInfoActivity.this, taskId, objTask.getTaskDetails().getTitle(), imagePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                if (data != null) {
                    try {
                        final Uri selectedUri = data.getData();
                        if (selectedUri != null) {
                            imagePath = BIG_Common_Utils.getPathFromURI(Big_TaskDetailsInfoActivity.this, selectedUri);
                            Glide.with(getApplicationContext())
                                    .load(imagePath)
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                    .into(loadSelectImage);
                            txtFileName.setText(new File(imagePath).getName().toString());
                            btnUpload.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(Big_TaskDetailsInfoActivity.this, "Cannot retrieve selected image", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class).getLovinNativeID()), Big_TaskDetailsInfoActivity.this);
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
                    lblLoadingAds.setVisibility(View.GONE);
                    layoutAds.setVisibility(View.VISIBLE);

                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
                    layoutAds.setVisibility(View.GONE);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                    frameLayoutNativeAd = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveShareTaskOffer(Big_ReferResponseModel responseModelShare1) {
        try {
//            MAX_AppLogger.getInstance().e("#response_save", new Gson().toJson(responseModelShare1));
            responseModelShare = responseModelShare1;
            if (responseModelShare != null) {
                if (responseModelShare1.getType().equals("3")) {
                    try {
                        ClipboardManager clipboard = (ClipboardManager) Big_TaskDetailsInfoActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", responseModelShare.getShareUrl());
                        clipboard.setPrimaryClip(clip);
                        BIG_Common_Utils.setToast(Big_TaskDetailsInfoActivity.this, "Copied!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (responseModelShare.getShareImage() != null && !responseModelShare.getShareImage().isEmpty()) {
                        if (Build.VERSION.SDK_INT <= 32) {
                            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 774);
                            } else {
                                shareImageData1();
                            }
                        } else {
                            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES);
                            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 774);
                            } else {
                                shareImageData1();
                            }
                        }
                    } else {
                        shareImageData1();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            MAX_AppLogger.getInstance().e("#catch", e.getMessage());
        }
    }

    public void shareImageData1() {

        BIG_AppLogger.getInstance().e("#WPTASK--)", new Gson().toJson(responseModelShare));
        try {
            BIG_Activity_Manager.isShowAppOpenAd = false;
            Intent share;
            if (responseModelShare.getShareImage().trim().length() > 0 && responseModelShare.getType().equals("1")) {
                File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_PICTURES) + File.separator)));
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String[] str = responseModelShare.getShareImage().trim().split("/");
                String extension = "";
                if (str[str.length - 1].contains(".")) {
                    extension = str[str.length - 1].substring(str[str.length - 1].lastIndexOf("."));
                    str[str.length - 1] = str[str.length - 1].substring(0, str[str.length - 1].lastIndexOf(".")) + "_" + taskId;
                }
                if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".gif")) {
                    // extension = "";
                } else {
                    extension = ".png";
                }

                File file = new File(dir, str[str.length - 1] + extension);

                BIG_AppLogger.getInstance().e("#responsetype111--)",""+file.exists());
                if (file.exists()) {
                    try {
                        share = new Intent(Intent.ACTION_SEND);
                        Uri uri = null;
                        if (Build.VERSION.SDK_INT >= 24) {
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        share.setType("image/*");
                        if (responseModelShare.getShareImage().contains(".gif")) {
                            share.setType("image/gif");
                        } else {
                            share.setType("image/*");
                        }
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        share.setPackage(BIG_Constants.whatsappPackageName);
                        share.putExtra(Intent.EXTRA_TEXT, responseModelShare.getType().equals("1") ? responseModelShare.getShareMessageWhatsApp() : Html.fromHtml(responseModelShare.getShareMessage()).toString());
                        Log.d("S", "shareImageData1: "+responseModelShare.getShareMessageWhatsApp());
                        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(share, PackageManager.MATCH_DEFAULT_ONLY);

                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }

                        startActivity(Intent.createChooser(share, "Share Task"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (BIG_Common_Utils.isNetworkAvailable(Big_TaskDetailsInfoActivity.this)) {

                    new Big_Download_Image_Share_Async(Big_TaskDetailsInfoActivity.this, file, responseModelShare.getShareImage(), responseModelShare.getType().equals("1") ? responseModelShare.getShareMessageWhatsApp() : Html.fromHtml(responseModelShare.getShareMessage()).toString(), responseModelShare.getType()).execute();
                    BIG_AppLogger.getInstance().e("shareimage11=",responseModelShare.getShareImage());
               }
            } else {
                try {
                    share = new Intent(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    share.putExtra(Intent.EXTRA_TEXT, responseModelShare.getType().equals("1") ? responseModelShare.getShareMessageWhatsApp() : Html.fromHtml(responseModelShare.getShareMessage()).toString());
                    if (responseModelShare.getType().equals("1")) {
                        share.setPackage(BIG_Constants.whatsappPackageName);
                    }
                    share.setType("text/plain");
                    startActivity(Intent.createChooser(share, "Share Task"));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}