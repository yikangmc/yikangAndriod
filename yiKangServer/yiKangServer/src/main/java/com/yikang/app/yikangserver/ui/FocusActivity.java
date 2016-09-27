package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;

/**
 * 关注者界面
 */
public class FocusActivity extends BaseActivity implements View.OnClickListener{
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;

    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();

    private XListView message_xlistview;

    private String messageinfo;
    private TextView lables_tv_already_focus,lables_tv_focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageinfo=getIntent().getStringExtra("serverId");
        mHandler = new Handler();
        geneItems();
        initContent();

        initTitleBar("关注者");
    }


    protected void geneItems() {
        for (int i = 0; i < 10; i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName("路人"+i + "");
            lable.setHeadTvLable("运动员  " + i);
            lable.setReleaseTime("2016/5/19 20:0" + i);
            lables.add(lable);
        }
    }
    @Override
    protected void findViews() {
        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(true);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(this,lables,R.layout.item_list_focus) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                 lables_tv_already_focus = holder.getView(R.id.lables_tv_already_focus);
                 lables_tv_focus = holder.getView(R.id.lables_tv_focus);
                lables_tv_title.setText(item.getHeadTvName());

            }
        };
        message_xlistview.setAdapter(mMessageAdapter);
        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start = ++refreshCnt;
                        lables.clear();
                       geneItems();
                        message_xlistview.setAdapter(mMessageAdapter);
                        onLoad();
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


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    protected void getData() {
        Api.getServerFocus(Integer.parseInt(messageinfo),getServerFocusHandler);
    }
    private ResponseCallback<User> getServerFocusHandler = new ResponseCallback<User>() {



        @Override
        public void onSuccess(User data) {
            hideWaitingUI();
           // LOG.i("debug", "HpWonderfulContent---->" + data);
            //user=data;
           // initContens();
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
