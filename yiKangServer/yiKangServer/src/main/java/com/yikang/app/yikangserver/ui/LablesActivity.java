package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.base.BaseFragmentActivity;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.fragment.SecondLablesFragment;

import java.util.ArrayList;
import java.util.List;

public class LablesActivity extends BaseFragmentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titles;
    private TextView tv;
    private TabLayout.Tab tab;
    List<LablesBean> firstLables=new ArrayList<>();
    private DisplayImageOptions options; //配置图片加载及显示选项
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();

        setContentView(R.layout.activity_lables);
        initTitleBar("健康圈");
        initView();


    }

    private void initData() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new SecondLablesFragment(firstLables.get(position).getChilds(),firstLables.get(position).getTaglibId());
            }

            @Override
            public int getCount() {
                return firstLables.size();
            }

        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);

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
            tab = tabLayout.getTabAt(i);
            if (tab != null) {

               tab.setCustomView(initTabItemView(i, tab));  //将每个导航卡都绑定为我们自己写的布局

            }


        }
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

    }

    private View initTabItemView(int position,TabLayout.Tab tabs) {  //获取item的布局，然后将名字放进去
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
         tv = (TextView) tabView.findViewById(R.id.tab_tv);
       tv.setText(firstLables.get(position).getTagName()+"");

        return tabView;
    }




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
        //获取一级二级标签列表
       Api.getFirstSecondLableContent(firstSecongLablesHandler);
    }

    @Override
    protected void initViewContent() {

    }
    private ResponseCallback<List<LablesBean>> firstSecongLablesHandler = new ResponseCallback<List<LablesBean>>() {


        @Override
        public void onSuccess(List<LablesBean> data) {
            hideWaitingUI();
           // LOG.i("debug", "HpWonderfulContent---->" + data+"哈哈");
            if(data!=null) {
                for (LablesBean hp : data) {
                    firstLables.add(hp);
                }
                initData();
            }
        }
        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

}
