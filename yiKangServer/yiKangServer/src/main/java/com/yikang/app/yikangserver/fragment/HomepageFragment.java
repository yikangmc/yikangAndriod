package com.yikang.app.yikangserver.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.BannerBean;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.TagHot;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.bean.WonderfulAnswers;
import com.yikang.app.yikangserver.interfaces.NoDoubleClickListener;
import com.yikang.app.yikangserver.ui.AnswerQuestionsDetailActivity;
import com.yikang.app.yikangserver.ui.KeywordSearchActivity;
import com.yikang.app.yikangserver.ui.LableDetaileContentActivity;
import com.yikang.app.yikangserver.ui.LableDetailsActivity;
import com.yikang.app.yikangserver.ui.LablesActivity;
import com.yikang.app.yikangserver.ui.MineInfoActivity;
import com.yikang.app.yikangserver.ui.PersonalHomepageActivity;
import com.yikang.app.yikangserver.ui.ProfessionalAnwserActivity;
import com.yikang.app.yikangserver.ui.PublishActivityActivity;
import com.yikang.app.yikangserver.ui.WebviewActivivty;
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面的我的fragment
 */
public class HomepageFragment extends BaseFragment implements OnClickListener {
    protected static final String TAG = "HomepageFragment";
    private CircleImageView homepage_ll_goodanswer_pictrue,homepage_ll_goodanswer_pictrue2,homepage_ll_goodanswer_pictrue3;
    private TextView tv_position,homepage_wonderfulcontent_supportlike,homepage_tv_arrow, homepage_tv_more, homepage_wonderfulcontent_support, homepage_wonderfulcontent_comment, homepage_ll_wonderfulcontent_text, tv_good_content_title, tv_good_content_username;
    private TextView tv_posion3,tv_posion2,tv_posion1,homepage_ll_goodanswer_text,homepage_ll_goodanswer_text2,homepage_ll_goodanswer_text3, homepage_ll_goodanswer_title,homepage_ll_goodanswer_title2,homepage_ll_goodanswer_title3,homepage_ll_goodanswer_zan,homepage_ll_goodanswer_zan2,homepage_ll_goodanswer_zan3,homepage_ll_goodanswer_name,homepage_ll_goodanswer_name2,homepage_ll_goodanswer_name3,homepage_ll_goodanswer_time,homepage_ll_goodanswer_time2,homepage_ll_goodanswer_time3;
    private String tab_lables[] = {"运动康复"};
    private List<TagHot> stringList = new ArrayList<TagHot>();
    private ImageView homepage_iv_banner, iv_good_content;
    private EditText search_et_content;
    private LinearLayout ll_homepage_wonderfulcontent_comment,homepage_ll_more_lable,homepage_ll_more_answer,homepage_ll_goodanswer_first, homepage_ll_goodanswer_second, homepage_ll_goodanswer_third, ll_wonderfulcontent_hp;
    private ViewPager homepage_vPager;
    private ViewGroup homepage_ll_viewGroup;
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private List<TextView> lableTvs = null;
    private List<ImageView> lableTIvs = null;
    private int labelIndex = 0;
    private XListView community_detals_listview;
    private CommonAdapter<WonderfulAnswers> ComlanleCommonAdapter;
    private Handler mHandler;
    private View lvHeaderView;
    private View lvFooterView;
    private List<BannerBean> bannerHp;
    private List<TagHot> tagHot = new ArrayList<>();
    private List<HpWonderfulContent> goodContentHp;
    private List<WonderfulAnswers> wonderfulActivity;
    private TextView tv_tag1;//标签1
    private TextView tv_tag2;//标签2
    private TextView tv_tag3;//标签3
    private TextView tv_tag4;//标签4
    private TextView homepage_tv_goodanswer_banner_name;//标签4
    private LinearLayout tv_publish_activity;//发布活动
    private List<View> advPics = new ArrayList<View>();
    private DisplayImageOptions options; //配置图片加载及显示选项
    private DisplayImageOptions option; //配置图片加载及显示选项
    private DisplayImageOptions optionss; //配置图片加载及显示选项
    private String wonderfulContentId;
    private String bannerUrl;
    private String bannerName;
    private LinearLayout community_find_personal_info_ll1;
    private LinearLayout community_find_personal_info_ll2;
    private LinearLayout community_find_personal_info_ll3;
    private LinearLayout community_find_personal_info_ll4;
    private LinearLayout community_find_personal_info_ll5;
    private CircleImageView yk_community_kfs_header1;
    private CircleImageView yk_community_kfs_header2;
    private CircleImageView yk_community_kfs_header3;
    private CircleImageView yk_community_kfs_header4;
    private CircleImageView yk_community_kfs_header5;
    private TextView yk_community_kfs_name1;
    private TextView yk_community_kfs_name2;
    private TextView yk_community_kfs_name3;
    private TextView yk_community_kfs_name4;
    private TextView yk_community_kfs_name5;
    private TextView yk_community_kfs_unit1;
    private TextView yk_community_kfs_unit2;
    private TextView yk_community_kfs_unit3;
    private TextView yk_community_kfs_unit4;
    private TextView yk_community_kfs_unit5;
    private String yk_community_kfs_id1;
    private String yk_community_kfs_id2;
    private String yk_community_kfs_id3;
    private String yk_community_kfs_id4;
    private String yk_community_kfs_id5;
    @Override
    public void onResume() {
        super.onResume();
        labelIndex = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .build();
        optionss = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
               // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败

                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .displayer(new FadeInBitmapDisplayer(500))
               // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        mHandler = new Handler();

        showEditPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        lvHeaderView = inflater.inflate(R.layout.fragment_home_headerview, null);//listview的头view
        lvFooterView = inflater.inflate(R.layout.fragment_home_lvfooterview, null);//listview的头view

        initView(rootView);
        findViews(lvHeaderView);
       findViewss(lvFooterView);

        return rootView;
    }

