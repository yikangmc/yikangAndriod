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

public class MyAddressActivity extends BaseActivity {
    private TextView tv_title_right_text;
    private EditText et_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("地址");
    }

    @Override
    protected void findViews() {
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        et_address=(EditText) findViewById(R.id.et_address);
        et_address.setText(getIntent().getStringExtra("address"));
        tv_title_right_text.setText("保存");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_address.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"请填写新的地址",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    showWaitingUI();
                    Api.alterAddr("","8888",et_address.getText().toString(),alterNameHandler);

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
            intent.putExtra("address", et_address.getText().toString());// mCurrentDistrictName
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
        setContentView(R.layout.activity_my_address);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
