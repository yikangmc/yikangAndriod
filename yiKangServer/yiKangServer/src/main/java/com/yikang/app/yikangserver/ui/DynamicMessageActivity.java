package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.Message;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统消息界面
 */
public class DynamicMessageActivity extends BaseActivity implements View.OnClickListener{
    private Handler mHandler;
    private int start = 1;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    public final static String MESSAGE_INFOS="messageinfo";
    private String messageinfo;
    List<Message> wonderfulActivity;
    private ArrayList<Message> Comlables = new ArrayList<Message>();
    private LinearLayout ll_no_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        initContent();
        messageinfo=getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);
    }
    protected void geneItems() {
        for (int i = 0; i < wonderfulActivity.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            if(wonderfulActivity.get(i).getContentGroup()==7){//回答被认同
                lable.setHeadTvName("支持动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }
            if(wonderfulActivity.get(i).getContentGroup()==8){//解答被评论
                lable.setHeadTvName("回答动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }
            if(wonderfulActivity.get(i).getContentGroup()==9){//解答的评论被回复
                lable.setHeadTvName("回复动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }
            if(wonderfulActivity.get(i).getContentGroup()==6){//文章被评论
                lable.setHeadTvName("评论动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }
            if(wonderfulActivity.get(i).getContentGroup()==5){//文章被认同
                lable.setHeadTvName("支持动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }

            if(wonderfulActivity.get(i).getContentGroup()==4){//关注标签下有新的文章
                lable.setHeadTvName("关注动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle());

            }
            if(wonderfulActivity.get(i).getContentGroup()==3){//帖子被评论
                lable.setHeadTvName("评论动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }
            if(wonderfulActivity.get(i).getContentGroup()==2){//帖子被认同
                lable.setHeadTvName("支持动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }
            if(wonderfulActivity.get(i).getContentGroup()==1){//问题被解答
                lable.setHeadTvName("回答动态"+ "");
                lable.setQuestionId(wonderfulActivity.get(i).getDataId());
                lable.setHeadTvLable(wonderfulActivity.get(i).getTitle()+"");

            }
            lable.setToquestionId(wonderfulActivity.get(i).getMessagesId());
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(),wonderfulActivity.get(i).getCreateTime()));
            lable.setIsStar(wonderfulActivity.get(i).getContentGroup());
            lable.setCreateUserPosition(wonderfulActivity.get(i).getIsRead());
            lables.add(lable);
        }
    }
    @Override
    protected void findViews() {

        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        ll_no_message=(LinearLayout) findViewById(R.id.ll_no_message);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(this,lables,R.layout.list_lables_dynamic_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_tv_tiezi);
                TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_tiezi.setText(item.getHeadTvLable());
                lables_tv_time.setText(item.getReleaseTime());
                if(item.getCreateUserPosition()==0){
                    ll_item.setBackgroundResource(R.color.common_blue);
                }
                if(item.getCreateUserPosition()==1){
                    ll_item.setBackgroundResource(R.color.white);
                }
            }
        };
        message_xlistview.setAdapter(mMessageAdapter);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                //AppContext.showToast(wonderfulActivity.get(position-1).getTitle()+wonderfulActivity.get(position-1).getDataId());
                Api.changeMessageState(lables.get(position - 1).getToquestionId(), new ResponseCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        lables.get(position - 1).setCreateUserPosition(1);
                        mMessageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String status, String message) {

                    }
                });
                if(lables.get(position-1).getIsStar()==1){
                    if(position!=0) {
                        Intent intent = new Intent(getApplicationContext(), AnswerQuestionsDetailActivity.class);
                        intent.putExtra("AnswersDetailsActivityID", lables.get(position - 1).getQuestionId() + "");
                        //intent.putExtra("AnswersDetailsActivityTitle", wonderfulActivity.get(position - 1).getDataId()+"");
                        startActivity(intent);
                    }
                }
                if(lables.get(position-1).getIsStar()==2){
                    if(position!=0) {
                        Intent intent = new Intent(DynamicMessageActivity.this, ContentDetailsActivity.class);
                        intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getQuestionId() + "");
                        //intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, lables.get(i - 1).getIsStore() + "");
                        intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENT, AppContext.getAppContext().getUser().getName() + "");
                        startActivity(intent);
                    }
                }
                if(lables.get(position-1).getIsStar()==3){
                    if(position!=0) {
                        Intent intent = new Intent(DynamicMessageActivity.this, ContentDetailsActivity.class);
                            intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getQuestionId() + "");
                            //intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, lables.get(i - 1).getIsStore() + "");
                            intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENT, AppContext.getAppContext().getUser().getName() + "");
                            startActivity(intent);
                    }
                }
                if(lables.get(position-1).getIsStar()==4){
                    if(position!=0) {
                        Intent intent = new Intent(getApplicationContext(), LableDetaileContentActivity.class);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT,AppContext.getAppContext().getUser().getName());
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getQuestionId() + "");
                        startActivity(intent);
                    }
                }
                if(lables.get(position-1).getIsStar()==5){
                    if(position!=0) {
                        Intent intent = new Intent(getApplicationContext(), LableDetaileContentActivity.class);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT,AppContext.getAppContext().getUser().getName());
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getQuestionId() + "");
                        startActivity(intent);
                    }
                }
                if(lables.get(position-1).getIsStar()==6){
                    if(position!=0) {
                        Intent intent = new Intent(getApplicationContext(), LableDetaileContentActivity.class);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT,AppContext.getAppContext().getUser().getName());
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getQuestionId() + "");
                        startActivity(intent);
                    }
                }

                if(lables.get(position-1).getIsStar()==7){
                    if(position!=0) {
                        Intent intent = new Intent(getApplicationContext(), AnswerQuestionsDetailActivity.class);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT,AppContext.getAppContext().getUser().getName());
                        intent.putExtra("AnswersDetailsActivityID", lables.get(position - 1).getQuestionId() + "");
                        startActivity(intent);
                    }
                }
                if(lables.get(position-1).getIsStar()==8){
                    if(position!=0) {
                        Intent intent = new Intent(getApplicationContext(), AnswerQuestionsDetailCommentActivity.class);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT,AppContext.getAppContext().getUser().getName());
                        intent.putExtra("AnswersDetailsID", lables.get(position - 1).getQuestionId() + "");
                        startActivity(intent);
                    }
                }
                if(lables.get(position-1).getIsStar()==9){
                    if(position!=0) {
                        Intent intent = new Intent(getApplicationContext(), AnswerQuestionsDetailCommentActivity.class);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT,AppContext.getAppContext().getUser().getName());
                        intent.putExtra("AnswersDetailsID", lables.get(position - 1).getQuestionId() + "");
                        startActivity(intent);
                    }
                }
            }
        });
        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                start = 1;
                Api.getDynamicMessageNew(1,systemMessageHandler);
            }


            @Override
            public void onLoadMore() {
                start += 1;
                getDataMore();
            }
            protected void getDataMore() {

                //获取标签下的所有专业内容
                Api.getDynamicMessageNew( start, allLableContentHandlers);
            }
            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }
            private ResponseCallback<List<Message>> allLableContentHandlers = new ResponseCallback<List<Message>>() {

                @Override
                public void onSuccess(List<Message> data) {
                    hideWaitingUI();
                    if(data.size()<10){
                        message_xlistview.setPullLoadEnable(false);
                    }else {
                        message_xlistview.setPullLoadEnable(true);
                    }
                    onLoad();

                    wonderfulActivity = new ArrayList<>();
                    for (Message hp : data) {
                        wonderfulActivity.add(hp);
                    }
                    geneItems();
                    mMessageAdapter.notifyDataSetChanged();
                    // LOG.i("debug", "HpWonderfulContent---->" + data + "嘎嘎嘎");

                }

                @Override
                public void onFailure(String status, String message) {
                    // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                    hideWaitingUI();
                    AppContext.showToast(message);
                }
            };


        });
    }

    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        Api.getDynamicMessageNew(1,systemMessageHandler);
    }
    private ResponseCallback<List<Message>> systemMessageHandler = new ResponseCallback<List<Message>>() {



        @Override
        public void onSuccess(List<Message> data) {
            hideWaitingUI();
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            if(data.size()==0){
                ll_no_message.setVisibility(View.VISIBLE);
            }
            //LOG.i("debug", "HpWonderfulContent---->" + data);
            wonderfulActivity = new ArrayList<>();
            for (Message hp : data) {
                wonderfulActivity.add(hp);
            }
            lables.clear();
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
            onLoad();
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
