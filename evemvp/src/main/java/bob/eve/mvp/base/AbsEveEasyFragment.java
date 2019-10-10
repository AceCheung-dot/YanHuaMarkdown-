/*
 * Create by BobEve on 17-12-18 下午6:49
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-18 下午6:49
 */

package bob.eve.mvp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Bob on 17/12/18.
 */

public abstract class AbsEveEasyFragment extends SupportFragment implements IFragment {

	protected View mView;
	protected Activity mActivity;
	protected Application mContext;
	private Unbinder mUnBinder;
	protected boolean isInited = false;
	boolean isinit=false;
	public static boolean b=false;

	@Override
	public void onAttach(Context context) {
		mActivity = (Activity) context;
		mContext = mActivity.getApplication();
		b=((Activity) context).getIntent().hasExtra("info");
		super.onAttach(context);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
													 @Nullable Bundle savedInstanceState) {
		mView = inflater.inflate(layoutResID(), null);
		isinit = true;



		return mView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mUnBinder = ButterKnife.bind(this, view);
		super.onViewCreated(view, savedInstanceState);


	attachView();

		initView(view);
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		isInited = true;
		initLazyView(mView);
	}

	@Override
	public void onDestroyView() {
		isinit=false;
		super.onDestroyView();
		mUnBinder.unbind();
	}

	@Override
	public Application application() {
		return mContext;
	}
}
