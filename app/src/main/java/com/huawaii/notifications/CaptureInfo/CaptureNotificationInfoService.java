/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/25.
 */
package com.huawaii.notifications.CaptureInfo;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;


public class CaptureNotificationInfoService extends NotificationListenerService {

    protected static CaptureNotificationInfoService sService;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sService = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sService = null;
    }
}
