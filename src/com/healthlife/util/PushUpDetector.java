package com.healthlife.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class PushUpDetector implements SensorEventListener {
	
	private final long TIMEGAPGATE = 500;//ms
	
	private float mProximity;
	private int mMotionAmount;
	private int mValidMotionAmount;
	private int mPerfectMotionAmount;
	private long mLastTime;
	private long mCurrentTime;
	private long mTimeGap;
	private boolean mValidFlag;
	private boolean mTimeGapFlag;
	
	public PushUpDetector(){
		mMotionAmount = 0;
		mValidMotionAmount = -1;
		mPerfectMotionAmount = 0;
		mValidFlag = false;
		mTimeGapFlag = false;
	}
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//Log.i("dd","proximityX:" + String.valueOf(event.values[0]));
		synchronized (this){

			Sensor sensor = event.sensor;
			if(sensor.getType()== Sensor.TYPE_PROXIMITY);
			{
				mTimeGapFlag=isTimeGapEnough();
				mProximity = event.values[0];
				//Log.i("dd","proximity:" + String.valueOf(event.values[0]));
				
				if(mProximity>3&&mTimeGapFlag)
				{
					mValidMotionAmount+=1;
					mValidFlag=true;
				}
				else if(mProximity<=3&&isValid())
				{
					mPerfectMotionAmount+=1;
				}
				
				Log.i("dd","vAmount="+ mValidMotionAmount+";   pAmount="+mPerfectMotionAmount);
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
		//Log.i("dd", "TimeGap:"+String.valueOf(mTimeGap));
		
		if(mTimeGap>=TIMEGAPGATE) 
		{
			return true;
		}
		
		else 
			return false;
	}
	
	private boolean isValid(){
		if(mValidFlag==true)
		{
			mValidFlag=false;
			return true;
		}
		else 
			return false;
		
	}
}
