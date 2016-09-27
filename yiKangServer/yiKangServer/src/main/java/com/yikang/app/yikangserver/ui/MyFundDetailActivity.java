package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;

public class MyFundDetailActivity extends BaseActivity {
    private TextView tv_title_right_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initContent();
        initTitleBar("积分明细");
    }

    @Override
    protected void findViews() {
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("常见问题");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WebIntent = new Intent(getApplicationContext(), WebComunicateviewActivivty.class);
                WebIntent.putExtra("bannerUrl","http://www.baidu.com");
                WebIntent.putExtra("bannerName","积分明细常见问题");
                startActivity(WebIntent);
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_my_fund_detail);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
