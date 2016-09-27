package com.yikang.app.yikangserver.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.data.MyData;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;
import com.yikang.app.yikangserver.ui.BasicInfoActivity;
import com.yikang.app.yikangserver.ui.InviteCustomerActivivty;
import com.yikang.app.yikangserver.ui.InviteCustomerListActivity;
import com.yikang.app.yikangserver.ui.KangfushiYiyuanBasicInfoActivity;
import com.yikang.app.yikangserver.ui.MineActivityActivity;
import com.yikang.app.yikangserver.ui.MineFocusonActivity;
import com.yikang.app.yikangserver.ui.MineInfoActivity;
import com.yikang.app.yikangserver.ui.MineInvitationActivity;
import com.yikang.app.yikangserver.ui.MineProfessionalActivity;
import com.yikang.app.yikangserver.ui.MineQuestionActivity;
import com.yikang.app.yikangserver.ui.MyFundActivity;

/**
 * 主页面的我的fragment
 */
public class MineFragment extends BaseFragment implements OnClickListener {
    protected static final String TAG = "MineFragment";
    private static SparseIntArray defaultAvatar = new SparseIntArray();

    static {
        defaultAvatar.put(MyData.DOCTOR, R.drawable.default_pic);
        defaultAvatar.put(MyData.NURSING, R.drawable.default_pic);
        defaultAvatar.put(MyData.THERAPIST, R.drawable.default_pic);
    }

    private TextView tv_mine_profession_in_check, tvName, tvProfession, tvInviteCode, tvCustomerNum;
    private ImageView ivAvatar;
    private LinearLayout mine_ly_fund, ly_invite_code, ll_linezhuanye, mine_ly_professional, mine_ly_authentication, mine_ly_invitation, mine_ly_problem, mine_ly_focuson, mine_ly_activities;
    private User user;
    private View tvCheckTip;
    private DisplayImageOptions option; //配置图片加载及显示选项
    private UserInfoAlteredReceiver receiver;

