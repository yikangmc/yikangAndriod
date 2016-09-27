package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.MyLablesGridViewAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/21.
 *
 * 精心解答，专业内容
 */
public class ProfessionalContentActivity extends BaseActivity implements XListView.IXListViewListener,View.OnClickListener {

    private GridView homepage_more_tables_gridview;
    private XListView lables_listview;
    private String tab_lables[]={"运动康复","瘦身","营养专家","日常训练","体态矫正","吞咽障碍",
            "语言康复","糖尿病","神经","老人","儿童","偏瘫"};
    private List<String> stringList=new ArrayList<String>();
    private int NUM = 6; // 每行显示个数
    private int LIEWIDTH;//每列宽度
    DisplayMetrics dm;
    private Handler mHandler;
    private ArrayList<LablesBean> lables = new ArrayList<LablesBean>();
    private int start = 0;
    private static int refreshCnt = 0;
//    private MyAdapater  myAdapater;
    private CommonAdapter<LablesBean> lanleCommonAdapter;
    private LinearLayout community_professional_ll_posting;
    private ImageView community_professional_iv_posting,professional_add_lable_iv;
    private int fromCommunityFind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromCommunityFind=getIntent().getIntExtra("fromCommunityFind",1);
        mHandler=new Handler();
        geneItems("康复");
        initContent();
        if (fromCommunityFind==1){
            initTitleBar("专业内容");
        }else  if (fromCommunityFind==2){
            initTitleBar("问答");
        }
    }

    private void geneItems(String name) {
        for (int i = 0; i != 20; ++i) {
            LablesBean  lable=new LablesBean();
            lable.setLableName(name+"  "+i);
            lable.setLableTiezi("tiezi  "+i);
            lable.setLableTime("time  "+i);
            lable.setLableSee("see  "+i);
            lables.add(lable);
        }
    }

    @Override
    protected void findViews() {
        lables_listview=(XListView) findViewById(R.id.community_professional_listview);
        lables_listview.setPullLoadEnable(true);
//        myAdapater=new MyAdapater(this, lables);
        if (fromCommunityFind==1){
            lanleCommonAdapter=new CommonAdapter<LablesBean>(this,lables,R.layout.list_professional_content_item) {
                @Override
                protected void convert(ViewHolder holder, LablesBean item) {

//                TextView lables_tv_title=holder.getView(R.id.lables_tv_title);
//                TextView lables_tv_tiezi=holder.getView(R.id.lables_tv_tiezi);
//                TextView lables_tv_time=holder.getView(R.id.lables_tv_time);
//                TextView lables_tv_see=holder.getView(R.id.lables_tv_see);
//                lables_tv_title.setText(item.getLableName());
//                lables_tv_tiezi.setText(item.getLableTiezi());
//                lables_tv_time.setText(item.getLableTime());
//                lables_tv_see.setText(item.getLableSee());
                }
            };
        }else if (fromCommunityFind==2){
            lanleCommonAdapter=new CommonAdapter<LablesBean>(this,lables,R.layout.list_answer_content_item) {
                @Override
                protected void convert(ViewHolder holder, LablesBean item) {

                }
            };
        }
        lables_listview.setAdapter(lanleCommonAdapter);
        lables_listview.setXListViewListener(this);

        lables_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ProfessionalContentActivity.this,"您选择了标签："+lables.get(i).getLableName(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ProfessionalContentActivity.this,LableDetaileContentActivity.class);
                intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER,1);
                startActivity(intent);
            }
        });

        community_professional_ll_posting=(LinearLayout)findViewById(R.id.community_professional_ll_posting);
        community_professional_iv_posting=(ImageView)findViewById(R.id.community_professional_iv_posting);
        professional_add_lable_iv=(ImageView)findViewById(R.id.professional_add_lable_iv);
        community_professional_iv_posting.setOnClickListener(this);
        professional_add_lable_iv.setOnClickListener(this);

        homepage_more_tables_gridview=(GridView)findViewById(R.id.community_professional_gridview);
        getScreenDen();
        LIEWIDTH = dm.widthPixels / NUM;
        setValue();
    }

    private void setValue() {
        for(int i=0;i<tab_lables.length;i++){
            stringList.add(tab_lables[i]);
        }
        MyLablesGridViewAdapter adapter = new MyLablesGridViewAdapter(this, stringList);
        homepage_more_tables_gridview.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * LIEWIDTH,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        homepage_more_tables_gridview.setLayoutParams(params);// 设置GirdView布局参数,横向布局的关键
        homepage_more_tables_gridview.setColumnWidth(dm.widthPixels / NUM);// 设置列表项宽
        homepage_more_tables_gridview.setStretchMode(GridView.NO_STRETCH);
        int count = adapter.getCount();
        homepage_more_tables_gridview.setNumColumns(count);// 设置列数量=列表集合数

        homepage_more_tables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                Toast.makeText(ProfessionalContentActivity.this,"您选择了："+stringList.get(position),Toast.LENGTH_LONG).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start = ++refreshCnt;
                        lables.clear();
                        geneItems(stringList.get(position));
                        lables_listview.setAdapter(lanleCommonAdapter);
                        onLoad();
                    }
                }, 10);
            }
        });
    }

    private void getScreenDen() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_community_professionalcontent);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }

    private void onLoad() {
        lables_listview.stopRefresh();
        lables_listview.stopLoadMore();
        lables_listview.setRefreshTime("刚刚");
    }
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                lables.clear();
                geneItems("康复");
                lables_listview.setAdapter(lanleCommonAdapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems("康复");
                lanleCommonAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onScrollMove() {
       /* Animation mHiddenAction= AnimationUtils.loadAnimation(this,R.anim.trans_bottom_out);
        community_professional_ll_posting.startAnimation(mHiddenAction);*/
        community_professional_ll_posting.setVisibility(View.GONE);
    }

    @Override
    public void onScrollFinish() {
        Animation mShowAction= AnimationUtils.loadAnimation(this,R.anim.trans_bottom_in);
        community_professional_ll_posting.startAnimation(mShowAction);
        community_professional_ll_posting.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.community_professional_iv_posting:
                Intent intent1 = new Intent(this,
                        ProfessionPostActivity.class);  //发布专业内容
                startActivity(intent1);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
            case R.id.professional_add_lable_iv:
                Intent intent = new Intent(this,
                        LablesEditorActivity.class);   //编辑便签
                startActivity(intent);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;
        }
    }
}
