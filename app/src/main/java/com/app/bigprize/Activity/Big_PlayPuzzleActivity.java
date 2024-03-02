package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Math.abs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Async.Big_Get_Image_Puzzle_Async;
import com.app.bigprize.Async.Models.Big_Image_Puzzle_Response_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Big_Save_Image_Puzzle_Async;
import com.app.bigprize.Customviews.Big_Imagepuzzle.Big_Puzzle_Piece;
import com.app.bigprize.Customviews.Big_Imagepuzzle.Big_Touch_Listener;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_Custom_ScrollView;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Big_PlayPuzzleActivity extends AppCompatActivity {
    private ArrayList<Big_Puzzle_Piece> pieces;
    private TextView tvMainTimer, lblSubTitle, tvTimer, tvPoints, lblLoadingAds, tvWinningPoints;
    private LinearLayout layoutPoints, layoutNoData;
    private RelativeLayout layoutData;
    private ConstraintLayout layoutContent;
    private ImageView ivHistory, ivHelp;
    private Big_Response_Model responseMain;
    private Big_Image_Puzzle_Response_Model objImagePuzzle;
    private LottieAnimationView ivLottieNoData;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private LinearLayout layoutAds;
    private FrameLayout frameLayoutNativeAd;

    private RelativeLayout layoutMain;
    private CountDownTimer mainTimer, puzzleTimer;
    private String todayDate, lastDate, puzzleTimeGapInMinutes;
    private boolean isMainTimerSet, isTimerOver;
    private RelativeLayout layoutImagePieces;
    private BIG_Custom_ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_PlayPuzzleActivity.this);
        setContentView(R.layout.big_activity_play_puzzle);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        setViews();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setViews() {
        scrollView = findViewById(R.id.scrollView);
        layoutImagePieces = findViewById(R.id.layoutImagePieces);
        if (!BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
            layoutImagePieces.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    BIG_Common_Utils.NotifyLogin(Big_PlayPuzzleActivity.this);
                    return false;
                }
            });
        }
        layoutData = findViewById(R.id.layoutData);
        layoutData.setVisibility(View.INVISIBLE);
        tvMainTimer = findViewById(R.id.tvMainTimer);
        layoutMain = findViewById(R.id.layoutMain);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        layoutNoData = findViewById(R.id.layoutNoData);
        layoutContent = findViewById(R.id.layoutContent);
        tvWinningPoints = findViewById(R.id.tvWinningPoints);
        lblSubTitle = findViewById(R.id.lblSubTitle);
        tvTimer = findViewById(R.id.tvTimer);
        ivHelp = findViewById(R.id.ivHelp);
        layoutAds = findViewById(R.id.layoutAds);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(GONE);
        }

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_PlayPuzzleActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_PlayPuzzleActivity.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        //    Common_Utils.startRoundAnimation(PlayPuzzleActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_PlayPuzzleActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.IMAGE_PUZZLE).
                            putExtra("title", "Arrange Image Pieces"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_PlayPuzzleActivity.this);
                }
            }
        });
        new Big_Get_Image_Puzzle_Async(Big_PlayPuzzleActivity.this);
    }

    public void checkGameOver() {
        try {
            if (isGameOver()) {
                if (!isTimerOver) {
                    // Call Save Api
                    if (puzzleTimer != null) {
                        puzzleTimer.cancel();
                    }
                    BIG_Common_Utils.setToast(Big_PlayPuzzleActivity.this, "Well done!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_PlayPuzzleActivity.this, new BIG_Ads_Utils.AdShownListener() {
                                @Override
                                public void onAdDismiss() {
                                    // api call
                                    new Big_Save_Image_Puzzle_Async(Big_PlayPuzzleActivity.this, objImagePuzzle.getPoints(), objImagePuzzle.getId());
                                }
                            });
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutContent.getLayoutParams();
                            params.height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._270sdp);
                            layoutContent.setLayoutParams(params);
                        }
                    }, 500);
                } else {
                    BIG_Common_Utils.logFirebaseEvent(Big_PlayPuzzleActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Image_Puzzle_BigPrize", "Time Over");
                    BIG_Common_Utils.Notify(Big_PlayPuzzleActivity.this, getString(R.string.app_name), "Time is over. Better luck, next time!", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isGameOver() {
        for (Big_Puzzle_Piece piece : pieces) {
            if (piece.canMove) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    public void setData(Big_Image_Puzzle_Response_Model responseModel) {
        objImagePuzzle = responseModel;

        if (objImagePuzzle != null) {
            if (!BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getEarningPoint())) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, objImagePuzzle.getEarningPoint());
                tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getHelpVideoUrl())) {
                ivHelp.setVisibility(VISIBLE);
                ivHelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BIG_Common_Utils.openUrl(Big_PlayPuzzleActivity.this, objImagePuzzle.getHelpVideoUrl());
                    }
                });
            }
            if (objImagePuzzle.getStatus().equals("2") || objImagePuzzle == null) {
                layoutData.setVisibility(GONE);
                layoutNoData.setVisibility(VISIBLE);
                ivLottieNoData.playAnimation();
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_PlayPuzzleActivity.this, null);
            } else {
                layoutData.setVisibility(VISIBLE);
                layoutNoData.setVisibility(GONE);

                // Load home note webview top
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, objImagePuzzle.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load top ad
                try {
                    if (objImagePuzzle.getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getTopAds().getImage())) {
                        LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                        BIG_Common_Utils.loadTopBannerAd(Big_PlayPuzzleActivity.this, layoutTopAds, objImagePuzzle.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (objImagePuzzle.getTodayDate() != null) {
                    todayDate = objImagePuzzle.getTodayDate();
                }
                if (objImagePuzzle.getLastPuzzleDate() != null) {
                    lastDate = objImagePuzzle.getLastPuzzleDate();
                }

                if (objImagePuzzle.getPuzzleTimer() != null) {
                    puzzleTimeGapInMinutes = objImagePuzzle.getPuzzleTimer();// minutes
                }

                setMainTimer(true);
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!isMainTimerSet && !BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getIsTodayTaskCompleted()) && objImagePuzzle.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);

                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(objImagePuzzle.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if(!BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getTaskButton())){
                            btnCompleteTask.setText(objImagePuzzle.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getScreenNo())) {
//                                    if (!BIG_Common_Utils.hasUsageAccessPermission(Big_PlayPuzzleActivity.this)) {
//                                        BIG_Common_Utils.showUsageAccessPermissionDialog(Big_PlayPuzzleActivity.this);
//                                        return;
//                                    } else {
//
//                                    }
                                    BIG_Common_Utils.Redirect(Big_PlayPuzzleActivity.this, objImagePuzzle.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getTaskId())) {
                                    Intent intent = new Intent(Big_PlayPuzzleActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", objImagePuzzle.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_PlayPuzzleActivity.this, Big_TasksCategoryTypeActivity.class);
                                    intent.putExtra("taskTypeId", BIG_Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                                finish();
                            }
                        });
                       /* btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                                if (!Common_Utils.isStringNullOrEmpty(objImagePuzzle.getTaskId())) {
                                    Intent intent = new Intent(PlayPuzzleActivity.this, TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", objImagePuzzle.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(PlayPuzzleActivity.this, TasksCategoryTypeActivity.class);
                                    intent.putExtra("taskTypeId", Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                            }
                        });*/
                    } else {
                        layoutCompleteTask.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tvWinningPoints.setText(objImagePuzzle.getPoints());
                tvTimer.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(objImagePuzzle.getTimer()) * 1000));

                BIG_Common_Utils.showProgressLoader(Big_PlayPuzzleActivity.this);

                final ImageView imageView = findViewById(R.id.imageView);
                // run image related code after the view was laid out
                // to have all dimensions calculated
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(Big_PlayPuzzleActivity.this).asBitmap().load(objImagePuzzle.getImageUrl()).into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                try {
                                    imageView.setImageBitmap(resource);
                                    if ((!BIG_Common_Utils.isStringNullOrEmpty(objImagePuzzle.getIsTodayTaskCompleted()) && objImagePuzzle.getIsTodayTaskCompleted().equals("1"))) {
                                        pieces = splitImage(Integer.parseInt(objImagePuzzle.getPiece()));
                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutContent.getLayoutParams();
                                        params.height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._270sdp) + getResources().getDimensionPixelSize(R.dimen.dim_20) + pieces.get(0).pieceHeight;
                                        layoutContent.setLayoutParams(params);
                                        // shuffle pieces order
                                        Collections.shuffle(pieces);
                                        for (Big_Puzzle_Piece piece : pieces) {
                                            layoutImagePieces.addView(piece);
                                            // randomize position, on the bottom of the screen
                                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                                            lParams.leftMargin = new Random().nextInt(layoutImagePieces.getWidth() - piece.pieceWidth);
                                            lParams.topMargin = layoutImagePieces.getHeight() - piece.pieceHeight;
                                            piece.setLayoutParams(lParams);
                                        }
                                        setImageData();
                                    } else {
                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutContent.getLayoutParams();
                                        params.height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._270sdp);
                                        layoutContent.setLayoutParams(params);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                BIG_Common_Utils.dismissProgressLoader();
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                BIG_Common_Utils.dismissProgressLoader();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                    }
                });
            }
        }
    }

    private void setImageData() {
        try {
            if (!isMainTimerSet && BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                Big_Touch_Listener touchListener = new Big_Touch_Listener(Big_PlayPuzzleActivity.this);
                touchListener.setScrollView(scrollView);
                for (Big_Puzzle_Piece piece : pieces) {
                    piece.setOnTouchListener(touchListener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimer() {
        try {
            if (puzzleTimer == null) {
                puzzleTimer = new CountDownTimer(Integer.parseInt(objImagePuzzle.getTimer()) * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        isTimerOver = false;
                        lblSubTitle.setText("Remaining time is");
                        tvTimer.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        tvTimer.setText("Time is over. Better luck, next time!");
                        tvTimer.setText("");
                        isTimerOver = true;
                        BIG_Common_Utils.logFirebaseEvent(Big_PlayPuzzleActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Image_Puzzle_BigPrize", "Time Over");
                        BIG_Common_Utils.Notify(Big_PlayPuzzleActivity.this, getString(R.string.app_name), "Oops, time is over. Better luck, next time!", true);
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainTimer(boolean isFromOnCreate) {
        try {
            if (BIG_Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(puzzleTimeGapInMinutes)) {
                tvMainTimer.setVisibility(View.GONE);
                isMainTimerSet = false;
            } else {
                isMainTimerSet = true;
                tvMainTimer.setVisibility(VISIBLE);
                if (mainTimer != null) {
                    mainTimer.cancel();
                }
                int time = BIG_Common_Utils.timeDiff(todayDate, lastDate);
                mainTimer = new CountDownTimer((Integer.parseInt(puzzleTimeGapInMinutes) - time) * 60000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvMainTimer.setText("Please Try After\n\n" + BIG_Common_Utils.updateTimeRemainingImagePuzzle(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        tvMainTimer.setVisibility(View.GONE);
                        isMainTimerSet = false;
                        setImageData();
                    }
                }.start();
                if (isFromOnCreate) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_PlayPuzzleActivity.this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onImagePuzzleDataChanged(Big_Image_Puzzle_Response_Model responseModel) {
        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        }
        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastPuzzleDate() != null) {
            lastDate = responseModel.getLastPuzzleDate();
        }
        if (responseModel.getPuzzleTimer() != null) {
            puzzleTimeGapInMinutes = responseModel.getPuzzleTimer();
        }
        //    setMainTimer(false);
        if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
            BIG_Common_Utils.logFirebaseEvent(Big_PlayPuzzleActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Image_Puzzle_BigPrize", "Got Reward");
            showWinPopup(objImagePuzzle.getPoints());
        }
    }

    public void showWinPopup(String point) {
        Dialog dialogWin = new Dialog(Big_PlayPuzzleActivity.this, android.R.style.Theme_Light);
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

        TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
        try {
            int pt = Integer.parseInt(point);
            lblPoints.setText((pt <= 1 ? "Ruby" : "Rubies"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            lblPoints.setText("Rubies");
        }

        AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
        ImageView ivClose = dialogWin.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BIG_Ads_Utils.showAppLovinRewardedAd(Big_PlayPuzzleActivity.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialogWin != null) {
                            dialogWin.dismiss();
                        }
                    }
                });
            }
        });

        btnOk.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinRewardedAd(Big_PlayPuzzleActivity.this, new BIG_Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (dialogWin != null) {
                        dialogWin.dismiss();
                    }
                }
            });
        });
        dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                BIG_Common_Utils.GetCoinAnimation(Big_PlayPuzzleActivity.this, layoutMain, layoutPoints);
                tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_PlayPuzzleActivity.this);
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

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_PlayPuzzleActivity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd.setVisibility(View.VISIBLE);
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
                if (nativeAdWin != null && nativeAdLoaderWin != null) {
                    nativeAdLoaderWin.destroy(nativeAdWin);
                    nativeAdWin = null;
                }
                if (puzzleTimer != null) {
                    puzzleTimer.cancel();
                }
                if (mainTimer != null) {
                    mainTimer.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Big_Puzzle_Piece> splitImage(int piecesNumber) {
        int rows = piecesNumber / 3 == 0 ? 3 : piecesNumber / 3;
        int cols = 3;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<Big_Puzzle_Piece> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        //AppLogger.getInstance().e("SCALED IMAGE", "Width : " + scaledBitmapWidth + " === " + "Height : " + scaledBitmapHeight);
        //AppLogger.getInstance().e("CROPPED IMAGE", "Width : " + croppedImageWidth + " === " + "Height : " + croppedImageHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = croppedImageWidth / cols;
        int pieceHeight = croppedImageHeight / rows;
        //AppLogger.getInstance().e("PIECE IMAGE", "Width : " + pieceWidth + " === " + "Height : " + pieceHeight);
        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                // calculate offset for each piece
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3;
                }

                // apply the offset to each piece
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                Big_Puzzle_Piece piece = new Big_Puzzle_Piece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;

                // this bitmap will hold our final puzzle piece image
                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);

                // draw path
                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (row == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    // top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }

                if (col == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }

                if (row == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }

                if (col == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }

                // mask the piece
                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                // draw a white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                // draw a black border
                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                // set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece);

                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null) return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }


}