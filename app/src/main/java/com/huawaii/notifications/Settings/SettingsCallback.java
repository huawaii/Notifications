/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/15.
 */
package com.huawaii.notifications.Settings;


import android.view.View;

public interface SettingsCallback {
    void onNotificationTemplateChange(int templateType, SettingsGroup1.CustomState customState, int buttonNum);

    void onNotificationFeatureChange(SettingsGroup2.FeatureState featureState);

    void onNotificationCustomChange();

    void onNotificationTipsChange(int number, int period);

    void onSettingsTipsClick(View popupView);
}
