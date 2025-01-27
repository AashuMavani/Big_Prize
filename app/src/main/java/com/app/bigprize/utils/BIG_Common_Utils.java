package com.app.bigprize.utils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.airbnb.lottie.BuildConfig;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.app.bigprize.Activity.Big_ActivityDailyLogin;
import com.app.bigprize.Activity.Big_ActivityDailyReward;
import com.app.bigprize.Activity.Big_AdjoeLeaderboardHistoryActivity;
import com.app.bigprize.Activity.Big_AlphabetgameActivity;
import com.app.bigprize.Activity.Big_Brain_trainerActivity;
import com.app.bigprize.Activity.Big_CardMatchActivity;
import com.app.bigprize.Activity.Big_ColorTapActivity;
import com.app.bigprize.Activity.Big_CountUpActivity;
import com.app.bigprize.Activity.Big_Dice_Roll_Activity;
import com.app.bigprize.Activity.Big_EarningOptionsActivity;
import com.app.bigprize.Activity.Big_FeedbackSubmitActivity;
import com.app.bigprize.Activity.Big_GiveAwaySocialActivity;
import com.app.bigprize.Activity.Big_LoginUserActivity;
import com.app.bigprize.Activity.Big_LuckyNumberDrawActivity;
import com.app.bigprize.Activity.Big_LuckySpinWheelActivity;
import com.app.bigprize.Activity.Big_MainActivity;
import com.app.bigprize.Activity.Big_MinesFinderActivity;
import com.app.bigprize.Activity.Big_My_Wallet_Activity;
import com.app.bigprize.Activity.Big_PlayPuzzleActivity;
import com.app.bigprize.Activity.Big_PointHistoryActivity;
import com.app.bigprize.Activity.Big_QuizGameActivity;
import com.app.bigprize.Activity.Big_ReferUsersActivity;
import com.app.bigprize.Activity.Big_ScanActivity;
import com.app.bigprize.Activity.Big_ScanAndPayActivity;
import com.app.bigprize.Activity.Big_ScratchCardGameActivity;
import com.app.bigprize.Activity.Big_SortWordsActivity;
import com.app.bigprize.Activity.Big_SplashScreenActivity;
import com.app.bigprize.Activity.Big_TaskDetailsInfoActivity;
import com.app.bigprize.Activity.Big_TasksCategoryTypeActivity;
import com.app.bigprize.Activity.Big_TypeTextGameActivity;
import com.app.bigprize.Activity.Big_WatchVideoActivity;
import com.app.bigprize.Activity.Big_WebActivity;
import com.app.bigprize.Async.Big_Delete_Account_Async;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Top_Ads;
import com.app.bigprize.Big_App_Controller;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.appsamurai.appsprize.AppsPrize;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.pubscale.sdkone.offerwall.OfferWall;
import com.pubscale.sdkone.offerwall.models.OfferWallListener;
import com.pubscale.sdkone.offerwall.models.Reward;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.RotationRatingBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.adjoe.sdk.Adjoe;
import io.adjoe.sdk.AdjoeException;
import io.adjoe.sdk.AdjoeInitialisationListener;
import io.adjoe.sdk.AdjoeNotInitializedException;


public class BIG_Common_Utils {
    private static Dialog dialogLoader, dialogAdLoader;
    private static Handler handler1;

