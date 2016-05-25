package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.LvHightUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonalHomepageActivity extends BaseActivity implements View.OnClickListener{

    private CommonAdapter<String> mMessageAdapter;
    private List<String> mList=new ArrayList<>();
    private ListView personal_new_dynamic_lv;
    public final static String MESSAGE_INFO="messageinfo";
    private boolean flag;
    private ImageView personal_homepage_focuson_iv;
    private TextView personal_homepage_focuson_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContent();
        initTitleBar("个人信息");
	}
	
	@Override
	protected void findViews() {
        for (int i=0;i<5;i++){
            mList.add("333333");
        }
        personal_new_dynamic_lv=(ListView)findViewById(R.id.personal_new_dynamic_lv);
        mMessageAdapter=new CommonAdapter<String>(this,mList,R.layout.item_dynamic_new) {
            @Override
            protected void convert(ViewHolder holder, String item) {

            }
        };
        personal_new_dynamic_lv.setAdapter(mMessageAdapter);
        LvHightUtils.setListViewHeightBasedOnChildren(personal_new_dynamic_lv);
        personal_new_dynamic_lv.setFocusable(false);
        personal_new_dynamic_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LOG.i("debug","position===>"+position);
            }
        });
        personal_homepage_focuson_iv=(ImageView)findViewById(R.id.personal_homepage_focuson_iv);
        personal_homepage_focuson_tv=(TextView) findViewById(R.id.personal_homepage_focuson_tv);
        personal_homepage_focuson_iv.setOnClickListener(this);
        personal_homepage_focuson_tv.setOnClickListener(this);
	}


	@Override
	protected void setContentView() {
        setContentView(R.layout.activity_personal_homepage);
	}

	@Override
	protected void getData() {}
	@Override
	protected void initViewContent() {}

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.personal_homepage_focuson_iv:
                if (flag){
                    personal_homepage_focuson_iv.setImageResource(R.drawable.yk_all_personal_focuson);
                }else{
                    personal_homepage_focuson_iv.setImageResource(R.drawable.yk_all_personal_focuson_cancle);
                }
                flag=!flag;
                break;
            case R.id.personal_homepage_focuson_tv:
                Intent intent = new Intent(getApplicationContext(),
                        FocusActivity.class);
                intent.putExtra(FocusActivity.MESSAGE_INFO,"关注者");//动态消息
                startActivity(intent);
                break;
        }
    }
}
