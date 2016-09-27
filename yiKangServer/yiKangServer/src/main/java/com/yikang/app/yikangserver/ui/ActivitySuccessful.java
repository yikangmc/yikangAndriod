package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yikang.app.yikangserver.R;

public class ActivitySuccessful extends BaseActivity {
    private Button bt_backto_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("报名成功");
    }

    @Override
    protected void findViews() {
        bt_backto_activity=(Button) findViewById(R.id.bt_backto_activity);
        bt_backto_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_activity_successful);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
