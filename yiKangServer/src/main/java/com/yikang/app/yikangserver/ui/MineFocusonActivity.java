package com.yikang.app.yikangserver.ui;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.base.BaseFragmentActivity;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailCollectionFragment;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailFragment;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailLableFragment;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailProfessnalFragment;
import com.yikang.app.yikangserver.view.NoScrollViewPager;
import com.yikang.app.yikangserver.view.XListView;
import java.util.ArrayList;

/**
 * 我的关注界面
 */
public class MineFocusonActivity extends BaseFragmentActivity implements View.OnClickListener {

    private NoScrollViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private LinearLayout community_focuson_professionals, community_focuson_lables, community_focuson_collections;
    private TextView community_focuson_professional, community_focuson_lable, community_focuson_collection;
    private ImageView cursor;// 动画图片
    private int offSet;
    private Matrix matrix = new Matrix();
    private int bmWidth;
    private int currentItem;
    private Animation animation;
    public final static String MESSAGE_INFOS = "messageinfo";
    private String messageinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        messageinfo = getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);
    }
    @Override
    protected void findViews() {
        InitViewPager();

        InitImage();

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_mine_focuson);

    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }

    private void InitImage() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmWidth = BitmapFactory.decodeResource(getResources(), R.drawable.yk_cummunity_focuson_slide).getWidth();// 获取图片宽度
        bmWidth = cursor.getWidth();
        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();
        offSet = (dm.widthPixels - 3 * bmWidth) / 6;
        matrix.setTranslate(offSet, 0);
        cursor.setImageMatrix(matrix);
        currentItem = 0;
    }

    private void InitViewPager() {
        community_focuson_professionals = (LinearLayout) findViewById(R.id.community_focuson_professionals);
        community_focuson_lables = (LinearLayout) findViewById(R.id.community_focuson_lables);
        community_focuson_collections = (LinearLayout) findViewById(R.id.community_focuson_collections);
        community_focuson_professional = (TextView) findViewById(R.id.community_focuson_professional);
        community_focuson_lable = (TextView) findViewById(R.id.community_focuson_lable);
        community_focuson_collection = (TextView) findViewById(R.id.community_focuson_collection);
        community_focuson_professionals.setOnClickListener(new txListener(0));
        community_focuson_lables.setOnClickListener(new txListener(1));
        community_focuson_collections.setOnClickListener(new txListener(2));
        mPager = (NoScrollViewPager) findViewById(R.id.focuson_viewpager);
        mPager.setNoScroll(true);
        fragmentList = new ArrayList<Fragment>();
        CommunityFocusonDetailProfessnalFragment mCommunityFindFragment1 = new CommunityFocusonDetailProfessnalFragment("专业人员");
        CommunityFocusonDetailLableFragment mCommunityFindFragment2 = new CommunityFocusonDetailLableFragment("标签");
        CommunityFocusonDetailCollectionFragment mCommunityFindFragment3 = new CommunityFocusonDetailCollectionFragment("收藏");
        fragmentList.add(mCommunityFindFragment1);
        fragmentList.add(mCommunityFindFragment2);
        fragmentList.add(mCommunityFindFragment3);

        //给ViewPager设置适配器
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
       community_focuson_professional.setTextColor(Color.BLACK);
        community_focuson_lable.setTextColor(Color.GRAY);
        community_focuson_collection.setTextColor(Color.GRAY);
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
            CommunityFocusonDetailFragment.setIndex(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 0:
                    if (currentItem == 1) {
                        animation = new TranslateAnimation(
                                offSet * 2 + bmWidth, 0, 0, 0);
                    } else if (currentItem == 2) {
                        animation = new TranslateAnimation(
                                offSet * 4 + 2 * bmWidth, 0, 0, 0);
                    }
                    community_focuson_professional.setTextColor(Color.BLACK);
                    community_focuson_lable.setTextColor(Color.GRAY);
                    community_focuson_collection.setTextColor(Color.GRAY);
                    break;
                case 1:
                    if (currentItem == 0) {
                        animation = new TranslateAnimation(
                                0, offSet * 2 + bmWidth, 0, 0);
                    } else if (currentItem == 2) {
                        animation = new TranslateAnimation(
                                4 * offSet + 2 * bmWidth, offSet * 2 + bmWidth, 0, 0);
                    }
                    community_focuson_professional.setTextColor(Color.GRAY);
                    community_focuson_lable.setTextColor(Color.BLACK);
                    community_focuson_collection.setTextColor(Color.GRAY);
                    break;
                case 2:
                    if (currentItem == 0) {
                        animation = new TranslateAnimation(
                                0, 4 * offSet + 2 * bmWidth, 0, 0);
                    } else if (currentItem == 1) {
                        animation = new TranslateAnimation(
                                offSet * 2 + bmWidth, 4 * offSet + 2 * bmWidth, 0, 0);
                    }
                    community_focuson_professional.setTextColor(Color.GRAY);
                    community_focuson_lable.setTextColor(Color.GRAY);
                    community_focuson_collection.setTextColor(Color.BLACK);
                    break;
            }
            currentItem = arg0;

            animation.setDuration(500);
            animation.setFillAfter(true);
            cursor.startAnimation(animation);
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
        }
    }
}
