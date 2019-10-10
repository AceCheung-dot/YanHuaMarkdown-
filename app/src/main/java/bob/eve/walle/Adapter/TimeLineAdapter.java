package bob.eve.walle.Adapter;

import android.content.Context;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bob.eve.walle.R;
import bob.eve.walle.bean.DateText;
import bob.eve.walle.bean.TimeFormat;
import bob.eve.walle.group.GroupRecyclerView;
import bob.eve.walle.ui.activity.UpdateActivity;

public class TimeLineAdapter extends
		RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
	private Context context;

    public List<DateText> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<DateText> mDatas) {
        this.mDatas = mDatas;
    }

    private LayoutInflater mInflater;
	private List<DateText> mDatas;

	public TimeLineAdapter(Context context, List<DateText> mDatas) {
		this.context = context;
		this.mDatas = mDatas;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	// 绑定viewHolder
	@Override
	public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {

        TimeLineViewHolder tvh=	new TimeLineViewHolder(mInflater.inflate(
				R.layout.item_time_line, parent, false));

		tvh.im2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position= tvh.getAdapterPosition();
				int mid=mDatas.get(position).getId();
				Intent i=new Intent(context, UpdateActivity.class);
				i.putExtra("id",mid);
				context.startActivity(i);

			}
		});

        return tvh;

	}

	@Override
	public void onBindViewHolder(TimeLineViewHolder holder, int position) {
		// 时间轴竖线的layout
		LayoutParams params = (LayoutParams) holder.line.getLayoutParams();
		// 第一条数据，肯定显示时间标题

			holder.title.setVisibility(View.VISIBLE);
			holder.date_time.setText(mDatas.get(position).getDate());
			params.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
			params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.img);



		holder.line.setLayoutParams(params);
		holder.date_content.setText(mDatas.get(position).getText());
		holder.img.setImageBitmap(BitmapFactory.decodeByteArray(mDatas.get(position).getIcon(), 0, mDatas.get(position).getIcon().length));
		holder.tv_person.setText(mDatas.get(position).getPerson());
		holder.tv_place.setText(mDatas.get(position).getPlace());

	}

	final class TimeLineViewHolder extends RecyclerView.ViewHolder {
		private TextView date_time, date_content;
		private View line;
		private RelativeLayout title;
        private ImageView img;
		private ImageView im2;
        private TextView tv_person;
		private TextView tv_place;

		public TimeLineViewHolder(View view) {
			super(view);
			date_time = (TextView) view.findViewById(R.id.txt_date_time);
			date_content = (TextView) view.findViewById(R.id.txt_date_content);
			line = (View) view.findViewById(R.id.v_line);
			title = (RelativeLayout) view.findViewById(R.id.rl_title);
			img = (ImageView) view
					.findViewById(R.id.img);
			im2 = (ImageView) view
					.findViewById(R.id.imageView7);
			tv_person=(TextView)view.findViewById(R.id.tV_psname);
			tv_place=(TextView)view.findViewById(R.id.tVmkplace);


		}

	}

}
