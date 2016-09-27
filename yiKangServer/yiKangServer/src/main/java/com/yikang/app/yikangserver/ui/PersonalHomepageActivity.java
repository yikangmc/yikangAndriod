package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.base.BaseFragmentActivity;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.bean.QuestionAnswers;
import com.yikang.app.yikangserver.bean.UserInfo;
import com.yikang.app.yikangserver.bean.WonderfulActivity;
import com.yikang.app.yikangserver.data.MyData;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailActivityPersonalFragment;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailCollectionPersonalFragment;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailQuestionPersonalFragment;
import com.yikang.app.yikangserver.fragment.CommunityFocusonDetailTieziPersonalFragment;
import com.yikang.app.yikangserver.fragment.MineInfoFragment;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class PersonalHomepageActivity extends BaseFragmentActivity implements View.OnClickListener {

    private CommonAdapter<String> mMessageAdapter;
    private List<String> mList = new ArrayList<>();
    private ListView personal_new_dynamic_lv;
    private boolean flag;
    private ImageView personal_homepage_focuson_ivs, personal_homepage_focuson_ivyi;
    private TextView fanhao,tag_first, tag_second, tag_third, tag_fourth, user_introduce, personal_homepage_focuson_tv, name, tagNum, tieZiNum, followNum;
    private String serverUserId;
    private CircleImageView photo;
    private UserInfo user;
    private DisplayImageOptions options;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titles;
    private TextView tv;
    private TabLayout.Tab tab;
    List<LablesBean> firstLables = new ArrayList<>();
    List<String> firstLable = new ArrayList<>();
    private ArrayList<HpWonderfulContent> collections = new ArrayList<HpWonderfulContent>();
    private ArrayList<WonderfulActivity> wonderfulActivity = new ArrayList<>();
    private ArrayList<HpWonderfulContent> bannerHp = new ArrayList<>();
    private ArrayList<QuestionAnswers> wonderfulActivitys= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverUserId = getIntent().getStringExtra("userId");

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .build();


        initContent();
        initTitleBar("个人主页");

    }

    private View initTabItemView(int position, TabLayout.Tab tabs) {  //获取item的布局，然后将名字放进去
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_items, null);
        tv = (TextView) tabView.findViewById(R.id.tab_tv);
        tv.setText(firstLable.get(position) + "");

        return tabView;
    }
    private void initDatas() {
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new MineInfoFragment(user);
                }
                if (position == 1) {
                    return new CommunityFocusonDetailQuestionPersonalFragment(0,serverUserId,wonderfulActivitys);
                    // return new CommunityFocusonDetailTieziPersonalFragment(serverUserId, bannerHp);
                }
                if (position == 2) {
                    return new CommunityFocusonDetailTieziPersonalFragment(serverUserId, bannerHp);
                } else {
                    return new CommunityFocusonDetailActivityPersonalFragment(serverUserId, wonderfulActivity);
                }
            }

            @Override
            public int getCount() {
                return firstLable.size();
            }

        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < firstLable.size(); i++) {  //获取导航卡并遍历
            tab = tabLayout.getTabAt(i);
            if (tab != null) {

                tab.setCustomView(initTabItemView(i, tab));  //将每个导航卡都绑定为我们自己写的布局

            }


        }
    }

    private void initData() {
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new MineInfoFragment(user);
                }
                if (position == 1) {
                    return new CommunityFocusonDetailQuestionPersonalFragment(1,serverUserId,wonderfulActivitys);
                   // return new CommunityFocusonDetailTieziPersonalFragment(serverUserId, bannerHp);
                }
                if (position == 2) {
                    return new CommunityFocusonDetailTieziPersonalFragment(serverUserId, bannerHp);
                }
                if (position == 3) {
                    return new CommunityFocusonDetailCollectionPersonalFragment(serverUserId, collections);
                } else {
                    return new CommunityFocusonDetailActivityPersonalFragment(serverUserId, wonderfulActivity);
                }
            }

            @Override
            public int getCount() {
                return firstLable.size();
            }

        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < firstLable.size(); i++) {  //获取导航卡并遍历
            tab = tabLayout.getTabAt(i);
            if (tab != null) {

                tab.setCustomView(initTabItemView(i, tab));  //将每个导航卡都绑定为我们自己写的布局

            }


        }
    }

    @Override
    protected void findViews() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        personal_homepage_focuson_ivs = (ImageView) findViewById(R.id.personal_homepage_focuson_ivs);
        personal_homepage_focuson_ivyi = (ImageView) findViewById(R.id.personal_homepage_focuson_ivyi);
        personal_homepage_focuson_tv = (TextView) findViewById(R.id.personal_homepage_focuson_tv);
        tag_first = (TextView) findViewById(R.id.tag_first);
        tag_second = (TextView) findViewById(R.id.tag_second);
        tag_third = (TextView) findViewById(R.id.tag_third);
        tag_fourth = (TextView) findViewById(R.id.tag_fourth);
        name = (TextView) findViewById(R.id.name);
        fanhao = (TextView) findViewById(R.id.fanhao);
        tagNum = (TextView) findViewById(R.id.tagNum);
        tieZiNum = (TextView) findViewById(R.id.tieZiNum);
        followNum = (TextView) findViewById(R.id.followNum);
        photo = (CircleImageView) findViewById(R.id.photo);
        personal_homepage_focuson_ivs.setOnClickListener(this);
        personal_homepage_focuson_ivyi.setOnClickListener(this);
        personal_homepage_focuson_tv.setOnClickListener(this);
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_homepage);
    }

    @Override
    protected void getData() {


        if (serverUserId != null) {
            showWaitingUI();
            Api.getServerInfo(Integer.parseInt(serverUserId), getServerInfoHandler);
        }

    }

    private ResponseCallback<List<QuestionAnswers>> wonderfulActivityHandlers = new ResponseCallback<List<QuestionAnswers>>() {


        @Override
        public void onSuccess(List<QuestionAnswers> data) {
            hideWaitingUI();
            // LOG.i("debug", "HpWonderfulContent---->" + data);
            wonderfulActivitys = new ArrayList<>();
            for (QuestionAnswers hp : data) {
                wonderfulActivitys.add(hp);
            }

        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };


    /**
     * 某一个标签下的所有文章回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();
            bannerHp = new ArrayList<>();
            for (HpWonderfulContent hp : data) {
                bannerHp.add(hp);
            }


        }

        @Override
        public void onFailure(String status, String message) {
            //  LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    /**
     * 活动的回调
     */
    private ResponseCallback<List<WonderfulActivity>> wonderfulActivityHandler = new ResponseCallback<List<WonderfulActivity>>() {


        @Override
        public void onSuccess(List<WonderfulActivity> data) {
            hideWaitingUI();
            LOG.i("debug", "活动---->" + data.size());
            wonderfulActivity = new ArrayList<>();
            for (WonderfulActivity hp : data) {
                wonderfulActivity.add(hp);
            }

        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
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

    private ResponseCallback<UserInfo> getServerInfoHandler = new ResponseCallback<UserInfo>() {

        @Override
        public void onSuccess(UserInfo data) {
            hideWaitingUI();
            //  LOG.i("debug", "HpWonderfulContent---->" + data);
            user = data;
            if (user.profession > 0) {
                String[] s = {"资料", "回答", "帖子", "专家说", "发起活动"};
                List<String> allList = new ArrayList<>();
                for (int i = 0; i < s.length; i++) {
                    firstLable.add(s[i]);
                }
                //获取解答
               // Api.getMyAnswer(Long.parseLong(serverUserId + ""), 1, wonderfulActivityHandlers);
                //专家说
               // Api.getMyContents(Long.parseLong(serverUserId + ""), 1, allMyFocusCollectionHandler);
                //获取活动
               // Api.getAllActiviysWhoPublish(Long.parseLong(serverUserId + ""), 1, wonderfulActivityHandler);
                //获取帖子
               // Api.getMyTiezi(Long.parseLong(serverUserId + ""), 1, allLableContentHandler);
                initContens();
                initData();
            }else {
                String[] s = {"资料", "提问", "帖子", "参与活动"};
                List<String> allList = new ArrayList<>();
                for (int i = 0; i < s.length; i++) {
                    firstLable.add(s[i]);
                }
                //获取解答
               // Api.getMyQuestion(Long.parseLong(serverUserId + ""), 1, wonderfulActivityHandlers);
                //获取活动
               // Api.getAllActiviysWhoTakePartIn(Long.parseLong(serverUserId + ""), 1, wonderfulActivityHandler);
                //获取帖子
               // Api.getMyTiezi(Long.parseLong(serverUserId + ""), 1, allLableContentHandler);
                initContens();
                initDatas();
            }
        }

        private void initContens() {
            ImageLoader.getInstance().displayImage(user.getAvatarImg(), photo, options);
            tagNum.setText(user.getWatchTaglibNumber() + "");
            tieZiNum.setText(user.getWatchUserNumber() + "");
            followNum.setText(user.getFollowUserNumber() + "");
            name.setText(user.getName() + "");

            if (user.profession >= 0) { //职业
                String profession = MyData.professionMap.valueAt(user.profession);
                if(user.profession==1) {
                    fanhao.setText("康复师");

                }
                if(user.profession==0) {

                    if(user.designationName==null) {
                        fanhao.setText("未设置番号");
                    }
                    if(user.designationName!=null) {
                        fanhao.setText(user.designationName);
                    }

                }

                if(user.profession==5) {
                    fanhao.setText("医院科室/主体");

                }
                if(user.profession==2) {
                    fanhao.setText("中医师");

                }
                if(user.profession==3) {
                    fanhao.setText("护士");

                }
                if(user.profession==4) {
                    fanhao.setText("企业主体");

                }

            }

            // user_introduce.setText(user.getUserIntroduce()+"");
            if (user.getIsFollow() == 0) {
                personal_homepage_focuson_ivs.setVisibility(View.VISIBLE);
                personal_homepage_focuson_ivyi.setVisibility(View.GONE);
            }
            if (user.getIsFollow() == 1) {
                personal_homepage_focuson_ivs.setVisibility(View.GONE);
                personal_homepage_focuson_ivyi.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void initViewContent() {
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.personal_homepage_focuson_ivs:
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(PersonalHomepageActivity.this, LoginActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Api.addMyFocusPerson(Integer.parseInt(serverUserId), getGuanzhuHandler);
                }
                break;
            case R.id.personal_homepage_focuson_ivyi:
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(PersonalHomepageActivity.this, LoginActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Api.deleteMyFocusPerson(Integer.parseInt(serverUserId), deleteGuanzhuHandler);
                }
                break;
            case R.id.personal_homepage_focuson_tv:
               /* Intent intent = new Intent(getApplicationContext(),
                        FocusActivity.class);
                intent.putExtra("serverId",serverUserId);
                startActivity(intent);*/
                break;
        }
    }

    private ResponseCallback<String> deleteGuanzhuHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
            hideWaitingUI();
            // LOG.i("debug", "HpWonderfulContent---->" + data);
            personal_homepage_focuson_ivs.setVisibility(View.VISIBLE);
            personal_homepage_focuson_ivyi.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    private ResponseCallback<String> getGuanzhuHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
            hideWaitingUI();
            // LOG.i("debug", "HpWonderfulContent---->" + data);
            personal_homepage_focuson_ivs.setVisibility(View.GONE);
            personal_homepage_focuson_ivyi.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };


}
