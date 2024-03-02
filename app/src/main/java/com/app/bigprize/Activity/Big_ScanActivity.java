package com.app.bigprize.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.bigprize.Async.Big_ValidateUpiAsync;
import com.app.bigprize.Async.Models.Big_Giveaway_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Activity_Manager;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Big_ScanActivity extends AppCompatActivity {
    private ViewGroup contentFrame;
    private ZXingScannerView zXingScannerView;
    private ArrayList<Integer> mSelectedIndices;
    private FloatingActionButton flash, gallery;
    private boolean isFlash, isCallingApi = false;
    private int camId, rearCamId;
    public final int Request_Storage_resize = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ScanActivity.this);
        setContentView(R.layout.big_activity_scan);
        try {
            initVar();
            zXingScannerView = new ZXingScannerView(Big_ScanActivity.this);
            setupFormats();
            initView();
            initListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Big_Response_Model responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getPoweredByScanAndImage())) {
                ImageView ivPoweredBy = findViewById(R.id.ivPoweredBy);
                Glide.with(Big_ScanActivity.this)
                        .load(responseMain.getPoweredByScanAndImage())
                        .into(ivPoweredBy);
                ivPoweredBy.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ScanActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.SCAN_AND_PAY)
                            .putExtra("title", "Scan and Pay History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ScanActivity.this);
                }
            }
        });

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initVar() {
        isFlash = BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.FLASH, false); // flash off by default
        camId = BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.CAM_ID); // back camera by default
        if (camId == -1) {
            camId = rearCamId;
        }
        loadCams();
    }

    private void initView() {
        contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        flash = (FloatingActionButton) findViewById(R.id.flash);
        gallery = (FloatingActionButton) findViewById(R.id.gallery);

        initConfigs();
    }

    private void initListener() {
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFlash();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request_Storage_resize);
                } else {
                    SelectImage();
                }
            }
        });

        zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                String resultStr = result.getText();
                zXingScannerView.resumeCameraPreview(this);
                if (resultStr != null && resultStr.length() > 0 && !isCallingApi) {
                    isCallingApi = true;
                    new Big_ValidateUpiAsync(Big_ScanActivity.this, resultStr);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_Storage_resize) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage();
            } else {
                BIG_Common_Utils.setToast(Big_ScanActivity.this, "Allow storage permission!");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Storage_resize) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    SelectImage();
                } else {
                    BIG_Common_Utils.setToast(Big_ScanActivity.this, "Allow storage permission!");
                }
            }
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            resultIntent -> {
                if (resultIntent.getResultCode() == Activity.RESULT_OK) {
                    try {
                        if (resultIntent.getData() != null) {
                            Uri uriImage = resultIntent.getData().getData();
                            if (uriImage != null) {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                                    String contents = null;
                                    int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
                                    bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                                    LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
                                    BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
                                    Reader reader = new MultiFormatReader();
                                    Result result = reader.decode(bitmap1);
                                    contents = result.getText();
                                    if (contents != null && contents.length() > 0 && !isCallingApi) {
                                        isCallingApi = true;
                                        new  Big_ValidateUpiAsync(Big_ScanActivity.this, contents);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    private void SelectImage() {
        BIG_Activity_Manager.isShowAppOpenAd = false;
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
    }

    public void activateScanner() {
        try {
            if (zXingScannerView != null) {
                if (zXingScannerView.getParent() != null) {
                    ((ViewGroup) zXingScannerView.getParent()).removeView(zXingScannerView); // to prevent crush on re adding view
                }
                contentFrame.addView(zXingScannerView);

                if (zXingScannerView.isActivated()) {
                    zXingScannerView.stopCamera();
                }

                zXingScannerView.startCamera(camId);
                zXingScannerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            zXingScannerView.setFlash(isFlash);
//                            zXingScannerView.setAutoFocus(isAutoFocus);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupFormats() {
        try {
            List<BarcodeFormat> formats = new ArrayList<>();
            if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
                mSelectedIndices = new ArrayList<>();
                for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                    mSelectedIndices.add(i);
                }
            }

            for (int index : mSelectedIndices) {
                formats.add(ZXingScannerView.ALL_FORMATS.get(index));
            }
            if (zXingScannerView != null) {
                zXingScannerView.setFormats(formats);
            }
            zXingScannerView.setBorderColor(getResources().getColor(R.color.colorPrimary));
            zXingScannerView.setBorderStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dim_5));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activateScanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    public void stopCamera() {
        try {
            if (zXingScannerView != null) {
                zXingScannerView.stopCamera();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleFlash() {
        try {
            if (isFlash) {
                isFlash = false;
                flash.setImageResource(R.drawable.ic_flash_on);
            } else {
                isFlash = true;
                flash.setImageResource(R.drawable.ic_flash_off);
            }
            BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.FLASH, isFlash);

            zXingScannerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    zXingScannerView.setFlash(isFlash);
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initConfigs() {
        try {
            if (isFlash) {
                flash.setImageResource(R.drawable.ic_flash_off);
            } else {
                flash.setImageResource(R.drawable.ic_flash_on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCams() {
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    // frontCamId = i;
                } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    rearCamId = i;
                }
            }
            BIG_SharePrefs.getInstance().putInt(BIG_SharePrefs.CAM_ID, rearCamId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void verifyUpiIdResponse(Big_Giveaway_Model responseModel) {
        try {
            if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
                BIG_Common_Utils.logFirebaseEvent(Big_ScanActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Upi_Verify", "Success");
                startActivity(new Intent(Big_ScanActivity.this, Big_ScanAndPayActivity.class)
                        .putExtra("upiId", responseModel.getUpiId())
                        .putExtra("name", responseModel.getRecipientName())
                        .putExtra("upiImage", responseModel.getUpiImage())
                        .putExtra("homeNote", responseModel.getHomeNote())
                        .putExtra("topAds", responseModel.getTopAds())
                        .putExtra("paymentAmount", Integer.parseInt(responseModel.getPaymentAmount()))
                        .putExtra("minPayAmountForCharges", Integer.parseInt(responseModel.getMinPayAmountForCharges()))
                        .putExtra("minPayAmount", Integer.parseInt(responseModel.getMinPayAmount()))
                        .putExtra("charges", Integer.parseInt(responseModel.getExtraCharge())));
                isCallingApi = false;
            } else {
                showErrorMessage(getString(R.string.app_name), responseModel.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String title, String message) {
        try {
            final Dialog dialog1 = new Dialog(Big_ScanActivity.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.big_popup_message_notify);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ScanActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
            dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    isCallingApi = false;
                }
            });
            if (!isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}