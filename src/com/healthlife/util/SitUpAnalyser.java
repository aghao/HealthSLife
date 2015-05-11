package com.healthlife.util;

import java.text.SimpleDateFormat;

import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


@SuppressLint("SimpleDateFormat")
public class SitUpAnalyser implements SensorEventListener {
	
	private Sports sitUp; 
	
    private float[] accelerometerValues = new float[3];  
    private float[] magneticFieldValues = new float[3];  
    private float[] values = new float[3]; 
    
    private Context context;  
    private int mNum;  
	private int mValidNum;  
	private int mGoodNum;  
	private int mPerfectNum;  
	
	private long mStartTime;
	private long mEndTime;
	private boolean mStartFlag;
	private long mCurrentTime;
		
	private String mDate;
	private String mDuration;
	private int mPace;
	
	private boolean mValidFlag;
	private boolean mGoodFlag;
	private boolean mPerfectFlag;
	private boolean mBaseValidFlag;
	private boolean mBaseGoodFlag;
	private boolean mBasePerfectFLag;
	private boolean mInEnd;
	
	private int mDirection;
	private int mLastDirection;
	private float mLastYOrientation;
	
	private long mPaceStartTime;
	private int mPaceSamples;
	
	private static final float VALID_POINT=225;
	private static final float GOOD_POINT=255;
	private static final float PERFECT_POINT=270;
	
	private static final float BASE_VALID_POINT=135;
	private static final float BASE_GOOD_POINT=110;
	private static final float BASE_PERFECT_POINT=90;
	
	private static final float SENSITIVITY = 9.5f;
    	
	public SitUpAnalyser(Context context){
		mStartFlag=true;
		this.context=context;		
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		// TODO Auto-generated method stub
		//获得产生该次数据时的方向
	    
		
		synchronized(this){
	    	if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)  
		    	magneticFieldValues = sensorEvent.values.clone();  
		    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)  
		        accelerometerValues = sensorEvent.values.clone();  
		    
		    //获得当前朝向角度
		    calculateOrientation();
		    Log.i("dd", "Orientation: "+values[2]);
		    checkKeyPoints();
			judgeMotion();
		    
