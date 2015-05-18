package com.healthlife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.healthlife.R;
import com.healthlife.entity.User;
import com.healthlife.util.CurrentUser;

public class AppStart extends Activity{
	
	private Button loginBtn;
	private Button regBtn;
	private CurrentUser currentUser = new CurrentUser(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CurrentUser user=new CurrentUser(AppStart.this);
        User user1=user.QueryCurrentUser();
        if(user1==null)
        {
        	Log.i("TEST","首次登陆");
        	Toast.makeText(this, "~欢迎进入健康生活~", Toast.LENGTH_SHORT).show();
        }
        else
        {
        	Intent intent = new Intent();
        	intent.setClass(AppStart.this, MainActivity.class);
        	startActivity(intent);
        	finish();
        	Log.i("TEST","已登录");
        	Log.i("TEST","userid:"+user1.getUserId());
        	Log.i("TEST","username:"+user1.getUsersName());
        	Log.i("TEST","password:"+user1.getPassWord());
        }
		
		setContentView(R.layout.activity_start);
		
		loginBtn = (Button)findViewById(R.id.appstart_loginbtn);
		regBtn = (Button)findViewById(R.id.appstart_regbtn);
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AppStart.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		regBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AppStart.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
