package com.healthlife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.healthlife.R;
import com.healthlife.util.CurrentUser;

public class AppStart extends Activity{
	
	private Button loginBtn;
	private Button regBtn;
	private CurrentUser currentUser = new CurrentUser(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
