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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_ReferSuggestionAdapter;
import com.app.bigprize.Async.Big_Download_Image_Share_Async;
import com.app.bigprize.Async.Big_Get_Invite_Async;
import com.app.bigprize.Async.Models.Big_Invite_Response_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.gson.Gson;
import com.skydoves.balloon.Balloon;

import java.io.File;

public class Big_ReferUsersActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STORAGE = 1000;

    private boolean isResumeCalled = false, isParentImageLoaded = false, isTopImageLoaded = false;
    private LinearLayout layoutLogin, layoutInvite, layoutPoints1, more_copy, telegram_copy, sms_copy, copy_wp, layoutPoints;
    private AppCompatButton btnLogin;
    private TextView tvRules, tvInviteNo, tvInviteIncome, tvInviteCode, tvInvite, tvLoginText, lblInviteNo, lblIncome, tvPoints;
    private Big_Invite_Response_Model objInvite;
    private LinearLayout layoutBtnInvite;
    private ImageView ivCopy, ivHistory, ivMenu;
    private LottieAnimationView  parentLottie, dataLottie, topLottie;
    private View viewSeparator;
    RecyclerView rvRules;
    private long lastClickTime = 0;
    private  LottieAnimationView inviteBtnLottie;
    private Big_Response_Model responseMain;
    private Balloon balloon;
    private String shareMessage, shareType = "0";//0=general,1=telegram,2=whatsapp
    private LinearLayout howItWorks,needHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ReferUsersActivity.this);
        setContentView(R.layout.big_activity_refer_users);

        howItWorks = findViewById(R.id.howItWorks);
        needHelp = findViewById(R.id.needHelp);
        howItWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });
        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Big_ReferUsersActivity.this, Big_FeedbackSubmitActivity.class);
                intent.putExtra("title", "Give Feedback");
                startActivity(intent);

            }
        });


        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);



//        viewSeparator = findViewById(R.id.viewSeparator);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints1 = findViewById(R.id.layoutPoints1);
        ivMenu = findViewById(R.id.ivMenu);
        ivHistory = findViewById(R.id.ivHistory);
        tvPoints = findViewById(R.id.tvPoints);

        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        new Big_Get_Invite_Async(Big_ReferUsersActivity.this);


        ivMenu = findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {

                    startActivity(new Intent(Big_ReferUsersActivity.this, Big_ReferPointHistoryActivity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ReferUsersActivity.this);
                }
            }
        });
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ReferUsersActivity.this, Big_ReferPointHistoryActivity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ReferUsersActivity.this);
                }
            }
        });
        layoutPoints1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ReferUsersActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ReferUsersActivity.this);
                }
            }
        });
        tvLoginText = findViewById(R.id.tvLoginText);
        tvInviteNo = findViewById(R.id.tvInviteNo);
        tvInviteIncome = findViewById(R.id.tvInviteIncome);
        tvInviteCode = findViewById(R.id.tvInviteCode);
        ivCopy = findViewById(R.id.ivCopy);
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) Big_ReferUsersActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", tvInviteCode.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                BIG_Common_Utils.setToast(Big_ReferUsersActivity.this, "Copied!");
            }
        });
        tvInviteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) new Big_ReferUsersActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", tvInviteCode.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                BIG_Common_Utils.setToast(new Big_ReferUsersActivity(), "Copied!");
            }
        });
