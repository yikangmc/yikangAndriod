package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.yikang.app.yikangserver.R;

public class MessageSettingActivity extends BaseActivity implements View.OnClickListener{

    private CheckBox setting_checkbox_system,setting_checkbox_answer,setting_checkbox_support,setting_checkbox_discuss;
    private boolean flag1,flag2,flag3,flag4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContent();
        initTitleBar("消息设置");
	}
	
	@Override
	protected void findViews() {
        setting_checkbox_discuss=(CheckBox)findViewById(R.id.setting_checkbox_discuss);
        setting_checkbox_support=(CheckBox)findViewById(R.id.setting_checkbox_support);
        setting_checkbox_answer=(CheckBox)findViewById(R.id.setting_checkbox_answer);
        setting_checkbox_system=(CheckBox)findViewById(R.id.setting_checkbox_system);
        setting_checkbox_discuss.setOnClickListener(this);
        setting_checkbox_support.setOnClickListener(this);
        setting_checkbox_answer.setOnClickListener(this);
        setting_checkbox_system.setOnClickListener(this);
	}


	@Override
	protected void setContentView() {
        setContentView(R.layout.activity_message_setting);
	}

	@Override
	protected void getData() {}
	@Override
	protected void initViewContent() {}

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.setting_checkbox_discuss:
                flag1=!flag1;
                setting_checkbox_discuss.setChecked(flag1);
                break;
            case R.id.setting_checkbox_support:
                flag2=!flag2;
                setting_checkbox_support.setChecked(flag2);
                break;
            case R.id.setting_checkbox_answer:
                flag3=!flag3;
                setting_checkbox_answer.setChecked(flag3);
                break;
            case R.id.setting_checkbox_system:
                flag4=!flag4;
                setting_checkbox_system.setChecked(flag4);
                break;
        }
    }
}
