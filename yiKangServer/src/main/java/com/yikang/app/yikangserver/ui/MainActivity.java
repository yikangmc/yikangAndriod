package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppConfig;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.base.BaseFragmentActivity;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.fragment.CommunityFragment;
import com.yikang.app.yikangserver.fragment.HomepageFragment;
import com.yikang.app.yikangserver.fragment.MessageFragment;
import com.yikang.app.yikangserver.fragment.MineFragment;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;
import com.yikang.app.yikangserver.utils.DeviceUtils;
import com.yikang.app.yikangserver.utils.DoubleClickExitHelper;
import com.yikang.app.yikangserver.utils.LOG;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseFragmentActivity implements OnCheckedChangeListener{
	protected static final String TAG = "MainActivity";
	private DoubleClickExitHelper mExitHelper; // 双击退出
	private UserInfoAlteredReceiver receiver;

	private ArrayList<Fragment> fragList = new ArrayList<>();

	private RadioGroup main_tab_group;
	private RadioButton main_tab_featured;
	private RadioButton main_tab_wallpaper;
	private RadioButton main_tab_mine;
	private RadioButton main_tab_more;

	private final int MAIN_TAB_FEATURED= 0;
	private final int MAIN_TAB_WALLPAPER= 1;
	private final int MAIN_TAB_MINE= 2;
	private final int MAIN_TAB_MORE= 3;

	private Fragment mineFragment,mineFragment2;
	private Fragment mineFragment3,mineFragment4;
    private ViewPager main_viewpage;
	/**
	 * 注册设备handler 测试1233333
	 */
	private ResponseCallback<Void> registerDeviceHandler=  new ResponseCallback<Void>() {
		@Override
		public void onSuccess(Void data) {
			AppConfig appConfig = AppConfig.getAppConfig(getApplicationContext());
			appConfig.setProperty(AppConfig.CONF_IS_DEVICE_REGISTED, "" + 1); // 将设备设置为注册成功
			if (AppContext.DEBUG) {
				String log = appConfig.getProperty(AppConfig.CONF_DEVICE_ID_TYPE) + "******"
						+ appConfig.getProperty(AppConfig.CONF_DEVICE_ID);
				LOG.e(TAG, "[registerDeviceHandler]" + log);
			}
			LOG.e(TAG, "[registerDeviceHandler]device register success!!");
		}

		@Override
		public void onFailure(String status, String message) {
			LOG.d(TAG, "[registerDeviceHandler]sorry,device register fail.error message:" + message);
		}
	};

	/**
	 * 加载用户信信息回调
	 */
	private ResponseCallback<User> loadUserInfoHandler = new ResponseCallback<User>(){
		@Override
		public void onSuccess(User user) {
			findViewById(R.id.main_load_error).setVisibility(View.GONE);
			hideWaitingUI();
			hideLoadError();
			if(user!=null){
				AppContext.getAppContext().login(user);
                LOG.i("debug","数据更新了 ");
				initViewsAfterGetInfo();
			}
		}

		@Override
		public void onFailure(String status, String message) {
			LOG.i(TAG,"[loadUserInfo]加载失败"+message);
			hideWaitingUI();
			AppContext.showToast(message);
			showLoadError();
		}
	};


	/**
	 * 获取推送别名的回调
	 */
	private ResponseCallback<Map<String,String>> getPushAliasHandler = new ResponseCallback<Map<String,String>>() {
		@Override
		public void onSuccess(Map<String,String> map) {
			LOG.w(TAG, "[requestAndSetJPushAlias]获取别名成功" + map);
			String alias = map.get("alias");
			if (!TextUtils.isEmpty(alias)) {
				setJPushAlias(alias);
			}
		}

		@Override
		public void onFailure(String status, String message) {
			LOG.w(TAG, "[requestAndSetJPushAlias]sorry! get alias fail.error message:" + message);
		}
	};




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED);
		receiver = new UserInfoAlteredReceiver();
		registerReceiver(receiver, filter);
		initContent();
        changeTitleBar("首页");
		mExitHelper = new DoubleClickExitHelper(this);
		if(!AppContext.getAppContext().isDeviceRegisted()){
			registerDevice(); // 注册设备
		}
