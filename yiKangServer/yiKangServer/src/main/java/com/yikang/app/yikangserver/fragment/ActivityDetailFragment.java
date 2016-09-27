package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.WonderfulActivity;
import com.yikang.app.yikangserver.ui.ActivitySuccessful;
import com.yikang.app.yikangserver.ui.LoginActivity;
import com.yikang.app.yikangserver.ui.PersonalHomepageActivity;
import com.yikang.app.yikangserver.view.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 活动详情
 */
public class ActivityDetailFragment extends BaseFragment implements OnClickListener{

    private Button activity_bt_signup,activity_bt_signup_already;

    private TextView tv_activity_title;

    private TextView tv_activity_cost;

    private TextView tv_activity_type;

    private TextView tv_detail;

    private ImageView iv_foucson;

    private ImageView iv_hasfoucson;

    private ImageView iV_activity_state;

    private TextView tv_activity_join_nums;

    private CircleImageView iv_activity_user_photo;

    private TextView tv_activity_user_name;

    private TextView tv_activity_user_publish_nums;

    private TextView tv_activity_start_time;

    private WonderfulActivity wonderfulActivity;

    private CircleImageView homepage_support_iv1;//内容图片

    private CircleImageView homepage_support_iv2;//支持图片

    private CircleImageView homepage_support_iv3;

    private CircleImageView homepage_support_iv4;

    private CircleImageView homepage_support_iv5;

    private CircleImageView homepage_support_iv6;

    private CircleImageView homepage_support_iv7;

    private TextView tv_more;//更多头像

    private DisplayImageOptions options; //配置图片加载及显示选项

    private WebView webView;

