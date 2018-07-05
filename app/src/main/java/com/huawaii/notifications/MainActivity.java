/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 16-8-11.
 */
package com.huawaii.notifications;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huawaii.notifications.Settings.SettingsCallback;
import com.huawaii.notifications.Settings.SettingsGroup1;
import com.huawaii.notifications.Settings.SettingsGroup2;
import com.huawaii.notifications.Settings.SettingsTips;
import com.huawaii.notifications.utils.PopupWindowUtils;


public class MainActivity extends Activity implements SettingsCallback {

    private CreateNotification mCreateNotification;
    private int mNotificationTemplateType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreateNotification = new CreateNotification(this);
        initSettingsView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        int id = item.getItemId();
        switch (id) {
            case R.id.action_capture_notification_info:
                intent.setAction("com.huawaii.notification.CAPTURE_NOTIFICATION_INFO");
                startActivity(intent);
                return true;
            case R.id.action_settings:
                Uri uri = Uri.parse("https://github.com/huawaii/NotificationDemo/blob/master/README.md");
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSettingsView() {
        SettingsGroup1 mSettingGroup1 = (SettingsGroup1) findViewById(R.id.settings_first);
        mSettingGroup1.setCallback(this);

        SettingsGroup2 mSettingGroup2 = (SettingsGroup2) findViewById(R.id.settings_second);
        mSettingGroup2.setCallback(this);

        SettingsTips mSettingTips = (SettingsTips) findViewById(R.id.settings_tips);
        mSettingTips.setCallback(this);
    }

    @Override
    public void onNotificationTemplateChange(int templateType, SettingsGroup1.CustomState customState, int buttonNum) {
        mNotificationTemplateType = templateType;
        mCreateNotification.setSmallCustom(customState.smallCustom);
        mCreateNotification.setBigCustom(customState.bigCustom);
        mCreateNotification.setHeadsUpCustom(customState.headsUpCustom);
        mCreateNotification.setButtonNum(buttonNum);
    }

    @Override
    public void onNotificationFeatureChange(SettingsGroup2.FeatureState featureState) {
        mCreateNotification.setHeadsUp(featureState.headsUp);
        mCreateNotification.setVibrate(featureState.vibrate);
        mCreateNotification.setSound(featureState.sound);
        mCreateNotification.setLights(featureState.lights);
        mCreateNotification.setOnGoing(featureState.onGoing);
        mCreateNotification.setReply(featureState.reply);
        mCreateNotification.setGroup(featureState.group);
    }

    @Override
    public void onNotificationCustomChange() {

    }

    @Override
    public void onNotificationTipsChange(int number, int period) {
        mCreateNotification.setSendParameters(number, period);
    }

    @Override
    public void onSettingsTipsClick(View popupView) {
        new PopupWindowUtils(this).startPopupWindow(popupView);
    }

    public void sendNotification(View view) {
        mCreateNotification.sendNotificationTimer(mNotificationTemplateType);
    }

    public void clearNotification(View view) {
        mCreateNotification.cancelAllNotification();
    }

}
