package com.yikang.app.yikangserver.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.data.MyData;
import com.yikang.app.yikangserver.dialog.QrCodeDialog;
import com.yikang.app.yikangserver.reciever.UserInfoAlteredReceiver;
import com.yikang.app.yikangserver.ui.MineInvitationActivity;
import com.yikang.app.yikangserver.ui.MineQuestionActivity;
import com.yikang.app.yikangserver.ui.MineFocusonActivity;
import com.yikang.app.yikangserver.ui.InviteCustomerListActivity;
import com.yikang.app.yikangserver.ui.MineInfoActivity;
import com.yikang.app.yikangserver.utils.LOG;

/**
 * 主页面的我的fragment
 */
public class MineFragment extends BaseFragment implements OnClickListener {
    protected static final String TAG = "MineFragment";
    private static SparseIntArray defaultAvatar = new SparseIntArray();

    static {
        defaultAvatar.put(MyData.DOCTOR, R.drawable.doctor_default_avatar);
        defaultAvatar.put(MyData.NURSING, R.drawable.nurse_default_avatar);
        defaultAvatar.put(MyData.THERAPIST, R.drawable.therapists_default_avatar);
    }

    private TextView tvName, tvProfession, tvInviteCode, tvCustomerNum;
    private ImageView ivAvatar;
    private LinearLayout mine_ly_invitation, mine_ly_problem, mine_ly_focuson;
    private User user;
    private View tvCheckTip;

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
            LOG.i(TAG, "[loadUserInfo]加载失败" + message);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppContext.getAppContext().getUser();

        //用户信息未保存，就跳转用户信息编辑页面
        if (user.infoStatus != User.INFO_STATUS_COMPLETE) {
//            showEditPage();
        }
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
        if (receiver.getAndConsume()) {
            Api.getUserInfo(loadUserInfoHandler);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        user = AppContext.getAppContext().getUser();
        fillToViews(user);
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
        tvProfession = (TextView) view.findViewById(R.id.tv_mine_profession);
        tvInviteCode = (TextView) view.findViewById(R.id.tv_mine_invite_code);
        tvCustomerNum = (TextView) view.findViewById(R.id.tv_mine_customer_number);
        tvCheckTip = view.findViewById(R.id.tv_mine_profession_in_check);
        /*
        我的帖子
         */
        mine_ly_invitation = (LinearLayout) view.findViewById(R.id.mine_ly_invitation);

        /*
        我的问题
         */
        mine_ly_problem = (LinearLayout) view.findViewById(R.id.mine_ly_problem);

        /*
        我的关注
         */
        mine_ly_focuson = (LinearLayout) view.findViewById(R.id.mine_ly_focuson);

        LinearLayout lyBasicInfo = ((LinearLayout) view.findViewById(R.id.ly_mine_basic_info));
        LinearLayout lyQrCode = (LinearLayout) view.findViewById(R.id.ly_mine_qr_code);
        View lyCustomNumber = view.findViewById(R.id.ly_mine_customer_number);

        lyQrCode.setOnClickListener(this);
        lyBasicInfo.setOnClickListener(this);
        lyCustomNumber.setOnClickListener(this);
        mine_ly_invitation.setOnClickListener(this);
        mine_ly_problem.setOnClickListener(this);
        mine_ly_focuson.setOnClickListener(this);


//        fillToViews();
    }


    /**
     * 将map中的信息填写到Views中
     */
    private void fillToViews(User user) {
        tvName.setText(user.name);//名字
        tvInviteCode.setText(user.inviteCode); //邀请码
        int visible = user.professionCheckStatus == User.CHECK_STATUS_CHECKING ? View.VISIBLE : View.GONE;
        tvCheckTip.setVisibility(visible);

        if (user.profession >= 0) { //职业
            String profession = MyData.professionMap.valueAt(user.profession);
            tvProfession.setText(profession);
        }

        if (!TextUtils.isEmpty(user.avatarImg)) { // 显示头像
            ImageLoader.getInstance().displayImage(user.avatarImg, ivAvatar);
        } else {
            ivAvatar.setImageResource(defaultAvatar.get(user.profession));
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
                showEditPage();
                break;
            case R.id.ly_mine_qr_code:
                showQrCodeDialog();
                break;
            case R.id.ly_mine_customer_number:
                toCustomerListPage();
                break;
            case R.id.mine_ly_invitation:
                Intent intent1 = new Intent(getActivity(),
                        MineInvitationActivity.class);
                intent1.putExtra(MineInvitationActivity.MESSAGE_INFOS,"我的帖子");
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.mine_ly_problem:
                Intent intent2 = new Intent(getActivity(),
                        MineQuestionActivity.class);
                intent2.putExtra(MineQuestionActivity.MESSAGE_INFOS,"我的问题");
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.mine_ly_focuson:
                Intent intent3 = new Intent(getActivity(),
                        MineFocusonActivity.class);
                intent3.putExtra(MineFocusonActivity.MESSAGE_INFOS,"我的关注");
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
        Dialog qrCodeDialog = new QrCodeDialog(getActivity(), user.invitationUrl);
        qrCodeDialog.show();
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
