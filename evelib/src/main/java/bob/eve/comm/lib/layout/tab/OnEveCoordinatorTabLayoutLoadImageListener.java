/*
 * Create by BobEve on 17-12-18 下午6:58
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-18 下午2:45
 */

package bob.eve.comm.lib.layout.tab;

import com.google.android.material.tabs.TabLayout;
import android.widget.ImageView;

/**
 * Created by Bob on 17/12/18.
 */

public interface OnEveCoordinatorTabLayoutLoadImageListener {
	void loadHeaderImages(ImageView imageView, TabLayout.Tab tab);
}
