/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 16-8-11.
 */
package com.huawaii.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.huawaii.notifications.utils.NotificationChannelUtils;
import com.meizu.flyme.reflect.NotificationProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import static com.huawaii.notifications.Settings.SettingsGroup1.BIG_PICTURE_STYLE;
import static com.huawaii.notifications.Settings.SettingsGroup1.BIG_TEXT_STYLE;
import static com.huawaii.notifications.Settings.SettingsGroup1.FOREGROUND_SERVICE_STYLE;
import static com.huawaii.notifications.Settings.SettingsGroup1.INBOX_STYLE;
import static com.huawaii.notifications.Settings.SettingsGroup1.MESSAGING_STYLE;
import static com.huawaii.notifications.Settings.SettingsGroup1.NORMAL_STYLE;
import static com.huawaii.notifications.Settings.SettingsGroup1.PROGRESSBAR_CIRCLE_STYLE;


/** Created by lzh on 16-8-11.**/
public class CreateNotification {

    private static final String TAG = "huawaii";

    public static final String KEY_TEXT_REPLY = "key_text_reply";
    public static final String FILTER_INTENT_REPLY = "filter_intent_reply";
    public static final String FILTER_INTENT_ACTION = "intent.action.huawaii";
    private static NotificationManager mNotificationManager;

