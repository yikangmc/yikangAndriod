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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.LableChooseGridViewAdapter;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.BannerBean;
import com.yikang.app.yikangserver.bean.Expert;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.dialog.AlertDialog;
import com.yikang.app.yikangserver.photo.Bimp;
import com.yikang.app.yikangserver.photo.PhotiAlbumActivity;
import com.yikang.app.yikangserver.photo.PhotoActivity;
import com.yikang.app.yikangserver.photo.PhotoFileUtils;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.T;
import com.yikang.app.yikangserver.utils.TimeCastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布帖子(问题)页面
 */
public class PublishPostActivity extends BaseActivity {

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private LinearLayout activity_selectimg_send;
    private LinearLayout activity_selectimg_cancle;
    private EditText et_title, et_content;
    public static String EXTRA_LABLE_EXAMPLEIDS = "lableexample";
    private List<String> mDatas;
    List<String> list = new ArrayList<String>();
    private GridView mLableGridView;
    private LableChooseGridViewAdapter mLbaleChooseAdapter;
    private static final int LABLE_CHOOSE_REQUEST = 0x000001;
    public static final int LABLE_CHOOSE_RESULT = 0x000002;
    private List<String> pictrue = new ArrayList<String>();//上传的图片集合
    private String title,content;//问题的标题，内容
    int[] lables = {2};//问题的标签
    private String tagLib;
    public PublishPostActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_selectimg);
        tagLib=getIntent().getStringExtra(EXTRA_LABLE_EXAMPLEIDS);
        Init();
    }

    public void Init() {
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        et_content = (EditText) findViewById(R.id.et_content);
        et_title = (EditText) findViewById(R.id.et_title);
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


                /*第一步上传图片
                 *
                 */

                for (int i = 0; i < Bimp.drr.size(); i++) {
                    String Str = Bimp.drr.get(i).substring(
                            Bimp.drr.get(i).lastIndexOf("/") + 1,
                            Bimp.drr.get(i).lastIndexOf("."));
                    Log.i("debug", "path-->" + Str);
                    list.add(PhotoFileUtils.SDPATH + Str + ".JPEG");
                }
                for (int j = 0; j < list.size(); j++) {
                    Log.i("debug", "list---------path-->" + list.get(j));
                    //此处是一次上传的，怎么样保证全部上传成功了呢,这个难住我了
                    Api.uploadFile(new File(list.get(j)), uploadPicHandler);
                }

                for (int k = 0; k < mDatas.size(); k++) {
                    Log.i("debug", "mDatas-->" + mDatas.get(k));
                }
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                /**
                 * 第二步根据上传图片返回来的地址结果，再提交
                 */
               if(list==null|| list.size()==0) {

                   //问题标题
                   title = et_title.getText().toString().trim();
                   //问题描述
                   content = et_content.getText().toString().trim();
                   //所属标签
                   int[] tag = {Integer.parseInt(tagLib)};
                   //将图片集合转化为图片数组
                   String[] toBeStored = pictrue.toArray(new String[pictrue.size()]);
                   //图片数组
                   String[] pictrues = {};

                   if (title == null || "".equals(title)) {
                       Toast.makeText(getApplicationContext(), "请填写您的问题", Toast.LENGTH_SHORT).show();
                       return;
                   }

                   //发布文章
                   //Api.publishContent("我是文章标题","我是文章内容",a,b,publishAnswerHandler);

                   //发布问题
                   Api.addAnswer(title, content, tag, toBeStored, publishAnswerHandler);
               }
               // showWaitingUI();
                //查询问题详情
                // Api.searchQuestionDetail(Integer.parseInt("1"),publishAnswerHandler);
                //Api.getAllQuestionContent(Integer.parseInt("1"),publishAnswerHandler);
                //查询一个标签下的所有文章
                //Api.getAllLableContent(Integer.parseInt("1"),publishAnswerHandler);
                //获取某一个文章的详情
                //Api.getDetailContent(Integer.parseInt("1"),publishAnswerHandler);
                //支持文章或取消支持
                // Api.support(Integer.parseInt("1"),publishAnswerHandler);
                //首页推荐列表
                //Api.getWonderfulContent(publishAnswerHandler);
                //问题回答的支持
                //Api.supportAnswer(Integer.parseInt("1"),publishAnswerHandler);
                //回复问题
                //Api.addAnswerQuestionDetail(Integer.parseInt("1"),"我来测试回复这个问题，答得怎么样啊",publishAnswerHandler);
                //回复文章
                //Api.addAnswerContentDetail(Integer.parseInt("1"),"我来测试回复这篇文章,怎么样呢",publishAnswerHandler);
                //模糊搜索标签列表
                //Api.getSearchContent("腰",publishAnswerHandler);

            }

            private ResponseCallback<FileResponse> uploadPicHandler = new ResponseCallback<FileResponse>() {
                @Override
                public void onSuccess(FileResponse data) {

                    String picStr=data.fileUrl;
                    pictrue.add(picStr);

                    LOG.i("debug", "[uploadAvatarHandler]上传图片成功" + picStr);
                    hideWaitingUI();
                    //发布问题
                    if(pictrue.size()==list.size()) {
                        PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                        Toast.makeText(getApplicationContext(),pictrue.size()+"",Toast.LENGTH_SHORT).show();
                        //问题标题
                        title=et_title.getText().toString().trim();
                        //问题描述
                        content=et_content.getText().toString().trim();
                        //所属标签
                        int[] tag = {Integer.parseInt(tagLib)};
                        //将图片集合转化为图片数组
                        String[] toBeStored = pictrue.toArray(new String[pictrue.size()]);
                        //图片数组
                        String[] pictrues = {"https://biophoto.s3.cn-north-1.amazonaws.com.cn/6e128d34-94ea-4426-ae8b-57bb142f4dbf.JPEG","https://biophoto.s3.cn-north-1.amazonaws.com.cn/35321316-661d-4280-8afa-3fd3fb9aaff7.JPEG"};

                        if(title == null || "".equals(title)){
                            Toast.makeText(getApplicationContext(),"请填写您的问题",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //发布问题
                        Api.addAnswer(title, content, tag, toBeStored, publishAnswerHandler);
                    }
                }

                @Override
                public void onFailure(String status, String message) {
                    LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
                    AppContext.showToast("上传图片失败：" + message);

                   // PhotoFileUtils.deleteDir();  //删除保存图片的文件夹
                }
            };


            private ResponseCallback<String> publishAnswerHandler = new ResponseCallback<String>() {

                @Override
                public void onSuccess(String data) {
                    hideWaitingUI();
                    Toast.makeText(getApplicationContext(), "问题发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                    LOG.i("debug", "HpWonderfulContent---->" + data);

                }

                @Override
                public void onFailure(String status, String message) {
                    LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                    // hideWaitingUI();
                    AppContext.showToast(message);
                }
            };
        });


        mLableGridView = (GridView) findViewById(R.id.lable_gv_test);
        initDatas();
        mLbaleChooseAdapter = new LableChooseGridViewAdapter(PublishPostActivity.this, mDatas);
        mLableGridView.setAdapter(mLbaleChooseAdapter);

    }


    //默认携带过来的标签
    private void initDatas() {
        mDatas = new ArrayList<String>();
        mDatas.add("运动康复");
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void getData() {
        //发布文章
        //Api.publishContent("我是文章标题","我是文章内容",a,b,publishAnswerHandler);
        //发布问题
        //Api.addAnswer("我是问题标题","我是问题内容",a,publishAnswerHandler);
        //查询问题详情
        // Api.searchQuestionDetail(Integer.parseInt("1"),publishAnswerHandler);
        //查询一个标签下的所有问题
        //Api.getAllQuestionContent(Integer.parseInt("1"),publishAnswerHandler);
        //查询一个标签下的所有文章
        //Api.getAllLableContent(Integer.parseInt("1"),publishAnswerHandler);
        //获取某一个文章的详情
        //Api.getDetailContent(Integer.parseInt("1"),publishAnswerHandler);
        //支持文章或取消支持
        // Api.support(Integer.parseInt("1"),publishAnswerHandler);
        //首页推荐列表
        //Api.getWonderfulContent(publishAnswerHandler);
        //问题回答的支持
        //Api.supportAnswer(Integer.parseInt("1"),publishAnswerHandler);
        //回复问题
        //Api.addAnswerQuestionDetail(Integer.parseInt("1"),"我来测试回复这个问题，答得怎么样啊",publishAnswerHandler);
        //回复文章
        //Api.addAnswerContentDetail(Integer.parseInt("1"),"我来测试回复这篇文章,怎么样呢",publishAnswerHandler);
        //模糊搜索标签列表
        //Api.getSearchContent("腰",publishAnswerHandler);

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
                    intent.putExtra("where","wenti");
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
