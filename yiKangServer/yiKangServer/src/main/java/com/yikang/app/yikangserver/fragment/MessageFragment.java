package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.MessageState;
import com.yikang.app.yikangserver.ui.DynamicMessageActivity;
import com.yikang.app.yikangserver.ui.MessageActivity;
import com.yikang.app.yikangserver.ui.MessageSettingActivity;

/**
 * Created by yudong on 2016/4/20.
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener{

    private TextView message_setting_tv;
    private LinearLayout message_system_rl,message_dynamic_rl;
    private ImageView iv_dynamic,iv_system;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        findView(rootView);

        return rootView;
    }



   @Override
   public void onResume() {
       super.onResume();
       if (AppContext.getAppContext().getAccessTicket() != null) {
           getData();
       }
   }

    private void getData() {
    Api.messageState(new ResponseCallback<MessageState>() {
        @Override
        public void onSuccess(MessageState data) {
            if(data.getDynamicNoReadNum()>0){
                iv_dynamic.setVisibility(View.VISIBLE);
            }else {
                iv_dynamic.setVisibility(View.GONE);
            }
            if(data.getSystemNoReadNum()>0){
                iv_system.setVisibility(View.VISIBLE);
            }else {
                iv_system.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(String status, String message) {

        }
    });
    }

    private void findView(View rootView) {

        iv_dynamic=(ImageView) rootView.findViewById(R.id. iv_dynamic);
        iv_system=(ImageView) rootView.findViewById(R.id. iv_system);
        message_setting_tv=(TextView)rootView.findViewById(R.id.message_setting_tv);
        message_setting_tv.setOnClickListener(this);
        message_dynamic_rl=(LinearLayout)rootView.findViewById(R.id.message_dynamic_rl);
        message_system_rl=(LinearLayout)rootView.findViewById(R.id.message_system_rl);
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
