package bob.eve.walle.common;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


import com.hjq.bar.TitleBar;
import com.hjq.umeng.UmengClient;

import org.jetbrains.annotations.NotNull;

import bob.eve.comm.lib.util.ToastUtils;
import bob.eve.walle.helper.DebugUtils;
import bob.eve.walle.other.EventBusManager;
import bob.eve.walle.other.StatusManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 项目中 Fragment 懒加载基类
 */
public abstract class MyLazyFragment<A extends MyActivity> extends UILazyFragment<A> {

    private Unbinder mButterKnife; // View注解

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mButterKnife = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initFragment() {
        super.initFragment();
        EventBusManager.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mButterKnife != null) {
            mButterKnife.unbind();
        }
        EventBusManager.unregister(this);
    }

    @Nullable
    public TitleBar getTitleBar() {
        if (getTitleId() > 0 && findViewById(getTitleId()) instanceof TitleBar) {
            return findViewById(getTitleId());
        }
        return null;
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence s) {
        ToastUtils.show(getContext(),s.toString());
    }

    public void toast(int id) {
        ToastUtils.show(getContext(),id);
    }

    public void toast(Object object) {

    }

    /**
     * 打印日志
     */
    public void log(Object object) {
        if (DebugUtils.isDebug(getBindingActivity())) {
            Log.v(getClass().getSimpleName(), object != null ? object.toString() : "null");
        }
    }
    @NotNull
    //fixme
    @Override
    public void onResume() {
        super.onResume();
        UmengClient.onResume(getActivity());
    }

    @Override
    public void onPause() {
        UmengClient.onPause(getActivity());
        super.onPause();
    }

    private final StatusManager mStatusManager = new StatusManager();

    /**
     * 显示加载中
     */
    public void showLoading() {
        mStatusManager.showLoading(this.getActivity());
    }

    /**
     * 显示加载完成
     */
    public void showComplete() {
        mStatusManager.showComplete();
    }

    /**
     * 显示空提示
     */
    public void showEmpty() {
        mStatusManager.showEmpty(getView());
    }

    /**
     * 显示错误提示
     */
    public void showError() {
        mStatusManager.showError(getView());
    }

    /**
     * 显示自定义提示
     */
    public void showLayout(@DrawableRes int iconId, @StringRes int textId) {
        mStatusManager.showLayout(getView(), iconId, textId);
    }

    public void showLayout(Drawable drawable, CharSequence hint) {
        mStatusManager.showLayout(getView(), drawable, hint);
    }
}