package bob.eve.walle.ui.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bob.eve.walle.R;

public class adapter1 extends RecyclerView.Adapter<adapter1.ViewHolder> {//适配器
    private List<xx> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView zuoimg, youimg;
        TextView zuotext, youtext;
        ViewGroup zuolin, youlin;

        public ViewHolder(View itemView) {
            super(itemView);
            zuolin = itemView.findViewById(R.id.zuo);
            zuoimg = itemView.findViewById(R.id.zuoimg);
            zuotext = itemView.findViewById(R.id.zuotext);
            youlin = itemView.findViewById(R.id.you);
            youimg = itemView.findViewById(R.id.youimg);
            youtext = itemView.findViewById(R.id.youtext);
        }
    }

    public adapter1(List l) {
        list = l;
    }

    @Override
    public adapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.xx_qunliao, parent, false);
        ViewHolder h = new ViewHolder(v);
        Log.d("QunLiaoMainActivity", "onCreate");
        return h;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//滑动RecyclerView出发的事件
        xx x = list.get(position);
        if (x.zuo) {//判断该信息该信息是显示在左边还是右边，如果要在左边显示则把右边的部分隐藏
            holder.zuolin.setVisibility(View.VISIBLE);
            holder.youlin.setVisibility(View.GONE);//把右边的隐藏
            holder.zuoimg.setImageResource(x.img);
            holder.zuotext.setText(x.text);
        }else{
            holder.youlin.setVisibility(View.VISIBLE);
            holder.zuolin.setVisibility(View.GONE);//把左边的隐藏
            holder.youimg.setImageResource(x.img);
            holder.youtext.setText(x.text);
        }
        Log.d("QunLiaoMainActivity", "onBind");
    }

    @Override
    public int getItemCount() {//这里要重写一下 不然不会显示任何信息
        Log.d("asdasd", "" + list.size());
        return list.size();
    }
}

class xx {//信息类
    public String text;//信息内容
    public int img;//头像的图片id
    public boolean zuo = true;//控制信息显示在左边还有右边，默认左边

    public xx(String s, int i) {
        text = s;
        img = i;
    }

    public xx(String s, int i, boolean b) {
        text = s;
        img = i;
        zuo = b;
    }
}