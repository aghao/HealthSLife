package com.healthlife.activity;

import java.util.ArrayList;
import java.util.List;

import android.R.menu;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification.Action;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Position;
import com.healthlife.entity.Sports;

public class LocationResult extends Activity{
	
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	private Sports newSports;
	private Position newPosition;
	private List<LatLng> pts;
	private ArrayList<Position> pos;
	private List<Position> points;
	private TextView durationTv;
	private TextView distanceTv;
	private TextView speedTv;
	private TextView stepsTv;
	private Button saveBtn;
	private Double centerLatitude;
    private Double centerLongitude;
    private int steps;
    private boolean isFirst = true;
    private double distance;
    private long recordTime;
    private float speed;
	private long sportID;
    private String date;
	private String duration;
	private int showmode;
	private DBManager myDB;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_result);

		newSports = new Sports();
		newPosition = new Position();
		myDB = new DBManager(LocationResult.this);
		
		durationTv = (TextView)findViewById(R.id.location_result_duration);
		distanceTv = (TextView)findViewById(R.id.location_result_distance);
		speedTv = (TextView)findViewById(R.id.location_result_speed);
		stepsTv = (TextView)findViewById(R.id.location_result_pace);
		saveBtn = (Button)findViewById(R.id.location_result_savebtn);
		
		Intent intent = getIntent();
		showmode = intent.getIntExtra("showmode",-1);
		if(showmode == 1){
			newSports = (Sports)intent.getSerializableExtra("jog");
			date = newSports.getDate();
			centerLatitude = Double.valueOf((double)newSports.getGoodNum());
			centerLongitude = Double.valueOf((double)newSports.getPerfectNum());
			distance = (double)newSports.getDistance();
			duration = newSports.getDuration();
			speed = newSports.getAVGSpeed();
			steps = newSports.getNum();
			
			saveBtn.setText("删除运动");
			
			pos = myDB.getPosList(newSports.getSportsID());
			initMap(centerLatitude, centerLongitude);
			pts = new ArrayList<LatLng>();
			for(int i = 0;i<pos.size();i++)
			{
				LatLng pt = new LatLng(pos.get(i).getLatitude(), pos.get(i).getLongitude());
				pts.add(pt);
			}
				//添加起点和终点
				LatLng start = new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude());
				LatLng end = new LatLng(pos.get(pos.size()-1).getLatitude(),
						pos.get(pos.size()-1).getLongitude());
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_st);
				BitmapDescriptor bitmap1 = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_en);
				OverlayOptions option = new MarkerOptions()  
			    	.position(start)
			    	.icon(bitmap);
				OverlayOptions option1 = new MarkerOptions()  
		    		.position(end)
		    		.icon(bitmap1);
				mBaiduMap.addOverlay(option);
				mBaiduMap.addOverlay(option1);
				
				//构建绘制折线的Option对象  
				OverlayOptions polylineOption = new PolylineOptions()  
				    .points(pts)
				    .width(9)
				    .color(0xAAFF4F4F);
				mBaiduMap.addOverlay(polylineOption);
		}else if(showmode == 0){
			points = (List<Position>) intent.getSerializableExtra("locinfo");
			date = intent.getStringExtra("date");
			centerLatitude = intent.getDoubleExtra("cenlat", 39.923963);
			centerLongitude = intent.getDoubleExtra("cenlon", 116.403029);
			distance = intent.getDoubleExtra("distance", 0.0);
			recordTime = intent.getLongExtra("rectime", 0);
			duration = intent.getStringExtra("duration");
			speed = (float)(distance / recordTime);
			steps = intent.getIntExtra("steps", 0);
			
			Log.i("Test", String.valueOf(centerLatitude.floatValue()));
			//newSports.setUserId(1); //测试用1
			newSports.setDate(date);
			newSports.setType(GlobalVariables.SPORTS_TYPE_JOG);
			newSports.setGoodNum(centerLatitude.floatValue());  //中心纬度
			newSports.setPerfectNum(centerLongitude.floatValue()); //中心经度
			newSports.setDistance((float)distance);
			newSports.setDuration(duration);
			newSports.setAVGSpeed(speed);
			newSports.setNum(steps);
			
			if(points.size() != 0){
				initMap(centerLatitude, centerLongitude);
				pts = new ArrayList<LatLng>();
				for(int i = 0;i<points.size();i++)
				{
					LatLng pt = new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
					pts.add(pt);
				}
				if(points.size() > 1){
					//添加起点和终点
					LatLng start = new LatLng(points.get(0).getLatitude(), points.get(0).getLongitude());
					LatLng end = new LatLng(points.get(points.size()-1).getLatitude(),
							points.get(points.size()-1).getLongitude());
					BitmapDescriptor bitmap = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_st);
					BitmapDescriptor bitmap1 = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_en);
					OverlayOptions option = new MarkerOptions()  
				    	.position(start)
				    	.icon(bitmap);
					OverlayOptions option1 = new MarkerOptions()  
			    		.position(end)
			    		.icon(bitmap1);
					mBaiduMap.addOverlay(option);
					mBaiduMap.addOverlay(option1);
					
					//构建绘制折线的Option对象  
					OverlayOptions polylineOption = new PolylineOptions()  
					    .points(pts)
					    .width(9)
					    .color(0xAAFF4F4F);
					mBaiduMap.addOverlay(polylineOption);
				}	
			}else {
				Toast.makeText(this, "坐标为空！", Toast.LENGTH_SHORT).show();
			}
		}else {
			Log.i("Test", "No intent data.");
		}
		
		Log.i("Test", "传过来的距离为：" + String.valueOf(distance));
		
		durationTv.setText(duration);
		distanceTv.setText(String.format("%.1f", distance) + "米");
		speedTv.setText(String.format("%.1f", speed) + "m/s");
		stepsTv.setText(String.valueOf(steps));

		
		saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(showmode == 1){
					myDB.removeSport(newSports.getSportsID());
					myDB.removePosition(newSports.getSportsID());
					Toast.makeText(LocationResult.this, "记录删除成功！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(LocationResult.this, ShowSportsHistoryActivity.class);
					startActivity(intent);
					finish();
				}else if (showmode == 0) {
					// 存入数据库
					sportID = myDB.insertSport(newSports);
					for(int i=0;i<points.size();i++){
						newPosition.setSportId(sportID);
						newPosition.setTime(points.get(i).getTime());
						newPosition.setLatitude(points.get(i).getLatitude());
						newPosition.setLongitude(points.get(i).getLongitude());
						myDB.insertPosition(newPosition);
					}
					Toast.makeText(LocationResult.this, "记录保存成功！", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Log.i("Test","Others");
				}
				
			}
		});
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
