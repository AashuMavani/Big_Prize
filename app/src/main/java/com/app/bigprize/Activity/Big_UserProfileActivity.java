package com.app.bigprize.Activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.app.bigprize.Async.Big_Get_User_Profile_Async;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_User_Details;
import com.app.bigprize.Async.Models.Big_User_Profile_Model;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class Big_UserProfileActivity extends AppCompatActivity {
    private View  viewShine, viewShineLogin;
    private ImageView ivProfilePic;
    private TextView tvName, tvEmail, tvTotalPoints;
    private Big_Response_Model responseMain;
    private Big_User_Details userDetails;
    private ImageView ivMenu;
    private AppCompatButton btnWithdraw, btnLogin;
    private LinearLayout  layoutInvite, layoutFAQ, layoutFeedback, layoutLogout, layoutLogin, layoutDeleteAccount,layoutWallet;
    private LinearLayout layoutProfile;
    private Big_User_Profile_Model responseModel;
    private Animation animUpDown;

    public Big_UserProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_activity_user_profile);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        layoutWallet = findViewById(R.id.layoutWallet);
        viewShineLogin = findViewById(R.id.viewShineLogin);
        viewShine = findViewById(R.id.viewShine);
        layoutProfile = findViewById(R.id.layoutProfile);
        layoutLogin = findViewById(R.id.layoutLogin);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);


        ivMenu = findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tvTotalPoints = findViewById(R.id.tvTotalPoints);
        layoutInvite = findViewById(R.id.layoutInvite);


            layoutInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_UserProfileActivity.this, Big_ReferUsersActivity.class));
            }
        });


        layoutFAQ = findViewById(R.id.layoutFAQ);
        layoutFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_UserProfileActivity.this, Big_ActivityFAQ.class));
            }
        });

        layoutFeedback = findViewById(R.id.layoutFeedback);
        layoutFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_UserProfileActivity.this, Big_FeedbackSubmitActivity.class).putExtra("title", "Give Feedback"));
            }
        });


        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_UserProfileActivity.this, Big_WithdrawTypesActivity.class));
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Big_UserProfileActivity.this, Big_LoginUserActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layoutLogout = findViewById(R.id.layoutLogout);
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BIG_Common_Utils.NotifyLogout(Big_UserProfileActivity.this, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layoutDeleteAccount = findViewById(R.id.layoutDeleteAccount);
        layoutDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BIG_Common_Utils.NotifyDeleteAccount(Big_UserProfileActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        animUpDown =AnimationUtils.loadAnimation(Big_UserProfileActivity.this,R.anim.big_left_right);
        animUpDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    viewShine.startAnimation(animUpDown);
                } else {
                    viewShineLogin.startAnimation(animUpDown);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
            layoutWallet.setVisibility(View.VISIBLE);
            layoutProfile.setVisibility(View.VISIBLE);
            layoutLogout.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
            layoutInvite.setVisibility(View.VISIBLE);
//            layoutMakeMoney.setVisibility(View.VISIBLE);
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAccountDeleteOption()) && responseMain.getIsShowAccountDeleteOption().equals("1")) {
                layoutDeleteAccount.setVisibility(View.VISIBLE);
            }
            viewShine.startAnimation(animUpDown);
        } else {
            layoutProfile.setVisibility(View.GONE);
            layoutWallet.setVisibility(View.GONE);
//            layoutWalletWithdraw.setVisibility(View.GONE);
            layoutInvite.setVisibility(View.GONE);
//            layoutMakeMoney.setVisibility(View.GONE);
            layoutLogout.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
            layoutDeleteAccount.setVisibility(View.GONE);
            viewShineLogin.startAnimation(animUpDown);
        }
        if(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN))
        {
            new Big_Get_User_Profile_Async(Big_UserProfileActivity.this);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserPoints();
    }

    private void updateUserPoints() {
        try {
            userDetails = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.User_Details), Big_User_Details.class);
            tvTotalPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            tvEmail.setText(userDetails.getEmailId());
//            tvName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
//            tvExpendPoints.setText(userDetails.getWithdrawPoint());
            BIG_AppLogger.getInstance().e("#onresumepo Ruby", userDetails.getWithdrawPoint());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


    public void onProfileDataChanged(Big_User_Profile_Model responseModel1) {
        responseModel=responseModel1;

        try {
            // Load home note webview top
            try {
                if (responseModel != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
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
                if (responseModel != null && responseModel.getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTopAds().getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    BIG_Common_Utils.loadTopBannerAd(Big_UserProfileActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvTotalPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            tvEmail.setText(userDetails.getEmailId());
            tvName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
            if (userDetails.getProfileImage() != null) {
                Glide.with(new Big_UserProfileActivity()).load(userDetails.getProfileImage()).override(getResources().getDimensionPixelSize(R.dimen.dim_80), getResources().getDimensionPixelSize(R.dimen.dim_80)).into(ivProfilePic);
            }
//            tvExpendPoints.setText(userDetails.getWithdrawPoint());
//            ivEdit.setVisibility(View.VISIBLE);
            // start the animation
            viewShine.startAnimation(animUpDown);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Big_User_Profile_Model getResponseModel() {
        return responseModel;
    }

    public void setResponseModel(Big_User_Profile_Model responseModel) {
        this.responseModel = responseModel;
    }
}