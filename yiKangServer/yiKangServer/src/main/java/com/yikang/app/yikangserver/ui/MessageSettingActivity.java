package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.MessageState;

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
	protected void getData() {
        Api.messageSettingState(new ResponseCallback<MessageState>() {
            @Override
            public void onSuccess(MessageState data) {

                if(data.getDynamicAlert()==0){
                    setting_checkbox_discuss.setChecked(true);
                }else {
                    setting_checkbox_discuss.setChecked(false);
                }
                if(data.getSystemAlert()==0){
                    setting_checkbox_system.setChecked(true);
                }else {
                    setting_checkbox_system.setChecked(false);
                }
            }

            @Override
            public void onFailure(String status, String message) {

            }
        });
    }
	@Override
	protected void initViewContent() {}

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.setting_checkbox_discuss:
                flag1=!flag1;

                if(setting_checkbox_discuss.isChecked()==true){

                    Api.changeMessageSettingDynState(0,new ResponseCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            setting_checkbox_discuss.setChecked(true);
                        }

                        @Override
                        public void onFailure(String status, String message) {
                            setting_checkbox_discuss.setChecked(false);
                        }
                    });
                }else if(setting_checkbox_discuss.isChecked()==false){

                    Api.changeMessageSettingDynState(1,new ResponseCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {

                            setting_checkbox_discuss.setChecked(false);
                        }

                        @Override
                        public void onFailure(String status, String message) {
                            setting_checkbox_discuss.setChecked(true);
                        }
                    });
                }
                break;
            case R.id.setting_checkbox_support:
                flag2=!flag2;
                setting_checkbox_support.setChecked(flag2);
                if(flag2==true){
                    AppContext.showToast("关闭支持");
                }else if(flag2==false){
                    AppContext.showToast("开启支持");
                }
                break;
            case R.id.setting_checkbox_answer:
                flag3=!flag3;
                setting_checkbox_answer.setChecked(flag3);
                if(flag3==true){
                    AppContext.showToast("关闭回答");
                }else if(flag3==false){
                    AppContext.showToast("开启回答");
                }
                break;
            case R.id.setting_checkbox_system:
                flag4=!flag4;

                if(setting_checkbox_system.isChecked()==true){

                    Api.changeMessageSettingSysState(0,new ResponseCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            setting_checkbox_system.setChecked(true);
                        }

                        @Override
                        public void onFailure(String status, String message) {
                            setting_checkbox_system.setChecked(false);
                        }
                    });
                }else if(setting_checkbox_system.isChecked()==false){

                    Api.changeMessageSettingSysState(1,new ResponseCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {

                            setting_checkbox_system.setChecked(false);
                        }

                        @Override
                        public void onFailure(String status, String message) {
                            setting_checkbox_system.setChecked(true);
                        }
                    });
                }
                break;
        }
    }
}
