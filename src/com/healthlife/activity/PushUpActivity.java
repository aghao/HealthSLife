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
import android.view.Menu;
import android.view.MenuItem;
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
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
			btnStart.setBackgroundResource(R.drawable.sportresult_cancelbtn_selector);
			startFlag=true;
			}
			
			else{
			pushUp = dBinder.getPushUp();
			if(pushUp.getNum()!=0){
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
			else{
				showWarning();
			}
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
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}
	
}
