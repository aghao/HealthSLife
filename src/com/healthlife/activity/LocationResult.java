package com.healthlife.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.healthlife.R;
import com.healthlife.entity.Point;

public class LocationResult extends Activity{
	
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	private List<LatLng> pts;
	private List<Point> points;
	private TextView durationTv;
	private TextView distanceTv;
	private TextView speedTv;
	private Double centerLatitude;
    private Double centerLongitude;
    private double distance;
    private long recordTime;
    private float speed;
	private String duration;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_result);
		
		Intent intent = getIntent();
		points = (List<Point>) intent.getSerializableExtra("locinfo");
		centerLatitude = intent.getDoubleExtra("cenlat", 39.923963);
		centerLongitude = intent.getDoubleExtra("cenlon", 116.403029);
		distance = intent.getDoubleExtra("distance", 0.0);
		recordTime = intent.getLongExtra("rectime", 0);
		duration = intent.getStringExtra("duration");
		Log.i("Test", "recordTime:" + String.valueOf(recordTime));
		speed = (float)(distance / recordTime);
		
		Log.i("Test", "传过来的距离为：" + String.valueOf(distance));
		durationTv = (TextView)findViewById(R.id.location_result_duration);
		distanceTv = (TextView)findViewById(R.id.location_result_distance);
		speedTv = (TextView)findViewById(R.id.location_result_speed);

		durationTv.setText(duration);
		distanceTv.setText(String.format("%.1f", distance) + "米");
		speedTv.setText(String.format("%.1f", speed) + "m/s");

		if(points.size() != 0){
			initMap(centerLatitude, centerLongitude);
			pts = new ArrayList<LatLng>();
			for(int i = 0;i<points.size();i++)
			{
				LatLng pt = new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
				pts.add(pt);
			}
			//构建绘制折线的Option对象  
			OverlayOptions polylineOption = new PolylineOptions()  
			    .points(pts)
			    .width(9)
			    .color(0xAAFF4F4F);
			mBaiduMap.addOverlay(polylineOption);
		}else {
			Toast.makeText(this, "坐标为空！", Toast.LENGTH_SHORT).show();
		}
	}
	
	//地图初始化
	private void initMap(double cenLat, double cenLon){
		mMapView = (MapView)findViewById(R.id.location_result_bmapView);
		mBaiduMap = mMapView.getMap();
		LatLng cenpt = new LatLng(cenLat, cenLon); 
//		LatLng cenpt = new LatLng(30.516939,114.441744);   //0.001误差范围
//		LatLng cenpt = new LatLng(30.516989,114.440994); 
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(15).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}
}
