package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.ChattingAboutActivity;
import com.yikang.app.yikangserver.ui.ComActivitiesActivity;
import com.yikang.app.yikangserver.ui.LoginActivity;
import com.yikang.app.yikangserver.ui.PersonalHomepageActivity;
import com.yikang.app.yikangserver.ui.ProfessionalContentActivity;
import com.yikang.app.yikangserver.utils.DensityUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ActivityChattingFragment extends BaseFragment implements OnClickListener, XListView.IXListViewListener {
    protected static final String TAG = "CommunityFindFragment";
    private TextView homepage_tv_arrow, homepage_tv_more, homepage_wonderfulcontent_support, homepage_wonderfulcontent_comment;
    private FrameLayout homepage_fl_top;
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    public ActivityChattingFragment(String activityId) {
        this.activityId = activityId;
    }
    private String activityId;
    private XListView community_detals_listview;
    private List<ChattingAboutActivity> chattingAboutActivity=new ArrayList<>();
    private CommonAdapter<ChattingAboutActivity> ComlanleCommonAdapter;
    private Handler mHandler;
    private View lvHeaderView;
    private FrameLayout community_fl_top2, community_fl_top1;
    private LinearLayout community_find_personal_info_ll1;
    private LinearLayout community_find_personal_info_ll2;
    private LinearLayout community_find_personal_info_ll3;
    private LinearLayout community_find_personal_info_ll4;
    private LinearLayout community_find_personal_info_ll5;
    private ImageView lable_details_iv_discuss,lable_details_iv_discussss;
    private EditText et_comment_activity;
    private DisplayImageOptions options; //配置图片加载及显示选项

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        mHandler = new Handler();
        geneItems();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_chatting, container, false);
        //lvHeaderView = inflater.inflate(R.layout.fragment_community_find_headerview, null);//listview的头view
        //initViewPager(lvHeaderView);
        findViews(rootView);
        return rootView;
    }

    //获取加载数据
    private void geneItems() {
        Api.getAllCommentsAboutActivity(Integer.parseInt(activityId),allCommentsAboutActivityHanler);

    }

    private void findViews(View view) {
        community_detals_listview = (XListView) view.findViewById(R.id.community_detals_listview);
        lable_details_iv_discuss = (ImageView) view.findViewById(R.id.lable_details_iv_discuss);
        lable_details_iv_discussss = (ImageView) view.findViewById(R.id.lable_details_iv_discussss);
        et_comment_activity = (EditText) view.findViewById(R.id.et_comment_activity);
        et_comment_activity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    {
                        if (AppContext.getAppContext().getAccessTicket() == null) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                }

                return false;
            }
        });
        et_comment_activity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lable_details_iv_discuss.setVisibility(View.VISIBLE);
                lable_details_iv_discussss.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                lable_details_iv_discuss.setVisibility(View.GONE);
                lable_details_iv_discussss.setVisibility(View.VISIBLE);
                if(s.length()==0){
                    lable_details_iv_discuss.setVisibility(View.VISIBLE);
                    lable_details_iv_discussss.setVisibility(View.GONE);
                }
            }
        });
        community_detals_listview.setPullLoadEnable(false);
        ComlanleCommonAdapter = new CommonAdapter<ChattingAboutActivity>(getContext(), chattingAboutActivity, R.layout.list_lables_item) {
            @Override
            protected void convert(ViewHolder holder, final ChattingAboutActivity item) {
                TextView lables_tv_tiezi = holder.getView(R.id.lables_tv_tiezi);
                TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                TextView lables_tv_focusnum = holder.getView(R.id.lables_tv_focusnum);
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                CircleImageView iv_mine_avatar = holder.getView(R.id.iv_mine_avatar);
                lables_tv_tiezi.setText(item.getContent());
                lables_tv_time.setText(item.getCreateTimes());
                lables_tv_title.setText(item.getUserName());
                lables_tv_focusnum.setText("");
                ImageLoader.getInstance().displayImage(item.getPhotoUrl(),iv_mine_avatar,options);
                iv_mine_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),
                                PersonalHomepageActivity.class);
                        intent.putExtra("userId",item.getCreateUserId()+"");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    }
                });
                lables_tv_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),
                                PersonalHomepageActivity.class);
                        intent.putExtra("userId",item.getCreateUserId()+"");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    }
                });
            }
        };

        community_detals_listview.setFocusable(false);
        community_detals_listview.setXListViewListener(this);
        lable_details_iv_discussss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_comment_activity.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"请填写您的评论",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    showWaitingUI();
                    Api.commentActivity(Integer.parseInt(activityId),et_comment_activity.getText().toString().trim(),commentActivityHandler);
                }
            }
        });
    }

    private void initViewPager(View view) {

        community_fl_top1 = (FrameLayout) view.findViewById(R.id.community_fl_top1);   //专业内容模块
        community_fl_top2 = (FrameLayout) view.findViewById(R.id.community_fl_top2);    //精心解答模块
        community_fl_top1.setOnClickListener(this);
        community_fl_top2.setOnClickListener(this);
        community_find_personal_info_ll1 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll1);
        community_find_personal_info_ll2 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll2);
        community_find_personal_info_ll3 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll3);
        community_find_personal_info_ll4 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll4);
        community_find_personal_info_ll5 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll5);
        community_find_personal_info_ll1.setOnClickListener(this);
        community_find_personal_info_ll2.setOnClickListener(this);
        community_find_personal_info_ll3.setOnClickListener(this);
        community_find_personal_info_ll4.setOnClickListener(this);
        community_find_personal_info_ll5.setOnClickListener(this);

        advPager = (ViewPager) view.findViewById(R.id.adv_pager);
        ViewGroup group = (ViewGroup) view.findViewById(R.id.ly_viewGroup);

        //      这里存放的是四张广告背景
        List<View> advPics = new ArrayList<View>();

        ImageView img1 = new ImageView(getActivity());
        img1.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
        img1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),
                        ComActivitiesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        advPics.add(img1);

        ImageView img2 = new ImageView(getActivity());
        img2.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
        img2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),
                        ComActivitiesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        advPics.add(img2);

        ImageView img3 = new ImageView(getActivity());
        img3.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
        img3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),
                        ComActivitiesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        advPics.add(img3);

        ImageView img4 = new ImageView(getActivity());
        img4.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
        img4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),
                        ComActivitiesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        advPics.add(img4);

        //      对imageviews进行填充
        imageViews = new ImageView[advPics.size()];
        //小图标
        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(DensityUtils.dp2px(getActivity(), 5), 0, DensityUtils.dp2px(getActivity(), 5), 0);
            imageView.setLayoutParams(lp);
            imageView.setPadding(5, 5, 5, 5);
            imageViews[i] = imageView;

            group.addView(imageViews[i]);
        }

        advPager.setAdapter(new AdvAdapter(advPics));
        advPager.setOnPageChangeListener(new GuidePageChangeListener());
        advPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOption();
                    }
                }
            }

        }).start();
    }


    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
    }

    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    private void onLoad() {
        community_detals_listview.stopRefresh();
        community_detals_listview.stopLoadMore();
        community_detals_listview.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chattingAboutActivity.clear();
                geneItems();
                onLoad();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                ComlanleCommonAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 1000);
    }

    @Override
    public void onScrollMove() {

    }

    @Override
    public void onScrollFinish() {

    }

    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]

                        .setBackgroundResource(R.drawable.yk_community_tab_circle_select);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.yk_community_tab_circle);
                }
            }

        }

    }

    /**
     * 获取活动所有评论的回调
     */
    private ResponseCallback<List<ChattingAboutActivity>> allCommentsAboutActivityHanler = new ResponseCallback<List<ChattingAboutActivity>>() {


        @Override
        public void onSuccess(List<ChattingAboutActivity> data) {
            hideWaitingUI();
           // LOG.i("debug", "活动评论详情>>>>>>>" +data);
            for (int i = 0; i<data.size(); ++i) {
                ChattingAboutActivity lable = new ChattingAboutActivity();
                lable.setContent(data.get(i).getContent()+"");
                lable.setUserName(data.get(i).getUserName()+"");
                lable.setPhotoUrl(data.get(i).getPhotoUrl()+"");
                lable.setCreateTimes(TimeCastUtils.compareDateTime(System.currentTimeMillis(),data.get(i).getCreateTime()));
                lable.setCreateUserId(data.get(i).getCreateUserId());
                chattingAboutActivity.add(lable);
            }
            community_detals_listview.setAdapter(ComlanleCommonAdapter);

        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            AppContext.showToast( message);

        }
    };

    /**
     * 发表评论的回调
     */
    private ResponseCallback<Void > commentActivityHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
           // LOG.i("debug", "活动评论结果>>>>>>>" +data);
            hideWaitingUI();
            chattingAboutActivity.clear();
            geneItems();
            onLoad();
            et_comment_activity.setText("");
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            AppContext.showToast( message);

        }
    };



    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }

    /**
     * 将map中的信息填写到Views中
     */
    private void fillToViews() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //专业内容点击事件
            case R.id.community_fl_top1:
                showEditPage();
                break;
            //精心解答点击事件
            case R.id.community_fl_top2:
                showQrCodeDialog();
                break;
            case R.id.community_find_personal_info_ll1:
                toCustomerListPage();
                break;
            case R.id.community_find_personal_info_ll2:
                toCustomerListPage();
                break;
            case R.id.community_find_personal_info_ll3:
                toCustomerListPage();
                break;
            case R.id.community_find_personal_info_ll4:
                toCustomerListPage();
                break;
            case R.id.community_find_personal_info_ll5:
                toCustomerListPage();
                break;
            default:
                break;
        }
    }

    /**
     * 个人信息简介
     */
    private void toCustomerListPage() {
        Intent intent = new Intent(getActivity(),
                PersonalHomepageActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


    /**
     * 显精心问答
     */
    private void showQrCodeDialog() {
        Intent intent = new Intent(getActivity(),
                ProfessionalContentActivity.class);
        intent.putExtra("fromCommunityFind", 2);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


    /**
     * 专业内容
     */
    private void showEditPage() {
        Intent intent = new Intent(getActivity(),
                ProfessionalContentActivity.class);
        intent.putExtra("fromCommunityFind", 1);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


}
