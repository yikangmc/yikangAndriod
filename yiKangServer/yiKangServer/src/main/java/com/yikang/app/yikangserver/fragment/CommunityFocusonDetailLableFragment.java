package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.Taglibs;
import com.yikang.app.yikangserver.ui.LableDetailsActivity;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东on 2016/4/26.
 * 社区关注模块下的标签页面
 */
public class CommunityFocusonDetailLableFragment extends BaseFragment implements View.OnClickListener {
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<Taglibs> mMessageAdapter;
    private ArrayList<Taglibs> lables = new ArrayList<Taglibs>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;


    private String titlename;
    private SwipeView openedSwipeView;
    private int position;
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

    public CommunityFocusonDetailLableFragment(String name) {
        this.titlename = name;
    }

    public CommunityFocusonDetailLableFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getData();
    }

    private void getData() {
        showWaitingUI();
        //得到我关注的标签
        Api.getAllMyFocusLables(allMyFocusPersonsHandler);
    }

    /**
     * 获取关注标签的回调
     */
    private ResponseCallback<List<Taglibs>> allMyFocusPersonsHandler = new ResponseCallback<List<Taglibs>>() {

        @Override
        public void onSuccess(List<Taglibs> data) {

            hideWaitingUI();
            for (Taglibs hp : data) {
                lables.add(hp);
            }
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };

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
        message_xlistview = (XListView) rootView.findViewById(R.id.message_xlistview);

        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter = new CommonAdapter<Taglibs>(getActivity(), lables, R.layout.list_lables_lables_item) {
            @Override
            protected void convert(ViewHolder holder, Taglibs item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
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
                         position = (Integer) v.getTag();
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
                            return;
                        }
                        Intent intent = new Intent(getActivity(), LableDetailsActivity.class);
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE, lables.get(position-1).getTagName());
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ID,lables.get(position-1).getTaglibId() + "");
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, 1);
                        getActivity().startActivity(intent);

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
        for (int i = 0; i < lables.size(); i++) {
            Taglibs lable = new Taglibs();
            lable.setTagName("小儿脑瘫" + i + "");

            //lable.setReleaseTime("2016/5/19 20:0" + i);
            lables.add(lable);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

}
