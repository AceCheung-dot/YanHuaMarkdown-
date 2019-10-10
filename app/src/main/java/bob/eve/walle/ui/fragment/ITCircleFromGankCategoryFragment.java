/*
 * Create by BobEve on 17-12-19 上午10:48
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-19 上午10:48
 */

package bob.eve.walle.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import bob.eve.mvp.di.scope.PreFragment;
import bob.eve.walle.R;
import bob.eve.walle.app.EveDiBaseFragment;
import bob.eve.walle.di.component.FragmentComponent;
import bob.eve.walle.mvp.contract.ITCircleFromGankCategoryContract;
import bob.eve.walle.mvp.model.bean.gank.GankItem;
import bob.eve.walle.mvp.presenter.ITCircleFromCategoryPresenter;
import bob.eve.walle.pojo.Item;
import bob.eve.walle.pojo.Markdown;
import bob.eve.walle.ui.activity.BNaviGuideActivity;
import bob.eve.walle.ui.activity.UpdateActivity;
import bob.eve.walle.ui.activity.WNaviGuideActivity;
import bob.eve.walle.ui.activity.insertActivity;

import bob.eve.walle.util.MyUser;
import bob.eve.walle.util.OvalImageView;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.bikenavi.params.BikeRouteNodeInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Bob on 17/12/19.
 */
@PreFragment
public class  ITCircleFromGankCategoryFragment
		extends EveDiBaseFragment<ITCircleFromCategoryPresenter>
		implements ITCircleFromGankCategoryContract.View, OnRefreshLoadmoreListener,
							 BaseQuickAdapter.OnItemClickListener,BaiduMap.OnMarkerClickListener,View.OnClickListener {
	private final static String TAG = "BNaviApplication";
	public static final int WITH_IMAGE = 1;
	public static final int WITHOUT_IMAGE = 2;
	public static final int WITH_GIRL = 3;
	 static ArrayList<BitmapDescriptor> link2 ;
	static ArrayList<BitmapDescriptor> link3 ;
	Bitmap bmp = null;
	Bitmap newbmp = null;
	int k = 0;
	String myplace = "天津城建大学";
	static private MapView mMapView;
	static private BaiduMap mBaiduMap;
	static public LocationClient mLocationClient;
	LinkedList<Item> myList = new LinkedList<Item>();

	/*导航起终点Marker，可拖动改变起终点的坐标*/
	static private Marker mStartMarker;
	static private Marker mEndMarker;
	public MyLocationListener myLocationListener = new MyLocationListener();

	private LatLng startPt;
	private LatLng endPt;
	boolean isFirstLocate = true;
	double longtitude;
	double latitude;
	public boolean b = true;
	int i = 0;

	private BikeNaviLaunchParam bikeParam;
	private WalkNaviLaunchParam walkParam;

	private static boolean isPermissionRequested = false;

	private BitmapDescriptor bdStart = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_start);
	private BitmapDescriptor bdEnd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_end);
	MyLocationConfiguration.LocationMode locationMode = MyLocationConfiguration.LocationMode.NORMAL;//普通模式
	static ITCircleFromGankCategoryFragment fragment;

	public static ITCircleFromGankCategoryFragment newInstance(String category, boolean b) {

		fragment = new ITCircleFromGankCategoryFragment();
		fragment.setB(b);


		return fragment;
	}

	public boolean isB() {
		return b;
	}

	public void setB(boolean b) {
		this.b = b;
	}

	@Override
	public int layoutResID() {
		return R.layout.fragment_common_recycler_layout;
	}

	@Override
	public void attachView() {
		mPresenter.attach(this);
	}

	@Override
	public void initView(View view) {
		initView1(view);
	}

	public void initView1(View view) {
		requestPermission();
		mMapView = (MapView) view.findViewById(R.id.mapview);
		initMapStatus();
		if(link2==null)link2=new ArrayList<>();
		if(link3==null)link3=new ArrayList<>();


		mLocationClient = new LocationClient(this.getActivity());
		mLocationClient.registerLocationListener(myLocationListener);

		//mTextMessage = (TextView) findViewById(R.id.message);
		final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) getActivity().findViewById(R.id.fam_menu);

		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setOnMarkerClickListener(this);
		if(MyUser.isB()){
			MapView.setMapCustomEnable(true);
			mBaiduMap.clear();//清屏
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
					setScreenBrightness(0);
					MapView.setMapCustomEnable(true);
					mBaiduMap.setMyLocationEnabled(false);
					mBaiduMap.clear();

				}
			});



		}
		sendRequestWithkHttpURLConnection();

		ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
		drawable.getPaint().setColor(getResources().getColor(R.color.white));


		// Test that FAMs containing FABs with visibility GONE do not cause crashes
		//getActivity().findViewById(R.id.fal_ar).setVisibility(View.GONE);


		final FloatingActionButton insertBtn = (FloatingActionButton) menuMultipleActions.findViewById(R.id.insert_button);

		insertBtn.setOnClickListener(this);

		/*骑行导航入口*/

		final FloatingActionButton bikeBtn = (FloatingActionButton) menuMultipleActions.findViewById(R.id.fal_bike);
		bikeBtn.setIcon(R.drawable.icon_bike);
		bikeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startBikeNavi();
			}
		});

		/*普通步行导航入口*/
		final FloatingActionButton walkBtn = (com.getbase.floatingactionbutton.FloatingActionButton) menuMultipleActions.findViewById(R.id.fal_walk);
		walkBtn.setIcon(R.drawable.icon_walk);
		walkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				walkParam.extraNaviMode(0);
				startWalkNavi();
			}
		});
		final FloatingActionButton dingwei = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.flb_dingwei);
		dingwei.setIcon(R.drawable.icon_dingwei);
		dingwei.setOnClickListener(this);
		/*AR步行导航入口*/
		final FloatingActionButton arWalkBtn = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fal_ar);

		arWalkBtn.setIcon(R.drawable.icon_ar);
		arWalkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				walkParam.extraNaviMode(1);
				startWalkNavi();
			}
		});

		startPt = new LatLng(40.057038, 116.307899);
		endPt = new LatLng(40.035916, 116.340722);