    public static void showProgressLoader(Context activity) {
        try {
            if (dialogLoader == null || !dialogLoader.isShowing()) {
                //AppLogger.getInstance().e("Activity Loader:", "=================LOADER===============" + activity);
                dialogLoader = new Dialog(activity, android.R.style.Theme_Light);
                dialogLoader.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialogLoader.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogLoader.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialogLoader.setCancelable(true);
                dialogLoader.setCanceledOnTouchOutside(true);
                dialogLoader.setContentView(R.layout.big_dialog_loader);
                dialogLoader.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressLoader() {
        try {
            if (dialogLoader != null && dialogLoader.isShowing()) {
                dialogLoader.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAdLoader(Context activity, String title) {
        try {
            if (dialogAdLoader == null || !dialogAdLoader.isShowing()) {
                dialogAdLoader = new Dialog(activity, android.R.style.Theme_Light);
                dialogAdLoader.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialogAdLoader.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAdLoader.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialogAdLoader.setCancelable(true);
                dialogAdLoader.setCanceledOnTouchOutside(true);
                dialogAdLoader.setContentView(R.layout.big_dialog_ads_loader);
                TextView tvTitle = dialogAdLoader.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
                dialogAdLoader.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("deprecation")
    public static String getIpAddress(Context context) {
        try {
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            if (ip == null || BIG_Common_Utils.isStringNullOrEmpty(ip) || ip.contains("0.0.0.0")) {
                try {
                    List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                    for (NetworkInterface intf : interfaces) {
                        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                        for (InetAddress addr : addrs) {
                            if (!addr.isLoopbackAddress()) {
                                ip = addr.getHostAddress();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void dismissAdLoader() {
        try {
            if (dialogAdLoader != null && dialogAdLoader.isShowing()) {
                dialogAdLoader.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (isStringNullOrEmpty(dateString)) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void RateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static void setDayNightTheme(Activity context) {
        try {
//            if (SharePrefs.getInstance().getString(SharePrefs.NIGHT_MODE) == null || SharePrefs.getInstance().getString(SharePrefs.NIGHT_MODE).length() <= 0) {
            setLightTheme(context);
//            } else if (SharePrefs.getInstance().getString(SharePrefs.NIGHT_MODE).equals("no")) {
//                setLightTheme(context);
//            } else if (SharePrefs.getInstance().getString(SharePrefs.NIGHT_MODE).equals("yes")) {
//                setDarkTheme(context);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLightTheme(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = context.getWindow();
            window.setNavigationBarColor(context.getColor(R.color.background));

            window.setStatusBarColor(context.getColor(R.color.colorPrimary));
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setBackgroundDrawable(context.getDrawable(R.drawable.big_bg_header));
        }
    }

    public static void setDarkTheme(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            if (context instanceof Big_SplashScreenActivity) {
                window.setStatusBarColor(context.getColor(R.color.background));
                window.setNavigationBarColor(context.getColor(R.color.background));
            } else {
                window.setStatusBarColor(context.getColor(android.R.color.transparent));
                window.setNavigationBarColor(context.getColor(R.color.white));
            }
            window.setBackgroundDrawable(context.getDrawable(R.drawable.big_header_bg));
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    public static void sendEmailUsingGmail(Context c, String subject, String message, String recipient) {
        try {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message));
            //need this to prompts email client only
            email.setType("message/rfc822");
            email.setPackage("com.google.android.gm");
            c.startActivity(Intent.createChooser(email, "Send Feedback"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static boolean isShowRatePopup() {
        if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.RATE_POPUP_SHOWN_COUNT) >= 5) {
            return false;
        } else if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.RATE_POPUP_MOVE_TO_PLAY_STORE_COUNT) > BIG_Constants.SHOW_RATE_US_POPUP_COUNT) {
            return false;
        }
        return true;
    }

    static Dialog dialog;

    public static void showRatePopup(Context context) {
        if (dialog == null || !dialog.isShowing()) {
            try {
                dialog = new Dialog(context, android.R.style.Theme_Light);
                dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.big_dialog_rate);

                TextView tvMessage = dialog.findViewById(R.id.tvMessage);
                TextView tvRateUs = dialog.findViewById(R.id.tvRateUs);
                ImageView ivClose = dialog.findViewById(R.id.ivClose);
                ImageView ivArrow = dialog.findViewById(R.id.ivArrow);
                // Animation
                Animation animUpDown = AnimationUtils.loadAnimation(context, R.anim.big_updown_anim);
                animUpDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ivArrow.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                // start the animation
                ivArrow.startAnimation(animUpDown);

                RotationRatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                        tvMessage.setVisibility(rating < 4 ? View.VISIBLE : View.INVISIBLE);
                        tvRateUs.setVisibility(rating < 4 ? View.INVISIBLE : View.VISIBLE);
                        ivClose.setVisibility(rating < 4 ? View.VISIBLE : View.INVISIBLE);
                    }
                });

                tvRateUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ratingBar.getRating() < 4) {
                            if (ratingBar.getRating() <= 0) {
                                setToast(context, "Please select rating stars");
                                return;
                            }
                        } else {
                            dialog.dismiss();
                            BIG_SharePrefs.getInstance().putInt(BIG_SharePrefs.RATE_POPUP_MOVE_TO_PLAY_STORE_COUNT, (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.RATE_POPUP_MOVE_TO_PLAY_STORE_COUNT) + 1));
                            RateApp(context);
                        }
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog1) {
                        dialog = null;
                    }
                });
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String getBase64(Context c, int drawable, int h, int w) {
        Bitmap bitmap = getResizedBitmap(BitmapFactory.decodeResource(c.getResources(), drawable), h, w);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static String getVersionName(Context c) {
        try {
            PackageManager manager = c.getPackageManager();
            PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
            return ("v" + info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSizeFromBytes(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static final String DATE_TIME_FORMAT_STANDARDIZED_UTC = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_STANDARDIZED_UTC = "yyyy-MM-dd";

    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_FORMAT_STANDARDIZED_UTC).format(new Date());
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat(DATE_TIME_FORMAT_STANDARDIZED_UTC).format(new Date());
    }

    public static Date getDateObjectFromString(String datetime) throws ParseException {
        return new SimpleDateFormat(DATE_TIME_FORMAT_STANDARDIZED_UTC).parse(datetime);
    }

    public static long getTimeDifference(Date currentDate, Date previousDate) {
        try {
            long diff = Math.abs(previousDate.getTime() - currentDate.getTime());
            long hour = diff / (60 * 60 * 1000);
            return hour;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean shouldShowRatePopup() {
        try {
            if (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.RATE_POPUP_LAST_SHOW_DATE).length() == 0) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.RATE_POPUP_LAST_SHOW_DATE, BIG_Common_Utils.getCurrentDateTime());
            }
            long timeDiff = 0;
            try {
                timeDiff = BIG_Common_Utils.getTimeDifference(new Date(), BIG_Common_Utils.getDateObjectFromString(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.RATE_POPUP_LAST_SHOW_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.RATE_POPUP_LAST_SHOW_DATE, BIG_Common_Utils.getCurrentDateTime());
            }
            if (timeDiff >= BIG_Constants.ratePopupHourDifference && BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.RATE_POPUP_MOVE_TO_PLAY_STORE_COUNT) < BIG_Constants.SHOW_RATE_US_POPUP_COUNT) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void logFirebaseEvent(Context context, String title, String contentType, String eventName, String itemId) {
        try {
            if (!BuildConfig.DEBUG) {
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
                mFirebaseAnalytics.logEvent(eventName, bundle);
                //AppLogger.getInstance().e("EVENT: ", "LOGGED========" + contentType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public static boolean verifyInstallerId(Activity context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }

    public static int getRandomNumberBetweenRange(int min, int max) {
        if (max == 0) {
            return 0;
        }
        Random r = new Random();
        int i1 = r.nextInt(max - min) + min;// min inclusive & max exclusive
        return i1;
    }

    public static String getRandomAdUnitId(List<String> list) {
        return list.get(getRandomNumberBetweenRange(0, list.size()));
    }

    public static boolean isShowAppLovinAds() {
        try {
            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            if (responseMain.getIsAppLovinAdShow() != null && responseMain.getIsAppLovinAdShow().equals("1"))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
//    public static long convertTimeInMillis(String dateTimeFormat, String date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        try {
//            Date mDate = sdf.parse(date);
//            return mDate.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public static boolean isShowAppLovinNativeAds() {
        try {
            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            if (responseMain.getIsAppLovinAdShow() != null && responseMain.getIsAppLovinAdShow().equals("1") && responseMain.getLovinNativeID() != null && responseMain.getLovinNativeID().size() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static int getDaysDiff(String date1, String Date2) {
        int days = 0;
        try {
            long timeDiff = BIG_Common_Utils.convertTimeInMillis("yyyy-MM-dd HH:mm:ss", date1) - BIG_Common_Utils.convertTimeInMillis("yyyy-MM-dd HH:mm:ss", Date2);
            if (timeDiff > 0) {
                days = (int) (timeDiff / (1000 * 60 * 60 * 24));
                return days;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    public static boolean isLoadAppLovinInterstitialAds() {
        try {
            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            if ((responseMain.getIsAppLovinAdShow() != null) && responseMain.getIsAppLovinAdShow().equals("1") && (responseMain.getLovinInterstitialID() != null) && (responseMain.getLovinInterstitialID().size() > 0)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isLoadAppLovinRewardedAds() {
        try {
            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            if (responseMain.getIsAppLovinAdShow() != null && responseMain.getIsAppLovinAdShow().equals("1") && responseMain.getLovinRewardID() != null && responseMain.getLovinRewardID().size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isShowAppLovinAppOpenAds() {
        try {
            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            if (responseMain.getIsAppLovinAdShow() != null && responseMain.getIsAppLovinAdShow().equals("1") && responseMain.getLovinAppOpenID() != null && responseMain.getLovinAppOpenID().size() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isShowAppLovinBannerAds() {
        try {
            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            if (responseMain.getIsAppLovinAdShow() != null && responseMain.getIsAppLovinAdShow().equals("1") && responseMain.getLovinBannerID() != null && responseMain.getLovinBannerID().size() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void NotifyLogin(final Activity activity) {
        try {
            if (activity != null) {
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.big_dialog_login);
                dialog1.setCancelable(false);

                AppCompatButton btnLogin = dialog1.findViewById(R.id.btnLogin);
                btnLogin.setOnClickListener(v -> {
                    dialog1.dismiss();
                    activity.startActivity(new Intent(activity, Big_LoginUserActivity.class));
                });

                AppCompatButton btnNotNow = dialog1.findViewById(R.id.btnNotNow);
                btnNotNow.setOnClickListener(v -> {
                    dialog1.dismiss();
                });

                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NotifyDeleteAccount(final Activity activity) {
        try {
            if (activity != null) {
                BIG_AppLogger.getInstance().e("logout",""+activity);
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.big_dialog_logout);
                dialog1.setCancelable(false);

                TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
                tvTitle.setText("Delete Account");
                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
                tvMessage.setText("You will not be able to use Big Prize with same account details in future. Are you sure you want to permanently delete your Big Prize account?");
                RelativeLayout relPopup = dialog1.findViewById(R.id.relPopup);
                relPopup.setVisibility(View.GONE);
                ImageView ivIcon = dialog1.findViewById(R.id.ivIcon);
                ivIcon.setImageResource(R.drawable.big_ic_delete_forever);
                ivIcon.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

                AppCompatButton btnLogout = dialog1.findViewById(R.id.btnLogout);
                btnLogout.setOnClickListener(v -> {
                    dialog1.dismiss();
                   new Big_Delete_Account_Async(activity);
                });

                AppCompatButton btnNotNow = dialog1.findViewById(R.id.btnNotNow);
                btnNotNow.setText("No");
                btnNotNow.setOnClickListener(v -> {
                    dialog1.dismiss();
                });

                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NotifyLogout(final Activity activity, String imageLink) {
        try {
            if (activity != null) {
                BIG_AppLogger.getInstance().e("logout",""+activity);
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.big_dialog_logout);
                dialog1.setCancelable(false);

                ProgressBar probrBanner = dialog1.findViewById(R.id.probrBanner);
                LottieAnimationView ivLottieView = dialog1.findViewById(R.id.ivLottieView);
                ImageView imgBanner = dialog1.findViewById(R.id.imgBanner);

                if (!BIG_Common_Utils.isStringNullOrEmpty(imageLink)) {
                    if (imageLink.contains(".json")) {
                        probrBanner.setVisibility(View.GONE);
                        imgBanner.setVisibility(View.GONE);
                        ivLottieView.setVisibility(View.VISIBLE);
                        BIG_Common_Utils.setLottieAnimation(ivLottieView, imageLink);
                        ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                    } else {
                        probrBanner.setVisibility(View.VISIBLE);
                        imgBanner.setVisibility(View.VISIBLE);
                        ivLottieView.setVisibility(View.GONE);
                        Glide.with(activity)
                                .load(imageLink)
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                        probrBanner.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(imgBanner);
                    }
                } else {
                    imgBanner.setVisibility(View.GONE);
                    probrBanner.setVisibility(View.GONE);
                }

                AppCompatButton btnLogout = dialog1.findViewById(R.id.btnLogout);
                btnLogout.setOnClickListener(v -> {
                    dialog1.dismiss();
                    doLogout(activity);
                });

                AppCompatButton btnNotNow = dialog1.findViewById(R.id.btnNotNow);
                btnNotNow.setOnClickListener(v -> {
                    dialog1.dismiss();
                });

                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
          BIG_AppLogger.getInstance().e("logout",""+ BIG_SharePrefs.IS_LOGIN);
            e.printStackTrace();
        }
    }

    public static void doLogout(Activity activity) {
        try {
            String whatsAppAuth = BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.isShowWhatsAppAuth);
            String adId = BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.AdID);
            String FCMToken = BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.FCMregId);

            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            Big_Response_Model responseMain1 = new Big_Response_Model();


            responseMain1.setLoginSlider(responseMain.getLoginSlider());

            BIG_AppLogger.getInstance().e("logout==",""+responseMain.getLoginSlider());
            responseMain1.setLoginSliderWhatsApp(responseMain.getLoginSliderWhatsApp());
            responseMain1.setLovinAppOpenID(responseMain.getLovinAppOpenID());
            responseMain1.setLovinBannerID(responseMain.getLovinBannerID());
            responseMain1.setLovinInterstitialID(responseMain.getLovinInterstitialID());
            responseMain1.setLovinNativeID(responseMain.getLovinNativeID());
            responseMain1.setLovinAppOpenID(responseMain.getLovinAppOpenID());
            responseMain1.setIsAppLovinAdShow(responseMain.getIsAppLovinAdShow());

            BIG_SharePrefs.getInstance().clearSharePrefs();
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.HomeData, new Gson().toJson(responseMain1));
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.isShowWhatsAppAuth, whatsAppAuth);
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.AdID, adId);
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.FCMregId, FCMToken);
            BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_USER_CONSENT_ACCEPTED, true);
            BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_FIRST_LOGIN, false);
            BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_SKIPPED_LOGIN, true);

            BIG_AppLogger.getInstance().e("logout",""+ BIG_SharePrefs.HomeData);

            activity.startActivity(new Intent(activity, Big_LoginUserActivity.class));
            activity.finishAffinity();
        } catch (Exception e) {
            BIG_AppLogger.getInstance().e("logout",""+e);
            e.printStackTrace();
        }
    }

    public static void Notify(final Activity activity, String title, String message, boolean isFinish) {
        try {
            if (activity != null) {
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.big_dialog_notify);
                dialog1.setCancelable(false);

                Button btnOk = dialog1.findViewById(R.id.btnOk);
                TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
//                tvTitle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("Copied Text", message);
//                        clipboard.setPrimaryClip(clip);
//                        setToast(activity, "Copied");
//                    }
//                });
                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
                tvMessage.setText(message);
                btnOk.setOnClickListener(v -> {
                    dialog1.dismiss();
                    if (isFinish && !activity.isFinishing()) {
                        activity.finish();
                    }
                });
                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NotifyMessage(final Activity activity, String title, String message, boolean isFinish) {
        try {
            if (activity != null) {
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.big_dialog_note);
                dialog1.setCancelable(true);

                ImageView ivClose = dialog1.findViewById(R.id.ivClose);
                TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
                tvTitle.setText(title);

                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
                tvMessage.setText(message);

                ivClose.setOnClickListener(v -> {
                    dialog1.dismiss();
                    if (isFinish && !activity.isFinishing()) {
                        activity.finish();
                    }
                });
                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NotifySuccess(final Activity activity, String title, String message, boolean isFinish) {
        try {
            if (activity != null) {
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.big_dialog_notify_success);
                dialog1.setCancelable(false);

                Button btnOk = dialog1.findViewById(R.id.btnOk);
                TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
                tvMessage.setText(message);
                btnOk.setOnClickListener(v -> {
                    dialog1.dismiss();
                    if (isFinish && !activity.isFinishing()) {
                        activity.finish();
                    }
                });
                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UpdateApp(final Activity activity, String isForupdate,
                                 final String appurl, String msg) {
        try {
            if (activity != null) {
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.big_dialog_updateapp);

                Button btnUpdate = dialog1.findViewById(R.id.btnUpdate);
                Button btnCancel = dialog1.findViewById(R.id.btnCancel);
                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
                tvMessage.setText(msg);
                if (isForupdate.equals("1")) {
                    dialog1.setCancelable(false);
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.GONE);
                } else {
                    dialog1.setCancelable(true);
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                }
                btnUpdate.setOnClickListener(v -> {
                    if (!activity.isFinishing() && !isForupdate.equals("1")) {
                        dialog1.dismiss();
                    }
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appurl));
                        activity.startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(activity, "No application can handle this request." + " Please install a web browser", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });
                btnCancel.setOnClickListener(view -> {
                    if (!activity.isFinishing()) {
                        dialog1.dismiss();
                    }
                });
                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isStringNullOrEmpty(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    public static String formatString(double value) {
        try {
//            return (String.format("%,.2f", value));

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            return formatter.format(value).replace("₹ ", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(value);
    }

//    public static void loadTopBannerAd(Activity activity, LinearLayout layoutTopAds, Top_Ads model) {
//        try {
//            if (!POC_Common_Utils.isStringNullOrEmpty(model.getImage())) {
//                layoutTopAds.setVisibility(View.VISIBLE);
//                ProgressBar progressBar = layoutTopAds.findViewById(R.id.progressBar);
//                if (model.getImage().endsWith(".json")) {
//                    LottieAnimationView ivLottieTopBanner = layoutTopAds.findViewById(R.id.ivLottieTopBanner);
//                    ivLottieTopBanner.setVisibility(View.VISIBLE);
//                    POC_Common_Utils.setLottieAnimation(ivLottieTopBanner, model.getImage());
//                    ivLottieTopBanner.setRepeatCount(LottieDrawable.INFINITE);
//                    ivLottieTopBanner.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            POC_Common_Utils.Redirect(activity, model.getScreenNo(), model.getTitle(), model.getUrl(), model.getId(), model.getTaskId(), model.getImage());
//                        }
//                    });
//                    progressBar.setVisibility(View.GONE);
//                } else {
//                    ImageView imgTopBanner = layoutTopAds.findViewById(R.id.imgTopBanner);
//                    imgTopBanner.setVisibility(View.VISIBLE);
//                    imgTopBanner.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            POC_Common_Utils.Redirect(activity, model.getScreenNo(), model.getTitle(), model.getUrl(), model.getId(), model.getTaskId(), model.getImage());
//                        }
//                    });
//                    Glide.with(activity)
//                            .load(model.getImage())
//                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                            .listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    layoutTopAds.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
//                                    progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//                            })
//                            .into(imgTopBanner);
//                }
//            } else {
//                layoutTopAds.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            layoutTopAds.setVisibility(View.GONE);
//        }
//    }

    public static void openUrl(Context c, String url) {
        if (!BIG_Common_Utils.isStringNullOrEmpty(url)) {
            if (url.contains("/t.me/") || url.contains("telegram") || url.contains("facebook.com") || url.contains("instagram.com") || url.contains("youtube.com") || url.contains("play.google.com/store/apps/details") || url.contains("market.android.com/details")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    c.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    openUrlInChrome(c, url);
                }
            } else {
                openUrlInChrome(c, url);
            }
        }
    }

    private static void openUrlInChrome(Context c, String url) {
        //AppLogger.getInstance().e("URL openUrlInChrome :============", url);
        if (!BIG_Common_Utils.isStringNullOrEmpty(url)) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                intent.setPackage("com.android.chrome");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    intent.setPackage(null);
                    c.startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    setToast(c, "No application found to handle this url");
                }
            }
        }
    }

    public static void loadBannerAds(Activity c, LinearLayout layoutBannerAd, TextView
            lblAdSpace) {
        try {
            if (BIG_Common_Utils.isShowAppLovinBannerAds()) {
                lblAdSpace.setHeight(c.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height));
                MaxAdView adView = new MaxAdView(getRandomAdUnitId(new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class).getLovinBannerID()), c);
                adView.setListener(new MaxAdViewAdListener() {
                    @Override
                    public void onAdExpanded(MaxAd ad) {

                    }

                    @Override
                    public void onAdCollapsed(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        lblAdSpace.setVisibility(View.GONE);
                        layoutBannerAd.removeAllViews();
                        layoutBannerAd.addView(adView);
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        //AppLogger.getInstance().e("APPLOVIN BANNER onAdLoadFailed==", "===" + error.getMessage());
                        layoutBannerAd.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        //AppLogger.getInstance().e("APPLOVIN BANNER onAdDisplayFailed==", "===" + error.getMessage());
                        layoutBannerAd.setVisibility(View.GONE);
                    }
                });
                adView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, c.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height)));
                adView.loadAd();
            } else {
                layoutBannerAd.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            layoutBannerAd.setVisibility(View.GONE);
        }
    }
    public static void loadTopBannerAd(Activity activity, LinearLayout layoutTopAds, Big_Top_Ads model) {
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(model.getImage())) {
                layoutTopAds.setVisibility(View.VISIBLE);
                ProgressBar progressBar = layoutTopAds.findViewById(R.id.progressBar);
                if (model.getImage().endsWith(".json")) {
                    LottieAnimationView ivLottieTopBanner = layoutTopAds.findViewById(R.id.ivLottieTopBanner);
                    ivLottieTopBanner.setVisibility(android.view.View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(ivLottieTopBanner, model.getImage());
                    ivLottieTopBanner.setRepeatCount(LottieDrawable.INFINITE);
                    ivLottieTopBanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BIG_Common_Utils.Redirect(activity, model.getScreenNo(), model.getTitle(), model.getUrl(), model.getId(), model.getTaskId(), model.getImage());
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                } else {
                    ImageView imgTopBanner = layoutTopAds.findViewById(R.id.imgTopBanner);
                    imgTopBanner.setVisibility(View.VISIBLE);
                    imgTopBanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BIG_Common_Utils.Redirect(activity, model.getScreenNo(), model.getTitle(), model.getUrl(), model.getId(), model.getTaskId(), model.getImage());
                        }
                    });
                    Glide.with(activity)
                            .load(model.getImage())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    layoutTopAds.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imgTopBanner);
                }
            } else {
                layoutTopAds.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            layoutTopAds.setVisibility(View.GONE);
        }
    }

    public static boolean appInstalledOrNot(Activity activity, String uri) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(uri, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showImageLotteGIF(Context context, String jsonURL, String imgURL, LottieAnimationView ltView, ImageView ivBanner, ImageView ivGIF, ProgressBar progressBar, CardView cardView) {
        if (jsonURL != null && jsonURL.length() > 0) {
            ivGIF.setVisibility(View.GONE);
            ivBanner.setVisibility(View.GONE);
            if (cardView != null) {
                cardView.setVisibility(View.VISIBLE);
            }
            BIG_Common_Utils.setLottieAnimation(ltView, jsonURL);
            ltView.setRepeatCount(LottieDrawable.INFINITE);
            ltView.playAnimation();
            ltView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }

                    //AppLogger.getInstance().e("Lottie1--)", "Start");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                    //AppLogger.getInstance().e("Lottie1--)", "Repeat");
                }
            });
        } else {
            if (imgURL != null && imgURL.length() > 0) {
                if (imgURL.contains("gif")) {
                    ivGIF.setVisibility(View.VISIBLE);
                    ltView.setVisibility(View.GONE);
                    if (cardView != null) {
                        cardView.setVisibility(View.GONE);
                    }
                    ivBanner.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(imgURL)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    if (progressBar != null) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    return false;
                                }
                            })
                            .into(ivGIF);
                } else {

                    ivGIF.setVisibility(View.GONE);
                    ltView.setVisibility(View.GONE);
                    if (cardView != null) {
                        cardView.setVisibility(View.GONE);
                    }
                    ivBanner.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(imgURL)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    if (progressBar != null) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    return false;
                                }
                            })
                            .into(ivBanner);
                }

            }
        }

    }




    public static void setEnableDisable(final Activity activity, final View v) {
        v.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                });
            }
        }, 2000);
    }

    public static String getPathFromURI(final Context context, final Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if (id != null && (id.startsWith("msf:") || id.startsWith("raw:"))) {
                    final File file = new File(context.getCacheDir(), "temp." + Objects.requireNonNull(context.getContentResolver().getType(uri)).split("/")[1]);
                    try (final InputStream inputStream = context.getContentResolver().openInputStream(uri); OutputStream output = new FileOutputStream(file)) {
                        final byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }

                        output.flush();
                        return file.getAbsolutePath();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                } else {
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static long convertTimeInMillis(String dateTimeFormat, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date mDate = sdf.parse(date);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static long mLastClickTime = 0;

    public static void Redirect(Activity context, String screenNo, String title, String url, String id, String taskId, String imageLink) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        //AppLogger.getInstance().e("ClickEvent--)", "" + url + "--)" + screenNo);
        if (screenNo != null && screenNo.length() > 0) {
            switch (screenNo) {
                case "1":
                    if (context instanceof Big_MainActivity) {
                        ((Big_MainActivity) context).performHomeClick();
                    } else {
                        Intent intent = new Intent(context, Big_MainActivity.class);
                        intent.putExtra("type", "home");
                        context.startActivity(intent);
                    }
                    break;
                case "2":
                    // Open link externally
                    openUrl(context, url);
                    break;
                case "3":
                    if (title != null || url != null) {
                        Intent in = new Intent(context, Big_WebActivity.class);
                        in.putExtra("Title", title);
                        in.putExtra("URL", url);
                        context.startActivity(in);
                    }
                    break;
                case "4":
                    Intent inSpin = new Intent(context, Big_LuckySpinWheelActivity.class);
                    context.startActivity(inSpin);
                    break;
                case "5":
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        Intent intent = new Intent(context, Big_My_Wallet_Activity.class);
                        context.startActivity(intent);
                    } else {
                        NotifyLogin(context);
                    }
                    break;
                case "6":
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        Intent intent = new Intent(context, Big_PointHistoryActivity.class);
                        intent.putExtra("type", BIG_Constants.HistoryType.WITHDRAW_HISTORY);
                        intent.putExtra("title", "Withdrawal History");
                        context.startActivity(intent);
                    } else {
                        NotifyLogin(context);
                    }
                    break;
                case "7":
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        Intent inHistory = new Intent(context, Big_PointHistoryActivity.class);
                        context.startActivity(inHistory);
                    } else {
                        NotifyLogin(context);
                    }
                    break;
                case "8":
//                    if (context instanceof MainActivity) {
//                        ((MainActivity) context).performInviteClick();
//                    } else {
//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.putExtra("type", "invite");
//                        context.startActivity(intent);
//                    }
                    context.startActivity(new Intent(context, Big_ReferUsersActivity.class));
                    break;
                case "9":
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        Intent inWallet = new Intent(context, Big_My_Wallet_Activity.class);
                        context.startActivity(inWallet);
                    } else {
                        NotifyLogin(context);
                    }
                    break;
                case "10":
                    if (context instanceof Big_MainActivity) {
                        ((Big_MainActivity) context).performInviteClick();
                    } else {
                        Intent intent = new Intent(context, Big_MainActivity.class);
                        intent.putExtra("type", "me");
                        context.startActivity(intent);
                    }
                    break;
                case "11":
//                    if (context instanceof MainActivity) {
//                        ((MainActivity) context).performTaskClick();
//                    } else {
//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.putExtra("type", "task");
//                        context.startActivity(intent);
//                    }
                    context.startActivity(new Intent(context, Big_TasksCategoryTypeActivity.class));
                    break;
                case "12":
                    Intent inTaskDetail = new Intent(context, Big_TaskDetailsInfoActivity.class);
                    if (taskId != null) {
                        inTaskDetail.putExtra("taskId", taskId);
                    } else {
                        inTaskDetail.putExtra("taskId", id);
                    }
                    context.startActivity(inTaskDetail);
                    break;

                case "14":
//                    if (context instanceof MainActivity) {
//                        ((MainActivity) context).performRewardClick();
//                    } else {
//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.putExtra("type", "reward");
//                        context.startActivity(intent);
//                    }
                    context.startActivity(new Intent(context, Big_EarningOptionsActivity.class));
                    break;
                case "15":
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if (url != null) {
                            loadOffer(context, url);
                        }
                    } else {
                        NotifyLogin(context);
                    }
                    break;
                case "16":
                    break;
                case "17":
                    Intent intent = new Intent(context, Big_TasksCategoryTypeActivity.class);
                    intent.putExtra("taskTypeId", id);
                    context.startActivity(intent);
                    break;
                case "18":
                    NotifyLogout(context, imageLink);
                    break;
                case "19":
                    context.startActivity(new Intent(context, Big_ActivityDailyLogin.class));
                    break;
                case "20":
//                    Intent inTopProductTask = new Intent(context, TopProductListActivity.class);
//                    context.startActivity(inTopProductTask);
                    break;
                case "21":
                    context.startActivity(new Intent(context, Big_WatchVideoActivity.class));
                    break;
                case "22":
                    if (url != null && url.length() > 0) {
                        Intent i = new Intent(context, Big_WebActivity.class);
                        i.putExtra("URL", url);
                        i.putExtra("Title", title);
                        context.startActivity(i);
                    }
                    break;
                case "23":
                    context.startActivity(new Intent(context, Big_FeedbackSubmitActivity.class).putExtra("title", title));
                    break;
                case "24":
                    showRatePopup(context);
                    break;
                case "25":
//                    context.startActivity(new Intent(context, MoreAppsActivity.class));
                    break;
                case "26":
                    context.startActivity(new Intent(context, Big_ScratchCardGameActivity.class));
                    break;
                case "27":
                   context.startActivity(new Intent(context, Big_GiveAwaySocialActivity.class));
                    break;
                case "28":
                   context.startActivity(new Intent(context, Big_LuckyNumberDrawActivity.class));
                    break;
                case "29":
                    context.startActivity(new Intent(context, Big_PlayPuzzleActivity.class));
                    break;
                case "30":
                   context.startActivity(new Intent(context, Big_QuizGameActivity.class));
                    break;
                case "31":
                   openAdjoeOfferWall(context);
                    break;
                case "32":
                   context.startActivity(new Intent(context, Big_ActivityDailyReward.class));
                    break;
                case "33":
                   context.startActivity(new Intent(context, Big_Dice_Roll_Activity.class));
                    break;
                case "34":
                    context.startActivity(new Intent(context, Big_CountUpActivity.class));
                    break;
                case "35":
                    context.startActivity(new Intent(context, Big_CardMatchActivity.class));
                    break;
                case "36":
                   context.startActivity(new Intent(context, Big_ColorTapActivity.class));
                    break;
                case "37":
                    context.startActivity(new Intent(context, Big_Dice_Roll_Activity.class));
                    break;
//                case "38":
//                    Intent intentTextTyping = new Intent(context, Big_MilestonesActivity.class);
//                    context.startActivity(intentTextTyping);
//                    break;
                case "39":
                    //Intent adjoeLeaderboard = new Intent(context, AdjoeLeaderboardActivity.class);
                   // context.startActivity(adjoeLeaderboard);
                    break;
                case "40":
                    Intent adjoeLeaderboardHistory = new Intent(context, Big_AdjoeLeaderboardHistoryActivity.class);
                   context.startActivity(adjoeLeaderboardHistory);
                    break;
                case "41":
                    context.startActivity(new Intent(context, Big_AlphabetgameActivity.class));
                    break;
                case "42":
                    context.startActivity(new Intent(context, Big_TypeTextGameActivity.class));
                    break;
              case "43":
                  try {
//                      String Big_Response_Model;
//                      Log.d("pubscale", "Redirect: "+Big_Response_Model responseMain1.getIsShowPubScale());
                      Big_Response_Model responseMain1 = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
                      if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain1.getIsShowPubScale()) && responseMain1.getIsShowPubScale().matches("1")) {
                          Log.d("pubscale", "Redirect: "+ responseMain1.getIsShowPubScale());
                          OfferWall.launch(context, new OfferWallListener() {
                              @Override
                              public void onOfferWallShowed() {
                                  BIG_Activity_Manager.isShowAppOpenAd = false;
                                  logFirebaseEvent(context, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Pubscale", "Offerwall Opened");
                                  BIG_AppLogger.getInstance().e("INIT", "PUBSCALE onOfferWallShowed==========");
                              }

                              @Override
                              public void onOfferWallClosed() {
                                  BIG_AppLogger.getInstance().e("INIT", "PUBSCALE onOfferWallClosed==========");
                              }

                              @Override
                              public void onRewardClaimed(Reward reward) {
                                  BIG_AppLogger.getInstance().e("INIT", "PUBSCALE onRewardClaimed==========");
                              }

                              @Override
                              public void onFailed(String s) {
                                  BIG_AppLogger.getInstance().e("INIT", "PUBSCALE onFailed==========");
                              }
                          });
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                  break;
                case "44":
                    context.startActivity(new Intent(context, Big_SortWordsActivity.class));
                    break;
                case "45":
                    context.startActivity(new Intent(context, Big_MinesFinderActivity.class));
                    break;
                case "46":
                    Big_Response_Model responseMain1 = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain1.getIsShowAdjump()) && responseMain1.getIsShowAdjump().matches("1")) {
                        Big_App_Controller app = (Big_App_Controller) context.getApplication();
                        if (app.getAdjump().isAvailable()) {
                            BIG_Activity_Manager.isShowAppOpenAd = false;
                            logFirebaseEvent(context, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Adjump", "Offerwall Opened");
                            app.getAdjump().open(context);
                        } else {
                            setToast(context, "Adjump is not initialized, please try again later");
                        }
                    }
                    break;
                case "47":
                    try {
                        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_SHOW_SCAN_PAY_INFO) && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            context.requestPermissions(new String[]{Manifest.permission.CAMERA}, 111);
                        } else {
                            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_SHOW_SCAN_PAY_INFO)) {
                                context.startActivity(new Intent(context, Big_ScanActivity.class));
                            } else {
                                context.startActivity(new Intent(context, Big_ScanAndPayActivity.class));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "48":
                    try {
                        Big_Response_Model responseMain2 = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain2.getIsShowAppPrize()) && responseMain2.getIsShowAppPrize().matches("1")) {
                            Big_App_Controller app = (Big_App_Controller) context.getApplication();
                            boolean available = AppsPrize.launchActivity(context);
                            if (available) {
                                logFirebaseEvent(context, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Appsamurai_AppsPrize", "Offerwall Opened");
                            }else{
                                BIG_AppLogger.getInstance().e("[AppsPrize]", "AppsPrize not initialized");
                                setToast(context, "AppsPrize is not initialized, please try again later");
                                app.initAppPrizeSDK();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "49":
                    try {
                        Uri uri = Uri.parse(url);
                        new CustomTabsIntent.Builder()
                                .build()
                                .launchUrl(context, uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "50":
                    context.startActivity(new Intent(context, Big_Brain_trainerActivity.class));
                    break;

                case "59":
                    try {
                        Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowOfferToro()) && responseMain.getIsShowOfferToro().matches("1")) {
                            Big_App_Controller app = (Big_App_Controller) context.getApplication();
                            if (com.torox.sdk.OfferWall.getInstance().isInitialized()) {
                                com.torox.sdk.OfferWall.getInstance().showOfferWall(context);
                            } else {
                                setToast(context, "OfferToro is not initialized, please try again later");
                                app.initOfferToro();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }

    public static void openAdjoeOfferWall(Activity context) {
        try {
            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
//                if (!POC_Common_Utils.hasUsageAccessPermission(context)) {
//                    POC_Common_Utils.showUsageAccessPermissionDialog(context);
//                } else {

                    Intent adjoeOfferwallIntent = Adjoe.getOfferwallIntent(context);
                    context.startActivity(adjoeOfferwallIntent);
                    if (Adjoe.canShowOfferwall(context)) {
                        BIG_Activity_Manager.isShowAppOpenAd = false;
                        Adjoe.sendUserEvent(context, Adjoe.EVENT_TEASER_SHOWN, null, null);
                        BIG_Common_Utils.logFirebaseEvent(context, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Adjoe_Playtime", "Offerwall Opened");
 //                   }
                }
            } else {
                NotifyLogin(context);
            }
        } catch (AdjoeNotInitializedException notInitializedException) {
            // you have not initialized the adjoe SDK
            showProgressLoader(context);
            initializeAdJoe(context, true);
            //AppLogger.getInstance().e("COMMON UTILS===", "ADJOE SDK NOT INITIALIZED");
        } catch (AdjoeException exception) {
            if (exception.getMessage() != null) {
                if (exception.getMessage().equals("no content available")) {
                    setToast(context, "No games are available, try after some time");
                } else if (exception.getMessage().equals("cannot display the offerwall without a network connection")) {
                    setToast(context, "Games are not ready yet");
                } else if (exception.getMessage().equals("not available for this user")) {
                    setToast(context, "Your access is restricted due to fraud behaviour");
                } else {
                    setToast(context, "Games are not ready yet");
                }
            }
        } catch (Exception exception) {

        }
    }




//    public static boolean hasUsageAccessPermission(Activity activity) {
//        AppOpsManager appOps = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
//        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), activity.getPackageName());
//        boolean granted;
//        if (mode == AppOpsManager.MODE_DEFAULT) {
//            granted = (activity.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
//        } else {
//            granted = (mode == AppOpsManager.MODE_ALLOWED);
//        }
//        return granted;
//    }



//    public static void showUsageAccessPermissionDialog(Context context) {
//
//        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
//        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        dialog.setContentView(R.layout.big_permission_dialog);
//
//        Button btnContinue = dialog.findViewById(R.id.btnContinue);
//
//        btnContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    dialog.dismiss();
//                    BIG_Activity_Manager.isShowAppOpenAd = false;
//                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                    intent.setData(Uri.parse("package:" + context.getPackageName()));
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    showOpenSettingDialog(context);
////                    setToast(context,"No able to open settings screen");
//                }
//            }
//        });
//
//        dialog.show();
//    }

    public static void showOpenSettingDialog(Context context) {

        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.setContentView(R.layout.big_opensetting_dialog);

        Button btnContinue = dialog.findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private static String finalUrl = "";
    private static Activity activityLoad;
    private static WebView webLoader;
    private static Handler handler;
    private static Dialog dialogLoaderOffer;

    public static void loadOffer(Activity activity, String str) {
        activityLoad = activity;
        dialogLoaderOffer = new Dialog(activity, android.R.style.Theme_Light);
        dialogLoaderOffer.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialogLoaderOffer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoaderOffer.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialogLoaderOffer.setCancelable(true);
        dialogLoaderOffer.setCanceledOnTouchOutside(true);
        dialogLoaderOffer.setContentView(R.layout.big_dialog_loader);

        webLoader = dialogLoaderOffer.findViewById(R.id.webloader);

        if (!activity.isFinishing() && !dialogLoaderOffer.isShowing()) {
            dialogLoaderOffer.show();
        }

        webLoader.getSettings().setJavaScriptEnabled(true);
        webLoader.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webView, String str) {
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                webView.loadUrl(str);
                return true;
            }

            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                //AppLogger.getInstance().e("finalUrl11--)", "" + finalUrl);
                if (str.startsWith("market://") || str.startsWith("intent://") || str.startsWith("http://") || str.startsWith("https://")) {
                    finalUrl = str;
                }
                if (str.startsWith("market://") || str.startsWith("intent://")) {
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    openWebPage();
                }
            }
        });
        webLoader.loadUrl(str);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        startTimer();
    }

    public static void openWebPage() {
        try {
            if (dialogLoaderOffer != null && !activityLoad.isFinishing()) {
                dialogLoaderOffer.dismiss();
            }

            if (finalUrl != null) {
                if (finalUrl.startsWith("intent:")) {
                    try {
                        Intent intent = Intent.parseUri(finalUrl, Intent.URI_INTENT_SCHEME);
                        if (intent.resolveActivity(activityLoad.getPackageManager()) != null) {
                            activityLoad.startActivity(intent);
                            return;
                        }
                        //try to find fallback url
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null) {
                            webLoader.loadUrl(fallbackUrl);
                            return;
                        }
                        //invite to install
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(
                                Uri.parse("market://details?id=" + intent.getPackage()));
                        if (marketIntent.resolveActivity(activityLoad.getPackageManager()) != null) {
                            activityLoad.startActivity(marketIntent);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (finalUrl.startsWith("market://")) {
                    try {
                        //invite to install
                        String packageName = finalUrl.substring("market://details?id=".length());
                        if (packageName.contains("&")) {
                            packageName = packageName.substring(0, packageName.indexOf("&"));
                        }
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=" + packageName));
                        if (marketIntent.resolveActivity(activityLoad.getPackageManager()) != null) {
                            activityLoad.startActivity(marketIntent);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                BIG_Activity_Manager.isShowAppOpenAd = false;
                BIG_Common_Utils.openUrl(activityLoad, finalUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        finalUrl = "";
        activityLoad = null;
        webLoader = null;
        handler = null;
        dialogLoaderOffer = null;
    }

    private static void startTimer() {
        handler = new Handler();
        handler.postDelayed(BIG_Common_Utils::openWebPage, 8000);
    }

    public static String modifyDateLayout(String inputDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inputDate);
            String time = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(date);
            time.replace("AM", "am");
            time.replace("PM", "pm");
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void GetCoinAnimation(Context context, final RelativeLayout frameLayout, View view) {
        int i;
        float f;
        float f2;
        float f3;
        Random random;
        int i2;
        RelativeLayout frameLayout2 = frameLayout;
        Random random2 = new Random();
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            i = 12;
            if (i4 >= 12) {
                break;
            }
            arrayList.add(Integer.valueOf((i4 * 50) + 500));
            i4++;
        }
        int round = Math.round(Resources.getSystem().getDisplayMetrics().density * 20.0f);
        ViewParent parent = view.getParent();
        float f4 = 0.0f;
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            f4 = viewGroup.getX();
            f = viewGroup.getY();
        } else {
            f = 0.0f;
        }
        float f5 = 0.5f;
        Point point = new Point((int) (((view.getWidth() - round) * 0.5f) + view.getX() + f4), (int) (((view.getHeight() - round) * 0.5f) + view.getY() + f));
        int i5 = 0;
        while (i5 < i) {
            final View view2 = new View(context);
            frameLayout2.addView(view2, new ViewGroup.LayoutParams(round, round));
            view2.setBackgroundResource(R.drawable.big_ic_coin);
            if (i5 == 10) {
                view2.setTag(5);
            } else {
                view2.setTag(Integer.valueOf(i3));
            }
            float width = (frameLayout.getWidth() * f5) + frameLayout.getX();
            float height = (frameLayout.getHeight() * 0.4f) + frameLayout.getY();
            double radians = Math.toRadians((i5 * 15) + 180);
            Path path = new Path();
            float f6 = round * 0.5f;
            int i6 = round;
            double nextInt = ((random2.nextInt(10) * 0.01f) + 0.2f) * Math.min(frameLayout.getWidth(), frameLayout.getHeight());
            Random random3 = random2;
            float cos = (((float) (Math.cos(radians) * nextInt)) + width) - f6;
            float sin = (height - ((float) (Math.sin(radians) * nextInt))) - f6;
            path.moveTo(width, height);
            path.lineTo(cos, sin);
            if (cos <= width) {
                f2 = 0.5f;
                f3 = cos * 0.5f;
            } else {
                f2 = 0.5f;
                f3 = cos * 1.5f;
            }
            path.quadTo(f3, sin * 1.5f, point.x, point.y);
            i5++;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, View.X, View.Y, path);
            ofFloat.setInterpolator(new AccelerateInterpolator());
            if (arrayList.size() > 0) {
                random = random3;
                i2 = random.nextInt(arrayList.size());
            } else {
                random = random3;
                i2 = 0;
            }
            ofFloat.setDuration(((Integer) arrayList.get(i2)).intValue());
            arrayList.remove(i2);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.lenoketo.videomaker.statusstory.pro16.Utils.utils.1
                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    frameLayout.removeView(view2);
                }
            });
            animatorSet.playTogether(ofFloat);
            random2 = random;
            frameLayout2 = frameLayout;
            round = i6;
            i = 12;
            f5 = f2;
            i3 = 0;
        }
        animatorSet.start();
    }

    public static String updateTimeRemaining(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 3) {
                return String.format(Locale.getDefault(), "%02d days left", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "You can spin now!!";
        }
    }

    public static String updateTimeRemainingImagePuzzle(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 3) {
                return String.format(Locale.getDefault(), "%02d days", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "You can solve puzzle now!!";
        }
    }

    public static String updateTimeRemainingScratch(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 3) {
                return String.format(Locale.getDefault(), "%02d days left", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "You can scratch now!!";
        }
    }

    public static String updateTimeRemainingWatchVideo(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 3) {
                return String.format(Locale.getDefault(), "%02d days left", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "Watch Now";
        }
    }

    public static String updateTimeRemainingLuckyNumber(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 3) {
                return String.format(Locale.getDefault(), "%02d days", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "";
        }
    }


    public static String calculateRemainingTime(long timeDiff) {
        if (timeDiff > 0) {
            String text = "";
            try {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int days = (int) (timeDiff / (1000 * 60 * 60 * 24));

                if (days > 0) {
                    if (days == 1) {
                        text = days + " day";
                    } else {
                        text = days + " days";
                    }
                }
                if (hours > 0) {
                    if (text.length() > 0)
                        text = text + " ";
                    if (hours == 1) {
                        text = text + hours + " hour";
                    } else {
                        text = text + hours + " hours";
                    }
                }
                if (minutes > 0) {
                    if (text.length() > 0)
                        text = text + " ";
                    if (minutes == 1) {
                        text = text + minutes + " minute";
                    } else {
                        text = text + minutes + " minutes";
                    }
                }
                if (seconds > 0) {
                    if (text.length() > 0)
                        text = text + " ";
                    text = text + seconds + " seconds";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (text.length() == 0)
                return "You can spin now!!";
            else
                return "Spin will be unlocked after " + text;
        } else {
            return "You can spin now!!";
        }
    }

    public static void startTextCountAnimation(TextView tvPoints, String point) {
        ValueAnimator animator = ValueAnimator.ofInt(0, Integer.parseInt(point));
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                tvPoints.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

    public static int timeDiff(String date1, String Date2) {
        long diff = convertTimeInMillis("yyyy-MM-dd HH:mm:ss", date1) - BIG_Common_Utils.convertTimeInMillis("yyyy-MM-dd HH:mm:ss", Date2);
        double seconds = Math.abs(diff) / 1000;
        int minutes = (int) (seconds / 60);
        return minutes;
    }

    public static void startRoundAnimation(Context context, ImageView ivIcon) {
        try {
            Animation rotation = AnimationUtils.loadAnimation(context, R.anim.big_rotate);
            rotation.setFillAfter(true);
            ivIcon.startAnimation(rotation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Pattern VALID_MOBILE_REGEX =
            Pattern.compile("^[6-9]\\d{9}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidMobile(String mobile) {
        Matcher matcher = VALID_MOBILE_REGEX.matcher(mobile);
        return matcher.matches();
    }

    public static void setLottieAnimation(LottieAnimationView animation_view, String celebrationLottieUrl) {
        try {
            animation_view.setFailureListener(new LottieListener<Throwable>() {
                @Override
                public void onResult(Throwable result) {

                }
            });
            animation_view.setAnimationFromUrl(celebrationLottieUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initializeAdJoe(Activity context, boolean isOpenAdJoe) {
        try {
            Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getAdjoeKeyHash())) {
                String userId = !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? "GU_" + BIG_Common_Utils.getRandomNumberBetweenRange(1, 1000000) : BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.userId);
                Adjoe.Options options = new Adjoe.Options().setUserId(userId);
                Adjoe.init(context, responseMain.getAdjoeKeyHash(), options, new AdjoeInitialisationListener() {
                    @Override
                    public void onInitialisationFinished() {
                        dismissProgressLoader();
                        // the adjoe SDK was initialized successfully
                        //AppLogger.getInstance().e("MainActivity2->OnResume===", "the adjoe SDK was initialized successfully");
                        if (isOpenAdJoe) {
                            try {
                                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                                    Intent adjoeOfferwallIntent = Adjoe.getOfferwallIntent(context);
                                    context.startActivity(adjoeOfferwallIntent);
                                    if (Adjoe.canShowOfferwall(context)) {
                                        Adjoe.sendUserEvent(context, Adjoe.EVENT_TEASER_SHOWN, null, null);
                                        BIG_Common_Utils.logFirebaseEvent(context, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Adjoe_Playtime", "Offerwall Opened");
                                    }
                                } else {
                                    NotifyLogin(context);
                                }
                            } catch (AdjoeNotInitializedException notInitializedException) {
                                // you have not initialized the adjoe SDK
                                setToast(context, "Games are not ready yet");
                                //AppLogger.getInstance().e("COMMON UTILS===initializeAdJoe", "ADJOE SDK NOT INITIALIZED");
                            } catch (AdjoeException exception) {
                                // the offerwall cannot be displayed for some other reason
                                if (exception.getMessage() != null) {
                                    if (exception.getMessage().equals("no content available")) {
                                        setToast(context, "No games are available, try after some time");
                                    } else if (exception.getMessage().equals("cannot display the offerwall without a network connection")) {
                                        setToast(context, "Games are not ready yet");
                                    } else if (exception.getMessage().equals("not available for this user")) {
                                        setToast(context, "Your access is restricted due to fraud behaviour");
                                    } else {
                                        setToast(context, "Games are not ready yet");
                                    }
                                }
                                //AppLogger.getInstance().e("COMMON UTILS===initializeAdJoe", "ADJOE SDK EXCEPTION");
                            } catch (Exception exception) {
                                // other exceptions
                                //AppLogger.getInstance().e("COMMON UTILS===initializeAdJoe", "EXCEPTION");
                            }
                        }
                    }

                    @Override
                    public void onInitialisationError(Exception exception) {
                        dismissProgressLoader();
                        // an error occurred while initializing the adjoe SDK.
                        // note that exception might be null
                        //AppLogger.getInstance().e("MainActivity2->OnResume===", "the adjoe SDK initialization Error");
                    }
                });
            } else {
                dismissProgressLoader();
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public static void InitializeApplovinSDK() {
        try {
            AppLovinSdk.getInstance(Big_App_Controller.getContext()).setMediationProvider("max");
            AppLovinSdk.initializeSdk(Big_App_Controller.getContext(), new AppLovinSdk.SdkInitializationListener() {
                @Override
                public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                    // AppLovin SDK is initialized, start loading ads
                    //AppLogger.getInstance().e("AppLovinSdk initialize", "DONE=========");
                    BIG_Ads_Utils.loadAppOpenAdd(Big_App_Controller.getContext());
                    BIG_Ads_Utils.loadAdMobInterstitialAds(false);
                    BIG_Ads_Utils.loadRewardedAd(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertPointsInINR(String points, String pointValue) {
        try {
            double calRupees = (Double.parseDouble(points) * 1) / Double.parseDouble(pointValue);
            String value = String.valueOf(calRupees);
            if (value.endsWith(".00")) {
                value = value.replace(".00", "");
            }
            if (value.endsWith(".0")) {
                value = value.replace(".0", "");
            }
            return "₹ " + value;
        } catch (Exception e) {
        }
        return "";
    }


    public static int timeDiffSeconds(String date1, String Date2) {
        long diff = BIG_Common_Utils.convertTimeInMillis("yyyy-MM-dd HH:mm:ss", date1) - BIG_Common_Utils.convertTimeInMillis("yyyy-MM-dd HH:mm:ss", Date2);
        double seconds = Math.abs(diff) / 1000;
        return (int) seconds;
    }

    public static Bitmap setViewToBitmapImage(Context c, View view, int defaultColor) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(defaultColor);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    public static String saveImageInCacheDirBitmap(Context c, Bitmap bitmap, String imageName) {
        File cachePath = new File(c.getCacheDir(), "shared_images");
        try {
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/" + (imageName.length() > 0 ? imageName :"temp.jpg")); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cachePath.getAbsolutePath() + "/" + (imageName.length() > 0 ? imageName : "temp.jpg");
    }
    public static void mShare(Activity activity, String filepath,String message) {
        try {
            Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(filepath));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, message);
            activity.startActivity(Intent.createChooser(intent, "Share using"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
