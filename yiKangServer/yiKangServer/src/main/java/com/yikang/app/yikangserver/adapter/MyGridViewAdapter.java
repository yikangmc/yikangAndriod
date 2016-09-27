package com.yikang.app.yikangserver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;

import java.util.List;

/**
 * Created by yudong on 2016/4/18.
 */
public class MyGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    public MyGridViewAdapter(Context context, List<String> _list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list=_list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
       // LOG.i("debug","position222-->"+position);
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
       // LOG.i("debug","position-->"+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stud
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.gridview_item_choose_sticker, null);
            holder.tv = (TextView) convertView.findViewById(R.id.homepage_hot_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
