package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统消息界面
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener{
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    public final static String MESSAGE_INFO="messageinfo";
    private String messageinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mHandler = new Handler();
        geneItems();
		initContent();
        messageinfo=getIntent().getStringExtra(MESSAGE_INFO);
        initTitleBar(messageinfo);
	}
    protected void geneItems() {
        for (int i = 0; i < 10; i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName("佳佳官方通知"+i + "");
            lable.setHeadTvLable("运动员  " + i);
            lable.setReleaseTime("2016/5/19 20:0" + i);
            lables.add(lable);
        }
    }
	@Override
	protected void findViews() {

        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(true);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(this,lables,R.layout.list_lables_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                //TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                //TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                lables_tv_title.setText(item.getHeadTvName());
                //lables_tv_tiezi.setText(item.getHeadTvLable());
                //lables_tv_time.setText(item.getReleaseTime());
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
	protected void getData() {}
	@Override
	protected void initViewContent() {}

    @Override
    public void onClick(View view) {

        switch (view.getId()){
        }
    }
}
