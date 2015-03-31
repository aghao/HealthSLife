package com.healthlife.activity;

import com.healthslife.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;

public class HeartRate extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartrate_main);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	//自定义相机视图  开启相机，预览图像
	class CameraView extends SurfaceView {
		public CameraView(Context context) {
			super(context);
		}
	}

}