//		requestAndSetJPushAlias(); // 设置极光推送的别名,别名是跟用户绑定在一起的
	}


	@Override
	protected void findViews() {
        mineFragment = new HomepageFragment();//首页
        mineFragment2 = new CommunityFragment();//社区
        mineFragment3 = new MessageFragment();//消息
        mineFragment4 = new MineFragment();//我的
		main_tab_group=(RadioGroup)findViewById(R.id.main_tab_group);
		main_tab_wallpaper=(RadioButton)findViewById(R.id.main_tab_wallpaper);
		main_tab_featured=(RadioButton)findViewById(R.id.main_tab_featured);
		main_tab_mine=(RadioButton)findViewById(R.id.main_tab_mine);
		main_tab_more=(RadioButton)findViewById(R.id.main_tab_more);
		main_tab_group.setOnCheckedChangeListener(this);
        main_viewpage=(ViewPager)findViewById(R.id.main_viewpage);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        main_viewpage.setAdapter(adapter);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void getData() {}

	@Override
	protected void initViewContent() {
		refreshFragments();
	}

	/**
	 * 加载用户信息刷新界面
	 */
	private void refreshFragments(){
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		for (Fragment fragment : fragList) {
//			ft.remove(fragment);
//		}
//		ft.commit();
//		fragList.clear();
		loadUserInfo();
	}

	@Override
	protected void onStart() {
		super.onStart();
		//当用户信息发生改变时，重新加载页面
		if(receiver.getAndConsume()){
			refreshFragments();
		}
	}


    /**
     * 注销用户
     */
    private void logout(){
        AppContext.getAppContext().logout();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


	/**
	 * 向服务器请求注册设备
	 */
	private void registerDevice() {
		int codeType = AppContext.getAppContext().getDeviceIdType();
		String deviceCode = AppContext.getAppContext().getDeviceID();
		Api.registerDevice(codeType, deviceCode, registerDeviceHandler);
	}



	/**
	 * 获取数据
	 */
	private void loadUserInfo() {
		showWaitingUI();
		Api.getUserInfo(loadUserInfoHandler);
	}


	private void hideLoadError(){
		findViewById(R.id.main_load_error).setVisibility(View.GONE);
	}

	private void showLoadError(){
		View view = findViewById(R.id.main_load_error);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideLoadError();
				loadUserInfo();
			}
		});
		TextView tvTips = ((TextView) findViewById(R.id.tv_error_tips));
		tvTips.setText(R.string.default_network_reload_describe);
	}


	/**
	 * init views after loading data;
	 */
	private void initViewsAfterGetInfo() {
		// 将fragment添加到MainActivity中
		//Fragment busiFragment = new BusinessMainFragment();
//		mineFragment = new HomepageFragment();
//		mineFragment2 = new CommunityFindFragment();
//		mineFragment3 = new CommunityFragment();
//		mineFragment4 = new MineFragment();

		//addTabsFragment(busiFragment, String.valueOf(R.id.rb_main_business));
//		addTabsFragment(mineFragment,MAIN_TAB_FEATURED+"");
	}

	/**
	 * 将fragment天骄到主页面中，tag设置为id
	 */
	private void addTabsFragment(Fragment fragment, String tag) {
		fragList.add(fragment);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(fragment.isAdded()){
			ft.show(fragment);
		}else{
			ft.add(R.id.fl_main_container, fragment, tag);
		}
//		//if (!String.valueOf(currentCheck).equals(tag)) {
//		//	ft.hide(fragment);
//		//}
		ft.commit();
//		initTitleBar("首页");
	}



	/**
	 * 点击之后，更改当前显示的fragment
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId){
			case R.id.main_tab_featured:
				changeTitleBar("首页");
                main_viewpage.setCurrentItem(MAIN_TAB_FEATURED,false);
				break;
			case R.id.main_tab_wallpaper:
				changeTitleBar("社区");
                main_viewpage.setCurrentItem(MAIN_TAB_WALLPAPER,false);
				break;
			case R.id.main_tab_mine:
				changeTitleBar("消息");
                main_viewpage.setCurrentItem(MAIN_TAB_MINE,false);
				break;
			case R.id.main_tab_more:
				changeTitleBar("我的");
                main_viewpage.setCurrentItem(MAIN_TAB_MORE,false);
				break;
		}
	}

    public class FragmentAdapter extends FragmentPagerAdapter {
        private final static int TAB_COUNT = 4;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int id) {
            switch (id) {
                case MAIN_TAB_FEATURED:
                    return mineFragment;
                case MAIN_TAB_WALLPAPER:
                    return mineFragment2;
                case MAIN_TAB_MINE:
                    return mineFragment3;
                case MAIN_TAB_MORE:
                    return mineFragment4;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==100 && resultCode == SettingActivity.RESULT_LOGOUT){
            logout();
        }
    }

    /**
	 * 在fragment切换时需要改变标题栏
	 */
	protected void changeTitleBar(String title) {
		findViewById(R.id.ibtn_title_back).setVisibility(View.GONE);
		findViewById(R.id.iv_title_right).setVisibility(View.GONE);
		if(title.equals("消息") || title.equals("社区")){
			findViewById(R.id.rl_title_bar).setVisibility(View.GONE);
		}else{
			findViewById(R.id.rl_title_bar).setVisibility(View.VISIBLE);
		}

		TextView tvTitle = (TextView) findViewById(R.id.tv_title_text);
		tvTitle.setText(title);

		if(title.equals("我的")){
			findViewById(R.id.iv_title_right).setVisibility(View.VISIBLE);
			ImageView ivTitleRight = (ImageView) findViewById(R.id.iv_title_right);
//		ivTitleRight.setImageResource(R.drawable.ic_setting);测试分支功能
			ivTitleRight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,SettingActivity.class);
					startActivityForResult(intent, 100);
				}
			});
		}

		/**
		 * 文字显示不全，隐藏点击查看
		TextView textview_weibo_name = (TextView) findViewById(R.id.textview_weibo_name);
		String sinaName = textview_weibo_name.getText().toString();
		SpannableString sinaSpanString = new SpannableString(sinaName);
		ClickableSpan clickableSpan1 = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				startActivity(BrowserUtils.getBrowserIntent(mContext, "http://weibo.com/u/5876672114"));
			}
		};
		sinaSpanString.setSpan(clickableSpan1, 0, sinaName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textview_weibo_name.setText(sinaSpanString);
		textview_weibo_name.setMovementMethod(LinkMovementMethod.getInstance());
		 */

	}





	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 是否退出应用
			if (AppContext.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true)) {
				return mExitHelper.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	protected void onStop() {
		super.onStop();
		if (mHandler.hasMessages(MSG_SET_ALIAS)) {
			mHandler.removeMessages(MSG_SET_ALIAS);
		}
		if (mHandler.hasMessages(MSG_SET_TAGS)) {
			mHandler.removeMessages(MSG_SET_TAGS);
		}

	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		unregisterReceiver(receiver);
	}





	/*******以下是从服务器获取标识，病设置给极光推送别名的代码******/

	/**
	 * 获取别名,获取成功后设置别名
	 */
	private void requestAndSetJPushAlias() {
		Api.getPushAlias(getPushAliasHandler);
	}



	private void setJPushAlias(String alias) {
		if (TextUtils.isEmpty(alias)) {
			AppContext.showToast(R.string.error_alias_empty);
			LOG.w(TAG, "[setJPushAlias]alias parament is empty");
			return;
		}
		if (!isValidTagAndAlias(alias)) {
			AppContext.showToast(R.string.error_tag_gs_empty);
			LOG.w(TAG, "[setJPushAlias]alias parament is invalid");
			return;
		}
		// 调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}



	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg){
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				LOG.d(TAG, "[mHandler.handleMessage]Set alias in handler."
						+ msg.obj);
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				LOG.d(TAG, "[mHandler.handleMessage]Set tags in handler.");
				JPushInterface.setAliasAndTags(getApplicationContext(), null,
						(Set<String>) msg.obj, mTagsCallback);
				break;
			default:
				LOG.i(TAG, "[mHandler.handleMessage]Unhandled msg - "
						+ msg.what);
			}
		}
	};


	/**
	 * 设置别名的回调
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		int count = 0;

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				count = 0;
				logs = "Set tag and alias success";
				LOG.i(TAG, "[mAliasCallback.gotResult]:" + logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				LOG.i(TAG, "[mAliasCallback.gotResult]:" + logs);
				count++;
				if (DeviceUtils.checkNetWorkIsOk()
						&& count < 10) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 10);
				} else {
					LOG.i(TAG,
							"[mAliasCallback.gotResult]:No network.set alias fails");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, "[mAliasCallback.gotResult]:" + logs);
			}
		}
	};


	/**
	 * 设置标签的回调
	 */
	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				LOG.i(TAG, "[mTagsCallback.gotResult]" + logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				LOG.i(TAG, "[mTagsCallback.gotResult]" + logs);
				if (DeviceUtils.checkNetWorkIsOk()) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
				} else {
					LOG.i(TAG, "[mTagsCallback.gotResult]:No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				LOG.w(TAG, "[mTagsCallback.gotResult]" + logs);
			}
			AppContext.showToast(logs);
		}

	};



	/**
	 * 校验Tag Alias 只能是数字,英文字母和中文
	 */
	public boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}



	private void setTag(ArrayList<String> tags) {
		// ","隔开的多个 转换成 Set
		Set<String> tagSet = new LinkedHashSet<String>();
		tagSet.addAll(tags);
		for (String tag : tags) {
			if (!isValidTagAndAlias(tag)) {
				AppContext.showToast(R.string.error_tag_gs_empty);
				return;
			}
			tagSet.add(tag);
		}

		// 调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}


}