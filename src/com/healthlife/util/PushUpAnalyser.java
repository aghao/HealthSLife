package com.healthlife.util;

import java.text.SimpleDateFormat;

import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class PushUpAnalyser implements SensorEventListener {
	
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
	private long mStartTime;
	private long mEndTime;
	private String mDate;
	private String mDuration;

	private Sports pushUp;
	
	@SuppressLint("SimpleDateFormat")
	public PushUpAnalyser(){
		mValidMotionAmount = -1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
		mDate = formatter.format(new java.util.Date());
	}
	@SuppressLint("SimpleDateFormat")
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//Log.i("dd","proximityX:" + String.valueOf(event.values[0]));		
		synchronized (this){

			Sensor sensor = event.sensor;
			if(sensor.getType()== Sensor.TYPE_PROXIMITY);
			{	
				if(mStartTime==0) 
					mStartTime=System.currentTimeMillis();	     
				
				mTimeGapFlag=isTimeGapEnough();
				mProximity = event.values[0];
				//Log.i("dd","proximity:" + String.valueOf(event.values[0]));
				
				if(mProximity>3&&mTimeGapFlag)
				{
					mValidMotionAmount+=1;
					mValidFlag=true;
					mEndTime=System.currentTimeMillis();
				}
				else if(mProximity<=3&&isValid())
				{
					mPerfectMotionAmount+=1;
					mEndTime=System.currentTimeMillis();
				}
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
	
	private float getValidRate(){
		return mValidMotionAmount/mMotionAmount;
	}
	
	private float getPerfectRate(){
		return mPerfectMotionAmount/mMotionAmount;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getDuration(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		mDuration = formatter.format(mEndTime-mStartTime);
		return mDuration;
	}
	
	public Sports getPushUp(){
		
		pushUp.setType(GlobalVariables.SPORTS_TYPE_PUSHUP);
		pushUp.setNum(mMotionAmount);
		pushUp.setValidRate(getValidRate());
		pushUp.setPerfectRate(getPerfectRate());
		pushUp.setDuration(getDuration());
		pushUp.setDate(mDate);
		
		return pushUp;
	}
}
