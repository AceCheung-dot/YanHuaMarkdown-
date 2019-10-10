package bob.eve.walle.WeChat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import bob.eve.walle.R;

public class HomeTabAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HomeTabAdapter() {
        super(R.layout.textview);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_home_name,(helper.getAdapterPosition()+1)+"→"+item);
    }

}