    /**
     * 加载用户信信息回调
     */
    private ResponseCallback<User> loadUserInfoHandler = new ResponseCallback<User>() {
        @Override
        public void onSuccess(User user) {
            hideWaitingUI();

            if (user != null) {
                AppContext.getAppContext().login(user);

            }
            fillToViews(user);

        }

        @Override
        public void onFailure(String status, String message) {
            // LOG.i(TAG, "[loadUserInfo]加载失败" + message);
            hideWaitingUI();
          //  AppContext.showToast(message);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppContext.getAppContext().getUser();

        //用户信息未保存，就跳转用户信息编辑页面
       /* if (user.infoStatus != User.INFO_STATUS_COMPLETE) {
           showEditPage();
        }*/
        option = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                //.displayer(new FadeInBitmapDisplayer(500))
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED);
        receiver = new UserInfoAlteredReceiver();
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //当用户信息发生改变时，重新加载页面
        if (AppContext.getAppContext().getAccessTicket() != null) {
            if (receiver.getAndConsume()) {
                Api.getUserInfo(loadUserInfoHandler);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if (user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        findViews(rootView);
        return rootView;
    }

    private void findViews(View view) {
        ivAvatar = (ImageView) view.findViewById(R.id.iv_mine_avatar);
        tvName = (TextView) view.findViewById(R.id.tv_mine_name);
        tv_mine_profession_in_check = (TextView) view.findViewById(R.id.tv_mine_profession_in_check);
        tvProfession = (TextView) view.findViewById(R.id.tv_mine_profession);
        tvInviteCode = (TextView) view.findViewById(R.id.tv_mine_invite_code);
        tvCustomerNum = (TextView) view.findViewById(R.id.tv_mine_customer_number);
        tvCheckTip = view.findViewById(R.id.tv_mine_profession_in_check);
        /*
        我的帖子
         */
        mine_ly_invitation = (LinearLayout) view.findViewById(R.id.mine_ly_invitation);

        /**
         * 我的专业内容
         */
        mine_ly_professional = (LinearLayout) view.findViewById(R.id.mine_ly_professional);

        ll_linezhuanye = (LinearLayout) view.findViewById(R.id.ll_linezhuanye);

        /*
        我的问题
         */
        mine_ly_problem = (LinearLayout) view.findViewById(R.id.mine_ly_problem);

        /*
        我的关注
         */
        mine_ly_focuson = (LinearLayout) view.findViewById(R.id.mine_ly_focuson);

        /*
        参与活动
         */
        mine_ly_activities = (LinearLayout) view.findViewById(R.id.mine_ly_activities);
        /*
        身份认证
         */
        mine_ly_authentication = (LinearLayout) view.findViewById(R.id.mine_ly_authentication);

        /**
         * 邀请好友
         */
        ly_invite_code = (LinearLayout) view.findViewById(R.id.ly_invite_code);

        /*
        我的佳佳基金
         */
        mine_ly_fund = (LinearLayout) view.findViewById(R.id.mine_ly_fund);

        LinearLayout lyBasicInfo = ((LinearLayout) view.findViewById(R.id.ly_mine_basic_info));
        LinearLayout lyQrCode = (LinearLayout) view.findViewById(R.id.ly_mine_qr_code);
        View lyCustomNumber = view.findViewById(R.id.ly_mine_customer_number);

        lyQrCode.setOnClickListener(this);
        lyBasicInfo.setOnClickListener(this);
        lyCustomNumber.setOnClickListener(this);
        mine_ly_invitation.setOnClickListener(this);
        mine_ly_professional.setOnClickListener(this);
        mine_ly_problem.setOnClickListener(this);
        mine_ly_focuson.setOnClickListener(this);
        mine_ly_activities.setOnClickListener(this);
        mine_ly_authentication.setOnClickListener(this);
        ly_invite_code.setOnClickListener(this);
        mine_ly_fund.setOnClickListener(this);
    if(user!=null) {
    fillToViews(user);
    }

    }


    /**
     * 将map中的信息填写到Views中
     */
    private void fillToViews(User user) {
        if (user.name != "") {
            tvName.setText(user.name);//名字
        }
        if (user.name.equals("")) {
            tvName.setText("请设置昵称");//名字
        }

        if (user.professionCheckStatus == 0) {
            tv_mine_profession_in_check.setVisibility(View.GONE);

            mine_ly_authentication.setVisibility(View.VISIBLE);


        }
        if (user.professionCheckStatus == 1) {
            tv_mine_profession_in_check.setVisibility(View.VISIBLE);
            mine_ly_authentication.setVisibility(View.GONE);
        }
        if (user.professionCheckStatus == 2) {
            tv_mine_profession_in_check.setVisibility(View.GONE);
            mine_ly_authentication.setVisibility(View.GONE);

        }
        if (user.professionCheckStatus == 3) {
            tv_mine_profession_in_check.setVisibility(View.GONE);
            mine_ly_authentication.setVisibility(View.VISIBLE);
        }
        if (user.profession >= 0) { //职业
            String profession = MyData.professionMap.valueAt(user.profession);
            if (user.profession == 1) {
                tvProfession.setText("康复师");
                mine_ly_authentication.setVisibility(View.GONE);
                mine_ly_professional.setVisibility(View.VISIBLE);
            }
            if (user.profession == 0) {
                mine_ly_professional.setVisibility(View.GONE);
                ll_linezhuanye.setVisibility(View.GONE);
                //mine_ly_authentication.setVisibility(View.VISIBLE);

                if (user.designationName != null) {
                    if (user.designationName.equals("")) {
                        tvProfession.setText("未设置番号");
                    }else {
                        tvProfession.setText(user.designationName);
                    }
                }

            }

            if (user.profession == 5) {
                tvProfession.setText("医院科室/主体");
                mine_ly_authentication.setVisibility(View.GONE);
                mine_ly_professional.setVisibility(View.VISIBLE);
            }
            if (user.profession == 2) {
                tvProfession.setText("中医师");
                mine_ly_authentication.setVisibility(View.GONE);
            }
            if (user.profession == 3) {
                tvProfession.setText("护士");
                mine_ly_authentication.setVisibility(View.GONE);
                mine_ly_professional.setVisibility(View.VISIBLE);
            }
            if (user.profession == 4) {
                tvProfession.setText("企业主体");
                mine_ly_authentication.setVisibility(View.GONE);
                mine_ly_professional.setVisibility(View.VISIBLE);
            }

        }


        if (!TextUtils.isEmpty(user.avatarImg)) { // 显示头像
            ImageLoader.getInstance().displayImage(user.avatarImg, ivAvatar,option);
        } else {
            ivAvatar.setImageResource(R.drawable.default_pic);
        }

        //患者人数
        final String customNumStr = String.format(
                getString(R.string.mine_format_customer_nums), user.paintsNums);
        tvCustomerNum.setText(customNumStr);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_mine_basic_info:
                if (AppContext.getAppContext().getUser().getProfession()== 0) {
                    Intent intentBasicInfo = new Intent(getActivity(),
                            BasicInfoActivity.class);
                    startActivity(intentBasicInfo);
                    getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                } else {
                    Intent intentBasicInfo = new Intent(getActivity(),
                            KangfushiYiyuanBasicInfoActivity.class);
                    startActivity(intentBasicInfo);
                    getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
                break;
            case R.id.ly_mine_qr_code:
                showQrCodeDialog();
                break;
            case R.id.ly_invite_code:
                //AppContext.showToast("邀请好友");
                Intent WebIntent = new Intent(getActivity(), InviteCustomerActivivty.class);
                WebIntent.putExtra("bannerUrl", "http://www.jjkangfu.cn/appPage/invitation2");
                //WebIntent.putExtra("bannerName",bannerName);
                startActivity(WebIntent);
                break;
            case R.id.ly_mine_customer_number:
                toCustomerListPage();
                break;
            case R.id.mine_ly_fund:
                Intent fund_intent = new Intent(getActivity(),
                        MyFundActivity.class);
                startActivity(fund_intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.mine_ly_activities:
                Intent intent4 = new Intent(getActivity(),
                        MineActivityActivity.class);
                intent4.putExtra(MineActivityActivity.MESSAGE_INFOS, "我的活动");
                startActivity(intent4);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.mine_ly_authentication:
                showEditPage();
                /*if(user.jobType==0) {

                }
                if(user.jobType!=0){
                    Toast.makeText(getActivity(),"您已申请过认证",Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.mine_ly_invitation:
                Intent intent1 = new Intent(getActivity(),
                        MineInvitationActivity.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.mine_ly_professional:
                Intent intent5 = new Intent(getActivity(),
                        MineProfessionalActivity.class);
                intent5.putExtra("MineProfessional", "我的问题");
                startActivity(intent5);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.mine_ly_problem:
                Intent intent2 = new Intent(getActivity(),
                        MineQuestionActivity.class);
                intent2.putExtra(MineQuestionActivity.MESSAGE_INFOS, "我的问题");
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.mine_ly_focuson:
                Intent intent3 = new Intent(getActivity(),
                        MineFocusonActivity.class);
                intent3.putExtra(MineFocusonActivity.MESSAGE_INFOS, "我的关注");
                startActivity(intent3);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到患者列表页面
     */
    private void toCustomerListPage() {
        Intent intent = new Intent(getActivity(),
                InviteCustomerListActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }


    /**
     * 显示大的二维码dialog
     */
    private void showQrCodeDialog() {

    }


    /**
     * 显示编辑page
     */
    private void showEditPage() {
        Intent intent = new Intent(getActivity(), MineInfoActivity.class);
        intent.putExtra(MineInfoActivity.EXTRA_USER, user);

        startActivity(intent);
    }


}
