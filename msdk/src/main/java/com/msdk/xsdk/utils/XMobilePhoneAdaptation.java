package com.msdk.xsdk.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

public class XMobilePhoneAdaptation {


    public static void setSystenFull(Activity activity){

        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = (activity).getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = (activity).getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }
}
