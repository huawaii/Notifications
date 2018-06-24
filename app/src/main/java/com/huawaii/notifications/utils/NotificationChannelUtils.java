package com.huawaii.notifications.utils;

/**
 * Copyright (C) 2017 Meizu Telecom Equipment Co., Ltd. All rights reserved.
 *
 * @author lizongheng@meizu.com (李宗恒)
 */
public class NotificationChannelUtils {
    private static final ChannelParamRecord sChannelParamInstance = new ChannelParamRecord();

    public static ChannelParamRecord getChannelParamInstance() {
        return sChannelParamInstance;
    }

    private NotificationChannelUtils() {
    }

    public static class ChannelParamRecord {
        public int importance;
        public boolean isBuzz = false;
        public boolean isBeep = false;
        public boolean isBlink = false;

        @Override
        public String toString() {
            return "importance:" + importance
                    + "buzz:" + (isBuzz ? "1" : "0")
                    + "beep:" + (isBeep ? "1" : "0")
                    + "blink:" + (isBlink ? "1" : "0");
        }
    }
}
