package com.yikang.app.yikangserver.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.FundBean;
import com.yikang.app.yikangserver.bean.Job;
import com.yikang.app.yikangserver.utils.LOG;

import java.util.ArrayList;

public class MyFundActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_right_text,tv_fund_rule,tv_today_fund,tv_sum_fund,tv_new_avator,tv_first_sign,tv_try_publish,tv_first_comment,tv_try_ask,tv_first_share,tv_health_sign,tv_share,tv_publish,tv_comment,tv_ask,tv_invite_friend;
    private LinearLayout ll_fund_introduce,ll_gonglue,ll_richang;
    private FrameLayout fl_fund_mall;
    private com.yikang.app.yikangserver.view.MyListView ll_more_fund;
    private com.yikang.app.yikangserver.view.MyListView ll_once_fund;
    private ImageView iv_gonglue_shouqi,iv_gonglue_zhankai,iv_richang_shouqi,iv_richang_zhankai;
    private Handler mHandler;
    private FundBean fundBean;
    private CommonAdapter<Job> mMessageAdapter;
    private CommonAdapter<Job> mMessageAdapterOnce;
    private ArrayList<Job> lables = new ArrayList<Job>();
    private ArrayList<Job> labless = new ArrayList<Job>();
    private ArrayList<Job> lablesss = new ArrayList<Job>();
    private ArrayList<Job> oncelables = new ArrayList<Job>();
    private ArrayList<Job> oncelabless = new ArrayList<Job>();
    private ArrayList<Job> oncelablesss = new ArrayList<Job>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("我的佳佳积分");
        mHandler = new Handler();
    }

    @Override
    protected void findViews() {
        ll_more_fund=(com.yikang.app.yikangserver.view.MyListView) findViewById(R.id.ll_more_fund);
        ll_once_fund=(com.yikang.app.yikangserver.view.MyListView) findViewById(R.id.ll_once_fund);
        iv_gonglue_shouqi=(ImageView) findViewById(R.id.iv_gonglue_shouqi);
        iv_gonglue_zhankai=(ImageView) findViewById(R.id.iv_gonglue_zhankai);
        iv_richang_shouqi=(ImageView) findViewById(R.id.iv_richang_shouqi);
        iv_richang_zhankai=(ImageView) findViewById(R.id.iv_richang_zhankai);
        ll_fund_introduce = (LinearLayout) findViewById(R.id.ll_fund_introduce);
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        fl_fund_mall=(FrameLayout) findViewById(R.id.fl_fund_mall);
        tv_today_fund=(TextView) findViewById(R.id.tv_today_fund);
        tv_sum_fund=(TextView) findViewById(R.id.tv_sum_fund);


        tv_title_right_text.setText("积分规则");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WebIntent = new Intent(getApplicationContext(), WebComunicateviewActivivty.class);
                WebIntent.putExtra("bannerUrl","http://www.jjkangfu.cn/appPage/integralRule");
                WebIntent.putExtra("bannerName","佳佳积分规则");
                startActivity(WebIntent);
            }
        });

        ll_fund_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent WebIntent = new Intent(getApplicationContext(), WebComunicateviewActivivty.class);
                WebIntent.putExtra("bannerUrl","");
                WebIntent.putExtra("bannerName","攻略说明");
                startActivity(WebIntent);*/
            }
        });
        iv_gonglue_shouqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_gonglue_zhankai.setVisibility(View.VISIBLE);
                iv_gonglue_shouqi.setVisibility(View.GONE);

            }
        });
        iv_gonglue_zhankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_gonglue_zhankai.setVisibility(View.GONE);
                iv_gonglue_shouqi.setVisibility(View.VISIBLE);

            }
        });
        iv_richang_shouqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_richang_zhankai.setVisibility(View.VISIBLE);
                iv_richang_shouqi.setVisibility(View.GONE);


            }
        });
        iv_richang_zhankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_richang_zhankai.setVisibility(View.GONE);
                iv_richang_shouqi.setVisibility(View.VISIBLE);


            }
        });

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_my_fund);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        Api.getJFDetail(new ResponseCallback<FundBean>() {
            @Override
            public void onSuccess(FundBean data) {
                LOG.i("debug", "JF---->" + data);
                fundBean=data;

                lables = (ArrayList<Job>) fundBean.getUsualJobs();

                oncelables = (ArrayList<Job>) fundBean.getOnceJobs();

                hideWaitingUI();
                initViewContents();
            }

            @Override
            public void onFailure(String status, String message) {
                LOG.i("debug", "JF---->" + status+message);
                //hideWaitingUI();
                tv_today_fund.setText(0+"");
                tv_sum_fund.setText(0+"");
            }
        });
    }
    protected void initViewContents() {
        mMessageAdapterOnce=new CommonAdapter<Job>(getApplication(),oncelables,R.layout.list_fund_more_item) {
            @Override
            protected void convert(ViewHolder holder, final Job items) {
                final TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_fengshu = holder.getView(R.id.tv_fengshu);
                final ImageView iv_weiwancheng = holder.getView(R.id.iv_weiwancheng);
                final ImageView iv_yiwancheng = holder.getView(R.id.iv_yiwancheng);
                final ImageView iv_lingqu = holder.getView(R.id.iv_lingqu);
                tv_name.setText(items.getJobTitle());
                if(items.getScore()!=0) {
                    tv_fengshu.setText("+" + items.getScore());
                }
                if(items.getScore()==0){
                    tv_fengshu.setText("随机积分");
                }
                if(items.getJobState()==0){//未完成
                    iv_lingqu.setVisibility(View.GONE);
                    iv_weiwancheng.setVisibility(View.VISIBLE);
                    iv_yiwancheng.setVisibility(View.GONE);
                    tv_name.setTextColor(getResources().getColor(R.color.homepage_title_left_text_color));
                }
                if(items.getJobState()==1){//已完成，待领取
                    iv_weiwancheng.setVisibility(View.GONE);
                    iv_yiwancheng.setVisibility(View.GONE);
                    iv_lingqu.setVisibility(View.VISIBLE);
                    tv_name.setTextColor(getResources().getColor(R.color.community_activity_tv_color));
                    iv_lingqu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        showWaitingUI();
                            Api.receiveJF(items.getJobId(), new ResponseCallback<FundBean>() {
                                @Override
                                public void onSuccess(FundBean data) {
                                    hideWaitingUI();
                                    //AppContext.showToast("领取积分");
                                    showDialog(items.getJobTitle(),data.getScore()+"");
                                    if(data.getTodayScore()==null){
                                        tv_today_fund.setText(0 + "");
                                        tv_sum_fund.setText(data.getMyScore() + "");
                                    }else {
                                        tv_today_fund.setText(data.getTodayScore() + "");
                                        tv_sum_fund.setText(data.getMyScore() + "");
                                    }
                                    iv_lingqu.setVisibility(View.GONE);
                                    iv_weiwancheng.setVisibility(View.GONE);
                                    iv_yiwancheng.setVisibility(View.VISIBLE);
                                    tv_name.setTextColor(getResources().getColor(R.color.homepage_title_right_text_colors));
                                }

                                @Override
                                public void onFailure(String status, String message) {
                                    hideWaitingUI();
                                    AppContext.showToast(message);
                                }
                            });
                        }
                    });
                }
                if(items.getJobState()==2) {//已领取
                    iv_lingqu.setVisibility(View.GONE);
                    iv_weiwancheng.setVisibility(View.GONE);
                    iv_yiwancheng.setVisibility(View.VISIBLE);
                    tv_name.setTextColor(getResources().getColor(R.color.homepage_title_right_text_colors));
                }
            }
        };
        ll_once_fund.setAdapter(mMessageAdapterOnce);

        mMessageAdapter=new CommonAdapter<Job>(getApplication(),lables,R.layout.list_fund_more_item) {
            @Override
            protected void convert(ViewHolder holder, final Job item) {
                final TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_fengshu = holder.getView(R.id.tv_fengshu);
                final ImageView iv_weiwancheng = holder.getView(R.id.iv_weiwancheng);
                final ImageView iv_yiwancheng = holder.getView(R.id.iv_yiwancheng);
                final ImageView iv_lingqu = holder.getView(R.id.iv_lingqu);
                tv_name.setText(item.getJobTitle());
                if(item.getScore()!=0) {
                    tv_fengshu.setText("+" + item.getScore());
                }
                if(item.getScore()==0){
                    tv_fengshu.setText("随机积分");
                }
                if(item.getJobState()==0){//未完成
                    iv_weiwancheng.setVisibility(View.VISIBLE);
                    iv_yiwancheng.setVisibility(View.GONE);
                    iv_lingqu.setVisibility(View.GONE);
                    tv_name.setTextColor(getResources().getColor(R.color.homepage_title_left_text_color));
                }
                if(item.getJobState()==1){//已完成，待领取
                    iv_weiwancheng.setVisibility(View.GONE);
                    iv_yiwancheng.setVisibility(View.GONE);
                    iv_lingqu.setVisibility(View.VISIBLE);
                    tv_name.setTextColor(getResources().getColor(R.color.community_activity_tv_color));
                    iv_lingqu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                showWaitingUI();
                            Api.receiveJF(item.getJobId(), new ResponseCallback<FundBean>() {
                                @Override
                                public void onSuccess(FundBean data) {
                                    hideWaitingUI();
                                   //AppContext.showToast("领取积分");
                                    showDialog(item.getJobTitle(),data.getScore()+"");
                                    tv_today_fund.setText(data.getTodayScore()+"");
                                    tv_sum_fund.setText(data.getMyScore()+"");
                                    iv_lingqu.setVisibility(View.GONE);
                                    iv_weiwancheng.setVisibility(View.GONE);
                                    iv_yiwancheng.setVisibility(View.VISIBLE);
                                    tv_name.setTextColor(getResources().getColor(R.color.homepage_title_right_text_colors));
                                }

                                @Override
                                public void onFailure(String status, String message) {
                                    hideWaitingUI();
                                    AppContext.showToast(message);
                                }
                            });
                        }
                    });
                }
                if(item.getJobState()==2) {//已领取
                    iv_lingqu.setVisibility(View.GONE);
                    iv_weiwancheng.setVisibility(View.GONE);
                    iv_yiwancheng.setVisibility(View.VISIBLE);
                    tv_name.setTextColor(getResources().getColor(R.color.homepage_title_right_text_colors));
                }
            }
        };
        ll_more_fund.setAdapter(mMessageAdapter);

        tv_today_fund.setText(fundBean.getTodayScore()+"");
        tv_sum_fund.setText(fundBean.getMyScore()+"");

    }
    @Override
    protected void initViewContent() {

    }
    private Dialog dialog;
    private TextView tv_kind;
    private TextView tv_score;
    private TextView bt_dismiss;
    private void showDialog(String kind,String score) {
        // TODO Auto-generated method stub
        dialog = new Dialog(MyFundActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setContentView(R.layout.dialog_toast);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.height = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(attributes);
        dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        dialog.setCanceledOnTouchOutside(false);
        tv_kind = (TextView) dialog
                .findViewById(R.id.tv_kind);
        tv_score = (TextView) dialog
                .findViewById(R.id.tv_score);
        tv_kind.setText(tv_kind
                .getText() +kind);
        tv_score.setText("+" +score+"分");

        bt_dismiss = (TextView) dialog
                .findViewById(R.id.bt_dismiss);

        bt_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
