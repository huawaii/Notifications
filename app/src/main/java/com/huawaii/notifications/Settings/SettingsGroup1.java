/**
 * Copyright (C) 2017 huawaii. All rights reserved.
 *
 * @author huawaii on 2017/2/8.
 */
package com.huawaii.notifications.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawaii.notifications.R;


public class SettingsGroup1 extends RelativeLayout implements View.OnClickListener {

    public final static int NORMAL_STYLE = 0;
    public final static int BIG_TEXT_STYLE = 1;
    public final static int BIG_PICTURE_STYLE = 2;
    public final static int INBOX_STYLE = 3;
    public final static int PROGRESSBAR_CIRCLE_STYLE = 4;
    public final static int MESSAGING_STYLE = 5;
    public final static int FOREGROUND_SERVICE_STYLE = 6;
    private final static String TAG = "SettingsGroup1";
    private final static String ITEM_PREFIX = "自定义";
    private final static String[] mCustomTemplateStyleTypeItems = new String[]{ITEM_PREFIX + "小通知", ITEM_PREFIX +
            "大通知", ITEM_PREFIX + "浮动通知"};
    private final ArrayMap<Integer, String> mAllTemplateStyleItems = new ArrayMap<>();
    private final CustomState mCustomState = new CustomState(false, false, false);  //多选
    private final String[] mTemplateButtonNumberItems = new String[]{"0个", "1个", "2个", "3个"};
    private Context mContext;
    private SettingsCallback mCallback;
    /* 模板类型变量 */
    private TextView mAllTemplateStyleView;
    private int mCurrentTemplateStyleChecked;  //单选
    /* 普通模板自定义变量 */
    private TextView mCustomTemplateStyleTypeView;
    private String mCustomTemplateStyleTypeDefault;
    /* 通知按钮变量 */
    private TextView mTemplateButtonNumberView;
    private int mCurrentButtonNumberChecked;  //单选

