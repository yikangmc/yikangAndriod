package com.yikang.app.yikangserver.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;

/**
 * Created by yudong on 2016/4/20.
 * 社区关注模块下的收藏页面
 */
public class CommunityFocusonDetailCollectionFragment extends BaseFragment implements View.OnClickListener{

    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;


    private String titlename;
    private SwipeView openedSwipeView;

    public SwipeView.OnSwipeChangeListener onSwipeChangeListener = new SwipeView.OnSwipeChangeListener() {

        @Override
        public void onOpen(SwipeView swipeView) {
            //保存swipView
            openedSwipeView = swipeView;
        }

        @Override
        public boolean onDown(SwipeView swipeView) {
            if (openedSwipeView != null && openedSwipeView != swipeView) {
                openedSwipeView.close();
                return false;
            }
            return true;
        }

        @Override
        public void onClose(SwipeView swipeView) {
            if (openedSwipeView != null && openedSwipeView == swipeView) {
                openedSwipeView = null;
            }
        }
    };
    public CommunityFocusonDetailCollectionFragment(String name){
        this.titlename=name;
    }
    public CommunityFocusonDetailCollectionFragment(){
       
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        geneItems();
        View rootView = inflater.inflate(R.layout.fragment_community_focuson_professional, container, false);
        findView(rootView);
        return rootView;
    }
    private void findView(View rootView) {
        message_xlistview=(XListView)rootView.findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(true);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(getActivity(),lables,R.layout.list_lables_collection_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                //TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                //TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                lables_tv_title.setText(item.getHeadTvName());
                //lables_tv_tiezi.setText(item.getHeadTvLable());
                //lables_tv_time.setText(item.getReleaseTime());

                TextView tv_delete = (TextView) holder.getConvertView().findViewById(R.id.tv_delete);
                tv_delete.setTag(holder.getPosition()); //保存下标
                LinearLayout ll_content = (LinearLayout) holder.getConvertView().findViewById(R.id.ll_content);

                SwipeView swipeView = (SwipeView) holder.getConvertView();
                swipeView.setOnSwipeChangeListener(onSwipeChangeListener);
                /*
                    进行删除操作
                 */
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        lables.remove(position);
                        mMessageAdapter.notifyDataSetChanged();//刷新listView
                    }
                });

                /*
                使滑动出来的列表缩回
                 */
                ll_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (openedSwipeView != null) {
                            openedSwipeView.close();
                        }
                    }
                });
            }
        };
        message_xlistview.setAdapter(mMessageAdapter);
        //设置滚动的监听
        message_xlistview.setOnScrollListener(new AbsListView.OnScrollListener() {

            //当滚动状态改变时调用

            /**
             * SCROLL_STATE_TOUCH_SCROLL : 滚动
             * SCROLL_STATE_IDLE : 初始化(没有滚动)
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {//开始滚动
                    if (openedSwipeView != null) {
                        openedSwipeView.close();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

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
    protected void geneItems() {
        for (int i = 0; i < 10; i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName("如何让女生快乐的健身？"+i + "");
            lable.setHeadTvLable("运动员  " + i);
            lable.setReleaseTime("2016/5/19 20:0" + i);
            lables.add(lable);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
