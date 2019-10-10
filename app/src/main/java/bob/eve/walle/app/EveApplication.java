/*
 * Create by BobEve on 17-12-12 上午11:20
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-12 上午11:20
 */

/*
 * Create by BobEve on 17-12-12 上午11:20
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-12 上午11:20
 */

package bob.eve.walle.app;

import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengClient;
import com.hjq.umeng.UmengService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import bob.eve.mvp.base.EveBaseApplication;
import bob.eve.mvp.base.IApplication;
import bob.eve.mvp.base.proxy.ApplicationProxy;
import bob.eve.walle.config.ImplPreferencesHelper;
import bob.eve.walle.other.EventBusManager;
import bob.eve.walle.pojo.Bording;
import bob.eve.walle.pojo.DingWei;
import bob.eve.walle.ui.activity.MainActivity;
import bob.eve.walle.util.MyUser;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

import javax.inject.Inject;

/**
 * Created by Bob on 17/12/12.
 */

public class EveApplication extends EveBaseApplication implements IApplication {
	public static int SCREEN_WIDTH = -1;
	public static int SCREEN_HEIGHT = -1;
	public static float DIMEN_RATE = -1.0F;
	public static int DIMEN_DPI = -1;
static String token="";
	@Inject
	ImplPreferencesHelper preferencesHelper;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		initDefaultNightMode(base);
	}

	private void initDefaultNightMode(Context base) {
		boolean isNight = base.getSharedPreferences("eve_sp", Context.MODE_PRIVATE)
				.getBoolean("night_mode", false);
		if (isNight) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplicationProxy = new ApplicationProxy();
		mApplicationProxy.onCreate(this);
		getScreenSize();
		setMapCustomFile(this,"custom_map_config_yanmou.json");
		SDKInitializer.initialize(this);
		Fresco.initialize(this);

		// 初始化
		InitializeService.start(this);
		initSDK(this);
	}

	public void getScreenSize() {
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		Display display = windowManager.getDefaultDisplay();
		display.getMetrics(dm);
		DIMEN_RATE = dm.density / 1.0F;
		DIMEN_DPI = dm.densityDpi;
		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
	}
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
	public static void initSDK(Application application) {
		/**
		 * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
		 * 第一个参数：应用程序上下文
		 * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
		 */
		BGASwipeBackHelper.init(application, null);

		// 初始化吐司工具类
		ToastUtils.init(application);

		// 初始化图片加载器
		ImageLoader.init(application);

		// 初始化 EventBus
		EventBusManager.init();

		// 初始化友盟 SDK
		UmengClient.init(application);
		UmengService.cls=MainActivity.class;


	}
	public void zhuce(){

token=UmengClient.success_token;
		int User= MyUser.getMe().getUid();
		Bording b=new Bording(1,MyUser.getMe().getUid(),token,1);
		sendRequestWithkHttpURLConnection2(b);
	}
	public void dingwei(){
		Timer timer=new Timer();timer.schedule(new TimerTask() {
			@Override
			public void run() {
				DingWei ding=new DingWei(1,MyUser.getMe().getUid(),new Timestamp(System.currentTimeMillis()),MyUser.getMlatitude(),MyUser.getMlongtitude(),MyUser.getMyplace());
				sendRequestWithkHttpURLConnection3(ding);
			}
		},60000);

	}
	private void sendRequestWithkHttpURLConnection2(final Bording b){//按id查询用
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection=null;
				BufferedReader reader=null;
				try {
					String s="http://"+ MyUser.my_sever+":8080/AndroidSever/BordingServlet?mode=a&"+b.toString();
					String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectbordingcontent?mode=a&buser=";
					URL url=new URL(s);
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
	private void sendRequestWithkHttpURLConnection3(final DingWei d){//按id查询用
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection=null;
				BufferedReader reader=null;
				try {
					String s="http://"+ MyUser.my_sever+":8080/AndroidSever/DingWeiServlet?mode=a&"+d.toString();
					String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectbordingcontent?mode=a&buser=";
					URL url=new URL(s);
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
