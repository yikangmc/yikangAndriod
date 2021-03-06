package com.yikang.app.yikangserver.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.Expert;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/21.
 */
public class MyGoodatActivity extends BaseActivity {

    private GridView homepage_more_tables_gridview;
    private String tab_lables[]={"生意人","人民公仆","it搬砖","坐班白领","企业老板","音乐教父",
            "小护士","法律专家","掌勺的","在家看门","住院大人","修牙的","劳动模范","莘莘学子","娱乐圈","的士一哥"};
    private List<Expert> mineList=new ArrayList<Expert>();
    private List<Expert> experts=new ArrayList<>();
    private GridView community_mine_lables_gridview ,community_all_lables_gridview;
    private CommonAdapter<Expert> minelableEditorCAdapter,alllableEditorCAdapter;
    private GridViewAdapter longAdapter;
    private boolean isShowDelete=false;
    private TextView tv_title_right_text,lable;
    private Expert fanhao;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=getIntent().getIntExtra("position",0);
        initContent();
        initTitleBar("选择擅长项");
    }
    @Override
    protected void findViews() {

    }

    protected void findViewss() {
        community_mine_lables_gridview=(GridView)findViewById(R.id.community_mine_lables_gridview);
        tv_title_right_text=(TextView) findViewById(R.id.tv_title_right_text);
        tv_title_right_text.setText("保存");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AppContext.showToast("保存擅长方向"+experts.get(0).name);
            }
        });
        minelableEditorCAdapter =new CommonAdapter<Expert>(this,mineList,R.layout.gridview_item_choose_lables) {
            @Override
            protected void convert(ViewHolder holder, Expert item) {
                lable =(TextView)holder.getView(R.id.lables_top_textview);
                lable.setText(item.getName());

            }
        };
        longAdapter=new GridViewAdapter(MyGoodatActivity.this,mineList);
        community_mine_lables_gridview.setAdapter(minelableEditorCAdapter);
        community_mine_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*for (int i = 0; i < parent.getCount(); i++) {
                    TextView w = (TextView) parent.getChildAt(i).findViewById(R.id.lables_top_textview);
                    w.setTextColor(Color.parseColor("#323232"));
                    w.setBackgroundColor(Color.parseColor("#dcdcdc"));
                }*/
                TextView tvView = (TextView) view.findViewById(R.id.lables_top_textview);
                if(tvView.getBackground().equals("#dcdcdc")) {
                    tvView.setBackgroundColor(Color.parseColor("#0faadd"));
                    tvView.setTextColor(Color.WHITE);
                    experts.add(mineList.get(position));
                }
                if(tvView.getTextColors().equals("#ffffff")){
                    tvView.setTextColor(Color.parseColor("#323232"));
                    tvView.setBackgroundColor(Color.parseColor("#dcdcdc"));
                    experts.remove(mineList.get(position));
                }

            }
        });

    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_my_goodat);
    }

    @Override
    protected void getData() {
        showWaitingUI();
        Api.getExperts(position,mineFanhaoHandler);
    }

    private ResponseCallback<String> changeFanhaoHandler = new ResponseCallback<String>() {

        @Override
        public void onSuccess(String data) {
            hideWaitingUI();
           // LOG.i("debug", "HpWonderfulContent---->" + data);
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            Intent intent = new Intent();
            if(fanhao!=null) {
                intent.putExtra("fanhao", fanhao.getName());// mCurrentDistrictName
            }else{
                intent.putExtra("fanhao", "");
            }
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };


    private ResponseCallback<List<Expert>> mineFanhaoHandler = new ResponseCallback<List<Expert>>() {



        @Override
        public void onSuccess(List<Expert> data) {
            hideWaitingUI();
           // LOG.i("debug", "HpWonderfulContent---->" + data);
            mineList=data;
            findViewss();
        }

        @Override
        public void onFailure(String status, String message) {
          //  LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    @Override
    protected void initViewContent() {

    }

    public class GridViewAdapter extends BaseAdapter {
        List<Expert> list;
        private Context mContext;
        private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示

        public GridViewAdapter(Context mContext,List<Expert> _list ) {
            this.mContext = mContext;
            this.list=_list;
        }
        public void setIsShowDelete(boolean isShowDelete){
            this.isShowDelete=isShowDelete;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.gridview_item_choose_lables, null);
                holder.lable = (TextView) convertView.findViewById(R.id.lables_top_textview);
                holder.delete=(ImageView)convertView.findViewById(R.id.lables_top_iv_delete) ;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.lable.setText(list.get(position).getName());
            holder.lable.setBackgroundColor(Color.parseColor("#dcdcdc"));
            holder.delete.setVisibility(isShowDelete?View.VISIBLE:View.GONE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
        class ViewHolder {
            TextView lable;
            ImageView delete;
        }
    }


}
