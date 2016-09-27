package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.WonderfulActivity;

import java.text.SimpleDateFormat;


public class PayActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton activty_pay_weixin_rb, activty_pay_alipay_rb;
    private Button bt_confrim_pay;
    private String activityId;
    private WonderfulActivity wonderfulActivity;
    private TextView activity_title,activity_username,activity_time,activity_address,activity_pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        wonderfulActivity=(WonderfulActivity)intent.getSerializableExtra("users");
        activityId=wonderfulActivity.getActivetyId()+"";
        initContent();
        initTitleBar("活动报名");
    }
    @Override
    protected void findViews() {
        activty_pay_weixin_rb=(RadioButton)findViewById(R.id.activty_pay_weixin_rb);
        activty_pay_alipay_rb=(RadioButton)findViewById(R.id.activty_pay_alipay_rb);
        bt_confrim_pay=(Button)findViewById(R.id.bt_confrim_pay);
        activty_pay_weixin_rb.setOnClickListener(this);
        activty_pay_alipay_rb.setOnClickListener(this);
        bt_confrim_pay.setOnClickListener(this);
        activity_title=(TextView) findViewById(R.id.activity_title);
        activity_username=(TextView) findViewById(R.id.activity_username);
        activity_time=(TextView) findViewById(R.id.activity_time);
        activity_address=(TextView) findViewById(R.id.activity_address);
        activity_pay=(TextView) findViewById(R.id.activity_pay);
        activity_title.setText(wonderfulActivity.getTitle());
        activity_username.setText(wonderfulActivity.getCreateUsername()+" (发起)");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        activity_time.setText(sdf.format(wonderfulActivity.getStartTime())+"");
        activity_address.setText(wonderfulActivity.getDetailAddress());
        activity_pay.setText("￥"+wonderfulActivity.getCost()+"");
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
            case R.id.bt_confrim_pay:
                showWaitingUI();
                Api.joinActivity(Integer.parseInt(activityId),joinActivityHandler);
                break;
        }
    }
    /**
     * 参加活动的回调
     */
    private ResponseCallback<Void > joinActivityHandler = new ResponseCallback<Void>() {

        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            AppContext.showToast( message);

        }
    };
}
