package bob.eve.walle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;



import java.util.LinkedList;
import java.util.List;

import bob.eve.walle.R;
import bob.eve.walle.pojo.Item;


public class MarkdownItemAdapter extends ArrayAdapter<Item> implements View.OnFocusChangeListener {
    private int resourceId;
    LinkedList<Item> items=new LinkedList<Item>();
    public MarkdownItemAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    public LinkedList<Item> getItems() {
        return items;
    }

    public void setItems(LinkedList<Item> items) {
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        Item item=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView textView=(TextView)view.findViewById(R.id.tV_itemName);

        EditText editText=(EditText)view.findViewById(R.id.eT_item);
        editText.setOnFocusChangeListener(this);
        editText.setId(item.getId());
        editText.setText(item.getValue());
        textView.setText(item.getKey());
        return view;
    }


    @Override
    public Item getItem(int position) {


        return super.getItem(position);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus==false){
            EditText et=(EditText)v;
            for(int i=0;i<items.size();i++)
        {
            if(items.get(i).getId()==et.getId())
            {
                items.get(i).setValue(et.getText().toString());
            }
        }
        }
    }
//    @Override
//    public void afterTextChanged(Editable s) {
//        EditText et=(EditText)s;
//
//    }
}
