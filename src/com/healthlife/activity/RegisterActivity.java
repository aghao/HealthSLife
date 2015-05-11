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
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	
	private EditText usernameText;
	private EditText passwordText;
	private EditText confirmText;
	private Button register_bt;
	private Button cancel_bt;
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int Ret=arg1.getIntExtra("Result", 0);
			//Bundle bundle = arg1.getExtras();
			//String s=bundle.getString("Result");
			Log.i("TEST","receive:"+Ret);
			if(Ret==-1)
				Toast.makeText(RegisterActivity.this, "�û����Ѵ���", Toast.LENGTH_SHORT).show();
			else if(Ret==0)
				Toast.makeText(RegisterActivity.this, "�����쳣 ", Toast.LENGTH_SHORT).show();
			else if(Ret==1)
				Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
		}
		
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		usernameText=(EditText) findViewById(R.id.username);
		passwordText=(EditText) findViewById(R.id.password);
		confirmText=(EditText) findViewById(R.id.confirm);
		register_bt=(Button) findViewById(R.id.register_bt);
		cancel_bt=(Button) findViewById(R.id.cancel_bt);
		
		register_bt.setOnClickListener(new Register());
		cancel_bt.setOnClickListener(new Cancel());
		
		registerReceiver(receiver,new IntentFilter("REGISTER"));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		unregisterReceiver(receiver);
	}

	
	
	class Register implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String username=usernameText.getText().toString();
			String password=passwordText.getText().toString();
			String confirm=confirmText.getText().toString();
			if(username.isEmpty()||password.isEmpty())
			{
				Toast.makeText(RegisterActivity.this, "�˺����벻��Ϊ��", Toast.LENGTH_SHORT).show();
			}
			else if(!password.equals(confirm))
			{
			
				Toast.makeText(RegisterActivity.this, "������������벻һ��", Toast.LENGTH_SHORT).show();
			}
			else
			{
				
				Intent intent = new Intent(RegisterActivity.this, 
		        		ClientService.class); 
				intent.putExtra("action", "REGISTER");
				intent.putExtra("username", username);
				intent.putExtra("password", password);
		        startService(intent);
			}
		}
		
	}
	
	
	class Cancel implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
