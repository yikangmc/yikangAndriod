package com.yikang.app.yikangserver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.interfaces.CallBack;
import com.yikang.app.yikangserver.utils.ScreenModel;
import com.yikang.app.yikangserver.view.TitleViewCallBack;

import java.util.List;


public class SpinnerListAdapter extends BaseAdapter {
	private List<String> mList;
	private Context mContext;
	private TitleViewCallBack callBack;
	private CallBack callback;
	public SpinnerListAdapter(List<String> mList, Context mContext,
			TitleViewCallBack callBack, CallBack callback) {
		super();
		this.mList = mList;
		this.mContext = mContext;
		this.callBack = callBack;
		this.callback = callback;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.isEmpty() ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_spinner, null);
			vh = new ViewHolder();
			vh.name_Priduct = (TextView) convertView
					.findViewById(R.id.name_Priduct);
			vh.relative = (RelativeLayout) convertView
					.findViewById(R.id.relative);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		LayoutParams params = (LayoutParams) vh.relative.getLayoutParams();
		params.height = ScreenModel.getScreenModel(mContext).getPix(90f);
		vh.name_Priduct.setText(mList.get(position));
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callBack.callBack(position);
				callback.callBack();
			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView name_Priduct;
		private RelativeLayout relative;
	}

}