    private void findViewss(View lvFooterView) {
        tv_publish_activity = (LinearLayout) lvFooterView.findViewById(R.id.tv_publish_activity);
        tv_publish_activity.setOnClickListener(this);
    }

    private void initView(View view) {
        community_detals_listview = (XListView) view.findViewById(R.id.community_detals_listview);
        community_detals_listview.addHeaderView(lvHeaderView);
        community_detals_listview.addFooterView(lvFooterView);

        community_detals_listview.setPullLoadEnable(false);

        community_detals_listview.setFocusable(false);
        community_detals_listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Api.getKangfushi(bannerHandler);
                            //获取首页banner列表
                            Api.getFindBannerContent(bannerContentHandler);
                            //获取首页精彩内容
                           // Api.getWonderfulContent(wonderfulContentHandler);
                        //获取首页精彩解答
                            Api.getWonderfulAnswer(wonderfulActivityHandler);


                        Api.getKangfushi(bannerHandler);
                        onLoad();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                Intent Intent = new Intent(getActivity(), AnswerQuestionsDetailActivity.class);
                Intent.putExtra("AnswersDetailsActivityID", wonderfulActivity.get(position-2).getQuestionAnswerId()+ "");
                startActivity(Intent);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void findViews(View view) {
        community_find_personal_info_ll1 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll1);
        community_find_personal_info_ll2 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll2);
        community_find_personal_info_ll3 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll3);
        community_find_personal_info_ll4 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll4);
        community_find_personal_info_ll5 = (LinearLayout) view.findViewById(R.id.community_find_personal_info_ll5);
        yk_community_kfs_header1 = (CircleImageView) view.findViewById(R.id.yk_community_kfs_header1);
        yk_community_kfs_header2 = (CircleImageView) view.findViewById(R.id.yk_community_kfs_header2);
        yk_community_kfs_header3 = (CircleImageView) view.findViewById(R.id.yk_community_kfs_header3);
        yk_community_kfs_header4 = (CircleImageView) view.findViewById(R.id.yk_community_kfs_header4);
        yk_community_kfs_header5 = (CircleImageView) view.findViewById(R.id.yk_community_kfs_header5);
        yk_community_kfs_name1 = (TextView) view.findViewById(R.id.yk_community_kfs_name1);
        yk_community_kfs_name2 = (TextView) view.findViewById(R.id.yk_community_kfs_name2);
        yk_community_kfs_name3 = (TextView) view.findViewById(R.id.yk_community_kfs_name3);
        yk_community_kfs_name4 = (TextView) view.findViewById(R.id.yk_community_kfs_name4);
        yk_community_kfs_name5 = (TextView) view.findViewById(R.id.yk_community_kfs_name5);
        yk_community_kfs_unit1 = (TextView) view.findViewById(R.id.yk_community_kfs_unit1);
        yk_community_kfs_unit2 = (TextView) view.findViewById(R.id.yk_community_kfs_unit2);
        yk_community_kfs_unit3 = (TextView) view.findViewById(R.id.yk_community_kfs_unit3);
        yk_community_kfs_unit4 = (TextView) view.findViewById(R.id.yk_community_kfs_unit4);
        yk_community_kfs_unit5 = (TextView) view.findViewById(R.id.yk_community_kfs_unit5);
        community_find_personal_info_ll1.setOnClickListener(this);
        community_find_personal_info_ll2.setOnClickListener(this);
        community_find_personal_info_ll3.setOnClickListener(this);
        community_find_personal_info_ll4.setOnClickListener(this);
        community_find_personal_info_ll5.setOnClickListener(this);
        tv_tag1 = (TextView) view.findViewById(R.id.tv_tag1);
        tv_tag2 = (TextView) view.findViewById(R.id.tv_tag2);
        tv_tag3 = (TextView) view.findViewById(R.id.tv_tag3);
        tv_tag4 = (TextView) view.findViewById(R.id.tv_tag4);
        tv_position = (TextView) view.findViewById(R.id.tv_position);
        homepage_tv_arrow = (TextView) view.findViewById(R.id.homepage_tv_arrow);

        tv_good_content_title = (TextView) view.findViewById(R.id.tv_good_content_title);
        tv_good_content_username = (TextView) view.findViewById(R.id.tv_good_content_username);
        homepage_ll_wonderfulcontent_text = (TextView) view.findViewById(R.id.homepage_ll_wonderfulcontent_text);
        homepage_wonderfulcontent_support = (TextView) view.findViewById(R.id.homepage_wonderfulcontent_support);
        homepage_wonderfulcontent_supportlike = (TextView) view.findViewById(R.id.homepage_wonderfulcontent_supportlike);
        homepage_wonderfulcontent_comment = (TextView) view.findViewById(R.id.homepage_wonderfulcontent_comment);

        homepage_iv_banner = (ImageView) view.findViewById(R.id.homepage_iv_banner);
        iv_good_content = (ImageView) view.findViewById(R.id.iv_good_content);

        ll_wonderfulcontent_hp = (LinearLayout) view.findViewById(R.id.ll_wonderfulcontent_hp);
        homepage_ll_more_lable = (LinearLayout) view.findViewById(R.id.homepage_ll_more_lable);

        ll_wonderfulcontent_hp.setOnClickListener(this);
        homepage_ll_more_lable.setOnClickListener(this);
        homepage_ll_more_lable.setOnClickListener(this);
