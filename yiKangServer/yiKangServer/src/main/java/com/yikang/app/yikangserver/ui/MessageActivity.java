package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.Message;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统消息界面
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener{
    private Handler mHandler;
    private int start = 1;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    public final static String MESSAGE_INFO="messageinfo";
    private String messageinfo;
    List<Message> wonderfulActivity;
    private ArrayList<Message> Comlables = new ArrayList<Message>();
    private LinearLayout ll_no_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        initContent();
        messageinfo=getIntent().getStringExtra(MESSAGE_INFO);
        initTitleBar(messageinfo);
    }
    protected void geneItems() {
        for (int i = 0; i < wonderfulActivity.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            if(wonderfulActivity.get(i).getContentGroup()==10){//佳佳官方通知
                lable.setHeadTvName("佳佳官方通知 "+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");


            }
            if(wonderfulActivity.get(i).getContentGroup()==11){//佳佳产品团队
                lable.setHeadTvName("佳佳产品团队"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }

            lable.setToquestionId(wonderfulActivity.get(i).getMessagesId());
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(),wonderfulActivity.get(i).getCreateTime()));
            lable.setIsStar(wonderfulActivity.get(i).getContentGroup());
            lable.setCreateUserPosition(wonderfulActivity.get(i).getIsRead());
            lables.add(lable);
        }
    }
    @Override
    protected void findViews() {

        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        ll_no_message=(LinearLayout) findViewById(R.id.ll_no_message);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(this,lables,R.layout.list_lables_system_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                com.yikang.app.yikangserver.view.CircleImageView iv_mine_avatar = holder.getView(R.id.iv_mine_avatar);
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_tv_tiezi);
                TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_tiezi.setText(item.getHeadTvLable());
                lables_tv_time.setText(item.getReleaseTime());
                if(item.getCreateUserPosition()==0){
                    ll_item.setBackgroundResource(R.color.common_blue);
                }
                if(item.getCreateUserPosition()==1){
                    ll_item.setBackgroundResource(R.color.white);
                }
                if(item.getIsStar()==10){//官方通知
                    iv_mine_avatar.setBackgroundResource(R.drawable.guanfang);
                }
                if(item.getIsStar()==11){//产品团队
                    iv_mine_avatar.setBackgroundResource(R.drawable.chanpin);
                }
            }
        };
        message_xlistview.setAdapter(mMessageAdapter);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                //AppContext.showToast(wonderfulActivity.get(position-1).getTitle()+wonderfulActivity.get(position-1).getDataId());
                Api.changeMessageState(lables.get(position - 1).getToquestionId(), new ResponseCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        lables.get(position - 1).setCreateUserPosition(1);
                        mMessageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String status, String message) {

                    }
                });
            }
        });
        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                start = 1;
                Api.getSystemMessage(1,systemMessageHandler);
            }


            @Override
            public void onLoadMore() {
                start += 1;
                getDataMore();
            }
            protected void getDataMore() {

                //获取标签下的所有专业内容
                Api.getSystemMessage( start, allLableContentHandlers);
            }
            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }
            private ResponseCallback<List<Message>> allLableContentHandlers = new ResponseCallback<List<Message>>() {

                @Override
                public void onSuccess(List<Message> data) {
                    hideWaitingUI();
                    if(data.size()<10){
                        message_xlistview.setPullLoadEnable(false);
                    }else {
                        message_xlistview.setPullLoadEnable(true);
                    }
                    onLoad();

                    wonderfulActivity = new ArrayList<>();
                    for (Message hp : data) {
                        wonderfulActivity.add(hp);
                    }
                    geneItems();
                    mMessageAdapter.notifyDataSetChanged();
                    // LOG.i("debug", "HpWonderfulContent---->" + data + "嘎嘎嘎");

                }

                @Override
                public void onFailure(String status, String message) {
                    // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                    hideWaitingUI();
                    AppContext.showToast(message);
                }
            };


        });
    }

    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        Api.getSystemMessage(1,systemMessageHandler);
    }
    private ResponseCallback<List<Message>> systemMessageHandler = new ResponseCallback<List<Message>>() {



        @Override
        public void onSuccess(List<Message> data) {
            hideWaitingUI();
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            if(data.size()==0){
                ll_no_message.setVisibility(View.VISIBLE);
            }
            //LOG.i("debug", "HpWonderfulContent---->" + data);
            wonderfulActivity = new ArrayList<>();
            for (Message hp : data) {
                wonderfulActivity.add(hp);
            }
            lables.clear();
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
            onLoad();
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){
        }
    }
}
