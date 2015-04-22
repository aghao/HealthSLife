package com.healthlife.activity;

import com.healthlife.R;
import com.healthlife.R.layout;
import com.healthlife.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HeartHistory extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hearthistory_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.heart_history, menu);
		return true;
	}

}
