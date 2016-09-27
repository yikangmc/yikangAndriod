package com.yikang.app.yikangserver.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.ui.AnswersDetails;
import com.yikang.app.yikangserver.ui.LableDetaileContentActivity;
import com.yikang.app.yikangserver.ui.LoginActivity;
import com.yikang.app.yikangserver.ui.MineInfoActivity;
import com.yikang.app.yikangserver.ui.PublishPostActivity;
import com.yikang.app.yikangserver.ui.RichTextActivity;
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/5/27.
 */
public class ProfessionalContentGoodAnwserFragment extends BaseFragment {
    private TextView content;
    private String text;
    private String textName;
    private int fromCommunityFind;
    private Handler mHandler;
    private int start = 1;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    private LinearLayout community_professional_ll_posting;
    private ImageView community_professional_iv_posting;
    List<LablesBean> secondLables = new ArrayList<>();
    List<HpWonderfulContent> bannerHp = new ArrayList<>();
    private DisplayImageOptions options; //配置图片加载及显示选项
    private DisplayImageOptions option; //配置图片加载及显示选项
    private ProgressBar pb_load;
    public ProfessionalContentGoodAnwserFragment(String text, String textName, int fromCommunityFind) {  //用于初始化的时候传递一个文字显示在textview中便于知道是第几页，把后面看完就懂了
        this.text = text;
        this.textName = textName;
        this.fromCommunityFind = fromCommunityFind;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
               // .displayer(new FadeInBitmapDisplayer(500))
                //.displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                // .displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHandler = new Handler();

        return initView(inflater, container);
    }

    private void getData() {
        start = 1;
        if (fromCommunityFind == 1) {

            //获取标签下的所有专业内容
            Api.getAllLableContents(Integer.parseInt(text),1, allLableContentsHandler);
        } else {

            //获取标签下的所有问答内容
            Api.getAllQuestionContent(Integer.parseInt(text), 1,secondLablesHandler);
        }
    }
    private void getDataMore() {

        if (fromCommunityFind == 1) {
            // pb_load.setVisibility(View.VISIBLE);
            //获取标签下的所有专业内容
            Api.getAllLableContents(Integer.parseInt(text),start, allLableContentsHandlers);
        } else {
            // pb_load.setVisibility(View.VISIBLE);
            //获取标签下的所有问答内容
            Api.getAllQuestionContent(Integer.parseInt(text),start,secondLablesHandlers);
        }
    }
    private ResponseCallback<List<HpWonderfulContent>> allLableContentsHandlers;

    {
        allLableContentsHandlers = new ResponseCallback<List<HpWonderfulContent>>() {


            @Override
            public void onSuccess(List<HpWonderfulContent> data) {
                if(data.size()<10){
                    message_xlistview.setPullLoadEnable(false);
                }else {
                    message_xlistview.setPullLoadEnable(true);
                }
                onLoad();

                bannerHp = new ArrayList<>();

                for (HpWonderfulContent hp : data) {
                    bannerHp.add(hp);
                }
                geneItem(text);
                mMessageAdapter.notifyDataSetChanged();

                //LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

            }



            @Override
            public void onFailure(String status, String message) {
               // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                hideWaitingUI();
                AppContext.showToast(message);
            }
        };
    }


    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<List<HpWonderfulContent>> allLableContentsHandler;

    {
        allLableContentsHandler = new ResponseCallback<List<HpWonderfulContent>>() {


            @Override
            public void onSuccess(List<HpWonderfulContent> data) {
                start=1;
                if(data.size()<10){
                    message_xlistview.setPullLoadEnable(false);
                }else {
                    message_xlistview.setPullLoadEnable(true);
                }
                mMessageAdapter.notifyDataSetChanged();

                bannerHp = new ArrayList<>();
                for (HpWonderfulContent hp : data) {
                    bannerHp.add(hp);
                }
                lables.clear();
                geneItem(text);
                message_xlistview.setAdapter(mMessageAdapter);
                onLoad();
              //  LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

            }

            @Override
            public void onFailure(String status, String message) {
               // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                pb_load.setVisibility(View.GONE);
                AppContext.showToast(message);
            }
        };
    }

