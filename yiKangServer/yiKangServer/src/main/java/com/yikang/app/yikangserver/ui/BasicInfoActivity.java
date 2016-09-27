package com.yikang.app.yikangserver.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.interfaces.OnCameraInterfaces;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;
import com.yikang.app.yikangserver.utils.CommonUtils;
import com.yikang.app.yikangserver.view.CircleImageView;
import com.yikang.app.yikangserver.view.DateTimePickerDialogs;
import com.yikang.app.yikangserver.wheelview.NumericWheelAdapter;
import com.yikang.app.yikangserver.wheelview.OnWheelChangedListener;
import com.yikang.app.yikangserver.wheelview.WheelView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 基本信息页面
 */
public class BasicInfoActivity extends BaseActivity implements View.OnClickListener {
    private CircleImageView iv_mine_avatar;
    private TextView mine_tv_nickname;
    private TextView mine_tv_code;
    private TextView mine_tv_birthday;
    private TextView mine_tv_sex;
    private TextView mine_tv_address;
    private TextView mine_tv_goodat;
    private TextView tv_title_right_text;
    private LinearLayout ll_mine_avatar;
    private LinearLayout mine_ll_nickname;
    private LinearLayout mine_ll_code;
    private LinearLayout mine_ll_birthday;
    private LinearLayout mine_ll_sex;
    private LinearLayout mine_ll_address;
    private LinearLayout mine_ll_goodat;
    private User user;
    private Bitmap drawable = null;
    private String photoUrl;
    private String birthday;
    private int userSex;
    private Dialog dialog;
    private static int START_YEAR = 1940, END_YEAR = 2100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppContext.getAppContext().getUser();
        initContent();
        initTitleBar("基本信息");
    }

    @Override
    protected void findViews() {

        iv_mine_avatar = (CircleImageView) findViewById(R.id.iv_mine_avatar);
        mine_tv_nickname = (TextView) findViewById(R.id.mine_tv_nickname);
        mine_tv_code = (TextView) findViewById(R.id.mine_tv_code);
        mine_tv_birthday = (TextView) findViewById(R.id.mine_tv_birthday);
        mine_tv_sex = (TextView) findViewById(R.id.mine_tv_sex);
        mine_tv_address = (TextView) findViewById(R.id.mine_tv_address);
        tv_title_right_text = (TextView) findViewById(R.id.tv_title_right_text);
        mine_tv_goodat = (TextView) findViewById(R.id.mine_tv_goodat);
        ll_mine_avatar = (LinearLayout) findViewById(R.id.ll_mine_avatar);
        mine_ll_nickname = (LinearLayout) findViewById(R.id.mine_ll_nickname);
        mine_ll_birthday = (LinearLayout) findViewById(R.id.mine_ll_birthday);
        mine_ll_code = (LinearLayout) findViewById(R.id.mine_ll_code);
        mine_ll_sex = (LinearLayout) findViewById(R.id.mine_ll_sex);
        mine_ll_address = (LinearLayout) findViewById(R.id.mine_ll_address);
        mine_ll_goodat = (LinearLayout) findViewById(R.id.mine_ll_goodat);
        if (!TextUtils.isEmpty(user.avatarImg)) { // 显示头像
            ImageLoader.getInstance().displayImage(user.avatarImg, iv_mine_avatar);
        }
        tv_title_right_text.setText("保存");
        tv_title_right_text.setVisibility(View.INVISIBLE);
        if(user.name!=null) {
            mine_tv_nickname.setText(user.name);
        }
        if(user.profession>0) {
            mine_ll_goodat.setVisibility(View.VISIBLE);
            if (user.getSpecial().size()!=0&&user.getSpecial()!=null) {
                mine_tv_goodat.setText(user.getSpecial().get(0).getName());
            }
        }
        if(user.designationName!=null) {
            mine_tv_code.setText(user.designationName);
        }
        //mine_tv_code.setText("坐班白领");
        if(user.birthday!=null) {
            mine_tv_birthday.setText(getStringDate(Long.parseLong(user.birthday)));
        }
        if(user.userSex+""!=null) {
            if (user.userSex == 1) {
                mine_tv_sex.setText("男");
            }
            if (user.userSex == 2) {
                mine_tv_sex.setText("女");
            }
            if (user.userSex == 0) {
                mine_tv_sex.setText("请选择");
            }
        }
        if(user.addressDetail!=null) {
            mine_tv_address.setText(user.mapPositionAddress + user.addressDetail);
        }
        ll_mine_avatar.setOnClickListener(this);
        mine_ll_nickname.setOnClickListener(this);
        mine_ll_birthday.setOnClickListener(this);
        mine_ll_code.setOnClickListener(this);
        mine_ll_sex.setOnClickListener(this);
        mine_ll_address.setOnClickListener(this);
        mine_ll_goodat.setOnClickListener(this);
        //tv_title_right_text.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_basic_info);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            mine_tv_nickname.setText(data.getStringExtra("name"));
        }
        if (requestCode == 5 && resultCode == RESULT_OK) {

            mine_tv_code.setText(data.getStringExtra("fanhao"));
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {

            mine_tv_sex.setText(data.getStringExtra("sex"));
            if(data.getStringExtra("sex").equals("男")){
                userSex=0;
            }
            if(data.getStringExtra("sex").equals("女")){
                userSex=1;
            }
        }
        if (requestCode == 4 && resultCode == RESULT_OK) {

            mine_tv_address.setText(data.getStringExtra("address"));
        }
        if (requestCode == 8 && resultCode == RESULT_OK) {

            mine_tv_goodat.setText(data.getStringExtra("goodat"));
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
                Toast.makeText(BasicInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        if (requestCode == CommonUtils.PHOTO_REQUEST_CUT) {
           // if(drawable!=null) {
                drawable = data.getParcelableExtra("data");
            if(drawable!=null) {
                File fileBody = getFileBody(drawable, "photo" + ".png");
                System.out.println(fileBody + "我看看");
                showWaitingUI();
                Api.uploadFile(fileBody, photoHandler);
            }

        }


    }

    private ResponseCallback<FileResponse> photoHandler = new ResponseCallback<FileResponse>() {


        @Override
        public void onSuccess(FileResponse data) {
            PackageManager pm = getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "com.yikang.app.yikangserver"));
            if (permission) {

            }else {
                String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
                int permsRequestCode = 200;
                ActivityCompat.requestPermissions(BasicInfoActivity.this, perms, permsRequestCode);
            }
            String picStr = data.fileUrl;
            //LOG.i("debug", "[uploadAvatarHandler]上传图片成功" + picStr);
            hideWaitingUI();
            if (picStr.equals("")) {
                Toast.makeText(getApplicationContext(), "上传图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            photoUrl = picStr;
            iv_mine_avatar.setImageBitmap(drawable);
            Api.alterAvatar(photoUrl,alterAvatarHandlers);

        }

        @Override
        public void onFailure(String status, String message) {
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("上传图片失败：" + message);

        }
    };
    private ResponseCallback<Void> alterAvatarHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            dialog.dismiss();
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            mine_tv_birthday.setText(birthday);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            //LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("资料修改失败：" + message);

        }
    };
    private ResponseCallback<Void> alterAvatarHandlers = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();

            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            mine_tv_birthday.setText(birthday);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
           // LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("资料修改失败：" + message);

        }
    };

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mine_avatar:
                choosePhoto();
                break;
            case R.id.mine_ll_nickname:
                Intent name_intent = new Intent(getApplicationContext(),
                        MyNicknameActivity.class);
                name_intent.putExtra("name",mine_tv_nickname.getText().toString());
                startActivityForResult(name_intent, 1);
                break;
            case R.id.mine_ll_birthday:
                //showDialog();
                showDateTimePicker();
                break;
            case R.id.mine_ll_code:
                Intent fanhao_intent = new Intent(getApplicationContext(),
                        LablesEditorActivity.class);
                startActivityForResult(fanhao_intent, 5);
                break;
            case R.id.mine_ll_sex:
                Intent sex_intent = new Intent(getApplicationContext(),
                        MySexActivity.class);
                sex_intent.putExtra("sex",mine_tv_sex.getText().toString());
                startActivityForResult(sex_intent, 3);
                break;
            case R.id.mine_ll_address:
                Intent address_intent = new Intent(getApplicationContext(),
                        MyAddressActivity.class);
                address_intent.putExtra("address",mine_tv_address.getText().toString());
                startActivityForResult(address_intent, 4);
                break;
            case R.id.mine_ll_goodat:
                Intent goodat_intent = new Intent(getApplicationContext(),
                        LableChooseActivity.class);
                goodat_intent.putExtra("position",user.profession);
                startActivityForResult(goodat_intent, 8);
                break;

            default:
                break;
        }
    }
    public void showDialog() {
        DateTimePickerDialogs dialog = new DateTimePickerDialogs(
                BasicInfoActivity.this, System.currentTimeMillis());
        Window dialogWindow = dialog.getWindow();

        android.view.WindowManager.LayoutParams attributes = dialogWindow
                .getAttributes();
        attributes.width = ActionBar.LayoutParams.MATCH_PARENT;
        attributes.height = ActionBar.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER_HORIZONTAL;
        dialog.getWindow().setAttributes(attributes);
        dialog.setOnDateTimeSetListener(new DateTimePickerDialogs.OnDateTimeSetListener() {
            @Override
            public void OnDateTimeSet(android.app.AlertDialog dialog, long date) {
                birthday=getStringDate(date);
                Api.alterUserBirthday(getStringDate(date),alterAvatarHandler);

            }

        });
        dialog.show();
    }
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     */
    public static String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        return dateString;
    }
    private ResponseCallback<Void> alertInformationHandler = new ResponseCallback<Void>() {


        @Override
        public void onSuccess(Void data) {
            hideWaitingUI();
            AppContext.showToast("修改资料成功");
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            //LOG.i("debug", "[uploadAvatarHandler]上传图片失败");
            AppContext.showToast("资料修改失败：" + message);

        }
    };


    /**
     * 上传头像
     */
    private void choosePhoto() {
        CommonUtils.showCameraDialog(BasicInfoActivity.this,
                new OnCameraInterfaces() {
                    @Override
                    public void openPhone(View v) {
                        System.out.println("");
                        PackageManager pm = getPackageManager();
                        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.yikang.app.yikangserver"));
                        if (permission) {

                        }else {
                            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
                            int permsRequestCode = 200;
                            ActivityCompat.requestPermissions(BasicInfoActivity.this, perms, permsRequestCode);
                        }
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
                                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.yikang.app.yikangserver"));
                        if (permission) {

                        }else {
                            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
                            int permsRequestCode = 200;
                            ActivityCompat.requestPermissions(BasicInfoActivity.this, perms, permsRequestCode);
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

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
        String[] months_little = { "4", "6", "9", "11" };

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        dialog = new Dialog(this);
        dialog.setTitle("请您选择出生日期");
        // 找到dialog的布局文件
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_layout, null);

        // 年
        final WheelView wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        final WheelView wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month);

        // 日
        final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
        wv_day.setCyclic(true);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel("日");
        wv_day.setCurrentItem(day - 1);

      /*  // 时
        final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setCyclic(true);
        wv_hours.setCurrentItem(hour);

        // 分
        final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        wv_mins.setCyclic(true);
        wv_mins.setCurrentItem(minute);*/

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小
        int textSize = 14;

        textSize = dip2px(this, textSize);

        wv_day.TEXT_SIZE = textSize;
       // wv_hours.TEXT_SIZE = textSize;
       // wv_mins.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;

        Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
        Button btn_cancel = (Button) view
                .findViewById(R.id.btn_datetime_cancel);
        // 确定
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // 如果是个数,则显示为"02"的样式
                String parten = "00";
                DecimalFormat decimal = new DecimalFormat(parten);
                // 设置日期的显示
                 /*tv_time.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
                 + decimal.format((wv_month.getCurrentItem() + 1)) + "-"
                 + decimal.format((wv_day.getCurrentItem() + 1)) + " "
                 + decimal.format(wv_hours.getCurrentItem()) + ":"
                 + decimal.format(wv_mins.getCurrentItem()));*/
                 birthday=(wv_year.getCurrentItem() + START_YEAR) + "-"
                        + decimal.format((wv_month.getCurrentItem() + 1)) + "-"
                        + decimal.format((wv_day.getCurrentItem() + 1)) + " ";
                //AppContext.showToast(date);
                Api.alterUserBirthday(birthday,alterAvatarHandler);

            }
        });
        // 取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        // 设置dialog的布局,并显示
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
