package bob.eve.walle.WeChat.mvp.presenter;

import blcs.lwb.lwbtool.base.BasePresenter;

import bob.eve.walle.WeChat.mvp.view.IMainView;

public class MainPresenter extends BasePresenter<IMainView> {

    private IMainView v;
    public MainPresenter(IMainView v){
        this.v = v;
        initMenu();
        initViewPage();
    }

    private void initViewPage() {

        v.ViewPage();
    }

    private void initMenu() {
        v.BottomMenu();
    }

}
