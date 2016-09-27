package com.yikang.app.yikangserver.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.yikang.app.yikangserver.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateService extends Service {

	// 下载url
	private String download_url;

	// 文件存储
	private File updateDir;

	private File updateFile;

	// 通知�?
	private NotificationManager nm;

	private Notification notification;

	// 通知栏视�?
	private RemoteViews contentView;

	// 启动活动
	private PendingIntent pendingIntent;

	// 处理通知栏的事件
	private Handler handler = new Handler() {
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Uri uri = Uri.fromFile(updateFile);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				pendingIntent = PendingIntent.getActivity(UpdateService.this,
						0, intent, 0);
				 notification = new Notification.Builder(UpdateService.this)
						.setAutoCancel(true)
						.setContentTitle("佳佳康复")
						.setContentText("下载完成")
						.setContentIntent(pendingIntent)
						.setSmallIcon(R.drawable.ic_launcher)
						.setWhen(System.currentTimeMillis())
						.build();
				/*notification.flags = Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(UpdateService.this, "佳佳康复",
						"下载完成", pendingIntent);*/

				nm.notify(R.layout.update_notification, notification);

				stopSelf();
				handler.sendEmptyMessage(1);
				break;
			case 1:
				Intent intents = new Intent();
				intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intents.setAction(Intent.ACTION_VIEW);
				intents.setDataAndType(Uri.fromFile(updateFile),
						"application/vnd.android.package-archive");
				startActivity(intents);

				break;
			default:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		download_url = intent.getStringExtra("download_url");
		System.out.println(download_url + "服务中的url");
		updateDir = Environment.getExternalStorageDirectory();
		String fileName = intent.getStringExtra("fileName");

		updateFile = new File(updateDir, fileName);

		//Log.e("TAG", "onStartCommand()");
		createNotification();
		createThread();

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 创建通知�?
	 */
	@SuppressWarnings("deprecation")
	private void createNotification() {
		notification = new Notification(R.drawable.ic_launcher,
				"正在下载佳佳康复", System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		contentView = new RemoteViews(getPackageName(),
				R.layout.update_notification);
		contentView.setTextViewText(R.id.notification_update_state, "正在下载佳佳康复");
		contentView.setTextViewText(R.id.notification_update_progress_percent,
				"当前进度:");
		contentView.setProgressBar(R.id.notification_update_progress, 100, 0,
				false);

		notification.contentView = contentView;

		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(R.layout.update_notification, notification);

		//Log.e("TAG", "createNotification" + notification);
	}

	/**
	 * 创建分线程下�?
	 */
	private void createThread() {
		new DownLoadThread().start();
	}

	/**
	 * updateRunnable类的真正实现
	 * 
	 *
	 *
	 */
	private class DownLoadThread extends Thread {

		@Override
		public void run() {

			long downloadSize;
			try {
				downloadSize = downloadUpdateFile(download_url);
				if (downloadSize > 0) {
					// 下载成功
					handler.sendEmptyMessage(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * // 下载Apk的方�?
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public long downloadUpdateFile(String url) throws IOException {
		int down_step = 1;// 提示step
		int totalSize;// 文件总大�?
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大�?

		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.connect();

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(updateFile));
		InputStream is = null;

		// 获取下载文件的size
		totalSize = conn.getContentLength();

		if (conn.getResponseCode() == 200) {
			is = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int len = -1;

			while ((len = is.read(buffer)) > 0) {
				bos.write(buffer, 0, len);

				// 时时获取下载到的大小
				downloadCount += len;

				// 每次增张1%
				if (updateCount == 0
						|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
					updateCount += down_step;

					// 改变通知�?
					contentView.setTextViewText(
							R.id.notification_update_progress_percent, "正在下载"
									+ updateCount + "%");
					contentView.setProgressBar(
							R.id.notification_update_progress, 100,
							updateCount, false);

					notification.contentView = contentView;
					nm.notify(R.layout.update_notification, notification);
				}
			}
		}
		bos.close();
		is.close();
		conn.disconnect();

		return downloadCount;
	}
}
