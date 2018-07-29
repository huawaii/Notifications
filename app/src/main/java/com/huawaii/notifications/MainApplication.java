package com.huawaii.notifications;

import android.app.Application;

import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;

/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2018/7/29.
 */
public class MainApplication extends Application {

    public final static String APP_ID = "114880";
    public final static String APP_KEY = "2b88bd91e50e4467a3b5216635982d24";

    @Override
    public void onCreate() {
        super.onCreate();
        if(MzSystemUtils.isBrandMeizu(this)){
            PushManager.register(this, APP_ID, APP_KEY);
        }
    }
}