//        tvInvite = findViewById(R.id.tvInvite);
        lblInviteNo = findViewById(R.id.lblInviteNo);
        lblIncome = findViewById(R.id.lblIncome);
        layoutBtnInvite = findViewById(R.id.layoutBtnInvite);

        layoutBtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                callInviteFriends(objInvite.getShareMessage());
            }
        });
        more_copy = findViewById(R.id.more_copy);
        telegram_copy = findViewById(R.id.telegram_copy);
        sms_copy = findViewById(R.id.sms_copy);
        copy_wp = findViewById(R.id.copy_wp);

        copy_wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    if (objInvite.getReferralLink() != null) {
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setPackage("com.whatsapp");
//                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                        share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
//                        share.setType("text/plain");
//                        try {
//                            startActivity(share);
//                        } catch (ActivityNotFoundException ex) {
//                            Toast.makeText(Big_ReferUsersActivity.this, "Kindly install whatsapp first", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                    final boolean isAppInstalled = isAppAvailable(getApplicationContext(), BIG_Constants.whatsappPackageName);
                    if (isAppInstalled) {
                         shareType = "2";
                        callInviteFriends(objInvite.getShareMessage());
                    } else {
                        Toast.makeText(Big_ReferUsersActivity.this, "Kindly install whatsapp first", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }


        });
        sms_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    if (objInvite.getReferralLink() != null) {
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setPackage("com.instagram.android");
//                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                        share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
//                        share.setType("text/plain");
//                        try {
//                            startActivity(share);
//                        } catch (ActivityNotFoundException ex) {
//                            Toast.makeText(Big_ReferUsersActivity.this, "Kindly install Instagram first", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                    final boolean isAppInstalled = isAppAvailable(getApplicationContext(),BIG_Constants.instagramPackageName );
                    if (isAppInstalled) {
                        shareType = "3";
                        callInviteFriends(objInvite.getShareMessage());
                    } else {
                        Toast.makeText(Big_ReferUsersActivity.this, "instagram not Installed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
        });
        telegram_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    if (objInvite.getReferralLink() != null) {
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setPackage("org.telegram.messenger");
//                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                        share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
//                        share.setType("text/plain");
//                        try {
//                            startActivity(share);
//                        } catch (ActivityNotFoundException ex) {
//                            Toast.makeText(Big_ReferUsersActivity.this, "Kindly install Telegram first", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                    final boolean isAppInstalled = isAppAvailable(getApplicationContext(),BIG_Constants.telegramPackageName);
                    if (isAppInstalled) {
                        shareType = "1";
                        callInviteFriends(objInvite.getShareMessage());
                    } else {
                        Toast.makeText(Big_ReferUsersActivity.this, "Kindly install Telegram first", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
        });
        more_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objInvite.getReferralLink() != null) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
                    startActivity(Intent.createChooser(share, "Share via"));
                }
            }
        });


//        inviteBtnLottie = findViewById(R.id.inviteBtnLottie);
//        parentLottie = findViewById(R.id.parentLottie);
//        dataLottie = findViewById(R.id.dataLottie);
        // topLottie = view.findViewById(R.id.topLottie);
//        parentLottie.addAnimatorListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                isParentImageLoaded = true;
//                if (isParentImageLoaded && isTopImageLoaded) {
//                    layoutPoints.setVisibility(View.VISIBLE);
//                    tvInviteCode.setVisibility(View.VISIBLE);
//                    BIG_Common_Utils.dismissProgressLoader();
//                }
//            }
//        });
        //        topLottie.addAnimatorListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                isTopImageLoaded = true;
//                if (isParentImageLoaded && isTopImageLoaded) {
//                    layoutPoints.setVisibility(View.VISIBLE);
//                    tvInviteCode.setVisibility(View.VISIBLE);
//                    POC_Common_Utils.dismissProgressLoader();
//                }
//            }
//        });
        layoutLogin = findViewById(R.id.layoutLogin);
        layoutInvite = findViewById(R.id.layoutInvite);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Big_ReferUsersActivity.this, Big_LoginUserActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//          tvRules = findViewById(R.id.tvRules);
