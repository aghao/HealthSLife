package com.healthlife.util;

import com.healthlife.entity.GlobalVariables;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class SitUpDetector implements SensorEventListener {

	private float mCurrentSpeed;
	private float mDirection;
	private long mCurrentTime;
	private long mLastTime;
	private float mSamples[];
	private int mSampleCounter;
	private float mXOffSet;
	private Sensor sensor;
	private boolean mSampleFlag;
 
	public SitUpDetector(){
		mSamples = new float [50];
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		 synchronized (this){
			 
			 sensor = event.sensor;

			 if(mSampleCounter<49)
			 {
				 mSamples[mSampleCounter]=event.values[0];	 
				 mSampleCounter +=1;
			 }
			 
			 else 
			 {
				 if(mSampleFlag==false){
					 int i;
					 float vSum = 0;
					 for(i=0;i<mSampleCounter;i++){
						 Log.i("dd", i+"sample="+mSamples[i]);
						 vSum += mSamples[i];
					 }
					 Log.i("dd", "vSum="+vSum+" "+"Counter="+mSampleCounter);
					 mXOffSet=vSum/(mSampleCounter+1);
					 mSampleFlag=true;
				 }
			 }
			 
			 if(sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION&&mXOffSet!=0){
				 mCurrentSpeed = mCurrentSpeed+(event.values[0]-mXOffSet);
				 //Log.i("dd", String.valueOf(mXOffSet)+" "+String.valueOf(event.values[0])+" "+String.valueOf(event.values[0]-mXOffSet));
				 Log.i("dd", String.valueOf(mCurrentSpeed));	 
			 }
		 }
		

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

}
