package com.cyan.gps;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.cyan.community.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//轨迹回放
public class PathReplayActivity extends Activity {

	ExecutorService es = Executors.newFixedThreadPool(3);
	int current1;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	boolean isFirstLoc = true;// 是否首次定位

	private Button replayButton;

	com.cyan.gps.VerticalSeekBarNew seekBar;
	private Marker marker = null;// 当前轨迹点图案
	public Handler timerhandler = new Handler();// 定时器
	public Runnable runnable = null;
	// 存放所有坐标的数组
	private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();// 当前经纬度列表,可从数据库中查
	private ArrayList<LatLng> latlngList_path = new ArrayList<LatLng>();

	// private LatLng marker1 ;
	// private LatLng marker2 ;
	// private LatLng marker3 ;
	// private LatLng marker4;
	// private LatLng marker5 ;
	// private LatLng marker6 ;
	// private LatLng marker7;
	// private LatLng marker8;
	// private LatLng marker9 ;
	// private LatLng marker10 ;
	// private LatLng marker11;
	// private LatLng marker12;
	// private LatLng marker13 ;
	// private LatLng marker14 ;
	// private LatLng marker15 ;
	// private LatLng marker16 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_path_replay);
		initDataSource();
		initBaiduMap();
		initView();
	}

	private void initDataSource() {
		// marker1 = new LatLng(23.142521, 113.3688);
		// marker2 = new LatLng(23.152731, 113.3789);
		// marker3 = new LatLng(23.162941, 113.3890);
		// marker4 = new LatLng(23.173151, 113.3992);
		// marker5 = new LatLng(23.183461, 113.4093);
		// marker6 = new LatLng(23.193571, 113.4195);
		// marker7 = new LatLng(23.213881, 113.4296);
		// marker8 = new LatLng(23.223000, 113.4498);
		// marker9 = new LatLng(23.204510, 113.4599);
		// marker10 = new LatLng(23.194301, 113.4580);
		// marker11 = new LatLng(23.174201, 113.4518);
		// marker12 = new LatLng(23.164101, 113.4495);
		// marker13 = new LatLng(23.143801, 113.4400);
		// marker14 = new LatLng(23.133301, 113.4380);
		// marker15 = new LatLng(23.122801, 113.4285);
		// marker16 = new LatLng(23.112001, 113.4270);
		for (int i = 0; i < 10000; i++) {
			LatLng llLatLng;
			if (10000 / 2 == 0) {
				llLatLng = new LatLng(23.142521 + (i * 0.0001),
						113.3688 + (i * 0.0001));
			} else {
				llLatLng = new LatLng(23.142521 - (i * 0.0001),
						113.3688 + (i * 0.0002));
			}
			latlngList.add(llLatLng);
		}

	}

	// 根据定时器线程传递过来指令执行任务
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				int curpro = seekBar.getProgress();
				if (curpro != seekBar.getMax()) {
					seekBar.setProgress(curpro + 1);
					timerhandler.postDelayed(runnable, 10);// 延迟0.5秒后继续执行
				} else {
					replayButton.setText(" 回放 ");// 已执行到最后一个坐标 停止任务
				}
			}
		}
	};

	private void initView() {
		replayButton = (Button) findViewById(R.id.btn_replay);
		replayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 根据按钮上的字判断当前是否在回放
				if (replayButton.getText().toString().trim().equals("回放")) {
					if (latlngList.size() > 0) {
						// 假如当前已经回放到最后一点 置0
						if (seekBar.getProgress() == seekBar.getMax()) {
							seekBar.setProgress(0);
						}
						// 将按钮上的字设为"停止" 开始调用定时器回放
						replayButton.setText(" 停止 ");
						timerhandler.postDelayed(runnable, 10);
					}
				} else {
					// 移除定时器的任务
					timerhandler.removeCallbacks(runnable);
					replayButton.setText(" 回放 ");
				}
			}
		});

		runnable = new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		};

		// seekBar = (com.example.testoffline.VerticalSeekBar)
		// findViewById(R.id.process_bar);
		seekBar = (com.cyan.gps.VerticalSeekBarNew) findViewById(R.id.process_bar);

		seekBar.setSelected(false);
		seekBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				latlngList_path.clear();
				if (progress != 0) {
					for (int i = 0; i < seekBar.getProgress(); i++) {
						latlngList_path.add(latlngList.get(i));
					}
					drawLine(latlngList_path, progress);
				}
				// try {
				// Thread.sleep(100);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
			}

			// 第三个是onStopTrackingTouch,在停止拖动时执行
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				latlngList_path.clear();
				current1 = seekBar.getProgress();
				if (current1 != 0) {
					for (int i = 0; i < seekBar.getProgress(); i++) {
						latlngList_path.add(latlngList.get(i));
					}
					es.execute(new Runnable() {

						@Override
						public void run() {
							drawLine(latlngList_path, current1);
						}
					});

				}
			}
		});
		initDEMOMap();
	}

	private void drawLine(ArrayList<LatLng> list, int current) {
		mBaiduMap.clear();
		LatLng replayGeoPoint = latlngList.get(current - 1);
		if (marker != null) {
			marker.remove();
		}

		// 添加汽车位置图标
		MarkerOptions markerOptions1 = new MarkerOptions();
		markerOptions1.position(replayGeoPoint);
		markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.icon_gcoding)));
		markerOptions1.zIndex(14);
		markerOptions1.draggable(true);
		markerOptions1.anchor(0.5f, 0.5f);
		OverlayOptions ooA = markerOptions1;
		mBaiduMap.addOverlay(ooA);

		// 增加起点开始文字
		MarkerOptions markerOptions2 = new MarkerOptions();
		markerOptions2.position(latlngList.get(0));
		markerOptions2.title("起点");
		markerOptions2.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(),
						R.drawable.nav_route_result_start_point)));
		OverlayOptions ooB = markerOptions2;
		mBaiduMap.addOverlay(ooB);

		// 增加起点结束
		if (latlngList_path.size() > 1) {
			PolylineOptions plOptions = new PolylineOptions();
			plOptions.width(15);
			plOptions.color(0xAAFF0000);
			plOptions.points(latlngList_path);
			plOptions.color(Color.rgb(9, 129, 240));
			mBaiduMap.addOverlay(plOptions);
		}
		if (latlngList_path.size() == latlngList.size()) {
			MarkerOptions markerOptions3 = new MarkerOptions();
			markerOptions3.position(latlngList.get(latlngList.size() - 1));
			markerOptions3.title("终点");
			markerOptions3.icon(BitmapDescriptorFactory
					.fromBitmap(BitmapFactory.decodeResource(getResources(),
							R.drawable.nav_route_result_end_point)));
			mBaiduMap.addOverlay(markerOptions3);
		}
	}

	private void initDEMOMap() {
		// latlngList.add(marker1);
		// latlngList.add(marker2);
		// latlngList.add(marker3);
		// latlngList.add(marker4);
		// latlngList.add(marker5);
		// latlngList.add(marker6);
		// latlngList.add(marker7);
		// latlngList.add(marker8);
		// latlngList.add(marker9);
		// latlngList.add(marker10);
		// latlngList.add(marker11);
		// latlngList.add(marker12);
		// latlngList.add(marker13);
		// latlngList.add(marker14);
		// latlngList.add(marker15);
		// latlngList.add(marker16);

		// 设置进度条最大长度为数组长度
		seekBar.setMax(latlngList.size());
		mBaiduMap.setMapType(mBaiduMap.MAP_TYPE_NORMAL);
		// 画面移动到这个点
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlngList.get(0));
		mBaiduMap.animateMapStatus(u);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				System.out.println("location为null");
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void initBaiduMap() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(PathReplayActivity.this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(8 * 1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mLocClient.requestLocation();
	}
}