    public SettingsGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
    }

    public void setCallback(SettingsCallback callback) {
        mCallback = callback;
        setResult(mAllTemplateStyleItems.keyAt(mCurrentTemplateStyleChecked), mCustomState,
                mCurrentButtonNumberChecked);
        initView();
    }

    private void initData() {
        mAllTemplateStyleItems.put(NORMAL_STYLE, "普通模板");
        mAllTemplateStyleItems.put(BIG_TEXT_STYLE, "文本模板");
        mAllTemplateStyleItems.put(BIG_PICTURE_STYLE, "大图模板");
        mAllTemplateStyleItems.put(INBOX_STYLE, "邮件模板");
        mAllTemplateStyleItems.put(PROGRESSBAR_CIRCLE_STYLE, "下载模板");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mAllTemplateStyleItems.put(MESSAGING_STYLE, "消息模板");
        }
        mAllTemplateStyleItems.put(FOREGROUND_SERVICE_STYLE, "前台服务");

        mCustomTemplateStyleTypeDefault = getResources().getString(R.string.custom_template_style_type_default);
    }

    private void initView() {
        mAllTemplateStyleView = (TextView) findViewById(R.id.all_template_style);
        mAllTemplateStyleView.setOnClickListener(this);

        mCustomTemplateStyleTypeView = (TextView) findViewById(R.id.custom_template_style_type);
        mCustomTemplateStyleTypeView.setOnClickListener(this);

        mTemplateButtonNumberView = (TextView) findViewById(R.id.template_button_number);
        mTemplateButtonNumberView.setOnClickListener(this);
        mTemplateButtonNumberView.setText(mContext.getString(R.string.template_button_number_default, 0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_template_style:
                onClickAllTemplateStyleView(v);
                break;
            case R.id.custom_template_style_type:
                onClickCustomTemplateStyleTypeView(v);
                break;
            case R.id.template_button_number:
                onClickTemplateButtonNumberView();
                break;
            default:
                Log.d(TAG, "error, no match view id onClick !");
                break;
        }
    }

    private void setTemplateTypeResult(int templateType) {
        setResult(templateType, mCustomState, mCurrentButtonNumberChecked);
    }

    private void setCustomStateResult(boolean[] customState) {
        mCustomState.smallCustom = customState[0];
        mCustomState.bigCustom = customState[1];
        mCustomState.headsUpCustom = customState[2];
        setResult(mAllTemplateStyleItems.keyAt(mCurrentTemplateStyleChecked), mCustomState,
                mCurrentButtonNumberChecked);
    }

    private void setButtonNumberResult(int currentButtonNumberChecked) {
        setResult(mAllTemplateStyleItems.keyAt(mCurrentTemplateStyleChecked), mCustomState, currentButtonNumberChecked);
    }

    private void setResult(int templateType, CustomState customState, int buttonNum) {
        mCallback.onNotificationTemplateChange(templateType, customState, buttonNum);
    }

    private void setCustomTemplateStyleTypeViewVisibility(boolean visible) {
        mCustomTemplateStyleTypeView.setTextColor(visible ? Color.BLACK : Color.GRAY);
        mCustomTemplateStyleTypeView.setEnabled(visible);
    }

    private void onClickAllTemplateStyleView(View view) {
        String[] styleItems = new String[mAllTemplateStyleItems.keySet().size()];
        styleItems = mAllTemplateStyleItems.values().toArray(styleItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请选择通知模板类型");
        builder.setSingleChoiceItems(styleItems, mCurrentTemplateStyleChecked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mCurrentTemplateStyleChecked = i;
                mAllTemplateStyleView.setText(mAllTemplateStyleItems.valueAt(mCurrentTemplateStyleChecked));
                setTemplateTypeResult(mAllTemplateStyleItems.keyAt(mCurrentTemplateStyleChecked));
                setCustomTemplateStyleTypeViewVisibility(mCurrentTemplateStyleChecked == NORMAL_STYLE);
                dialogInterface.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                mAllTemplateStyleView.setText(mAllTemplateStyleItems.valueAt(mCurrentTemplateStyleChecked));
                setTemplateTypeResult(mAllTemplateStyleItems.keyAt(mCurrentTemplateStyleChecked));
                setCustomTemplateStyleTypeViewVisibility(mCurrentTemplateStyleChecked == NORMAL_STYLE);
            }
        });
        builder.show();
    }

    private void onClickCustomTemplateStyleTypeView(View view) {
        final boolean[] typeChecked = new boolean[]{mCustomState.smallCustom, mCustomState.bigCustom, mCustomState
                .headsUpCustom};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请选择通知自定义类别");
        builder.setMultiChoiceItems(mCustomTemplateStyleTypeItems, typeChecked, new DialogInterface
                .OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                /**
                 * dialog:触发这个方法的对话框
                 * which:用户所选的条目的下标
                 * isChecked:用户是选中该条目还是取消该条目
                 */
                typeChecked[which] = isChecked;
                setCustomStateResult(typeChecked);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                String text = "";
                int length = Math.min(mCustomTemplateStyleTypeItems.length, typeChecked.length);
                int temp = 0;
                for (int i = 0; i < length; i++) {
                    if (typeChecked[i]) {
                        if (temp == 0) {
                            text += mCustomTemplateStyleTypeItems[i];
                        } else {
                            text += "-" + mCustomTemplateStyleTypeItems[i].replace(ITEM_PREFIX, "");
                        }
                        temp++;
                    }
                }
                mCustomTemplateStyleTypeView.setText("".equals(text) ? mCustomTemplateStyleTypeDefault : text);
                setCustomStateResult(typeChecked);
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = "";
                int length = Math.min(mCustomTemplateStyleTypeItems.length, typeChecked.length);
                int temp = 0;
                for (int i = 0; i < length; i++) {
                    if (typeChecked[i]) {
                        if (temp == 0) {
                            text += mCustomTemplateStyleTypeItems[i];
                        } else {
                            text += "-" + mCustomTemplateStyleTypeItems[i].replace(ITEM_PREFIX, "");
                        }
                        temp++;
                    }
                }
                mCustomTemplateStyleTypeView.setText("".equals(text) ? mCustomTemplateStyleTypeDefault : text);
                setCustomStateResult(typeChecked);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int length = Math.min(mCustomTemplateStyleTypeItems.length, typeChecked.length);
                for (int i = 0; i < length; i++) {
                    typeChecked[i] = false;
                }
                mCustomTemplateStyleTypeView.setText(mCustomTemplateStyleTypeDefault);
                setCustomStateResult(typeChecked);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void onClickTemplateButtonNumberView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请选择通知按钮个数");
        builder.setSingleChoiceItems(mTemplateButtonNumberItems, mCurrentButtonNumberChecked, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mCurrentButtonNumberChecked = i;
                mTemplateButtonNumberView.setText(mContext.getString(R.string.template_button_number_default, i));
                setButtonNumberResult(mCurrentButtonNumberChecked);
                dialogInterface.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                mTemplateButtonNumberView.setText(mContext.getString(R.string.template_button_number_default,
                        mCurrentButtonNumberChecked));
                setButtonNumberResult(mCurrentButtonNumberChecked);
            }
        });
        builder.show();
    }

    public class CustomState {
        public boolean smallCustom;
        public boolean bigCustom;
        public boolean headsUpCustom;

        CustomState(boolean smallCustom, boolean bigCustom, boolean headsUpCustom) {
            this.smallCustom = smallCustom;
            this.bigCustom = bigCustom;
            this.headsUpCustom = headsUpCustom;
        }
    }
}
