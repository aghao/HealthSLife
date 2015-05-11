package com.healthlife.activity;

import com.healthlife.R;
import com.healthlife.R.id;
import com.healthlife.R.layout;
import com.healthlife.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BackupsActivity extends Activity {
	

	private Button backups_bt;
	private Button recovery_bt;
	
	private BroadcastReceiver backups_receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int Ret=arg1.getIntExtra("Result", 0);
			//Bundle bundle = arg1.getExtras();
			//String s=bundle.getString("Result");
			Log.i("TEST","receive:"+Ret);
			if(Ret==-1)
				Toast.makeText(BackupsActivity.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
			else if(Ret==0)
				Toast.makeText(BackupsActivity.this, "�����쳣 ", Toast.LENGTH_SHORT).show();
			else if(Ret==1)
				Toast.makeText(BackupsActivity.this, "�������ݳɹ�", Toast.LENGTH_SHORT).show();
		}
		
		
	};
	
	
	private BroadcastReceiver recovery_receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int Ret=arg1.getIntExtra("Result", 0);
			//Bundle bundle = arg1.getExtras();
			//String s=bundle.getString("Result");
			Log.i("TEST","receive:"+Ret);
			if(Ret==-1)
				Toast.makeText(BackupsActivity.this, "��ԭ����ʧ��", Toast.LENGTH_SHORT).show();
			else if(Ret==0)
				Toast.makeText(BackupsActivity.this, "�����쳣 ", Toast.LENGTH_SHORT).show();
			else if(Ret==1)
				Toast.makeText(BackupsActivity.this, "��ԭ���ݳɹ�", Toast.LENGTH_SHORT).show();
		}
		
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_backups);
		
		backups_bt=(Button) findViewById(R.id.backups_bt);
		recovery_bt=(Button) findViewById(R.id.recovery_bt);
		
		backups_bt.setOnClickListener(new Backups());
		recovery_bt.setOnClickListener(new Recovery());
		
		registerReceiver(backups_receiver,new IntentFilter("BACKUPS"));
		registerReceiver(recovery_receiver,new IntentFilter("RECOVERY"));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(backups_receiver);
		unregisterReceiver(recovery_receiver);
	}
	
	class Backups implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
			Intent intent = new Intent(BackupsActivity.this, 
	        		ClientService.class); 
			
			intent.putExtra("action", "BACKUPS");	
			//intent.putExtra("userid", 0);
			//intent.putExtra("username", "ssh");
			//intent.putExtra("password", "123456");
	        startService(intent);
		}
		
	}
	
	class Recovery implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		
			
				Intent intent = new Intent(BackupsActivity.this, 
		        		ClientService.class); 
				intent.putExtra("action", "RECOVERY");
				//intent.putExtra("userid", user.getUserId());
				//intent.putExtra("username", user.getUsersName());
				//intent.putExtra("password", user.getPassWord());
		        startService(intent);
		        

		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.backups, menu);
		return true;
	}

}
