package com.yikang.app.yikangserver.base;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.ui.SystemBarTintManager;
import com.yikang.app.yikangserver.view.CustomWatingDialog;

/**
 * Created by Tommy on 15/3/12.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

//	protected Context mContext;
//	protected Activity mActivity;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mContext = this;
//		mActivity = this;
//		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
////		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
////		MobclickAgent.onPageEnd(getClass().getSimpleName());
////		MobclickAgent.onPause(this);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
////		MobclickAgent.onPageStart(getClass().getSimpleName());
////		MobclickAgent.onResume(this);
//	}
//
//	@Override
//	public void finish() {
//		super.finish();
//	}
//
//	@Override
//	public void startActivity(Intent intent) {
//		super.startActivity(intent);
//	}
//
//	@Override
//	public void setContentView(int layoutResID) {
//		super.setContentView(layoutResID);
//		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			// getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}
//
//		if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
////			View v = findViewById(R.id.root_layout);
////			if (v != null) {
////				v.setFitsSystemWindows(true);
////			}
////			View v2 = findViewById(R.id.umeng_comm_main_container);
////			if (v2 != null) {
////				v2.setFitsSystemWindows(true);
////			}
////			View v3 = findViewById(R.id.umeng_comm_feed_detail_root);
////			if (v3 != null) {
////				v3.setFitsSystemWindows(true);
////			}
//		}
//
//
//		if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
////            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
//	}

    @TargetApi(19)
    protected void initContent() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            initStatusBar(getResources().getColor(R.color.statue_bar_color));
        }
        setContentView();
        getData();
        findViews();
        initViewContent();
    }

    @TargetApi(19)
    protected void initStatusBar(int color) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setTintColor(color);
        }
    }

    /**
     * 初始化Views
     */
    protected abstract void findViews();

    /**
     * 初始化contentView
     */
    protected abstract void setContentView();

    /**
     * 获取数据，
     */
    protected abstract void getData();


    /**
     * 展示内容
     */
    protected abstract void initViewContent();

    /**
     * 初始化titlebar，BaseActivity中此方法做了两件事 1.将左上角返回按钮设置点击后返回 2.设置标题为参数title
     *
     * 如果子类有不同的布局，或者要做更多的事情，请重写这个方法
     *
     * @param title  要设置的标题
     */
    protected void initTitleBar(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title_text);
        if (tvTitle != null) // 由于设计上的问题，并不是所有的Activity布局中都有统一的titleBar
            tvTitle.setText(title);

        ImageButton btBack = (ImageButton) findViewById(R.id.ibtn_title_back);
        if (btBack != null) // 设置返回监听
            btBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    private Dialog waitingDialog;

    public void showWaitingUI() {
        showWaitingUI(getString(R.string.waiting_loading));
    }

    /**
     * 显示等待的dialog
     */
    public void showWaitingUI(String message) {
        if (waitingDialog == null) {
            waitingDialog = createDialog(message);
        }
        if (!waitingDialog.isShowing())
            waitingDialog.show();
    }

    /**
     * dismiss 等待的dialog
     */
    public void hideWaitingUI() {
        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }
    }


    /**
     * 创建一个dialog，同过复写这个方法可以创建其他样式的dialog
     */
    protected Dialog createDialog(String message) {
        return new CustomWatingDialog(this, message);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideWaitingUI();
    }
}
