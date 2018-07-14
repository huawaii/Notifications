package com.huawaii.notifications.reflect;

import android.app.Notification;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Copyright (C) 2017 Meizu Telecom Equipment Co., Ltd. All rights reserved.
 *
 * @author lizongheng@meizu.com (李宗恒)
 */
public class NotificationProxy extends Proxy {
    private static Class<?> sClass = Notification.Builder.class;
    private static Field sField = null;
    private static Object sObject = null;
    private static Method sSetProgressBarStype = null;
    private static Method sSetCircleProgressBarColor = null;
    private static Method ssetCircleProgressRimColor = null;

    public NotificationProxy() {
    }

    public static void setProgressBarStype(Notification.Builder builder, boolean isCircle) {
        try {
            sField = sClass.getField("mFlymeNotificationBuilder");
            sObject = sField.get(builder);
            sSetProgressBarStype = sField.getType().getDeclaredMethod("setCircleProgressBar", Boolean.TYPE);
            if (sObject != null) {
                invoke(sSetProgressBarStype, sObject, new Object[]{isCircle});
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void setCircleProgressBarColor(int color) {
        try {
            if (sField != null && sObject != null) {
                sSetCircleProgressBarColor = sField.getType().getDeclaredMethod("setCircleProgressBarColor", Integer.TYPE);
                invoke(sSetCircleProgressBarColor, sObject, new Object[]{color});
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void setCircleProgressRimColor(int color) {
        try {
            if (sField != null && sObject != null) {
                ssetCircleProgressRimColor = sField.getType().getDeclaredMethod("ssetCircleProgressRimColor", Integer.TYPE);
                invoke(ssetCircleProgressRimColor, sObject, new Object[]{color});
            }
        } catch (Exception var2) {
            ;
        }

    }
}
