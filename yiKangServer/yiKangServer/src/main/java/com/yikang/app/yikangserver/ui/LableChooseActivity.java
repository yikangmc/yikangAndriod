package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.Expert;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;
import com.yikang.app.yikangserver.view.tag.FlowLayout;
import com.yikang.app.yikangserver.view.tag.TagAdapter;
import com.yikang.app.yikangserver.view.tag.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LableChooseActivity extends BaseActivity implements OnClickListener {

    public static final String ARG_PROFESSION = "profession";
    public static final String LABLE_CHOOSE= "chooseresult";
    private List<Expert> mData = new ArrayList<>();
    private TagFlowLayout mTflTags;
    private ArrayList<Expert> checkList=new ArrayList<>();
    private int position;

    private TextView lable_choose_save,lable_choose_cancle;
    private TagAdapter<Expert> mAdapter = new TagAdapter<Expert>(mData) {
        @Override
        public View getView(FlowLayout parent, int position, Expert expert) {
            LayoutInflater tagInflater = LayoutInflater.from(LableChooseActivity.this);
            TextView tag = ((TextView) tagInflater.inflate(R.layout.item_chk_tag, parent, false));
            tag.setText(mData.get(position).name);
            return tag;
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        position=getIntent().getIntExtra("position",0);
        initContent();
        initTitleBar("选择标签");
	}


	@Override
	protected void findViews() {
        lable_choose_save=(TextView)findViewById(R.id.lable_choose_save);
        lable_choose_cancle=(TextView)findViewById(R.id.lable_choose_cancle);
        lable_choose_save.setTextColor(getResources().getColor(R.color.community_activity_tv_color));
        mTflTags = (TagFlowLayout) findViewById(R.id.id_tfl_expert);
        mTflTags.setMaxSelectCount(4);
        mTflTags.setAdapter(mAdapter);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_lable_choose);
	}

	@Override
	protected void getData() {
        showWaitingUI();
       // LOG.i("debug","profession---->"+position);
        Api.getExperts(position, new ResponseCallback<List<Expert>>() {


            @Override
            public void onSuccess(List<Expert> data) {
                hideWaitingUI();
               // LOG.i("debug","data---->"+data);
                mData.clear();
                mData.addAll(data);
                mAdapter.notifyDataChanged();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked) {
                        mAdapter.setSelectedList(i);
                    }
                }
            }

            @Override
            public void onFailure(String status, String message) {
                hideWaitingUI();
                AppContext.showToast(message);
            }
        });
    }

    private boolean check() {
        Set<Integer> selectedList = mTflTags.getSelectedList();
        if(selectedList.isEmpty()){
            AppContext.showToast("请至少选择一个");
            return false;
        }
        return true;
    }


    private ResponseCallback<Void> alterHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            AppContext.showToast("修改成功");
            hideWaitingUI();
//            finishWithResult();
//            //发送已经修改广播
//            notifyUserInfoChanged();
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    /**
     * 带着信息返回上一页
     */
    private void finishWithResult(){
         checkList = new ArrayList<>();
        for (int index:  mTflTags.getSelectedList()) {
            checkList.add(mData.get(index));
        }
        showWaitingUI();
        Api.alterExpert(checkList,alterNameHandler);
        //LOG.i("debug","checkList---->"+checkList);

    }
    private ResponseCallback<Void> alterNameHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            Intent intent = new Intent();
            intent.putExtra("goodat", checkList.get(0).getName());
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("资料修改失败：" + message);

        }
    };


    @Override
	protected void initViewContent() {
        lable_choose_cancle.setOnClickListener(this);
        lable_choose_save.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.lable_choose_save:

            Set<Integer> selectedList = mTflTags.getSelectedList();
            if(selectedList.isEmpty()){
                AppContext.showToast("请至少选择一个");
                return;
            }

            finishWithResult();
			break;
		case R.id.lable_choose_cancle:
            finish();
			break;
		case R.id.tv_login_forget:
		default:
			break;
		}
	}
	


}
