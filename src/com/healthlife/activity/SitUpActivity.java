package com.healthlife.activity;

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

import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;
import com.healthlife.util.SitUpService;
import com.healthslife.R;


public class SitUpActivity extends Activity implements OnClickListener {
	private Sports sitUp;
	private Button btnStart,btnStop;
	private SitUpService.DBinder dBinder;
	
	public ServiceConnection connection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			dBinder = (SitUpService.DBinder) service;
			sitUp = dBinder.getSitUp();
			TextView text = (TextView)findViewById(R.id.text_situp_amount);
			text.setText(String.valueOf(sitUp.getNum()));
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_situp);
		sitUp.setType(GlobalVariables.SPORTS_TYPE_SITUP);
		
		btnStart=(Button)findViewById(R.id.button_start_situp);
		btnStop=(Button)findViewById(R.id.button_stop_situp);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button_start_situp:
			Intent intentStart = new Intent(this,SitUpService.class);
			bindService(intentStart,connection,BIND_AUTO_CREATE);
			startService(intentStart);
			break;
			
		case R.id.button_stop_situp:
			Intent intentStop = new Intent(this,SitUpService.class);
			unbindService(connection);
			stopService(intentStop);
			Intent intentNextActivity = new Intent(this,ShowPushUpOrSitUpActivity.class);
			intentNextActivity.putExtra("situp",sitUp);
			startActivity(intentNextActivity);
			break;
		}	
	}
	
}
