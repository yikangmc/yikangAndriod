package com.yikang.app.yikangserver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.bean.UserInfo;

/**
 * 主页面的我的fragment
 */
public class MineInfoFragment extends BaseFragment  {
    private UserInfo users;
    private LinearLayout ll_nomal;
    private TextView tag,tag_first,tag_second,tag_third,tag_fourth;
    private TextView tv_sex,tv_address,tv_introduce;
    public MineInfoFragment(UserInfo user){
        this.users=user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine_info, container, false);
        findView(rootView);
        return rootView;
    }

    private void findView(View rootView) {

        ll_nomal= (LinearLayout) rootView.findViewById(R.id.ll_nomal);
        if(users.getProfession()==0){
            ll_nomal.setVisibility(View.GONE);
        }
        tv_sex= (TextView) rootView.findViewById(R.id.tv_sex);
        tv_address= (TextView) rootView.findViewById(R.id.tv_address);
        tv_introduce= (TextView) rootView.findViewById(R.id.tv_introduce);
        tag=(TextView) rootView.findViewById(R.id.tag);
        tag_first=(TextView) rootView.findViewById(R.id.tag_first);
        tag_second=(TextView) rootView.findViewById(R.id.tag_second);
        tag_third=(TextView) rootView.findViewById(R.id.tag_third);
        tag_fourth=(TextView) rootView.findViewById(R.id.tag_fourth);
        if(users.getUserSex().equals("0")) {
            tv_sex.setText("暂无");
        }
        if(users.getUserSex().equals("1")) {
            tv_sex.setText("男");
        }
        if(users.getUserSex().equals("2")) {
            tv_sex.setText("女");
        }
        if(users.getAddressDetail().equals("")){
            tv_address.setText("暂无");
        }else {
            tv_address.setText(users.getAddressDetail());
        }

        if(users.getUserIntroduce().equals("")) {
            tv_introduce.setText("暂无");
        }else {
            tv_introduce.setText(users.getUserIntroduce());
        }
        if(users.getSpecial()!=null&&users.getSpecial().size()==0){
            tag_first.setText("暂无");
        }
        if(users.getSpecial()!=null&&users.getSpecial().size()==1){
            tag_first.setText(users.getSpecial().get(0).getName());
            tag_first.setBackgroundResource(R.color.main_background_colors);
        }
        if(users.getSpecial()!=null&&users.getSpecial().size()==2){
            tag_first.setText(users.getSpecial().get(0).getName());
            tag_second.setText(users.getSpecial().get(1).getName());
            tag_first.setBackgroundResource(R.color.main_background_colors);
            tag_second.setBackgroundResource(R.color.main_background_colors);
        }
        if(users.getSpecial()!=null&&users.getSpecial().size()==3){
            tag_first.setText(users.getSpecial().get(0).getName());
            tag_second.setText(users.getSpecial().get(1).getName());
            tag_third.setText(users.getSpecial().get(2).getName());
            tag_first.setBackgroundResource(R.color.main_background_colors);
            tag_second.setBackgroundResource(R.color.main_background_colors);
            tag_third.setBackgroundResource(R.color.main_background_colors);
        }
        if(users.getSpecial()!=null&&users.getSpecial().size()==4){
            tag_first.setText(users.getSpecial().get(0).getName());
            tag_second.setText(users.getSpecial().get(1).getName());
            tag_third.setText(users.getSpecial().get(2).getName());
            tag_fourth.setText(users.getSpecial().get(3).getName());
            tag_first.setBackgroundResource(R.color.main_background_colors);
            tag_second.setBackgroundResource(R.color.main_background_colors);
            tag_third.setBackgroundResource(R.color.main_background_colors);
            tag_fourth.setBackgroundResource(R.color.main_background_colors);
        }
    }

}
