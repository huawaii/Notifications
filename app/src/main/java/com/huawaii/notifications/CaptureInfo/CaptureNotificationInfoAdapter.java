/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/25.
 */
package com.huawaii.notifications.CaptureInfo;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawaii.notifications.R;

import java.util.List;

public class CaptureNotificationInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemBean> mDataList;
    private LayoutInflater mLayoutInflater;
    private AdapterCallback mCallback;

    public CaptureNotificationInfoAdapter(Context context, List<ItemBean> dataList) {
        mContext = context;
        mDataList = dataList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setCallback(AdapterCallback callback) {
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemBean info = mDataList.get(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.activity_capture_notification_info_item, null);
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mCallback != null) {
                        mCallback.onItemLongClick(v);
                    }
                    return false;
                }
            });
            holder.infoTV = (TextView) convertView.findViewById(R.id.info_item_textView);
            holder.sbn = info.sbn;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.infoTV.setText(info.info);
        return convertView;
    }

    public interface AdapterCallback {
        void onItemLongClick(View v);
    }

    public static class ViewHolder {
        TextView infoTV;
        StatusBarNotification sbn;
    }

    public static class ItemBean {
        String info;
        StatusBarNotification sbn;

        public ItemBean(String info, StatusBarNotification sbn) {
            this.info = info;
            this.sbn = sbn;
        }
    }
}
