package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.QuestionAnswers;
import com.yikang.app.yikangserver.view.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的问题界面
 */
public class MineQuestionActivity extends BaseActivity implements View.OnClickListener{
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    public final static String MESSAGE_INFOS="messageinfo";
    private String messageinfo;
    List<QuestionAnswers> wonderfulActivity;
    private ArrayList<QuestionAnswers> Comlables = new ArrayList<QuestionAnswers>();
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
            lable.setHeadTvName(wonderfulActivity.get(i).getTitle());
            lable.setHeadTvLable(getStringDate(wonderfulActivity.get(i).getCreateTime()));
            lable.setReleaseTime(wonderfulActivity.get(i).getAnswerNum()+"");
            lable.setDetailTv(wonderfulActivity.get(i).getTaglibs().get(0).getTagName());
            lable.setQuestionId(wonderfulActivity.get(i).getQuestionId());
            lables.add(lable);
        }
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     */
    public static String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);

        return dateString;
    }
    @Override
    protected void findViews() {

        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(this,lables,R.layout.list_mine_question_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
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
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AnswersDetails.class);
                intent.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER, 1);
                intent.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position-1).getQuestionId() + "");

                startActivity(intent);

            }
        });
        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                start = 1;
               getData();
            }


            @Override
            public void onLoadMore() {
                start += 1;
                getDataMore();
            }
            protected void getDataMore() {

                //获取标签下的所有专业内容
                Api.getMyQuestion(Long.parseLong( AppContext.getAppContext().getUser().getUserId() + ""), start, allLableContentHandlers);
            }


            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }

        });
    }
    private ResponseCallback<List<QuestionAnswers>> allLableContentHandlers = new ResponseCallback<List<QuestionAnswers>>() {

        @Override
        public void onSuccess(List<QuestionAnswers> data) {
            hideWaitingUI();
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            onLoad();

            wonderfulActivity = new ArrayList<>();
            for (QuestionAnswers hp : data) {
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

    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_mine_question);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        start = 1;
        Api.getMyQuestion(Long.parseLong( AppContext.getAppContext().getUser().getUserId() + ""), 1,wonderfulActivityHandler);
    }

    private ResponseCallback<List<QuestionAnswers>> wonderfulActivityHandler = new ResponseCallback<List<QuestionAnswers>>() {



        @Override
        public void onSuccess(List<QuestionAnswers> data) {
            hideWaitingUI();
           // LOG.i("debug", "HpWonderfulContent---->" + data);
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            onLoad();
            wonderfulActivity = new ArrayList<>();
            for (QuestionAnswers hp : data) {
                wonderfulActivity.add(hp);
            }
            lables.clear();
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
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
