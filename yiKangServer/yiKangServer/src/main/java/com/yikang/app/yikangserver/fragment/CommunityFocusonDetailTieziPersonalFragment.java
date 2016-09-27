package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.yikang.app.yikangserver.bean.ForumPostsImage;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.Taglibs;
import com.yikang.app.yikangserver.photo.PhotoActivitys;
import com.yikang.app.yikangserver.ui.ContentDetailsActivity;
import com.yikang.app.yikangserver.ui.ProfessionalAnwserActivity;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/4/26.
 * 社区关注模块下的收藏页面
 */
public class CommunityFocusonDetailTieziPersonalFragment extends BaseFragment implements View.OnClickListener{
    private Handler mHandler;
    private int start = 1;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private ArrayList<HpWonderfulContent> collections = new ArrayList<HpWonderfulContent>();
    private View lable_footer_view;
    private LinearLayout lable_footer_views;
    public CommunityFocusonDetailTieziPersonalFragment(String titlename, ArrayList<HpWonderfulContent> collections) {
        this.titlename = titlename;
        this.collections = collections;
    }
    private DisplayImageOptions options; //配置图片加载及显示选项
    private CommonAdapter<ForumPostsImage> alllableEditorCAdapter;
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
    public CommunityFocusonDetailTieziPersonalFragment(String name){
        this.titlename=name;
    }
    public CommunityFocusonDetailTieziPersonalFragment(){
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                //.displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        if(collections.size()!=0) {
            geneItems();
        }else {
            getData();
        }
        View rootView = inflater.inflate(R.layout.fragment_community_focuson_professional, container, false);
        //lable_footer_view=inflater.inflate(R.layout.collection_footer, null);
        findView(rootView);
        //findViewss(lable_footer_view);
        return rootView;
    }
    private void findViewss(View lvFooterView) {
        lable_footer_views = (LinearLayout) lvFooterView.findViewById(R.id.collection_footer_view);
        lable_footer_views.setOnClickListener(new View.OnClickListener() {
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
        //message_xlistview.addFooterView(lable_footer_view);
        if(collections.size()<10){
            message_xlistview.setPullLoadEnable(false);
        }else {
            message_xlistview.setPullLoadEnable(true);
        }
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(getActivity(),lables,R.layout.list_lable_my_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                // TextView lables_tv_title = holder.getView(R.id.lables_details_tv_name);
                TextView lables_details_tv_title = holder.getView(R.id.lables_details_tv_title);
                // TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                TextView lable_details_tv_support = holder.getView(R.id.homepage_wonderfulcontent_support);
                TextView lable_details_tv__comment = holder.getView(R.id.homepage_wonderfulcontent_comment);
                TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                GridView community_mine_lables_gridview = holder.getView(R.id.community_mine_lables_gridview);
                TextView tv_tag1 = (TextView) holder.getView(R.id.tv_tag1);
                TextView tv_tag2 = (TextView) holder.getView(R.id.tv_tag2);
                TextView tv_tag3 = (TextView) holder.getView(R.id.tv_tag3);
                TextView tv_tag4 = (TextView) holder.getView(R.id.tv_tag4);
                lables_details_tv_title.setText(item.getDetailTv());
                lables_tv_time.setText(item.getReleaseTime());
                lable_details_tv__comment.setText(item.getAnswersNums()+"");
                lable_details_tv_support.setText(item.getDetailSupport()+"");
                homepage_ll_wonderfulcontent_text.setText(item.getDetailLable());
                final List<ForumPostsImage> detailIvUrls = item.getDetailIvUrls();
                List<Taglibs> detailTaglibs = item.getDetailTaglibs();

                alllableEditorCAdapter =new CommonAdapter<ForumPostsImage>(getActivity(),detailIvUrls,R.layout.pic_layout) {
                    @Override
                    protected void convert(ViewHolder holder, final ForumPostsImage item) {
                        ImageView lable =(ImageView)holder.getView(R.id.lables_top_textview);
                        ImageLoader.getInstance().displayImage(item.getImageUrl(), lable,options);
                    }
                };

                community_mine_lables_gridview.setAdapter(alllableEditorCAdapter);
                community_mine_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(),
                                PhotoActivitys.class);

                        intent.putExtra("URL", (Serializable) detailIvUrls);
                        intent.putExtra("ID",position);

                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                if (detailTaglibs != null && detailTaglibs.size() == 1) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                }
                if (detailTaglibs != null && detailTaglibs.size() == 2) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(detailTaglibs.get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                }
                if (detailTaglibs != null && detailTaglibs.size() == 3) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(detailTaglibs.get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                    tv_tag3.setText(detailTaglibs.get(2).getTagName());
                    tv_tag3.setBackgroundResource(R.color.main_background_color);
                }
                if (detailTaglibs != null && detailTaglibs.size() == 4) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(detailTaglibs.get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                    tv_tag3.setText(detailTaglibs.get(2).getTagName());
                    tv_tag3.setBackgroundResource(R.color.main_background_color);
                    tv_tag4.setText(detailTaglibs.get(3).getTagName());
                    tv_tag4.setBackgroundResource(R.color.main_background_color);
                }
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
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ContentDetailsActivity.class);
                if (collections != null) {
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTID, collections.get(i - 1).getForumPostId() + "");
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, collections.get(i - 1).getIsStore() + "");
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENT,"");
                }
                startActivity(intent);
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

                Api.getMyTiezi(Long.parseLong(titlename + ""), start,allLableContentHandlers);
            }

            private ResponseCallback<List<HpWonderfulContent>> allLableContentHandlers = new ResponseCallback<List<HpWonderfulContent>>() {

                @Override
                public void onSuccess(List<HpWonderfulContent> data) {

                    if(data.size()<10){
                        message_xlistview.setPullLoadEnable(false);
                    }else {
                        message_xlistview.setPullLoadEnable(true);
                    }
                    onLoad();
                    hideWaitingUI();
                    collections = new ArrayList<>();
                    for (HpWonderfulContent hp : data) {
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

    private void getData() {
        Api.getMyTiezi(Long.parseLong(titlename + ""), 1,allLableContentHandler);
    }
    /**
     * 获取关注收藏的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {

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
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(collections.get(i).getUserName());
            lable.setHeadTvLable("路人甲");
            lable.setDetailTv(collections.get(i).getTitle());
            lable.setDetailLable(collections.get(i).getContent());
            lable.setDetailSupport(collections.get(i).getStars() + "");
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(), collections.get(i).getCreateTime()));
            lable.setAnswersNums(collections.get(i).getAnswersNums() + "");
            lable.setDetailIvUrls(collections.get(i).getForumPostsImage());
            lable.setDetailTaglibs(collections.get(i).getTaglibs());
            lables.add(lable);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
