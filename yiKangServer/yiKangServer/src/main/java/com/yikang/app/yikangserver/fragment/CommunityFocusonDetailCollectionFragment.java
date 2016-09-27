package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.ui.LableDetaileContentActivity;
import com.yikang.app.yikangserver.ui.ProfessionalAnwserActivity;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/4/26.
 * 社区关注模块下的收藏页面
 */
public class CommunityFocusonDetailCollectionFragment extends BaseFragment implements View.OnClickListener{
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private View lable_footer_view;
    private LinearLayout collection_footer_view;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private ArrayList<HpWonderfulContent> collections = new ArrayList<HpWonderfulContent>();

    public CommunityFocusonDetailCollectionFragment(String titlename, ArrayList<HpWonderfulContent> collections) {
        this.titlename = titlename;
        this.collections = collections;
    }

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
    public CommunityFocusonDetailCollectionFragment(String name){
        this.titlename=name;
    }
    public CommunityFocusonDetailCollectionFragment(){
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        getData();
        View rootView = inflater.inflate(R.layout.fragment_community_focuson_professional, container, false);
        lable_footer_view=inflater.inflate(R.layout.collection_footer, null);
        findView(rootView);
        findViewss(lable_footer_view);
        return rootView;
    }

    private void getData() {
        if (AppContext.getAppContext().getAccessTicket() != null) {
            Api.getAllMyFocusCollection(allMyFocusCollectionHandler);
        }
    }
    /**
     * 获取关注收藏的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allMyFocusCollectionHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {

            hideWaitingUI();

            start = ++refreshCnt;
            lables.clear();
            collections.clear();
            onLoad();

            hideWaitingUI();
            for (HpWonderfulContent hp : data) {
                collections.add(hp);
            }
            if(collections!=null) {
                geneItems();
            }
            message_xlistview.setAdapter(mMessageAdapter);

        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }
    private void findViewss(View lvFooterView) {
        collection_footer_view = (LinearLayout) lvFooterView.findViewById(R.id.collection_footer_view);

        collection_footer_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AppContext.showToast("哈哈");
                Intent intent = new Intent(getActivity(),
                        ProfessionalAnwserActivity.class);
                intent.putExtra("fromCommunityFind", 1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    private void findView(View rootView) {
        message_xlistview=(XListView)rootView.findViewById(R.id.message_xlistview);
        message_xlistview.addFooterView(lable_footer_view);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(getActivity(),lables,R.layout.list_lables_collection_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_title_id = holder.getView(R.id.lables_tv_title_id);
                ImageView iv_mine_avatar = holder.getView(R.id.iv_mine_avatar);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_title_id.setText(item.getHeadTvLable());
                ImageLoader.getInstance().displayImage(item.getReleaseTime(),iv_mine_avatar);
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

                        Api.adeleteMyFocusCollections(collections.get(position).getForumPostId(),deleteMyFocusCollectionsHadler);

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

                        Intent intent = new Intent(getActivity(), LableDetaileContentActivity.class);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT, item.getHeadTvLable()+"");
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, item.getTaglibId() + "");
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


            @Override
            public void onLoadMore() {
               /* mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        geneItems();
                        mMessageAdapter.notifyDataSetChanged();
                        onLoad();
                    }
                }, 1000);*/
            }

            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }

        });


    }

    private ResponseCallback<String> deleteMyFocusCollectionsHadler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {

            hideWaitingUI();
            lables.remove(position);
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
        for (int i = 0; i < collections.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            if(collections.get(i).getFormPosts()!=null) {
                lable.setHeadTvName(collections.get(i).getFormPosts().getTitle() + "");
                lable.setHeadTvLable(collections.get(i).getFormPosts().getUserName() + "");
                lable.setReleaseTime(collections.get(i).getFormPosts().getRecommendPicUrl() + "");
                lable.setTaglibId(collections.get(i).getForumPostId());
            }
            lables.add(lable);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