    private Context mContext;
    private PendingIntent mResultPendingIntent1;
    private PendingIntent mResultPendingIntent2;
    private ReplyBroadcastReceiver mReplyReceiver;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (FILTER_INTENT_ACTION.equals(intent.getAction())) {
                Log.d(TAG, "intent.action.huawaii cancelAllNotification");
                cancelAllNotification();
            }
        }
    };

    private int mNotificationPriority = Notification.PRIORITY_DEFAULT;

    /*Settings_Group1*/
    private int mButtonNum = 0;
    private boolean isSmallCustom = false;
    private boolean isBigCustom = false;
    private boolean isHeadsUpCustom = false;

    /*Settings_Group2*/
    private boolean isVibrate = false;
    private boolean isSound = false;
    private boolean isLights = false;
    private boolean isOnGoing = false;
    private boolean isReply = false;
    private boolean isGroup = false;
    private boolean isGroupSummary = false;
    private int mGroupNum = 0;

    /*Settings_tips*/
    private Timer mTimer;
    private int mSendNumber;
    private int mSendPeriod;
    private int mNumberTemp;
    private boolean isSending = false;

    private String mContentTitle = "通知标题";
    private String mContentText = "这里是写通知内容";
    private String mContentTicker = "通知Ticker";

    /*NotificationChannel*/
    private NotificationChannelUtils.ChannelParamRecord mChannelParam = NotificationChannelUtils.getChannelParamInstance();


    public CreateNotification(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mResultPendingIntent1 = getResultPendingIntent1();
        mResultPendingIntent2 = getResultPendingIntent3();

        mReplyReceiver = new ReplyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(FILTER_INTENT_REPLY);
        mContext.registerReceiver(mReplyReceiver, intentFilter);

        mContext.registerReceiver(mReceiver, new IntentFilter(FILTER_INTENT_ACTION));

        if (Build.VERSION.SDK_INT >= 26) {
            checkNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationChannelUtils.ChannelParamRecord channelParam) {
        Resources resources = mContext.getResources();
        CharSequence name = resources.getString(R.string.notification_channel_name);
        String description = resources.getString(R.string.notification_channel_description);

        // 传入参数：通道ID，通道名字，通道优先级（类似曾经的 builder.setPriority()）
        NotificationChannel channel = new NotificationChannel(channelParam.toString(), name, channelParam.importance);
        // 配置通知渠道的属性
        channel.setDescription(description);
        // 设置通知出现时声音，默认通知是有声音的
        if (!channelParam.isBeep) {
            channel.setSound(null, null);
        }

        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        channel.enableLights(channelParam.isBlink);

        // 设置通知出现时的震动（如果 android 设备支持的话）
        channel.enableVibration(channelParam.isBuzz);

        //最后在 notificationManager 中创建该通知渠道
        mNotificationManager.createNotificationChannel(channel);
        Log.d(TAG, "createNotificationChannel -> channelParamId " + channelParam.toString());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void checkNotificationChannel() {
        NotificationChannel channel = mNotificationManager.getNotificationChannel(mChannelParam.toString());
        Log.d(TAG, "CreateNotification checkNotificationChannel.146-> "+mChannelParam.toString()+"  "+(channel == null));
        if (channel == null) {
            createNotificationChannel(mChannelParam);
        }
    }

    private PendingIntent getResultPendingIntent1() {
        Intent intent = new Intent(mContext, ResultActivity.class);
        return PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getResultPendingIntent2() {
        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(mContext, ResultActivity.class);
        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getResultPendingIntent3() {
        Intent intent = new Intent();
        intent.setAction(FILTER_INTENT_ACTION);
        return PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void setButtonNum(int buttonNum) {
        mButtonNum = buttonNum;
    }

    public void setSmallCustom(boolean smallCustom) {
        isSmallCustom = smallCustom;
    }

    public void setBigCustom(boolean bigCustom) {
        isBigCustom = bigCustom;
    }

    public void setHeadsUpCustom(boolean headsUpCustom) {
        isHeadsUpCustom = headsUpCustom;
    }


    public void setHeadsUp(boolean headsUp) {
        mNotificationPriority = headsUp ? Notification.PRIORITY_HIGH : Notification.PRIORITY_DEFAULT;
        if (Build.VERSION.SDK_INT >= 26) {
            mChannelParam.importance = headsUp ? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_DEFAULT;
            checkNotificationChannel();
        }
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
        if (Build.VERSION.SDK_INT >= 26) {
            mChannelParam.isBuzz = vibrate;
            checkNotificationChannel();
        }
    }

    public void setSound(boolean sound) {
        isSound = sound;
        if (Build.VERSION.SDK_INT >= 26) {
            mChannelParam.isBeep = sound;
            checkNotificationChannel();
        }
    }

    public void setLights(boolean lights) {
        isLights = lights;
        if (Build.VERSION.SDK_INT >= 26) {
            mChannelParam.isBlink = lights;
            checkNotificationChannel();
        }
    }

    public void setOnGoing(boolean going) {
        isOnGoing = going;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public void setGroup(boolean group) {
        if (isGroup != group) {
            if (group) mGroupNum++;
            isGroupSummary = true;
            isGroup = group;
        }
    }

    public void setSendParameters(int number, int period) {
        mSendNumber = number;
        mSendPeriod = period;
    }


    private void setGroup(Builder builder) {
        if (isGroup) {
            if (isGroupSummary) {
                isGroupSummary = false;
                builder.setContentTitle(mGroupNum + "组 主通知标题");
                builder.setGroupSummary(true);
            } else {
                builder.setContentTitle(mGroupNum + "组 子通知标题");
            }
            builder.setGroup(String.valueOf(mGroupNum));
        }
    }

    private void setActionReply(Builder builder) {
        if (isReply) {
            // 远程的输入控件构造一个
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("回复点什么呢？").build();
            // Create the reply action and add the remote input.
            Intent intent = new Intent(FILTER_INTENT_REPLY);
            PendingIntent replyPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent
                    .FLAG_ONE_SHOT);
            Notification.Action action = new Notification.Action.Builder(R.drawable.ic_launcher, "点击这里开始回复",
                    replyPendingIntent).addRemoteInput(remoteInput).build();
            builder.addAction(action);
        }
    }

    private void setActionButton(Builder builder) {
        if (mButtonNum != 0) {
            for (int i = 0; i < mButtonNum; i++) {
                builder.addAction(R.drawable.ic_launcher, "已读", mResultPendingIntent2);
            }
        }
    }

    private void setDefaults(Builder builder) {
        int defaults = 0;
        if (isVibrate) defaults |= Notification.DEFAULT_VIBRATE;
        if (isSound) defaults |= Notification.DEFAULT_SOUND;
        if (isLights) defaults |= Notification.DEFAULT_LIGHTS;
        builder.setDefaults(defaults);
    }

    private void setNotificationFlag(Notification notification) {
        if (isOnGoing) notification.flags |= Notification.FLAG_ONGOING_EVENT;
    }


    private Notification createNormalNotification() {
        Bitmap picture = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.panda);

        Builder builder = (Build.VERSION.SDK_INT >= 26 ? new Builder(mContext, mChannelParam.toString()) : new Builder(mContext))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(mContentTitle + "Normal Notification") // 显示于 line1
                .setContentText(mContentText + "，普通小通知.，普通小通知.，普通小通知.，普通小通知.，普通小通知.，普通小通知.")   // 显示于 line3
                .setLargeIcon(picture)
                .setContentIntent(mResultPendingIntent1)
                .setAutoCancel(true)
                .setTicker("普通小通知！")
                .setPriority(mNotificationPriority);

        setActionButton(builder);
        setActionReply(builder);
        setGroup(builder);
        setDefaults(builder);
        Notification notification = builder.build();
        setNotificationFlag(notification);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_custom_remote);
        //实例化一个远程remote view
        remoteViews.setTextViewText(R.id.text_view, "自定义样式！");
        remoteViews.setOnClickPendingIntent(R.id.image_view, mResultPendingIntent1);
        if (isSmallCustom) {
            notification.contentView = remoteViews;
        }
        if (isBigCustom) {
            notification.bigContentView = remoteViews;  //通过.setContent(remoteViews)不能设置bigContentView
        }
        if (isHeadsUpCustom) {
            notification.headsUpContentView = remoteViews;  //for HeadsUp Notification
        }
        return notification;
    }


    private Notification createProgressBarCircleNotification() {
        Bitmap picture = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.panda);

        Builder builder = (Build.VERSION.SDK_INT >= 26 ? new Builder(mContext, mChannelParam.toString()) : new Builder(mContext))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(mContentTitle + "Circle ProgressBar " + "Notification")
                .setContentText(mContentText + "，Flyme 定制进度条.，Flyme 定制进度条.，Flyme 定制进度条.，Flyme 定制进度条.")
                .setProgress(0, 0, true)
                .setLargeIcon(picture)
                .setContentIntent(mResultPendingIntent1)
                .setAutoCancel(true)
                .setTicker("Flyme 定制进度条！")
                .setShowWhen(false)
                .setPriority(mNotificationPriority);

        NotificationProxy.setProgressBarStype(builder, true);   //设置ProgressBar为圆环形

        setActionButton(builder);
        setActionReply(builder);
        setGroup(builder);
        setDefaults(builder);
        Notification notification = builder.build();
        setNotificationFlag(notification);
        return notification;
    }


    private Notification createBigTextNotification() {
        Bitmap picture = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.panda);
        CharSequence bigText = "最新最全的Flyme固件更新；Flyme软件商店提供海量应用供用户下载；" +
                "Flyme云服务提供用户帐号与数据管理服务；Flyme论坛为用户提供沟通交流平台；品牌窗口展示最新的Flyme资讯。";

        // Create the style object with BigTextStyle subclass.
        Notification.BigTextStyle notiStyle = new Notification.BigTextStyle();
        notiStyle.setBigContentTitle(mContentTitle + "Big Text Expanded");  // 显示于 line1
        notiStyle.setSummaryText(mContentText + "Nice big text."); // 显示于 line3
        notiStyle.bigText(bigText);     // Add the big text to the style.
        try {
            Class<? extends Notification.BigTextStyle> styleClass = notiStyle.getClass();
            Method setHeadsUpMaxLines = styleClass.getMethod("setHeadsUpMaxLines", int.class);
            setHeadsUpMaxLines.invoke(notiStyle, 3);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, "CreateNotification createBigTextNotification-> \n" + Log.getStackTraceString(e));
        }

        Builder builder = (Build.VERSION.SDK_INT >= 26 ? new Builder(mContext, mChannelParam.toString()) : new Builder(mContext))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(mContentTitle + "Big Text Notification")   // 显示于 line1
                .setContentText(mContentText + "，大通知为系统大文本样式.，大通知为系统大文本样式.，大通知为系统大文本样式.") // 显示于 line3
                .setLargeIcon(picture)
                .setContentIntent(mResultPendingIntent1)
                .setAutoCancel(true)
                .setTicker("大通知为系统大文本样式！")
                .setPriority(mNotificationPriority)
                .setStyle(notiStyle);

        setActionButton(builder);
        setActionReply(builder);
        setGroup(builder);
        setDefaults(builder);
        Notification notification = builder.build();
        setNotificationFlag(notification);
        return notification;
    }


    private Notification createBigPictureNotification() {
        Bitmap picture = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.panda);

        // Create the style object with BigPictureStyle subclass.
        Notification.BigPictureStyle notiStyle = new Notification.BigPictureStyle();
        notiStyle.setBigContentTitle(mContentTitle + "Big Picture Expanded");
        notiStyle.setSummaryText(mContentText + "Nice big picture.");
        notiStyle.bigPicture(picture);  // Add the big picture to the style.

        Builder builder = (Build.VERSION.SDK_INT >= 26 ? new Builder(mContext, mChannelParam.toString()) : new Builder(mContext))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(mContentTitle + "Big Picture Notification")
                .setContentText(mContentText + "，大通知为系统大图样式.，大通知为系统大图样式.，大通知为系统大图样式.，大通知为系统大图样式.")
                .setLargeIcon(picture)
                .setContentIntent(mResultPendingIntent1)
                .setAutoCancel(true)
                .setTicker("大通知为系统大图样式！")
                .setPriority(mNotificationPriority)
                .setStyle(notiStyle);

        setActionButton(builder);
        setActionReply(builder);
        setGroup(builder);
        setDefaults(builder);
        Notification notification = builder.build();
        setNotificationFlag(notification);
        return notification;
    }


    private Notification createInboxNotification() {
        Bitmap picture = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.panda);

        // Create the style object with InboxStyle subclass.
        Notification.InboxStyle notiStyle = new Notification.InboxStyle();
        notiStyle.setBigContentTitle(mContentTitle + "Inbox Style Expanded");

        // Add the multiple lines to the style. This is strictly for providing an example of multiple lines.
        for (int i = 0; i < 5; i++) {
            notiStyle.addLine("(" + i + " of 6) Line one here.");
        }
        notiStyle.setSummaryText(mContentText + "Flyme: +2 more Line Samples");

        Builder builder = (Build.VERSION.SDK_INT >= 26 ? new Builder(mContext, mChannelParam.toString()) : new Builder(mContext))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(mContentTitle + "Inbox Style Notification")
                .setContentText(mContentText + "，大通知为系统邮件样式.，大通知为系统邮件样式.，大通知为系统邮件样式.，大通知为系统邮件样式.")
                .setLargeIcon(picture)
                .setContentIntent(mResultPendingIntent1)
                .setAutoCancel(true)
                .setTicker("大通知为系统邮件样式！")
                .setPriority(mNotificationPriority)
                .setStyle(notiStyle);

        setActionButton(builder);
        setActionReply(builder);
        setGroup(builder);
        setDefaults(builder);
        Notification notification = builder.build();
        setNotificationFlag(notification);
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Notification createMessagingNotification() {
        Bitmap picture = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.panda);

        // Create the style object with MessagingStyle subclass.
        Notification.MessagingStyle notiStyle = new Notification.MessagingStyle("大师");
        notiStyle.setConversationTitle("来自老王");
        notiStyle.addMessage("大师，昨夜我做梦瘦了18斤。", System.currentTimeMillis(), "老王");
        notiStyle.addMessage("梦和现实是反的。", System.currentTimeMillis(), null);
        notiStyle.addMessage("难道我会胖18斤？", System.currentTimeMillis(), "老王");
        notiStyle.addMessage("不，你会胖81斤。", System.currentTimeMillis(), null);
        notiStyle.addMessage("... ...", System.currentTimeMillis(), "老王");

        Builder builder = (Build.VERSION.SDK_INT >= 26 ? new Builder(mContext, mChannelParam.toString()) : new Builder(mContext))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(mContentTitle + "Inbox Style Notification")
                .setContentText(mContentText + "，大通知为系统邮件样式.")
                .setLargeIcon(picture)
                .setContentIntent(mResultPendingIntent1)
                .setAutoCancel(true)
                .setTicker("大通知为系统消息样式！")
                .setPriority(mNotificationPriority)
                .setStyle(notiStyle);

        setActionButton(builder);
        setActionReply(builder);
        setGroup(builder);
        setDefaults(builder);
        Notification notification = builder.build();
        setNotificationFlag(notification);
        return notification;
    }

    private Notification createForegroundService() {
        mContext.startService(new Intent(mContext, ForegroundService.class));
        return null;
    }

    public void cancelAllNotification() {
        mContext.stopService(new Intent(mContext, ForegroundService.class));
        mNotificationManager.cancelAll();
    }

    public void sendNotificationTimer(final int style) {
        if (isSending) {
            Toast.makeText(mContext, "正在发送中...", Toast.LENGTH_SHORT).show();
            return;
        }
        mNumberTemp = mSendNumber;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isSending = true;
                sendNotification(style);
                mNumberTemp--;
                if (mNumberTemp == 0) {
                    isSending = false;
                    mTimer.cancel();
                    mTimer = null;
                }
            }
        }, 0, mSendPeriod);
    }

    private void sendNotification(int style) {
        Notification noti = null;

        switch (style) {
            case NORMAL_STYLE:
                noti = createNormalNotification();
                break;

            case BIG_TEXT_STYLE:
                noti = createBigTextNotification();
                break;

            case BIG_PICTURE_STYLE:
                noti = createBigPictureNotification();
                break;

            case INBOX_STYLE:
                noti = createInboxNotification();
                break;

            case PROGRESSBAR_CIRCLE_STYLE:
                noti = createProgressBarCircleNotification();
                break;

            case MESSAGING_STYLE:
                noti = createMessagingNotification();
                break;

            case FOREGROUND_SERVICE_STYLE:
                noti = createForegroundService();
                break;
        }

        if (noti != null) {
            int id = (int) (Math.random() * 1000);
            mNotificationManager.notify(id, noti);
        }
    }
}