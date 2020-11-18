package com.huawaii.notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by lizongheng on 20-11-18.
 */
public class ReplyService extends Service {
    private static final String TAG = "ReplyService";

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "RelayService onCreate.23-> ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "RelayService onStartCommand.28-> ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "RelayService onDestroy.34-> ");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "RelayService onUnbind.40-> ");
        return super.onUnbind(intent);
    }
}
