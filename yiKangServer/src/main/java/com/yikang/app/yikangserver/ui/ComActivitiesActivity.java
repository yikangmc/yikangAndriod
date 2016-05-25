package com.yikang.app.yikangserver.ui;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.fragment.ActivityDetailFragment;
import com.yikang.app.yikangserver.fragment.ActivityChattingFragment;
import com.yikang.app.yikangserver.fragment.CommunityFindFragment;

import java.util.ArrayList;

/**
 * 活动详情页面
 */
public class ComActivitiesActivity extends FragmentActivity {

    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private TextView activity_tv_eventdetail, activity_tv_interactive;
    private int currIndex;//当前页卡编号
    private ImageView cursor;// 动画图片
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    private int screenW;
    private int one, two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_activities);
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }

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
        fragmentList = new ArrayList<Fragment>();
        ActivityDetailFragment mCommunityFindFragment1 = new ActivityDetailFragment();
        ActivityChattingFragment   mCommunityFindFragment2 = new ActivityChattingFragment();
        fragmentList.add(mCommunityFindFragment1);
        fragmentList.add(mCommunityFindFragment2);

        //给ViewPager设置适配器
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
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
