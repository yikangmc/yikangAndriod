package com.yikang.app.yikangserver.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.utils.CachUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GuideActivity extends Activity {
    private ViewPager viewpager;
    private ArrayList<ImageView> imageViews;
    private int currentview = 0;
    private EdgeEffectCompat leftEdge;
    private EdgeEffectCompat rightEdge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        // 数据准备好了
        int ids[] = {R.drawable.guide_1, R.drawable.guide_2,
                R.drawable.guide_3};
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {
            // 根据id 资源创建图片
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setBackgroundResource(ids[i]);

            // 把图片添加到集合中
            imageViews.add(imageView);
        }
        imageViews.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);

            }
        });
        imageViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(2);

            }
        });

        imageViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.保存参数-标识已经进入主页面
                CachUtils.setBoolean(GuideActivity.this,
                        LoginActivity.START_MAIN, true);

                // 2.跳转到主页面
                Intent intent = new Intent(GuideActivity.this,
                        MainActivity.class);
                startActivity(intent);

                // 3.关闭引导页面
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        // 设置适配器
        viewpager.setAdapter(new MyPagerAdapter());
        try {
            Field leftEdgeField = viewpager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = viewpager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewpager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewpager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                if(rightEdge!=null&&!rightEdge.isFinished()){//到了最后一张并且还继续拖动，出现蓝色限制边条了
                    startActivity(new Intent(GuideActivity.this, MainActivity.class));
                    GuideActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
            @Override
            public void onPageSelected(int arg0) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
        });*/
       // viewpager.setPageTransformer(true, new DepthPageTransformer());


    }


    class MyPagerAdapter extends PagerAdapter {

        // 得到页面的总数
        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 相当于getView()方法 container：容器其实就是ViewPager position:页面对应的下标位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        /**
         * view:当前页面 object：instantiateItem返回的对象 作用：比较是否是同一个页面
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        /**
         * 销毁对应坐标的页面 object：要销毁的页面的对象 position：要销毁的位置
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }


    }


    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
