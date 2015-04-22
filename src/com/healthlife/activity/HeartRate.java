package com.healthlife.activity;

import com.healthlife.util.Mallat;
import com.healthlife.util.YuvToRGB;
import com.healthlife.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class HeartRate extends Activity {

	private static final int NUM = 64;
	private Button historyButton = null ;
	private static Camera mCamera = null ;
	private CameraView myCV = null ;
	private TextView lastText= null;
	private TextView helpText = null;
	private double [] red = new double [NUM];
	private int point = 0;
	//计时变量
	private long startTime = 0;
	private long endTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartrate_main);
		historyButton = (Button) findViewById(R.id.historybt);
		lastText = (TextView) findViewById(R.id.heartratetest);
		lastText.setText("无记录");
		
		//检测摄像头
		if(checkCameraHardware(this)){
			Log.e("==========", "摄像头存在");
		}
		helpText = new TextView(this);
		ImageButton cameraButton = new ImageButton(this);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.cameraview);
		myCV = new CameraView(HeartRate.this);
		

		helpText.setText("点击开始测量");
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		cameraButton.setLayoutParams(lp);
		frameLayout.addView(cameraButton);
		helpText.setLayoutParams(lp);
		frameLayout.addView(helpText);
		//设定自定义surfaceview大小
		lp = new FrameLayout.LayoutParams(1, 1);
		lp.gravity = Gravity.CENTER;
		myCV.setLayoutParams(lp);
		frameLayout.addView(myCV);  
//		myCV = (CameraView) findViewById(R.id.cameraview);

		
		//按钮响应
		cameraButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(null == mCamera)
				{
					startPreview(myCV.getSurfaceHolder());
					helpText.setText("请将手指轻放在摄像头上");
				}
				else
				{
					helpText.setText("点击开始测量");
					stopCamera();
				}
			}
		});
		
		historyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HeartRate.this,HeartHistory.class);
				startActivity(intent);
			}
		});
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
		stopCamera();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		helpText.setText("点击开始测量");
//		mCamera.stopPreview();
//		mCamera.release();
//        mCamera = null;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	//预览图像的数据处理
	private PreviewCallback mPriviewCallBack = new PreviewCallback(){

		@Override
		public void onPreviewFrame(byte[] data, Camera cam) {
			// TODO Auto-generated method stub
			 if (data == null) throw new NullPointerException();
	         Camera.Size size = cam.getParameters().getPreviewSize();
	         if (size == null) throw new NullPointerException();
	         
	         int width = size.width;
	         int height = size.height;
	         
	         double redAvg = YuvToRGB.getRed(data.clone(), width, height);
	         if(redAvg == -1)
	         {
	        	 helpText.setText("请将手指轻放在摄像头上");
	        	 point =0;
	         }
	         else
	         {
	        	 if(point == 0)
	        	 {
	        		 startTime = System.currentTimeMillis();
	        		 helpText.setText("正在测量请勿移动手指");
	        	 }
		         red[point]=redAvg;
//		         Log.v("rrrrrrrred",""+redAvg);
		         point ++;
		         if(point==63)
		         {
		        	 endTime = System.currentTimeMillis();
		        	 getHeartRate();
		        	 
		        	 stopCamera();
		         }
	         }
	         Log.v("point",""+point);
	        
		}
		
	};
	
	//自定义相机视图  开启相机，预览图像
	 class CameraView extends SurfaceView implements SurfaceHolder.Callback{
		
		private  SurfaceHolder mHolder = null;
		
		public CameraView(Context context) {
			super(context);
			
			mHolder = this.getHolder();
			mHolder.addCallback(this);
			
			}
		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			stopCamera();
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		public  SurfaceHolder getSurfaceHolder(){
			return mHolder;
		}
		
	}
    
	private void startPreview(SurfaceHolder holder){
		try{
			mCamera = Camera.open();
			//开启闪光灯
            Camera.Parameters param = mCamera.getParameters(); 
            param.setFlashMode(Parameters.FLASH_MODE_TORCH);
//            param.setPreviewFpsRange(20,30);
            //添加预览回调
            mCamera.setPreviewCallback(mPriviewCallBack);
            mCamera.setParameters(param); 
            mCamera.setPreviewDisplay(holder); 
		}
		catch(Exception e){
		//如果失败释放摄像头

			Log.e("==========", "ERROR");
			mCamera.release();
	        mCamera = null;
		}
		//开始预览
		mCamera.startPreview();
	}
	
	private static void stopCamera(){
		if(null != mCamera)
		{
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
	        mCamera = null;
		}
	}
	
	//去噪后的数据寻峰运算得到心率
	private void getHeartRate()
	{
		double [] output = Mallat.Analyze(red,NUM);
		int num = 0;
		int start = 0;
		int end =0;
		//寻找峰数NUM
		for(int i =1;i<NUM-1;i++)
		{
			if(output[i]>output[i-1]&&output[i]>output[i+1])
			{
				if(num == 1)
				{
					start = i;
				}
				num++;
				end = i;
			}
		}
//		Log.v("time",""+endTime);
//
//		Log.v("time",""+startTime);
		double time = (double)(endTime-startTime)/60000;
		//计时器归0
		endTime = startTime = 0;
		//寻峰计算心率
		num = (int) ((num-1)*(end-start)/time/NUM);
		helpText.setText(num+"bmp");
		Intent intent = new Intent();
		intent.setClass(HeartRate.this,HeartResult.class);
		startActivity(intent);
		point = 0;
	}
	
	//对话框

	
	//检查摄像头是否存在
	private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // 摄像头存在
            return true;
        } else {
            // 摄像头不存在
            return false;
        }
    }
    
}
