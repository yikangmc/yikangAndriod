package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.ui.PayActivity;

/**
 * 主页面的我的fragment
 */
public class ActivityDetailFragment extends BaseFragment implements OnClickListener{
    private Button activity_bt_signup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_activity_eventdetail, container, false);
        findview(rootView);
        return rootView;
    }

    private void findview(View rootView) {

        activity_bt_signup=(Button)rootView.findViewById(R.id.activity_bt_signup);
        activity_bt_signup.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.activity_bt_signup:
                Intent intent = new Intent(getActivity(),
                        PayActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
        }
    }
}
