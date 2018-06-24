/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/8.
 */
package com.huawaii.notifications.Settings;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.huawaii.notifications.R;

import java.util.Arrays;
import java.util.Locale;


public class SettingsTips extends LinearLayout implements View.OnClickListener {

    private final static String TAG = "SettingsTips";
    private final static String[] SEND_PERIODS = {"50", "100", "200", "500", "1000", "2000"};

    private Context mContext;
    private SettingsCallback mCallback;

    private int mNumberDefault = 1;
    private int mPeriodIndexDefault = 1;
    private EditText mEditTextNum;
    private EditText mEditTextPeriod;

    public SettingsTips(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
    }

    public void setCallback(SettingsCallback callback) {
        mCallback = callback;
        initView();
        setResult(mNumberDefault, mPeriodIndexDefault);
    }

    private void initData() {
        mNumberDefault = 1;
        mPeriodIndexDefault = 1;
    }

    private void initView() {
        setOnClickListener(this);

        mEditTextNum = (EditText) findViewById(R.id.editText_num);
        mEditTextPeriod = (EditText) findViewById(R.id.editText_period);
    }

    private void setResult(int number, int periodIndex) {
        int period = Integer.parseInt(SEND_PERIODS[periodIndex]);
        mEditTextNum.setText(String.format(Locale.getDefault(), "%d", number));
        mEditTextPeriod.setText(String.format(Locale.getDefault(), "%d", period));
        mCallback.onNotificationTipsChange(number, period);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public void onClick(View v) {
        View popupView = View.inflate(mContext, R.layout.settings_tips_popupwindow, null);
        initNumberPicker(popupView);
        mCallback.onSettingsTipsClick(popupView);
    }


    private void initNumberPicker(View v) {
        NumberPickerConfig numConfig = new NumberPickerConfig();
        NumberPicker numPicker = (NumberPicker) v.findViewById(R.id.np_num_picker);
        numPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numPicker.setFormatter(numConfig);
        numPicker.setOnValueChangedListener(numConfig);
        numPicker.setOnScrollListener(numConfig);
        numPicker.setMaxValue(30);
        numPicker.setMinValue(1);
        numPicker.setValue(mNumberDefault);

        NumberPickerConfig periodConfig = new NumberPickerConfig();
        NumberPicker periodPicker = (NumberPicker) v.findViewById(R.id.np_period_picker);
        periodPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        periodPicker.setFormatter(periodConfig);
        periodPicker.setOnValueChangedListener(periodConfig);
        periodPicker.setOnScrollListener(periodConfig);
        periodPicker.setDisplayedValues(SEND_PERIODS);
        periodPicker.setMaxValue(SEND_PERIODS.length - 1);
        periodPicker.setMinValue(0);
        periodPicker.setValue(mPeriodIndexDefault);
    }

    class NumberPickerConfig implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener,
            NumberPicker.Formatter {

        @Override
        public String format(int value) {
            String tmpStr = String.valueOf(value);
            if (value < 10) {
                tmpStr = "0" + tmpStr;
            }
            return tmpStr;
        }

        @Override
        public void onScrollStateChange(NumberPicker view, int scrollState) {
            switch (scrollState) {
                case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                    //Toast.makeText(this, "后续滑动", Toast.LENGTH_SHORT).show();
                    break;
                case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                    //Toast.makeText(this, "不滑动", Toast.LENGTH_SHORT).show();
                    break;
                case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    //Toast.makeText(this, "滑动中", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            String[] strings = picker.getDisplayedValues();

            if (strings != null && Arrays.equals(strings, SEND_PERIODS)) {
                mPeriodIndexDefault = newVal;
            } else {
                mNumberDefault = newVal;
            }
            setResult(mNumberDefault, mPeriodIndexDefault);
            //Toast.makeText( this, "原来的值 " + oldVal + "--新值: " + newVal, Toast.LENGTH_SHORT)
            // .show();
        }
    }
}
