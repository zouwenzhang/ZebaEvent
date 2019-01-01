package com.zeba.event;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtil<T> {
    private static volatile Gson gson;
    public static Gson Instance(){
        if (gson == null) {
            synchronized (GsonUtil.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
    /**
     * 对象转换成json字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return Instance().toJson(obj);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        return Instance().fromJson(str, type);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        return Instance().fromJson(str, type);
    }
}
