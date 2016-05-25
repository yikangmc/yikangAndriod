package com.yikang.app.yikangserver.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.LableChooseGridViewAdapter;
import com.yikang.app.yikangserver.bean.Expert;
import com.yikang.app.yikangserver.dialog.AlertDialog;
import com.yikang.app.yikangserver.photo.Bimp;
import com.yikang.app.yikangserver.photo.PhotiAlbumActivity;
import com.yikang.app.yikangserver.photo.PhotoActivity;
import com.yikang.app.yikangserver.photo.PhotoFileUtils;
import com.yikang.app.yikangserver.utils.LOG;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布帖子页面
 */
public class PublishPostActivity extends Activity {

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private LinearLayout activity_selectimg_send;
    private LinearLayout activity_selectimg_cancle;

    private List<String> mDatas;
    private GridView mLableGridView;
    private LableChooseGridViewAdapter mLbaleChooseAdapter;
    private static final int LABLE_CHOOSE_REQUEST = 0x000001;
    public static final int LABLE_CHOOSE_RESULT = 0x000002;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_selectimg);
        Init();
    }

    public void Init() {
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
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
                finish();
            }
        });
        activity_selectimg_send.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"发布",Toast.LENGTH_SHORT).show();
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < Bimp.drr.size(); i++) {
                    String Str = Bimp.drr.get(i).substring(
                            Bimp.drr.get(i).lastIndexOf("/") + 1,
                            Bimp.drr.get(i).lastIndexOf("."));
                    Log.i("debug", "path-->" + Str);
                    list.add(PhotoFileUtils.SDPATH + Str + ".JPEG");
                }
                for (int j = 0; j < list.size(); j++) {
                    Log.i("debug", "list---------path-->" + list.get(j));
                }
                for (int k = 0; k < mDatas.size(); k++) {
                    Log.i("debug", "mDatas-->" + mDatas.get(k));
                }
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
            }
        });

        mLableGridView = (GridView) findViewById(R.id.lable_gv_test);
        initDatas();
        mLbaleChooseAdapter = new LableChooseGridViewAdapter(PublishPostActivity.this, mDatas);
        mLableGridView.setAdapter(mLbaleChooseAdapter);
        mLableGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == adapterView.getChildCount() - 1) {
//                    mDatas.add("康复哈哈");
//                    mLbaleChooseAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(PublishPostActivity.this, LableChooseActivity.class);
                    intent.putExtra(LableChooseActivity.ARG_PROFESSION, 2);
                    startActivityForResult(intent, LABLE_CHOOSE_REQUEST);
                }
            }
        });
        mLableGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PublishPostActivity.this);
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
    }

    private void initDatas() {
        mDatas = new ArrayList<String>();
        mDatas.add("运动康复");
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
                    Intent intent = new Intent(PublishPostActivity.this,
                            PhotiAlbumActivity.class);
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
                if (Bimp.drr.size() < 4 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
                break;
            case LABLE_CHOOSE_REQUEST:
                if (resultCode == LABLE_CHOOSE_RESULT) {
                    List<Expert> mDataExpert = (List<Expert>) data.getSerializableExtra(LableChooseActivity.LABLE_CHOOSE);
                    for (int i = 0; i < mDataExpert.size(); i++) {
                        LOG.i("debug", "mDatas-11111--->>>>>>>>" + mDataExpert.get(i).name);
                        mDatas.add(mDataExpert.get(i).name);
                    }
                    mLbaleChooseAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        return super.onKeyDown(keyCode, event);
    }
}
