package com.healthlife.activity;

import android.app.ActionBar;
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
import com.healthlife.util.PushUpService;
import com.healthlife.util.VibratorUtil;

public class PushUpActivity extends Activity implements OnClickListener {
		
	private Button btnStart;
	private TextView textNum;
	
	private PushUpService.DBinder dBinder;
	private ServiceConnection connection;

	private IntentFilter intentFilter;
	private MotionReceiver motionReceiver;
	
	private Sports pushUp;
	private Activity thisActivity;
	private boolean startFlag;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pushup);
		
		startFlag=false;
		
		thisActivity=this;
		
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.healthlife.activity.PushUpActivity.MotionAdd");
		
		motionReceiver = new MotionReceiver();
		
		registerReceiver(motionReceiver,intentFilter);
		
		
		btnStart=(Button)findViewById(R.id.button_start_pushup);
		
		textNum=(TextView)findViewById(R.id.text_pushup_amount);
		
		btnStart.setOnClickListener(this);
		
		connection = new ServiceConnection(){
			
			@Override
			public void onServiceDisconnected(ComponentName name){
			}
		
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				
				dBinder = (PushUpService.DBinder) service;
			}
		};
		

		
	}

	@Override
	public void onClick(View v) {
	

			if(!startFlag){
			Intent intntBind = new Intent(this,com.healthlife.util.PushUpService.class);
			bindService(intntBind,connection,BIND_AUTO_CREATE);
			btnStart.setText("Stop");
			startFlag=true;
			}
			
			else{
			pushUp = dBinder.getPushUp();
			unregisterReceiver(motionReceiver);
			unbindService(connection);
			
			Intent intentStop = new Intent(this,PushUpService.class);
			stopService(intentStop);
			
			Intent intentNextActivity = new Intent(this,com.healthlife.activity.ShowPushUpOrSitUpActivity.class);
			intentNextActivity.putExtra("pushup",pushUp);
			intentNextActivity.putExtra("type", GlobalVariables.SPORTS_TYPE_PUSHUP);
			startActivity(intentNextActivity);
			finish();
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
