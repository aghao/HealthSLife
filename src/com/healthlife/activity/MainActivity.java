package com.healthlife.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.Text;
import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Calorie;
import com.healthlife.entity.Sports;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Button heartBtn;
	private Button locateBtn;
	private Button sportBtn;
	private Button musicBtn;
	private Button heatAddBtn;
	private TextView mainInfoTv;
	private LinearLayout linearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("dd", getTodayCalorie()+"");
		
		//使用SDK之前各组件初始化context信息，传入ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.healthlife_main);
		
		getActionBar().setHomeButtonEnabled(true);
		
		heartBtn = (Button)findViewById(R.id.heartbtn);
		sportBtn = (Button)findViewById(R.id.sportbtn);
		musicBtn = (Button)findViewById(R.id.musicbtn);
		heatAddBtn = (Button)findViewById(R.id.main_heataddbtn);
		mainInfoTv = (TextView)findViewById(R.id.main_heatinfo);
		linearLayout = (LinearLayout)findViewById(R.id.main_layout);
		
		if(getTodayCalorie() > 0){
			mainInfoTv.setText("您今天净增加了" + String.format("%.2f", (Math.abs(getTodayCalorie()))) + "卡路里~");
		}else {
			mainInfoTv.setText("您今天净消耗了" + String.format("%.2f", (Math.abs(getTodayCalorie()))) + "卡路里~");
		}
		
		//心率按钮响应
		heartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HeartRate.class);
				startActivity(intent);
			}
		});
		
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
		//添加摄入卡路里按钮响应
		heatAddBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CalCalorie.class);
				startActivity(intent);
			}
		});
		//添加顶部layout点击响应
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//linearLayout.setBackgroundColor(Color.parseColor("#f0f0f0"));
				linearLayout.setBackgroundResource(R.drawable.mainlayout_selecor);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ShowCalDiff.class);
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
		if (id == R.id.personal_center) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, BackupsActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("SimpleDateFormat")
	private float getTodayCalorie(){
		DBManager db = new DBManager(this);
		SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String today=simpleFormatter.format(new java.util.Date());
		
		Calorie cal = db.queryCalorieByDate(today);
		ArrayList<Sports> sportsList = db.querySportsByDate(today);
		
		float sumCalorie=0;
		for(int i=0;i<sportsList.size();i++){
			sumCalorie+=sportsList.get(i).getCalorie();
		}
		cal.setCalorie(cal.getCalorie()-sumCalorie);
		
		return cal.getCalorie();
		
	}
}
