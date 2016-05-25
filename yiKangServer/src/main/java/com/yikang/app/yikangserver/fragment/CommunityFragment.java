package com.yikang.app.yikangserver.fragment;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.SimpleFragmentPagerAdapter;
import com.yikang.app.yikangserver.utils.DensityUtils;

import java.util.ArrayList;

/**
 * Created by 郝晓东 on 2016/5/25.
 * 社区页面
 */
public class CommunityFragment extends BaseFragment{
    private ViewPager vpPager;
    private int[] tabIds = new int[] {R.id.rb_inviteCustomer_item_tab_registed,
            R.id.rb_inviteCustomer_item_tab_consumed };
    private ArrayList<Fragment> fragmentList;
    private ImageView indicator;
    private Fragment fragment_find;
    private Fragment fragment_find1;
    private TextView community_tv_top_left,community_tv_top_right;

    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    private int screenW;
    private int one, two;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        findView(rootView);
        return rootView;
    }

    private void findView(View rootView) {
        vpPager = (ViewPager) rootView.findViewById(R.id.vp_inviteCustomer_item_pager);
        community_tv_top_left = (TextView) rootView.findViewById(R.id.community_tv_top_left);
        community_tv_top_right = (TextView) rootView.findViewById(R.id.community_tv_top_right);
        indicator = (ImageView) rootView.findViewById(R.id.view_inviteCustomer_item_indicator);
        initViewContent();
        community_tv_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpPager.setCurrentItem(0);
            }
        });
        community_tv_top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpPager.setCurrentItem(1);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initViewContent() {
        fragment_find = new CommunityFindFragment();  //发现
        fragment_find1 = new CommunityFoucusFragment();  //关注
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) indicator
//                .getLayoutParams();
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        params.leftMargin = (int) (0.5*(screenWidth/tabIds.length)-(0.5*params.width));
//        LOG.i("debug","params.leftMargin-->"+screenWidth);
//        indicator.setLayoutParams(params);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.yk_community_tab_tiao_select).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;// 获取分辨率宽度
        offset = screenW/3 + DensityUtils.dp2px(getActivity(),20);// 计算偏移量
        one = offset + screenW / 6 - DensityUtils.dp2px(getActivity(),5);
        two = one + DensityUtils.dp2px(getActivity(),20);
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        indicator.setImageMatrix(matrix);// 设置动画初始位置

        initFragment();
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(
                getChildFragmentManager(), fragmentList);
        vpPager.setAdapter(adapter);
        //vpPager.setOffscreenPageLimit(2);

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        community_tv_top_left.setTextColor(Color.parseColor("#0faadd"));
                        community_tv_top_right.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        community_tv_top_left.setTextColor(Color.BLACK);
                        community_tv_top_right.setTextColor(Color.parseColor("#0faadd"));
                        break;
                }
            }

            public void onPageScrolled(int arg0, float arg1,
                                       int arg2) {
//                moveIndicator(positionOffset + position);
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
                indicator.setImageMatrix(matrix);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 移动指示器的位置
     */
    private void moveIndicator(float position) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) indicator
                .getLayoutParams();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        params.leftMargin = (int) ((position+0.5)*(screenWidth/tabIds.length)-(0.5*params.width));
        indicator.setLayoutParams(params);
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(fragment_find);
        fragmentList.add(fragment_find1);
    }
}
