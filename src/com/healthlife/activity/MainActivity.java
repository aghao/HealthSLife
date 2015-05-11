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
		
		//ʹ��SDK֮ǰ�������ʼ��context��Ϣ������ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.healthlife_main);
		
		heartBtn = (Button)findViewById(R.id.heartbtn);
		locateBtn = (Button)findViewById(R.id.locatebtn);
		sportBtn = (Button)findViewById(R.id.sportbtn);
		musicBtn = (Button)findViewById(R.id.musicbtn);
		
		//���ʰ�ť��Ӧ
		heartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HeartRate.class);
				startActivity(intent);
			}
		});
		
		//��ͼ��λ��ť��Ӧ
		locateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, GetLocation.class);
				startActivity(intent);
			}
		});
		
		//���ֲ��Ű�ť��Ӧ
		musicBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MusicMain.class);
				startActivity(intent);
			}
		});
		//�˶����ݰ�ť��Ӧ
		sportBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//	Intent intent = new Intent();
			//	intent.setClass(MainActivity.this, GetLocation.class);
			//	startActivity(intent);
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
