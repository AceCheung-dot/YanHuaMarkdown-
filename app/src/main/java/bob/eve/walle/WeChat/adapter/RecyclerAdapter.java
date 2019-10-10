package bob.eve.walle.WeChat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import bob.eve.walle.R;
import bob.eve.walle.WeChat.bean.HomeItem;


public class RecyclerAdapter extends BaseQuickAdapter<HomeItem, BaseViewHolder> {

    public RecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItem item) {
        helper.setText(R.id.item_tv_title, item.getTitle());
        helper.setImageResource(R.id.itme_iv_icon, item.getImageResource());
    }
}
