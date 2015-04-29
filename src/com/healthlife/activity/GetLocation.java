package com.healthlife.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.healthlife.R;

public class GetLocation extends Activity {
	
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.location_main);
		
		mMapView = (MapView)findViewById(R.id.bmapView);
		
		mBaiduMap = mMapView.getMap();
		LatLng cenpt = new LatLng(30.516985,114.440993); 
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);

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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
