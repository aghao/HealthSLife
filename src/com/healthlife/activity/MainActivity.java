package com.healthlife.activity;

import com.baidu.mapapi.SDKInitializer;
import com.healthlife.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button heartBtn;
	private Button locateBtn;
	private Button sportBtn;
	private Button musicBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//使用SDK之前各组件初始化context信息，传入ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.healthlife_main);
		
		heartBtn = (Button)findViewById(R.id.heartbtn);
//		locateBtn = (Button)findViewById(R.id.locatebtn);
		sportBtn = (Button)findViewById(R.id.sportbtn);
		musicBtn = (Button)findViewById(R.id.musicbtn);
		
		//心率按钮响应
		heartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HeartRate.class);
				startActivity(intent);
			}
		});
		
//		//地图定位按钮响应
//		locateBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, GetLocation.class);
//				startActivity(intent);
//			}
//		});
		
		//音乐播放按钮响应
		musicBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MusicMain.class);
				startActivity(intent);
			}
		});
		//运动数据按钮响应
		sportBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SelectSportsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
