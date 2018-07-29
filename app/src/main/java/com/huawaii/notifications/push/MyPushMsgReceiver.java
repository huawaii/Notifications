package com.huawaii.notifications.push;

import android.content.Context;
import android.util.Log;

import com.huawaii.notifications.R;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2018/7/29.
 *
 * https://github.com/MEIZUPUSH/PushDemo
 */
public class MyPushMsgReceiver extends MzPushMessageReceiver {

    private final static String TAG = "MeizuPush";

    public static String sPushId = null;

    @Override
    public void onRegister(Context context, String s) {
        //调用PushManager.register(context）方法后，会在此回调注册状态
        //应用在接受返回的pushid
        Log.d(TAG, "onRegister -> s:" + s);
    }

    @Override
    public void onUnRegister(Context context, boolean b) {
        //调用PushManager.unRegister(context）方法后，会在此回调反注册状态
        Log.d(TAG, "onUnRegister -> b:" + b);
    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
        //检查通知栏和透传消息开关状态回调
        Log.d(TAG, "onPushStatus -> pushSwitchStatus:" + pushSwitchStatus.toString());
    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        //调用新版订阅PushManager.register(context,appId,appKey)回调
        sPushId = registerStatus.getPushId();
        Log.d(TAG, "onRegisterStatus -> registerStatus:" + registerStatus.toString());
    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
        //新版反订阅回调
        Log.d(TAG, "onUnRegisterStatus -> unRegisterStatus:" + unRegisterStatus.toString());
    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {
        //标签回调
        Log.d(TAG, "onSubTagsStatus -> subTagsStatus:" + subTagsStatus.toString());
    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {
        //别名回调
        Log.d(TAG, "onSubAliasStatus -> subAliasStatus:" + subAliasStatus.toString());
    }


    /**
     * 以下为选择性复写的方法
     */
    @Override
    public void onMessage(Context context, String s) {
        //接收服务器推送的透传消息
        Log.d(TAG, "onMessage -> s:" + s);
    }

    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        //重要，设置通知栏小图标，详情参考应用小图标自定设置
        Log.d(TAG, "onUpdateNotificationBuilder -> pushNotificationBuilder:" + pushNotificationBuilder.toString());
        pushNotificationBuilder.setmLargIcon(R.drawable.ic_linux);
        pushNotificationBuilder.setmStatusbarIcon(R.drawable.ic_linux);
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        //通知栏消息点击回调
        Log.d(TAG, "onNotificationClicked title " + mzPushMessage.getTitle() + "content "
                + mzPushMessage.getContent() + " selfDefineContentString " + mzPushMessage.getSelfDefineContentString() + " notifyId " + mzPushMessage.getNotifyId());
    }

}
