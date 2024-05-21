package com.msdk.xsdk.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class XWebChromeClient extends WebChromeClient {

    private ValueCallback<Uri[]> filePathCallback;
    private final int FILE_CHOOSER_RESULT_CODE = 1;
    private Activity activity;

    // 构造方法，传入关联的 Activity
    public XWebChromeClient(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        this.filePathCallback = filePathCallback;

        // 启动文件选择器
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        // 调用关联的 Activity 的 startActivityForResult
        activity.startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);

        return true;
    }

    // 处理文件选择器返回的结果
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // 获取用户选择的文件
                Uri result = data.getData();

                // 处理文件选择的逻辑，可以根据需求进行上传等操作

                // 将结果传递给 WebView
                if (filePathCallback != null) {
                    filePathCallback.onReceiveValue(new Uri[]{result});
                }
            } else {
                // 用户取消了文件选择
                if (filePathCallback != null) {
                    filePathCallback.onReceiveValue(null);
                }
            }
        }
    }
}
