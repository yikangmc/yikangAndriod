package com.yikang.app.yikangserver.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.adapter.CommonAdapter;
import com.yikang.app.yikangserver.adapter.ViewHolder;
import com.yikang.app.yikangserver.utils.DensityUtils;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.view.swipemenulistview.SwipeMenu;
import com.yikang.app.yikangserver.view.swipemenulistview.SwipeMenuCreator;
import com.yikang.app.yikangserver.view.swipemenulistview.SwipeMenuItem;
import com.yikang.app.yikangserver.view.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudong on 2016/4/20.
 */
public class CommunityFocusonDetailFragment extends BaseFragment implements View.OnClickListener{

    private SwipeMenuListView community_focuson_detail_smlv;
    private List<String> mList =new ArrayList<String>();
    private CommonAdapter<String> mFoucsonAdapter;

    private String titlename;
    public CommunityFocusonDetailFragment(String name){
        this.titlename=name;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_focuson_details, container, false);
        findView(rootView);
        return rootView;
    }

    private void findView(View rootView) {
        for(int i=0;i<5;i++){
            mList.add(titlename+" - "+i);
        }
        community_focuson_detail_smlv=(SwipeMenuListView)rootView.findViewById(R.id.community_focuson_detail_smlv);  //滑动删除控件
        mFoucsonAdapter=new CommonAdapter<String>(getActivity(),mList,R.layout.list_lables_item) {
            @Override
            protected void convert(ViewHolder holder, String item) {

            }
        };
        community_focuson_detail_smlv.setAdapter(mFoucsonAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xef, 0,
                        0)));
                // set item width
                openItem.setWidth(DensityUtils.dp2px(getActivity(),90));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };
        // set creator
        community_focuson_detail_smlv.setMenuCreator(creator);
        // step 2. listener item click event
        community_focuson_detail_smlv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
//					delete(item);
                        mList.remove(position);
                        mFoucsonAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

    public static void setIndex(int index){
        LOG.i("debug","index-->"+index);
    }
}
