package com.yikang.app.yikangserver.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ShareBitmapUtils {
    static ImageView img;
    static Bitmap bit;
    static String myJpgPath = Environment.getExternalStorageDirectory()+"/pepper/" + "test.png";
    public static void  setPicPosition(final String urls) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String url =urls;
                Bitmap tmpBitmap = null;
                try {
                    InputStream is = new URL(url).openStream();
                    tmpBitmap = BitmapFactory.decodeStream(is);
                    saveMyBitmap("myBitmap", tmpBitmap);
                    bit = getBitmapByPath("myBitmap");
                    is.close();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public Bitmap getBitmapByPath(String fileName) {
//        String myJpgPath = Environment.getExternalStorageDirectory()+"pepper/" + fileName;
                BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 12;
                Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
                return bm;
            }

            public Handler handler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        //img.setImageBitmap(bit);
                    }
                }

            };
            public void saveMyBitmap(String bitName, Bitmap mBitmap)
                    throws IOException {
                File tmp = new File("/sdcard/pepper/");
                if (!tmp.exists()) {
                    tmp.mkdir();
                }
                File f = new File(myJpgPath);
                f.createNewFile();
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                try {
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }).start();
    }




}
