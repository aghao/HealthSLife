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
import com.healthlife.util.SitUpService;
import com.healthlife.util.VibratorUtil;


public class SitUpActivity extends Activity implements OnClickListener {

	private Button btnStart,btnStop;
	private TextView textNum;
	
	private SitUpService.DBinder dBinder;
	private ServiceConnection connection;
	
	private IntentFilter intentFilter;
	private MotionReceiver motionReceiver;
	
	private Sports sitUp;
	private Activity thisActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_situp);
		
		thisActivity = this;
		
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.healthlife.activity.SitUpActivity.MotionAdd");

		motionReceiver = new MotionReceiver();
		
		registerReceiver(motionReceiver,intentFilter);
		
		btnStart=(Button)findViewById(R.id.button_start_situp);
		btnStop=(Button)findViewById(R.id.button_stop_situp);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
		textNum=(TextView)findViewById(R.id.text_situp_amount);
		
		connection = new ServiceConnection(){
			
			@Override
			public void onServiceDisconnected(ComponentName name){
			}
		
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				
				dBinder = (SitUpService.DBinder) service;
			}
		};
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.button_start_situp:
			Intent intentBind = new Intent(this,com.healthlife.util.SitUpService.class);
			bindService(intentBind,connection,BIND_AUTO_CREATE);
			break;
			
		case R.id.button_stop_situp:
			sitUp = dBinder.getSitUp();
			unregisterReceiver(motionReceiver);
			unbindService(connection);
			Intent intentStop = new Intent(this,SitUpService.class);
			stopService(intentStop);
			Intent intentNextActivity = new Intent(this,com.healthlife.activity.ShowPushUpOrSitUpActivity.class);
			intentNextActivity.putExtra("situp",sitUp);
			intentNextActivity.putExtra("type", GlobalVariables.SPORTS_TYPE_SITUP);
			startActivity(intentNextActivity);
			finish();
			break;
			
			default:
		}	
	}
	
	class MotionReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			int motionNum;
			motionNum = intent.getIntExtra("motionNum",-1);
			VibratorUtil.Vibrate(thisActivity,100);
			textNum.setText(String.valueOf(motionNum));	
			
		}
		
	}
	
}
