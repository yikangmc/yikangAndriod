package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.application.AppContext;

public class KangFushiActivity extends BaseActivity implements View.OnClickListener {
    private RadioButton rb_profession_doctor, rb_profession_therapists;
    private Button btDone;
    private RadioGroup rg_professionqiye_choices;
    private int profession; //选中的职业

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("身份认证");
    }

    @Override
    protected void findViews() {
        rg_professionqiye_choices = ((RadioGroup) findViewById(R.id.rg_professionqiye_choices));
        rb_profession_doctor = ((RadioButton) findViewById(R.id.rb_profession_doctor));
        rb_profession_therapists = ((RadioButton) findViewById(R.id.rb_profession_therapists));
        btDone = ((Button) findViewById(R.id.bt_profession_done));
        btDone.setOnClickListener(this);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_kang_fushi);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_profession_done:
                if (rb_profession_doctor.isChecked()) {
                    Intent intentBasicInfo = new Intent(getApplicationContext(),
                            YiyuanKangfushiActivity.class);
                    startActivity(intentBasicInfo);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                   // finish();
                    return;
                }
                if (rb_profession_therapists.isChecked()) {

                    Intent intentBasicInfo = new Intent(getApplicationContext(),
                            FeiYiyuanKangfushiActivity.class);
                    startActivity(intentBasicInfo);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                   // finish();
                    return;
                } else {
                    AppContext.showToast("请选择工作主体");
                }
        }
    }
}
