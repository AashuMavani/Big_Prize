package com.app.bigprize.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.app.bigprize.Async.Big_Login_Async;
import com.app.bigprize.Async.Models.Big_Home_Slider_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Customviews.Big_Recyclerview.Big_Recycler_View_Pager_Login;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Request_Model;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import java.util.List;

public class Big_LoginUserActivity extends AppCompatActivity {
    private EditText etReferralCode;
    private LinearLayout layoutLogin;
    private AppCompatButton btnSkip;
    private GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    private AppCompatCheckBox cbAgree;
    private GoogleSignInClient mGoogleSignInClient;
    private BIG_Request_Model requestModel;
    private List<Big_Home_Slider_Item> loginSlider;
    private RelativeLayout layoutSlider;
    private Big_Recycler_View_Pager_Login rvSlider;
    private  TextView tvPrivacyPolicy,tvTerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_LoginUserActivity.this);
        setContentView(R.layout.big_activity_login_user2);
        loginSlider = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class).getLoginSlider();
        initView();
    }
    private void initView() {
        rvSlider = findViewById(R.id.rvSlider);
        layoutSlider = findViewById(R.id.layoutSlider);
        etReferralCode = findViewById(R.id.etReferralCode);
        etReferralCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etReferralCode.post(new Runnable() {
                    @Override
                    public void run() {
                        etReferralCode.setLetterSpacing(etReferralCode.getText().toString().length() > 0 ? 0.2f : 0.0f);
                    }
                });
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        if (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.ReferData).length() > 0) {
            try {
                etReferralCode.setText(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.ReferData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        requestModel = new BIG_Request_Model();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        tvTerms = findViewById(R.id.tvTerms);
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Big_LoginUserActivity.this, Big_WebActivity.class);
                    i.putExtra("URL", new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class).getTermsConditionUrl());
                    i.putExtra("Title", getResources().getString(R.string.app_terms));
                    i.putExtra("Source", "UserConsent");
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Big_LoginUserActivity.this, Big_WebActivity.class);
                    i.putExtra("URL", new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class).getPrivacyPolicy());
                    i.putExtra("Title", getResources().getString(R.string.privacy_policy));
                    i.putExtra("Source", "UserConsent");
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cbAgree = findViewById(R.id.cbAgree);
        cbAgree.setChecked(BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_USER_CONSENT_ACCEPTED));

        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

              //  Toast.makeText(Big_LoginUserActivity.this, ""+isChecked, Toast.LENGTH_SHORT).show();

                BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_USER_CONSENT_ACCEPTED, isChecked);
            }
        });

        layoutLogin = findViewById(R.id.layoutLogin);
        layoutLogin.setOnClickListener(v -> {
            if(cbAgree.isChecked()){

                BIG_Common_Utils.setEnableDisable(Big_LoginUserActivity.this, v);
                if (isValidReferralCode()) {
                    requestModel.setReferralCode(etReferralCode.getText().toString());
                    signIn();

                } else {
                    BIG_Common_Utils.Notify(Big_LoginUserActivity.this, getString(R.string.app_name), "Please enter valid referral code", false);
                }
            }
            else {
                Toast.makeText(this, "Please accept Privacy", Toast.LENGTH_SHORT).show();

            }
        });



        btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(v -> {
            if (cbAgree.isChecked()){
                BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_SKIPPED_LOGIN, true);
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_FIRST_LOGIN, true) || isTaskRoot()) {
                    BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_FIRST_LOGIN, false);
                    Intent in = new Intent(Big_LoginUserActivity.this, Big_MainActivity.class);
                    in.putExtra("isFromLogin", true);
                    startActivity(in);
                }
                finish();
            }
            else {
                Toast.makeText(this, "Please accept Privacy", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private boolean isValidReferralCode() {
        if (etReferralCode.getText().toString().trim().length() > 0) {
            if (etReferralCode.getText().toString().trim().length() < 6 || etReferralCode.getText().toString().trim().length() > 10) {
                return false;
            } else {
                String code = etReferralCode.getText().toString().trim();
                boolean flag = true;
                for (int i = 0; i < code.length(); i++) {
                    if (!Character.isDigit(code.charAt(i)) && !Character.isUpperCase(code.charAt(i))) {
                        flag = false;
                        break;
                    }
                }
                return flag;
            }
        }
        return true;
    }

    private void signIn() {


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                        BIG_Common_Utils.showProgressLoader(Big_LoginUserActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        BIG_Common_Utils.dismissProgressLoader();
                        FirebaseAuth.getInstance().signOut();
                        mGoogleSignInClient.signOut();
                        requestModel.setEmailId("asmitamavani098@gmail.com");
                        requestModel.setProfileImage("https://lh3.googleusercontent.com/a/AAcHTteE_3XfZf4C8ABH41VQ2v-8gV3J9cnIAWXz4vFAXCB_=s256-c");
                        requestModel.setFirstName("Asmita");
                        requestModel.setLastName("Mavani");
                        new Big_Login_Async(Big_LoginUserActivity.this, requestModel);
                    }
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseAuth.getInstance().signOut();
                                mGoogleSignInClient.signOut();
                                if (user.getEmail() != null) {
                                    requestModel.setEmailId(user.getEmail());
                                }
                                if (user.getPhotoUrl() != null) {
                                    requestModel.setProfileImage(user.getPhotoUrl().toString().trim().replace("96", "256"));
                                    BIG_AppLogger.getInstance().e("#photourl", "" + user.getPhotoUrl());
                                }
                                if (user.getDisplayName().trim().contains(" ")) {
                                    String[] str = user.getDisplayName().trim().split(" ");
                                    requestModel.setFirstName(str[0]);
                                    requestModel.setLastName(str[1]);
                                } else {
                                    requestModel.setFirstName(user.getDisplayName());
                                    requestModel.setLastName("");
                                }
                                BIG_Common_Utils.dismissProgressLoader();
                                requestModel.setCaptchaToken("");

                                new Big_Login_Async(Big_LoginUserActivity.this, requestModel);

                            } else {
                                BIG_Common_Utils.dismissProgressLoader();
                                FirebaseAuth.getInstance().signOut();
                                mGoogleSignInClient.signOut();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN) && !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_SKIPPED_LOGIN)) {
            System.exit(0);
        }
    }
}