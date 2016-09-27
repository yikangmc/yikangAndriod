package com.yikang.app.yikangserver.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.utils.CachUtils;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends BaseActivity {
	private Handler mhHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContent();
		mhHandler = new Handler();

		mhHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startNextPage();
				finish();
			}
		}, 3000);

	}

	@Override
	protected void onResume() {
		super.onResume();
		/*Intent stateService =  new Intent (this,LogService.class);
		startService( stateService );*/
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@TargetApi(19)
	protected void initContent() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			initStatusBar(getResources().getColor(R.color.transparent));
		}
		setContentView();
		findViews();
		getData();
		initViewContent();
	}

	private void startNextPage() {
		if (AppContext.getAppContext().getAccessTicket() == null) {
			// 判断是否进入过主页面
			boolean isStartMained = CachUtils.getBoolean(SplashActivity.this,
					LoginActivity.START_MAIN);
			if (isStartMained) {
				// 进入主页面
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			} else {
				// 如果没有进入，就进入引导页面
				Intent intent = new Intent(SplashActivity.this,
						GuideActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}

		}else {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		}
	}


	@Override
	protected void findViews() {
		
	}


	@Override
	protected void setContentView() {
		ImageView img = new ImageView(this);
		img.setImageResource(R.drawable.start_background);
		img.setScaleType(ImageView.ScaleType.CENTER_CROP);
		setContentView(img);
	}

	@Override
	protected void getData() {}
	@Override
	protected void initViewContent() {}
}
