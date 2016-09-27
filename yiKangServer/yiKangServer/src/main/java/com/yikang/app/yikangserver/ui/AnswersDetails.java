package com.yikang.app.yikangserver.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.QuestionAnswers;
import com.yikang.app.yikangserver.bean.QuestionImages;
import com.yikang.app.yikangserver.photo.PhotoActivitys;
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
 *
 */
public class AnswersDetails extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {
    /*
    支持头像列表
     */
    private GridView homepage_support_gridview;

    private XListView lable_details_listview;

    private ImageView lable_details_iv_posting, lable_details_iv_discuss;

    private EditText et_comment;

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

    private String contentTitle;

    private int lableanswer;

    private LinearLayout lable_details_ll__shared, lable_details_ll_discuss, lable_detail_ll_support_img;

    private View ldeaHeaderView;

    private TextView lable_detail_tv_answerordiscuss;

    private List<String> stringList = new ArrayList<String>();

    private List<QuestionAnswers> answersesnums = new ArrayList<QuestionAnswers>();

    private CircleImageView lables_details_iv_user;//用户头像
    private TextView lables_details_tv_name;//用户名称
    private TextView lables_details_tv_title;//问题标题
    private TextView lables_details_tv_zhicheng;//用户职称
    private TextView lables_details_tv_pushtime;//发布时间
    private TextView homepage_ll_wonderfulcontent_text;//内容
    private TextView lable_details_tv_support;//支持数
    private TextView lable_details_tv_supports;//支持数
    private TextView lable_details_tv__comment;//评论数
    private TextView tv_tag1;//标签1
    private TextView tv_tag2;//标签2
    private TextView tv_tag3;//标签3
    private TextView tv_tag4;//标签4
    private LinearLayout lable_details_ll_support;//点赞
    private View view_line;//点赞
    private LinearLayout ll_images;//图片列表
    private DisplayImageOptions options; //配置图片加载及显示选项
    private DisplayImageOptions option; //配置图片加载及显示选项
    private CommonAdapter<QuestionImages> alllableEditorCAdapter;
    private  GridView community_mine_lables_gridview;
    private String shareTitle;

    private String shareContent;

