package com.yikang.app.yikangserver.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.MyLablesGridViewAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.ForumPostsAnswers;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
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

    private Handler mHandler;

    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();

    private CommonAdapter<LableDetailsBean> lanleCommonAdapter;

    private int start = 0;
    private int NUM = 3; // 每行显示个数
    private int LIEWIDTH;//每列宽度
    DisplayMetrics dm;

    private static int refreshCnt = 0;

    public static String EXTRA_LABLE_EXAMPLE = "lableexample";

    public static String EXTRA_LABLE_ANSWER = "lableanswer";

    public static String EXTRA_LABLE_ANSWER_CONTENT = "lableanswercontent";

    public static String EXTRA_LABLE_ANSWER_CONTENTID = "lableanswercontentid";

    private String lablesTitle;

    private String contentId;

    private int lableanswer;

    private LinearLayout lable_details_ll__shared, lable_details_ll_discuss, lable_detail_ll_support_img;

    private ImageView lable_details_iv_posting, lable_details_iv_discuss;

    private EditText et_comment;

    private View ldeaHeaderView;

    private TextView lable_detail_tv_answerordiscuss;

    private List<String> stringList = new ArrayList<String>();
    private List<ForumPostsAnswers> answersesnums = new ArrayList<ForumPostsAnswers>();

    private ImageView lables_details_iv_user;//用户头像
    private TextView lables_details_tv_name;//用户名称
    private TextView lables_details_tv_zhicheng;//用户职称
    private TextView lables_details_tv_pushtime;//发布时间
    private ImageView lables_details_iv_thumnnail1;//内容图片
    private ImageView lables_details_iv_thumnnail2;//内容图片
    private ImageView lables_details_iv_thumnnail3;//内容图片
    private ImageView lables_details_iv_thumnnail4;//内容图片
    private TextView homepage_ll_wonderfulcontent_text;//内容
    private TextView lable_details_tv_support;//支持数
    private TextView lable_details_tv_supports;//支持数
    private TextView lable_details_tv__comment;//评论数
    private TextView tv_tag1;//标签1
    private TextView tv_tag2;//标签2
    private TextView tv_tag3;//标签3
    private TextView tv_tag4;//标签4
    private LinearLayout lable_details_ll_support;//点赞

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater mInflater = getLayoutInflater();
        ldeaHeaderView = mInflater.inflate(R.layout.activity_lable_detail_example_headerview, null);
        initHeaderView(ldeaHeaderView);
        mHandler = new Handler();
        lablesTitle = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENT);
        lableanswer = getIntent().getIntExtra(EXTRA_LABLE_ANSWER, 0);
        contentId = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENTID);
        //geneItems(lablesTitle);

        initContent();
        if (lableanswer == 1) {
            initTitleBar(lablesTitle + "");
            TextView tvTitle = (TextView) findViewById(R.id.tv_title_right_text);
            tvTitle.setText("我来回答");
            tvTitle.setOnClickListener(this);
        } else {
            initTitleBar(lablesTitle + "");
        }
    }

    private void initHeaderView(View ldeaHeaderView) {
        lables_details_iv_user = (ImageView) ldeaHeaderView.findViewById(R.id.lables_details_iv_user);
        lables_details_tv_name = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_name);
        lables_details_tv_zhicheng = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_zhicheng);
        lables_details_tv_pushtime = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_pushtime);
        lables_details_iv_thumnnail1 = (ImageView) ldeaHeaderView.findViewById(R.id.lables_details_iv_thumnnail1);
        lables_details_iv_thumnnail2 = (ImageView) ldeaHeaderView.findViewById(R.id.lables_details_iv_thumnnail2);
        lables_details_iv_thumnnail3 = (ImageView) ldeaHeaderView.findViewById(R.id.lables_details_iv_thumnnail3);
        lables_details_iv_thumnnail4 = (ImageView) ldeaHeaderView.findViewById(R.id.lables_details_iv_thumnnail4);
        homepage_ll_wonderfulcontent_text = (TextView) ldeaHeaderView.findViewById(R.id.homepage_ll_wonderfulcontent_text);
        lable_details_tv_support = (TextView) ldeaHeaderView.findViewById(R.id.lable_details_tv_support);
        lable_details_tv_supports = (TextView) ldeaHeaderView.findViewById(R.id.lable_details_tv_supports);
        lable_details_tv__comment = (TextView) ldeaHeaderView.findViewById(R.id.lable_details_tv__comment);
        tv_tag1 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag1);
        tv_tag2 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag2);
        tv_tag3 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag3);
        tv_tag4 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag4);
        lable_details_ll_support = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll_support);
    }

    private void geneItems() {
        if (answersesnums != null) {
            for (int i = 0; i < answersesnums.size(); ++i) {
                LableDetailsBean lable = new LableDetailsBean();
                lable.setHeadTvName(answersesnums.get(i).getCreateUserName());
                lable.setHeadTvLable(answersesnums.get(i).getContent());
                lable.setHeadIvUrl(answersesnums.get(i).getCreateUserPhotoUrl());
                lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(), answersesnums.get(i).getCreateTime()));
                lables.add(lable);
            }
        }
    }


    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_example_listview);
        if (ldeaHeaderView != null) {
            lable_details_listview.addHeaderView(ldeaHeaderView);
            lable_details_ll_support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支持文章或取消支持
                    Api.support(Integer.parseInt(contentId), zanHandler);
                }
            });
        }
        lable_details_listview.setPullLoadEnable(false);
        /*
        整体列表适配器
         */
        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.list_lable_details_commmen_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                ImageView lables_details_iv_user = holder.getView(R.id.lables_details_iv_user);
                TextView lables_details_tv_name = holder.getView(R.id.lables_details_tv_name);
                TextView lables_details_tv_zhicheng = holder.getView(R.id.lables_details_tv_zhicheng);
                TextView lables_details_tv_pushtime = holder.getView(R.id.lables_details_tv_pushtime);
                TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                lables_details_tv_name.setText(item.getHeadTvName() + "");//名称
                lables_details_tv_zhicheng.setText("康复师");//职称
                lables_details_tv_pushtime.setText(item.getReleaseTime() + "");//时间
                homepage_ll_wonderfulcontent_text.setText(item.getHeadTvLable() + "");//内容
                ImageLoader.getInstance().displayImage(item.getHeadIvUrl(), lables_details_iv_user);//评论者头像
            }
        };
        /*
        支持头像适配器
         */
        for (int i = 0; i < 5; i++) {
            stringList.add("4");
        }
        MyLablesGridViewAdapter photoCommonAdapter = new MyLablesGridViewAdapter(this, stringList);
        lable_details_listview.setAdapter(lanleCommonAdapter);
        lable_details_listview.setFocusable(false);
        lable_details_listview.setXListViewListener(this);

        lable_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LableDetaileExampleActivity.this, "给评论加评论", Toast.LENGTH_SHORT).show();
            }
        });

        lable_details_iv_discuss = (ImageView) findViewById(R.id.lable_details_iv_discuss);
        et_comment = (EditText) findViewById(R.id.et_comment);
        lable_details_ll__shared = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll__shared);
        lable_detail_ll_support_img = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_detail_ll_support_img);
        lable_detail_tv_answerordiscuss = (TextView) ldeaHeaderView.findViewById(R.id.lable_detail_tv_answerordiscuss);
        homepage_support_gridview = (GridView) ldeaHeaderView.findViewById(R.id.homepage_support_gridview);
        homepage_support_gridview.setAdapter(photoCommonAdapter);
        homepage_support_gridview.setFocusable(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(photoCommonAdapter.getCount() * LIEWIDTH,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        homepage_support_gridview.setLayoutParams(params);// 设置GirdView布局参数,横向布局的关键
        int count = photoCommonAdapter.getCount();
        homepage_support_gridview.setNumColumns(count);// 设置列数量=列表集合数
        lable_details_ll_discuss = (LinearLayout) findViewById(R.id.lable_details_ll_discuss);
        lable_details_iv_discuss.setOnClickListener(this);
        lable_details_ll__shared.setOnClickListener(this);
        lable_details_iv_discuss.setOnClickListener(this);
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
        if (contentId != null) {
            Api.getDetailContent(Integer.parseInt(contentId), contentInfoHandler);
        }
    }

    @Override
    protected void initViewContent() {
    }

    /**
     * 文章详情页面的回调
     */
    private ResponseCallback<HpWonderfulContent> contentInfoHandler = new ResponseCallback<HpWonderfulContent>() {
        @Override
        public void onSuccess(HpWonderfulContent data) {

            hideWaitingUI();
            if (data.getForumPostsAnswers() != null) {
                answersesnums = data.getForumPostsAnswers();
                geneItems();//加载评论的
            }

            ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_user);
            homepage_ll_wonderfulcontent_text.setText(data.getContent() + "");
            lables_details_tv_pushtime.setText(TimeCastUtils.compareDateTime(System.currentTimeMillis(), data.getCreateTime()));
            lables_details_tv_name.setText(data.getUserName() + "");
            lable_details_tv_support.setText(data.getStars() + "");
            lable_details_tv_supports.setText(data.getStars() + "");
            lable_details_tv__comment.setText(data.getStars() + "");
            if (data.getForumPostsImage() != null && data.getForumPostsImage().size() == 1) {
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail1);
            }
            if (data.getForumPostsImage() != null && data.getForumPostsImage().size() == 2) {
                ImageLoader.getInstance().displayImage(data.getRecommendPicUrl(), lables_details_iv_thumnnail1);
                ImageLoader.getInstance().displayImage(data.getRecommendPicUrl(), lables_details_iv_thumnnail2);
            }
            if (data.getForumPostsImage() != null && data.getForumPostsImage().size() == 3) {
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail1);
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail2);
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail3);
            }
            if (data.getForumPostsImage() != null && data.getForumPostsImage().size() == 4) {
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail1);
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail2);
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail3);
                ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_thumnnail4);
            }
            if (data.getForumPostsImage() != null && data.getTaglibs().size() == 1) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
            }
            if (data.getForumPostsImage() != null && data.getTaglibs().size() == 2) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
                tv_tag2.setText(data.getTaglibs().get(1).getTagName());
                tv_tag2.setBackgroundResource(R.color.main_background_color);
            }
            if (data.getForumPostsImage() != null && data.getTaglibs().size() == 3) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
                tv_tag2.setText(data.getTaglibs().get(1).getTagName());
                tv_tag2.setBackgroundResource(R.color.main_background_color);
                tv_tag3.setText(data.getTaglibs().get(2).getTagName());
                tv_tag3.setBackgroundResource(R.color.main_background_color);
            }
            if (data.getForumPostsImage() != null && data.getTaglibs().size() == 4) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
                tv_tag2.setText(data.getTaglibs().get(1).getTagName());
                tv_tag2.setBackgroundResource(R.color.main_background_color);
                tv_tag3.setText(data.getTaglibs().get(2).getTagName());
                tv_tag3.setBackgroundResource(R.color.main_background_color);
                tv_tag4.setText(data.getTaglibs().get(3).getTagName());
                tv_tag4.setBackgroundResource(R.color.main_background_color);
            }


        }

        @Override
        public void onFailure(String status, String message) {

            //hideWaitingUI();

            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
        }
    };

    private void hideLoadError() {
        findViewById(R.id.main_load_error).setVisibility(View.GONE);
    }

    private void showLoadError() {
        View view = findViewById(R.id.main_load_error);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLoadError();
                getData();
            }
        });
        TextView tvTips = ((TextView) findViewById(R.id.tv_error_tips));
        tvTips.setText(R.string.default_network_reload_describe);
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
                geneItems();
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
                //回复文章
                Api.addAnswerContentDetail(Integer.parseInt(contentId), et_comment.getText().toString().trim(), answerHandler);
                break;
            case R.id.lable_details_ll__shared:
                new PopupWindows(LableDetaileExampleActivity.this, lable_details_ll__shared);
                break;
            case R.id.tv_title_right_text:
                Toast.makeText(LableDetaileExampleActivity.this, "我来回答！", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
    }

    /**
     * 回复的回调
     */
    private ResponseCallback<String> answerHandler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {
            LOG.i("debug", "HpWonderfulContent---->" + data);
            //回复成功后，重新刷新列表，将新的评论刷新
            start = ++refreshCnt;
            lables.clear();
            geneItems();
            lable_details_listview.setAdapter(lanleCommonAdapter);
            onLoad();
        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };
    /**
     * 点赞回调
     */
    private ResponseCallback<String> zanHandler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {
            Toast.makeText(LableDetaileExampleActivity.this, "回复success！", Toast.LENGTH_LONG).show();
            LOG.i("debug", "HpWonderfulContent---->" + data);

        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };


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
