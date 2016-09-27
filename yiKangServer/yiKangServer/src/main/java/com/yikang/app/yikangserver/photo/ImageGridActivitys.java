package com.yikang.app.yikangserver.photo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yikang.app.yikangserver.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ImageGridActivitys extends Activity {
    public static final String EXTRA_IMAGE_LISTS = "imagelists";

    // ArrayList<Entity> dataList;
    List<ImageItem> dataLists;
    GridView gridView;
    TextView quxiao;
    public ImageGridAdapters adapter;
    AlbumHelper helper;
    Button bt;
    //String wheres;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivitys.this, "最多选择4张图片", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_image_grid);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataLists = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LISTS);
       // Collections.sort(dataLists, Collections.reverseOrder());
        //Collections.sort(dataLists);
        initView();
        /**
         * 完成保存图片的按钮
         */
        bt = (Button) findViewById(R.id.bt);
        quxiao = (TextView) findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
finish();
            }
        });
        bt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext();) {
                    list.add(it.next());
                }
                for (int i = 0; i < list.size(); i++) {
                    if (Bimp.drr.size() < 4) {
                        Bimp.drr.add(list.get(i));
                    }
                }


                finish();
            }

        });
    }

    /**
     * 鍒濆鍖杤iew瑙嗗浘
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapters(ImageGridActivitys.this, dataLists,
                mHandler);
        gridView.setAdapter(adapter);
        adapter.setTextCallback(new ImageGridAdapters.TextCallback() {
            public void onListen(int count) {
                bt.setText("完成" + "(" + count + "/4)");
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * 鏍规嵁position鍙傛暟锛屽彲浠ヨ幏寰楄窡GridView鐨勫瓙View鐩哥粦瀹氱殑瀹炰綋绫伙紝鐒跺悗鏍规嵁瀹冪殑isSelected鐘舵
                 * �锛� 鏉ュ垽鏂槸鍚︽樉绀洪�涓晥鏋溿� 鑷充簬閫変腑鏁堟灉鐨勮鍒欙紝涓嬮潰閫傞厤鍣ㄧ殑浠ｇ爜涓細鏈夎鏄�
                 */
                // if(dataList.get(position).isSelected()){
                // dataList.get(position).setSelected(false);
                // }else{
                // dataList.get(position).setSelected(true);
                // }
                /**
                 * 閫氱煡閫傞厤鍣紝缁戝畾鐨勬暟鎹彂鐢熶簡鏀瑰彉锛屽簲褰撳埛鏂拌鍥�
                 */
                adapter.notifyDataSetChanged();
            }

        });

    }
}
