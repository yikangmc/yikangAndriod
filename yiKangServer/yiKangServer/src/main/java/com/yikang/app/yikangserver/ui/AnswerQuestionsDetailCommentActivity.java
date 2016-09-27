package com.yikang.app.yikangserver.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yikang.app.yikangserver.bean.QuestionAnswersComment;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class AnswerQuestionsDetailCommentActivity extends BaseActivity {
    private String contentId;
    private XListView message_xlistview;
    private Handler mHandler;
    public  int start = 1;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private View lvHeaderView;
    private ImageView lable_details_iv_posting, lable_details_iv_discuss,lable_details_iv_discussss,lable_details_iv_huifu,lable_details_iv_huifus;

    private EditText et_comment;

    private TextView tv_title_right_text;

    private List<QuestionAnswersComment> bannerHp = new ArrayList<>();

    private DisplayImageOptions options; //配置图片加载及显示选项
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = getIntent().getStringExtra("AnswersDetailsID");
        mHandler=new Handler();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                //.displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        initContent();
        initTitleBar("评论");
    }
    protected void geneItems() {
        for (int i = 0; i < bannerHp.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(bannerHp.get(i).getUserName() + "");
            lable.setDetailLable(bannerHp.get(i).getContent());
            lable.setHeadTvLable(bannerHp.get(i).getPhotol());
            lable.setDetailSupport(bannerHp.get(i).getToUserName());
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(), bannerHp.get(i).getCreateTime()));
            lable.setQuestionId(bannerHp.get(i).getCreateUserId());
            lable.setToquestionId(bannerHp.get(i).getToUserId());
            lables.add(lable);
        }
    }
    @Override
    protected void findViews() {
        tv_title_right_text = (TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("取消回复");
        tv_title_right_text.setVisibility(View.GONE);
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_comment.setHint("我想说两句...");
                lable_details_iv_discuss.setVisibility(View.VISIBLE);
                lable_details_iv_discussss.setVisibility(View.GONE);
                lable_details_iv_huifus.setVisibility(View.GONE);
                lable_details_iv_huifu.setVisibility(View.GONE);
                tv_title_right_text.setVisibility(View.GONE);
            }
        });
        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        //lvHeaderView = getLayoutInflater().inflate(R.layout.headerview, null);//listview的头view
        //message_xlistview.addHeaderView(lvHeaderView);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(this,lables,R.layout.list_lable_details_commmens_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_details_tv_name);
                TextView homepage_ll_wonderfulcontent_text_answer = holder.getView(R.id.homepage_ll_wonderfulcontent_text_answer);
                TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                TextView lables_details_tv_pushtime = holder.getView(R.id.lables_details_tv_pushtime);
                com.yikang.app.yikangserver.view.CircleImageView lables_details_iv_user = holder.getView(R.id.lables_details_iv_user);
                lables_tv_title.setText(item.getHeadTvName());
                if(item.getDetailSupport()!=null) {
                    homepage_ll_wonderfulcontent_text_answer.setText("@" + item.getDetailSupport() + ":");

                }else {
                    homepage_ll_wonderfulcontent_text_answer.setText("");
                }
                lables_details_tv_pushtime.setText(item.getReleaseTime());
                homepage_ll_wonderfulcontent_text.setText(item.getDetailLable());
                ImageLoader.getInstance().displayImage(item.getHeadTvLable(), lables_details_iv_user,options);
                lables_details_iv_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),
                                PersonalHomepageActivity.class);
                        intent.putExtra("userId",item.getQuestionId()+"");
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    }
                });
                lables_tv_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),
                                PersonalHomepageActivity.class);
                        intent.putExtra("userId",item.getQuestionId()+"");
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    }
                });
                homepage_ll_wonderfulcontent_text_answer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),
                                PersonalHomepageActivity.class);
                        intent.putExtra("userId",item.getToquestionId()+"");
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    }
                });
            }
        };
        message_xlistview.setAdapter(mMessageAdapter);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                tv_title_right_text.setVisibility(View.VISIBLE);
                et_comment.setHint("回复" + lables.get(position - 1).getHeadTvName());
                lable_details_iv_discuss.setVisibility(View.GONE);
                lable_details_iv_discussss.setVisibility(View.GONE);
                lable_details_iv_huifu.setVisibility(View.VISIBLE);
                lable_details_iv_huifus.setVisibility(View.GONE);

                et_comment.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        lable_details_iv_discuss.setVisibility(View.GONE);
                        lable_details_iv_discussss.setVisibility(View.GONE);
                        lable_details_iv_huifu.setVisibility(View.VISIBLE);
                        lable_details_iv_huifus.setVisibility(View.GONE);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        lable_details_iv_discuss.setVisibility(View.GONE);
                        lable_details_iv_huifus.setVisibility(View.VISIBLE);
                        lable_details_iv_huifu.setVisibility(View.GONE);
                        lable_details_iv_discussss.setVisibility(View.GONE);
                        if(s.length()==0){
                            et_comment.setHint("回复" + lables.get(position - 1).getHeadTvName());
                            lable_details_iv_discuss.setVisibility(View.GONE);
                            lable_details_iv_discussss.setVisibility(View.GONE);
                            lable_details_iv_huifus.setVisibility(View.GONE);
                            lable_details_iv_huifu.setVisibility(View.VISIBLE);
                        }
                        lable_details_iv_huifus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //AppContext.showToast("回复");
                                showWaitingUI();
                                if(et_comment.getText().toString().trim().equals("")){
                                    AppContext.showToast("评论不能为空");
                                    return;
                                }
                                Api.addHuifuForQuestion(Long.parseLong(contentId),Long.parseLong(lables.get(position-1).getQuestionId()+""),et_comment.getText().toString().trim(), new ResponseCallback<Void>() {

                                    @Override
                                    public void onSuccess(Void data) {
                                        hideWaitingUI();
                                        getData();
                                        // AppContext.showToast("success");
                                        et_comment.setText("");
                                        InputMethodManager imm = (InputMethodManager)AnswerQuestionsDetailCommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                        //关闭软键盘
                                        imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
                                        et_comment.setHint("我想说两句...");
                                        lable_details_iv_discuss.setVisibility(View.VISIBLE);
                                        lable_details_iv_discussss.setVisibility(View.GONE);
                                        lable_details_iv_huifus.setVisibility(View.GONE);
                                        lable_details_iv_huifu.setVisibility(View.GONE);
                                        tv_title_right_text.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(String status, String message) {
                                        AppContext.showToast(message);
                                    }
                                });

                            }
                        });
                    }
                });
                //}

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
                start +=1;
                getDataMore();
            }

            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }

        });


        lable_details_iv_discuss = (ImageView) findViewById(R.id.lable_details_iv_discuss);
        lable_details_iv_discussss = (ImageView) findViewById(R.id.lable_details_iv_discussss);
        lable_details_iv_huifu = (ImageView) findViewById(R.id.lable_details_iv_huifu);
        lable_details_iv_huifus = (ImageView) findViewById(R.id.lable_details_iv_huifus);
        et_comment = (EditText) findViewById(R.id.et_comment);

        lable_details_iv_discussss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitingUI();
                if(et_comment.getText().toString().trim().equals("")){
                    AppContext.showToast("评论不能为空");
                    return;
                }
                Api.addAnswerForQuestion(Long.parseLong(contentId+""),et_comment.getText().toString().trim(), new ResponseCallback<String>() {

                    @Override
                    public void onSuccess(String data) {
                        hideWaitingUI();
                        //AppContext.showToast("success");
                        getData();
                        et_comment.setText("");
                        InputMethodManager imm = (InputMethodManager)AnswerQuestionsDetailCommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        //关闭软键盘
                        imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
                    }

                    @Override
                    public void onFailure(String status, String message) {
                        hideWaitingUI();
                        AppContext.showToast(message);
                    }
                });
            }
        });
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lable_details_iv_discuss.setVisibility(View.VISIBLE);
                lable_details_iv_discussss.setVisibility(View.GONE);
                lable_details_iv_huifu.setVisibility(View.GONE);
                lable_details_iv_huifus.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                lable_details_iv_discuss.setVisibility(View.GONE);
                lable_details_iv_huifus.setVisibility(View.GONE);
                lable_details_iv_huifu.setVisibility(View.GONE);
                lable_details_iv_discussss.setVisibility(View.VISIBLE);
                if(s.length()==0){
                    lable_details_iv_discuss.setVisibility(View.VISIBLE);
                    lable_details_iv_discussss.setVisibility(View.GONE);
                    lable_details_iv_huifus.setVisibility(View.GONE);
                    lable_details_iv_huifu.setVisibility(View.GONE);
                }
            }
        });
    }

    private ResponseCallback<List<QuestionAnswersComment>> allLableContentsHandlers = new ResponseCallback<List<QuestionAnswersComment>>() {

        @Override
        public void onSuccess(List<QuestionAnswersComment> data) {

            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            onLoad();
            hideWaitingUI();
            bannerHp = new ArrayList<>();
            for (QuestionAnswersComment hp : data) {
                bannerHp.add(hp);
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
    protected void setContentView() {
        setContentView(R.layout.activity_answer_questions_detail_comment);
    }
    protected void getDataMore() {

        //获取标签下的所有专业内容
        Api.answersForQuestion(Integer.parseInt(contentId+""), start,allLableContentsHandlers);
    }
    @Override
    protected void getData() {
        showWaitingUI();
        start=1;
       Api.answersForQuestion(Long.parseLong(contentId+""),start,contentHandler);
    }
    /**
     * 某一个标签下的所有文章回调
     */
    private ResponseCallback<List<QuestionAnswersComment>> contentHandler = new ResponseCallback<List<QuestionAnswersComment>>() {

        @Override
        public void onSuccess(List<QuestionAnswersComment> data) {

            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            onLoad();
            hideWaitingUI();
            bannerHp = new ArrayList<>();
            for (QuestionAnswersComment hp : data) {
                bannerHp.add(hp);
            }
            lables.clear();

            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
        }

        @Override
        public void onFailure(String status, String message) {
            //  LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    @Override
    protected void initViewContent() {

    }
    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }


}
