package bob.eve.walle.pojo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

import bob.eve.walle.R;

public class FullDelDemoAdapter extends RecyclerView.Adapter<FullDelDemoAdapter.FullDelDemoVH> {
    private Context mContext;
    private LayoutInflater mInfalter;
    private List<SwipeBean> mDatas;

    public FullDelDemoAdapter(Context context, List<SwipeBean> mDatas) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public FullDelDemoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FullDelDemoVH(mInfalter.inflate(R.layout.item_cst_swipe, parent, false));
    }
    public Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }


    @Override
    public void onBindViewHolder(final FullDelDemoVH holder, final int position) {
        ((SwipeMenuLayout) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);//这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑

        holder.tv_content.setText(mDatas.get(position).getContent());
        holder.tv_name.setText(mDatas.get(position).getName());
        holder.tv_tittle.setText(mDatas.get(position).getTitle());
        holder.tv_time.setText(mDatas.get(position).getTime());
       RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(100,100);
        Fresco.initialize(mContext);
        if(mDatas.get(position).getName().compareTo("系统")!=0){
            holder.iv_headView.setBackgroundResource(R.drawable.guojia);
            holder.iv_headView.setBackgroundResource(R.drawable.ic_launcher_foreground);
        }
holder.iv_headView.setLayoutParams(lp);

        //验证长按
//        holder.content.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(mContext, "longclig", Toast.LENGTH_SHORT).show();
//                Log.d("TAG", "onLongClick() called with: v = [" + v + "]");
//                return false;
//            }
//        });

        holder.btnUnRead.setVisibility(position % 3 == 0 ? View.GONE : View.VISIBLE);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。

        //置顶：
        holder.btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnSwipeListener){
                    mOnSwipeListener.onTop(holder.getAdapterPosition());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return null != mDatas ? mDatas.size() : 0;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    class FullDelDemoVH extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_time;
        TextView tv_tittle;
        TextView tv_content;
        SimpleDraweeView iv_headView;
        Button btnDelete;
        Button btnUnRead;
        Button btnTop;

        public FullDelDemoVH(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.text_name);
            tv_time = (TextView) itemView.findViewById(R.id.text_date);
            tv_tittle=(TextView)itemView.findViewById(R.id.text_job_title);
            tv_content=(TextView)itemView.findViewById(R.id.text_question);
            iv_headView=(SimpleDraweeView)itemView.findViewById(R.id.avatar);

            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnUnRead = (Button) itemView.findViewById(R.id.btnUnRead);
            btnTop = (Button) itemView.findViewById(R.id.btnTop);
        }
    }
}