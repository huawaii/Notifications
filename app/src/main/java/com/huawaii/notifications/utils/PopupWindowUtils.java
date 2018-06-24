/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/26.
 */
package com.huawaii.notifications.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.huawaii.notifications.R;

public class PopupWindowUtils {

    private Activity mActivity;

    public PopupWindowUtils(Activity activity) {
        mActivity = activity;
    }

    /**
     * @param popupView 禁止有 parent view
     *                  java.lang.IllegalStateException: The specified child already has a parent. You must call
     *                  removeView() on the child's parent first.
     */
    public void startPopupWindow(View popupView) {
        PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        window.setAnimationStyle(R.style.popup_window_anim);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        window.setFocusable(true);
        window.showAtLocation(getActivityRootView(mActivity), Gravity.CENTER, 0, 0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1.0f);
            }
        });
        darkenBackground(0.2f);
        window.update();
    }

    private void darkenBackground(Float alpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        mActivity.getWindow().setAttributes(lp);
    }

    private View getActivityRootView(Activity activity) {
        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
        //return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }
}
