package com.yangzai.superapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.msdk.xsdk.MSDKSingleton
import com.msdk.xsdk.Test
import com.msdk.xsdk.bean.XConfigData
import com.msdk.xsdk.utils.XException
import com.yangzai.superapp.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        try {
            MSDKSingleton.getInstance(this).setMSDKConfig(XConfigData("xxxx", "aaaaaa"))
            MSDKSingleton.setDebug(true)
            MSDKSingleton.getInstance(this).initConfig()
        } catch (e: XException) {
            throw RuntimeException(e)
        }

    }
}

