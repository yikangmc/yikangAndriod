package com.yikang.app.yikangserver.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.LableChooseGridViewAdapter;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.dialog.AlertDialog;
import com.yikang.app.yikangserver.photo.Bimp;
import com.yikang.app.yikangserver.photo.PhotiAlbumActivity;
import com.yikang.app.yikangserver.photo.PhotoActivity;
import com.yikang.app.yikangserver.photo.PhotoFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布问题页面
 */
public class PublishPostActivity extends BaseActivity {

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private LinearLayout activity_selectimg_send;
    private LinearLayout activity_selectimg_cancle;
    private LinearLayout ll_lable_gv_test;
    private EditText et_title, et_content;
    public static String EXTRA_LABLE_EXAMPLEIDS = "lableexample";
    public static String EXTRA_LABLE_EXAMPLENAME = "lableexampless";
    private List<String> mDatas;
    List<String> list = new ArrayList<String>();
    private TextView mLableGridView;
    private LableChooseGridViewAdapter mLbaleChooseAdapter;
    private static final int LABLE_CHOOSE_REQUEST = 0x000001;
    public static final int LABLE_CHOOSE_RESULT = 0x000002;
    private List<String> pictrue = new ArrayList<String>();//上传的图片集合
    private String title, content;//问题的标题，内容
    private String tagLib="0";
    private String tagLibName;

