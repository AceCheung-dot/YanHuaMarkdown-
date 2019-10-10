package bob.eve.walle.WeChat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import bob.eve.walle.R;
import bob.eve.walle.WeChat.bean.SqliteDemo;

public class SQLiteShowAdapter extends BaseQuickAdapter<SqliteDemo, BaseViewHolder> {
    public SQLiteShowAdapter() {
        super(R.layout.adapter_sql_show);
    }

    @Override
    protected void convert(BaseViewHolder helper, SqliteDemo item) {
        helper.setText(R.id.tv_adapter_sql_id, String.valueOf(item.getId()));
        helper.setText(R.id.tv_adapter_sql_name, item.getName());
        helper.setText(R.id.tv_adapter_sql_address, item.getAddress());
    }
}
