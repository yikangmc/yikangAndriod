package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.Childs;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.ui.LableDetailsActivity;
import com.yikang.app.yikangserver.utils.TimeCastUtils;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/5/26.
 * 二级标签
 */
public class SecondLablesFragment extends BaseFragment {
    private TextView content;

    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    private List<Childs> secondLables ;
    private int id ;
    private DisplayImageOptions options; //配置图片加载及显示选项
    public SecondLablesFragment(List<Childs> secondLables,int id) {
        this.secondLables = secondLables;
        this.id=id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_jiajia)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_jiajia)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_jiajia)  //image加载失败
                .cacheInMemory(true)
                .cacheOnDisc(true)

                 //设置用户加载图片task(这里是圆角图片显示)
                .displayer(new FadeInBitmapDisplayer(500))
                .displayer(new RoundedBitmapDisplayer(8))

                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHandler = new Handler();
        geneItems();

        return initView(inflater, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        lables.clear();
        geneItems();
    }

    protected void geneItems() {

        for (int i = 0; i < secondLables.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName((secondLables.get(i).getTagName()));
            lable.setHeadTvLable(secondLables.get(i).getForumPostsTznumber() + "条帖子");
            lable.setDetailSupport(secondLables.get(i).getFollowNumber() + "人在关注");
            if(secondLables.get(i).getForumPostsTzUpdateTime()!=null){
                lable.setReleaseTime("最新更新：" + TimeCastUtils.compareDateTime(System.currentTimeMillis(), secondLables.get(i).getForumPostsTzUpdateTime()));
            }
            if(secondLables.get(i).getForumPostsTzUpdateTime()==null){
                lable.setReleaseTime("最新更新："+ TimeCastUtils.compareDateTime(System.currentTimeMillis(), 0L));
            }


            //lable.setReleaseTime("最近更新：" + "10天内");
            lable.setHeadIvUrl(secondLables.get(i).getTagPic() + "");
            lables.add(lable);
        }
    }



    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        message_xlistview = (XListView) view.findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(false);
        message_xlistview.setPullRefreshEnable(true);
        message_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LableDetailsActivity.class);
                intent.putExtra(LableDetailsActivity.EXTRA_LABLE, secondLables.get(position-1).getTagName());
                intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ID,secondLables.get(position-1).getTaglibId() + "");
                intent.putExtra(LableDetailsActivity.EXTRA_LABLE_ANSWER_CONTENTISSTORE, secondLables.get(position-1).getIsStore()+ "");
                startActivity(intent);
            }
        });
        mMessageAdapter = new CommonAdapter<LableDetailsBean>(getActivity(), lables, R.layout.second_lables_item) {
            @Override
            protected void convert(ViewHolder holder, LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_tiezi = holder.getView(R.id.lables_tv_tiezi);
                TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                TextView lables_tv_focusnum = holder.getView(R.id.lables_tv_focusnum);
                ImageView iv_mine_avatar = holder.getView(R.id.iv_mine_avatar);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_tiezi.setText(item.getHeadTvLable());
                lables_tv_time.setText(item.getReleaseTime());
                lables_tv_focusnum.setText(item.getDetailSupport());
                ImageLoader.getInstance().displayImage(item.getHeadIvUrl(), iv_mine_avatar,options);
            }
        };
        message_xlistview.setAdapter(mMessageAdapter);

        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start = ++refreshCnt;
                secondLables.clear();
                lables.clear();
                Api.getSecondLableContent(id,firstSecongLablesHandler);
                    }
                }, 1000);
            }
            private ResponseCallback<List<Childs>> firstSecongLablesHandler = new ResponseCallback<List<Childs>>() {


                @Override
                public void onSuccess(List<Childs> data) {
                    hideWaitingUI();
                   // LOG.i("debug", "HpWonderfulContent---->" + data+"哈哈");
                    secondLables=data;
                    geneItems();
                    message_xlistview.setAdapter(mMessageAdapter);
                    onLoad();
                }
                @Override
                public void onFailure(String status, String message) {
                   // LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
                    hideWaitingUI();
                    AppContext.showToast(message);
                }
            };


            private void onLoad() {
                message_xlistview.stopRefresh();
                message_xlistview.stopLoadMore();
                message_xlistview.setRefreshTime("刚刚");
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMessageAdapter.notifyDataSetChanged();
                        onLoad();
                    }
                }, 1000);

            }

            @Override
            public void onScrollMove() {

            }

            @Override
            public void onScrollFinish() {

            }

        });
        return view;
    }
}