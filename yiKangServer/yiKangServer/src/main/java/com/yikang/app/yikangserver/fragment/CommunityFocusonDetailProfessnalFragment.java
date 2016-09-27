package com.yikang.app.yikangserver.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.LableDetailsBean;
import com.yikang.app.yikangserver.bean.MyFocusPerson;
import com.yikang.app.yikangserver.ui.PersonalHomepageActivity;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.view.SwipeView;
import com.yikang.app.yikangserver.view.SwipeView.OnSwipeChangeListener;
import com.yikang.app.yikangserver.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝晓东 on 2016/4/26.
 * 关注下的专业人士模块
 */
public class CommunityFocusonDetailProfessnalFragment extends BaseFragment implements View.OnClickListener {

    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private CommonAdapter<LableDetailsBean> mMessageAdapter;
    private ArrayList<LableDetailsBean> lables = new ArrayList<LableDetailsBean>();
    //private List<String> mList=new ArrayList<>();
    private XListView message_xlistview;
    int position;
    private DisplayImageOptions options; //配置图片加载及显示选项
    public CommunityFocusonDetailProfessnalFragment(String titlename, ArrayList<MyFocusPerson> persons) {
        this.titlename = titlename;
        this.persons = persons;
    }

    private ArrayList<MyFocusPerson> persons = new ArrayList<MyFocusPerson>();

    private String titlename;
    private SwipeView openedSwipeView;

    public OnSwipeChangeListener onSwipeChangeListener = new OnSwipeChangeListener() {

        @Override
        public void onOpen(SwipeView swipeView) {
            //保存swipView
            openedSwipeView = swipeView;
        }

        @Override
        public boolean onDown(SwipeView swipeView) {
            if (openedSwipeView != null && openedSwipeView != swipeView) {
                openedSwipeView.close();
                return false;
            }
            return true;
        }

        @Override
        public void onClose(SwipeView swipeView) {
            if (openedSwipeView != null && openedSwipeView == swipeView) {
                openedSwipeView = null;
            }
        }
    };

    public CommunityFocusonDetailProfessnalFragment(String name) {
        this.titlename = name;
    }

    public CommunityFocusonDetailProfessnalFragment() {

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_pic)    //在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic)  //image连接地址为空时
                .showImageOnFail(R.drawable.default_pic)  //image加载失败
                .bitmapConfig(Bitmap.Config.ARGB_4444) // default
                //.displayer(new RoundedBitmapDisplayer(8))  //设置用户加载图片task(这里是圆角图片显示)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .build();
        mHandler = new Handler();
        getData();

