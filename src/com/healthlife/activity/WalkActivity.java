package com.healthlife.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.healthlife.R;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;
import com.healthlife.util.StepService;

public class WalkActivity extends Activity implements OnClickListener {
		
	private Button btnStart,btnStop;
	private TextView textNum;
	
	private ServiceConnection connection;
	private StepService.DBinder dBinder;
	
	private IntentFilter intentFilter;
	private MotionReceiver motionReceiver;
	
	private Sports walk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walk);
		
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.healthlife.activity.WalkActivity.MotionAdd");
		
		motionReceiver = new MotionReceiver();
		
		registerReceiver(motionReceiver,intentFilter);
		
		connection = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {			
				dBinder = (StepService.DBinder) service;			
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				
				
			}
			
		};
		
		btnStart=(Button)findViewById(R.id.button_start_walk);
		btnStop=(Button)findViewById(R.id.button_stop_walk);
		textNum= (TextView)findViewById(R.id.text_step_amount);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
		
		
		Intent intntBind = new Intent(this,com.healthlife.util.StepService.class);
		bindService(intntBind,connection,BIND_AUTO_CREATE);
		
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.button_start_walk:
			Intent intentStart = new Intent(this,com.healthlife.util.StepService.class);
			bindService(intentStart,connection,BIND_AUTO_CREATE);
			startService(intentStart);
			break;
			
		case R.id.button_stop_walk:
			walk =dBinder.getWalk();
			
			unregisterReceiver(motionReceiver);
			unbindService(connection);
			
			Intent intentStop = new Intent(this,StepService.class);
			stopService(intentStop);
			
			Intent intentNextActivity = new Intent(this,com.healthlife.activity.ShowWalkActivity.class);
			intentNextActivity.putExtra("walk",walk);
			intentNextActivity.putExtra("type", GlobalVariables.SPORTS_TYPE_WALK);
			startActivity(intentNextActivity);
			break;
		}
		
	}
	class MotionReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			int motionNum;
			motionNum = intent.getIntExtra("motionNum",-1);
			textNum.setText(String.valueOf(motionNum));	
		}
		
	}
	
}
