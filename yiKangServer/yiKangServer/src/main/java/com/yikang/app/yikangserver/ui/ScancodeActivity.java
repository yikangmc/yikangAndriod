package com.yikang.app.yikangserver.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.utils.ScreenModel;
import com.yikang.app.yikangserver.view.CameraManager;
import com.yikang.app.yikangserver.view.CaptureActivityHandler;
import com.yikang.app.yikangserver.view.InactivityTimer;
import com.yikang.app.yikangserver.view.TitleView;
import com.yikang.app.yikangserver.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;


public class ScancodeActivity extends Activity implements Callback {

    private CaptureActivityHandler handler;

    private ViewfinderView viewfinderView;

    private boolean hasSurface;

    private Vector<BarcodeFormat> decodeFormats;

    private String characterSet;

    private InactivityTimer inactivityTimer;

    private MediaPlayer mediaPlayer;

    private boolean playBeep;

    private static final float BEEP_VOLUME = 0.10f;

    private boolean vibrate;

    private TextView etGoodsCode;

    private float scale;

    private float density;

    private TitleView titleView;

    private String type;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", "com.yikang.app.yikangserver"));
        if (permission) {

        }else {
            String[] perms = {"android.permission.CAMERA"};
            int permsRequestCode = 200;
            ActivityCompat.requestPermissions(this, perms, permsRequestCode);
        }



        type = getIntent().getStringExtra("type");
        setContentView(R.layout.activity_scancode_capture);

        CameraManager.init(getApplication());
        scale = ScreenModel.getScreenModel(this).scale;
        density = ScreenModel.getScreenModel(this).density;

        titleView = (TitleView) this
                .findViewById(R.id.activity_scancode_titleview);
        titleView.setRightTextViewVisibility(View.GONE);

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        Intent intent = getIntent();
        if (intent != null) {
            int flag = intent.getIntExtra("flag", -100);
            if (flag == 1) {
                titleView.setContentTextView("付款码扫");
                viewfinderView.setTishi("");
            } else {
                titleView.setContentTextView("扫一扫去电脑上编辑");
            }
        } else {
            titleView.setContentTextView("商品条码扫描");
        }

        final LinearLayout layout = (LinearLayout) findViewById(R.id.input_goodscode_bar);
        RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) layout
                .getLayoutParams();
        layoutParams.setMargins((int) (20 * scale), 0, (int) (20 * scale),
                (int) (150 * scale));
        etGoodsCode = (TextView) this.findViewById(R.id.et_goodscode_input);
        etGoodsCode.setTextSize(40 * scale / density);

        TextView handInput = (TextView) findViewById(R.id.hand_input);
        RelativeLayout.LayoutParams handInputParams = (android.widget.RelativeLayout.LayoutParams) handInput
                .getLayoutParams();
        handInputParams.setMargins(0, 0, (int) (20 * scale),
                (int) (150 * scale));
        handInput.setTextSize(60 * scale / density);
        handInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
            }
        });

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        TextView searchGoodsCode = (TextView) this
                .findViewById(R.id.bt_goodscode_search);
        searchGoodsCode.setTextSize(40 * scale / density);
        searchGoodsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultString = etGoodsCode.getText().toString();
                if (resultString.equals("")) {

                } else {


                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }


    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted){
                    //授权成功之后，调用系统相机进行拍照操作等
                }else{
                    //用户授权拒绝之后，友情提示一下就可以了
                }

                break;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 扫码完成后的回调方法
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(ScancodeActivity.this, "扫描失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            Intent resultIntent = new Intent(getApplicationContext(),
                    ScanResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            bundle.putString("type", type);
            resultIntent.putExtras(bundle);
            startActivity(resultIntent);
            ScancodeActivity.this.finish();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {

            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}