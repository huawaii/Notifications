/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/25.
 */
package com.huawaii.notifications.CaptureInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawaii.notifications.R;

import java.util.List;

public class InfoPopupWindowAdapter extends BaseAdapter {

    private Context mContext;
    private List<InfoBean> mDataList;
    private LayoutInflater mLayoutInflater;

    public InfoPopupWindowAdapter(Context context, List<InfoBean> dataList) {
        mContext = context;
        mDataList = dataList;
        mLayoutInflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.info_popupwindow_item, null);
            holder.keyTV = (TextView) convertView.findViewById(R.id.info_popupWindow_item_key);
            holder.valueTV = (TextView) convertView.findViewById(R.id.info_popupWindow_item_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InfoBean info = mDataList.get(position);
        holder.keyTV.setText(info.key);
        holder.valueTV.setText(info.value);
        return convertView;
    }

    public static class InfoBean {
        String key;
        String value;

        public InfoBean(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private class ViewHolder {
        TextView keyTV;
        TextView valueTV;
    }
}
