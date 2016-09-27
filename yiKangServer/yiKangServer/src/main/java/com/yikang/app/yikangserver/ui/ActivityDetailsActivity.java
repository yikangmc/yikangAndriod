package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;

public class ActivityDetailsActivity extends BaseActivity {
    public final static String MESSAGE_INFOS = "messageinfo";
    private String messageinfo;
    private String details;
    private TextView tv_title_right_text;
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
        tv_theme =(EditText) findViewById(R.id.tv_theme);
        tv_title_right_text.setText("完成");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details=tv_theme.getText().toString();
                if(details.equals("")) {
                    Toast.makeText(getApplicationContext(), "信息填写不全", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("ActivityDetails", tv_theme.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_activity_details);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }


}
