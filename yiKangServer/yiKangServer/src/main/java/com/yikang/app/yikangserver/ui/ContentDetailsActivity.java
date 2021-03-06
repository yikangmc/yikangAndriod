package com.yikang.app.yikangserver.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.MyLablesGridViewAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.ForumPostsAnswers;
import com.yikang.app.yikangserver.bean.ForumPostsImage;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.interfaces.NoDoubleClickListener;
import com.yikang.app.yikangserver.photo.PhotoActivitys;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by yudong on 2016/4/21.
 * 带全部评论的标签页面
 */
public class ContentDetailsActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {
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

    public static String EXTRA_LABLE_ANSWER_CONTENTISSTORE = "lableanswercontentisstore";
    public static String EXTRA_LABLE_ANSWER_CONTENTTAG = "lableanswercontentisstoretag";

    private String lablesTitle;

    private String contentId;

    private String contentIsStore;

    private String contentIsTag;

    private int lableanswer;

    private LinearLayout lable_details_ll__comment,lable_details_ll__shared, lable_details_ll_discuss,lable_detail_ll_support_img;

    private ImageView lable_details_iv_posting, lable_details_iv_discuss,lable_details_iv_discussss;

    private EditText et_comment;

    private View ldeaHeaderView;

    private TextView lable_detail_tv_answerordiscuss;

    private List<String> stringList = new ArrayList<String>();

    private List<ForumPostsAnswers> answersesnums = new ArrayList<ForumPostsAnswers>();

    private CircleImageView lables_details_iv_user;//用户头像
    private TextView lables_details_tv_name;//用户名称
    private TextView lables_details_tv_title;//标题
    private TextView lables_details_tv_zhicheng;//用户职称
    private TextView lables_details_tv_pushtime;//发布时间
    private ImageView lables_details_iv_thumnnail1;//内容图片
    private ImageView lables_details_iv_thumnnail2;//内容图片
    private ImageView lables_details_iv_thumnnail3;//内容图片
    private ImageView lables_details_iv_thumnnail4;//内容图片
    private CircleImageView homepage_support_iv1;//内容图片

    private CircleImageView homepage_support_iv2;//支持图片

    private CircleImageView homepage_support_iv3;

    private CircleImageView homepage_support_iv4;

    private CircleImageView homepage_support_iv5;

    private CircleImageView homepage_support_iv6;

    private CircleImageView homepage_support_iv7;

    private LinearLayout llkong;//内容图片
    private TextView homepage_ll_wonderfulcontent_text;//内容
    private TextView lable_details_tv_support,homepage_wonderfulcontent_supportlike;//支持数
    private TextView lable_details_tv_supports;//支持数
    private TextView lable_details_tv__comment;//评论数
    private TextView tv_tag1;//标签1
    private TextView tv_tag2;//标签2
    private TextView tv_tag3;//标签3
    private TextView tv_tag4;//标签4
    private TextView tv_more;//更多头像
    private LinearLayout lable_details_ll_support,lable_details_ll_supportlike;//点赞
    private LinearLayout ll_title;
    private int toUserId;

    private DisplayImageOptions options; //配置图片加载及显示选项
    private DisplayImageOptions option; //配置图片加载及显示选项
    private CommonAdapter<ForumPostsImage> alllableEditorCAdapter;
    private CommonAdapter<String> alllableEditorCAdapters;
    private  GridView community_mine_lables_gridview;
    private String shareTitle;

    private String shareContent;

    private String shareImage;

