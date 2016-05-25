package com.yikang.app.yikangserver.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.CommunityDetailsBean;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.ui.ComActivitiesActivity;
import com.yikang.app.yikangserver.ui.KeywordSearchActivity;
import com.yikang.app.yikangserver.ui.LableDetaileExampleActivity;
import com.yikang.app.yikangserver.ui.LableDetailsActivity;
import com.yikang.app.yikangserver.ui.LablesActivity;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.T;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面的我的fragment
 */
public class HomepageFragment extends BaseFragment implements OnClickListener {
    protected static final String TAG = "HomepageFragment";
    //    private static SparseIntArray defaultAvatar = new SparseIntArray();
//
//    static {
//        defaultAvatar.put(MyData.DOCTOR,R.drawable.doctor_default_avatar);
//        defaultAvatar.put(MyData.NURSING,R.drawable.nurse_default_avatar);
//        defaultAvatar.put(MyData.THERAPIST,R.drawable.therapists_default_avatar);
//    }
    private TextView homepage_tv_arrow, homepage_tv_more, homepage_wonderfulcontent_support, homepage_wonderfulcontent_comment;
    //    private GridView homepage_tables_gridview;
    private String tab_lables[] = {"运动康复", "瘦身", "营养专家", "日常训练", "体态矫正", "吞咽障碍",
            "语言康复", "糖尿病", "神经", "老人", "儿童",
            "偏瘫", "冠心病", "动脉硬化", "中风", "软组织损伤", "富贵病", "慢性肾炎"};
    private List<String> stringList = new ArrayList<String>();
//    private int NUM = 4; // 每行显示个数
//    private int LIEWIDTH;//每列宽度
//    DisplayMetrics dm;
    private ImageView homepage_iv_banner;
    private EditText search_et_content;
    private LinearLayout homepage_ll_goodanswer_first,homepage_ll_goodanswer_second;
    private ViewPager homepage_vPager;
    private ViewGroup homepage_ll_viewGroup;
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private List<TextView> lableTvs = null;
    private int labelIndex = 0;
    private XListView community_detals_listview;
    private ArrayList<CommunityDetailsBean> Comlables = new ArrayList<CommunityDetailsBean>();
    private CommonAdapter<CommunityDetailsBean> ComlanleCommonAdapter;
    private Handler mHandler;
    private View lvHeaderView;
    @Override
    public void onResume() {
        super.onResume();
        labelIndex = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        showEditPage();
        geneItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        lvHeaderView = inflater.inflate(R.layout.fragment_home_headerview, null);//listview的头view

        initView(rootView);
        findViews(lvHeaderView);
        return rootView;
    }
    private void initView(View view) {
        community_detals_listview = (XListView) view.findViewById(R.id.community_detals_listview);
        community_detals_listview.addHeaderView(lvHeaderView);
        community_detals_listview.setPullLoadEnable(true);
        ComlanleCommonAdapter = new CommonAdapter<CommunityDetailsBean>(getContext(), Comlables, R.layout.list_community_details_item) {
            @Override
            protected void convert(ViewHolder holder, CommunityDetailsBean item) {

            }
        };
        community_detals_listview.setAdapter(ComlanleCommonAdapter);
        community_detals_listview.setFocusable(false);
        community_detals_listview.setXListViewListener(new XListView.IXListViewListener() {
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
        });
        community_detals_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(),
                        ComActivitiesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.M)
    private void findViews(View view) {
        homepage_tv_arrow = (TextView) view.findViewById(R.id.homepage_tv_arrow);
        homepage_tv_more = (TextView) view.findViewById(R.id.homepage_tv_more);
        homepage_wonderfulcontent_support = (TextView) view.findViewById(R.id.homepage_wonderfulcontent_support);
        homepage_iv_banner = (ImageView) view.findViewById(R.id.homepage_iv_banner);

        homepage_wonderfulcontent_comment = (TextView) view.findViewById(R.id.homepage_wonderfulcontent_comment);
        homepage_ll_goodanswer_first = (LinearLayout) view.findViewById(R.id.homepage_ll_goodanswer_first);
        homepage_ll_goodanswer_second = (LinearLayout) view.findViewById(R.id.homepage_ll_goodanswer_second);
//        homepage_tables_gridview=(GridView)view.findViewById(R.id.homepage_tables_gridview);
        homepage_ll_goodanswer_first.setOnClickListener(this);
        homepage_ll_goodanswer_second.setOnClickListener(this);
        homepage_iv_banner.setOnClickListener(this);
        homepage_tv_more.setOnClickListener(this);
        search_et_content = (EditText) view.findViewById(R.id.search_et_content);
        search_et_content.setOnClickListener(this);
        homepage_vPager = (ViewPager) view.findViewById(R.id.homepage_vPager);
        homepage_ll_viewGroup = (ViewGroup) view.findViewById(R.id.homepage_ll_viewGroup);

        fillToViews();
//        getScreenDen();
//        LIEWIDTH = dm.widthPixels / NUM;
//        setValue();

        /**
         * 文字显示不全，隐藏点击查看 */
        TextView textview_weibo_name = (TextView) view.findViewById(R.id.homepage_ll_wonderfulcontent_text);
        String sinaName = textview_weibo_name.getText().toString();
        if (sinaName.length() > 110) {
            sinaName = sinaName.substring(0, 110) + "....查看全部";
        }
        SpannableString sinaSpanString = new SpannableString(sinaName);
        ClickableSpan clickableSpan1 = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                //startActivity(BrowserUtils.getBrowserIntent(mContext, "http://weibo.com/u/5876672114"));
                Intent intent=new Intent(getActivity(),LableDetaileExampleActivity.class);
                intent.putExtra(LableDetaileExampleActivity.EXTRA_LABLE_ANSWER,1);
                startActivity(intent);
            }
        };
        sinaSpanString.setSpan(clickableSpan1, sinaName.length() - 4, sinaName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview_weibo_name.setText(sinaSpanString);
        textview_weibo_name.setMovementMethod(LinkMovementMethod.getInstance());

//        textview_weibo_name.setOnClickListener(new OnClickListener() {
//            boolean flag=true;
//            @Override
//            public void onClick(View view) {
//                    if(flag){
//                    flag = false;
//                    textview_weibo_name.setEllipsize(null); // 展开
//                    textview_weibo_name.setSingleLine(flag);
//                }else{
//                    flag = true;
//                    textview_weibo_name.setEllipsize(TextUtils.TruncateAt.END); // 收缩
//                }
//            }
//        });
    }
