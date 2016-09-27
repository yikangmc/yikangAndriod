package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.Taglibs;
import com.yikang.app.yikangserver.ui.LableDetailsActivity;
import com.yikang.app.yikangserver.ui.LablesActivity;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东on 2016/4/26.
 * 社区关注模块下的标签页面
 */
public class CommunityFocusonDetailLableFragments extends BaseFragment implements View.OnClickListener {
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private View lable_footer_views;
    private CommonAdapter<Taglibs> mMessageAdapter;
    private ArrayList<Taglibs> lables = new ArrayList<Taglibs>();
    private ArrayList<Taglibs> labless = new ArrayList<Taglibs>();
    private XListView message_xlistview;
    private String titlename;
    private LinearLayout lable_footer_view;
    int position;
    private DisplayImageOptions options; //配置图片加载及显示选项
    public CommunityFocusonDetailLableFragments(String titlename, ArrayList<Taglibs> lables) {
        this.titlename = titlename;
        this.labless = lables;
    }
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

    public CommunityFocusonDetailLableFragments(String name) {
        this.titlename = name;
    }

    public CommunityFocusonDetailLableFragments() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        getData();
        View rootView = inflater.inflate(R.layout.fragment_community_focuson_professional, container, false);
        lable_footer_views=inflater.inflate(R.layout.lable_footer, null);

        findViewss(lable_footer_views);
        findView(rootView);
        return rootView;
    }

    private void getData() {
        if (AppContext.getAppContext().getAccessTicket() != null) {
            //得到我关注的标签
            Api.getAllMyFocusLables(allMyFocusPersonsHandler);
        }
    }

    private void findViewss(View lvFooterView) {
        lable_footer_view = (LinearLayout) lvFooterView.findViewById(R.id.lable_footer_view);
        lable_footer_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AppContext.showToast("哈哈");
                Intent intent = new Intent(getActivity(),
                        LablesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);

            }
        });
    }
    private void findView(View rootView) {
        message_xlistview = (XListView) rootView.findViewById(R.id.message_xlistview);
        message_xlistview.addFooterView(lable_footer_views);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter = new CommonAdapter<Taglibs>(getActivity(), labless, R.layout.list_lables_lables_item) {
            @Override
            protected void convert(ViewHolder holder, final Taglibs item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                ImageView iv_mine_avatar = holder.getView(R.id.iv_mine_avatar);
                lables_tv_title.setText(item.getTagName());
                ImageLoader.getInstance().displayImage(item.getTagPic(),iv_mine_avatar,options);
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
                        showWaitingUI();
                        position = (Integer) v.getTag();
                        Api.deleteMyFocusLables(labless.get(position).getTaglibId(),deleteMyFocusLablesHadler);

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
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE, item.getTagName());
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ID,item.getTaglibId() + "");
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE,1+ "");
                        //intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, 1);
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
                getData();

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
     * 获取关注标签的回调
     */
    private ResponseCallback<List<Taglibs>> allMyFocusPersonsHandler = new ResponseCallback<List<Taglibs>>() {

        @Override
        public void onSuccess(List<Taglibs> data) {

            start = ++refreshCnt;
            lables.clear();
            labless.clear();
            onLoad();

            hideWaitingUI();
            for (Taglibs hp : data) {
                lables.add(hp);
            }
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
        }

        private void onLoad() {
            message_xlistview.stopRefresh();
            message_xlistview.stopLoadMore();
            message_xlistview.setRefreshTime("刚刚");
        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    private ResponseCallback<String> deleteMyFocusLablesHadler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {

            hideWaitingUI();
            labless.remove(position);
            mMessageAdapter.notifyDataSetChanged();//刷新listView
        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    protected void geneItems() {
        for (int i = 0; i < lables.size(); i++) {
            Taglibs lable = new Taglibs();
            lable.setTagName(lables.get(i).getTagName());
            lable.setTagPic(lables.get(i).getTagPic());
            lable.setTaglibId(lables.get(i).getTaglibId());//标签号
            labless.add(lable);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

}
