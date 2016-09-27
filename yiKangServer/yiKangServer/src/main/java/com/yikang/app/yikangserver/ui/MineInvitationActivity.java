package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yikang.app.yikangserver.bean.ForumPostsImage;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.Taglibs;
import com.yikang.app.yikangserver.photo.PhotoActivitys;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/21.
 * 我的帖子
 */
public class MineInvitationActivity extends BaseActivity implements XListView.IXListViewListener {

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
    public static String EXTRA_LABLE_ANSWER_CONTENTISSTORE = "lableanswercontentisstore";
    private String lablesTitle;
    private String lablesId;
    private String contentIsStore;
    private LinearLayout lable_details_ll_posting;
    private ImageView lable_details_iv_posting;
    private ImageView personal_homepage_focuson_iv, personal_homepage_focuson_iv_already;
    List<HpWonderfulContent> bannerHp = new ArrayList<>();
    private boolean flag;
    private DisplayImageOptions options; //配置图片加载及显示选项
    private CommonAdapter<ForumPostsImage> alllableEditorCAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initContent();

        initTitleBar("我的帖子");
    }

    private void geneItems(String name) {
        for (int i = 0; i < bannerHp.size(); ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(bannerHp.get(i).getUserName());
            lable.setHeadTvLable("路人甲");
            lable.setDetailTv(bannerHp.get(i).getTitle());
            lable.setDetailLable(bannerHp.get(i).getContent());
            lable.setDetailSupport(bannerHp.get(i).getStars() + "");
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(), bannerHp.get(i).getCreateTime()));
            lable.setAnswersNums(bannerHp.get(i).getAnswersNums() + "");
            lable.setDetailIvUrls(bannerHp.get(i).getForumPostsImage());
            lable.setDetailTaglibs(bannerHp.get(i).getTaglibs());
            lables.add(lable);
        }
    }

    private ResponseCallback<Void> addMyFocusLablesHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            personal_homepage_focuson_iv.setVisibility(View.GONE);
            personal_homepage_focuson_iv_already.setVisibility(View.VISIBLE);
           // LOG.i("debug", "HpWonderfulContent---->" + data);
            AppContext.showToast("关注成功");
        }

        @Override
        public void onFailure(String status, String message) {
          //  LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_listview);
        personal_homepage_focuson_iv = (ImageView) findViewById(R.id.personal_homepage_focuson_iv);
        personal_homepage_focuson_iv_already = (ImageView) findViewById(R.id.personal_homepage_focuson_iv_already);
        personal_homepage_focuson_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitingUI();
                Api.addMyFocusLables(Integer.parseInt(lablesId), addMyFocusLablesHandler);

            }
        });
        lable_details_listview.setPullLoadEnable(false);
        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.list_lable_my_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
               // TextView lables_tv_title = holder.getView(R.id.lables_details_tv_name);
                TextView lables_details_tv_title = holder.getView(R.id.lables_details_tv_title);
               // TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                TextView lable_details_tv_support = holder.getView(R.id.homepage_wonderfulcontent_support);
                TextView lable_details_tv__comment = holder.getView(R.id.homepage_wonderfulcontent_comment);
                TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                GridView community_mine_lables_gridview = holder.getView(R.id.community_mine_lables_gridview);
                TextView tv_tag1 = (TextView) holder.getView(R.id.tv_tag1);
                TextView tv_tag2 = (TextView) holder.getView(R.id.tv_tag2);
                TextView tv_tag3 = (TextView) holder.getView(R.id.tv_tag3);
                TextView tv_tag4 = (TextView) holder.getView(R.id.tv_tag4);
                lables_details_tv_title.setText(item.getDetailTv());
                lables_tv_time.setText(item.getReleaseTime());
                lable_details_tv__comment.setText(item.getAnswersNums()+"");
                lable_details_tv_support.setText(item.getDetailSupport()+"");
                homepage_ll_wonderfulcontent_text.setText(item.getDetailLable());
                final List<ForumPostsImage> detailIvUrls = item.getDetailIvUrls();
                List<Taglibs> detailTaglibs = item.getDetailTaglibs();

                alllableEditorCAdapter =new CommonAdapter<ForumPostsImage>(getApplicationContext(),detailIvUrls,R.layout.pic_layout) {
                    @Override
                    protected void convert(ViewHolder holder, final ForumPostsImage item) {
                        ImageView lable =(ImageView)holder.getView(R.id.lables_top_textview);
                        ImageLoader.getInstance().displayImage(item.getImageUrl(), lable,options);
                    }
                };

                community_mine_lables_gridview.setAdapter(alllableEditorCAdapter);
                community_mine_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(),
                                PhotoActivitys.class);

                        intent.putExtra("URL", (Serializable) detailIvUrls);
                        intent.putExtra("ID",position);

                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                if (detailTaglibs != null && detailTaglibs.size() == 1) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                }
                if (detailTaglibs != null && detailTaglibs.size() == 2) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(detailTaglibs.get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                }
                if (detailTaglibs != null && detailTaglibs.size() == 3) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(detailTaglibs.get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                    tv_tag3.setText(detailTaglibs.get(2).getTagName());
                    tv_tag3.setBackgroundResource(R.color.main_background_color);
                }
                if (detailTaglibs != null && detailTaglibs.size() == 4) {
                    tv_tag1.setText(detailTaglibs.get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(detailTaglibs.get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                    tv_tag3.setText(detailTaglibs.get(2).getTagName());
                    tv_tag3.setBackgroundResource(R.color.main_background_color);
                    tv_tag4.setText(detailTaglibs.get(3).getTagName());
                    tv_tag4.setBackgroundResource(R.color.main_background_color);
                }


            }
        };

        lable_details_listview.setXListViewListener(this);

        lable_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MineInvitationActivity.this, ContentDetailsActivity.class);
                if (bannerHp != null) {
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTID, bannerHp.get(i - 1).getForumPostId() + "");
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, bannerHp.get(i - 1).getIsStore() + "");
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENT, AppContext.getAppContext().getUser().getName()+"");
                }
                startActivity(intent);
            }
        });

        lable_details_iv_posting = (ImageView) findViewById(R.id.lable_details_iv_posting);
        lable_details_ll_posting = (LinearLayout) findViewById(R.id.lable_details_ll_posting);
        lable_details_ll_posting.setVisibility(View.GONE);
        lable_details_iv_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),
                        PublishLablesActivity.class);  //发布帖子
                intent1.putExtra(PublishLablesActivity.EXTRA_LABLE_ANSWER_CONTENTID, lablesId + "");
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
        start = 1;
        Api.getMyTiezi(Long.parseLong( AppContext.getAppContext().getUser().getUserId() + ""), 1,allLableContentHandler);
    }

    /**
     * 某一个标签下的所有文章回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();
            if(data.size()<10){
                lable_details_listview.setPullLoadEnable(false);
            }else {
                lable_details_listview.setPullLoadEnable(true);
            }
            onLoad();
            bannerHp = new ArrayList<>();
            for (HpWonderfulContent hp : data) {
                bannerHp.add(hp);
            }
            lables.clear();
            geneItems(lablesTitle + lablesId);
            lable_details_listview.setAdapter(lanleCommonAdapter);
           // LOG.i("debug", "HpWonderfulContent---->" + data + "嘎嘎嘎");

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
        lable_details_listview.stopRefresh();
        lable_details_listview.stopLoadMore();
        lable_details_listview.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        start = 1;
        Api.getMyTiezi(Long.parseLong( AppContext.getAppContext().getUser().getUserId() + ""), 1,allLableContentHandler);
    }

    @Override
    public void onLoadMore() {
        start += 1;
        getDataMore();
    }
    protected void getDataMore() {

        //获取标签下的所有专业内容
        Api.getMyTiezi(Long.parseLong( AppContext.getAppContext().getUser().getUserId() + ""), start, allLableContentHandlers);
    }
    private ResponseCallback<List<HpWonderfulContent>> allLableContentHandlers = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();
            if(data.size()<10){
                lable_details_listview.setPullLoadEnable(false);
            }else {
                lable_details_listview.setPullLoadEnable(true);
            }
            onLoad();

            bannerHp = new ArrayList<>();
            for (HpWonderfulContent hp : data) {
                bannerHp.add(hp);
            }
            geneItems(lablesTitle + lablesId);
            lanleCommonAdapter.notifyDataSetChanged();
            // LOG.i("debug", "HpWonderfulContent---->" + data + "嘎嘎嘎");

        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

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

        /*Animation mShowAction = AnimationUtils.loadAnimation(this, R.anim.trans_bottom_in);
        lable_details_ll_posting.startAnimation(mShowAction);*/
        lable_details_ll_posting.setVisibility(View.GONE);
    }

}
