package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.QuestionAnswers;
import com.yikang.app.yikangserver.fragment.DetailFragment;
import com.yikang.app.yikangserver.interfaces.NoDoubleClickListener;
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;

import java.util.ArrayList;

public class AnswerQuestionsDetailActivity extends BaseActivity {
    private String contentId;
    private String contentTitle;
    private String contentTitles;
    private QuestionAnswers questionAnswers;
    private TextView lables_details_tv_title;
    private CircleImageView lables_details_iv_user;
    private TextView lables_details_tv_zhicheng, tv_time, lables_details_tv_name, lable_details_tv_support, homepage_wonderfulcontent_supportlike;
    private WebView webView;
    private LinearLayout lable_details_ll__cpmment;
    private DisplayImageOptions options; //配置图片加载及显示选项
    private ViewPager viewpager;
    private ArrayList<DetailFragment> imageViews=new ArrayList<>();
    private int currentview = 0;
    private EdgeEffectCompat leftEdge;
    private EdgeEffectCompat rightEdge;
    private DetailFragment detail;
   // private ArrayList<QuestionAnswers> lables = new ArrayList<QuestionAnswers>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = getIntent().getStringExtra("AnswersDetailsActivityID");
        contentTitle = getIntent().getStringExtra("AnswersDetailsActivityTitle");
        //lables =  (ArrayList<QuestionAnswers>)getIntent().getSerializableExtra("AnswersDetailsList");
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        initContent();
        initTitleBar("回答");

    }

    @Override
    protected void findViews() {
      /*  viewpager = (ViewPager) findViewById(R.id.viewpager);
        for(int i = 0; i < lables.size(); i++){
            detail = new DetailFragment(lables.get(i));  //发现
            imageViews.add(detail);
        }
        //detail = new DetailFragment();  //发现
        // 数据准备好了
       *//* int ids[] = {R.drawable.guide_1, R.drawable.guide_2,
                R.drawable.guide_3};
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {
            // 根据id 资源创建图片
            ImageView imageView = new ImageView(AnswerQuestionsDetailActivity.this);
            imageView.setBackgroundResource(ids[i]);

            // 把图片添加到集合中
            imageViews.add(imageView);
        }
        imageViews.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);

            }
        });
        imageViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(2);

            }
        });

        imageViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*//*
        // 设置适配器
        viewpager.setAdapter(new MyPagerAdapter());
        try {
            Field leftEdgeField = viewpager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = viewpager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewpager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewpager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                if(rightEdge!=null&&!rightEdge.isFinished()){//到了最后一张并且还继续拖动，出现蓝色限制边条了
                    startActivity(new Intent(AnswerQuestionsDetailActivity.this, MainActivity.class));
                    AnswerQuestionsDetailActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
            @Override
            public void onPageSelected(int arg0) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
        });
        viewpager.setPageTransformer(true, new DepthPageTransformer());
*/




        lables_details_tv_title = (TextView) findViewById(R.id.lables_details_tv_title);
        lables_details_tv_zhicheng = (TextView) findViewById(R.id.lables_details_tv_zhicheng);
        tv_time = (TextView) findViewById(R.id.tv_time);
        lables_details_tv_name = (TextView) findViewById(R.id.lables_details_tv_name);
        lable_details_tv_support = (TextView) findViewById(R.id.lable_details_tv_support);
        homepage_wonderfulcontent_supportlike = (TextView) findViewById(R.id.homepage_wonderfulcontent_supportlike);
        lables_details_iv_user = (CircleImageView) findViewById(R.id.lables_details_iv_user);
        webView = (WebView) findViewById(R.id.webView);
        lable_details_ll__cpmment = (LinearLayout) findViewById(R.id.lable_details_ll__cpmment);
        lable_details_ll__cpmment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(AnswerQuestionsDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Intent intent = new Intent(getApplicationContext(), AnswerQuestionsDetailCommentActivity.class);
                    intent.putExtra("AnswersDetailsID", contentId + "");
                    // intent.putExtra("AnswersDetailsActivityTitle", contentTitle+"");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_answer_questions_detail);
    }

    @Override
    protected void getData() {
        showWaitingUI();

        Api.answerDetail(Integer.parseInt(contentId), contentInfoHandler);

    }

    private void loadViewData() {
        PositionUtils.getPosition(questionAnswers.getUserPosition(), lables_details_tv_zhicheng);
        tv_time.setText("回答于" + TimeCastUtils.compareDateTime(System.currentTimeMillis(), questionAnswers.getCreateTime()));
        if (contentTitles != null) {
            lables_details_tv_title.setText(contentTitles + "");
        }
        if (contentTitle != null) {
            lables_details_tv_title.setText(contentTitle + "");
        }
        lables_details_tv_name.setText(questionAnswers.getUserName() + "");
         /*
            跳入专业人员个人主页
             */
        lables_details_tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        PersonalHomepageActivity.class);
                intent.putExtra("userId", questionAnswers.getCreateUserId() + "");
                startActivity(intent);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        lables_details_iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        PersonalHomepageActivity.class);
                intent.putExtra("userId", questionAnswers.getCreateUserId() + "");
                startActivity(intent);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        ImageLoader.getInstance().displayImage(questionAnswers.getPhotoUrl() + "", lables_details_iv_user, options);
        webView.getSettings().setJavaScriptEnabled(true);
        //加载HTML字符串进行显示
        webView.loadData("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<head>\n" +
                        "\t<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\" />\n" +
                        "\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                        "\t<style type='text/css'> img { width: 100%; } </style>" +
                        "</head>\n" +
                        "\t<body>\n" + questionAnswers.getHtmlDetailContent() + "</body>\n" +
                        "</html>",
                "text/html ;charset=UTF-8", null);
        if (questionAnswers.getIsStar() == 0) {
            lable_details_tv_support.setVisibility(View.VISIBLE);
            homepage_wonderfulcontent_supportlike.setVisibility(View.GONE);
            lable_details_tv_support.setText(questionAnswers.getStarNum() + "");
            lable_details_tv_support.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (AppContext.getAppContext().getAccessTicket() == null) {
                        Intent intent = new Intent(AnswerQuestionsDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else {

                        Api.supportAnswer(Integer.parseInt(contentId), zanHandler);
                    }
                }
            });
            homepage_wonderfulcontent_supportlike.setText(questionAnswers.getStarNum() + "");
            homepage_wonderfulcontent_supportlike.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (AppContext.getAppContext().getAccessTicket() == null) {
                        Intent intent = new Intent(AnswerQuestionsDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else {
                        Api.supportAnswer(Integer.parseInt(contentId), quxiaozanHandler);
                    }
                }
            });
        }
        if (questionAnswers.getIsStar() == 1) {
            lable_details_tv_support.setVisibility(View.GONE);
            homepage_wonderfulcontent_supportlike.setVisibility(View.VISIBLE);
            homepage_wonderfulcontent_supportlike.setText(questionAnswers.getStarNum() + "");
            homepage_wonderfulcontent_supportlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppContext.getAppContext().getAccessTicket() == null) {
                        Intent intent = new Intent(AnswerQuestionsDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                       // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else {
                        Api.supportAnswer(Integer.parseInt(contentId), quxiaozanHandler);
                    }
                }
            });
        }
    }

    /**
     * 点赞的回调
     */
    private ResponseCallback<String> zanHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
            homepage_wonderfulcontent_supportlike.setVisibility(View.VISIBLE);
            homepage_wonderfulcontent_supportlike.setText((Integer.parseInt(lable_details_tv_support.getText().toString()) + 1) + "");
            lable_details_tv_support.setVisibility(View.GONE);

            // LOG.i("debug", "HpWonderfulContent---->" + data);


        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
        }
    };

    /**
     * 取消点赞的回调
     */
    private ResponseCallback<String> quxiaozanHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {

            homepage_wonderfulcontent_supportlike.setVisibility(View.GONE);
            lable_details_tv_support.setVisibility(View.VISIBLE);
            lable_details_tv_support.setText((Integer.parseInt(homepage_wonderfulcontent_supportlike.getText().toString()) - 1) + "");
            // LOG.i("debug", "HpWonderfulContent---->" + data);


        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast("取消点赞失败");
        }
    };


    /**
     * 文章详情页面的回调
     */
    private ResponseCallback<QuestionAnswers> contentInfoHandler = new ResponseCallback<QuestionAnswers>() {


        @Override
        public void onSuccess(QuestionAnswers data) {
            hideWaitingUI();

            questionAnswers = data;
            contentTitles = questionAnswers.getTitle();
            loadViewData();
            // LOG.i("debug", "HpWonderfulContent---->" + data);

        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
        }
    };


    @Override
    protected void initViewContent() {

    }
/*
    class MyPagerAdapter extends PagerAdapter {

        // 得到页面的总数
        @Override
        public int getCount() {
            return lables.size();
        }

        *//**
         * 相当于getView()方法 container：容器其实就是ViewPager position:页面对应的下标位置
         *//*
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            DetailFragment imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        *//**
         * view:当前页面 object：instantiateItem返回的对象 作用：比较是否是同一个页面
         *//*
        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        *//**
         * 销毁对应坐标的页面 object：要销毁的页面的对象 position：要销毁的位置
         *//*
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }


    }*/


    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
