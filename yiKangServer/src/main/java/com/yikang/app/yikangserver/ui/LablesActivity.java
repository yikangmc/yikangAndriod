package com.yikang.app.yikangserver.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.base.BaseFragmentActivity;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.fragment.SecondLablesFragment;
import com.yikang.app.yikangserver.utils.LOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LablesActivity extends BaseFragmentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titles;

    List<LablesBean> firstLables=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        setContentView(R.layout.activity_lables);
        initTitleBar("标签");
        initView();
        initData();

    }

    private void initData() {
        //titles = Arrays.asList("运动康复", "瘦身", "营养专家", "日常训练", "体态矫正", "吞咽障碍", "语言康复","糖尿病","神经","偏瘫");

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new SecondLablesFragment(firstLables.get(position).getTaglibId()+"");
            }

            @Override
            public int getCount() {
                return firstLables.size();
            }

//            @Override
//            public CharSequence getPageTitle(int position) {  //注释掉，因为tablayout导航卡名字是根据这里进行绑定的，现在我们自定义了，不需要了
//                return titles.get(position);
//            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < firstLables.size(); i++) {  //获取导航卡并遍历
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(initTabItemView(i));  //将每个导航卡都绑定为我们自己写的布局
            }
        }
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private View initTabItemView(int position) {  //获取item的布局，然后将名字放进去
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
        TextView tv = (TextView) tabView.findViewById(R.id.tab_tv);
       //tv.setText(titles.get(position));
       tv.setText(firstLables.get(position).getTagName()+"");
        return tabView;
    }

    /**
     * 获取一级标签的回调
     */
    private ResponseCallback<List<LablesBean>> firstLablesHandler = new ResponseCallback<List<LablesBean>>() {

        @Override
        public void onSuccess(List<LablesBean> data) {
            hideWaitingUI();


            for (LablesBean hp : data) {
                firstLables.add(hp);
            }
            initData();
            LOG.i("debug", "HpWonderfulContent---->" + data.size()+"哈哈");

        }
        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void findViews() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lables);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        //获取一级标签列表
       Api.getFristLableContent(firstLablesHandler);
    }

    @Override
    protected void initViewContent() {

    }
}
