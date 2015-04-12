package com.healthlife.activity;

import com.healthlife.util.SensorController;
import com.healthlife.util.StepService;

import com.healthslife.R;
import com.healthslife.R.id;
import com.healthslife.R.layout;
import com.healthslife.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class JoggingActivity extends Activity {
	
	public static final int JOG = 0;
	public static final int WALK = 1;
	public static final int PUSHUP = 2;
	public static final int SITUP = 3;
	
	private boolean mSportingFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sporting);
		
		SensorController sensorController = new SensorController();
		sensorController.initSensors(JOG, this);
		
		Intent intentSporting = new Intent(this,StepService.class);
		startService(intentSporting);
		
		Button buttonStop = (Button)findViewById(R.id.button_stop_sporting);
		
		buttonStop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentStopSporting = new Intent(JoggingActivity.this,StepService.class);
				stopService(intentStopSporting);
				Intent intentShowJog = new Intent(JoggingActivity.this,ShowJogActivity.class);
				startActivity(intentShowJog);
			}
			
		});

	}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sporting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
