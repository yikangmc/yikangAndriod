package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.bean.BannerBean;
import com.yikang.app.yikangserver.bean.CommunityDetailsBean;
import com.yikang.app.yikangserver.bean.ForumPostsImage;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.Taglibs;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.photo.PhotoActivitys;
import com.yikang.app.yikangserver.ui.ContentDetailsActivity;
import com.yikang.app.yikangserver.ui.LableDetailsActivity;
import com.yikang.app.yikangserver.ui.LablesActivity;
import com.yikang.app.yikangserver.ui.ProfessionalAnwserActivity;
import com.yikang.app.yikangserver.ui.WebComunicateviewActivivty;
import com.yikang.app.yikangserver.ui.WonderfulActivityActivity;
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.io.Serializable;
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
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private XListView community_detals_listview;
    private ArrayList<CommunityDetailsBean> Comlables = new ArrayList<CommunityDetailsBean>();
    private CommonAdapter<LableDetailsBean> lanleCommonAdapter;
    private Handler mHandler;
    private View lvHeaderView;
    private FrameLayout community_fl_top2, community_fl_top1;
    private FrameLayout community_fl_top3, community_fl_top4;
    List<HpWonderfulContent> bannerHp = new ArrayList<>();
    List<User> bannerHps;
    private DisplayImageOptions option; //配置图片加载及显示选项
    private DisplayImageOptions options; //配置图片加载及显示选项
    private CommonAdapter<ForumPostsImage> alllableEditorCAdapter;
    private String bannerUrl;
    private String bannerName;
    private ImageView homepage_iv_banner;
    private int start = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(500))
                //.displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        mHandler = new Handler();
        //最热帖子
        Api.getHotTiezi(1,allLableContentHandler);
        //顶部banner图
        Api.getBannerContent(bannerHandlers);


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

        for (int i = 0; i < bannerHp.size(); ++i) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(bannerHp.get(i).getUserName()+"");
            lable.setHeadIvUrl(bannerHp.get(i).getPhotoUrl()+"");
            lable.setHeadTvLable(bannerHp.get(i).getDesignationName());
            lable.setDetailTv(bannerHp.get(i).getTitle());
            lable.setDetailLable(bannerHp.get(i).getContent());
            lable.setDetailSupport(bannerHp.get(i).getStars() + "");
            lable.setReleaseTime(TimeCastUtils.compareDateTime(System.currentTimeMillis(), bannerHp.get(i).getCreateTime()));
            lable.setAnswersNums(bannerHp.get(i).getAnswersNums() + "");
            lable.setDetailIvUrls(bannerHp.get(i).getForumPostsImage());
            lable.setDetailTaglibs(bannerHp.get(i).getTaglibs());
            lable.setUserPositions(bannerHp.get(i).getUserPosition());
            lable.setDetailDiscuss(bannerHp.get(i).getForumPostId()+"");
            lables.add(lable);
        }
    }

    private ResponseCallback<List<BannerBean>> bannerHandlers = new ResponseCallback<List<BannerBean>>() {
        @Override
        public void onSuccess(List<BannerBean> data) {
            bannerUrl=data.get(0).getActionUrl();
            bannerName=data.get(0).getTitle();
            bannerHp = new ArrayList<>();
           /* for (BannerBean hp : data) {
                bannerHp.add(hp);
            }*/
            //LOG.i("debug", "HpWonderfulContent---->" + bannerHp.get(0).getBanerPic()+"哈哈"+bannerHp.get(0).getActionUrl());
            if (data.size() != 0 && data != null) {

                ImageLoader.getInstance().displayImage(data.get(0).getBanerPic(), homepage_iv_banner,options);
            }
        }

        @Override
        public void onFailure(String status, String message) {

        }
    };


    /**
     * 最热帖子的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentHandler = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {
            hideWaitingUI();

            if(data.size()<10){
                community_detals_listview.setPullLoadEnable(false);
            }else {
                community_detals_listview.setPullLoadEnable(true);
            }
            onLoad();

            bannerHp = new ArrayList<>();
            for (HpWonderfulContent hp : data) {
                bannerHp.add(hp);
            }
            lables.clear();
            geneItems();
            community_detals_listview.setAdapter(lanleCommonAdapter);
           // LOG.i("debug", "HpWonderfulContent---->" + data + "嘎嘎嘎");

        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            // AppContext.showToast(message);
        }
    };


    private void findViews(View view) {
        community_detals_listview = (XListView) view.findViewById(R.id.community_detals_listview);
        community_detals_listview.setPullLoadEnable(false);
        community_detals_listview.addHeaderView(lvHeaderView);
        lanleCommonAdapter = new CommonAdapter<LableDetailsBean>(getContext(), lables, R.layout.list_find_details_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_details_tv_name);
                CircleImageView lables_details_iv_user = holder.getView(R.id.lables_details_iv_user);
                TextView lables_details_tv_title = holder.getView(R.id.lables_details_tv_title);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_details_tv_zhicheng);
                TextView lables_tv_time = holder.getView(R.id.lables_details_tv_pushtime);
                TextView lable_details_tv_support = holder.getView(R.id.homepage_wonderfulcontent_support);
                TextView lable_details_tv__comment = holder.getView(R.id.homepage_wonderfulcontent_comment);
                TextView homepage_ll_wonderfulcontent_text = holder.getView(R.id.homepage_ll_wonderfulcontent_text);
                GridView community_mine_lables_gridview = holder.getView(R.id.community_mine_lables_gridview);
                TextView tv_tag1 = holder.getView(R.id.tv_tag1);
                TextView tv_tag2 = holder.getView(R.id.tv_tag2);
                TextView tv_tag3 = holder.getView(R.id.tv_tag3);
                TextView tv_tag4 = holder.getView(R.id.tv_tag4);
                ImageLoader.getInstance().displayImage(item.getHeadIvUrl(), lables_details_iv_user,option);
                lables_tv_title.setText(item.getHeadTvName());

                lables_details_tv_title.setText(item.getDetailTv());
                if(item.getUserPositions()>0) {
                    PositionUtils.getPosition(item.getUserPositions(), lables_tv_tiezi);
                    lables_tv_tiezi.setText("");
                    lables_tv_tiezi.setBackgroundResource(R.color.transparent);
                }
                 if(item.getUserPositions()==0){
                    lables_tv_tiezi.setText(item.getHeadTvLable());
                    if(item.getHeadTvLable()!=null) {
                        lables_tv_tiezi.setBackgroundResource(R.drawable.allradius_zhicheng);
                    }
                }

                lables_tv_time.setText(item.getReleaseTime());
                lable_details_tv__comment.setText(item.getAnswersNums());
                lable_details_tv_support.setText(item.getDetailSupport());
                homepage_ll_wonderfulcontent_text.setText(item.getDetailLable());
                final List<ForumPostsImage> detailIvUrls = item.getDetailIvUrls();
                final List<Taglibs> detailTaglibs = item.getDetailTaglibs();

                alllableEditorCAdapter =new CommonAdapter<ForumPostsImage>(getActivity(),detailIvUrls,R.layout.pic_layout) {
                    @Override
                    protected void convert(ViewHolder holder, final ForumPostsImage item) {
                        ImageView lable =(ImageView)holder.getView(R.id.lables_top_textview);
                        ImageLoader.getInstance().displayImage(item.getImageUrl(), lable,options);
                    }
                };

                community_mine_lables_gridview.setAdapter(alllableEditorCAdapter);
                community_mine_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(),
                                PhotoActivitys.class);

                        intent.putExtra("URL", (Serializable) detailIvUrls);
                        intent.putExtra("ID",position);
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                if (detailTaglibs != null && detailTaglibs.size() >= 1) {
                    tv_tag4.setText(detailTaglibs.get(0).getTagName());
                }
                tv_tag4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LableDetailsActivity.class);
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE, detailTaglibs.get(0).getTagName());
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ID,detailTaglibs.get(0).getTaglibId() + "");
                        intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, detailTaglibs.get(0).getIsStore()+ "");
                        startActivity(intent);
                    }
                });


            }
        };
        community_detals_listview.setAdapter(lanleCommonAdapter);
        community_detals_listview.setFocusable(false);
        community_detals_listview.setXListViewListener(this);
        community_detals_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), ContentDetailsActivity.class);
                intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 2).getDetailDiscuss() + "");
                intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, lables.get(position - 2).getIsStore() + "");
                intent.putExtra(ContentDetailsActivity.EXTRA_LABLE_ANSWER_CONTENT, lables.get(position - 2).getHeadTvName() + "");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
    }

    private void initViewPager(View view) {

        community_fl_top1 = (FrameLayout) view.findViewById(R.id.community_fl_top1);   //专业内容模块
        community_fl_top2 = (FrameLayout) view.findViewById(R.id.community_fl_top2);    //精心解答模块
        community_fl_top3 = (FrameLayout) view.findViewById(R.id.community_fl_top3);    //精心解答模块
        community_fl_top4 = (FrameLayout) view.findViewById(R.id.community_fl_top4);    //精心解答模块
        community_fl_top1.setOnClickListener(this);
        community_fl_top2.setOnClickListener(this);
        community_fl_top3.setOnClickListener(this);
        community_fl_top4.setOnClickListener(this);
        homepage_iv_banner = (ImageView) view.findViewById(R.id.homepage_iv_banner);
        homepage_iv_banner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WebIntent = new Intent(getActivity(), WebComunicateviewActivivty.class);
                WebIntent.putExtra("bannerUrl",bannerUrl);
                WebIntent.putExtra("bannerName",bannerName);
                startActivity(WebIntent);

            }
        });
       // advPager = (ViewPager) view.findViewById(R.id.adv_pager);
        /*ViewGroup group = (ViewGroup) view.findViewById(R.id.ly_viewGroup);

        //      这里存放的是四张广告背景
        List<View> advPics = new ArrayList<View>();

        ImageView img1 = new ImageView(getActivity());
        //img1.setBackgroundResource(R.drawable.yk_homepage_top_banners);
       ImageLoader.getInstance().displayImage(bannerUrl, img1,options);
        img1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WebIntent = new Intent(getActivity(), WebComunicateviewActivivty.class);
                WebIntent.putExtra("bannerUrl",bannerUrl);
                WebIntent.putExtra("bannerName",bannerName);
                startActivity(WebIntent);

            }
        });
        advPics.add(img1);


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

        }).start();*/
    }


    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(2000000000);
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
        start = 1;
        Api.getHotTiezi(1,allLableContentHandler);
                //顶部banner图
        Api.getBannerContent(bannerHandlers);


    }

    @Override
    public void onLoadMore() {
        start +=1;
        Api.getHotTiezi(start,allLableContentsHandlers);
    }
    private ResponseCallback<List<HpWonderfulContent>> allLableContentsHandlers = new ResponseCallback<List<HpWonderfulContent>>() {

        @Override
        public void onSuccess(List<HpWonderfulContent> data) {

            if(data.size()<10){
                community_detals_listview.setPullLoadEnable(false);
            }else {
                community_detals_listview.setPullLoadEnable(true);
            }
            onLoad();
            hideWaitingUI();
            bannerHp = new ArrayList<>();
            for (HpWonderfulContent hp : data) {
                bannerHp.add(hp);
            }
            geneItems();
            lanleCommonAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(String status, String message) {
            //  LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            //AppContext.showToast(message);
        }
    };
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
            case R.id.community_fl_top3:
                Intent intent = new Intent(getActivity(),
                        LablesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.community_fl_top4:
                Intent intent4 = new Intent(getActivity() ,
                        WonderfulActivityActivity.class);
                intent4.putExtra(WonderfulActivityActivity.MESSAGE_INFOS,"优活动");
                startActivity(intent4);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;

            default:
                break;
        }
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
