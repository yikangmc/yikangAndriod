package com.yikang.app.yikangserver.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.base.BaseFragmentActivity;
import com.yikang.app.yikangserver.bean.WonderfulActivity;
import com.yikang.app.yikangserver.fragment.ActivityChattingFragment;
import com.yikang.app.yikangserver.fragment.ActivityDetailFragment;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 活动详情页面
 */
public class ComActivitiesActivity extends BaseFragmentActivity {
    private ImageView imageView;
    private ImageView ll_activity_background;
    private LinearLayout iv_back,iv_share;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private TextView activity_tv_eventdetail, activity_tv_interactive;
    private int currIndex;//当前页卡编号
    private ImageView cursor;// 动画图片
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    private int screenW;
    private int one, two;
    private String activityId;
    private WonderfulActivity wonderfulActivity;
    private DisplayImageOptions option;
    private String shareTitle;
    private String shareContent;
    private String shareImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(getApplicationContext(),"13fe60c8121ab");
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new FadeInBitmapDisplayer(500))
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        activityId=getIntent().getStringExtra("ACTIVITYID");
      /*  Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }*/
        initContent();
        InitTextView();
        InitImage();
        InitViewPager();
    }

    private void InitTextView() {
        activity_tv_eventdetail = (TextView) findViewById(R.id.activity_tv_eventdetail);
        activity_tv_interactive = (TextView) findViewById(R.id.activity_tv_interactive);
        activity_tv_eventdetail.setOnClickListener(new txListener(0));
        activity_tv_interactive.setOnClickListener(new txListener(1));
    }

    private void InitImage() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.yk_community_activity_tiao).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;// 获取分辨率宽度
        offset = bmpW/4;// 计算偏移量
        one = offset + screenW / 2;
        two = offset + (screenW / 2) * 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void InitViewPager() {

        mPager = (ViewPager) findViewById(R.id.activity_viewpager);

        ll_activity_background = (ImageView) findViewById(R.id.ll_activity_background);

    }

    @Override
    protected void findViews() {
        iv_back = (LinearLayout) findViewById(R.id.iv_back);
        iv_share = (LinearLayout) findViewById(R.id.iv_share);
        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AppContext.showToast("分享");
               /* ShareBitmapUtils.setPicPosition(shareImage);
                showShare();*/
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_community_activities);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        Api.getActivityDetailContent(Integer.parseInt(activityId),activityDetailContentHandler);

    }

    @Override
    protected void initViewContent() {

    }
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
        oks.setImagePath("/sdcard/pepper/test.png");//确保SDcard下面存在此张图片
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
    public class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset * 2 + bmpW;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
            Matrix matrix = new Matrix();
            switch (arg0) {
                case 0:
                    matrix.postTranslate(offset + (one - offset) * arg1, 0);
                    break;
                case 1:
                    matrix.postTranslate(one + (two - one) * arg1, 0);
                    break;

                default:
                    break;
            }
            cursor.setImageMatrix(matrix);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            Drawable drawable= getResources().getDrawable(R.drawable.yk_comunity_activities_eventdetail_select);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            Drawable drawable2= getResources().getDrawable(R.drawable.yk_comunity_activities_eventdetail);
            /// 这一步必须要做,否则不会显示.
            drawable2.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            Drawable drawable1= getResources().getDrawable(R.drawable.yk_comunity_activities_interactivechat_select);
            /// 这一步必须要做,否则不会显示.
            drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            Drawable drawable3= getResources().getDrawable(R.drawable.yk_comunity_activities_interactivechat);
            /// 这一步必须要做,否则不会显示.
            drawable3.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            switch (position) {
                case 0:
                    activity_tv_eventdetail.setTextColor(Color.parseColor("#0faadd"));
                    activity_tv_eventdetail.setCompoundDrawables(drawable,null,null,null);
                    activity_tv_interactive.setTextColor(Color.parseColor("#929292"));
                    activity_tv_interactive.setCompoundDrawables(drawable3,null,null,null);
                    break;
                case 1:
                    activity_tv_interactive.setTextColor(Color.parseColor("#0faadd"));
                    activity_tv_interactive.setCompoundDrawables(drawable1,null,null,null);
                    activity_tv_eventdetail.setTextColor(Color.parseColor("#929292"));
                    activity_tv_eventdetail.setCompoundDrawables(drawable2,null,null,null);
                    break;

                default:
                    break;
            }
        }
    }

    private ResponseCallback<WonderfulActivity>     activityDetailContentHandler = new ResponseCallback<WonderfulActivity>() {


        @Override
        public void onSuccess(WonderfulActivity data) {
            hideWaitingUI();
            //AppContext.showToast("获取数据成功");
           // LOG.i("debug", "活动详情>>>>>>>" +data);
            wonderfulActivity=data;
            ImageLoader.getInstance().displayImage(wonderfulActivity.getRecommendPicUrl(),ll_activity_background,option);
            fragmentList = new ArrayList<Fragment>();
            ActivityDetailFragment mCommunityFindFragment1 = new ActivityDetailFragment(wonderfulActivity);
            ActivityChattingFragment   mCommunityFindFragment2 = new ActivityChattingFragment(activityId);
            fragmentList.add(mCommunityFindFragment1);
            fragmentList.add(mCommunityFindFragment2);

            //给ViewPager设置适配器
            mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
            mPager.setCurrentItem(0);//设置当前显示标签页为第一页
            mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
            shareTitle=wonderfulActivity.getTitle();
            shareImage=wonderfulActivity.getRecommendPicUrl();
            if(wonderfulActivity.getDetailAddress().length()>20) {
                shareContent = wonderfulActivity.getDetailAddress().substring(0, 20) + "...";
            }else {
                shareContent = wonderfulActivity.getDetailAddress()+"";
            }
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            AppContext.showToast( message);

        }
    };


    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
    }

}
