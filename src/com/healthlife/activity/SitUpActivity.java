package com.healthlife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
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

	private Button btnStart;
	private TextView textNum;
	
	private SitUpService.DBinder dBinder;
	private ServiceConnection connection;
	
	private IntentFilter intentFilter;
	private MotionReceiver motionReceiver;
	
	private Sports sitUp;
	private Activity thisActivity;
	private boolean startFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_situp);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		startFlag=false;
		
		thisActivity = this;
		
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.healthlife.activity.SitUpActivity.MotionAdd");

		motionReceiver = new MotionReceiver();
		
		registerReceiver(motionReceiver,intentFilter);
		
		btnStart=(Button)findViewById(R.id.button_start_situp);
		
		btnStart.setOnClickListener(this);
		
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
		

		if(!startFlag){
			Intent intentBind = new Intent(this,com.healthlife.util.SitUpService.class);
			bindService(intentBind,connection,BIND_AUTO_CREATE);
			btnStart.setBackgroundResource(R.drawable.sportresult_cancelbtn_selector);
			startFlag=true;
		}
			
		else{	
			sitUp = dBinder.getSitUp();
			if(sitUp.getNum()!=0){
				unregisterReceiver(motionReceiver);
				unbindService(connection);
				Intent intentStop = new Intent(this,SitUpService.class);
				stopService(intentStop);
				Intent intentNextActivity = new Intent(this,com.healthlife.activity.ShowPushUpOrSitUpActivity.class);
				intentNextActivity.putExtra("situp",sitUp);
				intentNextActivity.putExtra("type", GlobalVariables.SPORTS_TYPE_SITUP);
				startActivity(intentNextActivity);
				finish();
			}
			else{
				 showWarning();
			}
		}	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}
	
	private void showWarning(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("未检测到运动哦");
		alertDialog.setMessage("是否退出运动");
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intentToNextActivity = new Intent(thisActivity,SelectSportsActivity.class); 
				startActivity(intentToNextActivity);	
				finish();
			}
		});
		alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});		
		alertDialog.show();
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
