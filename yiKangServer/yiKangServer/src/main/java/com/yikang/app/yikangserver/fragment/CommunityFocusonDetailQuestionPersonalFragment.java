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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.ForumPostsImage;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.QuestionAnswers;
import com.yikang.app.yikangserver.ui.AnswersDetails;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/4/26.
 * 社区关注模块下的收藏页面
 */
public class CommunityFocusonDetailQuestionPersonalFragment extends BaseFragment implements View.OnClickListener{
    private Handler mHandler;
    private int start = 1;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private ArrayList<QuestionAnswers> collections = new ArrayList<QuestionAnswers>();
    private View lable_footer_view;
    private LinearLayout lable_footer_views;
    private int posistion;
    public CommunityFocusonDetailQuestionPersonalFragment(int posistion,String titlename, ArrayList<QuestionAnswers> collections) {
        this.posistion=posistion;
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

        findView(rootView);

        return rootView;
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
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(getActivity(),lables,R.layout.list_mine_answer_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_tv_tiezi);
                TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                TextView lables_tv_times = holder.getView(R.id.lables_tv_times);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_tiezi.setText(item.getDetailTv());
                lables_tv_time.setText(item.getReleaseTime());
                lables_tv_times.setText(item.getHeadTvLable());
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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(posistion==1) {
                    Intent intent1 = new Intent(getActivity(), AnswersDetails.class);
                    intent1.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER, 1);
                    intent1.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position-1).getQuestionId() + "");
                    getActivity().startActivity(intent1);
                   // AppContext.showToast((i - 1) + "解答");
                }else {
                    Intent intent2 = new Intent(getActivity(), AnswersDetails.class);
                    intent2.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER, 1);
                    intent2.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position-1).getQuestionId() + "");
                    getActivity().startActivity(intent2);
                   // AppContext.showToast((i - 1) + "提问");
                }
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
                if(posistion==1) {
                    Api.getMyAnswer(Long.parseLong(titlename + ""), start, allLableContentHandlers);
                }else {
                    Api.getMyQuestion(Long.parseLong(titlename + ""), start, allLableContentHandlers);
                }
            }
            private ResponseCallback<List<QuestionAnswers>> allLableContentHandlers = new ResponseCallback<List<QuestionAnswers>>() {

                @Override
                public void onSuccess(List<QuestionAnswers> data) {

                    if(data.size()<10){
                        message_xlistview.setPullLoadEnable(false);
                    }else {
                        message_xlistview.setPullLoadEnable(true);
                    }
                    onLoad();
                    hideWaitingUI();
                    collections = new ArrayList<>();
                    for (QuestionAnswers hp : data) {
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
            private void onLoad() {
                message_xlistview.stopRefresh();
                message_xlistview.stopLoadMore();
                message_xlistview.setRefreshTime("刚刚");
            }
            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }

        });


    }

    private void getData() {
        if(posistion==1) {
            //获取解答
            Api.getMyAnswer(Long.parseLong(titlename + ""), 1, allLableContentHandler);
        }else {
            //获取解答
            Api.getMyQuestion(Long.parseLong(titlename + ""), 1, allLableContentHandler);
        }
    }
    /**
     * 获取关注收藏的回调
     */
    private ResponseCallback<List<QuestionAnswers>> allLableContentHandler = new ResponseCallback<List<QuestionAnswers>>() {

        @Override
        public void onSuccess(List<QuestionAnswers> data) {

            hideWaitingUI();
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            lables.clear();
            collections.clear();
            onLoad();

            hideWaitingUI();
            for (QuestionAnswers hp : data) {
                collections.add(hp);
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
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    private ResponseCallback<String> deleteMyFocusCollectionsHadler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {

            hideWaitingUI();
            lables.remove(position);
            mMessageAdapter.notifyDataSetChanged();//刷新listView
        }

        @Override
        public void onFailure(String status, String message) {

            AppContext.showToast(message);
        }
    };
    protected void geneItems() {
        for (int i = 0; i < collections.size(); ++i) {
            LableDetailsBean lable = new LableDetailsBean();

            lable.setHeadTvLable(getStringDate(collections.get(i).getCreateTime()));

            if(posistion==1) {
               lable.setHeadTvName(collections.get(i).getQuestion().getTitle());
                lable.setDetailTv(collections.get(i).getQuestion().getTaglibs().get(0).getTagName());
                lable.setReleaseTime(collections.get(i).getStarNum()+"");

                    lable.setQuestionId(collections.get(i).getQuestionId());

            }else {
                lable.setHeadTvName(collections.get(i).getTitle());
                lable.setDetailTv(collections.get(i).getTaglibs().get(0).getTagName());
                lable.setReleaseTime(collections.get(i).getAnswerNum()+"");
                lable.setQuestionId(collections.get(i).getQuestionId());
            }
            lables.add(lable);
        }
    }
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     */
    public static String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
