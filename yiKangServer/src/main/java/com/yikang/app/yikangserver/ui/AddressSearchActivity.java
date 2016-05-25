package com.yikang.app.yikangserver.ui;

import java.util.ArrayList;
import java.util.List;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.SimpleAddr;
import com.yikang.app.yikangserver.ui.AddressInputTipsModel.OnTipsResultListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 搜索地址的activity，负责确定一个位置
 */
public class AddressSearchActivity extends BaseActivity implements OnClickListener,
		OnTipsResultListener {
	private static String TAG = "AddressSearchActivity";
	public static final String EXTRA_ADCODE = "EXTRA_ADCODE";
	public static final String EXTRA_ADDR_DETIAL = "EXTRA_ADDR_DETIAL";
	public static final String EXTRA_ADDR_TITLE = "EXTRA_ADDR_TITLE";
	public static final String EXTRA_ADDR_DISTRICT = "EXTRA_ADDR_MAP";

	private String cityCode = "110000";

	private AddressInputTipsModel inputTipsModel; // 提供搜索的提示处理

	private EditText edtAddr;
	private EditText edtAddrDetail;

	// private TextView tvMyAddr;
	private ListView lvAddrs; // 显示提示的listView
	private List<SimpleAddr> addrList = new ArrayList<SimpleAddr>();
	private MyAddrAdapter adapter;
	private View tvTips;
	private SimpleAddr simpleAddr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContent();
		initTitleBar(getResources().getString(R.string.address_search_title));
		inputTipsModel = new AddressInputTipsModel(this, cityCode);
		inputTipsModel.setOnSearchResultListener(this);
	}

	@Override
	protected void initTitleBar(String title) {
		super.initTitleBar(title);
		TextView tvConfirm = (TextView) findViewById(R.id.tv_title_right_text);
		tvConfirm.setText(getString(R.string.confirm));
		tvConfirm.setOnClickListener(this);
	}

	@Override
	protected void findViews() {
		edtAddr = (EditText) findViewById(R.id.edt_addrSearch_input);
		edtAddrDetail = (EditText) findViewById(R.id.edt_addrSearch_input_detail);
		lvAddrs = (ListView) findViewById(R.id.lv_addrSearch_addrs_prompt);
		tvTips = findViewById(R.id.tv_no_addr_tips);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_addresss_search);
	}

	@Override
	protected void getData() {
	}

	@Override
	protected void initViewContent() {
		// 初始化listView
		adapter = new MyAddrAdapter(this, addrList);
		lvAddrs.setAdapter(adapter);

		edtAddr.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// 当文字改变时就发起搜索
				resetDetailAddr();
				String keyWord = s.toString();
				inputTipsModel.setKeyWord(keyWord); // 当文字改变，便开始搜索
				inputTipsModel.requestInputtips(); // 开始异步搜索
			}
		});

		// 设置item的点击事件，点击某一条地址之后，将选中的地址填写到输入框中
		lvAddrs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				edtAddr.setText(addrList.get(position).title);
				edtAddrDetail.setVisibility(View.VISIBLE);
				simpleAddr = addrList.get(position);

				edtAddrDetail.requestFocus();
			}
		});

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_title_right_text: // 将结果回传该activity的启动者
			checkAndConfirm();
			break;
		default:
			break;
		}

	}

	private void checkAndConfirm() {
		String addrDetial = edtAddrDetail.getText().toString();
		if (TextUtils.isEmpty(addrDetial)) {
			if (edtAddrDetail.getVisibility() == View.VISIBLE) {
				AppContext.showToast(R.string.addrSearch_detial_toast);
			} else {
				if(TextUtils.isEmpty(edtAddr.getText())){
					AppContext.showToast(R.string.addrSearch_addr_input_toast);
				}else{
					AppContext.showToast(R.string.addrSearch_addr_chose_tip);
				}
				resetDetailAddr();
			}
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_ADDR_TITLE, simpleAddr.title);
		intent.putExtra(EXTRA_ADDR_DETIAL, addrDetial);
		intent.putExtra(EXTRA_ADDR_DISTRICT, simpleAddr.district);
		intent.putExtra(EXTRA_ADCODE, simpleAddr.adCode);
		setResult(100, intent);
		finish();
	}

	/**
	 * 返回搜索结果的回调方法，OnSearchResultListener接口的方法
	 */
	@Override
	public void onTipsResult(List<SimpleAddr> addresses, boolean isResultOK) {
		addrList.clear();
		if (isResultOK) {
			if (!(addresses == null || addresses.isEmpty())) {
				Log.i(TAG, "接受到" + addresses.size() + "条数据");
				tvTips.setVisibility(View.GONE);
				addrList.addAll(addresses);
				adapter.notifyDataSetChanged();
			} else {
				tvTips.setVisibility(View.VISIBLE);
				addrList.clear();
				adapter.notifyDataSetChanged();
				resetDetailAddr();
			}
		}
	}

	private void resetDetailAddr() {
		edtAddrDetail.setText("");
		edtAddrDetail.setVisibility(View.GONE);
	}

	/**
	 * 显示提示地址的listView的adapter
	 * 
	 * @author LGhui
	 * 
	 */
	public class MyAddrAdapter extends CommonAdapter<SimpleAddr> {
		public MyAddrAdapter(Context context, List<SimpleAddr> mDatas) {
			super(context, mDatas, R.layout.item_addr_info);
		}

		@Override
		public void convert(ViewHolder helper, SimpleAddr item) {
			int position = helper.getPosition();
			TextView tvTitle = (TextView) helper
					.getView(R.id.tv_itme_addr_title);
			TextView district = (TextView) helper
					.getView(R.id.tv_item_addr_detail);
			district.setText(datas.get(position).district);
			tvTitle.setText(datas.get(position).title);
		}

	}

}
