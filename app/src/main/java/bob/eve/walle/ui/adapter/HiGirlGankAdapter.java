/*
 * Create by BobEve on 17-12-22 上午12:18
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-21 下午5:59
 */

package bob.eve.walle.ui.adapter;

import android.graphics.Bitmap;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

;
import android.view.ViewGroup;
import android.widget.ImageView;
import bob.eve.walle.R;
import bob.eve.walle.app.EveApplication;
import bob.eve.walle.mvp.model.bean.gank.GankItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Bob on 17/12/19.
 */

public class HiGirlGankAdapter extends BaseQuickAdapter<GankItem, BaseViewHolder> {
	private Fragment fragment;

	public HiGirlGankAdapter(Fragment fragment, @LayoutRes int layoutResId) {
		super(layoutResId);
		this.fragment = fragment;
	}

	@Override
	public int getItemViewType(int position) {
		if (getData().size() <= 0) {
			return 0;
		}

		return Math.round((float) EveApplication.SCREEN_WIDTH / (float) getData().get(position)
																																						 .getHeight() *
											10f);
	}

	@Override
	public void onBindViewHolder(final BaseViewHolder holder, final int position) {
		//存在记录的高度时先Layout再异步加载图片
		if (getData().size() > 0) {
			if (getData().get(position)
									 .getHeight() > 0) {
				ViewGroup.LayoutParams params = holder.getView(R.id.ig_image)
																							.getLayoutParams();
				params.height = getData().get(position)
																 .getHeight();
				params.width = EveApplication.SCREEN_WIDTH / 2;
				holder.getView(R.id.ig_image)
							.setLayoutParams(params);
			}
		}
		super.onBindViewHolder(holder, position);
	}

	@Override
	protected void convert(final BaseViewHolder helper, final GankItem item) {
		Glide.with(mContext)
						.asBitmap()
						.load(item.getUrl())
						.diskCacheStrategy(DiskCacheStrategy.ALL)
						.into(new SimpleTarget<Bitmap>(EveApplication.SCREEN_WIDTH / 2,
																					 EveApplication.SCREEN_WIDTH / 2) {
							@Override
							public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
								if (helper.getAdapterPosition() != RecyclerView.NO_POSITION) {
									if (item.getHeight() <= 0) {
										final ImageView image = helper.getView(R.id.ig_image);
										int imageHeight =
												(EveApplication.SCREEN_WIDTH / 2) * resource.getHeight() /
												resource.getWidth();
										item.setHeight(imageHeight);

										ViewGroup.LayoutParams params = image.getLayoutParams();
										params.height = imageHeight;
										image.setLayoutParams(params);
									}

									((ImageView) helper.getView(R.id.ig_image)).setImageBitmap(resource);
								}
							}
						});
	}
}