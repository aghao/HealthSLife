package com.healthlife.activity;

import com.healthlife.entity.Sports;
import com.healthslife.R;

import android.app.Activity;
import android.os.Bundle;


public class ShowWalkActivity extends Activity {

	private Sports walk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_walk);
		walk=(Sports) getIntent().getSerializableExtra("walk");
		
		
		
	}
}
