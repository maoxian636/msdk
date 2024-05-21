package com.msdk.xsdk.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class XAndroidBug5497WorkaroundUtils {
    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    public static void assistActivity(Activity activity) {
        new XAndroidBug5497WorkaroundUtils(activity);
    }

    private XAndroidBug5497WorkaroundUtils(Activity activity) {
        if (activity != null) {
            FrameLayout content = activity.findViewById(android.R.id.content);
            if (content != null) {
                mChildOfContent = content.getChildAt(0);
                if (mChildOfContent != null) {
                    mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            possiblyResizeChildOfContent();
                        }
                    });
                    frameLayoutParams = mChildOfContent.getLayoutParams();
                }
            }
        }
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            // 如果两次高度不一致，将计算的可视高度设置成视图的高度
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();// 请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        // 计算视图可视高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top); // 这里修改为底部减去顶部的高度，以避免与 WebView 底部产生冲突
    }
}
