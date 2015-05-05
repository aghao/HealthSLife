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
import com.healthlife.util.StepService;
import com.healthslife.R;
import com.healthslife.ShowWalkActivity;

public class WalkActivity extends Activity implements OnClickListener {
	
	private Sports walk;
	private Button btnStart,btnStop;
	private StepService.DBinder dBinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walk);
		walk.setType(GlobalVariables.SPORTS_TYPE_WALK);
		
		btnStart=(Button)findViewById(R.id.button_start_walk);
		btnStop=(Button)findViewById(R.id.button_stop_walk);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,StepService.class);
		switch(v.getId()){
		case R.id.button_start_walk:
			bindService(intent,connection,BIND_AUTO_CREATE);
			startService(intent);
			break;
			
		case R.id.button_stop_walk:
			unbindService(connection);
			stopService(intent);
			Intent intentNextActivity = new Intent(this,ShowWalkActivity.class);
			intentNextActivity.putExtra("walk",walk);
			startActivity(intentNextActivity);
			break;
		}
		
	}
	
	public ServiceConnection connection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			dBinder = (StepService.DBinder) service;
			walk = dBinder.getWalk();
			TextView text = (TextView)findViewById(R.id.text_step_amount);
			text.setText(String.valueOf(walk.getNum()));
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
}
