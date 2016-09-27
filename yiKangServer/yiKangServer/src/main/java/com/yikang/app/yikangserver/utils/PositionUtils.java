package com.yikang.app.yikangserver.utils;

import android.widget.TextView;

import com.yikang.app.yikangserver.R;

/**
 * Created by Administrator on 2016/7/11.
 */
public class PositionUtils {
    public static void  getPosition(int i,TextView tv) {

        if(i==1) {
            tv.setBackgroundResource(R.drawable.yk_all_tab_unit_kang);
        }
        if(i==2) {
            tv.setBackgroundResource(R.drawable.yk_all_tab_unit_zhong);
        }
        if(i==3) {
            tv.setBackgroundResource(R.drawable.yk_all_tab_unit_hu);
        }
        if(i==4) {
            tv.setBackgroundResource(R.drawable.yk_all_tab_unit_qi);
        }
        if(i==5) {
            tv.setBackgroundResource(R.drawable.yk_all_tab_unit_yuan);
        }
        if(i==0) {
            tv.setBackgroundResource(R.color.transparent);
        }
    }
}