    public ActivityDetailFragment(WonderfulActivity wonderfulActivity) {
        this.wonderfulActivity = wonderfulActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new FadeInBitmapDisplayer(500))
                //.displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_activity_eventdetail, container, false);
        findview(rootView);
        return rootView;
    }

    private void findview(View rootView) {
        homepage_support_iv1 = (CircleImageView) rootView.findViewById(R.id.homepage_support_iv1);
        homepage_support_iv2 = (CircleImageView) rootView.findViewById(R.id.homepage_support_iv2);
        homepage_support_iv3 = (CircleImageView) rootView.findViewById(R.id.homepage_support_iv3);
        homepage_support_iv4 = (CircleImageView) rootView.findViewById(R.id.homepage_support_iv4);
        homepage_support_iv5 = (CircleImageView) rootView.findViewById(R.id.homepage_support_iv5);
        homepage_support_iv6 = (CircleImageView) rootView.findViewById(R.id.homepage_support_iv6);
        homepage_support_iv7 = (CircleImageView) rootView.findViewById(R.id.homepage_support_iv7);
        tv_more = (TextView) rootView.findViewById(R.id.tv_more);
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        activity_bt_signup=(Button)rootView.findViewById(R.id.activity_bt_signup);
        activity_bt_signup_already=(Button)rootView.findViewById(R.id.activity_bt_signup_already);
        tv_activity_title=(TextView) rootView.findViewById(R.id.tv_activity_title);
        tv_activity_cost=(TextView) rootView.findViewById(R.id.tv_activity_cost);
        tv_activity_type=(TextView) rootView.findViewById(R.id.tv_activity_type);
        tv_detail=(TextView) rootView.findViewById(R.id.tv_detail);
        tv_activity_join_nums=(TextView) rootView.findViewById(R.id.tv_activity_join_nums);
        tv_activity_user_publish_nums=(TextView) rootView.findViewById(R.id.tv_activity_user_publish_nums);
        tv_activity_user_name=(TextView) rootView.findViewById(R.id.tv_activity_user_name);
        iv_activity_user_photo= (CircleImageView) rootView.findViewById(R.id.iv_activity_user_photo);
        tv_activity_start_time= (TextView) rootView.findViewById(R.id.tv_activity_start_time);
        iV_activity_state= (ImageView) rootView.findViewById(R.id.iV_activity_state);
        iv_foucson= (ImageView) rootView.findViewById(R.id.iv_foucson);
        iv_hasfoucson= (ImageView) rootView.findViewById(R.id.iv_hasfoucson);
        iv_foucson.setOnClickListener(this);
        activity_bt_signup.setOnClickListener(this);
        tv_detail.setText(wonderfulActivity.getContent()+"");
        if(wonderfulActivity.getContent().contains("<p")){
            tv_detail.setVisibility(View.GONE);
            //加载HTML字符串进行显示
            webView.loadData("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                            "<head>\n" +
                            "\t<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\" />\n" +
                            "\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                            "\t<style type='text/css'> img { width: 100%; } </style>"+
                            "</head>\n" +
                            "\t<body>\n"+wonderfulActivity.getContent()+"</body>\n" +
                            "</html>",
                    "text/html ;charset=UTF-8", null);

        }

        tv_activity_title.setText(wonderfulActivity.getTitle());
        tv_activity_user_publish_nums.setText("发起活动"+wonderfulActivity.getCreateActivetyNumber()+"场");
        if(wonderfulActivity.getIsFollow()==0){
            iv_hasfoucson.setVisibility(View.GONE);
            iv_foucson.setVisibility(View.VISIBLE);
        }
        if(wonderfulActivity.getIsFollow()==1){
            iv_hasfoucson.setVisibility(View.VISIBLE);
            iv_foucson.setVisibility(View.GONE);
        }
        if(!(wonderfulActivity.getCost()+"").equals("")) {
            tv_activity_cost.setText("￥" + wonderfulActivity.getCost() + "");
        }
        if((wonderfulActivity.getCost()+"").equals("0.0")){
            tv_activity_cost.setText("免费");
            tv_activity_cost.setTextColor(getActivity().getResources().getColor(R.color.community_activity_free_color));
        }

        tv_activity_join_nums.setText(wonderfulActivity.getPartakeNums()+"");

        tv_activity_user_name.setText(wonderfulActivity.getCreateUsername());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));  //加上这行代码就哦了
        tv_activity_start_time.setText(sdf.format(wonderfulActivity.getStartTime()));
        //LOG.i("debug", "ActivityTime---->" + wonderfulActivity.getStartTime()+"");

        if((wonderfulActivity.getActivetyMode()+"").equals("0")){
            tv_activity_type.setText(wonderfulActivity.getDetailAddress());
        }
        if((wonderfulActivity.getActivetyMode()+"").equals("1")){
            tv_activity_type.setText("线上活动");
        }
        if((wonderfulActivity.getActivetyStatus()+"").equals("4")){
            iV_activity_state.setVisibility(View.VISIBLE);
        }
        if(!(wonderfulActivity.getActivetyStatus()+"").equals("4")){
            iV_activity_state.setVisibility(View.GONE);
        }
        if((wonderfulActivity.getIsPartake()+"").equals("0")){
            activity_bt_signup_already.setVisibility(View.GONE);
                    activity_bt_signup.setVisibility(View.VISIBLE);
        }
        if((wonderfulActivity.getIsPartake()+"").equals("1")){
            activity_bt_signup_already.setVisibility(View.VISIBLE);
            activity_bt_signup.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(wonderfulActivity.getPhotoUrl(),iv_activity_user_photo,options);
         /*
            跳入专业人员个人主页
             */
        iv_activity_user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PersonalHomepageActivity.class);
                intent.putExtra("userId",wonderfulActivity.getCreateUserId()+"");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
        if(wonderfulActivity.getFollowUsers()!=null) {
            if(wonderfulActivity.getFollowUsers().size()==1){
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(0).getPhotoUrl(), homepage_support_iv1,options);
                tv_more.setVisibility(View.INVISIBLE);
            }
            if(wonderfulActivity.getFollowUsers().size()==2){
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(0).getPhotoUrl(), homepage_support_iv1,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(1).getPhotoUrl(), homepage_support_iv2,options);
                tv_more.setVisibility(View.INVISIBLE);
            }
            if(wonderfulActivity.getFollowUsers().size()==3){
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(0).getPhotoUrl(), homepage_support_iv1,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(1).getPhotoUrl(), homepage_support_iv2,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(2).getPhotoUrl(), homepage_support_iv3,options);
                tv_more.setVisibility(View.INVISIBLE);
            }
            if(wonderfulActivity.getFollowUsers().size()==4){
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(0).getPhotoUrl(), homepage_support_iv1,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(1).getPhotoUrl(), homepage_support_iv2,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(2).getPhotoUrl(), homepage_support_iv3,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(3).getPhotoUrl(), homepage_support_iv4,options);
                tv_more.setVisibility(View.INVISIBLE);
            }
            if(wonderfulActivity.getFollowUsers().size()==5){
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(0).getPhotoUrl(), homepage_support_iv1,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(1).getPhotoUrl(), homepage_support_iv2,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(2).getPhotoUrl(), homepage_support_iv3,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(3).getPhotoUrl(), homepage_support_iv4,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(4).getPhotoUrl(), homepage_support_iv5,options);
                tv_more.setVisibility(View.INVISIBLE);
            }
            if(wonderfulActivity.getFollowUsers().size()==6){
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(0).getPhotoUrl(), homepage_support_iv1,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(1).getPhotoUrl(), homepage_support_iv2,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(2).getPhotoUrl(), homepage_support_iv3,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(3).getPhotoUrl(), homepage_support_iv4,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(4).getPhotoUrl(), homepage_support_iv5,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(5).getPhotoUrl(), homepage_support_iv6,options);
                tv_more.setVisibility(View.INVISIBLE);

            }
            if(wonderfulActivity.getFollowUsers().size()>=7){
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(0).getPhotoUrl(), homepage_support_iv1,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(1).getPhotoUrl(), homepage_support_iv2,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(2).getPhotoUrl(), homepage_support_iv3,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(3).getPhotoUrl(), homepage_support_iv4,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(4).getPhotoUrl(), homepage_support_iv5,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(5).getPhotoUrl(), homepage_support_iv6,options);
                ImageLoader.getInstance().displayImage(wonderfulActivity.getFollowUsers().get(6).getPhotoUrl(), homepage_support_iv7,options);
                tv_more.setVisibility(View.VISIBLE);

            }


        }
    }

    private ResponseCallback<String> getGuanzhuHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
            hideWaitingUI();
           // LOG.i("debug", "HpWonderfulContent---->" + data);
            iv_hasfoucson.setVisibility(View.VISIBLE);
            iv_foucson.setVisibility(View.GONE);


        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    /**
     * 参加活动的回调
     */
    private ResponseCallback<Void > joinActivityHandler = new ResponseCallback<Void>() {

        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            Intent intent = new Intent(getActivity(),
                    ActivitySuccessful.class);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            AppContext.showToast( message);
            /*Intent intent = new Intent(getActivity(),
                    ActivitySuccessful.class);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
*/
        }
    };
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.activity_bt_signup:
               /* Intent intent = new Intent(getActivity(),
                        PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("users", wonderfulActivity);
                intent.putExtras(bundle);
                //intent.putExtra("ACTIVITYID",wonderfulActivity.getActivetyId()+"");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);*/
                /*Intent intent = new Intent(getActivity(),
                        ActivitySuccessful.class);
                //intent.putExtra("ACTIVITYID",wonderfulActivity.getActivetyId()+"");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);*/
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    showWaitingUI();
                    Api.joinActivity(Integer.parseInt(wonderfulActivity.getActivetyId() + ""), joinActivityHandler);
                }
                break;


            case R.id.iv_foucson:
                if (AppContext.getAppContext().getAccessTicket() == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Api.addMyFocusPerson(Integer.parseInt(wonderfulActivity.getCreateUserId() + ""), getGuanzhuHandler);
                }
                break;
        }

    }

}