//        ll_homepage_wonderfulcontent_comment.setOnClickListener(this);
        homepage_wonderfulcontent_support.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

                Api.support(Integer.parseInt(wonderfulContentId), zanHandler);


            }
        });
        homepage_wonderfulcontent_supportlike.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Api.support(Integer.parseInt(wonderfulContentId), zanHandlers);

            }
        });
        homepage_iv_banner.setOnClickListener(this);

        search_et_content = (EditText) view.findViewById(R.id.search_et_content);
        search_et_content.setOnClickListener(this);
        homepage_vPager = (ViewPager) view.findViewById(R.id.homepage_vPager);
        homepage_ll_viewGroup = (ViewGroup) view.findViewById(R.id.homepage_ll_viewGroup);

    }

    /**
     * 热门标签  手动轮播
     */
    private TextView tv1;
    private ImageView iv1;

    private void fillToViews() {
       advPics = new ArrayList<View>();
        LayoutInflater mInflater = getActivity().getLayoutInflater();
        lableTvs = new ArrayList<TextView>();
        lableTIvs = new ArrayList<ImageView>();
        View view1 = mInflater.inflate(R.layout.linearlayout_item_choose_sticker, null);
        fillToTextview(view1);
        advPics.add(view1);
        View view2 = mInflater.inflate(R.layout.linearlayout_item_choose_sticker, null);
        fillToTextview(view2);
        advPics.add(view2);
        View view3 = mInflater.inflate(R.layout.linearlayout_item_choose_sticker, null);
        fillToTextview(view3);
        advPics.add(view3);

        for (int i = 0; i <stringList.size() ; i++) {
            lableTvs.get(i).setText(stringList.get(i).getTagName());
           // ImageLoader.getInstance().displayImage(stringList.get(i).getTagPic(), lableTIvs.get(i),options);
            final String name = stringList.get(i).getTagName();
            final String tagLibId = stringList.get(i).getTaglibId();
            final String tagIsStore= stringList.get(i).getIsStore();
          //  LOG.i("debug", "HpWonderfulContent---->" + name + "热门标签");
            //首页标签点击，直接进入标签详情页面事件
            lableTvs.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LableDetailsActivity.class);
                    intent.putExtra(LableDetailsActivity.EXTRA_LABLE, name);
                    intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ID, tagLibId + "");
                    intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE,tagIsStore+"");
                    startActivity(intent);
                }
            });

        }

        //对imageviews进行填充
        imageViews = new ImageView[advPics.size()];
        //小图标
        for (int i = 0; i < advPics.size(); i++) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 4);
            params.setMargins(0, 0,26, 0);
            imageView = new ImageView(getActivity());
            imageView.setLayoutParams(params);
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
        iv1 = (ImageView) view.findViewById(R.id.homepage_hot_iv1);
        tv1.setTag(labelIndex++);//设置tag标记
        iv1.setTag(labelIndex++);//设置tag标记
        lableTIvs.add(iv1);
        lableTvs.add(tv1);

        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview2);
        iv1 = (ImageView) view.findViewById(R.id.homepage_hot_iv2);
        tv1.setTag(labelIndex++);
        iv1.setTag(labelIndex++);//设置tag标记
        lableTIvs.add(iv1);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview3);
        iv1 = (ImageView) view.findViewById(R.id.homepage_hot_iv3);
        tv1.setTag(labelIndex++);
        iv1.setTag(labelIndex++);//设置tag标记
        lableTIvs.add(iv1);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview4);
        iv1 = (ImageView) view.findViewById(R.id.homepage_hot_iv4);
        iv1.setTag(labelIndex++);//设置tag标记
        lableTIvs.add(iv1);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview5);
        iv1 = (ImageView) view.findViewById(R.id.homepage_hot_iv5);
        iv1.setTag(labelIndex++);//设置tag标记
        lableTIvs.add(iv1);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
        tv1 = (TextView) view.findViewById(R.id.homepage_hot_textview6);
        iv1 = (ImageView) view.findViewById(R.id.homepage_hot_iv6);
        iv1.setTag(labelIndex++);//设置tag标记
        lableTIvs.add(iv1);
        tv1.setTag(labelIndex++);
        lableTvs.add(tv1);
    }

    /**
     * 首页精彩内容点赞的回调
     */
    private ResponseCallback<HpWonderfulContent> zanHandler = new ResponseCallback<HpWonderfulContent>() {


        @Override
        public void onSuccess(HpWonderfulContent data) {
            hideWaitingUI();
           // AppContext.showToast("点赞成功");
            homepage_wonderfulcontent_supportlike.setVisibility(View.VISIBLE);
            homepage_wonderfulcontent_supportlike.setText((Integer.parseInt(homepage_wonderfulcontent_support.getText().toString())+1)+"");
            homepage_wonderfulcontent_support.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            AppContext.showToast("点赞失败:" + message);

        }
    };


    /**
     * 取消点赞回调
     */
    private ResponseCallback<HpWonderfulContent> zanHandlers = new ResponseCallback<HpWonderfulContent>() {



        @Override
        public void onSuccess(HpWonderfulContent data) {
           // Toast.makeText(getActivity(), "取消点赞success！", Toast.LENGTH_LONG).show();
          //  LOG.i("debug", "HpWonderfulContent---->" + data);
            homepage_wonderfulcontent_supportlike.setVisibility(View.GONE);
            homepage_wonderfulcontent_support.setText((Integer.parseInt(homepage_wonderfulcontent_supportlike.getText().toString()) - 1) + "");
            homepage_wonderfulcontent_support.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };
    private TextView tv_next,tv_goto;
    private Dialog dialog;

    private void showConfigDialog() {
        // TODO Auto-generated method stub
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setContentView(R.layout.dialog_config);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.allradius_white);
        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.height = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(attributes);

        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.tv_next).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_goto).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), MineInfoActivity.class);
                intent.putExtra(MineInfoActivity.EXTRA_USER,  AppContext.getAppContext().getUser());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_find_personal_info_ll1:
                toCustomerListPage( yk_community_kfs_id1);
                break;
            case R.id.community_find_personal_info_ll2:
                toCustomerListPage(yk_community_kfs_id2);
                break;
            case R.id.community_find_personal_info_ll3:
                toCustomerListPage(yk_community_kfs_id3);
                break;
            case R.id.community_find_personal_info_ll4:
                toCustomerListPage(yk_community_kfs_id4);
                break;
            case R.id.community_find_personal_info_ll5:
                toCustomerListPage(yk_community_kfs_id5);
                break;
            case R.id.ly_mine_basic_info:
                showEditPage();
                break;
            case R.id.homepage_iv_banner:

                Intent WebIntent = new Intent(getActivity(), WebviewActivivty.class);
                WebIntent.putExtra("bannerUrl",bannerUrl);
                WebIntent.putExtra("bannerName",bannerName);
                startActivity(WebIntent);

                break;
            case R.id.homepage_ll_more_lable:
                showQrCodeDialog();
                break;
            case R.id.tv_publish_activity:
                showAnserQuestions();

                break;

            case R.id.search_et_content:
                //Toast.makeText(getActivity(),"尽情期待~",Toast.LENGTH_SHORT).show();
                AppContext.showToast("尽情期待~");
                //toSearchPage();
                break;
            case R.id.homepage_ll_wonderfulcontent_text:
                Intent intents = new Intent(getActivity(), LableDetaileContentActivity.class);
                if (goodContentHp != null) {
                    intents.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, goodContentHp.get(0).getForumPostId() + "");
                    intents.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT, goodContentHp.get(0).getUserName() + "");
                }
                startActivity(intents);
                break;

            case R.id.ll_wonderfulcontent_hp:
                Intent intent = new Intent(getActivity(), LableDetaileContentActivity.class);
                if (goodContentHp != null) {
                    intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, goodContentHp.get(0).getForumPostId() + "");
                    intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT, goodContentHp.get(0).getUserName() + "");
                }
                startActivity(intent);
                break;

            default:
                break;
        }

        if (v.getTag() != null) { //根据tag标记判断哪个子view被点击
           // LOG.i("debug", "textview--->" + stringList.get(Integer.parseInt(v.getTag().toString()) % 18));
          //  LOG.i("debug", "textview--->" + v.getTag());
        }
    }


    /**
     * 显精心问答
     */
    private void showAnserQuestions() {
        Intent intent = new Intent(getActivity(),
                ProfessionalAnwserActivity.class);
        intent.putExtra("fromCommunityFind", 2);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


    /**
     * 发布活动
     */
    private void publishActivity() {
        Intent intent4 = new Intent(getActivity(),
                PublishActivityActivity.class);
        intent4.putExtra(PublishActivityActivity.MESSAGE_INFOS, "活动形式");
        startActivity(intent4);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }

    /**
     * 关键字搜索界面
     */
    private void toSearchPage() {
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
     * 初始化数据
     */
    private void showEditPage() {

        //获取首页banner列表
        Api.getFindBannerContent(bannerContentHandler);
        //获取首页精彩内容
        Api.getWonderfulContent(wonderfulContentHandler);
        //获取首页精彩解答
        Api.getWonderfulAnswer(wonderfulActivityHandler);
        //获取首页热门标签
        Api.getHotLableContent(hotLableHandler);
        Api.getKangfushi(bannerHandler);
    }


    /**
     * 加载首页banner内容回调
     */
    private ResponseCallback<List<BannerBean>> bannerContentHandler = new ResponseCallback<List<BannerBean>>() {

        @Override
        public void onSuccess(List<BannerBean> data) {
            hideWaitingUI();
            bannerUrl=data.get(0).getActionUrl();
            bannerName=data.get(0).getTitle();
            bannerHp = new ArrayList<>();
            for (BannerBean hp : data) {
                bannerHp.add(hp);
            }
            //LOG.i("debug", "HpWonderfulContent---->" + bannerHp.get(0).getBanerPic()+"哈哈"+bannerHp.get(0).getActionUrl());
            if (bannerHp.size() != 0 && bannerHp != null) {

                ImageLoader.getInstance().displayImage(bannerHp.get(0).getBanerPic(), homepage_iv_banner,optionss);
            }
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
           // AppContext.showToast(message);
        }
    };
    /**
     * 加载首页热门标签回调
     */
    private ResponseCallback<List<TagHot>> hotLableHandler = new ResponseCallback<List<TagHot>>() {


        @Override
        public void onSuccess(List<TagHot> data) {
            hideWaitingUI();
            for (TagHot hp : data) {
                tagHot.add(hp);
            }
           // LOG.i("debug", "HpWonderfulContent---->" + data.size() + "嘎嘎嘎嘎嘎嘎");
            if(data.size()>=18) {
                for (int i = 0; i < 18; i++) {
                    stringList.add(data.get(i));

                   // LOG.i("debug", "HpWonderfulContent---->" + data.get(i).getTagName());
                }
                fillToViews();
            }else{
                for (int i = 0; i < data.size(); i++) {
                    stringList.add(data.get(i));

                   // LOG.i("debug", "HpWonderfulContent---->" + data.get(i).getTagName());
                }
                fillToViews();
            }
           // LOG.i("debug", "HpWonderfulContent---->" + stringList.size() + "StringList的数量");
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
           // AppContext.showToast(message);
        }
    };

    /**
     * 加载首页精彩内容回调
     */
    private ResponseCallback<List<HpWonderfulContent>> wonderfulContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {



        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();
            goodContentHp = new ArrayList<>();
            wonderfulContentId=data.get(0).getForumPostId()+"";
            for (HpWonderfulContent hp : data) {
                goodContentHp.add(hp);
            }
            if (goodContentHp != null && goodContentHp.size() != 0) {
               // LOG.i("debug", "HpWonderfulContent---->" + data);
                ImageLoader.getInstance().displayImage(goodContentHp.get(0).getRecommendPicUrl(), iv_good_content,optionss);
                tv_good_content_title.setText(goodContentHp.get(0).getTitle());
                tv_good_content_username.setText(goodContentHp.get(0).getUserName());
                homepage_ll_wonderfulcontent_text.setText(goodContentHp.get(0).getContent() + "...");
                homepage_wonderfulcontent_comment.setText(goodContentHp.get(0).getAnswersNums() + "");
                PositionUtils.getPosition(goodContentHp.get(0).getUserPosition(),tv_position);

                if(goodContentHp.get(0).getIsStar()==0) {
                    homepage_wonderfulcontent_support.setVisibility(View.VISIBLE);
                    homepage_wonderfulcontent_support.setText(goodContentHp.get(0).getStars() + "");
                    homepage_wonderfulcontent_supportlike.setVisibility(View.GONE);
                }
                if(goodContentHp.get(0).getIsStar()==1){
                    homepage_wonderfulcontent_support.setVisibility(View.GONE);
                    homepage_wonderfulcontent_supportlike.setVisibility(View.VISIBLE);
                    homepage_wonderfulcontent_supportlike.setText(goodContentHp.get(0).getStars() + "");
                }


                if (goodContentHp.get(0).getTaglibs() != null && goodContentHp.get(0).getTaglibs().size() == 1) {
                    tv_tag1.setText(goodContentHp.get(0).getTaglibs().get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                }
                if (goodContentHp.get(0).getTaglibs() != null && goodContentHp.get(0).getTaglibs().size() == 2) {
                    tv_tag1.setText(goodContentHp.get(0).getTaglibs().get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(goodContentHp.get(0).getTaglibs().get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                }
                if (goodContentHp.get(0).getTaglibs() != null && goodContentHp.get(0).getTaglibs().size() == 3) {
                    tv_tag1.setText(goodContentHp.get(0).getTaglibs().get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(goodContentHp.get(0).getTaglibs().get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                    tv_tag3.setText(goodContentHp.get(0).getTaglibs().get(2).getTagName());
                    tv_tag3.setBackgroundResource(R.color.main_background_color);
                }
                if (goodContentHp.get(0).getTaglibs() != null && goodContentHp.get(0).getTaglibs().size() == 4) {
                    tv_tag1.setText(goodContentHp.get(0).getTaglibs().get(0).getTagName());
                    tv_tag1.setBackgroundResource(R.color.main_background_color);
                    tv_tag2.setText(goodContentHp.get(0).getTaglibs().get(1).getTagName());
                    tv_tag2.setBackgroundResource(R.color.main_background_color);
                    tv_tag3.setText(goodContentHp.get(0).getTaglibs().get(2).getTagName());
                    tv_tag3.setBackgroundResource(R.color.main_background_color);
                    tv_tag4.setText(goodContentHp.get(0).getTaglibs().get(3).getTagName());
                    tv_tag4.setBackgroundResource(R.color.main_background_color);
                }
            }


        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
           // AppContext.showToast(message);
        }
    };

    /**
     * 首页精彩活动推荐的回调
     */
    private ResponseCallback<List<WonderfulAnswers>> wonderfulActivityHandler = new ResponseCallback<List<WonderfulAnswers>>() {

        @Override
        public void onSuccess(List<WonderfulAnswers> data) {
            hideWaitingUI();
            wonderfulActivity=data;
            ComlanleCommonAdapter = new CommonAdapter<WonderfulAnswers>(getActivity(), wonderfulActivity, R.layout.list_wonderful_answers_item) {
                @Override
                protected void convert(ViewHolder holder, final WonderfulAnswers item) {

                    LinearLayout homepage_ll_goodanswer_third = (LinearLayout) holder.getView(R.id.homepage_ll_goodanswer_third);
                    TextView tv_posion3 = (TextView) holder.getView(R.id.tv_posion3);
                    ImageView tv_pic3 = (ImageView) holder.getView(R.id.tv_pic3);
                    TextView homepage_ll_goodanswer_text3 = (TextView) holder.getView(R.id.homepage_ll_goodanswer_text3);
                    TextView  homepage_ll_goodanswer_title3 = (TextView) holder.getView(R.id.homepage_ll_goodanswer_title3);
                    TextView  homepage_ll_goodanswer_zan3 = (TextView)holder.getView(R.id.homepage_ll_goodanswer_zan3);
                    TextView   homepage_ll_goodanswer_name3 = (TextView) holder.getView(R.id.homepage_ll_goodanswer_name3);
                    TextView  homepage_ll_goodanswer_time3 = (TextView) holder.getView(R.id.homepage_ll_goodanswer_time3);
                    CircleImageView   homepage_ll_goodanswer_pictrue3 = (CircleImageView) holder.getView(R.id.homepage_ll_goodanswer_pictrue3);
                    homepage_ll_goodanswer_title3.setText(item.getQuestion().getTitle());
                    homepage_ll_goodanswer_zan3.setText(item.getStarNum()+"");
                    homepage_ll_goodanswer_name3.setText(item.getUserName()+"");
                    homepage_ll_goodanswer_time3.setText("回答于"+TimeCastUtils.compareDateTime(System.currentTimeMillis(),item.getCreateTime()));
                    ImageLoader.getInstance().displayImage(item.getPhotoUrl(), homepage_ll_goodanswer_pictrue3,option);
                    PositionUtils.getPosition(item.getUserPosition(),tv_posion3);
                    homepage_ll_goodanswer_text3.setText(item.getContent());
                }
            };
            community_detals_listview.setAdapter(ComlanleCommonAdapter);
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
           // AppContext.showToast(message);
            community_detals_listview.setAdapter(ComlanleCommonAdapter);
        }
    };




    /**
     * 推荐康复师的回调
     */
    private ResponseCallback<List<User>> bannerHandler = new ResponseCallback<List<User>>() {

        @Override
        public void onSuccess(List<User> data) {
            hideWaitingUI();

            //LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈哈");

            if(data.size()>=5){
                yk_community_kfs_id1=data.get(0).getUserId();
                yk_community_kfs_id2=data.get(1).getUserId();
                yk_community_kfs_id3=data.get(2).getUserId();
                yk_community_kfs_id4=data.get(3).getUserId();
                yk_community_kfs_id5=data.get(4).getUserId();
            }else {
                yk_community_kfs_id1 = data.get(0).getUserId();
                yk_community_kfs_id2 = data.get(0).getUserId();
                yk_community_kfs_id3 = data.get(0).getUserId();
                yk_community_kfs_id4 = data.get(0).getUserId();
                yk_community_kfs_id5 = data.get(0).getUserId();
            }
            if(data.size()>=5) {
                ImageLoader.getInstance().displayImage(data.get(0).getAvatarImg(), yk_community_kfs_header1, option);
                ImageLoader.getInstance().displayImage(data.get(1).getAvatarImg(), yk_community_kfs_header2, option);
                ImageLoader.getInstance().displayImage(data.get(2).getAvatarImg(), yk_community_kfs_header3, option);
                ImageLoader.getInstance().displayImage(data.get(3).getAvatarImg(), yk_community_kfs_header4, option);
                ImageLoader.getInstance().displayImage(data.get(4).getAvatarImg(), yk_community_kfs_header5, option);
            }else {
                ImageLoader.getInstance().displayImage(data.get(0).getAvatarImg(), yk_community_kfs_header1, option);
                ImageLoader.getInstance().displayImage(data.get(0).getAvatarImg(), yk_community_kfs_header2, option);
                ImageLoader.getInstance().displayImage(data.get(0).getAvatarImg(), yk_community_kfs_header3, option);
                ImageLoader.getInstance().displayImage(data.get(0).getAvatarImg(), yk_community_kfs_header4, option);
                ImageLoader.getInstance().displayImage(data.get(0).getAvatarImg(), yk_community_kfs_header5, option);
            }
            if(data.size()>=5) {
                yk_community_kfs_name1.setText(data.get(0).getName());
                yk_community_kfs_name2.setText(data.get(1).getName());
                yk_community_kfs_name3.setText(data.get(2).getName());
                yk_community_kfs_name4.setText(data.get(3).getName());
                yk_community_kfs_name5.setText(data.get(4).getName());
            }else {
                yk_community_kfs_name1.setText(data.get(0).getName());
                yk_community_kfs_name2.setText(data.get(0).getName());
                yk_community_kfs_name3.setText(data.get(0).getName());
                yk_community_kfs_name4.setText(data.get(0).getName());
                yk_community_kfs_name5.setText(data.get(0).getName());
            }
            if(data.size()>=5) {
                if(data.get(0).getAddressDetail().length()>6){
                    yk_community_kfs_unit1.setText(data.get(0).getAddressDetail().substring(0,6));
                }else {
                    yk_community_kfs_unit1.setText(data.get(0).getAddressDetail());
                }
                if(data.get(1).getAddressDetail().length()>6) {
                    yk_community_kfs_unit2.setText(data.get(1).getAddressDetail().substring(0,6));
                }else {
                    yk_community_kfs_unit2.setText(data.get(1).getAddressDetail());
                }
                if(data.get(2).getAddressDetail().length()>6){
                    yk_community_kfs_unit3.setText(data.get(2).getAddressDetail().substring(0,6));
                }else {
                    yk_community_kfs_unit3.setText(data.get(2).getAddressDetail());
                }
                if(data.get(3).getAddressDetail().length()>6){
                    yk_community_kfs_unit4.setText(data.get(3).getAddressDetail().substring(0,6));
                }else {
                    yk_community_kfs_unit4.setText(data.get(3).getAddressDetail());
                }
                if(data.get(4).getAddressDetail().length()>6){
                    yk_community_kfs_unit4.setText(data.get(4).getAddressDetail().substring(0,6));
                }else {
                    yk_community_kfs_unit5.setText(data.get(4).getAddressDetail());
                }
            }else {
                yk_community_kfs_unit1.setText(data.get(0).getAddressDetail());
                yk_community_kfs_unit2.setText(data.get(0).getAddressDetail());
                yk_community_kfs_unit3.setText(data.get(0).getAddressDetail());
                yk_community_kfs_unit4.setText(data.get(0).getAddressDetail());
                yk_community_kfs_unit5.setText(data.get(0).getAddressDetail());
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
     * 个人信息简介
     */
    private void toCustomerListPage(String id) {
        Intent intent = new Intent(getActivity(),
                PersonalHomepageActivity.class);
        intent.putExtra("userId",id);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }

}
