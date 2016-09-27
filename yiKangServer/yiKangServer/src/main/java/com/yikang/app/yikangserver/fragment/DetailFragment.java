package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.bean.QuestionAnswers;
import com.yikang.app.yikangserver.ui.PersonalHomepageActivity;
import com.yikang.app.yikangserver.utils.PositionUtils;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.CircleImageView;

/**
 * Created by yudong on 2016/4/20.
 * 社区关注模块
 */
public class DetailFragment extends BaseFragment implements View.OnClickListener {

    private TextView lables_details_tv_title;
    private CircleImageView lables_details_iv_user;
    private TextView lables_details_tv_zhicheng, tv_time, lables_details_tv_name, lable_details_tv_support, homepage_wonderfulcontent_supportlike;
    private WebView webView;
    private DisplayImageOptions options; //配置图片加载及显示选项

    public DetailFragment(QuestionAnswers lableDetailsBean) {
        this.lableDetailsBean = lableDetailsBean;
    }

    private QuestionAnswers lableDetailsBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        findViews(rootView);
        return rootView;
    }

    private void findViews(View rootView) {

        lables_details_tv_title = (TextView) rootView.findViewById(R.id.lables_details_tv_title);
        lables_details_tv_zhicheng = (TextView) rootView.findViewById(R.id.lables_details_tv_zhicheng);
        tv_time = (TextView) rootView.findViewById(R.id.tv_time);
        lables_details_tv_name = (TextView) rootView.findViewById(R.id.lables_details_tv_name);
       // lable_details_tv_support = (TextView) rootView.findViewById(R.id.lable_details_tv_support);
       // homepage_wonderfulcontent_supportlike = (TextView) rootView.findViewById(R.id.homepage_wonderfulcontent_supportlike);
        lables_details_iv_user = (CircleImageView) rootView.findViewById(R.id.lables_details_iv_user);
        webView = (WebView) rootView.findViewById(R.id.webView);
    }


    private void loadViewData() {
        PositionUtils.getPosition(lableDetailsBean.getUserPosition(), lables_details_tv_zhicheng);
        tv_time.setText("回答于" + TimeCastUtils.compareDateTime(System.currentTimeMillis(), lableDetailsBean.getCreateTime()));

        lables_details_tv_name.setText(lableDetailsBean.getUserName() + "");
         /*
            跳入专业人员个人主页
             */
        lables_details_iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PersonalHomepageActivity.class);
                intent.putExtra("userId", lableDetailsBean.getCreateUserId() + "");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        ImageLoader.getInstance().displayImage(lableDetailsBean.getPhotoUrl() + "", lables_details_iv_user, options);
        webView.getSettings().setJavaScriptEnabled(true);
        //加载HTML字符串进行显示
        webView.loadData("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<head>\n" +
                        "\t<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\" />\n" +
                        "\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                        "\t<style type='text/css'> img { width: 100%; } </style>" +
                        "</head>\n" +
                        "\t<body>\n" + lableDetailsBean.getHtmlDetailContent() + "</body>\n" +
                        "</html>",
                "text/html ;charset=UTF-8", null);
       /* if (questionAnswers.getIsStar() == 0) {
            lable_details_tv_support.setVisibility(View.VISIBLE);
            homepage_wonderfulcontent_supportlike.setVisibility(View.GONE);
            lable_details_tv_support.setText(questionAnswers.getStarNum() + "");
            lable_details_tv_support.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {

                    Api.supportAnswer(Integer.parseInt(contentId), zanHandler);
                }
            });
            homepage_wonderfulcontent_supportlike.setText(questionAnswers.getStarNum() + "");
            homepage_wonderfulcontent_supportlike.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    Api.supportAnswer(Integer.parseInt(contentId), quxiaozanHandler);
                }
            });
        }
        if (questionAnswers.getIsStar() == 1) {
            lable_details_tv_support.setVisibility(View.GONE);
            homepage_wonderfulcontent_supportlike.setVisibility(View.VISIBLE);
            homepage_wonderfulcontent_supportlike.setText(questionAnswers.getStarNum() + "");
            homepage_wonderfulcontent_supportlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Api.supportAnswer(Integer.parseInt(contentId), quxiaozanHandler);
                }
            });
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
