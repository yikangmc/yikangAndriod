package com.yikang.app.yikangserver.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.utils.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/21.
 */
public class LablesEditorActivity extends BaseActivity implements View.OnClickListener{

    private GridView homepage_more_tables_gridview;
    private String tab_lables[]={"运动康复","瘦身","营养专家","日常训练","体态矫正","吞咽障碍",
            "语言康复","糖尿病","神经","老人","儿童","偏瘫"};
    private List<String> mineList=new ArrayList<String>();
    private List<String> allList=new ArrayList<String>();
    private GridView community_mine_lables_gridview ,community_all_lables_gridview;
    private CommonAdapter<String> minelableEditorCAdapter,alllableEditorCAdapter;
    private GridViewAdapter longAdapter;
    private boolean isShowDelete=false;
    private TextView lable_editoer_tv;
    private View  lable_divider;
    private LinearLayout lables_all_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        initTitleBar("编辑标签");
        for(int i=0;i<tab_lables.length;i++){
            mineList.add(tab_lables[i]);
            allList.add(tab_lables[i]);
        }
    }

    @Override
    protected void findViews() {
        lable_divider=(View)findViewById(R.id.lable_divider);
        lables_all_ll=(LinearLayout)findViewById(R.id.lables_all_ll);
        community_all_lables_gridview=(GridView)findViewById(R.id.community_all_lables_gridview);
        community_mine_lables_gridview=(GridView)findViewById(R.id.community_mine_lables_gridview);
        lable_editoer_tv=(TextView)findViewById(R.id.lable_editoer_tv);
        lable_editoer_tv.setOnClickListener(this);
        minelableEditorCAdapter =new CommonAdapter<String>(this,mineList,R.layout.gridview_item_choose_lables) {
            @Override
            protected void convert(ViewHolder holder, String item) {
                TextView lable =(TextView)holder.getView(R.id.lables_top_textview);
                lable.setText(item);
                lable.setBackgroundColor(Color.parseColor("#dcdcdc"));
            }
        };
        alllableEditorCAdapter =new CommonAdapter<String>(this,allList,R.layout.gridview_item_choose_lables) {
            @Override
            protected void convert(ViewHolder holder, String item) {
                TextView lable =(TextView)holder.getView(R.id.lables_top_textview);
                lable.setText(item);
                lable.setBackgroundColor(Color.parseColor("#dcdcdc"));
            }
        };
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lables_editor);
    }

    @Override
    protected void getData() {
        longAdapter=new GridViewAdapter(LablesEditorActivity.this,mineList);
        community_all_lables_gridview.setAdapter(alllableEditorCAdapter);
        community_mine_lables_gridview.setAdapter(minelableEditorCAdapter);
    }

    @Override
    protected void initViewContent() {

        community_all_lables_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mineList.add(allList.get(position));
                allList.remove(position);
                minelableEditorCAdapter.notifyDataSetChanged();
                alllableEditorCAdapter.notifyDataSetChanged();
            }
        });
        community_mine_lables_gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (lables_all_ll.isShown()==false){
                    if (isShowDelete) {
                        isShowDelete = false;
                    } else {
                        isShowDelete = true;
                    }
                    //lable_editoer_tv.setText("完成");
                    community_mine_lables_gridview.setAdapter(longAdapter);
                    longAdapter.setIsShowDelete(isShowDelete);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lable_editoer_tv:
                if (lable_editoer_tv.getText().toString().equals("完成")){
                    LOG.i("debug","minelist===>"+mineList);
                    finish();
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
                if (lable_editoer_tv.getText().toString().equals("编辑")){
                    lables_all_ll.setVisibility(View.GONE);
                    lable_divider.setVisibility(View.GONE);
                    lable_editoer_tv.setText("完成");
                }

                break;
        }
    }

    public class GridViewAdapter extends BaseAdapter {
        List<String> list;
        private Context mContext;
        private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示

        public GridViewAdapter(Context mContext,List<String> _list ) {
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
            holder.lable.setText(list.get(position));
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
