package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.ui.MessageActivity;
import com.yikang.app.yikangserver.ui.DynamicMessageActivity;
import com.yikang.app.yikangserver.ui.MessageSettingActivity;

/**
 * Created by yudong on 2016/4/20.
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener{

    private TextView message_setting_tv;
    private RelativeLayout message_system_rl,message_dynamic_rl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        findView(rootView);
        return rootView;
    }

    private void findView(View rootView) {
        message_setting_tv=(TextView)rootView.findViewById(R.id.message_setting_tv);
        message_setting_tv.setOnClickListener(this);
        message_dynamic_rl=(RelativeLayout)rootView.findViewById(R.id.message_dynamic_rl);
        message_system_rl=(RelativeLayout)rootView.findViewById(R.id.message_system_rl);
        message_dynamic_rl.setOnClickListener(this);
        message_system_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_setting_tv:
                Intent intent = new Intent(getActivity(),
                        MessageSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.message_system_rl:
                Intent intent1 = new Intent(getActivity(),
                        MessageActivity.class);
                intent1.putExtra(MessageActivity.MESSAGE_INFO,"系统消息");//动态消息
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.message_dynamic_rl:
                Intent intent2 = new Intent(getActivity(),
                        DynamicMessageActivity.class);
                intent2.putExtra(DynamicMessageActivity.MESSAGE_INFOS,"动态消息");
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
        }
    }
}