    private String shareImage;
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
        lablesTitle = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENT);
        lableanswer = getIntent().getIntExtra(EXTRA_LABLE_ANSWER, 0);
        contentId = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENTID);
        LayoutInflater mInflater = getLayoutInflater();
        ldeaHeaderView = mInflater.inflate(R.layout.activity_lable_detail_example_headerview, null);
        initHeaderView(ldeaHeaderView);
        mHandler = new Handler();

        initContent();
        if (lableanswer == 1) {
            initTitleBar("");
            TextView tvTitle = (TextView) findViewById(R.id.tv_title_right_text);
            tvTitle.setText("我来回答");

            tvTitle.setVisibility(View.VISIBLE);

            tvTitle.setOnClickListener(this);
        } else {
            initTitleBar("");
        }
    }

    private void initHeaderView(View ldeaHeaderView) {
        lables_details_iv_user = (CircleImageView) ldeaHeaderView.findViewById(R.id.lables_details_iv_user);
        lables_details_tv_name = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_name);
        lables_details_tv_title = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_title);
        lables_details_tv_zhicheng = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_zhicheng);
        lables_details_tv_pushtime = (TextView) ldeaHeaderView.findViewById(R.id.lables_details_tv_pushtime);

        community_mine_lables_gridview =(GridView) ldeaHeaderView.findViewById(R.id.community_mine_lables_gridview);
        lable_details_ll_support = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll_support);

        view_line = (View) ldeaHeaderView.findViewById(R.id.view_line);
        lable_details_ll_support.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);
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
    private String more="... 全部";

    private void geneItems() {
        for (int i = 0; i < answersesnums.size(); ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(answersesnums.get(i).getUserName()+"");
            if((answersesnums.get(i).getContent()+"").toString().length()>=100) {
                lable.setHeadTvLable(answersesnums.get(i).getContent() + "... 全部");
            }else{
                lable.setHeadTvLable(answersesnums.get(i).getContent() + "");
            }
            lable.setHeadIvUrl(answersesnums.get(i).getPhotoUrl()+"");
            lable.setReleaseTime("回答于"+TimeCastUtils.compareDateTime(System.currentTimeMillis(), answersesnums.get(i).getCreateTime()));
            lable.setDetailSupport(answersesnums.get(i).getStarNum()+"");
            lable.setTaglibId(answersesnums.get(i).getUserPosition());
            lable.setQuestionId(answersesnums.get(i).getCreateUserId());
            lables.add(lable);
        }
    }


    @Override
    protected void findViews() {
        lable_details_iv_discuss = (ImageView) findViewById(R.id.lable_details_iv_discuss);
       // ll_bianji=(LinearLayout)findViewById(R.id.ll_bianji);
       // ll_bianji.setOnClickListener(this);
        et_comment = (EditText) findViewById(R.id.et_comment);
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_example_listview);
        lable_details_iv_discuss.setOnClickListener(this);
        if (ldeaHeaderView != null) {
            lable_details_listview.addHeaderView(ldeaHeaderView);
            lable_details_ll_support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                    TextView lables_details_tv_name = holder.getView(R.id.lables_details_tv_name);
                    TextView lable_details_tv_support = holder.getView(R.id.lable_details_tv_support);
                    TextView homepage_wonderfulcontent_supportlike = holder.getView(R.id.homepage_wonderfulcontent_supportlike);
                    TextView lables_details_tv_zhicheng = holder.getView(R.id.lables_details_tv_zhicheng);
                    TextView lables_details_tv_pushtime = holder.getView(R.id.lables_details_tv_pushtime);
                   TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                    lables_details_tv_name.setText(item.getHeadTvName() + "");//名称
                    lable_details_tv_support.setText(item.getDetailSupport());
                    lables_details_tv_pushtime.setText(item.getReleaseTime() + "");//时间
                    if(item.getHeadTvLable().length()>=100) {
                        SpannableStringBuilder style = new SpannableStringBuilder(item.getHeadTvLable());
                        //style.setSpan(new BackgroundColorSpan(Color.RED),2,5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置textview的背景颜色
                        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.community_activity_tv_color)), 104, 106, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置文字的颜色
                        homepage_ll_wonderfulcontent_text.setText(style);
                    }else{
                        homepage_ll_wonderfulcontent_text.setText(item.getHeadTvLable());
                    }
                    ImageLoader.getInstance().displayImage(item.getHeadIvUrl(), lables_details_iv_user,options);//评论者头像
                    PositionUtils.getPosition(item.getTaglibId(),lables_details_tv_zhicheng);
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
                if(i!=0&&i!=1) {
                    Intent intent = new Intent(getApplicationContext(), AnswerQuestionsDetailActivity.class);
                    intent.putExtra("AnswersDetailsActivityID", answersesnums.get(i - 2).getQuestionAnswerId() + "");
                    intent.putExtra("AnswersDetailsActivityTitle", contentTitle+"");
                    //intent.putExtra("AnswersDetailsList",  (Serializable)answersesnums);
                    startActivity(intent);
                }
            }
        });


        lable_details_ll__shared = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_details_ll__shared);
        lable_detail_ll_support_img = (LinearLayout) ldeaHeaderView.findViewById(R.id.lable_detail_ll_support_img);
        lable_detail_tv_answerordiscuss = (TextView) ldeaHeaderView.findViewById(R.id.lable_detail_tv_answerordiscuss);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(photoCommonAdapter.getCount() * LIEWIDTH,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        int count = photoCommonAdapter.getCount();

        lable_details_ll_discuss = (LinearLayout) findViewById(R.id.lable_details_ll_discuss);
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
        if (contentId != null) {
            Api.searchQuestionDetail(Integer.parseInt(contentId), contentInfoHandler);
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
        public void onSuccess(final HpWonderfulContent data) {
            hideWaitingUI();
            if(data.getContent().length()>20) {
                shareContent = data.getContent().substring(0, 20) + "...";
            }else {
                shareContent = data.getContent()+"";
            }
            shareTitle=data.getTitle() + "";
            alllableEditorCAdapter =new CommonAdapter<QuestionImages>(getApplicationContext(),data.getQuestionImages(),R.layout.pic_layout) {
                @Override
                protected void convert(ViewHolder holder, final QuestionImages item) {
                    ImageView lable =(ImageView)holder.getView(R.id.lables_top_textview);
                    ImageLoader.getInstance().displayImage(item.getImageUrl(), lable,option);
                }
            };

            community_mine_lables_gridview.setAdapter(alllableEditorCAdapter);
            community_mine_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(AnswersDetails.this,
                            PhotoActivitys.class);

                    intent.putExtra("URLS", (Serializable) data.getQuestionImages());
                    intent.putExtra("ID",position);

                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
            contentTitle=data.getTitle();


            ImageLoader.getInstance().displayImage(data.getPhotoUrl(), lables_details_iv_user,options);

            homepage_ll_wonderfulcontent_text.setText(data.getContent() + "");
            lables_details_tv_pushtime.setText("提问于"+TimeCastUtils.compareDateTime(System.currentTimeMillis(), data.getCreateTime()));
            lables_details_tv_name.setText(data.getUserName() + "");
            lables_details_tv_title.setText(data.getTitle() + "");
            lable_details_tv_support.setText(data.getStars() + "");
            lable_details_tv_supports.setText(data.getStars() + "");
            lable_details_tv__comment.setText(data.getAnswersNum() + "");
            initTitleBar(data.getUserName() + "");
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
           if (data.getTaglibs() != null && data.getTaglibs().size() == 1) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
            }
            else if (data.getTaglibs() != null && data.getTaglibs().size() == 2) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
                tv_tag2.setText(data.getTaglibs().get(1).getTagName());
                tv_tag2.setBackgroundResource(R.color.main_background_color);
            }
            else if (data.getTaglibs()!= null && data.getTaglibs().size() == 3) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
                tv_tag2.setText(data.getTaglibs().get(1).getTagName());
                tv_tag2.setBackgroundResource(R.color.main_background_color);
                tv_tag3.setText(data.getTaglibs().get(2).getTagName());
                tv_tag3.setBackgroundResource(R.color.main_background_color);
            }
            else if (data.getTaglibs() != null && data.getTaglibs().size() == 4) {
                tv_tag1.setText(data.getTaglibs().get(0).getTagName());
                tv_tag1.setBackgroundResource(R.color.main_background_color);
                tv_tag2.setText(data.getTaglibs().get(1).getTagName());
                tv_tag2.setBackgroundResource(R.color.main_background_color);
                tv_tag3.setText(data.getTaglibs().get(2).getTagName());
                tv_tag3.setBackgroundResource(R.color.main_background_color);
                tv_tag4.setText(data.getTaglibs().get(3).getTagName());
                tv_tag4.setBackgroundResource(R.color.main_background_color);
            }
            if (data.getQuestionAnswers() != null) {
                answersesnums = data.getQuestionAnswers();
                geneItems();//加载评论的
            }

        }

        @Override
        public void onFailure(String status, String message) {

            //hideWaitingUI();

           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
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
                //回复问题
              if(et_comment.getText().toString().trim().equals("")){
                  Toast.makeText(getApplicationContext(),"请填写评论",Toast.LENGTH_SHORT).show();
                  return;
              }
                //Api.addAnswerQuestionDetail(Integer.parseInt(contentId), et_comment.getText().toString().trim(), answerHandler);
                break;

            case R.id.lable_details_ll__shared:
                //new PopupWindows(AnswersDetails.this, lable_details_ll__shared);
               // showShare();
                break;
            case R.id.tv_title_right_text:
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(AnswersDetails.this, LoginActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    if (AppContext.getAppContext().getUser().profession > 0) {
                        Intent intent = new Intent(getApplicationContext(), RichTextAnswerActivity.class)
                                .putExtra("role", "add");
                        intent.putExtra("tagLibId", contentId);
                        intent.putExtra("tagLibName", "哈哈");
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    } else {
                        showConfigDialog();

                    }
                }
                break;

            default:
                break;
        }
    }

    private TextView tv_next,tv_goto;
    private Dialog dialog;

    private void showConfigDialog() {
        // TODO Auto-generated method stub
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setContentView(R.layout.dialog_config);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.allradius_white);
        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.height = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(attributes);

        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_goto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MineInfoActivity.class);
                intent.putExtra(MineInfoActivity.EXTRA_USER,  AppContext.getAppContext().getUser());
                startActivity(intent);
            }
        });
    }


    /**
     * 回复的回调
     */
    private ResponseCallback<Void> answerHandler = new ResponseCallback<Void>() {

        @Override
        public void onSuccess(Void data) {
            et_comment.setText("");
            AppContext.showToast("回复成功");
            //回复成功后，重新刷新列表，将新的评论刷新
            start = ++refreshCnt;
            lables.clear();
           getData();
            lable_details_listview.setAdapter(lanleCommonAdapter);
            onLoad();
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };
    /**
     * 点赞回调
     */
    private ResponseCallback<HpWonderfulContent> zanHandler = new ResponseCallback<HpWonderfulContent>() {


        @Override
        public void onSuccess(HpWonderfulContent data) {
            //Toast.makeText(AnswersDetails.this, "回复success！", Toast.LENGTH_LONG).show();
            //LOG.i("debug", "HpWonderfulContent---->" + data);

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
        oks.setTitleUrl("http://cdn-p3.gtestin.cn/6143a520e2df437eab28e1bf331c2629.apk");
// text是分享文本，所有平台都需要这个字段
        oks.setText(shareContent);
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.png");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://jjkangfu.cn/appPage/invitation");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://jjkangfu.cn/appPage/invitation");

// 启动分享GUI
        oks.show(this);
    }

/*
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
                    Toast.makeText(AnswersDetails.this, "我要举报！", Toast.LENGTH_LONG).show();
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
    }*/

}
