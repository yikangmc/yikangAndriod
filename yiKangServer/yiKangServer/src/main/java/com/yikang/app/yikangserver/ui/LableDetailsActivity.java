package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import com.yikang.app.yikangserver.interfaces.NoDoubleClickListener;
import com.yikang.app.yikangserver.photo.PhotoActivitys;
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.io.Serializable;
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
    private int start = 1;
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
    private List<HpWonderfulContent> bannerHp = new ArrayList<>();
    private boolean flag;
    private DisplayImageOptions options; //配置图片加载及显示选项
    private DisplayImageOptions option; //配置图片加载及显示选项
    private CommonAdapter<ForumPostsImage> alllableEditorCAdapter;
    private ResponseCallback<HpWonderfulContent> zanHandler;
    private ResponseCallback<HpWonderfulContent> zanHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(true)
                .cacheOnDisc(true)
               // .displayer(new FadeInBitmapDisplayer(500))
                .build();
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                //.displayer(new FadeInBitmapDisplayer(500))
                .build();
        mHandler = new Handler();
        showWaitingUI();
        lablesTitle = getIntent().getStringExtra(EXTRA_LABLE);
        lablesId = getIntent().getStringExtra(EXTRA_LABLE_ID);
        contentIsStore = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENTISSTORE);
        initContent();

        initTitleBar(lablesTitle);
    }


    private void geneItems(String name) {
        for (int i = 0; i < bannerHp.size(); ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(bannerHp.get(i).getUserName());
             lable.setIsStar(bannerHp.get(i).getIsStar());
            lable.setDetailTv(bannerHp.get(i).getTitle());
            lable.setDetailLable(bannerHp.get(i).getContent());
            lable.setDetailSupport(bannerHp.get(i).getStars() + "");
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(), bannerHp.get(i).getCreateTime()));
            lable.setAnswersNums(bannerHp.get(i).getAnswersNums() + "");
            lable.setDetailIvUrls(bannerHp.get(i).getForumPostsImage());
            lable.setDetailTaglibs(bannerHp.get(i).getTaglibs());
            lable.setHeadIvUrl(bannerHp.get(i).getPhotoUrl());
            lable.setHeadTvLable(bannerHp.get(i).getDesignationName());
            lable.setUserPositions(bannerHp.get(i).getUserPosition());
            lable.setIsStore(bannerHp.get(i).getIsStore());
            lable.setDetailDiscuss(bannerHp.get(i).getForumPostId()+"");
            lable.setQuestionId(bannerHp.get(i).getForumPostId());
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
            // AppContext.showToast("关注成功");
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_listview);
        personal_homepage_focuson_iv = (ImageView) findViewById(R.id.personal_homepage_focuson_iv);
        personal_homepage_focuson_iv_already = (ImageView) findViewById(R.id.personal_homepage_focuson_iv_already);

        lable_details_listview.setPullLoadEnable(true);
        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.list_lable_details_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_details_tv_name);
                TextView lables_details_tv_title = holder.getView(R.id.lables_details_tv_title);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                final TextView lable_details_tv_support = holder.getView(R.id.lable_details_tv_support);
                TextView lable_details_tv__comment = holder.getView(R.id.lable_details_tv__comment);
                TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                final TextView homepage_wonderfulcontent_supportlike = holder.getView(R.id.homepage_wonderfulcontent_supportlike);
                LinearLayout llkong = holder.getView(R.id.llkong);
                final LinearLayout lable_details_ll_support = holder.getView(R.id.lable_details_ll_support);
                final LinearLayout lable_details_ll_supportlike = holder.getView(R.id.lable_details_ll_supportlike);
                final LinearLayout lable_details_ll__comment = holder.getView(R.id.lable_details_ll__comment);
                CircleImageView lables_details_iv_user = holder.getView(R.id.lables_details_iv_user);

                TextView tv_tag1 = (TextView) holder.getView(R.id.tv_tag1);
                TextView tv_tag2 = (TextView) holder.getView(R.id.tv_tag2);
                TextView tv_tag3 = (TextView) holder.getView(R.id.tv_tag3);
                TextView tv_tag4 = (TextView) holder.getView(R.id.tv_tag4);
                lable_details_ll__comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppContext.getAppContext().getAccessTicket() == null) {
                            Intent intent = new Intent(LableDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else {
                            // AppContext.showToast("评论");
                            Intent intent = new Intent(LableDetailsActivity.this, ContentDetailsActivity.class);
                            if (lables != null) {
                                intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTID, item.getDetailDiscuss() + "");
                                intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, item.getIsStore() + "");
                                intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENT, item.getHeadTvName() + "");
                                intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTTAG, "TAG");
                            }
                            startActivity(intent);
                        }

                    }
                });
                lable_details_ll_support.setOnClickListener(new NoDoubleClickListener(){
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (AppContext.getAppContext().getAccessTicket() == null) {
                            Intent intent = new Intent(LableDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else {
                            Api.support(Integer.parseInt(item.getQuestionId()+""), new ResponseCallback<HpWonderfulContent>() {

                                @Override
                                public void onSuccess(HpWonderfulContent data) {

                                    // LOG.i("debug", "HpWonderfulContent---->" + data);
                                    lable_details_ll_supportlike.setVisibility(View.VISIBLE);
                                    lable_details_ll_support.setVisibility(View.GONE);
                                    homepage_wonderfulcontent_supportlike.setText(data.getStars() + "" );
                                    item.setDetailSupport(data.getStars() + "");
                                    item.setIsStar(data.getIsStar());
                                    lable_details_tv_support.setText(data.getStars() + "");
                                    lanleCommonAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(String status, String message) {
                                    // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                                    AppContext.showToast(message);
                                }
                            });
                        }


                    }
                });

                lable_details_ll_supportlike.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (AppContext.getAppContext().getAccessTicket() == null) {
                            Intent intent = new Intent(LableDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else {
                            Api.support(Integer.parseInt(item.getQuestionId()+""), new ResponseCallback<HpWonderfulContent>() {

                                @Override
                                public void onSuccess(HpWonderfulContent data) {
                                    //  LOG.i("debug", "HpWonderfulContent---->" + data);
                                    lable_details_ll_supportlike.setVisibility(View.GONE);
                                    lable_details_ll_support.setVisibility(View.VISIBLE);
                                    item.setDetailSupport(data.getStars() + "");
                                    item.setIsStar(data.getIsStar());
                                    lable_details_tv_support.setText(data.getStars() + "" );
                                    homepage_wonderfulcontent_supportlike.setText(data.getStars() + "");
                                    lanleCommonAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(String status, String message) {
                                    // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                                    AppContext.showToast(message);
                                }
                            });
                        }


                    }
                });

                lables_tv_title.setText(item.getHeadTvName());
                if (item.getUserPositions() > 0) {
                    PositionUtils.getPosition(item.getUserPositions(), lables_tv_tiezi);
                    lables_tv_tiezi.setText("");
                    lables_tv_tiezi.setBackgroundResource(R.color.transparent);
                }
                if (item.getUserPositions() == 0) {
                    lables_tv_tiezi.setText(item.getHeadTvLable());
                    if (item.getHeadTvLable() != null) {

                        lables_tv_tiezi.setBackgroundResource(R.drawable.allradius_zhicheng);
                        // lables_tv_tiezi.setPadding(15,2,15,2);

                    }
                }
                lables_tv_time.setText(item.getReleaseTime());
                lable_details_tv__comment.setText(item.getAnswersNums());
                lable_details_tv_support.setText(item.getDetailSupport());

                if(item.getIsStar()==0) {
                    lable_details_ll_support.setVisibility(View.VISIBLE);
                    lable_details_tv_support.setText(item.getDetailSupport() + "");
                    lable_details_ll_supportlike.setVisibility(View.GONE);
                }
                if(item.getIsStar()==1){
                    lable_details_ll_support.setVisibility(View.GONE);
                    lable_details_ll_supportlike.setVisibility(View.VISIBLE);
                    homepage_wonderfulcontent_supportlike.setText(item.getDetailSupport() + "");
                }

                homepage_ll_wonderfulcontent_text.setText(item.getDetailLable());
                final List<ForumPostsImage> detailIvUrls = item.getDetailIvUrls();
                List<Taglibs> detailTaglibs = item.getDetailTaglibs();
                ImageLoader.getInstance().displayImage(item.getHeadIvUrl(), lables_details_iv_user, option);
                GridView community_mine_lables_gridview = holder.getView(R.id.community_mine_lables_gridview);
                if (detailIvUrls == null) {
                    llkong.setVisibility(View.GONE);
                }
                if (detailIvUrls != null) {
                    llkong.setVisibility(View.VISIBLE);
                }
                alllableEditorCAdapter = new CommonAdapter<ForumPostsImage>(getApplicationContext(), detailIvUrls, R.layout.pic_layout) {
                    @Override
                    protected void convert(ViewHolder holder, final ForumPostsImage item) {
                        ImageView lable = (ImageView) holder.getView(R.id.lables_top_textview);
                        ImageLoader.getInstance().displayImage(item.getImageUrl(), lable, options);
                    }
                };

                community_mine_lables_gridview.setAdapter(alllableEditorCAdapter);
                community_mine_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(LableDetailsActivity.this,
                                PhotoActivitys.class);

                        intent.putExtra("URL", (Serializable) detailIvUrls);
                        intent.putExtra("ID", position);

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

                Intent intent = new Intent(LableDetailsActivity.this, ContentDetailsActivity.class);
                if (lables != null) {
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(i - 1).getDetailDiscuss() + "");
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, lables.get(i - 1).getIsStore() + "");
                    intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENT, lables.get(i - 1).getHeadTvName() + "");
                }
                startActivity(intent);
            }
        });

        lable_details_iv_posting = (ImageView) findViewById(R.id.lable_details_iv_posting);
        lable_details_ll_posting = (LinearLayout) findViewById(R.id.lable_details_ll_posting);
        lable_details_iv_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(LableDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Intent intent1 = new Intent(getApplicationContext(),
                            PublishLablesActivity.class);  //发布帖子
                    intent1.putExtra(PublishLablesActivity.EXTRA_LABLE_ANSWER_CONTENTID, lablesId + "");
                    intent1.putExtra(PublishLablesActivity.EXTRA_LABLE_EXAMPLENAMES, lablesTitle);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
            }
        });
    }




    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lable_details);
    }

    @Override
    protected void getData() {
        start = 1;
        Api.getAllLableContent(Integer.parseInt(lablesId),1, allLableContentHandler);
        Api.getTaglibId(Integer.parseInt(lablesId), new ResponseCallback<Taglibs>() {
            @Override
            public void onSuccess(Taglibs data) {
                if (data.getIsStore()==0) {
                    personal_homepage_focuson_iv.setVisibility(View.VISIBLE);
                    personal_homepage_focuson_iv_already.setVisibility(View.GONE);
                }
                if (data.getIsStore()==1) {
                    personal_homepage_focuson_iv.setVisibility(View.GONE);
                    personal_homepage_focuson_iv_already.setVisibility(View.VISIBLE);
                }
                personal_homepage_focuson_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppContext.getAppContext().getAccessTicket() == null) {
                            Intent intent = new Intent(LableDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else {
                            showWaitingUI();
                            Api.addMyFocusLables(Integer.parseInt(lablesId), addMyFocusLablesHandler);
                        }

                    }
                });
            }

            @Override
            public void onFailure(String status, String message) {
                personal_homepage_focuson_iv.setVisibility(View.VISIBLE);
                personal_homepage_focuson_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppContext.getAppContext().getAccessTicket() == null) {
                            Intent intent = new Intent(LableDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }

                    }
                });
            }
        });
    }

    /**
     * 某一个标签下的所有内容回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();
            lable_details_listview.setAdapter(lanleCommonAdapter);
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
            onLoad();
            //LOG.i("debug", "HpWonderfulContent---->" + data + "嘎嘎嘎");

        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };


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
        getData();

    }

    @Override
    public void onLoadMore() {
        start += 1;
        getDataMore();
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
    protected void getDataMore() {

        //获取标签下的所有专业内容
        Api.getAllLableContent(Integer.parseInt(lablesId), start, allLableContentHandlers);
    }
}
