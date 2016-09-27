package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;

/**
 * 发布线上活动页面
 */
public class PublishActivityOnlineActivity extends BaseActivity implements View.OnClickListener {
    public final static String MESSAGE_INFOS = "messageinfo";
    private String messageinfo;
    private String startTimes;
    private String pictrues;
    private int personNumber=0;
    private String personNumbers;
    private String stringDetails;
    private String longActivity;
    private LinearLayout ll_theme, ll_pictrue, ll_time, ll_nums, ll_address, ll_pay, ll_lables, ll_detail;
    private TextView tv_theme, tv_pictrue, tv_time, tv_nums, tv_address, tv_pay, tv_lables, tv_detail;
    private TextView tv_title_right_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initContent();
        messageinfo = getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);

    }

    @Override
    protected void findViews() {
        tv_title_right_text =(TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("发布");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recommendPicUrl=pictrues;//图片
                String title=tv_theme.getText().toString().trim();//主题
                String content=tv_detail.getText().toString().trim();//内容
                String startTime=startTimes;//开始时间
                String endTime=startTimes;//结束时间
                String entryStartTime=longActivity;//报名时间
                String entryEndTime=startTimes;//报名结束时间
                String mapPositionAddress="线上活动";
                String detailAddress="线上活动";
                Long districtCode=Long.parseLong("746464646");
                Double lng=Double.parseDouble("3333");
                Double lat=Double.parseDouble("3333");
                personNumbers=tv_nums.getText().toString().trim();
                if(!personNumbers.equals("")) {
                    personNumber = Integer.parseInt(personNumbers);//人数
                }
                String cost=tv_pay.getText().toString();//费用
                int[] taglibs = {2};
                int activetyMode=1;
                if(title.equals("")|content.equals("")){
                    Toast.makeText(getApplicationContext(),"信息填写不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(startTime.equals("")|endTime.equals("")){
                    Toast.makeText(getApplicationContext(),"信息填写不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(entryStartTime.equals("")|entryEndTime.equals("")){
                    Toast.makeText(getApplicationContext(),"信息填写不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cost.equals("")|personNumbers.equals("")){
                    Toast.makeText(getApplicationContext(),"信息填写不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(getApplicationContext(),recommendPicUrl+title+content+startTime+endTime+entryStartTime+entryEndTime+mapPositionAddress+detailAddress+districtCode+lng+lat+personNumber+cost+taglibs+activetyMode,Toast.LENGTH_SHORT).show();
                showWaitingUI();
              Api.publishActivity(recommendPicUrl,title,stringDetails+"",startTimes,endTime,entryStartTime,entryEndTime,mapPositionAddress,detailAddress,districtCode,lng,lat,personNumber,cost,taglibs,activetyMode,publishActivityHandler);
            }
        });
        ll_theme = (LinearLayout) findViewById(R.id.ll_theme);
        ll_pictrue = (LinearLayout) findViewById(R.id.ll_pictrue);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        ll_lables = (LinearLayout) findViewById(R.id.ll_lables);
        ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
        ll_nums = (LinearLayout) findViewById(R.id.ll_nums);
        tv_theme = (TextView) findViewById(R.id.tv_theme);
        tv_pictrue = (TextView) findViewById(R.id.tv_pictrue);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_nums = (TextView) findViewById(R.id.tv_nums);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_lables = (TextView) findViewById(R.id.tv_lables);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        ll_theme.setOnClickListener(this);
        ll_pictrue.setOnClickListener(this);
        ll_time.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_pay.setOnClickListener(this);
        ll_lables.setOnClickListener(this);
        ll_detail.setOnClickListener(this);
        ll_nums.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_publish_online);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
    private ResponseCallback<String> publishActivityHandler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {
            hideWaitingUI();
            Toast.makeText(getApplicationContext(),"发布线上活动成功",Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
           // LOG.i("debug", "HpWonderfulContent---->" +data );

        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_theme:
                Intent theme_intent = new Intent(getApplicationContext(),
                       ActivityThemeActivity.class);
                theme_intent.putExtra(ActivityThemeActivity.MESSAGE_INFOS,"活动主题");
                startActivityForResult(theme_intent, 1);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.ll_pictrue:
               // Toast.makeText(getApplicationContext(), "去设置宣传图", Toast.LENGTH_SHORT).show();
                Intent pictrue_intent = new Intent(getApplicationContext(),
                        ActivityPictrueActivity.class);
                pictrue_intent.putExtra(ActivityPictrueActivity.MESSAGE_INFOS,"宣传图");
                startActivityForResult(pictrue_intent, 6);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.ll_time:
                Intent time_intent = new Intent(getApplicationContext(),
                        ActivityTimeActivity.class);
                time_intent.putExtra(ActivityTimeActivity.MESSAGE_INFOS,"时间");
                startActivityForResult(time_intent, 2);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.ll_nums:
                break;
            case R.id.ll_address:
                break;
            case R.id.ll_pay:
                break;
            case R.id.ll_lables:
                Toast.makeText(getApplicationContext(), "去设置主标签", Toast.LENGTH_SHORT).show();

                break;
            case R.id.ll_detail:
                Intent detail_intent = new Intent(getApplicationContext(),
                        ActivityDetailsActivity.class);
                detail_intent.putExtra(ActivityDetailsActivity.MESSAGE_INFOS,"活动详情");
                startActivityForResult(detail_intent, 3);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            default:
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            tv_theme.setText(data.getStringExtra("ActivityTheme"));

        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            startTimes=data.getStringExtra("ActivityTheme");
            longActivity=data.getStringExtra("long");
            tv_time.setText(data.getStringExtra("ActivityTheme")+"...");

        }

        if (requestCode == 3 && resultCode == RESULT_OK) {
            stringDetails=data.getStringExtra("ActivityDetails");
            if(data.getStringExtra("ActivityDetails").length()>15) {
                tv_detail.setText(data.getStringExtra("ActivityDetails").substring(0, 15) + "...");
            }else{
                tv_detail.setText(data.getStringExtra("ActivityDetails"));
            }

        }
        if (requestCode == 6 && resultCode == RESULT_OK) {

            pictrues=data.getStringExtra("ActivityPictrues");
            tv_pictrue.setText("已上传");

        }
    }
}