    private String shareUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(getApplicationContext(),"13fe60c8121ab");
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        LayoutInflater mInflater = getLayoutInflater();
        ldeaHeaderView = mInflater.inflate(R.layout.activity_lable_detail_example_headerview, null);
        initHeaderView(ldeaHeaderView);
        mHandler = new Handler();
        lablesTitle = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENT);
        lableanswer = getIntent().getIntExtra(EXTRA_LABLE_ANSWER, 0);
        contentId = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENTID);
        contentIsStore = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENTISSTORE);
        contentIsTag = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENTTAG);

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
        lables_details_iv_user = (CircleImageView) ldeaHeaderView.findViewById(R.id.lables_details_iv_user);
        lables_details_tv_name = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_name);
        lables_details_tv_title = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_title);
        lables_details_tv_zhicheng = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_zhicheng);
        lables_details_tv_pushtime = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_pushtime);
        community_mine_lables_gridview =(GridView) ldeaHeaderView.findViewById(R.id.community_mine_lables_gridview);
        homepage_support_iv1 = (CircleImageView) ldeaHeaderView.findViewById(R.id.homepage_support_iv1);
        homepage_support_iv2 = (CircleImageView) ldeaHeaderView.findViewById(R.id.homepage_support_iv2);
        homepage_support_iv3 = (CircleImageView) ldeaHeaderView.findViewById(R.id.homepage_support_iv3);
        homepage_support_iv4 = (CircleImageView) ldeaHeaderView.findViewById(R.id.homepage_support_iv4);
        homepage_support_iv5 = (CircleImageView) ldeaHeaderView.findViewById(R.id.homepage_support_iv5);
        homepage_support_iv6 = (CircleImageView) ldeaHeaderView.findViewById(R.id.homepage_support_iv6);
        homepage_support_iv7 = (CircleImageView) ldeaHeaderView.findViewById(R.id.homepage_support_iv7);
        tv_more = (TextView) ldeaHeaderView.findViewById(R.id.tv_more);
        llkong = (LinearLayout) ldeaHeaderView.findViewById(R.id.llkong);

        homepage_ll_wonderfulcontent_text = (TextView) ldeaHeaderView.findViewById(R.id.homepage_ll_wonderfulcontent_text);
        lable_details_tv_support = (TextView) ldeaHeaderView.findViewById(R.id.lable_details_tv_support);
        homepage_wonderfulcontent_supportlike = (TextView) ldeaHeaderView.findViewById(R.id.homepage_wonderfulcontent_supportlike);
        lable_details_ll_support = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll_support);
        lable_details_ll_supportlike = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll_supportlike);
        lable_details_tv_supports = (TextView) ldeaHeaderView.findViewById(R.id.lable_details_tv_supports);
        lable_details_tv__comment = (TextView) ldeaHeaderView.findViewById(R.id.lable_details_tv__comment);
        tv_tag1 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag1);
        tv_tag2 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag2);
        tv_tag3 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag3);
        tv_tag4 = (TextView) ldeaHeaderView.findViewById(R.id.tv_tag4);
        lable_details_ll_support = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll_support);
        ll_title = (LinearLayout) ldeaHeaderView.findViewById(R.id.ll_title);
        ll_title.setVisibility(View.GONE);

    }

    private void geneItems() {
        for (int i = 0; i < answersesnums.size(); ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(answersesnums.get(i).getCreateUserName());
            lable.setHeadTvLable(answersesnums.get(i).getContent());
            lable.setHeadIvUrl(answersesnums.get(i).getCreateUserPhotoUrl());
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(), answersesnums.get(i).getCreateTime()));
            lable.setCreateUserDesignationName(answersesnums.get(i).getCreateUserDesignationName());
            lable.setCreateUserPosition(answersesnums.get(i).getCreateUserPosition());
           lable.setQuestionId(answersesnums.get(i).getCreateUserId());
            lables.add(lable);
        }
    }


    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_example_listview);
        if (ldeaHeaderView != null) {
            lable_details_listview.addHeaderView(ldeaHeaderView);
            lable_details_ll_support.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    if (AppContext.getAppContext().getAccessTicket() == null) {
                        Intent intent = new Intent(ContentDetailsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else {
                        //支持文章或取消支持
                        Api.support(Integer.parseInt(contentId), zanHandler);
                    }
                }
            });
            lable_details_ll_supportlike.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //Toast.makeText(getApplication(),"取消支持",Toast.LENGTH_SHORT).show();
                    if (AppContext.getAppContext().getAccessTicket() == null) {
                        Intent intent = new Intent(ContentDetailsActivity.this, LoginActivity.class);
                        startActivity(intent);
                       // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else {
                        Api.support(Integer.parseInt(contentId), zanHandlers);
                    }
                }
            });
        }
        lable_details_listview.setPullLoadEnable(false);
        /*
        整体列表适配器
         */

        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(this, lables, R.layout.list_lable_details_commmen_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                if (lables != null) {
                    CircleImageView lables_details_iv_user = holder.getView(R.id.lables_details_iv_user);
                    LinearLayout lable_details_ll_support = holder.getView(R.id.lable_details_ll_support);
                    TextView lables_details_tv_name = holder.getView(R.id.lables_details_tv_name);
                    TextView lables_details_tv_zhicheng = holder.getView(R.id.lables_details_tv_zhicheng);
                    TextView lables_details_tv_pushtime = holder.getView(R.id.lables_details_tv_pushtime);
                    TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                    GridView comment_lables_gridview = holder.getView(R.id.comment_lables_gridview);
                    LinearLayout ll_arrow = holder.getView(R.id.ll_arrow);
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
                    lables_details_tv_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),
                                    PersonalHomepageActivity.class);
                            intent.putExtra("userId",item.getQuestionId()+"");
                            startActivity(intent);
                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                        }
                    });

                    lables_details_tv_name.setText(item.getHeadTvName() + "");//名称
                    lables_details_tv_pushtime.setText(item.getReleaseTime() + "");//时间
                    homepage_ll_wonderfulcontent_text.setText(item.getHeadTvLable() + "");//内容
                    ImageLoader.getInstance().displayImage(item.getHeadIvUrl(), lables_details_iv_user,options);//评论者头像
                    lable_details_ll_support.setVisibility(View.GONE);
                    if(item.getCreateUserPosition()>0) {
                        PositionUtils.getPosition(item.getCreateUserPosition(), lables_details_tv_zhicheng);
                        lables_details_tv_zhicheng.setText("");
                    }
                    if(item.getCreateUserPosition()==0){
                        lables_details_tv_zhicheng.setText(item.getCreateUserDesignationName());
                        if(item.getCreateUserDesignationName()!=null) {

                            lables_details_tv_zhicheng.setBackgroundResource(R.drawable.allradius_zhicheng);
                            //lables_details_tv_zhicheng.setPadding(15,2,15,2);
                        }
                    }
                }
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
               // Toast.makeText(ContentDetailsActivity.this, "给评论加评论", Toast.LENGTH_SHORT).show();
            }
        });

        lable_details_iv_discuss = (ImageView) findViewById(R.id.lable_details_iv_discuss);
        lable_details_iv_discussss = (ImageView) findViewById(R.id.lable_details_iv_discussss);
        et_comment = (EditText) findViewById(R.id.et_comment);

        et_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    {
                        if (AppContext.getAppContext().getAccessTicket() == null) {
                            Intent intent = new Intent(ContentDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                }

                return false;
            }
        });

        if(contentIsTag!=null&&contentIsTag.equals("TAG")){
            // 获取编辑框焦点
            et_comment.setFocusable(true);
            et_comment.setFocusableInTouchMode(true);
            et_comment.requestFocus();

            //打开软键盘
            InputMethodManager imm = (InputMethodManager)ContentDetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lable_details_iv_discuss.setVisibility(View.VISIBLE);
                lable_details_iv_discussss.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                lable_details_iv_discuss.setVisibility(View.GONE);
                lable_details_iv_discussss.setVisibility(View.VISIBLE);
                if(s.length()==0){
                    lable_details_iv_discuss.setVisibility(View.VISIBLE);
                    lable_details_iv_discussss.setVisibility(View.GONE);
                }
            }
        });
        lable_details_ll__comment = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll__comment);
        lable_details_ll__shared = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll__shared);
        lable_detail_ll_support_img = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_detail_ll_support_img);
        lable_detail_tv_answerordiscuss = (TextView) ldeaHeaderView.findViewById(R.id.lable_detail_tv_answerordiscuss);

        lable_details_ll_discuss = (LinearLayout) findViewById(R.id.lable_details_ll_discuss);
        lable_details_ll__shared.setOnClickListener(this);
        lable_details_ll__comment.setOnClickListener(this);
        //lable_details_iv_discuss.setOnClickListener(this);
        lable_details_iv_discussss.setOnClickListener(this);
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
     * 帖子详情页面的回调
     */
    private ResponseCallback<HpWonderfulContent> contentInfoHandler = new ResponseCallback<HpWonderfulContent>() {

        @Override
        public void onSuccess(final HpWonderfulContent data) {
            hideWaitingUI();
            if(data.getContent().length()>30) {
                shareContent = data.getContent().substring(0, 30) + "...";
            }else {
                shareContent = data.getContent()+"";
            }
            shareTitle=data.getTitle() + "";
            shareUrls=data.getShareUrl() + "";
            if(data.getForumPostsImage()==null){
                llkong.setVisibility(View.GONE);
            }
            if(data.getForumPostsImage()!=null&&data.getForumPostsImage().size()!=0){
                llkong.setVisibility(View.VISIBLE);
                shareImage=data.getForumPostsImage().get(0).getImageUrl();
            }
            alllableEditorCAdapter =new CommonAdapter<ForumPostsImage>(getApplicationContext(),data.getForumPostsImage(),R.layout.pic_layout) {
                @Override
                protected void convert(ViewHolder holder, final ForumPostsImage item) {
                    ImageView lable =(ImageView)holder.getView(R.id.lables_top_textview);
                    ImageLoader.getInstance().displayImage(item.getImageUrl(), lable,option);
                }
            };
            lables_details_iv_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),
                            PersonalHomepageActivity.class);
                    intent.putExtra("userId",data.getCreateUserId()+"");
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
            });
            lables_details_tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),
                            PersonalHomepageActivity.class);
                    intent.putExtra("userId",data.getCreateUserId()+"");
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
            });
            community_mine_lables_gridview.setAdapter(alllableEditorCAdapter);
            community_mine_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ContentDetailsActivity.this,
                            PhotoActivitys.class);

                    intent.putExtra("URL", (Serializable) data.getForumPostsImage());
                    intent.putExtra("ID",position);

                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });

            toUserId=data.getCreateUserId();

            if(data.getUserPosition()>0) {
                PositionUtils.getPosition(data.getUserPosition(), lables_details_tv_zhicheng);
                lables_details_tv_zhicheng.setText("");
                lables_details_tv_zhicheng.setBackgroundResource(R.color.transparent);
            }
            if(data.getUserPosition()==0){
                lables_details_tv_zhicheng.setText(data.getDesignationName());
                if(data.getDesignationName()!=null) {
                    lables_details_tv_zhicheng.setBackgroundResource(R.drawable.allradius_zhicheng);
                   // lables_details_tv_zhicheng.setPadding(15,2,15,2);
                }
            }
            ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_user,options);

            if(data.getFormPostsStarLists()!=null) {
                if(data.getFormPostsStarLists().size()==1){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==2){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==3){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==4){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==5){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==6){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(5).getPhotoUrl(), homepage_support_iv6,options);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()>=7){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(5).getPhotoUrl(), homepage_support_iv6,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(6).getPhotoUrl(), homepage_support_iv7,options);
                    tv_more.setVisibility(View.VISIBLE);
                }


            }
            if(data.getIsStar()==0) {
                lable_details_ll_support.setVisibility(View.VISIBLE);
                lable_details_tv_support.setText(data.getStars() + "");
                lable_details_ll_supportlike.setVisibility(View.GONE);
            }
            if(data.getIsStar()==1){
                lable_details_ll_support.setVisibility(View.GONE);
                lable_details_ll_supportlike.setVisibility(View.VISIBLE);
                homepage_wonderfulcontent_supportlike.setText(data.getStars() + "");
            }
            homepage_ll_wonderfulcontent_text.setText(data.getForumPostDetailContent() + "");
            lables_details_tv_pushtime.setText(TimeCastUtils.compareDateTime(System.currentTimeMillis(), data.getCreateTime()));
            lables_details_tv_name.setText(data.getUserName() + "");
            lable_details_tv_supports.setText(data.getStars() + "");
            lable_details_tv__comment.setText(data.getAnswersNums() + "");
            lables_details_tv_title.setText(data.getTitle() + "");

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
            if (data.getForumPostsAnswers() != null) {
                answersesnums = data.getForumPostsAnswers();
                geneItems();//加载评论的
            }

        }

        @Override
        public void onFailure(String status, String message) {

            //hideWaitingUI();

            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
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
                getData();
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

                if(!et_comment.getText().toString().trim().equals("")) {
                    //回复帖子
                    showWaitingUI();
                    //回复文章
                     Api.addAnswerContentDetail(Integer.parseInt(contentId), et_comment.getText().toString().trim(), toUserId, answerHandler);
                }else{
                    Toast.makeText(ContentDetailsActivity.this, "评论不能为空", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.lable_details_iv_discussss:

                if(!et_comment.getText().toString().trim().equals("")) {
                    //回复帖子
                    showWaitingUI();
                    //回复文章
                    Api.addAnswerContentDetail(Integer.parseInt(contentId), et_comment.getText().toString().trim(), toUserId, answerHandler);
                }else{
                    Toast.makeText(ContentDetailsActivity.this, "评论不能为空", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.lable_details_ll__shared:
               // new PopupWindows(ContentDetailsActivity.this, lable_details_ll__shared);
               /* ShareBitmapUtils.setPicPosition(shareImage);
                showShare();*/
                break;
            case R.id.tv_title_right_text:
                Toast.makeText(ContentDetailsActivity.this, "我来回答！", Toast.LENGTH_LONG).show();
                break;
            case R.id.lable_details_ll__comment:
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(ContentDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    // 获取编辑框焦点
                    et_comment.setFocusable(true);
                    et_comment.setFocusableInTouchMode(true);
                    et_comment.requestFocus();

                    //打开软键盘
                    InputMethodManager imm = (InputMethodManager) ContentDetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 回复的回调
     */
    private ResponseCallback<Void> answerHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            et_comment.setText("");
            InputMethodManager imm = (InputMethodManager)ContentDetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            //关闭软键盘
            imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
           // LOG.i("debug", "HpWonderfulContent---->" + data);
           // AppContext.showToast("回复成功");
            //回复成功后，重新刷新列表，将新的评论刷新
            start = ++refreshCnt;
            lables.clear();
            getData();
            lable_details_listview.setAdapter(lanleCommonAdapter);
            onLoad();
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };
    /**
     * 点赞回调
     */
    private ResponseCallback<HpWonderfulContent> zanHandler = new ResponseCallback<HpWonderfulContent>() {



        @Override
        public void onSuccess(HpWonderfulContent data) {
           // Toast.makeText(ContentDetailsActivity.this, "点赞success！", Toast.LENGTH_LONG).show();
            LOG.i("debug", "HpWonderfulContent---->" + data);
            lable_details_ll_supportlike.setVisibility(View.VISIBLE);
            lable_details_ll_support.setVisibility(View.GONE);
            homepage_wonderfulcontent_supportlike.setText(data.getStars() + "" );
            lable_details_tv_supports.setText(data.getStars() + "");
            if(data.getFormPostsStarLists()!=null) {

                if(data.getFormPostsStarLists().size()==1){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.INVISIBLE);
                    homepage_support_iv3.setVisibility(View.INVISIBLE);
                    homepage_support_iv4.setVisibility(View.INVISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==2){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.INVISIBLE);
                    homepage_support_iv4.setVisibility(View.INVISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==3){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.INVISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==4){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==5){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.VISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==6){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(5).getPhotoUrl(), homepage_support_iv6,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.VISIBLE);
                    homepage_support_iv6.setVisibility(View.VISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);

                }
                if(data.getFormPostsStarLists().size()>=7){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(5).getPhotoUrl(), homepage_support_iv6,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(6).getPhotoUrl(), homepage_support_iv7,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.VISIBLE);
                    homepage_support_iv6.setVisibility(View.VISIBLE);
                    homepage_support_iv7.setVisibility(View.VISIBLE);
                    tv_more.setVisibility(View.VISIBLE);

                }


            }
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };



    /**
     * 取消点赞回调
     */
    private ResponseCallback<HpWonderfulContent> zanHandlers = new ResponseCallback<HpWonderfulContent>() {



        @Override
        public void onSuccess(HpWonderfulContent data) {
            //Toast.makeText(ContentDetailsActivity.this, "取消点赞success！", Toast.LENGTH_LONG).show();
            //LOG.i("debug", "HpWonderfulContent---->" + data);
            lable_details_ll_supportlike.setVisibility(View.GONE);
            lable_details_ll_support.setVisibility(View.VISIBLE);
            lable_details_tv_support.setText(data.getStars() + "" );
            lable_details_tv_supports.setText(data.getStars() + "");
            if(data.getFormPostsStarLists().size()==0){
                homepage_support_iv1.setVisibility(View.INVISIBLE);
                homepage_support_iv2.setVisibility(View.INVISIBLE);
                homepage_support_iv3.setVisibility(View.INVISIBLE);
                homepage_support_iv4.setVisibility(View.INVISIBLE);
                homepage_support_iv5.setVisibility(View.INVISIBLE);
                homepage_support_iv6.setVisibility(View.INVISIBLE);
                homepage_support_iv7.setVisibility(View.INVISIBLE);
            }
            if(data.getFormPostsStarLists()!=null) {

                if(data.getFormPostsStarLists().size()==1){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.INVISIBLE);
                    homepage_support_iv3.setVisibility(View.INVISIBLE);
                    homepage_support_iv4.setVisibility(View.INVISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==2){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.INVISIBLE);
                    homepage_support_iv4.setVisibility(View.INVISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==3){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.INVISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==4){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.INVISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==5){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.VISIBLE);
                    homepage_support_iv6.setVisibility(View.INVISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);
                }
                if(data.getFormPostsStarLists().size()==6){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(5).getPhotoUrl(), homepage_support_iv6,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.VISIBLE);
                    homepage_support_iv6.setVisibility(View.VISIBLE);
                    homepage_support_iv7.setVisibility(View.INVISIBLE);
                    tv_more.setVisibility(View.INVISIBLE);

                }
                if(data.getFormPostsStarLists().size()>=7){
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(0).getPhotoUrl(), homepage_support_iv1,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(1).getPhotoUrl(), homepage_support_iv2,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(2).getPhotoUrl(), homepage_support_iv3,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(3).getPhotoUrl(), homepage_support_iv4,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(4).getPhotoUrl(), homepage_support_iv5,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(5).getPhotoUrl(), homepage_support_iv6,options);
                    ImageLoader.getInstance().displayImage(data.getFormPostsStarLists().get(6).getPhotoUrl(), homepage_support_iv7,options);
                    homepage_support_iv1.setVisibility(View.VISIBLE);
                    homepage_support_iv2.setVisibility(View.VISIBLE);
                    homepage_support_iv3.setVisibility(View.VISIBLE);
                    homepage_support_iv4.setVisibility(View.VISIBLE);
                    homepage_support_iv5.setVisibility(View.VISIBLE);
                    homepage_support_iv6.setVisibility(View.VISIBLE);
                    homepage_support_iv7.setVisibility(View.VISIBLE);
                    tv_more.setVisibility(View.VISIBLE);

                }


            }
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(shareTitle);
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(shareUrls);
// text是分享文本，所有平台都需要这个字段
        oks.setText(shareContent);
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/pepper/test.png");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareUrls);
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareUrls);

// 启动分享GUI
        oks.show(this);
    }
    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_lable_shared_popupwindows, null);
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
                    Toast.makeText(ContentDetailsActivity.this, "我要举报！", Toast.LENGTH_LONG).show();
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
