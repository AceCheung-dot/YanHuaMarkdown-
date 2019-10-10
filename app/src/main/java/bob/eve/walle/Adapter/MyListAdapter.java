package bob.eve.walle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bob.eve.walle.R;
import bob.eve.walle.pojo.Item;

public class MyListAdapter extends ArrayAdapter<Item> {
    private int resourceId;
    public MyListAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        Item item=getItem(position);
        TextView textView=(TextView)view.findViewById(R.id.tV_item_list);
        textView.setText(item.getKey());
        return view;
    }

}
