package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class AnswersActivity extends BaseActivity {
    private String tagId;
    private String tagName;
    private Handler mHandler;
    private int start = 1;

    List<HpWonderfulContent> bannerHp = new ArrayList<>();
    private XListView message_xlistview;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    List<LablesBean> secondLables = new ArrayList<>();
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private LinearLayout community_professional_ll_posting;
    private ImageView community_professional_iv_posting;
    private DisplayImageOptions option; //配置图片加载及显示选项

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagId = getIntent().getStringExtra("tagLibId");
        tagName = getIntent().getStringExtra("tagLibName");
        mHandler = new Handler();
        showWaitingUI();
        initContent();
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                //.displayer(new FadeInBitmapDisplayer(500))
                .build();

        initTitleBar(tagName);

    }

    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<List<LablesBean>> secondLablesHandlers = new ResponseCallback<List<LablesBean>>() {


        @Override
        public void onSuccess(List<LablesBean> data) {
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            onLoad();
            secondLables = new ArrayList<>();
            for (LablesBean hp : data) {
                secondLables.add(hp);
            }
            geneItems();
            mMessageAdapter.notifyDataSetChanged();

            //LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

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


    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<List<LablesBean>> secondLablesHandler = new ResponseCallback<List<LablesBean>>() {


        @Override
        public void onSuccess(List<LablesBean> data) {
            start=1;
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            mMessageAdapter.notifyDataSetChanged();
            hideWaitingUI();

            secondLables = new ArrayList<>();
            for (LablesBean hp : data) {
                secondLables.add(hp);
            }
            lables.clear();
            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);
            onLoad();
           // LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    protected void geneItems() {
        for (int i = 0; i < secondLables.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(secondLables.get(i).getUserName() + "");
            lable.setHeadTvLable(secondLables.get(i).getTitle() + "");
            lable.setDetailSupport(secondLables.get(i).getContent() + "");
            lable.setReleaseTime("发布于：" + TimeCastUtils.compareDateTime(System.currentTimeMillis(), secondLables.get(i).getCreateTime()));
            lable.setTaglibId(secondLables.get(i).getTaglibId());
            lable.setHeadIvUrl(secondLables.get(i).getPhotoUrl() + "");
            lable.setDetailSupport(secondLables.get(i).getAnswerNum() + "");
            lable.setDetailLable(secondLables.get(i).getQuestionId() + "");
            lables.add(lable);
        }


    }

    @Override
    protected void findViews() {
        message_xlistview = (XListView) findViewById(R.id.community_professional_listview);
        //community_professional_ll_posting = (LinearLayout)findViewById(R.id.community_professional_ll_posting);
        community_professional_iv_posting = (ImageView) findViewById(R.id.community_professional_iv_posting);
        community_professional_iv_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    // getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Intent intent1 = new Intent(getApplicationContext(),
                            PublishPostActivity.class);  //发布问题
                    intent1.putExtra(PublishPostActivity.EXTRA_LABLE_EXAMPLEIDS, tagId);
                    intent1.putExtra(PublishPostActivity.EXTRA_LABLE_EXAMPLENAME, tagName);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
            }
        });
        message_xlistview.setPullLoadEnable(true);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AnswersDetails.class);
                intent.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER, 1);
                intent.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getDetailLable() + "");

                startActivity(intent);
            }
        });
        mMessageAdapter = new CommonAdapter<LableDetailsBean>(getApplicationContext(), lables, R.layout.list_answer_content_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_username = holder.getView(R.id.lables_tv_username);
                TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                TextView lables_tv_nums = holder.getView(R.id.lables_tv_nums);
                CircleImageView lables_iv_userphoto = holder.getView(R.id.lables_iv_userphoto);
                lables_tv_title.setText(item.getHeadTvLable() + "");
                lables_tv_username.setText(item.getHeadTvName() + "");
                lables_tv_time.setText(item.getReleaseTime() + "");
                lables_tv_nums.setText(item.getDetailSupport() + "");
                ImageLoader.getInstance().displayImage(item.getHeadIvUrl() + "", lables_iv_userphoto, option);
            }
        };

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
                start += 1;
                getDataMore();
            }

            @Override
            public void onScrollMove() {
                community_professional_iv_posting.setVisibility(View.GONE);
            }

            @Override
            public void onScrollFinish() {
                Animation mShowAction = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.trans_bottom_in);
                community_professional_iv_posting.startAnimation(mShowAction);
                community_professional_iv_posting.setVisibility(View.VISIBLE);
            }

        });
    }

    protected void getDataMore() {

        //获取标签下的所有专业内容
        Api.getAllQuestionContent(Integer.parseInt(tagId), start, secondLablesHandlers);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_content);
    }

    @Override
    protected void getData() {

        start = 1;
//获取标签下的所有问答内容
        Api.getAllQuestionContent(Integer.parseInt(tagId), 1, secondLablesHandler);
    }

    @Override
    protected void initViewContent() {

    }
}