			Log.i("dd","----------------------------------------------------------------");
			}
		}
	    	    	    
	private  void calculateOrientation() {   
	    float[] R = new float[9];  
	    SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);           
	    SensorManager.getOrientation(R, values);  
	
	    // 要经过一次数据格式的转换，转换为度  
	    values[0] = (float) Math.toDegrees(values[0]);  
	    values[1] = (float) Math.toDegrees(values[1]);  
	    values[2] = (float) Math.toDegrees(values[2]);
	    values[2] +=90;	
	    if(values[2]<0&&values[2]>=-90){
	    	values[2]+=360;
	    }
	}
	
	private void sendBroadcast(){
		Intent intent = new Intent("com.healthlife.activity.SitUpActivity.MotionAdd");
		intent.putExtra("motionNum", mNum);
		context.sendBroadcast(intent);
		Log.i("dd", "mValidNum: "+mValidNum+"   mGoodNum: "+mGoodNum+"   mPerfectNum: "+mPerfectNum);
	}
	
	public String getDuration(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		mDuration = formatter.format(mEndTime-mStartTime+57600000);
		return mDuration;
	}
	
	public Sports getSitUp(){
		sitUp = new Sports();
		
		sitUp.setType(GlobalVariables.SPORTS_TYPE_SITUP);
		sitUp.setDate(mDate);
		sitUp.setNum(mNum);
		sitUp.setValidNum(mValidNum);
		sitUp.setGoodNum(mGoodNum);
		sitUp.setPerfectNum(mPerfectNum);
		sitUp.setDuration(getDuration());
		
		return sitUp;
		
	}
	
	private void operateTime(){

		mCurrentTime = System.currentTimeMillis();
		
		//若为第一次读数则记录开始时间
		if(mStartFlag){
			SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
			mDate = simpleFormatter.format( new java.util.Date());
			mStartTime = mCurrentTime;
			mPaceStartTime = mCurrentTime;
			mStartFlag = false;
		}
		mEndTime = mCurrentTime;
	}
	
	private void checkKeyPoints(){
		if(values[2]>=VALID_POINT){
			mInEnd=true;
			if(values[2]>=VALID_POINT)
				mValidFlag=true;
			if(values[2]>=GOOD_POINT)
				mGoodFlag=true;
			if(values[2]>=PERFECT_POINT)
				mPerfectFlag=true;
			
			
		}
		
		if(values[2]<=BASE_VALID_POINT){
			mInEnd=false;
			if(values[2]<=BASE_VALID_POINT)
				mBaseValidFlag = true;
			if(values[2]<=BASE_GOOD_POINT)
				mBaseGoodFlag = true;
			if(values[2]<=BASE_PERFECT_POINT)
				mBasePerfectFLag = true;
			
			mGoodFlag=false;
			mValidFlag=false;
			mPerfectFlag=false;
		}
		
		Log.i("dd", "mBaseValidFlag:  "+mBaseValidFlag+"   mBaseGoodFlag:"+mBaseGoodFlag+"   mBasePerfectFLag:"+ mBasePerfectFLag);
		Log.i("dd", "mValidFlag:  "+mValidFlag+"   mGoodFlag:"+mGoodFlag+"   mPerfectFLag:"+ mPerfectFlag);
	}

	private void judgeMotion(){
		Log.i("dd", "mDiffOrientation: "+(mLastYOrientation-values[2]));
		if(mLastYOrientation-values[2]>=(10-SENSITIVITY))
			mDirection = -1;
		else if(values[2]-mLastYOrientation>=(10-SENSITIVITY))
			mDirection = 1;
		Log.i("dd", "direction: "+mDirection+"     mLastDirection:  "+mLastDirection);
		if(mLastDirection==-1&&mDirection==-1&&mValidFlag&&mInEnd){
				Log.i("dd", "get motion");
			if(mBasePerfectFLag&&mPerfectFlag){
				mPerfectNum+=1;
				mNum +=1;
				mPaceAnalyse();
				sendBroadcast();
			}
			else if (mBaseGoodFlag&&mGoodFlag){
				mGoodNum+=1;
				mNum +=1;
				mPaceAnalyse();
				sendBroadcast();
			}
			else if (mBaseValidFlag&&mValidFlag){
				mValidNum+=1;
				mNum +=1;
				mPaceAnalyse();
				sendBroadcast();
			}
			Log.i("dd", "mValidNum: "+mValidNum+"   mGoodNum: "+mGoodNum+"   mPerfectNum: "+mPerfectNum+"  mNum:  "+mNum);
			
			mValidFlag=(values[2]>=BASE_VALID_POINT?true:false);
			mGoodFlag=(values[2]>=BASE_GOOD_POINT?true:false);
			mPerfectFlag=(values[2]>=BASE_PERFECT_POINT?true:false);		
		
			mBaseValidFlag = false;
			mBaseGoodFlag = false;
			mBasePerfectFLag = false;
			
			
		}
		mLastYOrientation = values[2];
		mLastDirection=mDirection;
		operateTime();
	}

	private void mPaceAnalyse() {
		// TODO Auto-generated method stub
    	if(mEndTime-mPaceStartTime<60000)
    		mPaceSamples+=1;
    	
    	else if(mEndTime-mPaceStartTime>=60000){
    		if(mPaceSamples>=0&&mPaceSamples<=15)
    			mPace=1;
    		else if(mPaceSamples>15&&mPaceSamples<=30)
    			mPace=2;
    		else if(mPaceSamples>30&&mPaceSamples<=45)
    			mPace=3;
    		else if(mPaceSamples<60&&mPaceSamples<=75)
    			mPace=4;
    		else if(mPaceSamples>75)
    			mPace=5;
    		
    		mPaceStartTime=System.currentTimeMillis();
    		mPaceSamples=0;
    		Intent intent = new Intent("com.healthlife.activity.MusicService");
    		intent.putExtra("pace", mPace);
    		context.startService(intent);
    	}
    		
		
	}

}








