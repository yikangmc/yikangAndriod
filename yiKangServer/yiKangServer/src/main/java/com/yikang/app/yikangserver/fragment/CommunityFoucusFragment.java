package com.yikang.app.yikangserver.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.MyFocusPerson;
import com.yikang.app.yikangserver.bean.Taglibs;
import com.yikang.app.yikangserver.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/20.
 * 社区关注模块
 */
public class CommunityFoucusFragment extends BaseFragment implements View.OnClickListener {

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
    private ArrayList<Taglibs> lables = new ArrayList<Taglibs>();
    private ArrayList<MyFocusPerson> persons = new ArrayList<MyFocusPerson>();
    private ArrayList<HpWonderfulContent> collections = new ArrayList<HpWonderfulContent>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_focuson, container, false);
        InitTextView(rootView);
        InitImage(rootView);
        InitViewPager(rootView);
        return rootView;
    }
    /**
     * 获取关注专业人士的回调
     */
    private ResponseCallback<List<MyFocusPerson>> allMyFocusPersonHandler = new ResponseCallback<List<MyFocusPerson>>() {

        @Override
        public void onSuccess(List<MyFocusPerson> data) {

            hideWaitingUI();
            for (MyFocusPerson hp : data) {
                persons.add(hp);
            }

        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
           // AppContext.showToast(message);
        }
    };



    /**
     * 获取关注收藏的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allMyFocusCollectionHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {

            hideWaitingUI();
            for (HpWonderfulContent hp : data) {
                collections.add(hp);
            }

        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };


    /**
     * 获取关注标签的回调
     */
    private ResponseCallback<List<Taglibs>> allMyFocusPersonsHandler = new ResponseCallback<List<Taglibs>>() {

        @Override
        public void onSuccess(List<Taglibs> data) {

            hideWaitingUI();
            for (Taglibs hp : data) {
                lables.add(hp);
            }


        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            //hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    private void InitTextView(View view) {
        community_focuson_professionals = (LinearLayout) view.findViewById(R.id.community_focuson_professionals);
        community_focuson_lables = (LinearLayout) view.findViewById(R.id.community_focuson_lables);
        community_focuson_collections = (LinearLayout) view.findViewById(R.id.community_focuson_collections);
        community_focuson_professional = (TextView) view.findViewById(R.id.community_focuson_professional);
        community_focuson_lable = (TextView) view.findViewById(R.id.community_focuson_lable);
        community_focuson_collection = (TextView) view.findViewById(R.id.community_focuson_collection);
        community_focuson_professionals.setOnClickListener(new txListener(0));
        community_focuson_lables.setOnClickListener(new txListener(1));
        community_focuson_collections.setOnClickListener(new txListener(2));
    }

    private void InitImage(View view) {
        cursor = (ImageView) view.findViewById(R.id.cursor);
        bmWidth = BitmapFactory.decodeResource(getResources(), R.drawable.yk_community_tab_tiao_select).getWidth();// 获取图片宽度
        bmWidth = cursor.getWidth();
        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();
        offSet = (dm.widthPixels - 3 * bmWidth) / 6;
        matrix.setTranslate(offSet, 0);
        cursor.setImageMatrix(matrix);
        currentItem = 0;
    }

    private void InitViewPager(View view) {
        mPager = (NoScrollViewPager) view.findViewById(R.id.focuson_viewpager);
        mPager.setNoScroll(true);
        fragmentList = new ArrayList<Fragment>();
        CommunityFocusonDetailProfessnalFragment mCommunityFindFragment1 = new CommunityFocusonDetailProfessnalFragment("专业人员",persons);
        CommunityFocusonDetailLableFragments mCommunityFindFragment2 = new CommunityFocusonDetailLableFragments("标签",lables);
        CommunityFocusonDetailCollectionFragment mCommunityFindFragment3 = new CommunityFocusonDetailCollectionFragment("收藏",collections);
        fragmentList.add(mCommunityFindFragment1);
        fragmentList.add(mCommunityFindFragment2);
        fragmentList.add(mCommunityFindFragment3);

        //给ViewPager设置适配器
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        community_focuson_professional.setTextColor(getResources().getColor(R.color.community_activity_tv_color));
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
                    community_focuson_professional.setTextColor(getResources().getColor(R.color.community_activity_tv_color));
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
                    community_focuson_lable.setTextColor(getResources().getColor(R.color.community_activity_tv_color));
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
                    community_focuson_collection.setTextColor(getResources().getColor(R.color.community_activity_tv_color));
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
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
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
