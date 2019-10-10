/*
 * Create by BobEve on 17-12-22 下午6:22
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-22 下午6:07
 */

package bob.eve.walle.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import bob.eve.mvp.base.AbsEveBaseActivity;
import bob.eve.mvp.di.component.AppComponent;
import bob.eve.mvp.view.IView;
import bob.eve.walle.Adapter.MarkdownItemAdapter;
import bob.eve.walle.R;
import bob.eve.walle.app.EveApplication;
import bob.eve.walle.config.ImplPreferencesHelper;
import bob.eve.walle.di.component.ActivityComponent;

import bob.eve.walle.di.component.DaggerActivityComponent;
import bob.eve.walle.meizu.MeiZuActivity;
import bob.eve.walle.mvp.presenter.MainPresenter;
import bob.eve.walle.pojo.Gexing;
import bob.eve.walle.pojo.Item;
import bob.eve.walle.pojo.Markdown;
import bob.eve.walle.pojo.User;
import bob.eve.walle.ui.activity.slide.DrawerAdapter;
import bob.eve.walle.ui.activity.slide.DrawerItem;
import bob.eve.walle.ui.activity.slide.SimpleItem;
import bob.eve.walle.ui.activity.slide.SpaceItem;
import bob.eve.walle.ui.fragment.AboutFragment;
import bob.eve.walle.ui.fragment.SettingFragment;
import bob.eve.walle.ui.fragment.tab.HiGirlTabFragment;
import bob.eve.walle.ui.fragment.tab.ITCircleTabFragment;
import bob.eve.walle.ui.fragment.tab.MovieTabFragment;
import bob.eve.walle.ui.fragment.tab.ReadTabFragment;
import bob.eve.walle.ui.view.IdentityImageView;
import bob.eve.walle.util.MyUser;
import bob.eve.walle.util.OvalImageView;
import bob.eve.walle.util.PopupMenuUtil;
import butterknife.*;

import com.baidu.mapapi.map.MapView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.UmengS;
import com.hjq.umeng.UmengService;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import me.yokeyword.fragmentation.SupportFragment;



public class MainActivity extends AbsEveBaseActivity<MainPresenter, ActivityComponent>
		implements DrawerAdapter.OnItemSelectedListener, IView,View.OnClickListener {
	private static final int SLIDE_FRAGMENGT_COUNT = 1;
	private static final int SLIDE_MENU_CODE = 0;
	private static final int SLIDE_MENU_GIRL = 1;
	private static final int SLIDE_MENU_MOVIE = 2;
	private static final int SLIDE_MENU_READ = 3;
	private static final int SLIDE_MENU_SETTING = 4;
	private static final int SLIDE_MENU_ABOUT = 5;
	public static final String TAG = "MainActivity";

	private SupportFragment[] mFragments = new SupportFragment[SLIDE_FRAGMENGT_COUNT];
	private String[] screenTitles;
	private Drawable[] screenIcons;
	int i=0;

	@Inject
	ImplPreferencesHelper preferencesHelper;

	private SlidingRootNav slidingRootNav;
	@BindView(R.id.toolbar)
	public Toolbar mToolbar;
	private DrawerAdapter adapter;
	private int curPosition = 0;

	private ImageView ivImg;
	private IdentityImageView identityImageView;

	private RelativeLayout rlClick;
	private Context context;


	@Override
	public void injectComponent(AppComponent appComponent) {
		DaggerActivityComponent.builder()
													 .appComponent(appComponent)
													 .build()
													 .inject(this);
	}

	@Override
	public ActivityComponent getComponent() {
		return null;
	}

	@Override
	public int layoutResID() {
		return R.layout.activity_main;
	}

	@Override
	public void initView() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int position = preferencesHelper.getFragmentIndex();
setContentView(R.layout.activity_main);
	setToolBar(mToolbar, "Eve");
		initTabFragment();
		initSlide(savedInstanceState, mToolbar);

		if (position != 0 && adapter != null) {
			adapter.setSelected(position);
			preferencesHelper.setFragmentIndex(0);
		}
		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.guojia);
		int bytes = bmp.getByteCount();
		ByteBuffer buf = ByteBuffer.allocate(bytes);
		bmp.copyPixelsToBuffer(buf);
		byte[] byteArray = buf.array();
		MyUser.getMygexing().setGicon(byteArray);
		initViews();
		ImageView  iv=(ImageView)findViewById(R.id.shijianzhou);
		iv.setOnClickListener(this);
		identityImageView=(IdentityImageView)findViewById(R.id.iiv);
		((EveApplication)getApplication()).zhuce();
		((EveApplication)getApplication()).dingwei();