//        tvRules.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showRulesBottomSheet();
//            }
//        });
    }
    private void showDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.big_bottom_sheet_layout);
        LinearLayout referOk = dialog.findViewById(R.id.referOk);
        RecyclerView rvReferList = dialog.findViewById(R.id.rvReferList);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.big_bottom_dialog));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        Big_ReferSuggestionAdapter mReferAdapter=new Big_ReferSuggestionAdapter(Big_ReferUsersActivity.this,objInvite.getHowToWork());

        rvReferList.setAdapter(mReferAdapter);
        rvReferList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        referOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public void callInviteDataApi(Activity context) {
        try {
            if (!isResumeCalled) {
                new Big_Get_Invite_Async(context);
            } else {
                onInviteDataChanged(null);
            }
            isResumeCalled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void callInviteFriends(String message) {
        try {

            if (!BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                BIG_Common_Utils.NotifyLogin(Big_ReferUsersActivity.this);
            } else {
                if (!BIG_Common_Utils.isStringNullOrEmpty(objInvite.getShareImage())) {
                    if (ContextCompat.checkSelfPermission(Big_ReferUsersActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Big_ReferUsersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        shareMessage = message;
                        requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                    } else {

                        shareImageData(Big_ReferUsersActivity.this, objInvite.getShareImage(), objInvite.getShareMessage());
                    }
                } else {

                    shareImageData(Big_ReferUsersActivity.this, "", objInvite.getShareMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


    public void onInviteDataChanged(Big_Invite_Response_Model responseModel) {
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        /*if (responseModel != null) {
            Common_Utils.showProgressLoader(getActivity());
        }*/


        layoutLogin.setVisibility(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? View.GONE : View.VISIBLE);
        layoutInvite.setVisibility(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) ? View.VISIBLE : View.GONE);
        if (responseModel == null) {

            return;
        }
        objInvite = responseModel;
//        rvRules = findViewById(R.id.rvRules);

//        rvRules.setAdapter(new Big_Invite_Rules_Adapter(Big_ReferUsersActivity.this, objInvite.getHowToWork(), new Big_Invite_Rules_Adapter.ClickListener() {
//            @Override
//            public void onInfoButtonClick(int position, View v) {
//                try {
//                    BIG_AppLogger.getInstance().e("ABC","===================" +objInvite.getHowToWork().get(position).getDescription());
//
//                    balloon = new Balloon.Builder(Big_ReferUsersActivity.this)
//                            .setArrowSize(10)
//                            .setArrowOrientation(ArrowOrientation.TOP)
//                            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
//                            .setArrowPosition(0.5f)
//                            .setWidth(BalloonSizeSpec.WRAP)
//                            .setHeight(65)
//                            .setTextSize(15f)
//                            .setCornerRadius(4f)
//                            .setAlpha(0.9f)
//                            .setText(objInvite.getHowToWork().get(position).getDescription())
//                            .setTextColor(ContextCompat.getColor(Big_ReferUsersActivity.this, R.color.white))
//                            .setTextIsHtml(true)
//                            .setBackgroundColor(ContextCompat.getColor(Big_ReferUsersActivity.this, R.color.black_transparent))
//                            .setOnBalloonClickListener(new OnBalloonClickListener() {
//                                @Override
//                                public void onBalloonClick(@NonNull View view) {
//                                    balloon.dismiss();
//                                }
//                            })
//                            .setBalloonAnimation(BalloonAnimation.FADE)
//                            .setLifecycleOwner(Big_ReferUsersActivity.this)
//                            .build();
//                    balloon.showAlignBottom(v);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }));
//        rvRules.setLayoutManager(new LinearLayoutManager(Big_ReferUsersActivity.this));



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
                BIG_Common_Utils.loadTopBannerAd(new Big_ReferUsersActivity(), layoutTopAds, responseModel.getTopAds());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//         tvRules.setVisibility(responseModel.getHowToWork() != null && responseModel.getHowToWork().size() > 0 ? View.VISIBLE : View.GONE);

//        tvInvite.setText(responseModel.getBtnName());
//        tvInvite.setTextColor(Color.parseColor(responseModel.getBtnTextColor()));

        tvInviteNo.setText(responseModel.getTotalReferrals());
//        tvInviteNo.setTextColor(Color.parseColor(responseModel.getInviteNoTextColor()));

        tvInviteIncome.setText(responseModel.getTotalReferralIncome());
//        tvInviteIncome.setTextColor(Color.parseColor(responseModel.getInviteNoTextColor()));

        tvInviteCode.setText(responseModel.getReferralCode());
//        BIG_AppLogger.getInstance().e("code===",""+responseModel.getReferralCode());
//       tvInviteCode.setTextColor(Color.parseColor(responseModel.getBtnTextColor()));
//
//
//
//        lblInviteNo.setTextColor(Color.parseColor(responseModel.getInviteLabelTextColor()));
////        lblIncome.setTextColor(Color.parseColor(responseModel.getInviteLabelTextColor()));
//        viewSeparator.setBackgroundColor(Color.parseColor(responseModel.getSeparatorColor()));
//
//
//        tvLoginText.setTextColor(Color.parseColor(responseModel.getTextColor()));
//
//        BIG_Common_Utils.setLottieAnimation(inviteBtnLottie, responseModel.getReferButtonBackgroundImage());
//        inviteBtnLottie.playAnimation();
//
//        BIG_Common_Utils.setLottieAnimation(dataLottie, responseModel.getReferDataBackgroundImage());
//        dataLottie.playAnimation();
//
////        BIG_Common_Utils.setLottieAnimation(parentLottie, responseModel.getReferBackgroundImage());
////        parentLottie.playAnimation();
//
//        BIG_Common_Utils.setLottieAnimation(topLottie, responseModel.getReferTopImage());
//        topLottie.playAnimation();
//

    }
    private void shareImageData(Activity activity, String shareImage, String shareMessage) {
        Intent share;

        if (shareImage.trim().length() > 0) {
            String[] str = shareImage.trim().split("/");
            String extension = "";
            if (str[str.length - 1].contains(".")) {
                extension = str[str.length - 1].substring(str[str.length - 1].lastIndexOf("."));
            }
            if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".gif")) {
                extension = "";
            } else {
                extension = ".png";
            }
            File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_PICTURES) + File.separator)));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, str[str.length - 1] + extension);
            if (file.exists()) {
                try {
                    share = new Intent(Intent.ACTION_SEND);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= 24) {
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getPackageName() + ".provider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    share.setType("image/*");
                    if (shareImage.contains(".gif")) {
                        share.setType("image/gif");
                    } else {
                        share.setType("image/*");
                    }
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    share.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    if (shareType.equals("1")) {
                        share.setPackage(BIG_Constants.telegramPackageName);
                        activity.startActivity(share);
                    } else if (shareType.equals("2")) {
                        share.setPackage(BIG_Constants.whatsappPackageName);
                        activity.startActivity(share);
                    } else if (shareType.equals("3")) {
                        share.setPackage(BIG_Constants.instagramPackageName);
                        activity.startActivity(share);
                    } else {
                        activity.startActivity(Intent.createChooser(share, "Share"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (BIG_Common_Utils.isNetworkAvailable(Big_ReferUsersActivity.this)) {
                new Big_Download_Image_Share_Async(activity, file, shareImage, "", shareMessage).execute();
            }
        } else {
            try {
                share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                share.putExtra(Intent.EXTRA_TEXT, shareMessage);
                share.setType("text/plain");
                if (shareType.equals("1")) {
                    share.setPackage(BIG_Constants.telegramPackageName);
                    activity.startActivity(share);
                } else if (shareType.equals("2")) {
                    share.setPackage(BIG_Constants.whatsappPackageName);
                    activity.startActivity(share);
                } else if (shareType.equals("3")) {
                    share.setPackage(BIG_Constants.instagramPackageName);
                    activity.startActivity(share);
                } else {
                    activity.startActivity(Intent.createChooser(share, "Share"));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_CODE_STORAGE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareImageData(Big_ReferUsersActivity.this, objInvite.getShareImage(), objInvite.getShareMessage());
                } else {
                    BIG_Common_Utils.setToast(Big_ReferUsersActivity.this, "Allow storage permissions!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}