       // geneItems();
        View rootView = inflater.inflate(R.layout.fragment_community_focuson_professional, container, false);
        findView(rootView);
        if (AppContext.getAppContext().getAccessTicket() != null) {
            Api.getAllMyFocusPerson(allMyFocusPersonHandler);
        }
        return rootView;
    }

    private void getData() {
        //获取标签下的所有问答内容
        if (AppContext.getAppContext().getAccessTicket() != null) {
            Api.getAllMyFocusPerson(allMyFocusPersonHandler);
        }
    }
    /**
     * 获取标签下所有内容的回调
     */
    private ResponseCallback<String> allMyFocusPersonsHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
            hideWaitingUI();


           /* for (LablesBean hp : data) {
                secondLables.add(hp);
            }
            geneItems(text);
            message_xlistview.setAdapter(mMessageAdapter);*/

            LOG.i("debug", "HpWonderfulContent---->" + data + "哈哈");

        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };

    private void findView(View rootView) {
        message_xlistview = (XListView) rootView.findViewById(R.id.message_xlistview);
        message_xlistview.setPullLoadEnable(false);
        mMessageAdapter = new CommonAdapter<LableDetailsBean>(getActivity(), lables, R.layout.list_lables_professional_item) {
            @Override
            protected void convert(ViewHolder holder, final LableDetailsBean item) {
                TextView lables_tv_title = holder.getView(R.id.lables_tv_title);
                TextView lables_tv_time = holder.getView(R.id.lables_tv_time);
                TextView tag_first=holder.getView(R.id.tag_first);
                TextView tag_second=holder.getView(R.id.tag_second);
                TextView tag_third=holder.getView(R.id.tag_third);
                TextView tag_fourth=holder.getView(R.id.tag_fourth);
                com.yikang.app.yikangserver.view.CircleImageView iv_mine_avatar = holder.getView(R.id.iv_mine_avatar);
                lables_tv_title.setText(item.getHeadTvName());
                lables_tv_time.setText("已有"+item.getTaglibId()+"人关注");
                ImageLoader.getInstance().displayImage(item.getHeadTvLable(),iv_mine_avatar,options);
                if(item.getAdepts()!=null&&item.getAdepts().size()==1){
                    tag_first.setText(item.getAdepts().get(0).getName());
                    tag_first.setBackgroundResource(R.color.main_background_color);
                }
                if(item.getAdepts()!=null&&item.getAdepts().size()==2){
                    tag_first.setText(item.getAdepts().get(0).getName());
                    tag_second.setText(item.getAdepts().get(1).getName());
                    tag_first.setBackgroundResource(R.color.main_background_color);
                    tag_second.setBackgroundResource(R.color.main_background_color);
                }
                if(item.getAdepts()!=null&&item.getAdepts().size()==3){
                    tag_first.setText(item.getAdepts().get(0).getName());
                    tag_second.setText(item.getAdepts().get(1).getName());
                    tag_third.setText(item.getAdepts().get(2).getName());
                    tag_first.setBackgroundResource(R.color.main_background_color);
                    tag_second.setBackgroundResource(R.color.main_background_color);
                    tag_third.setBackgroundResource(R.color.main_background_color);
                }
                if(item.getAdepts()!=null&&item.getAdepts().size()==4){
                    tag_first.setText(item.getAdepts().get(0).getName());
                    tag_second.setText(item.getAdepts().get(1).getName());
                    tag_third.setText(item.getAdepts().get(2).getName());
                    tag_fourth.setText(item.getAdepts().get(3).getName());
                    tag_first.setBackgroundResource(R.color.main_background_color);
                    tag_second.setBackgroundResource(R.color.main_background_color);
                    tag_third.setBackgroundResource(R.color.main_background_color);
                    tag_fourth.setBackgroundResource(R.color.main_background_color);
                }
                TextView tv_delete = (TextView) holder.getConvertView().findViewById(R.id.tv_delete);
                tv_delete.setTag(holder.getPosition()); //保存下标
                LinearLayout ll_content = (LinearLayout) holder.getConvertView().findViewById(R.id.ll_content);

                SwipeView swipeView = (SwipeView) holder.getConvertView();
                swipeView.setOnSwipeChangeListener(onSwipeChangeListener);

                /*
                    进行删除操作
                 */
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = (Integer) v.getTag();

                        Api.deleteMyFocusPerson(Integer.parseInt(lables.get(position).getHeadIvUrl()),deleteGuanzhuHandler);

                    }
                });

                /*
                使滑动出来的列表缩回
                 */
                ll_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (openedSwipeView != null) {
                            openedSwipeView.close();
                            return;
                        }
                        Intent intent = new Intent(getActivity(),
                                PersonalHomepageActivity.class);
                        intent.putExtra("userId",item.getHeadIvUrl());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                    }
                });
            }
        };

        message_xlistview.setAdapter(mMessageAdapter);
        //设置滚动的监听
        message_xlistview.setOnScrollListener(new AbsListView.OnScrollListener() {

            //当滚动状态改变时调用

            /**
             * SCROLL_STATE_TOUCH_SCROLL : 滚动
             * SCROLL_STATE_IDLE : 初始化(没有滚动)
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {//开始滚动
                    if (openedSwipeView != null) {
                        openedSwipeView.close();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

        message_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData();
            }

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
                        geneItems();
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
    }
    /**
     * 获取关注专业人士的回调
     */
    private ResponseCallback<List<MyFocusPerson>> allMyFocusPersonHandler = new ResponseCallback<List<MyFocusPerson>>() {

        @Override
        public void onSuccess(List<MyFocusPerson> data) {

            hideWaitingUI();


            start = ++refreshCnt;
            lables.clear();
            persons.clear();
            onLoad();

            for (MyFocusPerson hp : data) {
                persons.add(hp);
            }

            geneItems();
            message_xlistview.setAdapter(mMessageAdapter);

        }

        private void onLoad() {
            message_xlistview.stopRefresh();
            message_xlistview.stopLoadMore();
            message_xlistview.setRefreshTime("刚刚");
        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            AppContext.showToast(message);
        }
    };


    private ResponseCallback<String> deleteGuanzhuHandler = new ResponseCallback<String>() {


        @Override
        public void onSuccess(String data) {
            hideWaitingUI();
            LOG.i("debug", "HpWonderfulContent---->" + data);
            lables.remove(position);
            mMessageAdapter.notifyDataSetChanged();//刷新listView

        }

        @Override
        public void onFailure(String status, String message) {
            LOG.i("debug", "[loadUserInfo]加载失败-->" + message + "   status-->" + status);
            hideWaitingUI();
            AppContext.showToast(message);
        }
    };
    protected void geneItems() {
        for (int i = 0; i < persons.size(); i++) {
            LableDetailsBean lable = new LableDetailsBean();
            lable.setHeadTvName(persons.get(i).getUserName()+ "");
            lable.setHeadTvLable(persons.get(i).getPhotoUrl());
            lable.setHeadIvUrl(persons.get(i).getUserId()+"");
            lable.setTaglibId(persons.get(i).getFollowCount());
            lable.setAdepts(persons.get(i).getAdepts());
            lables.add(lable);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }


}
