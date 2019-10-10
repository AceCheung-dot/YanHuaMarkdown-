package bob.eve.walle.WeChat.mvp.presenter;

import blcs.lwb.lwbtool.base.BasePresenter;

import bob.eve.walle.WeChat.mvp.view.IHomeTabView;

public class HomeTabPresenter extends BasePresenter<IHomeTabView> {

    private IHomeTabView v;
    public HomeTabPresenter(IHomeTabView v){
        this.v = v;
        onAttach(v);
    }

    public void init() {
        v.Recycler_init();
        v.Recycler_click();
    }
}
