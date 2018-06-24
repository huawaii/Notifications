/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/25.
 */
package com.huawaii.notifications.CaptureInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.huawaii.notifications.CaptureInfo.CaptureNotificationInfoAdapter.ItemBean;
import com.huawaii.notifications.CaptureInfo.CaptureNotificationInfoAdapter.ViewHolder;
import com.huawaii.notifications.CaptureInfo.InfoPopupWindowAdapter.InfoBean;
import com.huawaii.notifications.R;
import com.huawaii.notifications.utils.PopupWindowUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Notification.EXTRA_BIG_TEXT;
import static android.app.Notification.EXTRA_LARGE_ICON;
import static android.app.Notification.EXTRA_SMALL_ICON;
import static android.app.Notification.EXTRA_TEXT;
import static android.app.Notification.EXTRA_TITLE;


public class CaptureNotificationInfoActivity extends Activity implements CaptureNotificationInfoAdapter
        .AdapterCallback {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private ListView mListView;
    private int mUpdateTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_notification_info);
        mListView = (ListView) findViewById(R.id.info_listView);

        captureNotificationInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh_notification_info) {
            captureNotificationInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void captureNotificationInfo() {
        if (!isEnabled()) {
            showConfirmDialog();
        } else {
            if (CaptureNotificationInfoService.sService != null) {
                StatusBarNotification sbns[] = CaptureNotificationInfoService.sService.getActiveNotifications();
                if (sbns != null) {
                    List<ItemBean> dataList = new ArrayList<>();
                    for (StatusBarNotification sbn : sbns) {
                        dataList.add(new ItemBean(sbn.toString(), sbn.clone()));
                    }
                    CaptureNotificationInfoAdapter adapter = new CaptureNotificationInfoAdapter(this, dataList);
                    adapter.setCallback(this);
                    mListView.setAdapter(adapter);
                    Toast.makeText(this, "Success，数据已更新", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "获取通知信息为null，请重启应用再试...", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "服务已被杀，请重新授权再试...", Toast.LENGTH_SHORT).show();
                mUpdateTimes++;
                if (mUpdateTimes >= 3) {
                    openNotificationAccess();
                    mUpdateTimes = 0;
                }
            }
        }
    }


    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String s : names) {
                final ComponentName cn = ComponentName.unflattenFromString(s);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this).setMessage("请打开“一键获取通知信息”的通知读取权限，然后再次点击“刷新”").setTitle("一键获取通知信息")
                .setIconAttribute(android.R.attr.alertDialogIcon).setCancelable(true).setPositiveButton(android.R
                .string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                openNotificationAccess();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
            }
        }).create().show();
    }

    private void openNotificationAccess() {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View v) {
        View popupView = View.inflate(this, R.layout.info_popupwindow, null);
        ListView listView = (ListView) popupView.findViewById(R.id.info_popupWindow_listView);

        listView.setAdapter(new InfoPopupWindowAdapter(this, getDataList(v)));
        new PopupWindowUtils(this).startPopupWindow(popupView);
    }

    private List<InfoBean> getDataList(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        StatusBarNotification sbn = holder.sbn;
        Notification n = sbn.getNotification();

        List<InfoBean> dataList = new ArrayList<>();
        dataList.add(new InfoBean("sbn.getPackageName", "" + sbn.getPackageName()));
        dataList.add(new InfoBean("sbn.getPostTime", "" + DateFormat.format("yyyy-MM-dd HH:mm:ss", sbn.getPostTime())));
        dataList.add(new InfoBean("tickerText", "" + n.tickerText));
        dataList.add(new InfoBean("title", n.extras.getString(EXTRA_TITLE, "")));
        dataList.add(new InfoBean("text", n.extras.getString(EXTRA_TEXT, "")));
        dataList.add(new InfoBean("bigText", n.extras.getString(EXTRA_BIG_TEXT, "")));
        dataList.add(new InfoBean("priority", "" + n.priority));
        dataList.add(new InfoBean("flags", "" + n.flags));
        dataList.add(new InfoBean("icon", "" + n.extras.getParcelable(EXTRA_SMALL_ICON)));
        dataList.add(new InfoBean("largeIcon", "" + n.extras.getParcelable(EXTRA_LARGE_ICON)));
        return dataList;
    }
}
