package com.huawaii.notifications.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Copyright (C) 2017 Meizu Telecom Equipment Co., Ltd. All rights reserved.
 *
 * @author lizongheng@meizu.com (李宗恒)
 */
public class Proxy {
    public Proxy() {
    }

    protected static Method getMethod(Method method, Class<?> clazz, String name, Class... parameterTypes) {
        if (method == null) {
            try {
                method = clazz.getMethod(name, parameterTypes);
            } catch (NoSuchMethodException var5) {
                var5.printStackTrace();
            }
        }

        return method;
    }

    protected static boolean invoke(Method method, Object obj, Object... args) {
        if (method != null) {
            try {
                method.invoke(obj, args);
                return true;
            } catch (IllegalAccessException var4) {
                var4.printStackTrace();
            } catch (IllegalArgumentException var5) {
                var5.printStackTrace();
            } catch (InvocationTargetException var6) {
                var6.printStackTrace();
            }
        }

        return false;
    }
}
