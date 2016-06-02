package com.yikang.app.yikangserver.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.LvHightUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索页面
 */
public class KeywordSearchActivity extends BaseActivity implements View.OnClickListener {
    public static final int RESULT_LOGOUT = -1;

    private ImageView keyword_delete_iv;
    private TextView keyword_search_tv;
    private EditText  keyword_search_info_et;

    private String[] content=new String[]{"如何治疗关节炎？","彻底摆脱关节炎","教你如何预防关节炎"};
    private String[] answer=new String[]{"如何治疗关节炎？","彻底摆脱关节炎","教你如何预防关节炎"};
    private ListView  keyword_search_more_people_lv,keyword_search_more_content_lv,keyword_search_more_answer_lv;
    private LinearLayout keyword_search_more_people_ll,keyword_search_more_content_ll,keyword_search_more_answer_ll;
    private CommonAdapter<Map<String, Object>> search_people;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("搜索");
    }
    @Override
    protected void findViews() {
        keyword_delete_iv=(ImageView)findViewById(R.id.keyword_delete_iv);
        keyword_delete_iv.setOnClickListener(this);
        keyword_search_tv=(TextView)findViewById(R.id.keyword_search_tv);
        keyword_search_tv.setOnClickListener(this);
        keyword_search_info_et=(EditText)findViewById(R.id.keyword_search_info_et);
        keyword_search_more_content_lv=(ListView)findViewById(R.id.keyword_search_more_content_lv);
        keyword_search_more_people_lv=(ListView)findViewById(R.id.keyword_search_more_people_lv);
        keyword_search_more_answer_lv=(ListView)findViewById(R.id.keyword_search_more_answer_lv);
        keyword_search_more_people_ll=(LinearLayout)findViewById(R.id.keyword_search_more_people_ll);
        keyword_search_more_content_ll=(LinearLayout)findViewById(R.id.keyword_search_more_content_ll);
        keyword_search_more_answer_ll=(LinearLayout)findViewById(R.id.keyword_search_more_answer_ll);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_keyword_search);
    }

    @Override
    protected void getData() {
        if (content.length>0){  //判断有无数据，有数据就显示，无数据就隐藏
            keyword_search_more_content_ll.setVisibility(View.VISIBLE);
        }
        if (answer.length>0){  //判断有无数据，有数据就显示，无数据就隐藏
            keyword_search_more_content_ll.setVisibility(View.VISIBLE);
        }
        if (getData2().size()>0){  //判断有无数据，有数据就显示，无数据就隐藏
            keyword_search_more_people_ll.setVisibility(View.VISIBLE);
        }
    }

    public List<Map<String, Object>> getData2(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", R.drawable.ic_launcher);
            map.put("title", "这是一个标题"+i);
            map.put("info", "这是一个详细信息"+i);
            list.add(map);
        }
        return list;
    }

    @Override
    protected void initViewContent() {
        //搜索相关专业内容结果
        keyword_search_more_content_lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,content));
        LvHightUtils.setListViewHeightBasedOnChildren(keyword_search_more_content_lv);
        search_people=new CommonAdapter<Map<String, Object>>(this,getData2(),R.layout.list_search_keyword_item) {
            @Override
            protected void convert(ViewHolder holder, Map<String, Object> item) {

            }
        };
        //搜索相关专业人员结果
        keyword_search_more_people_lv.setAdapter(search_people);
        LvHightUtils.setListViewHeightBasedOnChildren(keyword_search_more_people_lv);


    }



    /**
     * 登出
     */
    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_prompt)
                .setMessage(R.string.logout_dialog_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        }).create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.keyword_delete_iv:
                keyword_search_info_et.setText("");
                break;
            case R.id.keyword_search_tv:
               // markApp();
                Toast.makeText(getApplicationContext(),"开始搜索",Toast.LENGTH_SHORT).show();
                Api.getSearchContent(keyword_search_info_et.getText().toString().trim(),publishAnswerHandler);
                break;
        }
    }

    private ResponseCallback<String> publishAnswerHandler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {
            hideWaitingUI();
            LOG.i("debug", "HpWonderfulContent---->" + data);

        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            // hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    /**
     * 该app打分
     */
    private void markApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
