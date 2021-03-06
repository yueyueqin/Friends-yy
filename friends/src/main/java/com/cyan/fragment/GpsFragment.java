package com.cyan.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.cyan.community.R;
import com.cyan.gps.Footprint;
import com.cyan.gps.Location;
import com.cyan.gps.OfflineDemo;
import com.cyan.gps.PathReplayActivity;
import com.cyan.gps.Poidemo;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;

import java.util.ArrayList;
import java.util.List;


/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:41
 * 类说明：
 * 用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class GpsFragment extends PreferenceFragment implements
        BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener, TextWatcher,
        OnGetSuggestionResultListener, View.OnClickListener
{ private FloatingActionButtonPlus mActionButtonPlus;

    private boolean isFirstLoc = true;
    private ArrayAdapter<String> sugAdapter = null;
    private String city;// 当前城市名
    // private String street;// 当前街道名
    // private String district;// 当前县
    private LocationClient mLocClient;
    private MyLocationListenner myListener;
    // 浏览路线节点相关
    private Button mBtnPre = null;// 上一个节点
    private Button mBtnNext = null;// 下一个节点

    private int nodeIndex = -1;// 节点索引,供浏览节点时使用
    @SuppressWarnings("rawtypes")
    private RouteLine route = null;
    @SuppressWarnings("unused")
    private OverlayManager routeOverlay = null;
    private boolean useDefaultIcon = false;
    private TextView popupText = null;// 泡泡view
    AutoCompleteTextView start;
    AutoCompleteTextView end;
    SuggestionSearch suggestionSearch;
    private LatLng latLng;// 当前经纬度信息
    public static int distance;

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null; // 地图View
    private BaiduMap mBaidumap = null;
    // 搜索相关
    private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    public static ArrayList<String> arrayList;

    private SDKReceiver mReceiver;

    public static GpsFragment newInstance()
    {
        GpsFragment gpsFragment = new GpsFragment();
        return gpsFragment;
    }

    private Button drive, transit, walk, bt_plan;


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(getActivity(), R.string.key_error,
                        Toast.LENGTH_LONG).show();
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(getActivity(), "网络出错！",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        getActivity().registerReceiver(mReceiver, iFilter);
        mLocClient = new LocationClient(getActivity());
        myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
        CharSequence titleLable = "路线规划功能";
        getActivity().setTitle(titleLable);

        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
        sugAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line);

    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //        return super.onCreateView(inflater, container, savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_gps, null);
        initView(fragmentView);
        events();
        return fragmentView;

    }

    private void events()
    {
        mActionButtonPlus.setOnItemClickListener(new FloatingActionButtonPlus.OnItemClickListener()
        {
            @Override
            public void onItemClick(FabTagLayout tagView, int position)
            {
                if (position==0){
                    Intent intent=new Intent(getActivity(), OfflineDemo.class);
                    startActivity(intent);
                }
                if (position==1){
                    Intent intent=new Intent(getActivity(), PathReplayActivity.class);
                    startActivity(intent);
                }
                if (position==2){
                    Intent intent=new Intent(getActivity(), Location.class);
                    startActivity(intent);
                }
                if (position==3){
                    Intent intent=new Intent(getActivity(), Footprint.class);
                    startActivity(intent);
                }
                if (position==4){
                    Intent intent=new Intent(getActivity(), Poidemo.class);
                    startActivity(intent);
                }
               // Toast.makeText(getActivity(), "Click btn" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initView(View fragmentRootView)
    {
        mActionButtonPlus = (FloatingActionButtonPlus)fragmentRootView. findViewById(R.id.FabPlus);

        start = (AutoCompleteTextView) fragmentRootView.findViewById(R.id.start);
        end = (AutoCompleteTextView) fragmentRootView.findViewById(R.id.end);
        // 初始化地图
        mMapView = (MapView) fragmentRootView.findViewById(R.id.map);
        Log.e("+++++++++", mMapView + "");
        mBaidumap = mMapView.getMap();

        // 定位至当前位置

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mBtnPre = (Button) fragmentRootView.findViewById(R.id.pre);
        mBtnNext = (Button) fragmentRootView.findViewById(R.id.next);
        bt_plan = (Button) fragmentRootView.findViewById(R.id.bt_plan);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        bt_plan.setVisibility(View.INVISIBLE);
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        // 存储路线信息
        arrayList = new ArrayList<String>();
        end.addTextChangedListener(this);
        start.addTextChangedListener(this);


        drive = (Button) fragmentRootView.findViewById(R.id.drive);
        Log.e("drive++++++++", drive + "");
        walk = (Button) fragmentRootView.findViewById(R.id.walk);
        transit = (Button) fragmentRootView.findViewById(R.id.transit);
        bt_plan = (Button) fragmentRootView.findViewById(R.id.bt_plan);
        drive.setOnClickListener(this);
        walk.setOnClickListener(this);
        transit.setOnClickListener(this);

        mBtnPre.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /**
                 * 节点浏览示例
                 *
                 * @param v
                 */
                if (route == null || route.getAllStep() == null) {
                    Log.e("节点浏览","route == null ||route.getAllStep() == null");
                    return;
                }

                if (nodeIndex == -1 && v.getId() == R.id.pre) {
                    Log.e("节点浏览","nodeIndex == -1 && v.getId() == R.id.pre");
                    return;
                }
                // 设置节点索引
                if (v.getId() == R.id.next) {
                    if (nodeIndex < route.getAllStep().size() - 1) {
                        nodeIndex++;
                    } else {
                        return;
                    }
                } else if (v.getId() == R.id.pre) {
                    if (nodeIndex > 0) {
                        nodeIndex--;
                    } else {
                        return;
                    }
                }

                // 获取节结果信息
                LatLng nodeLocation = null;
                String nodeTitle = null;

                Object step = route.getAllStep().get(nodeIndex);
                if (step instanceof DrivingRouteLine.DrivingStep) {
                    nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace()
                            .getLocation();// 节点经纬度
                    nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();// 节点行驶路线
                } else if (step instanceof WalkingRouteLine.WalkingStep) {
                    nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace()
                            .getLocation();
                    nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
                } else if (step instanceof TransitRouteLine.TransitStep) {
                    nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace()
                            .getLocation();
                    nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
                }

                if (nodeLocation == null || nodeTitle == null) {
                    return;
                }

                // 移动节点至中心
                mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
                // show popup
                popupText = new TextView(getActivity());
                popupText.setBackgroundResource(R.drawable.popup);
                popupText.setTextColor(0xFF000000);
                popupText.setText(nodeTitle);
                mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {/**
             * 节点浏览示例
             *
             * @param v
             */

                if (route == null || route.getAllStep() == null) {
                    Log.e("节点浏览","route == null ||route.getAllStep() == null");
                    return;
                }

                if (nodeIndex == -1 && v.getId() == R.id.pre) {
                    Log.e("节点浏览","nodeIndex == -1 && v.getId() == R.id.pre");
                    return;
                }
                // 设置节点索引
                if (v.getId() == R.id.next) {
                    if (nodeIndex < route.getAllStep().size() - 1) {
                        nodeIndex++;
                    } else {
                        return;
                    }
                } else if (v.getId() == R.id.pre) {
                    if (nodeIndex > 0) {
                        nodeIndex--;
                    } else {
                        return;
                    }
                }

                // 获取节结果信息
                LatLng nodeLocation = null;
                String nodeTitle = null;

                Object step = route.getAllStep().get(nodeIndex);
                if (step instanceof DrivingRouteLine.DrivingStep) {
                    nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace()
                            .getLocation();// 节点经纬度
                    nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();// 节点行驶路线
                } else if (step instanceof WalkingRouteLine.WalkingStep) {
                    nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace()
                            .getLocation();
                    nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
                } else if (step instanceof TransitRouteLine.TransitStep) {
                    nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace()
                            .getLocation();
                    nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
                }

                if (nodeLocation == null || nodeTitle == null) {
                    return;
                }

                // 移动节点至中心
                mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
                // show popup
                popupText = new TextView(getActivity());
                popupText.setBackgroundResource(R.drawable.popup);
                popupText.setTextColor(0xFF000000);
                popupText.setText(nodeTitle);
                mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

            }
        });
        bt_plan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                arrayList.removeAll(arrayList);// 清空之前路线信息
                for (int i = 0; i < route.getAllStep().size(); i++) {
                    String nodeTitle = null;
                    Object step = route.getAllStep().get(i);
                    distance = route.getDistance();
                    if (step instanceof DrivingRouteLine.DrivingStep) {
                        nodeTitle = ((DrivingRouteLine.DrivingStep) step)
                                .getInstructions();// 节点行驶路线信息
                    } else if (step instanceof WalkingRouteLine.WalkingStep) {
                        nodeTitle = ((WalkingRouteLine.WalkingStep) step)
                                .getInstructions();
                    } else if (step instanceof TransitRouteLine.TransitStep) {
                        nodeTitle = ((TransitRouteLine.TransitStep) step)
                                .getInstructions();
                    }
                    arrayList.add(nodeTitle);
                    Log.e("加载之后的list里面的内容",""+nodeTitle);
                }
                Log.e("进行跳转","进行跳转");
                Intent intent = new Intent(getActivity(), com.cyan.gps.Routplan.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        // 重置浏览节点的路线数据
        route = null;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaidumap.clear();

        PlanNode stNode = null;
        if (start.getText().toString().equals("我的位置")) {
            stNode = PlanNode.withLocation(latLng);
            Log.e("我的位置是", stNode + "???????");
        } else {
            stNode = PlanNode.withCityNameAndPlaceName(city, start.getText()
                    .toString().trim());
            Log.e("输入的地址是", start.getText().toString().trim() + "");
            Log.e("stNode", stNode + "");
        }
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode enNode = null;

        enNode = PlanNode.withCityNameAndPlaceName(city, end.getText()
                .toString().trim());
        //发起路线规划搜索示例
        // 实际使用中请对起点终点城市进行正确的设定
        if (v.getId() == R.id.walk) {
            Log.e("---------------", "点击监听是否触发");
            mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
                    .to(enNode));
            Log.e("---------------", "？？？？？？？");
        }
        if (v.getId() == R.id.drive) {
            Log.e("---------------", "点击监听是否触发");
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
                    .to(enNode));
            Log.e("---------------", "？？？？？？？");
        }
        if (v.getId() == R.id.transit) {
            mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
                    .city(city).to(enNode));
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result)
    {
        Log.e("result!!!!", "" + result);
        Log.e("result.error!!!!!!", "" + result.error);
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            bt_plan.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result)
    {
        Log.e("result!!!!", "" + result);
        Log.e("result.error!!!!!!", "" + result.error);
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result)
    {
        Log.i("result的结果是什么啊", "" + result);
        Log.i("result.error是啥", "" + result.error);
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            Log.i("list里面的数值是啥", "" + result.getSuggestAddrInfo()
                    .getSuggestEndCity());
            List<CityInfo> list = result.getSuggestAddrInfo()
                    .getSuggestEndCity();

            Log.i("这个是空的啊？？？", list + "");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            bt_plan.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay
    {

        public MyDrivingRouteOverlay(BaiduMap baiduMap)
        {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker()
        {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker()
        {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay
    {

        public MyWalkingRouteOverlay(BaiduMap baiduMap)
        {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker()
        {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker()
        {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            city = location.getCity();// 合肥市
            // Toast.makeText(getApplicationContext(), city, 0).show();
            // street = location.getStreet();// 文曲路
            // district = location.getDistrict();// 肥西县
            // longitude = location.getLongitude();
            // latitude = location.getLatitude();
            // adress = location.getAddrStr()；
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                latLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                mBaidumap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation)
        {

        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay
    {

        public MyTransitRouteOverlay(BaiduMap baiduMap)
        {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker()
        {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker()
        {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng point)
    {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi)
    {
        return false;
    }

    @Override
    public void onPause()
    {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        mSearch.destroy();
        mMapView.onDestroy();
        suggestionSearch.destroy();
        super.onDestroy();
        // 取消监听 SDK 广播
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void afterTextChanged(Editable arg0)
    {

    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3)
    {

    }

    @Override
    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
    {
        if (cs.length() <= 0) {
            return;
        }
        String cityname = city;
        /**
         * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
         */
        suggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(cs.toString()).city(cityname));

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res)
    {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();

        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                sugAdapter.add(info.key);

            }
        }
        sugAdapter.notifyDataSetChanged();

    }

}
