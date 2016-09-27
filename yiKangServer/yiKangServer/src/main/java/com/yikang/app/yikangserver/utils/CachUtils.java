package com.yikang.app.yikangserver.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/7/28.
 */
public class CachUtils {
    private static SharedPreferences sp;

    /**
     * 取boolean 如果为true:参数保存过
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return sp.getBoolean(key, false);
    }

    /**
     * 保存一个boolean类型的参数
     *
     * @param context
     * @param key
     * @param values
     */
    public static void setBoolean(Context context, String key, boolean values) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editors = sp.edit();
        editors.putBoolean(key, values);
        editors.commit();
    }
}
