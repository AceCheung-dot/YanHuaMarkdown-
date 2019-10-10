package bob.eve.walle.WeChat;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;



import blcs.lwb.lwbtool.utils.RecyclerUtil;

import bob.eve.walle.R;
import bob.eve.walle.WeChat.manager.FramentManages;
import bob.eve.walle.WeChat.mvp.presenter.HomeTabPresenter;
import bob.eve.walle.WeChat.mvp.view.IHomeTabView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * RecyclerView
 */
public class HomeViewFragment extends Fragment implements IHomeTabView {

    @BindView(R.id.tool_recyclerView)
    RecyclerView recycler;
    private Unbinder bind;
    private View mView=null;
    private HomeTabAdapter adapter;
    private HomeTabPresenter presenter;
    private FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            activity = getActivity();
            mView = inflater.inflate(R.layout.tool_recyclerview, container, false);
            bind = ButterKnife.bind(this, mView);
            presenter = new HomeTabPresenter(this);
            presenter.init();
        }
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetch();
        if(bind != null){
            mView=null;
            bind.unbind();
            bind = null;
        }
    }

    @Override
    public void Recycler_init() {
        adapter = new HomeTabAdapter();
        RecyclerUtil.init(activity, OrientationHelper.VERTICAL,adapter,recycler);
        adapter.setNewData(MyUtils.getArray(activity, R.array.View));
    }

    @Override
    public void Recycler_click() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String item = (String) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.Item_Name,item);
                switch (item){
                    case FramentManages.QMUI_Android:
                        bundle.putString(Constants.URL, getString(R.string.QMUI_Android));
                        break;
                    default:break;
                }
                MyUtils.toFragment(activity,bundle,item);
            }
        });
    }


}
