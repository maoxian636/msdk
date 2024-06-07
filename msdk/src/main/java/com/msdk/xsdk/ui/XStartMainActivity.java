package com.msdk.xsdk.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.webbridge.AdjustBridge;
import com.msdk.xsdk.R;
import com.msdk.xsdk.bean.XLogName;
import com.msdk.xsdk.bean.XMSDKData;
import com.msdk.xsdk.detection.AdjustUtil;
import com.msdk.xsdk.detection.AppsFlyersUtils;
import com.msdk.xsdk.utils.XAES;
import com.msdk.xsdk.utils.XAndroidBug5497WorkaroundUtils;
import com.msdk.xsdk.utils.XMobilePhoneAdaptation;
import com.msdk.xsdk.utils.XWebChromeClient;
import com.msdk.xsdk.utils.XXmlData;

public class XStartMainActivity extends AppCompatActivity {

    private ProgressBar strat_progress;
    private WebView webView;
    private XWebChromeClient myWebChromeClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        XMobilePhoneAdaptation.setSystenFull(this);
        setContentView(R.layout.activity_main_strat_x);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_start), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initview();


    }

    private void initview() {
        strat_progress = findViewById(R.id.strat_progress);
        webView = findViewById(R.id.strat_webView);
        initWebViewConfig();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith(XAES.B2SrclYHQH3aCQJL(XMSDKData.MSDK_ME))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    return true;
                }
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp")) {
                    view.loadUrl(url);
                    Log.e("mao", url);
                    return false;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (view != null && view.getProgress() == 100) {
                    strat_progress.setVisibility(View.GONE);
                }
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.e("mao", url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        XLogName.MSDKLog('i', "路径-->", (String) XXmlData.getParam(this, "ad_path", ""));
        XLogName.MSDKLog('i', "检测状态-->", (Boolean) XXmlData.getParam(this, "ad_type", false));
        if ((boolean)XXmlData.getParam(this, "ad_type", false)){
            webView.addJavascriptInterface(new AdjustUtil(this), (String) XXmlData.getParam(this, "ad_path", ""));
        }else {
            webView.addJavascriptInterface(new AppsFlyersUtils(this), (String) XXmlData.getParam(this, "ad_path", ""));
        }

        Intent intent = getIntent();
        webView.loadUrl(intent.getStringExtra("url"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((boolean)XXmlData.getParam(this, "ad_type", false)){
            Adjust.onResume();
        }else {
            XLogName.MSDKLog('w', "", "Adjust onResume关闭");
        }

    }

    @Override
    protected void onDestroy() {

        if ((boolean)XXmlData.getParam(this, "ad_type", false)){
            AdjustBridge.unregister();
        }else {
            XLogName.MSDKLog('w', "", "Adjust unregister关闭");
        }

        super.onDestroy();
    }

    private void initWebViewConfig() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setBackgroundColor(0);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        myWebChromeClient = new XWebChromeClient(this);
        webView.setWebChromeClient(myWebChromeClient);
        XAndroidBug5497WorkaroundUtils.assistActivity(this);
    }

    // 处理文件选择器返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 将结果传递给 MyWebChromeClient 处理
        myWebChromeClient.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}