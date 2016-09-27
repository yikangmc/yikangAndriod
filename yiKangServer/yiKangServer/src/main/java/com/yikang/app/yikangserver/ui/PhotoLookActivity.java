package com.yikang.app.yikangserver.ui;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.yikang.app.yikangserver.R;
public class PhotoLookActivity extends SwipeBackActivity {
    private String picurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_look);
        // 获得传递来的图片url
       picurl = getIntent().getStringExtra("PICURL");
        // 获得句柄
       // PhotoView photoView = (PhotoView) findViewById(R.id.photoview);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
       // ImageLoader.getInstance().displayImage(picurl, photoView);
        // 设置退出的点击监听
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
