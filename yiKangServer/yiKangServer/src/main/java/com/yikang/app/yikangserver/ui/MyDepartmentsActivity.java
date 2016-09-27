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

public class MyDepartmentsActivity extends BaseActivity {
    private TextView tv_title_right_text;
    private EditText et_sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("所在科室");
    }

    @Override
    protected void findViews() {
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        et_sex=(EditText) findViewById(R.id.et_sex);
        tv_title_right_text.setText("保存");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_sex.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"请填写所在科室",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    showWaitingUI();
                    Api.alterOffces(et_sex.getText().toString(),alterNameHandler);
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
            intent.putExtra("department", et_sex.getText().toString());// mCurrentDistrictName
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
        setContentView(R.layout.activity_my_departments);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
