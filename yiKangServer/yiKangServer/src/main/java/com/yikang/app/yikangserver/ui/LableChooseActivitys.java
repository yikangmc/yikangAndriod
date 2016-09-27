package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.Childs;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.view.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加标签界面
 */
public class LableChooseActivitys extends BaseActivity implements View.OnClickListener{
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    private List<Childs> allList=new ArrayList<Childs>();
    private XListView message_xlistview;
    private String messageinfo;
    List<LablesBean> wonderfulActivity;
    private CommonAdapter<Childs> alllableEditorCAdapter;
    private ArrayList<Childs> Comlables = new ArrayList<Childs>();
    private String type;
    public static final String LABLE_CHOOSE= "chooseresult";
    private TextView tv_title_right_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        type=getIntent().getStringExtra("type");
        initContent();
        initTitleBar("选择标签");
    }

    protected void geneItems() {
        for (int i = 0; i < wonderfulActivity.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(wonderfulActivity.get(i).getTagName());
            lable.setChilds(wonderfulActivity.get(i).getChilds());
            lables.add(lable);
        }
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     */
    public static String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);

        return dateString;
    }
    @Override
    protected void findViews() {
       /* tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("保存");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppContext.showToast("保存");
                Intent intent = new Intent();
               *//* if (fanhao != null) {
                    intent.putExtra("fanhao", fanhao.getDesignationName());// mCurrentDistrictName
                } else {

                }*//*
                intent.putExtra("tag", Comlables);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });*/
        message_xlistview=(XListView)findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter=new CommonAdapter<LableDetailsBean>(this,lables,R.layout.list_mine_more_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                GridView community_mine_lables_gridview = holder.getView(R.id.community_mine_lables_gridview);
                lables_tv_title.setText(item.getHeadTvName());
                allList.clear();
                for(int i=0;i<item.getChilds().size();i++){
                    allList.add(item.getChilds().get(i));
                }
                community_mine_lables_gridview.setAdapter(alllableEditorCAdapter);
                alllableEditorCAdapter =new CommonAdapter<Childs>(getApplicationContext(),allList,R.layout.gridview_item_choose_lables) {
                    @Override
                    protected void convert(ViewHolder holder, final Childs item) {
                        final TextView lable =(TextView)holder.getView(R.id.lables_top_textview);
                        lable.setText(item.getTagName());
                        lable.setBackgroundColor(Color.parseColor("#dcdcdc")) ;
                        lable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              // AppContext.showToast(item.getTagName());
                               // getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
                                Intent intent = new Intent();
                                if (item.getTagName() != null) {
                                    intent.putExtra("name",item.getTagName());// mCurrentDistrictName
                                    intent.putExtra("id", item.getTaglibId()+"");// mCurrentDistrictName

                                }
                                setResult(RESULT_OK, intent);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });
                    }
                };
            }
        };
        message_xlistview.setAdapter(mMessageAdapter);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start = ++refreshCnt;
                        lables.clear();
                        geneItems();
                        message_xlistview.setAdapter(mMessageAdapter);
                        onLoad();
                    }
                }, 1000);
            }
            private void onLoad() {
                message_xlistview.stopRefresh();
                message_xlistview.stopLoadMore();
                message_xlistview.setRefreshTime("刚刚");
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        geneItems();
                        mMessageAdapter.notifyDataSetChanged();
                        onLoad();
                    }
                }, 1000);
            }

            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }

        });
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_more_lables);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        //获取一级二级标签列表
        Api.getFirstSecondLableContent(wonderfulActivityHandler);
    }


    private ResponseCallback<List<LablesBean>> wonderfulActivityHandler = new ResponseCallback<List<LablesBean>>() {



        @Override
        public void onSuccess(List<LablesBean> data) {
            hideWaitingUI();
            //LOG.i("debug", "HpWonderfulContent---->" + data);
            if(data!=null) {
                wonderfulActivity = new ArrayList<>();
                for (LablesBean hp : data) {
                    wonderfulActivity.add(hp);
                }
                geneItems();
            }

            message_xlistview.setAdapter(mMessageAdapter);
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    @Override
    protected void initViewContent() {}

    @Override
    public void onClick(View view) {

        switch (view.getId()){
        }
    }
}
