package com.yikang.app.yikangserver.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.SpinnerListAdapter;
import com.yikang.app.yikangserver.interfaces.CallBack;
import com.yikang.app.yikangserver.interfaces.OCallBack;
import com.yikang.app.yikangserver.utils.ScreenModel;

import java.util.List;

public class TitleView extends RelativeLayout {
	private TextView contentTextView;
	private TextView rightTextView;
	private LinearLayout linear;
	private ImageView leftImageView, rightIcon, contentImage;
	private Context mContext;

	@SuppressWarnings("static-access")
	public TitleView(final Context context) {
		super(context);
		this.mContext = context;
		getWindowWH(mContext);
		scale = ScreenModel.getScreenModel(context).scale;
		density = ScreenModel.getScreenModel(context).density;
		inflate = LayoutInflater.from(context).inflate(R.layout.title, null);
		int h = (windowH * 94 / 1280);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, h);
		inflate.setLayoutParams(params);
		contentTextView = (TextView) inflate.findViewById(R.id.content);
		contentTextView.setTextSize(52 * scale / density);
		rightTextView = (TextView) inflate.findViewById(R.id.right);
		rightTextView.setTextSize(42 * scale / density);
		int w = (windowW * 130 / 720);
		h = (windowH * 60 / 1280);
		params = new LayoutParams(w, h);
		params.addRule(RelativeLayout.CENTER_VERTICAL, R.id.left);
		leftImageView = (ImageView) inflate.findViewById(R.id.left);
		//leftImageView.setLayoutParams(params);
		leftImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				closeSoftInputFromWindow(mContext);
				((Activity) context).onBackPressed();
			}
		});
		contentImage = (ImageView) inflate.findViewById(R.id.contentImage);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) contentImage
				.getLayoutParams();
		linearParams.width = ScreenModel.getScreenModel(getContext()).getPix(
				23f);
		linearParams.height = ScreenModel.getScreenModel(getContext()).getPix(
				14f);
		linear = (LinearLayout) inflate.findViewById(R.id.linear);
		addView(inflate);
	}

	@SuppressWarnings("static-access")
	public TitleView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		getWindowWH(mContext);
		scale = ScreenModel.getScreenModel(context).scale;
		density = ScreenModel.getScreenModel(context).density;
		inflate = LayoutInflater.from(context).inflate(R.layout.title, null);
		int h = (windowH * 94 / 1280);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, h);
		inflate.setLayoutParams(params);
		contentTextView = (TextView) inflate.findViewById(R.id.content);
		contentTextView.setTextSize(52 * scale / density);
		rightTextView = (TextView) inflate.findViewById(R.id.right);
		rightTextView.setTextSize(42 * scale / density);
		int w = (windowW * 130 / 720);
		h = (windowH * 60 / 1280);
		params = new LayoutParams(w, h);
		params.addRule(RelativeLayout.CENTER_VERTICAL, R.id.left);
		leftImageView = (ImageView) inflate.findViewById(R.id.left);
		//leftImageView.setLayoutParams(params);
		leftImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				closeSoftInputFromWindow(mContext);
				((Activity) context).onBackPressed();
			}
		});
		rightIcon = (ImageView) inflate.findViewById(R.id.rightIcon);
		params = (LayoutParams) rightIcon.getLayoutParams();
		params.height = h;
		params.width = w;
		contentImage = (ImageView) inflate.findViewById(R.id.contentImage);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) contentImage
				.getLayoutParams();
		linearParams.width = ScreenModel.getScreenModel(getContext()).getPix(
				34.5f);
		linearParams.height = ScreenModel.getScreenModel(getContext()).getPix(
				21f);
		linear = (LinearLayout) inflate.findViewById(R.id.linear);
		addView(inflate);
		// params = (LayoutParams) contentSpinner.getLayoutParams();
	}

	@SuppressWarnings("static-access")
	public TitleView(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		getWindowWH(mContext);
		scale = ScreenModel.getScreenModel(context).scale;
		density = ScreenModel.getScreenModel(context).density;
		inflate = LayoutInflater.from(context).inflate(R.layout.title, null);
		int h = (windowH * 94 / 1280);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, h);
		inflate.setLayoutParams(params);
		contentTextView = (TextView) inflate.findViewById(R.id.content);
		contentTextView.setTextSize(52 * scale / density);
		rightTextView = (TextView) inflate.findViewById(R.id.right);
		rightTextView.setTextSize(42 * scale / density);
		int w = (windowW * 130 / 720);
		h = (windowH * 60 / 1280);
		params = new LayoutParams(w, h);
		params.addRule(RelativeLayout.CENTER_VERTICAL, R.id.left);
		leftImageView = (ImageView) inflate.findViewById(R.id.left);
		//leftImageView.setLayoutParams(params);
		leftImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				closeSoftInputFromWindow(mContext);
				((Activity) context).onBackPressed();
			}
		});
		contentImage = (ImageView) inflate.findViewById(R.id.contentImage);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) contentImage
				.getLayoutParams();
		linearParams.width = ScreenModel.getScreenModel(getContext()).getPix(
				23f);
		linearParams.height = ScreenModel.getScreenModel(getContext()).getPix(
				14f);
		linear = (LinearLayout) inflate.findViewById(R.id.linear);
		addView(inflate);
	}

	public static Dialog dialog = null;
	private List<String> list;

	// 显示dialog
	public void contentImageClick() {
		Context context = getContext();
		dialog = new Dialog(context);// 创建Dialog并设置样式主�?
		// 声明隐藏title标题，必须在dialog.show();之前声明
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		dialog.setContentView(R.layout.formcustomspinner);
		// 获取窗口
		Window dialogWindow = dialog.getWindow();
		// 设置dialog的背景颜色，也可在布�?��件中设置
		dialogWindow.setBackgroundDrawableResource(R.color.share_view);
		// 获取参数设置
		WindowManager.LayoutParams attributes = dialogWindow
				.getAttributes();
		// 设置dialog的宽高，也可以使用具体的�?
		attributes.width = LayoutParams.MATCH_PARENT;
		attributes.height = LayoutParams.WRAP_CONTENT;
		// 设置dialog的位置，貌似也能在他的位置上再进行参数设置，以前弄过，但是没有找到相关代�?
		attributes.gravity = Gravity.TOP;
		attributes.y = getHeight() - 10;
		// 把刚才的设置set进去
		dialog.getWindow().setAttributes(attributes);
		final ListView listview = (ListView) dialog
				.findViewById(R.id.formcustomspinner_list);
		SpinnerListAdapter adapters = new SpinnerListAdapter(list, context,
				callBack, new CallBack() {

					@Override
					public void callBack() {

					}
				});
		listview.setAdapter(adapters);
		dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
	}

	/** 隐藏软键�?**/
	public void closeSoftInputFromWindow(Context mContext) {
		View view = ((Activity) mContext).getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static int windowH;
	public static int windowW;

	private void getWindowWH(Context context) {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(mDisplayMetrics);
		// ((Activity) context).getWindowManager().getDefaultDisplay()
		// .getMetrics(mDisplayMetrics);
		windowW = mDisplayMetrics.widthPixels;
		windowH = mDisplayMetrics.heightPixels;
	}

	// 屏幕属�?
	private float scale;
	private float density;
	private View inflate;

	public void setContentClick(TitleViewCallBack callBack, List<String> list) {
		this.list = list;
		this.callBack = callBack;
		setContentTextView(this.list.get(0));
		linear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (View.VISIBLE == contentImage.getVisibility()) {
					contentImageClick();
				}
			}
		});
	}

	public void setRightImageViewVisibility(int b) {
		if (b == View.GONE) {
			b = View.INVISIBLE;
		} else if (b != View.INVISIBLE && b != View.VISIBLE)
			b = View.VISIBLE;
		rightIcon.setVisibility(b);
	}

	public TitleViewCallBack callBack;

	public void setContentImageVisibility(int b) {
		if (b == View.GONE) {
			b = View.INVISIBLE;
		} else if (b != View.INVISIBLE && b != View.VISIBLE)
			b = View.VISIBLE;
		contentImage.setVisibility(View.VISIBLE);
	}

	public void setLeftImageViewVisibility(int b) {
		if (b == View.GONE) {
			b = View.INVISIBLE;
		} else if (b != View.INVISIBLE && b != View.VISIBLE)
			b = View.VISIBLE;
		leftImageView.setVisibility(b);
	}

	/**
	 * 设置右面文字是否显示
	 * */
	public void setRightTextViewVisibility(int b) {
		if (b == View.GONE) {
			b = View.INVISIBLE;
		} else if (b != View.INVISIBLE && b != View.VISIBLE)
			b = View.VISIBLE;
		rightTextView.setVisibility(b);
	}

	private OCallBack oCallBack;

	public void setLeftImageViewOnClickListener(OnClickListener clickListener) {
		leftImageView.setOnClickListener(clickListener);
	}

	public void setRightImageViewOnClickListener(OnClickListener clickListener) {
		rightIcon.setOnClickListener(clickListener);
	}

	public void setRightTextViewOnClickListener(OnClickListener clickListener) {
		rightTextView.setOnClickListener(clickListener);
	}

	public void startIntent(final Intent intent) {
		rightTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mContext.startActivity(intent);
			}
		});
	}

	public void setLeftImageView(int id) {
		leftImageView.setImageResource(id);
	}

	public void setContentTextView(String contentTextView) {
		this.contentTextView.setText(contentTextView);
	}

	public void setRightTextView(String rightTextView) {
		this.rightTextView.setText(rightTextView);
	}

}
