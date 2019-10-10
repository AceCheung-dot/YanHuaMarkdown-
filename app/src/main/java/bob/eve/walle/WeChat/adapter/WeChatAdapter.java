package bob.eve.walle.WeChat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import bob.eve.walle.R;

/**
 * 仿微信功能列表
 */

public class WeChatAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public WeChatAdapter() {
        super(R.layout.adapter_wechat);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_adapter_name,item);
    }
}
