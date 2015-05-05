package com.healthlife.activity;

import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;
import com.healthlife.util.PushUpService;
import com.healthslife.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PushUpActivity extends Activity implements OnClickListener {
	
	private Sports sports;
	private Button btnStart,btnStop;
	private PushUpService.DBinder dBinder;
	
	private ServiceConnection connection = new ServiceConnection(){
		
		@Override
		public void onServiceDisconnected(ComponentName name){
		}
	
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			dBinder = (PushUpService.DBinder) service;
			sports = dBinder.getSports();
			TextView text = (TextView)findViewById(R.id.text_pushup_amount);
			text.setText(String.valueOf(sports.getNum()));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pushup);
		sports.setType(GlobalVariables.SPORTS_TYPE_PUSHUP);
		
		btnStart=(Button)findViewById(R.id.button_start_pushup);
		btnStop=(Button)findViewById(R.id.button_stop_pushup);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.button_start_pushup:
			Intent intentStart = new Intent(this,PushUpService.class);
			bindService(intentStart,connection,BIND_AUTO_CREATE);
			startService(intentStart);
			break;
			
		case R.id.button_stop_pushup:
			Intent intentStop = new Intent(this,PushUpService.class);
			unbindService(connection);
			stopService(intentStop);
			Intent intentNextActivity = new Intent(this,ShowPushUpOrSitUpActivity.class);
			intentNextActivity.putExtra("pushup",sports);
			startActivity(intentNextActivity);
			break;
		}
		
	}
}
