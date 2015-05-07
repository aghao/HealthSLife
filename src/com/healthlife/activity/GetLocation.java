package com.healthlife.activity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.healthlife.R;
/**
 * Title: GetLocation.java
 * @author Jusitn Yin
 * 2015-5-7
 */
public class GetLocation extends Activity {
	
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	private LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	private MyLocationConfiguration.LocationMode mLocationConfiguration = MyLocationConfiguration.LocationMode.FOLLOWING;
	private volatile boolean isFirstLocation = true;
	private boolean judge = true;
	private Double mCurrentLantitude;  
    private Double mCurrentLongitude;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.location_main);
		
		initMap();
		initMyLocation();
		
	}
	
	private void initMyLocation()
	{
		// 定位初始化
		mLocationClient = new LocationClient(this);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocationClient.setLocOption(option);
	}
	
	//地图初始化
	private void initMap(){
		mMapView = (MapView)findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		LatLng cenpt = new LatLng(30.513236,114.419936); 
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			MyLocationData locData = new MyLocationData.Builder()  
			    .accuracy(location.getRadius())  
			    // 此处设置开发者获取到的方向信息，顺时针0-360  
			    .direction(0).latitude(location.getLatitude())  
			    .longitude(location.getLongitude()).build();  
			// 设置定位数据  
			mBaiduMap.setMyLocationData(locData);
			mCurrentLantitude = location.getLatitude();  
            mCurrentLongitude = location.getLongitude();
            
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());       
    		String date = sDateFormat.format(new java.util.Date());  
    		/*
    		Point p = new Point();
    		p.setDate(date);
    		p.setLatitude(mCurrentLantitude);
    		p.setLongitude(mCurrentLongitude);
    		points.add(p);
    		
    		ActionBar actionBar = getActionBar();
    		actionBar.setTitle(String.valueOf(points.size()));
            */
            Log.i("Lat", "纬度：" + mCurrentLantitude.toString());
            Log.i("Lon", "经度" + mCurrentLongitude.toString());
            /*
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）  
			BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory  
			    .fromResource(R.drawable.locimg);
			MyLocationConfiguration config = new MyLocationConfiguration(mLocationConfiguration, true, mCurrentMarker);
			mBaiduMap.setMyLocationConfigeration(config); 
			*/
			// 第一次定位时，将地图位置移动到当前位置
			if (isFirstLocation)
			{
				isFirstLocation = false;
				returnMyLoc();
			}
		}
	}
	
	//点击“我的位置”
	private void returnMyLoc(){
		mLocationClient.requestLocation();
		LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);  
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);  
        mBaiduMap.animateMapStatus(u);
	}
	
	@Override
	protected void onStart() {
		// 开启图层定位  
        mBaiduMap.setMyLocationEnabled(true);  
        if (!mLocationClient.isStarted())  
        {  
            mLocationClient.start();  
        }
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy(); 
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause(); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume(); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.get_location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.location_returnloc) {
			try{
				returnMyLoc();
			}catch(Exception e){
				Toast.makeText(this, "没有检测到位置", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		if (id == R.id.location_switchtype){
			if(judge)
			{
				item.setTitle("切换至交通图");
				judge = false;
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);  //设置卫星图
			}else{
				item.setTitle("切换至卫星图");
				judge = true;
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
