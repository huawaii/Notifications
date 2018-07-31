package com.huawaii.notifications.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.huawaii.notifications.CaptureInfo.CaptureNotificationInfoService;

/**
 * Copyright (C) 2018 Meizu Telecom Equipment Co., Ltd. All rights reserved.
 *
 * @author lizongheng@meizu.com (李宗恒)
 */
public class NotificationListenerUtils {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    public static boolean isEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String s : names) {
                final ComponentName cn = ComponentName.unflattenFromString(s);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static void refreshNotificationListener(Context context, PackageManager pm) {
        if (isEnabled(context) && CaptureNotificationInfoService.sService == null) {
            toggleNotificationListenerService(context, pm);
        }
    }

    private static void toggleNotificationListenerService(Context context, PackageManager pm) {
        pm.setComponentEnabledSetting(new ComponentName(context, "com.huawaii.notifications.CaptureInfo.CaptureNotificationInfoService"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, "com.huawaii.notifications.CaptureInfo.CaptureNotificationInfoService"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
