package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.bean.CommunityDetailsBean;
import com.yikang.app.yikangserver.ui.ComActivitiesActivity;
import com.yikang.app.yikangserver.ui.LableDetaileExampleActivity;
import com.yikang.app.yikangserver.ui.PersonalHomepageActivity;
import com.yikang.app.yikangserver.ui.ProfessionalAnwserActivity;
import com.yikang.app.yikangserver.ui.ProfessionalContentActivity;
import com.yikang.app.yikangserver.utils.DensityUtils;
import com.yikang.app.yikangserver.utils.T;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 社区的发现模块fragment
 */
public class CommunityFindFragment extends BaseFragment implements OnClickListener, XListView.IXListViewListener {
    protected static final String TAG = "CommunityFindFragment";
    private TextView homepage_tv_arrow, homepage_tv_more, homepage_wonderfulcontent_support, homepage_wonderfulcontent_comment;
    private FrameLayout homepage_fl_top;

    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;

    private XListView community_detals_listview;
    private ArrayList<CommunityDetailsBean> Comlables = new ArrayList<CommunityDetailsBean>();
    private CommonAdapter<CommunityDetailsBean> ComlanleCommonAdapter;
    private Handler mHandler;
    private View lvHeaderView;
    private FrameLayout community_fl_top2, community_fl_top1;
    private LinearLayout community_find_personal_info_ll1;
    private LinearLayout community_find_personal_info_ll2;
    private LinearLayout community_find_personal_info_ll3;
    private LinearLayout community_find_personal_info_ll4;
    private LinearLayout community_find_personal_info_ll5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        geneItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_find, container, false);
        lvHeaderView = inflater.inflate(R.layout.fragment_community_find_headerview, null);//listview的头view
        initViewPager(lvHeaderView);
        findViews(rootView);
        return rootView;
    }

    //获取加载数据
    private void geneItems() {
        for (int i = 0; i != 5; ++i) {
            CommunityDetailsBean lable = new CommunityDetailsBean();
            lable.setUserTvName("最热帖子"+i);
            Comlables.add(lable);
        }
    }

    private void findViews(View view) {
        community_detals_listview = (XListView) view.findViewById(R.id.community_detals_listview);
        community_detals_listview.addHeaderView(lvHeaderView);
        community_detals_listview.setPullLoadEnable(true);
        ComlanleCommonAdapter = new CommonAdapter<CommunityDetailsBean>(getContext(), Comlables, R.layout.list_lable_details_item) {
            @Override
            protected void convert(ViewHolder holder, CommunityDetailsBean item) {

            }
        };
        community_detals_listview.setAdapter(ComlanleCommonAdapter);
        community_detals_listview.setFocusable(false);
        community_detals_listview.setXListViewListener(this);
        community_detals_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), LableDetaileExampleActivity.class);
                intent.putExtra(LableDetaileExampleActivity.EXTRA_LABLE_EXAMPLE, Comlables.get(position - 1).getUserTvName());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
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
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        advPics.add(img1);

        ImageView img2 = new ImageView(getActivity());
        img2.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
        img2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        advPics.add(img2);

        ImageView img3 = new ImageView(getActivity());
        img3.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
        img3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        advPics.add(img3);

        ImageView img4 = new ImageView(getActivity());
        img4.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
        img4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    @Override
    public void onPause() {
        super.onPause();
        isContinue = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isContinue = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isContinue = false;
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
                Comlables.clear();
                geneItems();
                community_detals_listview.setAdapter(ComlanleCommonAdapter);
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
                ProfessionalAnwserActivity.class);
        intent.putExtra("fromCommunityFind", 2);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


    /**
     * 专业内容
     */
    private void showEditPage() {
        Intent intent = new Intent(getActivity(),
                ProfessionalAnwserActivity.class);
        intent.putExtra("fromCommunityFind", 1);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


}
