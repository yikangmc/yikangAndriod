package com.yikang.app.yikangserver.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
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
import com.yikang.app.yikangserver.bean.Childs;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.dialog.AlertDialog;
import com.yikang.app.yikangserver.photo.Bimp;
import com.yikang.app.yikangserver.photo.PhotoActivity;
import com.yikang.app.yikangserver.photo.PhotoFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布帖子页面
 */
public class PublishLablesActivity extends BaseActivity {

    private GridView noScrollgridviews;
    private GridAdapter adapter;
    private LinearLayout activity_selectimg_send;
    private LinearLayout activity_selectimg_cancle;
    private EditText  et_content;
    List<String> pictrue = new ArrayList<String>();//上传的图片集合
    private List<String> mDatas;
    List<String> list = new ArrayList<String>();
    private String title, content;//帖子的标题，内容
    private GridView mLableGridView;
    private LableChooseGridViewAdapter mLbaleChooseAdapter;
    private static final int LABLE_CHOOSE_REQUEST = 0x000001;
    public static final int LABLE_CHOOSE_RESULT = 0x000002;
    public static String EXTRA_LABLE_ANSWER_CONTENTID = "lableanswercontentid";
    public static String EXTRA_LABLE_EXAMPLENAMES = "lableexampless";
    private String tagId;
    private String tagLibName;
    public PublishLablesActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_publish_lable);
        tagId = getIntent().getStringExtra(EXTRA_LABLE_ANSWER_CONTENTID);
        tagLibName = getIntent().getStringExtra(EXTRA_LABLE_EXAMPLENAMES);
        Init();
    }

    public void Init() {
        noScrollgridviews = (GridView) findViewById(R.id.noScrollgridviews);
        et_content = (EditText) findViewById(R.id.et_content);
        noScrollgridviews.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridviews.setAdapter(adapter);
        noScrollgridviews.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindowss(PublishLablesActivity.this, noScrollgridviews);
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    Intent intent = new Intent(PublishLablesActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
        noScrollgridviews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //AppContext.showToast(position+"");

               // showDeleteDialog(position);

                return true;

            }


        });
        activity_selectimg_send = (LinearLayout) findViewById(R.id.activity_selectimg_send);
        activity_selectimg_cancle = (LinearLayout) findViewById(R.id.activity_selectimg_cancle);
        activity_selectimg_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog ad = new AlertDialog(PublishLablesActivity.this);
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
                for (int k = 0; k < mDatas.size(); k++) {
                   // Log.i("debug", "mDatas-->" + mDatas.get(k));
                }
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                // 完成上传服务器后 .........
                /**
                 * 第二步根据上传图片返回来的地址结果，再提交
                 */
                if (list == null || list.size() == 0) {

                    //问题描述
                    content = et_content.getText().toString().trim();
                    //所属标签
                    int[] tag = {Integer.parseInt(tagId + "")};
                    //将图片集合转化为图片数组
                    String[] toBeStored = pictrue.toArray(new String[pictrue.size()]);

                   /* if (title == null || "".equals(title)) {
                        Toast.makeText(getApplicationContext(), "请填写您的帖子标题", Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                    if (content == null || "".equals(content)) {
                        Toast.makeText(getApplicationContext(), "请填写您的帖子描述", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showWaitingUI();
                    //发布帖子
                    Api.publishContent("", content, tag, toBeStored, publishTieziHandler);

                }
            }

            private ResponseCallback<FileResponse> uploadPicHandler = new ResponseCallback<FileResponse>() {
                @Override
                public void onSuccess(FileResponse data) {
                    String picStr = data.fileUrl;
                    pictrue.add(picStr);
                   // LOG.i("debug", "[uploadAvatarHandler]上传图片成功" + data.fileUrl);
                    hideWaitingUI();
                    //发布帖子
                    if (pictrue.size() == list.size()) {
                        // PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                        //帖子描述
                        content = et_content.getText().toString().trim();
                        //帖子标签
                        int[] tag = {Integer.parseInt(tagId + "")};
                        //将图片集合转化为图片数组
                        String[] toBeStored = pictrue.toArray(new String[pictrue.size()]);
                       /* if (title == null || "".equals(title)) {
                            Toast.makeText(getApplicationContext(), "请填写您的帖子", Toast.LENGTH_SHORT).show();
                            return;
                        }*/
                        if (content == null || "".equals(content)) {
                            Toast.makeText(getApplicationContext(), "请填写您的帖子描述", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showWaitingUI();
                        //发布帖子
                        if(toBeStored==null){
                            toBeStored= new String[]{};
                        }
                        Api.publishContent("", content, tag, toBeStored, publishTieziHandler);
                    }
                }

                @Override
                public void onFailure(String status, String message) {
                    //LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
                    AppContext.showToast("上传图片失败：" + message);
                    // PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                }
            };
            private ResponseCallback<String> publishTieziHandler = new ResponseCallback<String>() {


                @Override
                public void onSuccess(String data) {
                    PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                    Bimp.bmp.removeAll(Bimp.bmp);
                    Bimp.drr.removeAll(Bimp.drr);
                    Bimp.max = 0;
                    hideWaitingUI();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                   // LOG.i("debug", "HpWonderfulContent---->" + data);
                    //ImageLoader.getInstance().displayImage(bannerHp.get(0).getBanerPic(), homepage_iv_banner);
                }

                @Override
                public void onFailure(String status, String message) {
                   // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                    hideWaitingUI();
                    //finish();
                    // PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                    AppContext.showToast(message);
                }
            };
        });

        mLableGridView = (GridView) findViewById(R.id.lable_gv_test);
        initDatas();
        mLbaleChooseAdapter = new LableChooseGridViewAdapter(PublishLablesActivity.this, mDatas);
        mLableGridView.setAdapter(mLbaleChooseAdapter);
        mLableGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               /* if (position == adapterView.getChildCount() - 1) {
                    Intent intent = new Intent(PublishLablesActivity.this, LableChooseActivitys.class);
                    //intent.putExtra(LableChooseActivitys.ARG_PROFESSION, 2);
                    startActivityForResult(intent, LABLE_CHOOSE_REQUEST);
                }*/
            }
        });
       /* mLableGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PublishLablesActivity.this);
                if (position != 0) {
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
        });*/
    }

    private TextView update_notification_content;
    private Dialog dialog;
    private void showDeleteDialog(final int position) {
        dialog = new Dialog(PublishLablesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setContentView(R.layout.dialog_delete);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ViewPager.LayoutParams.MATCH_PARENT;
        attributes.height = ViewPager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;

        dialog.getWindow().setAttributes(attributes);

        dialog.setCanceledOnTouchOutside(true);

       dialog.findViewById(R.id.update_next_time).setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
               //Bimp.bmp.remove(position);
               //Bimp.drr.remove(position);
               //adapter.update();
           }
       });
        dialog.findViewById(R.id.update_now).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


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

    public class PopupWindowss extends PopupWindow {

        public PopupWindowss(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.photo_item_popupwindows, null);
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
                    Intent intent = new Intent(getApplicationContext(),
                            PhotiAlbumActivityTiezi.class);
                    //intent.putExtra("where", "wenti");
                    startActivity(intent);
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

    public  static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/myimages/", String.valueOf(System.currentTimeMillis())
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
            case LABLE_CHOOSE_REQUEST:
                if (resultCode == LABLE_CHOOSE_RESULT) {
                    List<Childs> mDataExpert = (List<Childs>) data.getSerializableExtra("tag");
                    for (int i = 0; i < mDataExpert.size(); i++) {
                       // LOG.i("debug", "mDatas-11111--->>>>>>>>" + mDataExpert.get(i).getTagName());
                        mDatas.add(mDataExpert.get(i).getTagName());
                    }
                    mLbaleChooseAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog ad = new AlertDialog(PublishLablesActivity.this);
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
