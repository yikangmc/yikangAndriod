package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;

public class MyBirthdayActivity extends BaseActivity {
    private TextView tv_title_right_text;
    private EditText et_birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("生日");
    }

    @Override
    protected void findViews() {
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        et_birthday=(EditText) findViewById(R.id.et_birthday);
        tv_title_right_text.setText("保存");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_birthday.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"请填写新的生日",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("birthday", et_birthday.getText().toString());// mCurrentDistrictName
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_my_birthday);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
