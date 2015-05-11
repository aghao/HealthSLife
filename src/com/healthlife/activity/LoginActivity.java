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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText usernameText;
	private EditText passwordText;
	private Button login_bt;
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
				Toast.makeText(LoginActivity.this, "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();
			else if(Ret==0)
				Toast.makeText(LoginActivity.this, "网络异常 ", Toast.LENGTH_SHORT).show();
			else if(Ret==1)
				Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
		}
		
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		usernameText=(EditText) findViewById(R.id.username);
		passwordText=(EditText) findViewById(R.id.password);
		login_bt=(Button) findViewById(R.id.login_bt);
		cancel_bt=(Button) findViewById(R.id.cancel_bt);
		
		login_bt.setOnClickListener(new Login());
		cancel_bt.setOnClickListener(new Cancel());
		
		registerReceiver(receiver,new IntentFilter("LOGIN"));
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Log.i("TEST","ondestroy");
		unregisterReceiver(receiver);
	}

	class Login implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
	
				String username=usernameText.getText().toString();
				String password=passwordText.getText().toString();
				
				if(username.isEmpty()||password.isEmpty())
				{
					Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent(LoginActivity.this, 
			        		ClientService.class); 
					intent.putExtra("action", "LOGIN");
					intent.putExtra("username", username);
					intent.putExtra("password", password);
			        startService(intent);
				}
		       // LoginActivity.this.finish();
				
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
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
