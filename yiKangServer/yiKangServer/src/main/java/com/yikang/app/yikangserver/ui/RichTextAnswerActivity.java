package com.yikang.app.yikangserver.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.dialog.AlertDialog;
import com.yikang.app.yikangserver.interfaces.OnCameraInterfaces;
import com.yikang.app.yikangserver.utils.CommonUtils;
import com.yikang.app.yikangserver.utils.FileUtils;
import com.yikang.app.yikangserver.view.InterceptLinearLayout;
import com.yikang.app.yikangserver.view.RichTextEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class RichTextAnswerActivity extends BaseActivity implements OnClickListener {
    private final int REQUEST_CODE_CAPTURE_CAMEIA = 100;
    private final int REQUEST_CODE_PICK_IMAGE = 200;
    private final String TAG = "RichTextActivity";
    private Context context;
    private LinearLayout ll_bianji,line_rootView, line_addImg,line_Bottom,ll_pictrue,ll_expression;
    private InterceptLinearLayout line_intercept;
    private RichTextEditor richText;
    private EditText et_name;
    private TextView tv_back, tv_title, tv_ok,lable_gv_test,tv_publish_recogpic,tv_publish_recogpic_yishangchuan,tv_publish_recogpic_shangchuanfail;
    private boolean isKeyBoardUp, isEditTouch;// 判断软键盘的显示与隐藏
    private File mCameraImageFile;// 照相机拍照得到的图片
    private FileUtils mFileUtils;
    private String ROLE = "add";// 当前页面是新增还是查看详情 add/modify
    private String tagLibIds;
    private String tagLibNames;
    String picStr;
    String recogpicStr;
    private String title;
    private Bitmap drawable = null;
    private ArrayList<String> images=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.richtext_answer);
        tagLibIds=getIntent().getStringExtra("tagLibId");
        tagLibNames=getIntent().getStringExtra("tagLibName");
        context = this;
        init();
    }


    private void init() {
        if (getIntent() != null)
            ROLE = getIntent().getStringExtra("role");

        mFileUtils = new FileUtils(context);
        ll_bianji=(LinearLayout)findViewById(R.id.ll_bianji);
        line_addImg = (LinearLayout) findViewById(R.id.line_addImg);
        ll_pictrue = (LinearLayout) findViewById(R.id.ll_pictrue);
        ll_expression = (LinearLayout) findViewById(R.id.ll_expression);
        line_Bottom = (LinearLayout) findViewById(R.id.line_Bottom);
        line_rootView = (LinearLayout) findViewById(R.id.line_rootView);
        line_intercept = (InterceptLinearLayout) findViewById(R.id.line_intercept);

        tv_publish_recogpic = (TextView) findViewById(R.id.tv_publish_recogpic);
        tv_publish_recogpic_yishangchuan = (TextView) findViewById(R.id.tv_publish_recogpic_yishangchuan);
        tv_publish_recogpic_shangchuanfail = (TextView) findViewById(R.id.tv_publish_recogpic_shangchuanfail);
        tv_publish_recogpic.setOnClickListener(this);
        ll_bianji.setOnClickListener(this);
        lable_gv_test = (TextView) findViewById(R.id.lable_gv_test);
        lable_gv_test.setText(tagLibNames);
        lable_gv_test.setBackgroundResource(R.color.community_activity_tv_color);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_name = (EditText) findViewById(R.id.et_name);
        richText = (RichTextEditor) findViewById(R.id.richText);
        richText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.showToast("点击了");
            }
        });
        initRichEdit();
        if ("modify".equals(ROLE)) {
            tv_ok.setText("修改");
            tv_title.setText("查看详情");
            line_intercept.setIntercept(true);
            richText.setIntercept(true);
            getData();
        } else {
            tv_ok.setText("发布");
            tv_title.setText("我来回答");
        }
    }

    private void initRichEdit() {
        ImageView img_addPicture, img_takePicture;
        img_addPicture = (ImageView) line_addImg
                .findViewById(R.id.img_addPicture);
        img_addPicture.setOnClickListener(this);
        ll_pictrue.setOnClickListener(this);
        ll_expression.setOnClickListener(this);
        img_takePicture = (ImageView) line_addImg
                .findViewById(R.id.img_takePicture);
        img_takePicture.setOnClickListener(this);

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isEditTouch = false;
                    line_addImg.setVisibility(View.GONE);
                    line_Bottom.setVisibility(View.VISIBLE);

                }
            }

        });
        richText.setLayoutClickListener(new RichTextEditor.LayoutClickListener() {
            @Override
            public void layoutClick() {
                isEditTouch = true;
                line_addImg.setVisibility(View.GONE);
//                line_Bottom.setVisibility(View.GONE);
            }
        });

        line_rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = line_rootView.getRootView()
                                .getHeight() - line_rootView.getHeight();
                        if (isEditTouch) {
                            if (heightDiff > 500) {// 大小超过500时，一般为显示虚拟键盘事件,此判断条件不唯一
                                isKeyBoardUp = true;
                                line_addImg.setVisibility(View.GONE);
                                ll_bianji.setVisibility(View.GONE);
//                                line_Bottom.setVisibility(View.GONE);
                            } else {
                                if (isKeyBoardUp) {
                                    isKeyBoardUp = false;
                                    isEditTouch = false;
                                    line_addImg.setVisibility(View.GONE);
                                    ll_bianji.setVisibility(View.VISIBLE);
//                                    line_Bottom.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setContentView() {

    }

    protected void getData() {
        et_name.setText("模拟几条数据");
        richText.insertText("第一行");
        richText.insertText("接下来是张图片-王宝强");
        richText.insertImageByURL("http://baike.soso.com/p/20090711/20090711100323-24213954.jpg");
        richText.insertText("下面是一副眼镜");
        richText.insertImageByURL("http://img4.3lian.com/sucai/img6/230/29.jpg");
        richText.insertImageByURL("http://pic9.nipic.com/20100812/3289547_144304019987_2.jpg");
        richText.insertText("上面是一个树妖");
        richText.insertText("最后一行");
    }

    @Override
    protected void initViewContent() {

    }

    private void openCamera() {
        try {
            File PHOTO_DIR = new File(mFileUtils.getStorageDirectory());
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录

            mCameraImageFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCameraImageFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
        }
    }

    private Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy_MM_dd_HH_mm_ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            richText.insertImage(mFileUtils.getFilePathFromUri(uri));
            File file=new File(mFileUtils.getFilePathFromUri(uri));
            Api.uploadFile(file, uploadPicHandler);
        }
        if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA
                && resultCode == Activity.RESULT_OK) {
            richText.insertImage(mCameraImageFile.getAbsolutePath());
            Api.uploadFile(mCameraImageFile, uploadPicHandler);
        }
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
                Toast.makeText(RichTextAnswerActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        if (requestCode == CommonUtils.PHOTO_REQUEST_CUT) {
            drawable = data.getParcelableExtra("data");


            File fileBody = getFileBody(drawable, "photo" + ".png");
            System.out.println(fileBody + "我看看");
            showWaitingUI();
            Api.uploadFile(fileBody, photoHandler);

        }


    }
    private ResponseCallback<String> publishProfessionalContentHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
           // AppContext.showToast("发布成功" );

            hideWaitingUI();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
            AppContext.showToast("发布失败：" + message);

        }
    };

    private ResponseCallback<FileResponse> photoHandler = new ResponseCallback<FileResponse>() {


        @Override
        public void onSuccess(FileResponse data) {

            recogpicStr = data.fileUrl;
            //LOG.i("debug", "[uploadAvatarHandler]上传图片成功" + recogpicStr);
            hideWaitingUI();
            if (recogpicStr.equals("")) {
                Toast.makeText(getApplicationContext(), "上传图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            tv_publish_recogpic_yishangchuan.setVisibility(View.VISIBLE);
            tv_publish_recogpic_shangchuanfail.setVisibility(View.GONE);

        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("上传图片失败：" + message);
            tv_publish_recogpic_yishangchuan.setVisibility(View.GONE);
            tv_publish_recogpic_shangchuanfail.setVisibility(View.VISIBLE);
        }
    };
    private ResponseCallback<FileResponse> uploadPicHandler = new ResponseCallback<FileResponse>() {


        @Override
        public void onSuccess(FileResponse data) {

            picStr = data.fileUrl;
            images.add(picStr);
           // LOG.i("debug", "[uploadAvatarHandler]上传图片成功" + picStr);
            AppContext.showToast("上传图片成功" );
            hideWaitingUI();

        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("上传图片失败：" + message);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFileUtils.deleteRichTextImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:

                if ("修改".equals(tv_ok.getText())) {
                    tv_ok.setText("提交");
                    line_intercept.setIntercept(false);
                    richText.setIntercept(false);
                } else {
                    //问题标题
                    title = et_name.getText().toString().trim();
                    int[] tagLib={Integer.parseInt(tagLibIds)};
                    String[] str = {picStr};
                    String bodyStr="";
                    String richTextStrs="";
                    int k=0;
                    for (int i = 0; i < richText.buildEditData().size(); i++) {

                        if(richText.buildEditData().get(i).getInputStr()!=null){
                            String richTextStr=richText.buildEditData().get(i).getInputStr();
                            bodyStr+="<p>"+richTextStr;
                            bodyStr+="</p>";
                            richTextStrs+=richTextStr;
                        }
                        if(richText.buildEditData().get(i).getImagePath()!=null){

                            bodyStr+="<p><img alt=\\\"\\\" src=";
                            bodyStr+='"'+images.get(k)+'"';
                            bodyStr+="/></p>";
                            k+=1;
                        }

                    }

                    Log.i(TAG, "---richtext-data:" + bodyStr);

                    if ("<p></p>".equals(bodyStr)) {

                        AppContext.showToast("请填写主要内容");
                        return;
                    }
                    showWaitingUI();
                  Api.addAnswerQuestionDetail(Integer.parseInt(tagLibIds),richTextStrs,bodyStr,publishProfessionalContentHandler);
                }
                break;
            case R.id.tv_back:
                final AlertDialog ad = new AlertDialog(RichTextAnswerActivity.this);
                ad.setMessage("确定退出编辑吗？");
                ad.setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                });
                ad.setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });

                break;

            case R.id.ll_bianji:
                Intent intents = new Intent();
                intents.setClass(this, ScancodeActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intents.putExtra("type","answers");
                this.startActivityForResult(intents, 201);
                finish();
                break;
            case R.id.tv_publish_recogpic:

                choosePhoto();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                break;

            case R.id.img_addPicture:
                // 打开系统相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.ll_pictrue:
                new PopupWindows(RichTextAnswerActivity.this,ll_pictrue);
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.img_takePicture:
                // 打开相机
                openCamera();
                break;
            case R.id.ll_expression:
                // 打开相机
               Toast.makeText(getApplicationContext(),"表情包开发中",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void choosePhoto() {
        CommonUtils.showCameraDialog(RichTextAnswerActivity.this,
                new OnCameraInterfaces() {
                    @Override
                    public void openPhone(View v) {
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

    /**
     * 选择照片的popwindow
     */
    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.photo_item_popupwindows, null);
//			view.startAnimation(AnimationUtils.loadAnimation(mContext,
//					R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            /*
            用相机拍照
             */
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    openCamera();
                    dismiss();
                }
            });
            /*
            相册选取
             */
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");// 相片类型
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                    dismiss();
                }
            });
            /*
            取消操作
             */
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog ad = new AlertDialog(RichTextAnswerActivity.this);
            ad.setMessage("确定退出编辑吗？");
            ad.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(View v) {

                    ad.dismiss();
                    finish();
                }
            });
            ad.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 存储图片的临时文件夹
     */
    private File tempFile;

    /**
     * 结果的回调事件
     */
    String strCode;


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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);
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
