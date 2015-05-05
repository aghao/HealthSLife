package com.healthlife.util;

import java.text.SimpleDateFormat;

import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SitUpAnalyser implements SensorEventListener {

	private Sensor sensor;
	private float mYOrientation;
	private float mLastYOrientation;
	private int mDirection;
	private int mLastDirection;
	private boolean mButtomFlag;
	private boolean mDiffFlag;
	private int mMotionAmount;
	private int mValidMotionAmount;
	private int mPerfectMotionAmount;
	private long mCurrentTime;
	private long mTimeGap;
	private long mLastTime;
	private float mLastDiffOrientation;
	private long mStartTime;
	private long mEndTime;
	private String mDate;
	private String mDuration;
	
	private Sports sitUp;
	
	private final float BUTTOM_ORIENTATION = 10;
	private final float VALID_ORIENTATION = 100;
	private final float PERFECT_ORIENTATION = 180;
	private final float TIME_GAP_GATE=1000;
	private final float DIFF_GATE=30;
	
	@SuppressLint("SimpleDateFormat")
	public SitUpAnalyser(){
		
		SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		mDate = simpleFormatter.format( new java.util.Date());

	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		 synchronized (this){
			 if(mStartTime==0)
				 mStartTime=System.currentTimeMillis();
			 
			 sensor = event.sensor;
			 mYOrientation = event.values[2];

			 if(sensor.getType()==Sensor.TYPE_ORIENTATION){
				 mDirection = (mLastYOrientation>mYOrientation?-1:(mLastYOrientation<mYOrientation?1:0));
				 mDiffFlag=(mDirection==mLastDirection?false:(isTimeGapEnough()&&isDiffEnough()?true:false));				 
				 mButtomFlag=(mYOrientation>=BUTTOM_ORIENTATION?true:false);
				 
				 if(mDiffFlag&&mButtomFlag){
					 mMotionAmount+=1;
					 mEndTime=System.currentTimeMillis();
					 mButtomFlag=false;
					 if(mYOrientation>VALID_ORIENTATION)
						mValidMotionAmount+=1;
					 else if(mYOrientation>PERFECT_ORIENTATION)
						mPerfectMotionAmount+=1; 
				 }					 
			 }	 
		 }
	}
		
	private boolean isDiffEnough() {
		// TODO Auto-generated method stub
		if(mLastDiffOrientation-mYOrientation<=DIFF_GATE)
			return false;
		else 
			mLastDiffOrientation=mYOrientation;
			return true;
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
		
		sitUp.setType(GlobalVariables.SPORTS_TYPE_SITUP);
		sitUp.setDate(mDate);
		sitUp.setNum(mMotionAmount);
		sitUp.setValidRate(mValidMotionAmount/mMotionAmount);
		sitUp.setPerfectRate(mPerfectMotionAmount/mMotionAmount);
		sitUp.setDuration(mDuration);
		
		return sitUp;
		
	}
}