//mBaiduMap.setBaiduHeatMapEnabled(true);

		/*构造导航起终点参数对象*/
		BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
		bikeStartNode.setLocation(startPt);
		BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
		bikeEndNode.setLocation(endPt);

		bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

		WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
		walkStartNode.setLocation(startPt);
		WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
		walkEndNode.setLocation(endPt);
		walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);
		mBaiduMap.setMyLocationEnabled(true);//开启地图的定位图层
		/* 初始化起终点Marker */
		initOverlay();
		initLocation();
		if(!MyUser.isB()){
		LatLng lll = new LatLng(longtitude,
				latitude);
		MapStatus.Builder builders = new MapStatus.Builder();
		builders.target(lll).zoom(18.0f).overlook(-45).rotate(0);
		if (i == 0) mBaiduMap.animateMapStatus(MapStatusUpdateFactory
				.newMapStatus(builders.build()));}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//如果当前平台版本大于23平台
			if (!Settings.System.canWrite(this.getContext())) {
				//如果没有修改系统的权限这请求修改系统的权限
				Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
				intent.setData(Uri.parse("package:" + this.getActivity().getPackageName()));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivityForResult(intent, 0);

			} else {
				//有了权限，你要做什么呢？具体的动作

			}
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(link2.size()==0) {
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua1).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua2).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua3).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua4).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua5).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua6).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua7).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yanhua8).copy(Bitmap.Config.ARGB_8888, true);
					link2.add(BitmapDescriptorFactory.fromBitmap(bmp));
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mBaiduMap.setOnMarkerClickListener(ITCircleFromGankCategoryFragment.this);
						}
					});


				}
			}
		}).start();



	}

	private void initMapStatus() {
		mBaiduMap = mMapView.getMap();
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(new LatLng(40.048424, 116.313513)).zoom(15);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
	}

	public void initLocation() {//初始化地图，设置参数
       /* BaiduMapOptions options = new BaiduMapOptions();
        options.overlookingGesturesEnabled(false);//是否允许俯视图手势
        options.rotateGesturesEnabled(true);//是否允许地图旋转手势，默认允许*/


		MyLocationConfiguration mLocationConfiguration = new MyLocationConfiguration(locationMode, true, null, 0xCCCCFF, 0xFF77FF);

		mBaiduMap.setMyLocationConfiguration(mLocationConfiguration);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setScanSpan(2000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		option.setNeedDeviceDirect(true); //返回的定位结果包括手机机头方向

		option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
		initMyLocationOverlay();
		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	public void initOverlay() {

		MarkerOptions ooA = new MarkerOptions().position(startPt).icon(bdStart)
				.zIndex(9).draggable(true);

		mStartMarker = (Marker) (mBaiduMap.addOverlay(ooA));
		mStartMarker.setDraggable(true);
		MarkerOptions ooB = new MarkerOptions().position(endPt).icon(bdEnd)
				.zIndex(5);
		mEndMarker = (Marker) (mBaiduMap.addOverlay(ooB));
		mEndMarker.setDraggable(true);

		mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				if (marker == mStartMarker) {
					startPt = marker.getPosition();
				} else if (marker == mEndMarker) {
					endPt = marker.getPosition();
				}

				BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
				bikeStartNode.setLocation(startPt);
				BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
				bikeEndNode.setLocation(endPt);
				bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

				WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
				walkStartNode.setLocation(startPt);
				WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
				walkEndNode.setLocation(endPt);
				walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
	}

	/**
	 * 开始骑行导航
	 */
	private void startBikeNavi() {
		Log.d(TAG, "startBikeNavi");
		try {
			BikeNavigateHelper.getInstance().initNaviEngine(getActivity(), new IBEngineInitListener() {
				@Override
				public void engineInitSuccess() {
					Log.d(TAG, "BikeNavi engineInitSuccess");
					routePlanWithBikeParam();
				}

				@Override
				public void engineInitFail() {
					Log.d(TAG, "BikeNavi engineInitFail");
					BikeNavigateHelper.getInstance().unInitNaviEngine();
				}
			});
		} catch (Exception e) {
			Log.d(TAG, "startBikeNavi Exception");
			e.printStackTrace();
		}
	}

	/**
	 * 开始步行导航
	 */
	private void startWalkNavi() {
		Log.d(TAG, "startWalkNavi");
		try {
			WalkNavigateHelper.getInstance().initNaviEngine(this.getActivity(), new IWEngineInitListener() {
				@Override
				public void engineInitSuccess() {
					Log.d(TAG, "WalkNavi engineInitSuccess");
					routePlanWithWalkParam();
				}

				@Override
				public void engineInitFail() {
					Log.d(TAG, "WalkNavi engineInitFail");
					WalkNavigateHelper.getInstance().unInitNaviEngine();
				}
			});
		} catch (Exception e) {
			Log.d(TAG, "startBikeNavi Exception");
			e.printStackTrace();
		}
	}

	/**
	 * 发起骑行导航算路
	 */
	private void routePlanWithBikeParam() {
		BikeNavigateHelper.getInstance().routePlanWithRouteNode(bikeParam, new IBRoutePlanListener() {
			@Override
			public void onRoutePlanStart() {
				Log.d("BaiduMap", "BikeNavi onRoutePlanStart");
			}

			@Override
			public void onRoutePlanSuccess() {
				Log.d(TAG, "BikeNavi onRoutePlanSuccess");
				Intent intent = new Intent();
				intent.setClass(getActivity(), BNaviGuideActivity.class);
				startActivity(intent);
			}

			@Override
			public void onRoutePlanFail(BikeRoutePlanError error) {
				Log.d(TAG, "BikeNavi onRoutePlanFail");
			}

		});
	}

	/**
	 * 发起步行导航算路
	 */
	private void routePlanWithWalkParam() {
		WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkParam, new IWRoutePlanListener() {
			@Override
			public void onRoutePlanStart() {
				Log.d("BaiduMap", "WalkNavi onRoutePlanStart");
			}

			@Override
			public void onRoutePlanSuccess() {

				Log.d("BaiduMap", "onRoutePlanSuccess");

				Intent intent = new Intent();
				intent.setClass(getActivity(), WNaviGuideActivity.class);
				startActivity(intent);

			}

			@Override
			public void onRoutePlanFail(WalkRoutePlanError error) {
				Log.d(TAG, "WalkNavi onRoutePlanFail");
			}

		});
	}

	/**
	 * Android6.0之后需要动态申请权限
	 */
	private void requestPermission() {
		if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

			isPermissionRequested = true;

			ArrayList<String> permissionsList = new ArrayList<>();

			String[] permissions = {
					Manifest.permission.RECORD_AUDIO,
					Manifest.permission.ACCESS_NETWORK_STATE,
					Manifest.permission.INTERNET,
					Manifest.permission.READ_PHONE_STATE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.MODIFY_AUDIO_SETTINGS,
					Manifest.permission.WRITE_SETTINGS,
					Manifest.permission.ACCESS_WIFI_STATE,
					Manifest.permission.CHANGE_WIFI_STATE,
					Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
					Manifest.permission.WRITE_SETTINGS


			};

			for (String perm : permissions) {
				if (PackageManager.PERMISSION_GRANTED != this.getActivity().checkSelfPermission(perm)) {
					permissionsList.add(perm);
					// 进入到这里代表没有权限.
				}
			}

			if (permissionsList.isEmpty()) {
				return;
			} else {
				requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 0);
			}
		}
	}

	private void navifateTo(BDLocation location) {

		if (isFirstLocate) {
			mBaiduMap.setMyLocationEnabled(false);
			longtitude = location.getLongitude();
			latitude = location.getLatitude();
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(update);
			update = MapStatusUpdateFactory.zoomTo(18f);
			mBaiduMap.animateMapStatus(update);

			MapStatus.Builder builders = new MapStatus.Builder();
			builders.target(ll).zoom(18.0f).overlook(-45).rotate(0);
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory
					.newMapStatus(builders.build()));
		isFirstLocate=false;
			View popMarker1 = View.inflate(getActivity(), R.layout.pop, null);
			popMarker1.setDrawingCacheEnabled(true);
			OvalImageView iv1=(OvalImageView) popMarker1.findViewById(R.id.iv_title);
			TextView tv1=(TextView) popMarker1.findViewById(R.id.tv_title);
			iv1.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.lanqiu));
			iv1.setStrokeColot(Color.argb(0.2F,1,1,1));
			iv1.setStrokeWidth(9);
			tv1.setText("   "+MyUser.getMe().getUnicheng());
			Bitmap bitmap = getViewBitmap(popMarker1);
			if(!MyUser.isB()){
			BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromBitmap(bitmap);
			MarkerOptions option3 = new MarkerOptions()//构建MarkerOption，用于在地图上添加Marker
					.position(new LatLng(latitude,longtitude))
					.animateType(MarkerOptions.MarkerAnimateType.none)//生长动画
					.perspective(true)//是否开启远大近小
					.draggable(true)//是否可以拖拽
					.title("1")
					.visible(true)//是否可见
					.flat(false)////设置平贴地图，在地图中双指下拉查看效果
					.icon(bitmapDescriptor1)
					.alpha(1f);//透明度
			mBaiduMap.addOverlay(option3);
			mBaiduMap.setMyLocationEnabled(false);
			}

		}





			//构造InfoWindow
			// point 描述的位置点
			// -100 InfoWindow相对于point在y轴的偏移量

			//使InfoWindow生效



			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				// GPS定位结果
             /*   Toast.makeText(getActivity(),
                        "GPS定位结果" + location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();*/
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				//网络定位结果
             /*   Toast.makeText(getActivity(),
                        "网络定位结果：" + location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();*/

			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                /*// 离线定位结果
                Toast.makeText(getActivity(),
                        "离线定位结果：" + location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();*/

			} else if (location.getLocType() == BDLocation.TypeServerError) {
				Toast.makeText(this.getContext(), "服务器错误，请检查",
						Toast.LENGTH_SHORT).show();
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				Toast.makeText(this.getContext(), "网络错误，请检查",
						Toast.LENGTH_SHORT).show();
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				Toast.makeText(this.getContext(), "手机模式错误，请检查是否飞行",
						Toast.LENGTH_SHORT).show();
			}



		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(location.getDirection()).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		mBaiduMap.setMyLocationData(locData);
	}
	void initMyLocationOverlay(){





		// 设置定位数据



	}

	private void requestLocation() {//请求初始化地图
		initLocation();
		mLocationClient.start();//开始定位
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker.getTitle().compareTo("1") != 0) {
			int id = new Integer(marker.getTitle());
			Intent i=new Intent(getContext(), UpdateActivity.class);
			i.putExtra("id",id);
			startActivity(i);
		}
		return false;
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(final BDLocation location) {

			if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
				navifateTo(location);
			}
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String state = location.getCountry();
					String street = location.getStreet();
					String s = location.getAddrStr();
					// 构造定位数据
					double jingdu = location.getLatitude();
					double weidu = location.getLongitude();

					StringBuilder currentPosition = new StringBuilder();
					currentPosition.append("纬度：" + location.getLatitude()).append("\n");
					currentPosition.append("经度：").append(location.getLongitude()).append("\n");
					currentPosition.append("国家：").append(location.getCountry()).append("\n");
					currentPosition.append("省：").append(location.getProvince()).append("\n");
					currentPosition.append("市：").append(location.getCity()).append("\n");
					currentPosition.append("区：").append(location.getDistrict()).append("\n");
					currentPosition.append("街道：").append(location.getStreet()).append("\n");
					currentPosition.append("定位方式：");
					myplace = location.getStreet();

					if (location.getLocType() == BDLocation.TypeGpsLocation) {
						currentPosition.append("GPS");
					} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
						currentPosition.append("网络");
					}
					latitude = location.getLatitude();
					longtitude = location.getLongitude();
					MyUser.setMlatitude(longtitude);
					MyUser.setMlatitude(latitude);
					MyUser.setMyplace(myplace);


							// 此处设置开发者获取到的方向信息，顺时针0-360



				}
			});
		}
	}

	void sendRequestWithkHttpURLConnection() {//全部查询用
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				BufferedReader reader = null;
				try {
					String s = "http://" + MyUser.my_sever + ":8080/AndroidSever/MarkdownServlet";
					String s2 = "http://" + MyUser.my_sever + ":8080/AndroidSever/selectmarkdown";
					URL url = new URL(s2);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");

					connection.setConnectTimeout(18000);
					InputStream is = connection.getInputStream();
					reader = new BufferedReader(new InputStreamReader(is));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					Log.d("info1111", new String(response));
					//if(!b)showResponse(response.toString());

					if (MyUser.isB()) {
						onInsertBack(response.toString());
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								//MyUser.setB(false);
							}
						});

					} else {

						showResponse(response.toString());
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
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

	private void showResponse(final String response) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {//注：这个县城用于修改TextView的信息、跳转等凡是跟UI油管的操作
				Gson gs = new Gson();
				//解析泛型
				mBaiduMap.clear();
				LinkedList<OverlayOptions> markers = new LinkedList<OverlayOptions>();
				//这里的类型（凡是有Markdown的地方）也可以换成自己的。
				Type type = new TypeToken<LinkedList<Markdown>>() {
				}.getType();
				List<Markdown> list = new Gson().fromJson(response, type);
				for (int i = 0; i < list.size(); i++) {
					Markdown m = list.get(i);
					System.out.println("111111222222" + m.toString());
					Resources res = getResources();
					Button button5 = new Button(getContext());

					button5.setText(m.getMtitle());
					//构造InfoWindow
					// point 描述的位置点
					// -100 InfoWindow相对于point在y轴的偏移量

					Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.khs);
					LatLng my_local = new LatLng(m.getMlatitude(), m.getMlongtitude());
					InfoWindow mInfoWindow5 = new InfoWindow(button5, my_local, -100);
					MarkerOptions option2 = new MarkerOptions()//构建MarkerOption，用于在地图上添加Marker
							.position(my_local)
							.animateType(MarkerOptions.MarkerAnimateType.none)//生长动画
							.perspective(true)//是否开启远大近小
							.draggable(true)//是否可以拖拽
							.infoWindow(mInfoWindow5)
							.title("" + m.getMid())
							.visible(true)//是否可见
							.flat(false)////设置平贴地图，在地图中双指下拉查看效果
							.icon(bdStart)
							.alpha(1f);//透明度

					Marker m1=(Marker)mBaiduMap.addOverlay(option2);

					b = true;



				}
				View popMarker = View.inflate(getActivity(), R.layout.pop, null);
				popMarker.setDrawingCacheEnabled(true);
				OvalImageView iv=(OvalImageView) popMarker.findViewById(R.id.iv_title);
				TextView tv=(TextView) popMarker.findViewById(R.id.tv_title);
				iv.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.lanqiu));
				iv.setStrokeColot(Color.argb(0.2F,1,1,1));
				iv.setStrokeWidth(9);
				tv.setText("   "+MyUser.getMe().getUnicheng());
				Bitmap bitmap1 = getViewBitmap(popMarker);
				BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap1);
				MarkerOptions option3 = new MarkerOptions()//构建MarkerOption，用于在地图上添加Marker
						.position(new LatLng(latitude,longtitude))
						.animateType(MarkerOptions.MarkerAnimateType.none)//生长动画
						.perspective(true)//是否开启远大近小
						.draggable(true)//是否可以拖拽
						.title("" + 1)
						.visible(true)//是否可见
						.flat(false)////设置平贴地图，在地图中双指下拉查看效果
						.icon(bitmapDescriptor)
						.alpha(1f);//透明度
				mBaiduMap.addOverlay(option3);
				mBaiduMap.setMyLocationEnabled(false);


			}
		});

	}

	@Override
	public void initLazyView(View view) {
		//initView1(view);
	}

	@Override
	public void showTechs(List<GankItem> items) {

	}

	@Override
	public void showMoreTechs(List<GankItem> items) {

	}

	@Override
	public void showTopGirl(GankItem girl) {

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
	protected void inject(FragmentComponent fragmentComponent) {
		fragmentComponent.inject(this);
	}

	@Override
	public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

	}

	@Override
	public void onLoadmore(RefreshLayout refreshlayout) {

	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.insert_button) {
			Intent i2 = new Intent(this.getActivity(), insertActivity.class);
			i2.putExtra("mstate", 1);
			i2.putExtra("mlatitude", latitude);
			i2.putExtra("mlongtitude", longtitude);
			i2.putExtra("myplace", myplace);
			startActivity(i2);
		} else if (v.getId() == R.id.flb_dingwei) {
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(latitude, longtitude), 16));


		}
	}

	public void onInsertBack(final String response) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {//注：这个县城用于修改TextView的信息、跳转等凡是跟UI油管的操作
				mBaiduMap.setMyLocationEnabled(false);
				MapView.setMapCustomEnable(true);
				mBaiduMap.clear();

				//mMapView.setBackgroundColor(getResources().getColor(R.color.half_black));

				//设置夜间模式
				//AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
				//setScreenBrightness(200);

				//mMapView.dispatchSystemUiVisibilityChanged(0);

				Gson gs = new Gson();
				//解析泛型

				LinkedList<OverlayOptions> markers = new LinkedList<OverlayOptions>();

				//对Markers进行倒转(将Markers)
				//Collections.reverse(markers);
				//这里的类型（凡是有Markdown的地方）也可以换成自己的。
				Type type = new TypeToken<LinkedList<Markdown>>() {
				}.getType();
				List<Markdown> list = new Gson().fromJson(response, type);
				int mtype = 1;
				for (int i = list.size() - 1; i >=0; i--) {
					Markdown m = list.get(i);


					Resources res = getResources();
					LatLng my_local = new LatLng(m.getMlatitude(), m.getMlongtitude());
					if (i == list.size() - 1) {
						mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(m.getMlatitude()+0.01, m.getMlongtitude()), 16F));
					}

					MarkerOptions option2 = new MarkerOptions()//构建MarkerOption，用于在地图上添加Marker
							.position(my_local)
							.perspective(false)//是否开启远大近小
							.draggable(true)//是否可以拖拽
							.period(15)//刷新周期
							//.infoWindow(mInfoWindow5)
							.title("" + m.getMid())
							.visible(true)//是否可见
							.flat(false);////设置平贴地图，在地图中双指下拉查看效果
					option2.icons(link2);


					if (i != list.size() - 1) option2.alpha(0.5f);//透明度
					mBaiduMap.addOverlay(option2);
				}





				Timer timer=new Timer();

				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setScreenBrightness(50);

							}
						});

					}
				},750);
                timer.schedule(new TimerTask() {
					@Override
					public void run() {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setScreenBrightness(150);

							}
						});

					}
				},1000);


				timer.schedule(new TimerTask() {
					@Override
					public void run() {





						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {

								setScreenBrightness(200);


//								LinearLayout linear = (LinearLayout) getActivity().findViewById(R.id.linearlayout_1);
//								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//								RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//								//设置linear的宽和高覆盖全屏幕
//								linear.setLayoutParams(rp);
//								//	linear.setBackgroundResource(R.drawable.a2);
//								SplashView3 sw3 = new SplashView3(getActivity());
//								//	sw3.setBackground(RoundedBitmapDrawableFactory.create(getResources(),BitmapFactory.decodeResource(getResources(),R.drawable.a2)));
								MapView.setMapCustomEnable(false);
//								linear.addView(sw3);
//								sw3.setLayoutParams(lp);
								showResponse2(response);


								//添加文本,this代表当前项目		TextView tv=new TextView(this);		tv.setText("示例文本框");		tv.setId(1);//设置ID，可有可无，也可以在R文件中添加字符串，然后在这里使用引用的方式使用		linear.addView(tv);			     // 将Button 加入到LinearLayout 中	      Button b1 = new Button(this);	      b1.setText("取消");	      linear.addView(b1);	 	      // 将Button 2 加入到LinearLayout 中	      Button b2 = new Button(this);	      b2.setText("确定");	      linear. addView ( b2 );	 	      // 从LinearLayout 中移除Button 1	     // linear. removeView ( b1 );

							}
						});
					}
				},2500);



			}});}

	private void setScreenBrightness(int process) {

		//设置当前窗口的亮度值.这种方法需要权限android.permission.WRITE_EXTERNAL_STORAGE
		WindowManager.LayoutParams localLayoutParams = this.getActivity().getWindow().getAttributes();
		float f = process / 255.0F;
		localLayoutParams.screenBrightness = f;
		getActivity().getWindow().setAttributes(localLayoutParams);
		//修改系统的亮度值,以至于退出应用程序亮度保持
		saveBrightness(getActivity().getContentResolver(), process);

	}

	private int getScreenBrightness() {

		//设置当前窗口的亮度值.这种方法需要权限android.permission.WRITE_EXTERNAL_STORAGE
		WindowManager.LayoutParams localLayoutParams = this.getActivity().getWindow().getAttributes();

		return Math.abs((int) (localLayoutParams.screenBrightness * 255));


	}

	public static void saveBrightness(ContentResolver resolver, int brightness) {
		//改变系统的亮度值
		//这里需要权限android.permission.WRITE_SETTINGS
		//设置为手动调节模式
		Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		//保存到系统中
		Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
		android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
		resolver.notifyChange(uri, null);
	}

	public static Bitmap scaleMatrix(Bitmap bitmap, float f) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		float scaleW = f;
		float scaleH = f;
		System.out.println("w" + w + "h" + h + "scaleW" + scaleW);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleW, scaleH); // 长和宽放大缩小的比例
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	private void showResponse2(final String response) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {//注：这个县城用于修改TextView的信息、跳转等凡是跟UI油管的操作

				Gson gs = new Gson();
				//解析泛型
				mBaiduMap.clear();
				LinkedList<OverlayOptions> markers = new LinkedList<OverlayOptions>();
				//这里的类型（凡是有Markdown的地方）也可以换成自己的。
				Type type = new TypeToken<LinkedList<Markdown>>() {
				}.getType();
				List<Markdown> list = new Gson().fromJson(response, type);
				for (int i = 0; i < list.size(); i++) {
					Markdown m = list.get(i);
					System.out.println("111111222222" + m.toString());
					Resources res = getResources();
					Button button5 = new Button(getContext());
					mBaiduMap.setMyLocationEnabled(false);
					button5.setText(m.getMtitle());
					//构造InfoWindow
					// point 描述的位置点
					// -100 InfoWindow相对于point在y轴的偏移量
					View popMarker = View.inflate(getActivity(), R.layout.pop, null);

					popMarker.setDrawingCacheEnabled(true);
					OvalImageView iv=(OvalImageView) popMarker.findViewById(R.id.iv_title);
					TextView tv=(TextView) popMarker.findViewById(R.id.tv_title);
					iv.setImageBitmap(BitmapFactory.decodeByteArray(m.getMicon(),0,m.getMicon().length));
					iv.setStrokeColot(Color.argb(0.2F,1,1,1));
					iv.setStrokeWidth(9);
					tv.setText("   "+m.getMtitle());
					Bitmap bitmap1 = getViewBitmap(popMarker);
					BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap1);
					//构建MarkerOption，用于在地图上添加Marker
					OverlayOptions option = new MarkerOptions()
							.position(new LatLng(m.getMlatitude(), m.getMlongtitude()))
							.title(""+m.getMid())
							.icon(bitmapDescriptor)
							.draggable(true);
					mBaiduMap.addOverlay(option);
					b = true;


				}
				View popMarker1 = View.inflate(getActivity(), R.layout.pop, null);
				popMarker1.setDrawingCacheEnabled(true);
				OvalImageView iv1=(OvalImageView) popMarker1.findViewById(R.id.iv_title);
				TextView tv1=(TextView) popMarker1.findViewById(R.id.tv_title);
				iv1.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.lanqiu));
				iv1.setStrokeColot(Color.argb(0.2F,1,1,1));
				iv1.setStrokeWidth(9);
				tv1.setText("   "+MyUser.getMe().getUnicheng());
				Bitmap bitmap = getViewBitmap(popMarker1);
				BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromBitmap(bitmap);
				MarkerOptions option3 = new MarkerOptions()//构建MarkerOption，用于在地图上添加Marker
						.position(new LatLng(latitude,longtitude))
						.animateType(MarkerOptions.MarkerAnimateType.none)//生长动画
						.perspective(true)//是否开启远大近小
						.draggable(true)//是否可以拖拽
						.title("" + 1)
						.visible(true)//是否可见
						.flat(false)////设置平贴地图，在地图中双指下拉查看效果
						.icon(bitmapDescriptor1)
						.alpha(1f);//透明度
				mBaiduMap.addOverlay(option3);
				mBaiduMap.setMyLocationEnabled(false);
				mBaiduMap.setOnMarkerClickListener(ITCircleFromGankCategoryFragment.this);


			}
		});

	}


private Bitmap getViewBitmap(View addViewContent) {

		addViewContent.setDrawingCacheEnabled(true);

		addViewContent.measure(
		View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
		View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		addViewContent.layout(0, 0,
		addViewContent.getMeasuredWidth(),
		addViewContent.getMeasuredHeight());
addViewContent.setDrawingCacheEnabled(true);
		addViewContent.buildDrawingCache();
		Bitmap cacheBitmap = addViewContent.getDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		return bitmap;
		}
	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap roundBitmap = Bitmap.createBitmap(bitmap.getWidth(),          bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(roundBitmap);
		int color = 0xff424242;
		Paint paint = new Paint();
		//设置圆形半径
		int radius;
		if(bitmap.getWidth()>bitmap.getHeight())
		{    	 radius = bitmap.getHeight()/2;
		}     else {
			radius = bitmap.getWidth()/2;
		}     //绘制圆形
		 paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle( bitmap.getWidth()/ 2, bitmap.getHeight() / 2, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, -1, paint);
		return roundBitmap; }


}
