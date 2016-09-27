package com.yikang.app.yikangserver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater inflater;
	protected Context context;
	protected List<T> datas;
	protected final int itemLayoutId;
	public CommonAdapter(Context context, List<T> datas, int itemLayoutId) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.datas = datas;
		this.itemLayoutId = itemLayoutId;
	}



	@Override
	public int getCount() {

		return datas != null ? datas.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		initCreatedHolder(viewHolder);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();

	}

	protected void initCreatedHolder(ViewHolder holder) {

	}

	/**
	 * 填充数据
	 * @param holder
	 * @param item
	 */
	protected abstract void convert(ViewHolder holder, T item);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(context, convertView, parent, itemLayoutId,
				position);
	}

}