//    private void setValue() {
//        MyGridViewAdapter adapter = new MyGridViewAdapter(getActivity(), stringList);
//        homepage_tables_gridview.setAdapter(adapter);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * LIEWIDTH,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        homepage_tables_gridview.setLayoutParams(params);// 设置GirdView布局参数,横向布局的关键
//        homepage_tables_gridview.setColumnWidth(dm.widthPixels / NUM);// 设置列表项宽
//        homepage_tables_gridview.setStretchMode(GridView.NO_STRETCH);
//        int count = adapter.getCount();
//        homepage_tables_gridview.setNumColumns(count);// 设置列数量=列表集合数
////        adapter.notifyDataSetChanged();
//                LOG.i("debug","position--"+homepage_tables_gridview.getFirstVisiblePosition());
//        homepage_tables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(getActivity(),"您选择了："+stringList.get(position),Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void getScreenDen() {
//        dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//    }

    /**
     * 热门标签  手动轮播
     */
    private TextView tv1;

    private void fillToViews() {

        List<View> advPics = new ArrayList<View>();

//        ImageView img1 = new ImageView(getActivity());
//        img1.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
//        advPics.add(img1);
//
//        ImageView img2 = new ImageView(getActivity());
//        img2.setBackgroundResource(R.drawable.yk_community_tab_palceholder);
//        advPics.add(img2);

        LayoutInflater mInflater = getActivity().getLayoutInflater();
//        LinearLayout ll1=new LinearLayout(getActivity());
//        ll1.setOrientation(LinearLayout.HORIZONTAL);
//        ll1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        ll1.setWeightSum(3);
//        ll1.setPadding(10,10,10,10);
        lableTvs = new ArrayList<TextView>();
        View view1 = mInflater.inflate(R.layout.linearlayout_item_choose_sticker, null);
        fillToTextview(view1);
        advPics.add(view1);
        View view2 = mInflater.inflate(R.layout.linearlayout_item_choose_sticker, null);
        fillToTextview(view2);
        advPics.add(view2);
        View view3 = mInflater.inflate(R.layout.linearlayout_item_choose_sticker, null);
        fillToTextview(view3);
        advPics.add(view3);

        for (int i = 0; i < stringList.size(); i++) {
            lableTvs.get(i).setText(stringList.get(i));
            final String name=stringList.get(i);
            //首页标签点击，直接进入标签详情页面事件
            lableTvs.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(getActivity(), stringList.get(15), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),LableDetailsActivity.class);
                    intent.putExtra(LableDetailsActivity.EXTRA_LABLE,name);
                    startActivity(intent);
                }
            });

        }

        //对imageviews进行填充
        imageViews = new ImageView[advPics.size()];
        //小图标
        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(160, 20));
            imageView.setPadding(5, 5, 5, 5);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i]
                        .setBackgroundResource(R.drawable.yk_community_tab_tiao_select);
            } else {
                imageViews[i]
                        .setBackgroundResource(R.drawable.yk_community_tab_tiao);
            }
            homepage_ll_viewGroup.addView(imageViews[i]);
        }

        homepage_vPager.setAdapter(new AdvAdapter(advPics));
        homepage_vPager.setOnPageChangeListener(new GuidePageChangeListener());
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
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.yk_community_tab_tiao_select);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.yk_community_tab_tiao);
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
     * 初始化每个view中的子view
     *
     * @param view
     */
    private void fillToTextview(View view) {
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview1);
        tv1.setTag(labelIndex++);//设置tag标记
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview2);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview3);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview4);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview5);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview6);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_mine_basic_info:
                showEditPage();
                break;
            case R.id.homepage_iv_banner:
                Toast.makeText(getActivity(),"别急，到时候进入H5活动页面",Toast.LENGTH_SHORT).show();
                break;
            case R.id.homepage_tv_more:
                showQrCodeDialog();
                break;
            case R.id.search_et_content:
                toCustomerListPage();
                break;
            case R.id.homepage_ll_goodanswer_first:
                Intent firstIntent=new Intent(getActivity(),LableDetaileExampleActivity.class);
                firstIntent.putExtra(LableDetaileExampleActivity.EXTRA_LABLE_ANSWER,1);
                startActivity(firstIntent);
                break;
            case R.id.homepage_ll_goodanswer_second:
                Intent secondIntent=new Intent(getActivity(),LableDetaileExampleActivity.class);
                secondIntent.putExtra(LableDetaileExampleActivity.EXTRA_LABLE_ANSWER,1);
                startActivity(secondIntent);
                break;
            default:
                break;
        }

        if (v.getTag() != null) { //根据tag标记判断哪个子view被点击
            LOG.i("debug", "textview--->" + stringList.get(Integer.parseInt(v.getTag().toString()) % 18));
            LOG.i("debug", "textview--->" + v.getTag());
        }
    }

    /**
     * 关键字搜索界面
     */
    private void toCustomerListPage() {
        Intent intent = new Intent(getActivity(),
                KeywordSearchActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }
    private void onLoad() {
        community_detals_listview.stopRefresh();
        community_detals_listview.stopLoadMore();
        community_detals_listview.setRefreshTime("刚刚");
    }







    /**
     * 显示更多标签页面
     */
    private void showQrCodeDialog() {
        Intent intent = new Intent(getActivity(),
                LablesActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


    /**
     * 初始化数据，获取精彩内容
     */
    private void showEditPage() {

        for (int i = 0; i < tab_lables.length; i++) {
            stringList.add(tab_lables[i]);
        }
        Api.getWonderfulContent(wonderfulContentHandler);
    }
    //获取加载数据
    private void geneItems() {
        for (int i = 0; i != 5; ++i) {
            CommunityDetailsBean lable = new CommunityDetailsBean();
            lable.setUserTvName("周琳");
            Comlables.add(lable);
        }
    }

    /**
     * 加载精彩内容回调
     */
    private ResponseCallback<List<HpWonderfulContent>> wonderfulContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();
            List<HpWonderfulContent> mHp = new ArrayList<>();
            for (HpWonderfulContent hp : data) {
                mHp.add(hp);
            }
            LOG.i("debug", "HpWonderfulContent---->" + mHp.get(0).getTitle());
        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };
}
