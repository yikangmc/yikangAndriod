package com.yikang.app.yikangserver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;

import java.util.List;

public class LableChooseGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	LayoutInflater layoutInflater;
	private TextView lable_chosse_item;

	public LableChooseGridViewAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size()+1;//注意此处
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = layoutInflater.inflate(R.layout.item_lable_choose, null);
        lable_chosse_item = (TextView) convertView.findViewById(R.id.lable_chosse_item);
		if (position < list.size()) {
            lable_chosse_item.setText(list.get(position));
            if (position==0){
                lable_chosse_item.setTextColor(Color.parseColor("#ffffff"));
                lable_chosse_item.setBackgroundColor(Color.parseColor("#0faadd"));
            }else{
                lable_chosse_item.setBackgroundColor(Color.parseColor("#dcdcdc"));
            }
		}else if (position==4){
            lable_chosse_item.setVisibility(View.GONE);
        }else{
            lable_chosse_item.setBackgroundResource(R.drawable.yk_lable_tab_choose);//最后一个显示加号图片
		}
		return convertView;
	}

}
