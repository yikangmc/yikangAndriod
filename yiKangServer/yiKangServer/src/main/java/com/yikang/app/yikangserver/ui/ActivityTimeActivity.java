package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.view.DateTimePickerDialog;

import java.text.SimpleDateFormat;

public class ActivityTimeActivity extends BaseActivity {
    public final static String MESSAGE_INFOS = "messageinfo";
    private String messageinfo;
    private String baomingStart;
    private String activityStart;
    private String timeLong;
    private TextView tv_title_right_text,tv_baoming_start,tv_activity_start;
    private LinearLayout ll_baoming_start,ll_activity_start,ll_time;
    private EditText tv_time_long;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initContent();
        messageinfo = getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);
    }

    @Override
    protected void findViews() {
        tv_time_long =(EditText) findViewById(R.id.tv_time_long);
        tv_title_right_text =(TextView) findViewById(R.id.tv_title_right_text);
        tv_baoming_start =(TextView) findViewById(R.id.tv_baoming_start);
        tv_activity_start =(TextView) findViewById(R.id.tv_activity_start);
        ll_baoming_start =(LinearLayout) findViewById(R.id.ll_baoming_start);
        ll_activity_start =(LinearLayout) findViewById(R.id.ll_activity_start);
        ll_time =(LinearLayout) findViewById(R.id.ll_time);
        tv_title_right_text.setText("完成");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLong=tv_time_long.getText().toString().trim();
                if(baomingStart==null|activityStart==null) {
                    Toast.makeText(getApplicationContext(), "信息填写不全", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(timeLong.equals("")){
                    Toast.makeText(getApplicationContext(), "信息填写不全", Toast.LENGTH_SHORT).show();
                    return;
                }

                    Intent intent = new Intent();
                    intent.putExtra("ActivityTheme", activityStart);
                    intent.putExtra("long", baomingStart);
                    setResult(RESULT_OK, intent);
                    finish();

            }
        });
        ll_baoming_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog();
            }


        });
        ll_activity_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogs();
            }
        });

    }
    public void showDialog() {
        DateTimePickerDialog dialog = new DateTimePickerDialog(
                ActivityTimeActivity.this, System.currentTimeMillis());
        Window dialogWindow = dialog.getWindow();

        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ActionBar.LayoutParams.MATCH_PARENT;
        attributes.height = ActionBar.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER_HORIZONTAL;
        dialog.getWindow().setAttributes(attributes);
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
            @Override
            public void OnDateTimeSet(android.app.AlertDialog dialog, long date) {
                //Toast.makeText(ActivityTimeActivity.this, "您输入的日期是："+getStringDate(date), Toast.LENGTH_LONG).show();
                tv_baoming_start.setText(getStringDate(date));
                baomingStart=getStringDate(date);
            }

        });
        dialog.show();
    }
    public void showDialogs() {
        DateTimePickerDialog dialog = new DateTimePickerDialog(
                ActivityTimeActivity.this, System.currentTimeMillis());
        Window dialogWindow = dialog.getWindow();

        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ActionBar.LayoutParams.MATCH_PARENT;
        attributes.height = ActionBar.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER_HORIZONTAL;
        dialog.getWindow().setAttributes(attributes);
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
            @Override
            public void OnDateTimeSet(android.app.AlertDialog dialog, long date) {
                //Toast.makeText(ActivityTimeActivity.this, "您输入的日期是："+getStringDate(date), Toast.LENGTH_LONG).show();
                tv_activity_start.setText(getStringDate(date));
                activityStart=getStringDate(date);
            }

        });
        dialog.show();
    }
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     */
    public static String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);

        return dateString;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_online);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