//		((EveApplication)getApplication()).sendWeiZhi();
		identityImageView.setBorderWidth(10);
		identityImageView.setBorderColor(R.color.colorTest);
		identityImageView.getBigCircleImageView().setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.guojia));
		identityImageView.setRadiusScale(1.5f);
		identityImageView.setIsprogress(true);
		//identityImageView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
		identityImageView.setProgressColor(R.color.colorAccent);
		i+=10;
		identityImageView.setProgress(200);
		identityImageView.setOnClickListener(this);
		TextView tvguanyu=(TextView)findViewById(R.id.tVguanyu);
		tvguanyu.setOnClickListener(this);
		TextView zhuxiao=(TextView)findViewById(R.id.tVzx);
		zhuxiao.setOnClickListener(this);
		TextView shezhi=(TextView)findViewById(R.id.tVshezhi);
		shezhi.setOnClickListener(this);
		ImageButton ib=(ImageButton)findViewById(R.id.tuisong);
		ib.setOnClickListener(this);
		ImageButton qunliao=(ImageButton)findViewById(R.id.iv_qunliao);
		qunliao.setOnClickListener(this);
		ImageView tubiao=(ImageView)findViewById(R.id.iVjianjie);
		tubiao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText editText=(EditText)findViewById(R.id.eTjianjie);
				editText.setEnabled(true);
				editText.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText editText=(EditText)findViewById(R.id.eTjianjie);
						Gexing gexing=MyUser.getMygexing();
						gexing.setGjianjie(editText.getText().toString());
						MyUser.setMygexing(gexing);
						updateGexingWithoutImage(gexing);
					}
				});
			}
		});
		TextView tv_info=(TextView)findViewById(R.id.tv_info);
		final String message = getIntent().getStringExtra(UmengS.getMessageBodyTitle());

		if (TextUtils.isEmpty(message))

			return;

	else
		try {


			Map<String, String> extra = UmengS.getMessageMap(message,context);


			AlertDialog dialog;

			if (null != extra && extra.containsKey("msgType")) {
				tv_info.setText(extra.get("title")+",详情请戳我");
				tv_info.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i=new Intent(MainActivity.this,FullDelDemoActivity.class);
						startActivity(i);
					}
				});
                Toast.makeText(this,extra.get("tirile"),Toast.LENGTH_LONG).show();
				Timer t=new Timer();
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						tv_info.setText("");
						tv_info.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						});
					}
				},2*1000);


				dialog = new AlertDialog.Builder(this)

						.setTitle(extra.get("title"))

						.setMessage(extra.get("text")+ "11111")

						.setCancelable(false)

						.setPositiveButton("确认", new DialogInterface.OnClickListener() {

							@Override

							public void onClick(DialogInterface dialog, int which) {

							}

						})

						.create();

			}	else

				dialog = new AlertDialog.Builder(this)

						.setTitle("else消息提示")

						.setMessage(extra.get("title"))

						.setCancelable(false)

						.setPositiveButton("确认", null)

						.create();



			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override

				public void onDismiss(DialogInterface dialog) {

					onBackPressed();

				}

			});

			dialog.show();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}



	private void initTabFragment() {
		SupportFragment itCircleTabFragment = null;
		if (itCircleTabFragment == null) {
			mFragments[SLIDE_MENU_CODE] = ITCircleTabFragment.newInstance();
			//mFragments[SLIDE_MENU_GIRL] = HiGirlTabFragment.newInstance();
			//mFragments[SLIDE_MENU_MOVIE] = MovieTabFragment.newInstance();
			//mFragments[SLIDE_MENU_READ] = ReadTabFragment.newInstance();
			//mFragments[SLIDE_MENU_SETTING] = SettingFragment.newInstance();
			//mFragments[SLIDE_MENU_ABOUT] = AboutFragment.newInstance();

			loadMultipleRootFragment(R.id.container, SLIDE_MENU_CODE, mFragments[SLIDE_MENU_CODE]
															// mFragments[SLIDE_MENU_GIRL],
					//mFragments[SLIDE_MENU_MOVIE],
														//	 mFragments[SLIDE_MENU_READ],
															//mFragments[SLIDE_MENU_SETTING],
															// mFragments[SLIDE_MENU_ABOUT]
					);
		}
		else {
			mFragments[SLIDE_MENU_CODE] = itCircleTabFragment;
			//mFragments[SLIDE_MENU_GIRL] = findFragment(HiGirlTabFragment.class);
			//mFragments[SLIDE_MENU_MOVIE] = findFragment(MovieTabFragment.class);
			//mFragments[SLIDE_MENU_READ] = findFragment(ReadTabFragment.class);
		//	mFragments[SLIDE_MENU_SETTING] = findFragment(SettingFragment.class);
			//mFragments[SLIDE_MENU_ABOUT] = findFragment(AboutFragment.class);
		}
	}

	private void initSlide(Bundle savedInstanceState, Toolbar toolbar) {
		slidingRootNav = new SlidingRootNavBuilder(this).withToolbarMenuToggle(toolbar)
																										.withMenuOpened(false)
																										.withContentClickableWhenMenuOpened(false)
																										.withSavedState(savedInstanceState)
																										.withToolbarMenuToggle(toolbar)
																										.withMenuLayout(R.layout.menu_slide_left_layout)
																										.inject();

		//screenIcons = loadScreenIcons();
		//screenTitles = loadScreenTitles();

		//adapter = new DrawerAdapter(Arrays.asList(createItemFor(SLIDE_MENU_CODE).setChecked(true)
																							//createItemFor(SLIDE_MENU_GIRL), // 妹纸
																							//createItemFor(SLIDE_MENU_MOVIE), // 小电影 哈哈～
																							//new SpaceItem(38), // 空item
																							//createItemFor(SLIDE_MENU_READ) 暂时关闭阅读模块
																						//	createItemFor(SLIDE_MENU_SETTING), // 设置
																							//createItemFor(SLIDE_MENU_ABOUT))
	//	)); // 关于
//		adapter.setListener(this);

//		RecyclerView list = findViewById(R.id.menu_rv);
//		list.setNestedScrollingEnabled(false);
//		list.setLayoutManager(new LinearLayoutManager(this));
//		list.setAdapter(adapter);

	//	adapter.setSelected(SLIDE_MENU_CODE);
        sendRequestWithkHttpURLConnection2(MyUser.getMe().getUid());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void attachView() {

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onItemSelected(int position) {
		if (slidingRootNav != null && slidingRootNav.isMenuOpened()) {
			slidingRootNav.closeMenu();
		}

		if (position == SLIDE_MENU_ABOUT) {
			creatAboutFragment();
			adapter.setSelected(curPosition);
		} else {
			//mToolbar.setTitle(screenTitles[position]);
			showHideFragment(mFragments[position]);
			curPosition = position;
		}
	}

	@ColorInt
	private int color(@ColorRes int res) {
		return ContextCompat.getColor(this, res);
	}

	@Override
	public void start() {

	}

	@Override
	public void complete() {

	}

	@Override
	public void error(Throwable error) {

	}

	@Override
	public void onBackPressedSupport() {
		if (slidingRootNav != null && slidingRootNav.isMenuOpened()) {
			slidingRootNav.closeMenu();
		} else {
			super.onBackPressedSupport();
		}
	}

	private DrawerItem createItemFor(int position) {
		return new SimpleItem(screenIcons[position], screenTitles[position]).withIconTint(
				color(R.color.textColorSecondary))
																																				.withTextTint(color(
																																						R.color.textColorPrimary))
																																				.withSelectedIconTint(color(
																																						R.color.colorAccent))
																																				.withSelectedTextTint(color(
																																						R.color.colorAccent));
	}

	private String[] loadScreenTitles() {
		return getResources().getStringArray(R.array.ld_activityScreenTitles);
	}

	private Drawable[] loadScreenIcons() {
		TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
		Drawable[] icons = new Drawable[ta.length()];
		for (int i = 0; i < ta.length(); i++) {
			int id = ta.getResourceId(i, 0);
			if (id != 0) {
				icons[i] = ContextCompat.getDrawable(this, id);
			}
		}
		ta.recycle();
		return icons;
	}

	public void changeDayNight(boolean isNight) {
		preferencesHelper.setFragmentIndex(curPosition);
		preferencesHelper.setNightModeState(isNight);
		useNightMode(isNight);
	}

	// about配置
	private void creatAboutFragment() {
		//		LibsSupportFragment fragment =
		new LibsBuilder().withLibraries(LIBRARIES)
										 .withAutoDetect(false)
										 .withLicenseShown(true)
										 .withVersionShown(true)
										 .withActivityTitle("关于")
										 .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
										 .start(MainActivity.this);
		//												 .supportFragment();

	}

	private String[] LIBRARIES = new String[] {
			"butterknife", "gson", "sliding-root-nav", "okhttp", "recyclerview_v7", "smartrefresh",
			"folding-cell", "crashreport", "cardview", "recyclerview", "photoview", "dagger", "RxCache",
			"rxjava", "rxandroid"
	};
	private void setMapCustomFile(Context context, String fileName) {
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		String moduleName = null;
		try {
			inputStream = context.getAssets().open("customConfigDir/" + fileName);
			byte[] b = new byte[inputStream.available()];
			inputStream.read(b);
			moduleName = context.getFilesDir().getAbsolutePath();
			File file = new File(moduleName + "/" + fileName);
			if (file.exists()) file.delete();
			file.createNewFile();
			fileOutputStream = new FileOutputStream(file);
			//将自定义样式文件写入本地
			fileOutputStream.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//设置自定义样式文件
		MapView.setCustomMapStylePath(moduleName + "/" + fileName);
	}
	private void initViews() {
		context = this;
		ivImg = (ImageView) findViewById(R.id.iv_img);
		 //recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		rlClick = (RelativeLayout) findViewById(R.id.rl_click);
		rlClick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupMenuUtil.getInstance()._show(context, ivImg);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity
		if(v.getId()==R.id.shijianzhou){
			Intent i=new Intent(MainActivity.this, MeiZuActivity.class);
			startActivity(i);

		}
		else if(v.getId()==R.id.iiv){
			Intent i=new Intent(MainActivity.this, PersonalDataActivity.class);
			startActivity(i);

		}
		else if(v.getId()==R.id.tuisong){
			Intent i=new Intent(MainActivity.this,FullDelDemoActivity.class);
			startActivity(i);

		}
		else if(v.getId()==R.id.iv_qunliao){
			Intent i=new Intent(MainActivity.this,QunLiaoMainActivity.class);
			startActivity(i);

		}
		else if(v.getId()==R.id.tVguanyu){
			Intent i=new Intent(MainActivity.this, AboutActivity.class);
			startActivity(i);

		}
		else if(v.getId()==R.id.tVshezhi){
			Intent i=new Intent(MainActivity.this, SettingActivity.class);
			startActivity(i);

		}
		else if (PopupMenuUtil.getInstance()._isShowing()) {
			PopupMenuUtil.getInstance()._rlClickAction();

		} else {
			super.onBackPressed();
		}
	}
    private void sendRequestWithkHttpURLConnection2(final int id){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/UserServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectuser?mode=a&uid="+id;
                    URL url=new URL(s2);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(8000);
                    InputStream is=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d("info1111",new String(response));
                    showResponse(response.toString());
                    sendRequestWithkHttpURLConnection3(MyUser.getMe().getUid());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try {
                            reader.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gs=new Gson();
                //解析泛型
                Type type=new TypeToken<LinkedList<User>>(){}.getType();
                List<User> list=new Gson().fromJson(response,type);
                for(int i=0;i<list.size();i++){
                    User u=list.get(i);
                    MyUser.setMe(u);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeGexingForUser(u);
                        }
                    });

                }

            }
        });

    }
    public void changeViewForUser(User u) {
        identityImageView=(IdentityImageView)findViewById(R.id.iiv);

        identityImageView.setBorderWidth(10);
        identityImageView.setBorderColor(R.color.colorTest);
        identityImageView.getBigCircleImageView().setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_eve_any));
        identityImageView.setRadiusScale(1.5f);
        identityImageView.setIsprogress(true);
        //identityImageView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        identityImageView.setProgressColor(R.color.colorAccent);
        identityImageView.setProgress(u.getUjingyan()%400);
        TextView tv_userNicheng=(TextView)findViewById(R.id.tV_userNicheng);
        tv_userNicheng.setText(u.getUnicheng());


    }
    private void sendRequestWithkHttpURLConnection3(final int id){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/Gexingervlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectgexing?mode=c&guser="+id;
                    URL url=new URL(s2);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(8000);
                    InputStream is=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d("info1111",new String(response));
                    showResponse2(response.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try {
                            reader.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
    }
    private void showResponse2(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gs=new Gson();
                //解析泛型
                Type type=new TypeToken<LinkedList<Gexing>>(){}.getType();
                List<Gexing> list=new Gson().fromJson(response,type);
                for(int i=0;i<list.size();i++){
                    Gexing gexing=list.get(i);
					MyUser.setIsInsert(false);
                    	//gexing.setGicon(MyUser.getMygexing().getGicon());
                   MyUser.setMygexing(gexing);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeViewForGexing(gexing);
                        }
                    });

                }

            }
        });

    }
    public void changeGexingForUser(User u) {
        identityImageView=(IdentityImageView)findViewById(R.id.iiv);

        identityImageView.setBorderWidth(10);
        identityImageView.setBorderColor(R.color.colorTest);
        identityImageView.getBigCircleImageView().setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.guojia));
        identityImageView.setRadiusScale(1.5f);
        identityImageView.setIsprogress(true);
        //identityImageView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        identityImageView.setProgressColor(R.color.colorAccent);
        identityImageView.setProgress(u.getUjingyan()%400);
        TextView tv_userNicheng=(TextView)findViewById(R.id.tV_userNicheng);
        tv_userNicheng.setText(u.getUnicheng());
        TextView tv_dengji=(TextView)findViewById(R.id.tV_zhuti2);
        tv_dengji.setText("LV-"+(u.getUjingyan()/400));
        TextView tv_jingyan=(TextView)findViewById(R.id.tV_jifen);
        tv_jingyan.setText(""+u.getUjingyan());


    }
    public void changeViewForGexing(Gexing g) {
        TextView tv_zhuti=(TextView)findViewById(R.id.tVzhuti);
        tv_zhuti.setText("烟花"+MyUser.getMygexing().getGzhuti());
		if(MyUser.getMygexing().getGicon().length==0||new String("null").compareTo(new String(MyUser.getMygexing().getGicon()))==0)
			identityImageView.getBigCircleImageView().setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.guojia));
		else
		identityImageView.getBigCircleImageView().setImageBitmap(BitmapFactory.decodeByteArray(g.getGicon(),0,g.getGicon().length));
        EditText tv_qianming=(EditText)findViewById(R.id.eTjianjie);
        tv_qianming.setText(g.getGjianjie());
    }
	private void updateGexingWithoutImage(final Gexing g){//删除用
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection=null;
				BufferedReader reader=null;
				try {
					String s="http://"+MyUser.my_sever+"8080/AndroidSever/GexingServlet?mode=c&"+g.toString();
					String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectgexing";
					URL url=new URL(s);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");

					connection.setConnectTimeout(8000);
					connection.connect();
//					InputStream is=connection.getInputStream();
//					reader=new BufferedReader(new InputStreamReader(is));
//					StringBuilder response=new StringBuilder();
//					String line;
//					while((line=reader.readLine())!=null){
//						response.append(line);
//					}
//					Log.d("info1111",new String(response));

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(reader!=null){
						try {
							reader.close();
							connection.disconnect();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}).start();
	}
}
