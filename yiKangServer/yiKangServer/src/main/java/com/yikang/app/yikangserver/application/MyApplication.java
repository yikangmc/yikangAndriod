package com.yikang.app.yikangserver.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;
import java.util.List;

;

public class MyApplication extends Application {
	private static MyApplication mInstance = null;
	private static Context context;
	private List<Activity> activityList = new LinkedList<Activity>();
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		mInstance = MyApplication.this;
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);
		ImageLoader.getInstance().init(configuration);

	}


	public static Context getContext() {
		return context;
	}
	//单例模式中获取唯一的MyApplication实例
	public static MyApplication getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new MyApplication();
		}
		return mInstance;
	}
	//添加Activity到容器中
	public void addActivity(Activity activity)
	{
		activityList.add(activity);
	}
	//遍历所有Activity并finish
	public void exit()
	{
		for(Activity activity:activityList)
		{
			activity.finish();
		}
		System.exit(0);
	}
}