    public PublishPostActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_selectimg);

        Init();
    }

    public void Init() {
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        et_content = (EditText) findViewById(R.id.et_content);
        et_title = (EditText) findViewById(R.id.et_title);
        et_title.addTextChangedListener(mTextWatcher);

        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(PublishPostActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(PublishPostActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        activity_selectimg_send = (LinearLayout) findViewById(R.id.activity_selectimg_send);
        activity_selectimg_cancle = (LinearLayout) findViewById(R.id.activity_selectimg_cancle);
        activity_selectimg_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog ad = new AlertDialog(PublishPostActivity.this);
                ad.setMessage("确定退出编辑吗？");
                ad.setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bimp.bmp.removeAll(Bimp.bmp);
                        Bimp.drr.removeAll(Bimp.drr);
                        Bimp.max = 0;
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
        });

        activity_selectimg_send.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)PublishPostActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                //关闭软键盘
                imm.hideSoftInputFromWindow(et_title.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);

                /*第一步上传图片
                 *
                 */
                for (int i = 0; i < Bimp.drr.size(); i++) {
                    String Str = Bimp.drr.get(i).substring(
                            Bimp.drr.get(i).lastIndexOf("/") + 1,
                            Bimp.drr.get(i).lastIndexOf("."));
                   // Log.i("debug", "path-->" + Str);
                    list.add(PhotoFileUtils.SDPATH + Str + ".JPEG");
                }
                for (int j = 0; j < list.size(); j++) {
                   // Log.i("debug", "list---------path-->" + list.get(j));
                    //此处是一次上传的，怎么样保证全部上传成功了呢,这个难住我了
                    Api.uploadFile(new File(list.get(j)), uploadPicHandler);
                }

                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                /**
                 * 第二步根据上传图片返回来的地址结果，再提交
                 */
                if (list == null || list.size() == 0) {

                    //问题标题
                    title = et_title.getText().toString().trim();
                    //问题描述
                    content = et_content.getText().toString().trim();
                    //所属标签

                    int[] tag = {Integer.parseInt(tagLib)};
                   // AppContext.showToast(tag[0]+"");
                    //将图片集合转化为图片数组
                    String[] toBeStored = pictrue.toArray(new String[pictrue.size()]);
                    if (tag == null ||tag[0]==0 ) {
                        Toast.makeText(getApplicationContext(), "为问题添加一个合适的标签吧~", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (title == null || "".equals(title)) {
                        Toast.makeText(getApplicationContext(), "请填写您的问题", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showWaitingUI();
                    //发布问题
                    Api.addAnswer(title, content, tag, toBeStored, new ResponseCallback<Void>() {


                        @Override
                        public void onSuccess(Void data) {
                            Toast.makeText(getApplicationContext(), "问题发布成功", Toast.LENGTH_SHORT).show();
                            hideWaitingUI();
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                           // LOG.i("debug", "HpWonderfulContent---->" + data);

                        }

                        @Override
                        public void onFailure(String status, String message) {
                           // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                            AppContext.showToast(message);
                        }
                    });
                }


            }

            private ResponseCallback<FileResponse> uploadPicHandler = new ResponseCallback<FileResponse>() {


                @Override
                public void onSuccess(FileResponse data) {

                    String picStr = data.fileUrl;
                    pictrue.add(picStr);

                  //  LOG.i("debug", "[uploadAvatarHandler]上传图片成功" + picStr);
                    hideWaitingUI();

                    //发布问题
                    if (pictrue.size() == list.size()) {
                        //PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                        //Toast.makeText(getApplicationContext(),pictrue.size()+"",Toast.LENGTH_SHORT).show();
                        //问题标题
                        title = et_title.getText().toString().trim();
                        //问题描述
                        content = et_content.getText().toString().trim();
                        //所属标签
                        int[] tag = {Integer.parseInt(tagLib)};
                        //将图片集合转化为图片数组
                        String[] toBeStored = pictrue.toArray(new String[pictrue.size()]);
                        if (tag == null ||tag.equals("0") ) {
                            Toast.makeText(getApplicationContext(), "为问题添加一个合适的标签吧~", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (title == null || "".equals(title)) {
                            Toast.makeText(getApplicationContext(), "请填写您的问题", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showWaitingUI();
                        //发布问题
                        Api.addAnswer(title, content, tag, toBeStored, new ResponseCallback<Void>() {

                            @Override
                            public void onSuccess(Void data) {
                               // PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                                Bimp.bmp.removeAll(Bimp.bmp);
                                Bimp.drr.removeAll(Bimp.drr);
                                Bimp.max = 0;
                                hideWaitingUI();
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                               // LOG.i("debug", "HpWonderfulContent---->" + data);

                            }

                            @Override
                            public void onFailure(String status, String message) {
                               // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                                AppContext.showToast(message);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String status, String message) {
                    //LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
                    AppContext.showToast("上传图片失败：" + message);

                }
            };

        });

        mLableGridView = (TextView) findViewById(R.id.lable_gv_test);
        ll_lable_gv_test = (LinearLayout) findViewById(R.id.ll_lable_gv_test);
        ll_lable_gv_test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppContext.showToast("选择标签页面");
                Intent intent = new Intent(PublishPostActivity.this, LableChooseActivitys.class);
                //intent.putExtra(LableChooseActivitys.ARG_PROFESSION, 2);
                startActivityForResult(intent, 8);
            }
        });
    }
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//          mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = et_title.getSelectionStart();
            editEnd = et_title.getSelectionEnd();

            if (temp.length() >=100) {
                AppContext.showToast("请输入100字以内的问题标题");
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                et_title.setText(s);
                et_title.setSelection(tempSelection);
            }
        }
    };

    //默认携带过来的标签
    private void initDatas() {
        mDatas = new ArrayList<String>();
        mDatas.add(tagLibName);
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }


    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater; // 视图容器

        private int selectedPosition = -1;// 选中的位置

        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {

            final int coord = position;

            ViewHolder holder = null;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.photo_item_published_grida,
                        parent, false);
                holder = new ViewHolder();

                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 4) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                PhotoFileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
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
            /*
            用相机拍照
             */
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            /*
            相册选取
             */
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PublishPostActivity.this,
                            PhotiAlbumActivity.class);
                    intent.putExtra("where", "wenti");
                    startActivity(intent);
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

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    /**
     * 用相机拍照取照片
     */
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
                if (Bimp.drr.size() < 4 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
                break;
            case 8:
                if (resultCode == RESULT_OK) {
                  /*  List<Expert> mDataExpert = (List<Expert>) data.getSerializableExtra(LableChooseActivity.LABLE_CHOOSE);
                    for (int i = 0; i < mDataExpert.size(); i++) {
                        LOG.i("debug", "mDatas-11111--->>>>>>>>" + mDataExpert.get(i).name);
                        mDatas.add(mDataExpert.get(i).name);
                    }
                    mLbaleChooseAdapter.notifyDataSetChanged();*/
                    tagLib = data.getStringExtra("id");
                    tagLibName = data.getStringExtra("name");
                    initDatas();
                    mLbaleChooseAdapter = new LableChooseGridViewAdapter(PublishPostActivity.this, mDatas);
                    mLableGridView.setText(tagLibName);
                    mLableGridView.setBackgroundResource(R.color.community_activity_tv_color);
                    mLableGridView.setCompoundDrawables(null,null,null,null);
                  //  AppContext.showToast(tagLibName+tagLib);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog ad = new AlertDialog(PublishPostActivity.this);
            ad.setMessage("确定退出编辑吗？");
            ad.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bimp.bmp.removeAll(Bimp.bmp);
                    Bimp.drr.removeAll(Bimp.drr);
                    Bimp.max = 0;
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
}
