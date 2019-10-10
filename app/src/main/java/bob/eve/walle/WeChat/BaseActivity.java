package bob.eve.walle.WeChat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import blcs.lwb.lwbtool.base.BasePresenter;
import blcs.lwb.lwbtool.base.BaseView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BaseView, P extends BasePresenter<T>> extends Activity implements BaseView {
    private static final String TAG = "BaseActivity";//用于打印日志（log）的类的标记
    protected blcs.lwb.lwbtool.base.BaseActivity context = null;
    protected View view = null;
    private Unbinder bind;
    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(bindLayout());
        bind = ButterKnife.bind(this);
        presenter = bindPresenter();
        if (presenter != null) {
            presenter.onAttach((T) this);
        }
        initView();
    }

    /**
     * 绑定P层
     */
    protected abstract P bindPresenter();

    /**
     * TODO UI显示方法，必须在子类onCreate方法内setContentView后调用
     */
    public abstract void initView();

    /**
     * TODO 关联布局
     */
    public abstract int bindLayout();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDetch();
            presenter = null;
        }
        bind.unbind();
    }
}