/*
public class SitUpAnalyser implements SensorEventListener {

	private Sensor sensor;
	private float mYOrientation;
	private float mLastYOrientation;
	private boolean mButtomFlag;
	private int mMotionAmount;
	private int mValidMotionAmount;
	private int mPerfectMotionAmount;
	private long mCurrentTime;
	private long mTimeGap;
	private long mLastTime;
	private long mStartTime;
	private long mEndTime;
	private String mDate;
	private String mDuration;
	
	private Sports sitUp;
	private Context context;
	private Intent intent;
	
	private final float BUTTOM_ORIENTATION = 30;
	private final float VALID_ORIENTATION = 100;
	private final float PERFECT_ORIENTATION = 170;
	private final float TIME_GAP_GATE=1000;
	private final float SENSITIVITY=5;
}
	
	
	@SuppressLint("SimpleDateFormat")
	public SitUpAnalyser(Context context){
		this.context = context;
		
		SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		mDate = simpleFormatter.format( new java.util.Date());
		
		intent = new Intent("com.healthlife.activity.SitUpActivity.MotionAdd");

	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		 synchronized (this){ 
			 sensor = event.sensor; 
			 
			 if(sensor.getType()==Sensor.TYPE_GYROSCOPE){
				 mYOrientation = -event.values[1];
				 Log.i("dd", String.valueOf(event.values[2]));
				 			 
				 if(mStartTime==0)
					 mStartTime=System.currentTimeMillis();
				 
				 getDirection();//(mLastYOrientation>mYOrientation?-1:(mLastYOrientation<mYOrientation?1:0));
				 if(mYOrientation<=BUTTOM_ORIENTATION){
					 mButtomFlag=true;
				 }
				 
					 //Log.i("dd", String.valueOf(mDirection));
				 if(mButtomFlag&&mYOrientation>VALID_ORIENTATION){
					 mMotionAmount+=1;
					 //Log.i("dd", String.valueOf(mMotionAmount));
					 mEndTime=System.currentTimeMillis();
					 mButtomFlag=false;
					 
					 intent.putExtra("motionNum", mValidMotionAmount+1);
					 context.sendBroadcast(intent);
					
					 if(mYOrientation>VALID_ORIENTATION)
						mValidMotionAmount+=1;
					 else if(mYOrientation>PERFECT_ORIENTATION)
						//Log.i("dd", String.valueOf(mMotionAmount));
						mPerfectMotionAmount+=1; 
				 }
				 mLastYOrientation = mYOrientation;
			 }	 
		 }
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private boolean isTimeGapEnough(){
		
		mCurrentTime=System.currentTimeMillis();
		mTimeGap=mCurrentTime-mLastTime;
		mLastTime=mCurrentTime;

		if(mTimeGap>=TIME_GAP_GATE) 
		{
			return true;
		}
		
		else 
			return false;
	}

	@SuppressLint("SimpleDateFormat")
	public Sports getSitUp(){
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat ("hh:mm:ss");
		mDuration = dateFormatter.format(mEndTime-mStartTime);
		sitUp = new Sports();
		
		sitUp.setType(GlobalVariables.SPORTS_TYPE_SITUP);
		sitUp.setDate(mDate);
		sitUp.setNum(mMotionAmount);
		sitUp.setValidNum(mValidMotionAmount);
		sitUp.setPerfectNum(mPerfectMotionAmount);
		sitUp.setDuration(mDuration);
		
		return sitUp;
		
	}
	
	public String getDuration(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		mDuration = formatter.format(mEndTime-mStartTime);
		return mDuration;
	}

	public void getDirection(){
		if(mLastYOrientation-mYOrientation>=(10-SENSITIVITY)) {
		} else if(mYOrientation-mLastYOrientation>=(10-SENSITIVITY)) {
		}
	}

}
*/
