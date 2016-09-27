package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/4/26.
 * 社区关注模块下的收藏页面
 */
public class CommunityFocusonDetailCollectionPersonalFragment extends BaseFragment implements View.OnClickListener{
    private Handler mHandler;
    private int start = 1;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private ArrayList<HpWonderfulContent> collections = new ArrayList<HpWonderfulContent>();
    private View lable_footer_view;
    private LinearLayout lable_footer_views;
    public CommunityFocusonDetailCollectionPersonalFragment(String titlename, ArrayList<HpWonderfulContent> collections) {
        this.titlename = titlename;
        this.collections = collections;
    }
    private DisplayImageOptions options;
    private XListView message_xlistview;
    private String titlename;
    private SwipeView openedSwipeView;
    private int position;

    public CommunityFocusonDetailCollectionPersonalFragment(String name){
        this.titlename=name;
    }
    public CommunityFocusonDetailCollectionPersonalFragment(){
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
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
        start=1;
        Api.getMyContents(Long.parseLong(titlename + ""), 1,allMyFocusCollectionHandler);
    }
    /**
     * 获取关注收藏的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allMyFocusCollectionHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {

            hideWaitingUI();
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(false);
            }
            start=1;
            onLoad();
            lables.clear();
            collections.clear();
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
    @Override
    public void onResume() {
        super.onResume();

    }

    private void findView(View rootView) {
        message_xlistview=(XListView)rootView.findViewById(R.id.message_xlistview);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(getActivity(),lables,R.layout.list_lables_personal_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_title_id = holder.getView(R.id.lables_tv_title_id);
                TextView homepage_wonderfulcontent_support = holder.getView(R.id.homepage_wonderfulcontent_support);
                TextView homepage_wonderfulcontent_comment = holder.getView(R.id.homepage_wonderfulcontent_comment);
                ImageView iv_mine_avatar = holder.getView(R.id.iv_mine_avatar);
                lables_tv_title.setText(item.getHeadTvName());
                homepage_wonderfulcontent_support.setText(item.getHeadTvLable());
                homepage_wonderfulcontent_comment.setText(item.getAnswersNums()+"");
                ImageLoader.getInstance().displayImage(item.getReleaseTime(),iv_mine_avatar,options);

            }
        };
        if(collections.size()<10){
            message_xlistview.setPullLoadEnable(false);
        }else {
            message_xlistview.setPullLoadEnable(true);
        }
        message_xlistview.setAdapter(mMessageAdapter);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LableDetaileContentActivity.class);
                intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT, "");
                intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getTaglibId() + "");
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

                Api.getMyContents(Long.parseLong(titlename + ""), start,allLableContentsHandlers);
            }
            private ResponseCallback<List<HpWonderfulContent>> allLableContentsHandlers = new ResponseCallback<List<HpWonderfulContent>>() {

                @Override
                public void onSuccess(List<HpWonderfulContent> data) {

                    if(data.size()<10){
                        message_xlistview.setPullLoadEnable(false);
                    }else {
                        message_xlistview.setPullLoadEnable(false);
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
        for (int i = 0; i < collections.size(); i++) {
                LableDetailsBean lable = new LableDetailsBean();
                lable.setHeadTvName(collections.get(i).getTitle() + "");
                lable.setHeadTvLable(collections.get(i).getStars() + "");
               lable.setAnswersNums(collections.get(i).getAnswersNums() + "");
                lable.setReleaseTime(collections.get(i).getRecommendPicUrl() + "");
                lable.setTaglibId(collections.get(i).getForumPostId());

            lables.add(lable);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
