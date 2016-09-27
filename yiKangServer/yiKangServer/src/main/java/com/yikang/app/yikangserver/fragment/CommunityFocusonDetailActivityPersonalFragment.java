package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yikang.app.yikangserver.ui.ComActivitiesActivity;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/4/26.
 * 社区关注模块下的收藏页面
 */
public class CommunityFocusonDetailActivityPersonalFragment extends BaseFragment implements View.OnClickListener{
    private Handler mHandler;
    private int start = 1;
    private static int refreshCnt = 0;
    private CommonAdapter<WonderfulActivity> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private ArrayList<WonderfulActivity> collections = new ArrayList<WonderfulActivity>();
    private View lable_footer_view;
    private LinearLayout lable_footer_views;
    private ArrayList<WonderfulActivity> Comlables = new ArrayList<WonderfulActivity>();
    public CommunityFocusonDetailActivityPersonalFragment(String titlename, ArrayList<WonderfulActivity> collections) {
        this.titlename = titlename;
        this.collections = collections;
    }

    private XListView message_xlistview;
    private String titlename;
    private SwipeView openedSwipeView;
    private int position;
    private DisplayImageOptions option;
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
    public CommunityFocusonDetailActivityPersonalFragment(String name){
        this.titlename=name;
    }
    public CommunityFocusonDetailActivityPersonalFragment(){
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new FadeInBitmapDisplayer(500))
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        if(collections.size()!=0) {
            geneItems();
        }else {
            getData();
        }

        View rootView = inflater.inflate(R.layout.fragment_community_focuson_professional, container, false);

        findView(rootView);

        return rootView;
    }

    private void getData() {
        Api.getAllActiviysPersonPublish(Long.parseLong(titlename+""),1,allMyFocusCollectionHandler);
    }
    /**
     * 获取关注收藏的回调
     */
    private ResponseCallback<List<WonderfulActivity>> allMyFocusCollectionHandler = new ResponseCallback<List<WonderfulActivity>>() {

        @Override
        public void onSuccess(List<WonderfulActivity> data) {

            hideWaitingUI();
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            start=1;
            onLoad();
            Comlables.clear();
            collections.clear();
            hideWaitingUI();
            for (WonderfulActivity hp : data) {
                collections.add(hp);
            }
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);

        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    private void findView(View rootView) {
        message_xlistview=(XListView)rootView.findViewById(R.id.message_xlistview);
        if(collections.size()<10){
            message_xlistview.setPullLoadEnable(false);
        }else {
            message_xlistview.setPullLoadEnable(true);
        }
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),
                        ComActivitiesActivity.class);
                intent.putExtra("ACTIVITYID", Comlables.get(position - 1).getActivetyId()+ "");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        mMessageAdapter=new CommonAdapter<WonderfulActivity>(getActivity(),Comlables,R.layout.list_community_details_item) {
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
                    tv_activity_content_cost.setTextColor(getActivity().getResources().getColor(R.color.red));
                }
                if((item.getCost()+"").equals("0.0")){
                    tv_activity_content_cost.setText("免费");
                    tv_activity_content_cost.setTextColor(getActivity().getResources().getColor(R.color.community_activity_free_color));
                }
                ImageLoader.getInstance().displayImage(item.getRecommendPicUrl(), iv_actvivty_pictrue,option);
                ImageLoader.getInstance().displayImage(item.getPhotoUrl(), iv_activity_user_photo);
                iv_activity_user_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //toCustomerPersonnalPage();
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
                start +=1;
                getDataMore();
            }
            protected void getDataMore() {

                Api.getAllActiviysPersonPublish(Long.parseLong(titlename+""),start,allLableContentsHandlers);
            }
            private ResponseCallback<List<WonderfulActivity>> allLableContentsHandlers = new ResponseCallback<List<WonderfulActivity>>() {

                @Override
                public void onSuccess(List<WonderfulActivity> data) {

                    if(data.size()<10){
                        message_xlistview.setPullLoadEnable(false);
                    }else {
                        message_xlistview.setPullLoadEnable(true);
                    }
                    onLoad();
                    hideWaitingUI();
                    collections = new ArrayList<>();
                    for (WonderfulActivity hp : data) {
                        collections.add(hp);
                    }
                    geneItems();
                    mMessageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String status, String message) {
                    //  LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                    hideWaitingUI();
                    AppContext.showToast(message);
                }
            };
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
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    protected void geneItems() {
        for (int i = 0; i < collections.size(); ++i) {
            WonderfulActivity lable = new WonderfulActivity();
            lable.setContent(collections.get(i).getTitle());
            lable.setMapPsitionAddress(collections.get(i).getMapPsitionAddress());
            lable.setPersonNumber(collections.get(i).getPartakeNums());
            lable.setRecommendPicUrl(collections.get(i).getRecommendPicUrl());
            lable.setCost(collections.get(i).getCost());
            lable.setCreateUsername(collections.get(i).getCreateUsername());
            lable.setPhotoUrl(collections.get(i).getPhotoUrl());
            lable.setActivetyId(collections.get(i).getActivetyId());
            Comlables.add(lable);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
