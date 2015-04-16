package com.healthlife.activity;

import com.healthlife.util.YuvToRGB;
import com.healthslife.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class HeartRate extends Activity {

	private static final int NUM = 100;
	private ImageButton cameraButton = null ;
	private static Camera mCamera = null ;
	private static CameraView myCV = null ;
	private static TextView testtext= null;
	private static int [] red = new int [NUM];
	private static int point = 0;
	//计时变量
	private static long startTime = 0;
	private static long endTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartrate_main);
		cameraButton = (ImageButton) findViewById(R.id.hrtestib);
		testtext = (TextView) findViewById(R.id.heartratetest);
		testtext.setText("无记录");
		
		//检测摄像头
		if(checkCameraHardware(this)){
			Log.e("==========", "摄像头存在");
		}
		
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.cameraview);
		myCV = new CameraView(HeartRate.this);
		//设定自定义surfaceview大小
		myCV.setLayoutParams(new FrameLayout.LayoutParams(10, 10));
		frameLayout.addView(myCV);  
		
		//按钮响应
		cameraButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(null == mCamera)
				{
					startPreview(myCV.getSurfaceHolder());
				}
				else
				{
					stopCamera();
				}
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	private static PreviewCallback mPriviewCallBack = new PreviewCallback(){

		@Override
		public void onPreviewFrame(byte[] data, Camera cam) {
			// TODO Auto-generated method stub
			 if (data == null) throw new NullPointerException();
	         Camera.Size size = cam.getParameters().getPreviewSize();
	         if (size == null) throw new NullPointerException();
	         
	         int width = size.width;
	         int height = size.height;
	         
	         int redAvg = YuvToRGB.getRed(data.clone(), width, height);
	         if(redAvg>0)
	         {
	        	 if(point == 0)
	        	 {
	        		 startTime = System.currentTimeMillis();
	        	 }
		         red[point]=redAvg;
		         point ++;
		         Log.v("NUM", ""+point);
		         if(point>=NUM)
		         {
		        	 endTime = System.currentTimeMillis();
		        	 getHeartRate();
		        	 
		        	 stopCamera();
		         }
	         }
	        
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
	private static void getHeartRate()
	{
		int point = 0;
		for(int i =1;i<NUM-1;i++)
		{
			if(red[i]>red[i-1]&&red[i]>red[i+1])
			{
				point++;
			}
		}
		long time = (endTime-startTime)*60000;
		//计时器归0
		endTime = startTime = 0;
		testtext.setText(""+point/time);
		point = 0;
	}
	
	
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
