/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/8.
 */
package com.huawaii.notifications.Settings;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.huawaii.notifications.R;


public class SettingsGroup2 extends RelativeLayout {

    private final static String TAG = "SettingsGroup2";
    private final FeatureState mFeatureState = new FeatureState();
    private Context mContext;
    private SettingsCallback mCallback;

    public SettingsGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setCallback(SettingsCallback callback) {
        mCallback = callback;
        initCheckBox();
    }

    private void initCheckBox() {
        final CheckBox headsUp = (CheckBox) findViewById(R.id.cb_headsUp);
        final CheckBox vibrate = (CheckBox) findViewById(R.id.cb_vibrate);
        final CheckBox sound = (CheckBox) findViewById(R.id.cb_sound);
        final CheckBox lights = (CheckBox) findViewById(R.id.cb_lights);
        final CheckBox onGoing = (CheckBox) findViewById(R.id.cb_onGoing);
        final CheckBox reply = (CheckBox) findViewById(R.id.cb_reply);
        final CheckBox group = (CheckBox) findViewById(R.id.cb_group);

        headsUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFeatureState.headsUp = isChecked;
                mCallback.onNotificationFeatureChange(mFeatureState);
                if (isChecked && !(vibrate.isChecked() || sound.isChecked())) {
                    vibrate.setChecked(true);
                    sound.setChecked(true);
                }
            }
        });

        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFeatureState.vibrate = isChecked;
                mCallback.onNotificationFeatureChange(mFeatureState);
                if (!isChecked && !sound.isChecked()) {
                    headsUp.setChecked(false);
                }
            }
        });

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFeatureState.sound = isChecked;
                mCallback.onNotificationFeatureChange(mFeatureState);
                if (!isChecked && !vibrate.isChecked()) {
                    headsUp.setChecked(false);
                }
            }
        });

        lights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFeatureState.lights = isChecked;
                mCallback.onNotificationFeatureChange(mFeatureState);
            }
        });

        onGoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFeatureState.onGoing = isChecked;
                mCallback.onNotificationFeatureChange(mFeatureState);
            }
        });

        reply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFeatureState.reply = isChecked;
                mCallback.onNotificationFeatureChange(mFeatureState);
            }
        });

        group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFeatureState.group = isChecked;
                mCallback.onNotificationFeatureChange(mFeatureState);
            }
        });

        headsUp.setChecked(mFeatureState.headsUp);
        vibrate.setChecked(mFeatureState.vibrate);
        sound.setChecked(mFeatureState.sound);
        lights.setChecked(mFeatureState.lights);
        onGoing.setChecked(mFeatureState.onGoing);
        reply.setChecked(mFeatureState.reply);
        group.setChecked(mFeatureState.group);
    }


    public class FeatureState {
        public boolean headsUp = true;
        public boolean vibrate = true;
        public boolean sound = true;
        public boolean lights = false;
        public boolean onGoing = false;

        public boolean reply = false;
        public boolean group = false;
    }
}
