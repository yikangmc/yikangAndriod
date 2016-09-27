package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.base.BaseFragmentActivity;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.fragment.ProfessionalContentGoodAnwserFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 专业内容，精心解答页面
 */
public class ProfessionalAnwserActivity extends BaseFragmentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView more_lable_iv;
    private List<String> titles;
    private int fromCommunityFind;
    private TextView tv_title_right_text;
    List<LablesBean> firstLables=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communcate_lables);
        fromCommunityFind=getIntent().getIntExtra("fromCommunityFind",1);

       initContent();
        if (fromCommunityFind==1){
            initTitleBar("专家说");

        }else  if (fromCommunityFind==2){
            initTitleBar("问答");
            tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
            tv_title_right_text.setText("提问须知");
            tv_title_right_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent WebIntent = new Intent(getApplicationContext(), WebviewActivivty.class);
                    WebIntent.putExtra("bannerUrl","http://jjkangfu.cn/appPage/activity?3");
                    WebIntent.putExtra("bannerName","提问须知");
                    startActivity(WebIntent);
                }
            });
        }

        initView();



    }

    private void initData() {
        //Api.getAllLable(firstLablesHandler);
        //titles = Arrays.asList("运动康复", "瘦身", "营养专家", "日常训练", "体态矫正", "吞咽障碍", "语言康复","糖尿病","神经","偏瘫");
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new ProfessionalContentGoodAnwserFragment(firstLables.get(position).getTaglibId()+"",firstLables.get(position).getTagName()+"",fromCommunityFind);
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
        for (int i = 0; i < firstLables.size(); i++) {  //获取七个导航卡并遍历
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(initTabItemView(i));  //将每个导航卡都绑定为我们自己写的布局
            }
        }
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        more_lable_iv = (ImageView) findViewById(R.id.more_lable_iv);
        more_lable_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MoreLablesActivity.class);   //全部便签
                intent.putExtra("type",fromCommunityFind+"");
                startActivity(intent);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
    }

    private View initTabItemView(int position) {  //获取item的布局，然后将名字放进去
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
        TextView tv = (TextView) tabView.findViewById(R.id.tab_tv);
        //tv.setText(titles.get(position));
        tv.setText(firstLables.get(position).getTagName()+"");
        return tabView;
    }
    /**
     * 获取顶部一级标签的回调List<LablesBean>
     */
    private ResponseCallback<List<LablesBean>> firstLablesHandler = new ResponseCallback<List<LablesBean>>() {


        @Override
        public void onSuccess(List<LablesBean> data) {
            hideWaitingUI();
            for (LablesBean hp : data) {
                firstLables.add(hp);
            }
            initData();
           // LOG.i("debug", "HpWonderfulContent---->" + data+"哈哈");

        }
        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void findViews() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_communcate_lables);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        //获取顶部二级标签列表
       Api.getTuijianAllLable(firstLablesHandler);

    }

    @Override
    protected void initViewContent() {

    }
}

