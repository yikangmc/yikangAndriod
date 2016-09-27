package com.yikang.app.yikangserver.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;

/**
 * Created by yudong on 2016/4/28.
 */
public class AlertDialog {
    Context context;
    android.app.AlertDialog ad;
    TextView titleView;
    TextView messageView;
    LinearLayout buttonLayoutsuer;
    LinearLayout buttonLayoutcancle;
    public AlertDialog(Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
        ad=new android.app.AlertDialog.Builder(context).create();
        ad.show();
        //关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = ad.getWindow();
        window.setContentView(R.layout.dialog_alter_layout);
        titleView=(TextView)window.findViewById(R.id.dialog_title);
        messageView=(TextView)window.findViewById(R.id.dialog_message);
        buttonLayoutsuer=(LinearLayout)window.findViewById(R.id.dialog_bt_sure);
        buttonLayoutcancle=(LinearLayout)window.findViewById(R.id.dialog_bt_cancle);
    }
    public void setTitle(int resId)
    {
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(resId);
    }
    public void setTitle(String title) {
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(title);
    }
    public void setMessage(int resId) {
        messageView.setText(resId);
    }  public void setMessage(String message)
    {
        messageView.setText(message);
    }
    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text,final View.OnClickListener listener)
    {
        buttonLayoutsuer.setOnClickListener(listener);
    }  /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text,final View.OnClickListener listener)
    {
        buttonLayoutcancle.setOnClickListener(listener);
     }
    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }
}
