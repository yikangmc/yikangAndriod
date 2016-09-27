package com.yikang.app.yikangserver.view;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }


    public MyListView(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
