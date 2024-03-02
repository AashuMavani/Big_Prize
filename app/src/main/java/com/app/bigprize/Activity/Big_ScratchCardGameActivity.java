package com.app.bigprize.Activity;

import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_Scratch_Card_Game_List_Adapter;
import com.app.bigprize.Async.Big_Get_Scratch_Card_List_Async;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Scratch_Card_List;
import com.app.bigprize.Async.Models.Big_Scratch_Card_Model;
import com.app.bigprize.Async.Big_Save_Scratch_Card_Async;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.myinnos.androidscratchcard.ScratchCard;

public class Big_ScratchCardGameActivity extends AppCompatActivity {
    private RecyclerView rvScratchCardList;
    private TextView tvPoints, lblLoadingAds, tvTimer;
    private LottieAnimationView ivLottieNoData;
    private Big_Response_Model responseMain;
    private LinearLayout layoutPoints;
    private CountDownTimer timer;
    private int time, selectedPos = -1;
    private String todayDate, lastDate, scratchTime;
    private Big_Scratch_Card_Model objScratchCardModel;
    private ImageView ivHistory, parentBackgroundImage;
    private MaxAd nativeAd, nativeAdWin, nativeAdScratchDialog;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderScratchDialog;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private RelativeLayout layoutMain;
    private List<Big_Scratch_Card_List> listScratchCards = new ArrayList<>();
    private Big_Scratch_Card_Game_List_Adapter adapter;
    private boolean isTimerSet = false;
    private Dialog dialogScratchCard;
    private ScratchCard scratchCard;
    private ImageView ivFrontImage, ivHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ScratchCardGameActivity.this);
        setContentView(R.layout.big_activity_scratch_card_game2);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        ivHelp = findViewById(R.id.ivHelp);
        rvScratchCardList = findViewById(R.id.rvScratchCardList);
        layoutMain = findViewById(R.id.layoutMain);
        ivHistory = findViewById(R.id.ivHistory);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ScratchCardGameActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ScratchCardGameActivity.this);
                }
            }
        });
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());

        parentBackgroundImage = findViewById(R.id.parentBackgroundImage);

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //     Common_Utils.startRoundAnimation(ScratchCardGameActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ScratchCardGameActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.SCRATCH_CARD)
                            .putExtra("title", "Scratch Tickets"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ScratchCardGameActivity.this);
                }
            }
        });
        new Big_Get_Scratch_Card_List_Async(Big_ScratchCardGameActivity.this);
    }


    public void changeScratchCardDataValues(Big_Scratch_Card_Model responseModel) {
        try {
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

            if (responseModel.getTodayDate() != null) {
                todayDate = responseModel.getTodayDate();
            }
            if (responseModel.getLastScratchedDate() != null) {
                lastDate = responseModel.getLastScratchedDate();
            }
            if (responseModel.getScratchTime() != null) {
                scratchTime = responseModel.getScratchTime();
            }

            BIG_Common_Utils.logFirebaseEvent(Big_ScratchCardGameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Scratch_Card_BigPrize", "Scratch Card Got Reward");
            showWinPopup(listScratchCards.get(selectedPos).getScratchCardPoints());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWinPopup(String point) {
        Dialog dialogWin = new Dialog(Big_ScratchCardGameActivity.this, android.R.style.Theme_Light);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.setContentView(R.layout.big_dialog_win_spin);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        FrameLayout fl_adplaceholder = dialogWin.findViewById(R.id.fl_adplaceholder);
        TextView lblLoadingAds = dialogWin.findViewById(R.id.lblLoadingAds);
        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds(fl_adplaceholder, lblLoadingAds);
        } else {
            lblLoadingAds.setVisibility(View.GONE);
        }

        TextView tvPoint = dialogWin.findViewById(R.id.tvPoints);

        LottieAnimationView animation_view = dialogWin.findViewById(R.id.animation_view);
        BIG_Common_Utils.setLottieAnimation(animation_view, responseMain.getCelebrationLottieUrl());
        animation_view.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                super.onAnimationStart(animation, isReverse);
                BIG_Common_Utils.startTextCountAnimation(tvPoint, point);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animation_view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        ImageView ivClose = dialogWin.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogWin != null) {
                    dialogWin.dismiss();
                }
            }
        });

        TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
        AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
        try {
            int pt = Integer.parseInt(point);
            lblPoints.setText((pt <= 1 ? "Ruby" : "Rubies"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            lblPoints.setText("Rubies");
        }

        btnOk.setOnClickListener(v -> {
            if (dialogWin != null) {
                dialogWin.dismiss();
            }
        });
        dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dialogScratchCard != null && dialogScratchCard.isShowing()) {
                    dialogScratchCard.dismiss();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BIG_Common_Utils.GetCoinAnimation(Big_ScratchCardGameActivity.this, layoutMain, layoutPoints);
                            tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                            listScratchCards.get(selectedPos).setIsScratched("1");
                            adapter.notifyItemChanged(selectedPos);
                            selectedPos = -1;
                            setTimer(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 200);
            }
        });
        if (!isFinishing() && !dialogWin.isShowing()) {
            dialogWin.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animation_view.setVisibility(View.VISIBLE);
                    animation_view.playAnimation();
                }
            }, 500);
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ScratchCardGameActivity.this);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ScratchCardGameActivity.this);
            nativeAdLoaderWin.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdWin != null) {
                        nativeAdLoaderWin.destroy(nativeAdWin);
                    }
                    nativeAdWin = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    lblLoadingAds.setVisibility(View.GONE);
                    frameLayoutNativeAd.addView(nativeAdView);
                    frameLayoutNativeAd.setVisibility(VISIBLE);
                    //AppLogger.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
                    frameLayoutNativeAd.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoaderWin.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAdsScratchDialog(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderScratchDialog = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ScratchCardGameActivity.this);
            nativeAdLoaderScratchDialog.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdScratchDialog != null) {
                        nativeAdLoaderScratchDialog.destroy(nativeAdScratchDialog);
                    }
                    nativeAdScratchDialog = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    lblLoadingAds.setVisibility(View.GONE);
                    frameLayoutNativeAd.addView(nativeAdView);
                    frameLayoutNativeAd.setVisibility(VISIBLE);
                    //AppLogger.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
                    frameLayoutNativeAd.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoaderScratchDialog.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (timer != null) {
                    timer.cancel();
                }
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                }
                if (nativeAdWin != null && nativeAdLoaderWin != null) {
                    nativeAdLoaderWin.destroy(nativeAdWin);
                    nativeAdWin = null;
                }
                if (nativeAdScratchDialog != null && nativeAdLoaderScratchDialog != null) {
                    nativeAdLoaderScratchDialog.destroy(nativeAdScratchDialog);
                    nativeAdScratchDialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


    public void setData(Big_Scratch_Card_Model responseModel) {
        objScratchCardModel = responseModel;
        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHelpVideoUrl())) {
            ivHelp.setVisibility(VISIBLE);
            ivHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BIG_Common_Utils.openUrl(Big_ScratchCardGameActivity.this, responseModel.getHelpVideoUrl());
                }
            });
        }

        if (responseModel.getScratchCardList() != null && responseModel.getScratchCardList().size() > 0) {
            try {
                if (responseModel.getTodayDate() != null) {
                    todayDate = responseModel.getTodayDate();
                }
                if (responseModel.getLastScratchedDate() != null) {
                    lastDate = responseModel.getLastScratchedDate();
                }

                if (responseModel.getScratchTime() != null) {
                    scratchTime = responseModel.getScratchTime();
                }
                Glide.with(Big_ScratchCardGameActivity.this).load(responseModel.getBackgroundImage()).into(parentBackgroundImage);

                listScratchCards.addAll(responseModel.getScratchCardList());
                if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                    if (listScratchCards.size() <= 4) {
                        listScratchCards.add(listScratchCards.size(), new Big_Scratch_Card_List());
                    } else {
                        for (int i2 = 0; i2 < this.listScratchCards.size(); i2++) {
                            if ((i2 + 1) % 5 == 0) {
                                //AppLogger.getInstance().e("POSITION AD", "==================" + i2);
                                listScratchCards.add(i2, new Big_Scratch_Card_List());
                            }
                        }
                    }
                }
                adapter = new Big_Scratch_Card_Game_List_Adapter(listScratchCards, Big_ScratchCardGameActivity.this, responseModel.getBackImage(), responseModel.getFrontImage(), new Big_Scratch_Card_Game_List_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        try {
                            if (listScratchCards.get(position).getIsScratched() != null && listScratchCards.get(position).getIsScratched().equals("0")) {
                                selectedPos = position;
                                showScratchCardDialog();
                            } else {
                                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ScratchCardGameActivity.this, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                GridLayoutManager mGridLayoutManager = new GridLayoutManager(Big_ScratchCardGameActivity.this, 2);
                mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (adapter.getItemViewType(position) == Big_Scratch_Card_Game_List_Adapter.ITEM_AD) {
                            return 2;
                        }
                        return 1;
                    }
                });
                rvScratchCardList.setLayoutManager(mGridLayoutManager);
                rvScratchCardList.setAdapter(adapter);

                boolean isAllCardsScratched = true;
                for (int i = 0; i < listScratchCards.size(); i++) {
                    if (listScratchCards.get(i).getIsScratched() != null && listScratchCards.get(i).getIsScratched().equals("0")) {
                        isAllCardsScratched = false;
                        break;
                    }
                }
                if (!isAllCardsScratched) {
                    setTimer(true);
                } else {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ScratchCardGameActivity.this, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                    BIG_Common_Utils.loadTopBannerAd(Big_ScratchCardGameActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ivLottieNoData = findViewById(R.id.ivLottieNoData);
            ivLottieNoData.setVisibility(View.VISIBLE);
            ivLottieNoData.playAnimation();
            lblLoadingAds = findViewById(R.id.lblLoadingAds);
            layoutAds = findViewById(R.id.layoutAds);
            layoutAds.setVisibility(View.VISIBLE);
            frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds();
            } else {
                layoutAds.setVisibility(View.GONE);
            }
        }
    }

    private void showScratchCardDialog() {
        try {
            dialogScratchCard = new Dialog(Big_ScratchCardGameActivity.this, android.R.style.Theme_Light);
            dialogScratchCard.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogScratchCard.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogScratchCard.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogScratchCard.setContentView(R.layout.big_dialog_scratch_card);
            dialogScratchCard.setCancelable(true);

            FrameLayout fl_adplaceholder = dialogScratchCard.findViewById(R.id.fl_adplaceholder);
            TextView lblLoadingAds = dialogScratchCard.findViewById(R.id.lblLoadingAds);
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAdsScratchDialog(fl_adplaceholder, lblLoadingAds);
            } else {
                lblLoadingAds.setVisibility(View.GONE);
            }

            tvTimer = dialogScratchCard.findViewById(R.id.tvTimer);
            tvTimer.setVisibility(isTimerSet ? VISIBLE : View.GONE);

            ivFrontImage = dialogScratchCard.findViewById(R.id.ivFrontImage);
            ivFrontImage.setVisibility(isTimerSet ? VISIBLE : View.GONE);

            TextView tvMessage = dialogScratchCard.findViewById(R.id.tvMessage);
            TextView tvTaskName = dialogScratchCard.findViewById(R.id.tvTaskName);
            tvTaskName.setText(listScratchCards.get(selectedPos).getTaskTitle());
            TextView tvPoints = dialogScratchCard.findViewById(R.id.tvPoints);
            tvPoints.setText(listScratchCards.get(selectedPos).getScratchCardPoints() + " Rubies");

            ArrayList<String> listMessage = new ArrayList<>();
            listMessage.add("Amazing!");
            listMessage.add("Fantastic!");
            listMessage.add("Cool!");
            listMessage.add("Super!");
            listMessage.add("Nice!");
            listMessage.add("Wow!");
            listMessage.add("Great!");
            listMessage.add("Congratulations!");

            ImageView ivClose = dialogScratchCard.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogScratchCard.dismiss();
                }
            });

            ImageView ivBackImage = dialogScratchCard.findViewById(R.id.ivBackImage);
            Glide.with(Big_ScratchCardGameActivity.this)
                    .load(objScratchCardModel.getBackImage())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(ivBackImage);

            scratchCard = dialogScratchCard.findViewById(R.id.scratchCard);
            scratchCard.setVisibility(isTimerSet ? View.INVISIBLE : VISIBLE);
            scratchCard.setScratchWidth(getResources().getDimensionPixelSize(R.dimen.dim_25));
            scratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
                @Override
                public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                    if (visiblePercent > 0.6) {
                        scratchCard.setVisibility(View.GONE);
                        tvMessage.setText(BIG_Common_Utils.getRandomAdUnitId(listMessage));
                        tvMessage.setVisibility(VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ScratchCardGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
                                    @Override
                                    public void onAdDismiss() {
                                        try {
                                            new Big_Save_Scratch_Card_Async(Big_ScratchCardGameActivity.this, listScratchCards.get(selectedPos).getId(), listScratchCards.get(selectedPos).getScratchCardPoints());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }, 500);
                    }
                }
            });


            Glide.with(this)
                    .asBitmap()
                    .load(objScratchCardModel.getFrontImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.dim_10))))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                ivFrontImage.setImageBitmap(resource);
                                scratchCard.setScratchDrawable(new BitmapDrawable(getResources(), resource));
                                dialogScratchCard.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimer(boolean isFromOnCreate) {
        try {
            if (BIG_Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(scratchTime)) {
                isTimerSet = false;
                // allow scratch
            } else {
                isTimerSet = true;
                // disable scratch
                if (timer != null) {
                    timer.cancel();
                }
                time = BIG_Common_Utils.timeDiff(todayDate, lastDate);
                timer = new CountDownTimer((Integer.parseInt(scratchTime) - time) * 60000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        try {
                            if (tvTimer != null) {
                                tvTimer.setText(BIG_Common_Utils.updateTimeRemainingScratch(millisUntilFinished));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        isTimerSet = false;
                        if (tvTimer != null) {
                            tvTimer.setVisibility(View.GONE);
                            scratchCard.setVisibility(VISIBLE);
                            ivFrontImage.setVisibility(View.INVISIBLE);
                        }
                    }
                }.start();
                if (isFromOnCreate) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ScratchCardGameActivity.this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}