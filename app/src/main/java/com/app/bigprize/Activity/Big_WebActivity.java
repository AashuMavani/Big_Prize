package com.app.bigprize.Activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;

public class Big_WebActivity extends AppCompatActivity {
    String IntentURL, IntentTitle = "";
    private TextView tvTitle;
    private ImageView ivBack;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_WebActivity.this);
        setContentView(R.layout.big_activity_web2);
        IntentURL = getIntent().getStringExtra("URL");
        IntentTitle = getIntent().getStringExtra("Title");

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(IntentTitle);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        webView = findViewById(R.id.webView);

        swipeRefreshLayout.setRefreshing(true);
        LoadPage(IntentURL);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(IntentURL);
            }
        });

    }
    public void LoadPage(String Url) {
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (BIG_Common_Utils.isNetworkAvailable(Big_WebActivity.this)) {
            webView.loadUrl(Url);
        } else {
            BIG_Common_Utils.setToast(Big_WebActivity.this, "No internet connection");
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (BIG_Common_Utils.isNetworkAvailable(Big_WebActivity.this)) {
                view.loadUrl(url);
            } else {
                BIG_Common_Utils.setToast(Big_WebActivity.this, "No internet connection");
                swipeRefreshLayout.setRefreshing(false);
            }
            return true;
        }
    }
}