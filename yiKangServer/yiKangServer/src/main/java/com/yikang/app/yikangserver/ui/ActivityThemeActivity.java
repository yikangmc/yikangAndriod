package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;

/**
 * 设置活动主题页面
 */
public class ActivityThemeActivity extends BaseActivity {
    public final static String MESSAGE_INFOS = "messageinfo";
    private String messageinfo;
    private TextView tv_title_right_text,tv_time_activity1,tv_time_activity2,tv_time_activity3;
    private EditText tv_theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        messageinfo = getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);
    }

    @Override
    protected void findViews() {
        tv_title_right_text =(TextView) findViewById(R.id.tv_title_right_text);
        tv_time_activity1 =(TextView) findViewById(R.id.tv_time_activity1);
        tv_time_activity2 =(TextView) findViewById(R.id.tv_time_activity2);
        tv_time_activity3 =(TextView) findViewById(R.id.tv_time_activity3);
        tv_theme =(EditText) findViewById(R.id.tv_theme);
        tv_title_right_text.setText("完成");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ActivityTheme", tv_theme.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tv_time_activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_theme.setText(tv_time_activity1.getText().toString().trim());
            }
        });
        tv_time_activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_theme.setText(tv_time_activity2.getText().toString().trim());
            }
        });
        tv_time_activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_theme.setText(tv_time_activity3.getText().toString().trim());
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_theme);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }


}
