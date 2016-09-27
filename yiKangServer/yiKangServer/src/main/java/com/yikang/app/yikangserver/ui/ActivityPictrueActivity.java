package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.interfaces.OnCameraInterfaces;
import com.yikang.app.yikangserver.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ActivityPictrueActivity extends BaseActivity {
    public final static String MESSAGE_INFOS = "messageinfo";
    private String messageinfo;
    private TextView tv_title_right_text;
    private Button bt_choose_pictrue;
    private Bitmap drawable = null;
    private ImageView iv_activity_pictrue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
        messageinfo = getIntent().getStringExtra(MESSAGE_INFOS);
        initTitleBar(messageinfo);
    }

    @Override
    protected void findViews() {
        tv_title_right_text = (TextView) findViewById(R.id.tv_title_right_text);

        bt_choose_pictrue = (Button) findViewById(R.id.bt_choose_pictrue);
        iv_activity_pictrue = (ImageView) findViewById(R.id.iv_activity_pictrue);
        tv_title_right_text.setText("完成");
        tv_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawable!=null) {
                    File fileBody = getFileBody(drawable, "activityPictrue" + ".png");
                    System.out.println(fileBody + "我看看");
                    showWaitingUI();
                    Api.uploadFile(fileBody, activityPictrueHandler);
                }else{
                    Toast.makeText(getApplicationContext(),"请上传图片",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_choose_pictrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showCameraDialog(ActivityPictrueActivity.this,
                        new OnCameraInterfaces() {
                            @Override
                            public void openPhone(View v) {
                                PackageManager pm = getPackageManager();
                                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                                        pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "com.yikang.app.yikangserver"));
                                if (permission) {

                                }else {
                                    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
                                    int permsRequestCode = 200;
                                    ActivityCompat.requestPermissions(ActivityPictrueActivity.this, perms, permsRequestCode);
                                }
                                System.out.println("");
                                File tempFile = new File(Environment
                                        .getExternalStorageDirectory(),
                                        CommonUtils.PHOTO_FILE_NAME);
                                if (tempFile.exists())
                                    tempFile.delete();
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent,
                                        CommonUtils.PHOTO_REQUEST_GALLERY);
                            }

                            @Override
                            public void openCamera(View v) {
                                PackageManager pm = getPackageManager();
                                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                                        pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "com.yikang.app.yikangserver"));
                                if (permission) {

                                }else {
                                    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
                                    int permsRequestCode = 200;
                                    ActivityCompat.requestPermissions(ActivityPictrueActivity.this, perms, permsRequestCode);
                                }
                                Intent intent = new Intent(
                                        "android.media.action.IMAGE_CAPTURE");
                                // 判断存储卡是否可以用，可用进行存储
                                File tempFile = new File(Environment
                                        .getExternalStorageDirectory(),
                                        CommonUtils.PHOTO_FILE_NAME);

                                if (CommonUtils.hasSdcard()) {
                                    intent.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(
                                                    Environment
                                                            .getExternalStorageDirectory(),
                                                    CommonUtils.PHOTO_FILE_NAME)));
                                }
                                startActivityForResult(intent,
                                        CommonUtils.PHOTO_REQUEST_CAMERA);
                            }

                            @Override
                            public void cancle(View v) {

                            }
                        });
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_activity_pictrue);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }


    private ResponseCallback<FileResponse> activityPictrueHandler = new ResponseCallback<FileResponse>() {


        @Override
        public void onSuccess(FileResponse data) {

            String picStr = data.fileUrl;
            //LOG.i("debug", "[uploadAvatarHandler]上传图片成功" + picStr);
            hideWaitingUI();
            if(picStr.equals("")) {
                Toast.makeText(getApplicationContext(), "上传图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("ActivityPictrues", picStr);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("上传图片失败：" + message);

        }
    };

    /**
     * 存储图片的临时文件夹
     */
    private File tempFile;

    /**
     * 结果的回调事件
     */
    String strCode;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonUtils.PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        }

        if (requestCode == CommonUtils.PHOTO_REQUEST_CAMERA) {
            if (CommonUtils.hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        CommonUtils.PHOTO_FILE_NAME);
                if (tempFile.isFile())
                    crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(ActivityPictrueActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        if (requestCode == CommonUtils.PHOTO_REQUEST_CUT) {
            //if(drawable!=null) {
                drawable = data.getParcelableExtra("data");

                this.iv_activity_pictrue.setImageBitmap(drawable);
            //}
        }


    }

    /**
     * 剪裁图片方法
     *
     * @param uri
     */
    protected void crop(Uri uri) {
        // TODO Auto-generated method stub
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，3：2
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
       // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 1000);
        intent.putExtra("outputY", 500);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, CommonUtils.PHOTO_REQUEST_CUT);
    }


    /**
     * 得到文件体
     *
     * @param bitmap
     * @param filePath
     * @return
     */
    public File getFileBody(Bitmap bitmap, String filePath) {
        File file2 = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + filePath);
        try {
            FileOutputStream out = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
        } catch (FileNotFoundException e1) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return file2;
    }


}
