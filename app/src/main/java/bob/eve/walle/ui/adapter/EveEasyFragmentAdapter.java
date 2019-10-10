/*
 * Create by BobEve on 17-12-19 下午1:01
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-19 下午1:01
 */

package bob.eve.walle.ui.adapter;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import bob.eve.mvp.base.AbsEveBaseFragment;

import java.util.List;

/**
 * Created by Bob on 17/12/19.
 */

public class EveEasyFragmentAdapter extends FragmentPagerAdapter {

	private List<AbsEveBaseFragment> mFragments;

	public EveEasyFragmentAdapter(FragmentManager fm, List<AbsEveBaseFragment> fragments) {
		super(fm);
//		if (fragments == null) {
//			fragments = Collections.emptyList();
//		}

		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
	}
}
