package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class MineProfessionalActivity extends BaseActivity {
    private XListView lable_details_listview;
    private CommonAdapter<LablesBean> mMessageAdapter;
    private ArrayList<LablesBean> lables = new ArrayList<LablesBean>();
    private View lvHeaderView;
    private int start = 1;
    private DisplayImageOptions options; //配置图片加载及显示选项
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_professional);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                //.displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
        initContent();
        initTitleBar("我的专家说");
    }

    @Override
    protected void findViews() {
        lable_details_listview = (XListView) findViewById(R.id.lable_detals_listview);
        lvHeaderView = getLayoutInflater().inflate(R.layout.headerview, null);
        lable_details_listview.addHeaderView(lvHeaderView);
        lable_details_listview.setPullLoadEnable(false);
        lable_details_listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getDatas();
            }

            private void getDatas() {
               Api.getMyContent(Long.parseLong(AppContext.getAppContext().getUser().getUserId() + ""),1,myContentsHandler);
            }

            @Override
            public void onLoadMore() {
                start +=1;
                getDataMore();
            }
            protected void getDataMore() {

                Api.getMyContent(Long.parseLong(AppContext.getAppContext().getUser().getUserId() + ""), start,myContentsHandlers);
            }
            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }
        });
    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void getData() {
        showWaitingUI();
        Api.getMyContent(Long.parseLong(AppContext.getAppContext().getUser().getUserId() + ""),1,myContentsHandler);
    }

    @Override
    protected void initViewContent() {

    }
    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<List<LablesBean>> myContentsHandler = new ResponseCallback<List<LablesBean>>() {

        @Override
        public void onSuccess(List<LablesBean> data) {
            hideWaitingUI();
            if(data.size()<10){
                lable_details_listview.setPullLoadEnable(false);
            }else {
                lable_details_listview.setPullLoadEnable(true);
            }
            lables.clear();
            lables= (ArrayList<LablesBean>) data;
            onLoad();
            initViewContents();
           // LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

        }

        private void onLoad() {
            lable_details_listview.stopRefresh();
            lable_details_listview.stopLoadMore();
            lable_details_listview.setRefreshTime("刚刚");
        }

        private void initViewContents() {
            mMessageAdapter = new CommonAdapter<LablesBean>(getApplicationContext(), lables, R.layout.mine_professional_content_item) {
                @Override
                protected void convert(ViewHolder holder, LablesBean item) {
                    TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                    TextView homepage_wonderfulcontent_support = holder.getView(R.id.homepage_wonderfulcontent_support);
                    TextView homepage_wonderfulcontent_comment = holder.getView(R.id.homepage_wonderfulcontent_comment);
                    ImageView lable_iv_header = holder.getView(R.id.lable_iv_header);
                    lables_tv_title.setText(item.getTitle() + "");
                    homepage_wonderfulcontent_support.setText(item.getStars() + "");
                    homepage_wonderfulcontent_comment.setText(item.getAnswersNums() + "");
                    ImageLoader.getInstance().displayImage(item.getRecommendPicUrl()+"", lable_iv_header,options);
                }
            };
            lable_details_listview.setAdapter(mMessageAdapter);
            lable_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), LableDetaileContentActivity.class);
                    intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER, 1);
                    if (lables != null) {
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENT, lables.get(position-2).getUserName());
                        intent.putExtra(LableDetaileContentActivity.EXTRA_LABLE_ANSWER_CONTENTID, lables.get(position-2).getForumPostId() + "");
                    }
                    startActivity(intent);


                }
            });
        }

        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    private ResponseCallback<List<LablesBean>> myContentsHandlers = new ResponseCallback<List<LablesBean>>() {

        @Override
        public void onSuccess(List<LablesBean> data) {
            hideWaitingUI();
            if(data.size()<10){
                lable_details_listview.setPullLoadEnable(false);
            }else {
                lable_details_listview.setPullLoadEnable(true);
            }
            onLoad();
        }
        private void onLoad() {
            lable_details_listview.stopRefresh();
            lable_details_listview.stopLoadMore();
            lable_details_listview.setRefreshTime("刚刚");
        }
        @Override
        public void onFailure(String status, String message) {
            //LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

}
