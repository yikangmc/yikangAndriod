package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;

public class InstitutionActivity extends BaseActivity implements View.OnClickListener {
    private TextView mine_tv_sex;
    private TextView mine_tv_introduce;
    private TextView mine_tv_address;
    private TextView mine_tv_goodat;
    private TextView mine_tv_lce;
    private TextView mine_tv_jigouname;
    private TextView mine_tv_jigouaddress;
    private TextView mine_tv_jigouintroduce;
    private TextView tv_title_right_text;
    private LinearLayout mine_ll_position;
    private LinearLayout mine_ll_sex;
    private LinearLayout mine_ll_introduce;
    private LinearLayout mine_ll_address;
    private LinearLayout mine_ll_goodat;
    private LinearLayout mine_ll_lce;
    private LinearLayout mine_ll_jigouname;
    private LinearLayout mine_ll_jigouaddress;
    private LinearLayout mine_ll_jigouintroduce;
    private Button bt_profession_done;
    private User user;
    private Bitmap drawable = null;
    private String photoUrl;
    private String profession;
    private String userCertificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppContext.getAppContext().getUser();
        profession=getIntent().getStringExtra("InfoName");

        initContent();
        if(profession.equals("3")) {
            initTitleBar("机构主体认证");
        }
        if(profession.equals("4")){
            initTitleBar("医院/科室主体认证");
        }
    }

    @Override
    protected void findViews() {

        mine_tv_sex = (TextView) findViewById(R.id.mine_tv_sex);
        mine_tv_jigouname = (TextView) findViewById(R.id.mine_tv_jigouname);
        mine_tv_jigouaddress = (TextView) findViewById(R.id.mine_tv_jigouaddress);
        mine_tv_jigouintroduce = (TextView) findViewById(R.id.mine_tv_jigouintroduce);
        mine_tv_introduce = (TextView) findViewById(R.id.mine_tv_introduce);
        mine_tv_address = (TextView) findViewById(R.id.mine_tv_address);
        mine_tv_goodat = (TextView) findViewById(R.id.mine_tv_goodat);
        mine_tv_lce = (TextView) findViewById(R.id.mine_tv_lce);
        tv_title_right_text = (TextView) findViewById(R.id.tv_title_right_text);
        mine_ll_position = (LinearLayout) findViewById(R.id.mine_ll_position);
        mine_ll_sex = (LinearLayout) findViewById(R.id.mine_ll_sex);
        mine_ll_introduce = (LinearLayout) findViewById(R.id.mine_ll_introduce);
        mine_ll_address = (LinearLayout) findViewById(R.id.mine_ll_address);
        mine_ll_goodat = (LinearLayout) findViewById(R.id.mine_ll_goodat);
        mine_ll_lce = (LinearLayout) findViewById(R.id.mine_ll_lce);
        mine_ll_jigouname = (LinearLayout) findViewById(R.id.mine_ll_jigouname);
        mine_ll_jigouaddress = (LinearLayout) findViewById(R.id.mine_ll_jigouaddress);
        mine_ll_jigouintroduce = (LinearLayout) findViewById(R.id.mine_ll_jigouintroduce);
        bt_profession_done = (Button) findViewById(R.id.bt_profession_done);
        tv_title_right_text.setText("保存");
        tv_title_right_text.setVisibility(View.INVISIBLE);
        //tv_title_right_text.setOnClickListener(this);
        if(profession.equals("3")) {
            mine_ll_jigouname.setOnClickListener(this);
            mine_ll_jigouaddress.setOnClickListener(this);
            mine_ll_jigouintroduce.setOnClickListener(this);
            mine_ll_lce.setOnClickListener(this);
            mine_ll_goodat.setOnClickListener(this);
            bt_profession_done.setOnClickListener(this);
        }
        if(profession.equals("4")){
            mine_ll_sex.setOnClickListener(this);
            mine_ll_introduce.setOnClickListener(this);
            mine_ll_lce.setOnClickListener(this);
            mine_ll_goodat.setOnClickListener(this);
            mine_ll_address.setOnClickListener(this);
            bt_profession_done.setOnClickListener(this);
        }
    }

    @Override
    protected void setContentView() {

        if(profession.equals("3")) {
            setContentView(R.layout.activity_institutions);
        }
        if(profession.equals("4")){
            setContentView(R.layout.activity_institution);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {

                mine_tv_introduce.setText(data.getStringExtra("department"));

        }
        if (requestCode == 3 && resultCode == RESULT_OK) {

            mine_tv_sex.setText(data.getStringExtra("sex"));

        }
        if (requestCode == 7 && resultCode == RESULT_OK) {

            mine_tv_jigouintroduce.setText(data.getStringExtra("sex"));
        }
        if (requestCode == 4 && resultCode == RESULT_OK) {

            mine_tv_address.setText(data.getStringExtra("address"));
        }
        if (requestCode == 6 && resultCode == RESULT_OK) {

            mine_tv_jigouaddress.setText(data.getStringExtra("jigouaddress"));
        }
        if (requestCode == 8 && resultCode == RESULT_OK) {

            mine_tv_goodat.setText(data.getStringExtra("goodat"));
        }
        if (requestCode == 5 && resultCode == RESULT_OK) {

            mine_tv_jigouname.setText(data.getStringExtra("jigouname"));
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            userCertificate=data.getStringExtra("Certificate");
            mine_tv_lce.setText("已上传");
        }


    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_ll_introduce:
                Intent birthday_intent = new Intent(getApplicationContext(),
                        MyDepartmentsActivity.class);
                startActivityForResult(birthday_intent, 2);
                break;
            case R.id.mine_ll_goodat:
                //Toast.makeText(getApplicationContext(), "番号", Toast.LENGTH_SHORT).show();
                if(profession.equals("3")){
                    Intent goodat_intent = new Intent(getApplicationContext(),
                            LableChooseActivity.class);
                    goodat_intent.putExtra("position", 4);
                    startActivityForResult(goodat_intent, 8);
                }
                if(profession.equals("4")) {
                    Intent goodat_intent = new Intent(getApplicationContext(),
                            LableChooseActivity.class);
                    goodat_intent.putExtra("position", 5);
                    startActivityForResult(goodat_intent, 8);
                }
                    break;
            case R.id.mine_ll_jigouname:
                Intent jigouname_intent = new Intent(getApplicationContext(),
                        MyJigounameActivity.class);
                startActivityForResult(jigouname_intent, 5);
                break;
            case R.id.mine_ll_jigouaddress:
                Intent jigouaddress_intent = new Intent(getApplicationContext(),
                        MyJigouaddressActivity.class);
                startActivityForResult(jigouaddress_intent, 6);
                break;
            case R.id.mine_ll_jigouintroduce:
                Intent jigouintroduce_intent = new Intent(getApplicationContext(),
                        MyJigouintroduceActivity.class);
                startActivityForResult(jigouintroduce_intent, 7);
                break;
            case R.id.mine_ll_lce:
                Intent pictrue_intent = new Intent(getApplicationContext(),
                        CertificateActivity.class);
                pictrue_intent.putExtra(CertificateActivity.MESSAGE_INFOS, "上传执业证书");
                startActivityForResult(pictrue_intent, 1);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);

                break;
            case R.id.mine_ll_sex:
                Intent sex_intent = new Intent(getApplicationContext(),
                        MyHospitalActivity.class);
                startActivityForResult(sex_intent, 3);
                break;
            case R.id.mine_ll_address:
                Intent address_intent = new Intent(getApplicationContext(),
                        MyAddressActivity.class);
                startActivityForResult(address_intent, 4);
                break;
            case R.id.bt_profession_done:
               if (profession.equals("3")) {
                   String mine_tv_lces = mine_tv_jigouname.getText().toString();
                   String mine_tv_sexs = mine_tv_jigouaddress.getText().toString();
                   String mine_tv_introduces = mine_tv_jigouintroduce.getText().toString();
                   String mine_tv_addresss = mine_tv_lce.getText().toString();
                   String mine_tv_goodats = mine_tv_goodat.getText().toString();
                   if (TextUtils.isEmpty(mine_tv_lces) //|| userId.length() != 11
                           || TextUtils.isEmpty(mine_tv_sexs)
                           || TextUtils.isEmpty(mine_tv_introduces)
                           || TextUtils.isEmpty(mine_tv_addresss)
                           || TextUtils.isEmpty(mine_tv_goodats)) {
                       AppContext.showToast("请完善信息");
                       return;
                   }
                        Api.applyChangeProfession(4, alterNameHandler);
                    }
                if (profession.equals("4")) {
                    String mine_tv_lces = mine_tv_lce.getText().toString();
                    String mine_tv_sexs = mine_tv_sex.getText().toString();
                    String mine_tv_introduces = mine_tv_introduce.getText().toString();
                    String mine_tv_addresss = mine_tv_address.getText().toString();
                    String mine_tv_goodats = mine_tv_goodat.getText().toString();
                    if (TextUtils.isEmpty(mine_tv_lces) //|| userId.length() != 11
                            || TextUtils.isEmpty(mine_tv_sexs)
                            || TextUtils.isEmpty(mine_tv_introduces)
                            || TextUtils.isEmpty(mine_tv_addresss)
                            || TextUtils.isEmpty(mine_tv_goodats)) {
                        AppContext.showToast("请完善信息");
                        return;
                    }
                        Api.applyChangeProfession(5, alterNameHandler);
                    }

                break;

            default:
                break;
        }
    }
    private ResponseCallback<Void> alterNameHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            finish();
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast( message);

        }
    };
    private ResponseCallback<Void> alertInformationHandler = new ResponseCallback<Void>() {

        @Override
        public void onSuccess(Void data) {
            AppContext.showToast("请耐心等待认证结果");
            finish();
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("上传图片失败：" + message);

        }
    };


}
