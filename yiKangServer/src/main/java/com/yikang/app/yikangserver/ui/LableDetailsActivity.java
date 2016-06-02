package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.BannerBean;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/21.
 * 二级标签下的所有帖子页面
 */
public class LableDetailsActivity extends BaseActivity implements XListView.IXListViewListener {

    private GridView homepage_more_tables_gridview;
    private XListView lable_details_listview;
    private List<String> stringList = new ArrayList<String>();
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> lanleCommonAdapter;
    public static String EXTRA_LABLE = "lable";
    public static String EXTRA_LABLE_ID = "id";
    private String lablesTitle;
    private String lablesId;
    private LinearLayout lable_details_ll_posting;
    private ImageView lable_details_iv_posting;
    List<HpWonderfulContent> bannerHp=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        lablesTitle = getIntent().getStringExtra(EXTRA_LABLE);
        lablesId = getIntent().getStringExtra(EXTRA_LABLE_ID);
        geneItems(lablesTitle+lablesId);
        initContent();
        initTitleBar(lablesTitle);
    }

    private void geneItems(String name) {
        for (int i = 0; i <bannerHp.size(); ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(bannerHp.get(i).getUserName());
            lable.setHeadTvLable("路人甲");
            lable.setDetailLable(bannerHp.get(i).getContent());
            lable.setDetailSupport(bannerHp.get(i).getStars()+"");
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(),bannerHp.get(i).getCreateTime()));
            //lable.setDetailIvUrls(bannerHp.get(i).);
            lables.add(lable);
        }
    }

    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_listview);
        lable_details_listview.setPullLoadEnable(false);
        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.list_lable_details_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {


                TextView lables_tv_title = holder.getView(R.id.lables_details_tv_name);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                TextView lable_details_tv_support = holder.getView(R.id.lable_details_tv_support);
                TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                ImageView lables_details_iv_thumnnail1=holder.getView(R.id.lables_details_iv_thumnnail1);
                ImageView lables_details_iv_thumnnail2=holder.getView(R.id.lables_details_iv_thumnnail2);
                ImageView lables_details_iv_thumnnail3=holder.getView(R.id.lables_details_iv_thumnnail3);
                ImageView lables_details_iv_thumnnail4=holder.getView(R.id.lables_details_iv_thumnnail4);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_tiezi.setText(item.getHeadTvLable());
                lables_tv_time.setText(item.getReleaseTime());
                lable_details_tv_support.setText(item.getDetailSupport());
                homepage_ll_wonderfulcontent_text.setText(item.getDetailLable());
            }
        };

        lable_details_listview.setXListViewListener(this);
        lable_details_listview.setAdapter(lanleCommonAdapter);
        lable_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(LableDetailsActivity.this, "您选择了标签：" + lables.get(i - 1).getHeadTvName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LableDetailsActivity.this, LableDetaileExampleActivity.class);
                if(bannerHp!=null) {
                    intent.putExtra(LableDetaileExampleActivity.EXTRA_LABLE_ANSWER_CONTENTID, bannerHp.get(i).getForumPostId() + "");
                    //intent.putExtra(LableDetaileExampleActivity.EXTRA_LABLE_ANSWER_CONTENT, bannerHp.get(i).getUserName() + "");
                }
                startActivity(intent);
            }
        });

        lable_details_iv_posting = (ImageView) findViewById(R.id.lable_details_iv_posting);
        lable_details_ll_posting = (LinearLayout) findViewById(R.id.lable_details_ll_posting);
        lable_details_iv_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),
                        PublishLablesActivity.class);  //发布帖子
                startActivity(intent1);
               overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lable_details);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        Api.getAllLableContent(Integer.parseInt(lablesId),allLableContentHandler);
    }
    /**
     * 某一个标签下的所有文章回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();

            bannerHp = new ArrayList<>();
            for (HpWonderfulContent hp : data) {
                bannerHp.add(hp);
            }
            LOG.i("debug", "HpWonderfulContent---->" + data+"嘎嘎嘎");

        }
        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void initViewContent() {

    }

    private void onLoad() {
        lable_details_listview.stopRefresh();
        lable_details_listview.stopLoadMore();
        lable_details_listview.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                lables.clear();
                geneItems(lablesTitle);
                lable_details_listview.setAdapter(lanleCommonAdapter);
                onLoad();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // geneItems(lablesTitle);
                lanleCommonAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 1000);
    }

    /**
     * 当滑动时，发表状态的按钮不显示
     */
    @Override
    public void onScrollMove() {

        lable_details_ll_posting.setVisibility(View.GONE);
    }

    /**
     * 当不滑动时，停止状态时，发表状态的按钮显示出来
     */
    @Override
    public void onScrollFinish() {

        Animation mShowAction = AnimationUtils.loadAnimation(this, R.anim.trans_bottom_in);
        lable_details_ll_posting.startAnimation(mShowAction);
        lable_details_ll_posting.setVisibility(View.VISIBLE);
    }

}
