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
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class ContentsActivity extends BaseActivity {
    private String tagId;
    private String tagName;
    private Handler mHandler;
    public  int start = 1;
    private List<HpWonderfulContent> bannerHp = new ArrayList<>();
    private XListView message_xlistview;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private List<LablesBean> secondLables = new ArrayList<>();
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private LinearLayout community_professional_ll_posting;
    private ImageView community_professional_iv_posting;
    private DisplayImageOptions options; //配置图片加载及显示选项
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        tagId=getIntent().getStringExtra("tagLibId");
        tagName=getIntent().getStringExtra("tagLibName");
        showWaitingUI();
        initContent();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
               // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
               // .displayer(new FadeInBitmapDisplayer(500))
                .build();

        initTitleBar(tagName);

    }

    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentsHandlers;

    {
        allLableContentsHandlers = new ResponseCallback<List<HpWonderfulContent>>() {


            @Override
            public void onSuccess(List<HpWonderfulContent> data) {
                if(data.size()<10){
                    message_xlistview.setPullLoadEnable(false);
                }else {
                    message_xlistview.setPullLoadEnable(true);
                }
                onLoad();
                hideWaitingUI();
                bannerHp = new ArrayList<>();

                for (HpWonderfulContent hp : data) {
                    bannerHp.add(hp);
                }
                geneItem();
                mMessageAdapter.notifyDataSetChanged();

              //  LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

            }



            @Override
            public void onFailure(String status, String message) {
               // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                hideWaitingUI();
                AppContext.showToast(message);
            }
        };
    }


    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentsHandler;

    {
        allLableContentsHandler = new ResponseCallback<List<HpWonderfulContent>>() {


            @Override
            public void onSuccess(List<HpWonderfulContent> data) {
                if(data.size()<10){
                    message_xlistview.setPullLoadEnable(false);
                }else {
                    message_xlistview.setPullLoadEnable(true);
                }
                start=1;
                mMessageAdapter.notifyDataSetChanged();

                hideWaitingUI();
                bannerHp = new ArrayList<>();
                for (HpWonderfulContent hp : data) {
                    bannerHp.add(hp);
                }
                lables.clear();
                geneItem();
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
    }

    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }


    protected void geneItem() {
        for (int i = 0; i < bannerHp.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(bannerHp.get(i).getUserName() + "");
            lable.setHeadTvLable(bannerHp.get(i).getTitle() + "");
            // lable.setDetailSupport(bannerHp.get(i).getContent() + "");
//            lable.setReleaseTime("发布于：" + TimeCastUtils.compareDateTime(System.currentTimeMillis(), secondLables.get(i).getCreateTime()));
             lable.setTaglibId(bannerHp.get(i).getUserPosition());
            lable.setHeadIvUrl(bannerHp.get(i).getRecommendPicUrl() + "");
            lable.setDetailSupport(bannerHp.get(i).getContent() + "");
            lable.setDetailLable(bannerHp.get(i).getForumPostId() + "");
            lables.add(lable);
        }

    }
    @Override
    protected void findViews() {
        message_xlistview = (XListView) findViewById(R.id.community_professional_listview);
       // community_professional_ll_posting = (LinearLayout)findViewById(R.id.community_professional_ll_posting);
        community_professional_iv_posting = (ImageView) findViewById(R.id.community_professional_iv_posting);

        community_professional_iv_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    // getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Intent intent = new Intent(getApplicationContext(), RichTextActivity.class)
                            .putExtra("role", "add");
                    intent.putExtra("tagLibId", tagId);
                    intent.putExtra("tagLibName", tagName);
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
            }
        });
        message_xlistview.setPullLoadEnable(true);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), LableDetaileContentActivity.class);
                intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                if (bannerHp != null) {
                    intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT, lables.get(position - 1).getHeadTvName());
                    intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getDetailLable() + "");
                }
                startActivity(intent);
            }
        });

        mMessageAdapter = new CommonAdapter<LableDetailsBean>(getApplicationContext(), lables, R.layout.list_professional_content_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_content = holder.getView(R.id.lables_tv_content);
                TextView lables_tv_username = holder.getView(R.id.lables_tv_username);
                TextView tv_position = holder.getView(R.id.tv_position);
                ImageView lable_iv_header = holder.getView(R.id.lable_iv_header);
                lables_tv_title.setText(item.getHeadTvLable() + "");
                lables_tv_username.setText(item.getHeadTvName() + "");
                lables_tv_content.setText(item.getDetailSupport() + "");
                ImageLoader.getInstance().displayImage(item.getHeadIvUrl()+"", lable_iv_header,options);
                PositionUtils.getPosition(item.getTaglibId(),tv_position);
            }
        };

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

            @Override
            public void onScrollMove() {
                if (AppContext.getAppContext().getUser().profession > 0) {
                    community_professional_iv_posting.setVisibility(View.GONE);
                }

            }

            @Override
            public void onScrollFinish() {
                if (AppContext.getAppContext().getUser().profession > 0) {
                    Animation mShowAction = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.trans_bottom_in);
                    community_professional_iv_posting.startAnimation(mShowAction);
                    community_professional_iv_posting.setVisibility(View.VISIBLE);
                }

            }

        });
    }
    protected void getDataMore() {

        //获取标签下的所有专业内容
        Api.getAllLableContents(Integer.parseInt(tagId), start,allLableContentsHandlers);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_content);
    }

    @Override
    protected void getData() {
        start=1;
        //获取标签下的所有专业内容
        Api.getAllLableContents(Integer.parseInt(tagId), start,allLableContentsHandler);
    }

    @Override
    protected void initViewContent() {

    }
}
