package com.yikang.app.yikangserver.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.interfaces.OnCameraInterfaces;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class CommonUtils {

	/**
	 * 是否存在sd卡
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private static Dialog dialog;

	public static final int PHOTO_REQUEST_CAMERA = 11;


	public static final int PHOTO_REQUEST_GALLERY = 12;


	public static final int PHOTO_REQUEST_CUT = 13;

	public static final int PHOTO_REQUEST_CUTS = 14;

	public static final String PHOTO_FILE_NAME = "temp_photos.png";

	public static void showCameraDialog(Context mContext,
			final OnCameraInterfaces onCamera) {
		Button bt_cancel, bt_head_photos, bt_head_camera;

		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		dialog.setContentView(R.layout.dialog_camera);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setBackgroundDrawableResource(R.color.transparent);
		android.view.WindowManager.LayoutParams attributes = dialogWindow
				.getAttributes();
		attributes.width = LayoutParams.MATCH_PARENT;
		attributes.height = LayoutParams.WRAP_CONTENT;
		attributes.gravity = Gravity.BOTTOM;
		dialog.getWindow().setAttributes(attributes);
		bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
		bt_head_photos = (Button) dialog.findViewById(R.id.bt_photos);
		bt_head_camera = (Button) dialog.findViewById(R.id.bt_camera);
		bt_head_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				onCamera.openCamera(v);
			}
		});
		bt_head_photos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				onCamera.openPhone(v);
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				onCamera.cancle(v);
			}
		});
	}

	public static String passwordFilter(String str)
			throws PatternSyntaxException {
		String regEx = "[^a-zA-Z0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String stringFilter(String str) throws PatternSyntaxException {

		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}


	public static String getIMEI(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String szImei = TelephonyMgr.getDeviceId();
		return szImei;
	}

	/**
	 * ɾ����ʱ�ļ��ķ���
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ�����˼;
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					deleteFile(files[i]); // ��ÿ���ļ� ������������е��
				}
			}
			file.delete();
		} else {
		}
	}


	/** ��������� **/
	public static void closeSoftInputFromWindow(Context mContext) {
		View view = ((Activity) mContext).getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) ((Activity) mContext)
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}


}