    protected void geneItems(String text) {
        for (int i = 0; i < secondLables.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(secondLables.get(i).getUserName() + "");
            lable.setHeadTvLable(secondLables.get(i).getTitle() + "");
            lable.setDetailSupport(secondLables.get(i).getContent() + "");
            lable.setReleaseTime("提问于" + TimeCastUtils.compareDateTime(System.currentTimeMillis(), secondLables.get(i).getCreateTime()));
            lable.setTaglibId(secondLables.get(i).getTaglibId());
            lable.setHeadIvUrl(secondLables.get(i).getPhotoUrl() + "");
            lable.setDetailSupport(secondLables.get(i).getAnswerNum() + "");
            lable.setDetailLable(secondLables.get(i).getQuestionId() + "");
            if (secondLables.get(i).getQuestionImages().size() != 0) {
                lable.setHasPic("yes");
            }
            if (secondLables.get(i).getQuestionImages().size() == 0) {
                lable.setHasPic("no");
            }
            lables.add(lable);
        }


    }

    protected void geneItem(String text) {
        for (int i = 0; i < bannerHp.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(bannerHp.get(i).getUserName() + "");
            lable.setHeadTvLable(bannerHp.get(i).getTitle() + "");
            lable.setTaglibId(bannerHp.get(i).getUserPosition());
//            lable.setReleaseTime("发布于：" + TimeCastUtils.compareDateTime(System.currentTimeMillis(), secondLables.get(i).getCreateTime()));
            // lable.setTaglibId(bannerHp.get(i).getTaglibId());
            lable.setHeadIvUrl(bannerHp.get(i).getRecommendPicUrl() + "");
            lable.setDetailLable(bannerHp.get(i).getForumPostId() + "");
            if ((bannerHp.get(i).getContent() + "").toString().length() >= 100) {
                lable.setDetailSupport(bannerHp.get(i).getContent() + "... 全部");
            } else {
                lable.setDetailSupport(bannerHp.get(i).getContent() + "");
            }

            lables.add(lable);
        }


    }

    private void onLoad() {
        message_xlistview.stopRefresh();
        message_xlistview.stopLoadMore();
        message_xlistview.setRefreshTime("刚刚");
    }


