package com.yikang.app.yikangserver.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragmentList;

	public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.mFragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

	// @Override
	// public CharSequence getPageTitle(int position) {
	// // TODO Auto-generated method stub
	// return "AAAAAA"+position;
	// }

}
