/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/3/5.
 */
package com.huawaii.notifications;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import static com.huawaii.notifications.CreateNotification.FILTER_INTENT_REPLY;
import static com.huawaii.notifications.CreateNotification.KEY_TEXT_REPLY;

public class ReplyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (FILTER_INTENT_REPLY.equals(intent.getAction())) {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null) {
                Toast.makeText(context, remoteInput.getCharSequence(KEY_TEXT_REPLY), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