    private ResponseCallback<List<LablesBean>> secondLablesHandlers = new ResponseCallback<List<LablesBean>>() {


        @Override
        public void onSuccess(List<LablesBean> data) {
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            onLoad();
            secondLables = new ArrayList<>();
            for (LablesBean hp : data) {
                secondLables.add(hp);
            }
            geneItems(text);
            mMessageAdapter.notifyDataSetChanged();

            //LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<List<LablesBean>> secondLablesHandler = new ResponseCallback<List<LablesBean>>() {


        @Override
        public void onSuccess(List<LablesBean> data) {
            if(data.size()<10){
                message_xlistview.setPullLoadEnable(false);
            }else {
                message_xlistview.setPullLoadEnable(true);
            }
            start=1;
            mMessageAdapter.notifyDataSetChanged();
            hideWaitingUI();
            secondLables = new ArrayList<>();
            for (LablesBean hp : data) {
                secondLables.add(hp);
            }
            lables.clear();
            geneItems(text);
            message_xlistview.setAdapter(mMessageAdapter);
            onLoad();
          //  LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

        }

        @Override
        public void onFailure(String status, String message) {
            pb_load.setVisibility(View.GONE);
          //  LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
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

        dialog.findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_goto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), MineInfoActivity.class);
                intent.putExtra(MineInfoActivity.EXTRA_USER,  AppContext.getAppContext().getUser());
                startActivity(intent);
            }
        });
    }
    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_content_answer, null);
        message_xlistview = (XListView) view.findViewById(R.id.community_professional_listview);
        pb_load = (ProgressBar) view.findViewById(R.id.pb_load);
        message_xlistview.setPullLoadEnable(false);
        community_professional_ll_posting = (LinearLayout) view.findViewById(R.id.community_professional_ll_posting);
        community_professional_iv_posting = (ImageView) view.findViewById(R.id.community_professional_iv_posting);
        if (fromCommunityFind == 1) {
            community_professional_ll_posting.setVisibility(View.VISIBLE);

        }
        if (fromCommunityFind == 2) {
            community_professional_ll_posting.setVisibility(View.VISIBLE);
        }
        community_professional_iv_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                   // getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    if (fromCommunityFind == 2) {
                        Intent intent1 = new Intent(getActivity(),
                                PublishPostActivity.class);  //发布问题
                        intent1.putExtra(PublishPostActivity.EXTRA_LABLE_EXAMPLEIDS, text);
                        intent1.putExtra(PublishPostActivity.EXTRA_LABLE_EXAMPLENAME, textName);
                        startActivity(intent1);
                        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    } else {
                        if(AppContext.getAppContext().getUser().profession > 0){
                            Intent intent = new Intent(getActivity(), RichTextActivity.class)
                                    .putExtra("role", "add");
                            intent.putExtra("tagLibId", text);
                            intent.putExtra("tagLibName", textName);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                        }else {
                            showConfigDialog();

                        }
                    }
                }

            }

        });
        message_xlistview.setPullLoadEnable(false);
        if (fromCommunityFind == 2) {
            message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), AnswersDetails.class);
                    intent.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER, 1);
                    intent.putExtra(AnswersDetails.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getDetailLable() + "");

                    startActivity(intent);
                }
            });
        }
        if (fromCommunityFind == 1) {
            message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), LableDetaileContentActivity.class);
                    intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                    if (bannerHp != null) {
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT, lables.get(position - 1).getHeadTvName());
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position - 1).getDetailLable() + "");
                    }
                    startActivity(intent);
                }
            });
        }
        if (fromCommunityFind == 1) {//专业文章
            mMessageAdapter = new CommonAdapter<LableDetailsBean>(getActivity(), lables, R.layout.list_professional_content_items) {
                @Override
                protected void convert(ViewHolder holder, LableDetailsBean item) {
                    TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                    TextView lables_tv_content = holder.getView(R.id.lables_tv_content);
                    TextView lables_tv_username = holder.getView(R.id.lables_tv_username);
                    TextView tv_position = holder.getView(R.id.tv_position);
                    ImageView lable_iv_header = holder.getView(R.id.lable_iv_header);
                    lables_tv_title.setText(item.getHeadTvLable() + "");
                    lables_tv_username.setText(item.getHeadTvName() + "");
                    PositionUtils.getPosition(item.getTaglibId(), tv_position);
                    if (item.getHeadTvLable().length() >= 100) {
                        SpannableStringBuilder styles = new SpannableStringBuilder(item.getDetailSupport());
                        styles.setSpan(new BackgroundColorSpan(Color.RED), 2, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置textview的背景颜色
                        styles.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.community_activity_tv_color)), 104, 106, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置文字的颜色
                        lables_tv_content.setText(styles);
                    } else {
                        lables_tv_content.setText(item.getDetailSupport());
                    }

                    ImageLoader.getInstance().displayImage(item.getHeadIvUrl() + "", lable_iv_header, options);
                }
            };
        }
        if (fromCommunityFind == 2) {//问答
            mMessageAdapter = new CommonAdapter<LableDetailsBean>(getActivity(), lables, R.layout.list_answer_content_items) {
                @Override
                protected void convert(ViewHolder holder, LableDetailsBean item) {
                    TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                    ImageView tv_pic = holder.getView(R.id.tv_pic);
                    TextView lables_tv_username = holder.getView(R.id.lables_tv_username);
                    TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                    TextView lables_tv_nums = holder.getView(R.id.lables_tv_nums);
                    CircleImageView lables_iv_userphoto = holder.getView(R.id.lables_iv_userphoto);
                    lables_tv_title.setText(item.getHeadTvLable() + "");
                    lables_tv_username.setText(item.getHeadTvName() + "");
                    lables_tv_time.setText(item.getReleaseTime() + "");
                    lables_tv_nums.setText(item.getDetailSupport() + "");
                    if (item.getHasPic().equals("yes")) {
                        tv_pic.setVisibility(View.VISIBLE);
                    }
                    if (item.getHasPic().equals("no")) {
                        tv_pic.setVisibility(View.GONE);
                    }
                    ImageLoader.getInstance().displayImage(item.getHeadIvUrl() + "", lables_iv_userphoto, option);
                }
            };
        }

        message_xlistview.setAdapter(mMessageAdapter);
        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            private void onLoad() {
                message_xlistview.stopRefresh();
                message_xlistview.stopLoadMore();
                message_xlistview.setRefreshTime("刚刚");
            }

            @Override
            public void onLoadMore() {
                start += 1;
                getDataMore();
            }

            @Override
            public void onScrollMove() {
                if(fromCommunityFind == 2) {
                    community_professional_ll_posting.setVisibility(View.GONE);
                }
                if(fromCommunityFind == 1) {
                   // if (AppContext.getAppContext().getUser().profession > 0) {
                        community_professional_ll_posting.setVisibility(View.GONE);
                   // }
                }

            }

            @Override
            public void onScrollFinish() {
                if(fromCommunityFind == 1) {

                        Animation mShowAction = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_bottom_in);
                        community_professional_ll_posting.startAnimation(mShowAction);
                        community_professional_ll_posting.setVisibility(View.VISIBLE);

                }
                if(fromCommunityFind == 2) {
                    Animation mShowAction = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_bottom_in);
                    community_professional_ll_posting.startAnimation(mShowAction);
                    community_professional_ll_posting.setVisibility(View.VISIBLE);
                }
            }

        });
        return view;
    }
}