package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.yikang.app.yikangserver.R;


public class PayActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton activty_pay_weixin_rb, activty_pay_alipay_rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("活动报名");
    }
    @Override
    protected void findViews() {
        activty_pay_weixin_rb=(RadioButton)findViewById(R.id.activty_pay_weixin_rb);
        activty_pay_alipay_rb=(RadioButton)findViewById(R.id.activty_pay_alipay_rb);
        activty_pay_weixin_rb.setOnClickListener(this);
        activty_pay_alipay_rb.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.community_activities_signup);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activty_pay_weixin_rb:
                activty_pay_weixin_rb.setChecked(true);
                break;
            case R.id.activty_pay_alipay_rb:
                activty_pay_alipay_rb.setChecked(true);
                break;
        }
    }
}
