package bob.eve.walle.fitpopwindow;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import bob.eve.walle.R;


/**
 * Created by DongJr on 2017/2/21.
 */

public class ListHolder extends RecyclerView.ViewHolder {

    ImageView ivRemove;

    public ListHolder(View itemView) {
        super(itemView);
        ivRemove = (ImageView) itemView.findViewById(R.id.iv_remove);
    }


}
