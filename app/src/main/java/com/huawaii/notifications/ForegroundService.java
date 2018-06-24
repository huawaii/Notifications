package com.huawaii.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.huawaii.notifications.utils.NotificationChannelUtils;


public class ForegroundService extends Service {

    private static int NOTIFICATION_ID = 1;
    private NotificationChannelUtils.ChannelParamRecord mChannelParam = NotificationChannelUtils.getChannelParamInstance();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*
         * 我们并不需要为 notification.flags 设置 FLAG_ONGOING_EVENT，因为
         * 前台服务的 notification.flags 总是默认包含了那个标志位
         */
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = (Build.VERSION.SDK_INT >= 26 ?
                new Notification.Builder(this, mChannelParam.toString()) : new Notification.Builder(this))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("前台服务标题")
                .setContentText("前台服务内容")
                .setContentIntent(pendingIntent)
                .build();

        /*
         * 注意使用 startForeground ，id 为 0 将不会显示 notification
         */
        startForegroundCompat(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForegroundCompat(NOTIFICATION_ID);
    }

    /**
     * 以兼容性方式开始前台服务
     */
    private void startForegroundCompat(int id, Notification n) {
        startForeground(id, n);
        //mNM.notify(id, n);
    }

    /**
     * 以兼容性方式停止前台服务
     */
    private void stopForegroundCompat(int id) {
        /*
         *  在 stopForeground 之前调用 cancel，因为我们有可能在取消前台服务之后
         *  的那一瞬间被kill掉。这个时候 notification 便永远不会从通知一栏移除
         */
        //mNM.cancel(id);
        stopForeground(true);
    }
}
