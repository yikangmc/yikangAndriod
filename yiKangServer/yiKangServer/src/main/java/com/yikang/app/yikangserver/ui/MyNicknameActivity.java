package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;

public class MyNicknameActivity extends BaseActivity {
    private TextView tv_title_right_text;
    private EditText et_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("名称修改");
    }

    @Override
    protected void findViews() {
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        et_name=(EditText) findViewById(R.id.et_name);
        et_name.setText(getIntent().getStringExtra("name"));
        tv_title_right_text.setText("保存");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"请填写新的名称",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    showWaitingUI();
                    Api.alterName(et_name.getText().toString(),alterNameHandler);

                }
            }
        });
    }
    private ResponseCallback<Void> alterNameHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            Intent intent = new Intent();
            intent.putExtra("name", et_name.getText().toString());// mCurrentDistrictName
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
        setContentView(R.layout.activity_my_nickname);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
