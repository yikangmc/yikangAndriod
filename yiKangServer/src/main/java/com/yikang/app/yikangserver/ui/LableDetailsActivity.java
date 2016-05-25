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
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/21.
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
    private String lablesTitle;
    private LinearLayout lable_details_ll_posting;
    private ImageView lable_details_iv_posting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        lablesTitle = getIntent().getStringExtra(EXTRA_LABLE);
        geneItems(lablesTitle);
        initContent();
        initTitleBar(lablesTitle);
    }

    private void geneItems(String name) {
        for (int i = 0; i != 20; ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(name + "  " + i);
            lable.setHeadTvLable("运动员  " + i);
            lable.setReleaseTime("2016/5/19 20:0" + i);
            lables.add(lable);
        }
    }

    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_listview);
        lable_details_listview.setPullLoadEnable(true);
        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.list_lable_details_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {

//                findViewById(R.id.homepage_wonderfulcontent_shared).setVisibility(View.GONE);
                TextView lables_tv_title = holder.getView(R.id.lables_details_tv_name);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_tiezi.setText(item.getHeadTvLable());
                lables_tv_time.setText(item.getReleaseTime());
            }
        };
        lable_details_listview.setAdapter(lanleCommonAdapter);
        lable_details_listview.setXListViewListener(this);

        lable_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(LableDetailsActivity.this, "您选择了标签：" + lables.get(i - 1).getHeadTvName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LableDetailsActivity.this, LableDetaileExampleActivity.class);
                intent.putExtra(LableDetaileExampleActivity.EXTRA_LABLE_EXAMPLE, lables.get(i - 1).getHeadTvName());
                startActivity(intent);
            }
        });

        lable_details_iv_posting = (ImageView) findViewById(R.id.lable_details_iv_posting);
        lable_details_ll_posting = (LinearLayout) findViewById(R.id.lable_details_ll_posting);
        lable_details_iv_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LableDetailsActivity.this, PublishPostActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lable_details);
    }

    @Override
    protected void getData() {

    }

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
                geneItems(lablesTitle);
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
//        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
//                0.0f);
//        mHiddenAction.setDuration(500);
      /*  Animation mHiddenAction= AnimationUtils.loadAnimation(this,R.anim.trans_bottom_out);
        lable_details_ll_posting.startAnimation(mHiddenAction);*/
        lable_details_ll_posting.setVisibility(View.GONE);
    }

    /**
     * 当不滑动时，停止状态时，发表状态的按钮显示出来
     */
    @Override
    public void onScrollFinish() {
//        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
//        mShowAction.setDuration(500);
        Animation mShowAction = AnimationUtils.loadAnimation(this, R.anim.trans_bottom_in);
        lable_details_ll_posting.startAnimation(mShowAction);
        lable_details_ll_posting.setVisibility(View.VISIBLE);
    }

}
