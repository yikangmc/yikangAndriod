package com.yikang.app.yikangserver.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
 * 带全部评论的文章详情页面
 */
public class LableDetaileExampleActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {
    /*
    支持头像列表
     */
    private GridView homepage_support_gridview;

    private XListView lable_details_listview;

    private List<String> stringList = new ArrayList<String>();

    private Handler mHandler;

    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();

    private CommonAdapter<LableDetailsBean> lanleCommonAdapter;

    private CommonAdapter<LableDetailsBean> photoCommonAdapter;

    private int start = 0;

    private static int refreshCnt = 0;

    public static String EXTRA_LABLE_EXAMPLE = "lableexample";

    public static String EXTRA_LABLE_ANSWER = "lableanswer";

    private String lablesTitle;

    private int lableanswer;

    private LinearLayout lable_details_ll__shared, lable_details_ll_discuss, lable_detail_ll_support_img;

    private ImageView lable_details_iv_posting;

    private View ldeaHeaderView;

    private TextView lable_detail_tv_answerordiscuss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater mInflater = getLayoutInflater();
        ldeaHeaderView = mInflater.inflate(R.layout.activity_lable_detail_example_headerview, null);
        mHandler = new Handler();
        lablesTitle = getIntent().getStringExtra(EXTRA_LABLE_EXAMPLE);
        lableanswer = getIntent().getIntExtra(EXTRA_LABLE_ANSWER, 0);
        geneItems(lablesTitle);
        initContent();
        if (lableanswer == 1) {
            initTitleBar("这是用户ID");
            TextView tvTitle = (TextView) findViewById(R.id.tv_title_right_text);
            tvTitle.setText("我来回答");
            tvTitle.setOnClickListener(this);
        } else {
            initTitleBar(lablesTitle);
        }
    }

    private void geneItems(String name) {
        for (int i = 0; i != 10; ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(name + "  " + i);
            lable.setHeadTvLable("运动员  " + i);
            lable.setReleaseTime("time  " + i);
            lables.add(lable);
        }
    }

    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_example_listview);
        if (ldeaHeaderView != null) {
            lable_details_listview.addHeaderView(ldeaHeaderView);
        }
        lable_details_listview.setPullLoadEnable(true);
        /*
        整体列表适配器
         */
        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.list_lable_details_commmen_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {

//                findViewById(R.id.homepage_wonderfulcontent_shared).setVisibility(View.GONE);
//                TextView lables_tv_title=holder.getView(R.id.lables_details_tv_name);
//                TextView lables_tv_tiezi=holder.getView(R.id.lables_details_tv_zhicheng);
//                TextView lables_tv_time=holder.getView(R.id.lables_details_tv_pushtime);
//                lables_tv_title.setText(item.getHeadTvName());
//                lables_tv_tiezi.setText(item.getHeadTvLable());
//                lables_tv_time.setText(item.getReleaseTime());
            }
        };
        /*
        支持头像适配器
         */
        photoCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.photo) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {

//                findViewById(R.id.homepage_wonderfulcontent_shared).setVisibility(View.GONE);
//                TextView lables_tv_title=holder.getView(R.id.lables_details_tv_name);
//                TextView lables_tv_tiezi=holder.getView(R.id.lables_details_tv_zhicheng);
//                TextView lables_tv_time=holder.getView(R.id.lables_details_tv_pushtime);
//                lables_tv_title.setText(item.getHeadTvName());
//                lables_tv_tiezi.setText(item.getHeadTvLable());
//                lables_tv_time.setText(item.getReleaseTime());
            }
        };
        lable_details_listview.setAdapter(lanleCommonAdapter);
        lable_details_listview.setFocusable(false);
        lable_details_listview.setXListViewListener(this);

        lable_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LableDetaileExampleActivity.this, "给评论加评论" + lables.get(i).getHeadTvName(), Toast.LENGTH_SHORT).show();
            }
        });

        lable_details_iv_posting = (ImageView) findViewById(R.id.lable_details_iv_discuss);
        lable_details_ll__shared = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll__shared);
        lable_detail_ll_support_img = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_detail_ll_support_img);
        lable_detail_tv_answerordiscuss = (TextView) ldeaHeaderView.findViewById(R.id.lable_detail_tv_answerordiscuss);
        homepage_support_gridview = (GridView) ldeaHeaderView.findViewById(R.id.homepage_support_gridview);
        homepage_support_gridview.setAdapter(photoCommonAdapter);
        homepage_support_gridview.setFocusable(false);
        lable_details_ll_discuss = (LinearLayout) findViewById(R.id.lable_details_ll_discuss);
        lable_details_iv_posting.setOnClickListener(this);
        lable_details_ll__shared.setOnClickListener(this);
        if (lableanswer == 1) {
            lable_details_ll_discuss.setVisibility(View.GONE);
            lable_detail_ll_support_img.setVisibility(View.GONE);
            lable_detail_tv_answerordiscuss.setText("全部回答");
        }
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lable_detail_example);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        //Api.getUserInfo(loadUserInfoHandler);
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

    @Override
    public void onScrollMove() {
    }

    @Override
    public void onScrollFinish() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lable_details_iv_discuss:
                Toast.makeText(LableDetaileExampleActivity.this, "我要評論！", Toast.LENGTH_LONG).show();
                break;
            case R.id.lable_details_ll__shared:
                new PopupWindows(LableDetaileExampleActivity.this, lable_details_ll__shared);
                break;
            case R.id.tv_title_right_text:
                Toast.makeText(LableDetaileExampleActivity.this, "我来回答！", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_lable_shared_popupwindows, null);
//            view.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.trans_bottom_in));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(LinearLayout.LayoutParams.FILL_PARENT);
            setHeight(LinearLayout.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.lable_item_popupwindows_report);
            Button bt2 = (Button) view
                    .findViewById(R.id.lable_item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(LableDetaileExampleActivity.this, "我要举报！", Toast.LENGTH_LONG).show();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    ;
                }
            });

        }
    }

}
