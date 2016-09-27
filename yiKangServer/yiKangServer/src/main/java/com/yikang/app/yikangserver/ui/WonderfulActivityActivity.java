package com.yikang.app.yikangserver.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.WonderfulActivity;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的活动界面
 */
public class WonderfulActivityActivity extends BaseActivity implements View.OnClickListener{
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<WonderfulActivity> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    public final static String MESSAGE_INFOS="messageinfo";
    private String messageinfo;
    List<WonderfulActivity> wonderfulActivity;
    private ArrayList<WonderfulActivity> Comlables = new ArrayList<WonderfulActivity>();
    private DisplayImageOptions option;
    private TextView tv_title_right_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new FadeInBitmapDisplayer(500))
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        mHandler = new Handler();
        initContent();
        messageinfo=getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);
    }
    //获取加载数据
    private void geneItems() {
        for (int i = 0; i < wonderfulActivity.size(); ++i) {
            WonderfulActivity lable = new WonderfulActivity();
            lable.setContent(wonderfulActivity.get(i).getTitle());
            lable.setMapPsitionAddress(wonderfulActivity.get(i).getMapPsitionAddress());
            lable.setPersonNumber(wonderfulActivity.get(i).getPartakeNums());
            lable.setRecommendPicUrl(wonderfulActivity.get(i).getRecommendPicUrl());
            lable.setCost(wonderfulActivity.get(i).getCost());
            lable.setCreateUsername(wonderfulActivity.get(i).getCreateUsername());
            lable.setPhotoUrl(wonderfulActivity.get(i).getPhotoUrl());
            lable.setCreateUserId(wonderfulActivity.get(i).getCreateUserId());
            Comlables.add(lable);
        }
    }
    @Override
    protected void findViews() {
        tv_title_right_text =(TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("我要发活动");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(WonderfulActivityActivity.this, LoginActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    if (AppContext.getAppContext().getUser() != null) {
                        if (AppContext.getAppContext().getUser().profession > 0) {
                            publishActivity();
                        } else {
                            showConfigDialog();

                        }

                    }

                }
            }
        });
        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter=new CommonAdapter<WonderfulActivity>(this,Comlables,R.layout.list_community_details_item) {
            @Override
            protected void convert(ViewHolder holder, final WonderfulActivity item) {
                ImageView iv_actvivty_pictrue = holder.getView(R.id.iv_actvivty_pictrue);
                TextView tv_activity_content = holder.getView(R.id.tv_activity_content);
                CircleImageView iv_activity_user_photo = holder.getView(R.id.iv_activity_uaser_photo);
                TextView tv_activity_user_name = holder.getView(R.id.tv_activity_user_name);
                TextView tv_activity_address = holder.getView(R.id.tv_activity_address);
                TextView tv_activity_nums = holder.getView(R.id.tv_activity_nums);
                TextView tv_activity_content_cost = holder.getView(R.id.tv_activity_content_cost);
                tv_activity_content.setText(item.getContent());
                tv_activity_user_name.setText(item.getCreateUsername());
                if(item.getMapPsitionAddress().length()>=5){
                    tv_activity_address.setText(item.getMapPsitionAddress().substring(0,4));
                }else{
                    tv_activity_address.setText(item.getMapPsitionAddress());
                }
                tv_activity_nums.setText(item.getPersonNumber() + "");

                if(!(item.getCost()+"").equals("")) {
                    tv_activity_content_cost.setText("￥" + item.getCost() + "");
                    tv_activity_content_cost.setTextColor(getApplication().getResources().getColor(R.color.red));
                }
                if((item.getCost()+"").equals("0.0")){
                    tv_activity_content_cost.setText("免费");
                    tv_activity_content_cost.setTextColor(getApplication().getResources().getColor(R.color.community_activity_free_color));
                }
                ImageLoader.getInstance().displayImage(item.getRecommendPicUrl(), iv_actvivty_pictrue,option);
                ImageLoader.getInstance().displayImage(item.getPhotoUrl(), iv_activity_user_photo);
                iv_activity_user_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toCustomerPersonnalPage(item.getCreateUserId()+"");
                    }
                });

            }
        };
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),
                        ComActivitiesActivity.class);
                intent.putExtra("ACTIVITYID", wonderfulActivity.get(position - 1).getActivetyId() + "");
                startActivity(intent);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });

        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Comlables != null && wonderfulActivity != null) {
                            Comlables.clear();
                            start = ++refreshCnt;
                            getData();
                            message_xlistview.setAdapter(mMessageAdapter);
                            onLoad();
                        }
                    }
                }, 1000);
            }
            private void onLoad() {
                message_xlistview.stopRefresh();
                message_xlistview.stopLoadMore();
                message_xlistview.setRefreshTime("刚刚");
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        geneItems();
                        mMessageAdapter.notifyDataSetChanged();
                        onLoad();
                    }
                }, 1000);
            }

            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }

        });
    }

    /**
     * 发布活动
     */
    private void publishActivity() {
        Intent intent4 = new Intent(getApplicationContext(),
                PublishActivityActivity.class);
        intent4.putExtra(PublishActivityActivity.MESSAGE_INFOS, "活动形式");
        startActivity(intent4);
      overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


    /**
     * 进入个人主页
     */
    private void toCustomerPersonnalPage(String id) {
        Intent intent = new Intent(getApplicationContext(),
                PersonalHomepageActivity.class);
        intent.putExtra("userId",id);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        //获取首页活动
        Api.getWonderfulActivity(wonderfulActivityHandler);
    }

    /**
     * 活动的回调
     */
    private ResponseCallback<List<WonderfulActivity>> wonderfulActivityHandler = new ResponseCallback<List<WonderfulActivity>>() {



        @Override
        public void onSuccess(List<WonderfulActivity> data) {
            hideWaitingUI();
           // LOG.i("debug", "HpWonderfulContent---->" + data);
            wonderfulActivity = new ArrayList<>();
            for (WonderfulActivity hp : data) {
                wonderfulActivity.add(hp);
            }
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void initViewContent() {}
    private TextView tv_next,tv_goto;
    private Dialog dialog;

    private void showConfigDialog() {
        // TODO Auto-generated method stub
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setContentView(R.layout.dialog_config);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.allradius_white);
        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.height = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(attributes);

        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_goto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MineInfoActivity.class);
                intent.putExtra(MineInfoActivity.EXTRA_USER,  AppContext.getAppContext().getUser());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
        }
    }
}
