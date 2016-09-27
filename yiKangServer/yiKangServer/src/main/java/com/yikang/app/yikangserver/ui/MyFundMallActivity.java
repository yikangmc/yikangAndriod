package com.yikang.app.yikangserver.ui;

import android.os.Bundle;

import com.yikang.app.yikangserver.R;

public class MyFundMallActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("积分商城");
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_my_fund_mall);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
