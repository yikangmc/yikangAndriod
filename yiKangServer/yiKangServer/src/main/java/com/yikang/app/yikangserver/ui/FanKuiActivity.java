package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;

public class FanKuiActivity extends BaseActivity {
    private TextView tv_title_right_text;
    private EditText tv_theme;
    private EditText et_phone;
    private String details;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initContent();
        initTitleBar("意见反馈");
    }

    @Override
    protected void findViews() {
        tv_title_right_text =(TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("完成");
        tv_theme =(EditText) findViewById(R.id.tv_theme);
        et_phone =(EditText) findViewById(R.id.et_phone);
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details=tv_theme.getText().toString();
                phone=et_phone.getText().toString().trim()+"";
                if(details.equals("")) {
                    Toast.makeText(getApplicationContext(), "提交内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                showWaitingUI();
               Api.submitFankui(details,phone,submitFankuiHandler);

            }
        });
    }
    private ResponseCallback<String> submitFankuiHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败"+data);
            hideWaitingUI();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            //LOG.i("debug", "[uploadAvatarHandler]上传图片失败"+status+message);
           // AppContext.showToast(message);
            if(status.equals("99999")){
                AppContext.showToast(message);
                return;
            }
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_fan_kui);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
