package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;

public class PublishActivityActivity extends BaseActivity {
    public final static String MESSAGE_INFOS="messageinfo";
    private String messageinfo;
    private Button bt_profession_done;
    private RadioButton rb_online,rb_offline;
    private TextView tv_title_right_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initContent();
        messageinfo=getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);
    }

    @Override
    protected void findViews() {
        tv_title_right_text = (TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("下一步");
        bt_profession_done=(Button)findViewById(R.id.bt_profession_done);
        rb_online=(RadioButton)findViewById(R.id.rb_online);
        rb_offline=(RadioButton)findViewById(R.id.rb_offline);
        bt_profession_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplication(), ScancodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type","activity");
                startActivity(intent);
                finish();
            }
        });
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_online.isChecked()) {
                    Intent intent1 = new Intent(getApplicationContext(),
                            PublishActivityOnlineActivity.class);
                    intent1.putExtra(PublishActivityOnlineActivity.MESSAGE_INFOS,"发布线上活动");
                    startActivity(intent1);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }else{

                    Intent intent2 = new Intent(getApplicationContext(),
                            PublishActivityOfflineActivity.class);
                    intent2.putExtra(PublishActivityOfflineActivity.MESSAGE_INFOS,"发布线下活动");
                    startActivity(intent2);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_activity_style);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
