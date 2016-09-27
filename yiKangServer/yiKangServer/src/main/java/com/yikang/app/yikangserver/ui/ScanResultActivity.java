package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;

public class ScanResultActivity extends BaseActivity {
    private LinearLayout ll_bianji;
    private String type;
    private int editorType;
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getIntent().getStringExtra("type");
        result=getIntent().getStringExtra("result");
        if(type.equals("content")) {
            editorType=1;
        }
        if(type.equals("answers")) {
            editorType=3;
        }
        if(type.equals("activity")) {
            editorType=2;
        }
        initContent();
        if(type.equals("content")) {
            initTitleBar("发布专家说");
        }
        if(type.equals("answers")) {
            initTitleBar("我来回答");
        }
        if(type.equals("activity")) {
            initTitleBar("发布活动");
        }
    }

    @Override
    protected void findViews() {
        ll_bianji=(LinearLayout)findViewById(R.id.ll_bianji);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_scan_result);
    }

    @Override
    protected void getData() {
        if(result.endsWith("editor")) {
            showWaitingUI();
            Api.submitErweima(result, editorType, submitHandler);
        }else{
            AppContext.showToast("二维码已过期");
        }
    }
    private ResponseCallback<Void> submitHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
           AppContext.showToast(data+"");
            hideWaitingUI();
            //finish();
           // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        @Override
        public void onFailure(String status, String message) {
            AppContext.showToast(message+"");
            hideWaitingUI();
            //finish();
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };
    @Override
    protected void initViewContent() {
        ll_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}
