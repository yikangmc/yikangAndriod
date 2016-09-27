package com.yikang.app.yikangserver.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.LableChooseGridViewAdapter;
import com.yikang.app.yikangserver.bean.Expert;
import com.yikang.app.yikangserver.dialog.AlertDialog;
import com.yikang.app.yikangserver.photo.Bimp;
import com.yikang.app.yikangserver.photo.PhotoFileUtils;
import com.yikang.app.yikangserver.utils.DeviceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProfessionPostActivity extends Activity {

    private TextView activity_selectimg_send;

    private List<String> mDatas;
    private GridView mLableGridView;
    private LableChooseGridViewAdapter mLbaleChooseAdapter;
    private static final int LABLE_CHOOSE_REQUEST = 0x000001;
    public static final int LABLE_CHOOSE_RESULT = 0x000002;
    private static final int IMAGE_CHOOSE_REQUEST = 0x000003;
    public static final int IMAGE_CHOOSE_RESULT = 0x000004;
    private ImageView profession_iv_upload_photo,profession_iv_titlephoto ;
    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession_posting);
        mContext=ProfessionPostActivity.this;
        Init();
    }

    public void Init() {
        activity_selectimg_send = (TextView) findViewById(R.id.activity_profession_tv_send);
        activity_selectimg_send.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < Bimp.drr.size(); i++) {
                    String Str = Bimp.drr.get(i).substring(
                            Bimp.drr.get(i).lastIndexOf("/") + 1,
                            Bimp.drr.get(i).lastIndexOf("."));
                   // Log.i("debug", "path-->" + Str);
                    list.add(PhotoFileUtils.SDPATH + Str + ".JPEG");
                }
                for (int j = 0; j < list.size(); j++) {
                   // Log.i("debug", "list---------path-->" + list.get(j));
                }
                for (int k = 0; k < mDatas.size(); k++) {
                   // Log.i("debug", "mDatas-->" + mDatas.get(k));
                }
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
            }
        });

        mLableGridView = (GridView) findViewById(R.id.lable_gv_test);
        initDatas();
        mLbaleChooseAdapter = new LableChooseGridViewAdapter(ProfessionPostActivity.this, mDatas);
        mLableGridView.setAdapter(mLbaleChooseAdapter);
        mLableGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == adapterView.getChildCount() - 1) {
                    Intent intent = new Intent(ProfessionPostActivity.this, LableChooseActivity.class);
                    intent.putExtra(LableChooseActivity.ARG_PROFESSION, 2);
                    startActivityForResult(intent, LABLE_CHOOSE_REQUEST);
                }
            }
        });
        mLableGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfessionPostActivity.this);
                if (position!=0){
                    builder.setTitle(R.string.dialog_title_prompt)
                            .setMessage("确定删除该标签？")
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.confirm,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            mDatas.remove(position);
                                            mLbaleChooseAdapter.notifyDataSetChanged();
                                        }
                                    }).create().show();
                }
                return false;
            }
        });

        profession_iv_titlephoto= (ImageView) findViewById(R.id.profession_iv_titlephoto);
        profession_iv_upload_photo=(ImageView)findViewById(R.id.profession_iv_upload_photo);
        profession_iv_upload_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupWindows(ProfessionPostActivity.this, profession_iv_upload_photo);
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<String>();
        mDatas.add("运动康复");
    }


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

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
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
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                    startActivityForResult(intent, IMAGE_CHOOSE_REQUEST);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/myimage/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (path!=null){
                    startPhotoZoom(Uri.fromFile(new File(path)));
                }
                break;
            case LABLE_CHOOSE_REQUEST:
                if (resultCode == LABLE_CHOOSE_RESULT) {
                    List<Expert> mDataExpert = (List<Expert>) data.getSerializableExtra(LableChooseActivity.LABLE_CHOOSE);
                    for (int i = 0; i < mDataExpert.size(); i++) {
                       // LOG.i("debug", "mDatas-11111--->>>>>>>>" + mDataExpert.get(i).name);
                        mDatas.add(mDataExpert.get(i).name);
                    }
                    mLbaleChooseAdapter.notifyDataSetChanged();
                }
                break;
            case IMAGE_CHOOSE_REQUEST:
                Uri uri = data.getData();
                //Log.e("uri", uri.toString());
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                   /* 将Bitmap设定到ImageView */
                    profession_iv_titlephoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                   // Log.e("Exception", e.getMessage(),e);
                }
                break;
            case IMAGE_CHOOSE_RESULT:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 -
                    // 100)压缩文件
                    profession_iv_titlephoto.setImageBitmap(photo);
                }
                break;
        }
    }

    public void startPhotoZoom(Uri uri) {
        int deviceWidth = DeviceUtils.getDeviceWidth(mContext);
        int deviceHeight = DeviceUtils.getDeviceHeight(mContext);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽�?
//        intent.putExtra("outputX", deviceWidth);
//        intent.putExtra("outputY", deviceHeight);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, IMAGE_CHOOSE_RESULT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final AlertDialog ad = new AlertDialog(ProfessionPostActivity.this);
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
        return super.onKeyDown(keyCode, event);
    }
}
