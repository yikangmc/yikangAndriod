package com.yikang.app.yikangserver.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 获取屏幕尺寸
 * 
 * @author haoxiaodong
 */
public class ScreenModel {

	public final float DEFAULT_HEIGHT_1920 = 1920;
	public final float DEFAULT_HEIGHT_1280 = 1280;
	public int screenHeight;
	public int screenWidth;
	public static float scale;
	public static float density;
	private static ScreenModel model = null;
	private static float scale_1280;

	private ScreenModel(Context context) {

		DisplayMetrics metric = new DisplayMetrics();
		((WindowManager) context.getSystemService(context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(metric);
		density = metric.density;
		Display display = ((WindowManager) context
				.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
		int w, h;
		w = display.getWidth();
		screenWidth = w;
		h = display.getHeight();
		screenHeight = h;
		scale = h / DEFAULT_HEIGHT_1920;
		scale_1280 = h / 1280f;
	}

	public static ScreenModel getScreenModel(Context context) {
		if (model == null) {
			model = new ScreenModel(context);
		}
		return model;
	}

	public static int getPix(float origin) {
		return (int) (origin * scale_1280);
	}

	public static int getTextSize(float origin) {
		return (int) (origin * scale_1280 / density);
	}

}
