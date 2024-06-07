package com.msdk.xsdk.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.msdk.xsdk.R
import com.msdk.xsdk.bean.XLogName
import com.msdk.xsdk.bean.XMSDKData
import com.msdk.xsdk.utils.XAES
import com.msdk.xsdk.utils.XAndroidBug5497WorkaroundUtils
import com.msdk.xsdk.utils.XMobilePhoneAdaptation
import com.msdk.xsdk.utils.XWebChromeClient
import org.json.JSONException
import org.json.JSONObject


class XNewActivity : AppCompatActivity() {
    private lateinit var fl: LinearLayout
    private lateinit var webView: WebView
    private lateinit var iv_fin: ImageView
    private lateinit var xWwebChromeClient: XWebChromeClient

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        XMobilePhoneAdaptation.setSystenFull(this)
        setContentView(R.layout.activity_new_x)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        setWebSetting()
        setShould()
        getIntentData()

    }

    private fun getIntentData() {
        val intent = intent
        val url1 = intent.getStringExtra(XAES.B2SrclYHQH3aCQJL(XMSDKData.MSDK_DATA))
        if (!url1.isNullOrEmpty()) {
            val eventValuesRegister: MutableMap<String, String> = HashMap()
            try {
                val jsonObject = JSONObject(url1)
                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = jsonObject.getString(key)
                    eventValuesRegister[key] = value
                }
                webView.loadUrl(eventValuesRegister[XAES.B2SrclYHQH3aCQJL(XMSDKData.MSDK_URL_VALVE)].toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }else{
            XLogName.MSDKLog('e', "getIntentData", "未获取openWindow数据")
        }
    }

    // 处理文件选择器返回结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 将结果传递给 MyWebChromeClient 处理
        xWwebChromeClient.onActivityResult(requestCode, resultCode, data)
    }
    private fun setShould() {
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                var url = request?.url.toString()
                if (url.startsWith(XAES.B2SrclYHQH3aCQJL(XMSDKData.MSDK_TG))) {
                    val JtCOkVvM = url.substring(url.indexOf("=") + 1)
                    val cZbOpcis = XAES.B2SrclYHQH3aCQJL(XMSDKData.MSDK_ME)+JtCOkVvM
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cZbOpcis))
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                    return true;
                }
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp")) {
                    view!!.loadUrl(url)
                    if (url.indexOf(XAES.B2SrclYHQH3aCQJL(XMSDKData.MSDK_PIX)) != -1) {
                        iv_fin.visibility = View.VISIBLE
                    }
                    return false
                }
                return true
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                finish()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view != null && view.getProgress() == 100) {
                    fl.visibility = View.GONE
                }
            }

        })

        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            Log.e("mao", url)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
            finish()
        }

    }

    private fun setWebSetting() {
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.allowFileAccess = true
        webView.setBackgroundColor(0)
        xWwebChromeClient = XWebChromeClient(this)
        webView.webChromeClient = xWwebChromeClient
        XAndroidBug5497WorkaroundUtils.assistActivity(this)

    }

    private fun initView() {
        fl = findViewById(R.id.fl)
        webView = findViewById(R.id.webView)
        iv_fin = findViewById(R.id.iv_fin)
        iv_fin.setOnClickListener { finish() }
    }

    override fun onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}