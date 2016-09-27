package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;

public class MySexActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_right_text;
    private EditText et_sex;
    private int userSex=0;
    private RadioButton activty_pay_weixin_rb, activty_pay_alipay_rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("性别修改");
    }

    @Override
    protected void findViews() {
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        activty_pay_weixin_rb=(RadioButton)findViewById(R.id.activty_pay_weixin_rb);
        activty_pay_alipay_rb=(RadioButton)findViewById(R.id.activty_pay_alipay_rb);
        activty_pay_weixin_rb.setOnClickListener(this);
        activty_pay_alipay_rb.setOnClickListener(this);
        et_sex=(EditText) findViewById(R.id.et_sex);
        tv_title_right_text.setText("保存");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showWaitingUI();
                    Api.alterUserSex(userSex,alterNameHandler);
            }
        });
    }
    private ResponseCallback<Void> alterNameHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            Intent intent = new Intent();
            if( userSex==1){
                intent.putExtra("sex", "男");
            }
            if( userSex==2){
                intent.putExtra("sex", "女");
            }
            if( userSex==0){
                intent.putExtra("sex", "男");
            }
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("资料修改失败：" + message);

        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_my_sex);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {
        if(getIntent().getStringExtra("sex")==null){
            activty_pay_weixin_rb.setChecked(true);
            userSex=0;
            return;
        }
        else if(getIntent().getStringExtra("sex").equals("男")){
            activty_pay_weixin_rb.setChecked(true);
            userSex=1;
            return;
        }
       else if(getIntent().getStringExtra("sex").equals("女")){
            activty_pay_alipay_rb.setChecked(true);
            userSex=2;
            return;
        } else{
            activty_pay_weixin_rb.setChecked(true);
            userSex=0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activty_pay_weixin_rb:
                activty_pay_weixin_rb.setChecked(true);
                userSex=1;
                break;
            case R.id.activty_pay_alipay_rb:
                activty_pay_alipay_rb.setChecked(true);
                userSex=2;
                break;

        }
    }
}
