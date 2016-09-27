package com.yikang.app.yikangserver.fragment.alter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.data.MyData;
import com.yikang.app.yikangserver.dialog.RenzhengAlertDialog;
import com.yikang.app.yikangserver.fragment.BaseFragment;
import com.yikang.app.yikangserver.ui.InstitutionActivity;
import com.yikang.app.yikangserver.ui.KangFushiActivity;
import com.yikang.app.yikangserver.ui.OtherCertificateActivity;
import com.yikang.app.yikangserver.ui.WebComunicateviewActivivty;
import com.yikang.app.yikangserver.utils.LOG;

/**
 * 选择职位
 */
public class InitProfessionFragment extends BaseFragment
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioButton rbDoctor, rbTherapists, rbNurse,rb_profession_qi,rb_profession_yuan;
    private Button btDone;
    private RadioGroup rgProfessions,rg_professionqiye_choices;

    private int profession; //选中的职业
    private TextView tv_personnal,tv_institution; //选中的职业

    private OnDone callback;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnDone){
            this.callback = (OnDone) activity;
        }else{
            throw new IllegalArgumentException("wrong argument,context should be instance of InitProfessionFragment.OnDone");
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_init_profession, container, false);

        tv_institution = (TextView) view.findViewById(R.id.tv_institution);
        tv_personnal = (TextView) view.findViewById(R.id.tv_personnal);
        rbDoctor = (RadioButton) view.findViewById(R.id.rb_profession_doctor);
        rbTherapists = (RadioButton) view.findViewById(R.id.rb_profession_therapists);
        rbNurse = (RadioButton) view.findViewById(R.id.rb_profession_nurse);
        rgProfessions = ((RadioGroup) view.findViewById(R.id.rg_profession_choices));
        rg_professionqiye_choices = ((RadioGroup) view.findViewById(R.id.rg_professionqiye_choices));
        rb_profession_qi = ((RadioButton) view.findViewById(R.id.rb_profession_qi));
        rb_profession_yuan = ((RadioButton) view.findViewById(R.id.rb_profession_yuan));
        btDone = ((Button) view.findViewById(R.id.bt_profession_done));

        rgProfessions.check(R.id.rb_profession_doctor);//默认选中
        btDone.setOnClickListener(this);
        rgProfessions.setOnCheckedChangeListener(this);
        rg_professionqiye_choices.setOnCheckedChangeListener(this);
        //showPopWindow();
        tv_personnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WebIntent = new Intent(getActivity(), WebComunicateviewActivivty.class);
                WebIntent.putExtra("bannerUrl"," http://jjkangfu.cn/appPage/personal");
                WebIntent.putExtra("bannerName","个人主体认证说明\"");
                startActivity(WebIntent);
            }
        });

        tv_institution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WebIntent = new Intent(getActivity(), WebComunicateviewActivivty.class);
                WebIntent.putExtra("bannerUrl"," http://jjkangfu.cn/appPage/organization");
                WebIntent.putExtra("bannerName","机构主体认证说明");
                startActivity(WebIntent);
            }
        });
        return view;
    }

    private void showPopWindow() {
        final RenzhengAlertDialog ad = new RenzhengAlertDialog(getActivity());
        ad.setMessage("该项认证暂只支持康复科室的认证，其他的认证暂不会通过哦~");
        ad.setPositiveButton("放弃", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ad.dismiss();
                getActivity().finish();
            }
        });
        ad.setNegativeButton("继续去认证", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_profession_done:
                //callback.afterChooseProfession(profession);
                //showWaitingUI();
              // Api.applyChangeProfession(profession,alterNameHandler);
                if(profession==2){
                    Intent intentBasicInfo = new Intent(getActivity(),
                            KangFushiActivity.class);
                    intentBasicInfo.putExtra("InfoName",profession+"");
                    startActivity(intentBasicInfo);
                    getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    return;
                }
                if(profession==3|profession==4){
                    Intent intentBasicInfo = new Intent(getActivity(),
                            InstitutionActivity.class);
                    intentBasicInfo.putExtra("InfoName",profession+"");
                    startActivity(intentBasicInfo);
                    getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    return;
                }else{
                    Intent intentBasicInfo = new Intent(getActivity(),
                            OtherCertificateActivity.class);
                    intentBasicInfo.putExtra("InfoName",profession+"");
                    startActivity(intentBasicInfo);
                    getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
               // getActivity().finish();
                break;
        }
    }

    private ResponseCallback<Void> alterNameHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            if(profession==2){
                Intent intentBasicInfo = new Intent(getActivity(),
                        KangFushiActivity.class);
                intentBasicInfo.putExtra("InfoName",profession+"");
                startActivity(intentBasicInfo);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                return;
            }
            if(profession==3|profession==4){
                Intent intentBasicInfo = new Intent(getActivity(),
                        InstitutionActivity.class);
                intentBasicInfo.putExtra("InfoName",profession+"");
                startActivity(intentBasicInfo);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                return;
            }else{
                Intent intentBasicInfo = new Intent(getActivity(),
                        OtherCertificateActivity.class);
                intentBasicInfo.putExtra("InfoName",profession+"");
                startActivity(intentBasicInfo);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
          getActivity().finish();
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast( message);

        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_profession_doctor:
                profession = MyData.DOCTOR;
                rg_professionqiye_choices.clearCheck();
                rgProfessions.check(R.id.rb_profession_doctor);
                break;
            case R.id.rb_profession_nurse:
                profession = MyData.NURSING;
                rg_professionqiye_choices.clearCheck();
                rgProfessions.check(R.id.rb_profession_nurse);
                break;
            case R.id.rb_profession_therapists:
                profession = MyData.THERAPIST;
                rg_professionqiye_choices.clearCheck();
                rgProfessions.check(R.id.rb_profession_therapists);
                break;
            case R.id.rb_profession_qi:
                profession = MyData.QIYE;
                rgProfessions.clearCheck();
                rg_professionqiye_choices.check(R.id.rb_profession_qi);
                break;
            case R.id.rb_profession_yuan:
                profession = MyData.YIYUAN;
                rgProfessions.clearCheck();
                rg_professionqiye_choices.check(R.id.rb_profession_yuan);
                break;
            default:
                break;
        }
    }



    public interface OnDone{
        /**
         * 回调选中的职业
         * @param profession 详细请看{@link  MyData};
         */
        void afterChooseProfession(int profession);
    }

}
