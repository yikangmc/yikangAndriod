package com.yikang.app.yikangserver.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.photo.AlbumHelpers;
import com.yikang.app.yikangserver.photo.ImageBucket;
import com.yikang.app.yikangserver.photo.ImageBucketAdapter;
import com.yikang.app.yikangserver.photo.ImageGridActivitys;

import java.io.Serializable;
import java.util.List;

/**
 * 从相册选取照片
 */
public class PhotiAlbumActivityTiezi extends Activity {
    // ArrayList<Entity> dataList;//用来装载数据源的列表
    List<ImageBucket> dataLists;
    GridView gridView;
    TextView quxiao;
    ImageBucketAdapter adapter;// 自定义的适配器
    AlbumHelpers helper;
    public static final String EXTRA_IMAGE_LIST = "imagelists";
    public static Bitmap bimap;
    //String fromWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_image_bucket);
        //fromWhere=getIntent().getStringExtra("where");
        helper = AlbumHelpers.getHelper();
        helper.init(getApplicationContext());

        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // /**
        // * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
        // */
        // dataList = new ArrayList<Entity>();
        // for(int i=-0;i<10;i++){
        // Entity entity = new Entity(R.drawable.picture, false);
        // dataList.add(entity);
        // }
        dataLists = helper.getImagesBucketList(false);
        bimap=BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        quxiao = (TextView) findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new ImageBucketAdapter(PhotiAlbumActivityTiezi.this, dataLists);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
                 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
                 */
                // if(dataList.get(position).isSelected()){
                // dataList.get(position).setSelected(false);
                // }else{
                // dataList.get(position).setSelected(true);
                // }
                /**
                 * 通知适配器，绑定的数据发生了改变，应当刷新视图
                 */
                // adapter.notifyDataSetChanged();
                Intent intent = new Intent(PhotiAlbumActivityTiezi.this,
                        ImageGridActivitys.class);
                intent.putExtra(ImageGridActivitys.EXTRA_IMAGE_LISTS,
                        (Serializable) dataLists.get(position).imageList);
               // intent.putExtra("wheress",fromWhere);
                startActivity(intent);
                finish();
            }

        });
    }
}
