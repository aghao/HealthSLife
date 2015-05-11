package com.healthlife.util;

import java.text.SimpleDateFormat;

import com.healthlife.activity.MusicService;
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
	private int mLastPace;
	
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
				paceAnalyse();
				sendBroadcast();
			}
			else if (mBaseGoodFlag&&mGoodFlag){
				mGoodNum+=1;
				mNum +=1;
				paceAnalyse();
				sendBroadcast();
			}
			else if (mBaseValidFlag&&mValidFlag){
				mValidNum+=1;
				mNum +=1;
				paceAnalyse();
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

	private void paceAnalyse() {
		// TODO Auto-generated method stub
    	if(mEndTime-mPaceStartTime<30000)
    		mPaceSamples+=1;
    	
    	else if(mEndTime-mPaceStartTime>=30000){
    		if(mPaceSamples>=0&&mPaceSamples<=10)
    			mPace=1;
    		else if(mPaceSamples>10&&mPaceSamples<=20)
    			mPace=2;
    		else if(mPaceSamples>20&&mPaceSamples<=30)
    			mPace=3;
    		else if(mPaceSamples<30&&mPaceSamples<=40)
    			mPace=4;
    		else if(mPaceSamples>40)
    			mPace=5;
    		
    		mPaceStartTime=System.currentTimeMillis();
    		mPaceSamples=0;
    		if(mPace!=mLastPace){
    			mLastPace = mPace;
	    		Intent intent = new Intent(context,MusicService.class);
	    		intent.putExtra("Pace", String.valueOf(mPace));
	    		intent.setAction("PaceSetting");
	    		context.startService(intent);    		
    		}
    	}
    		
		
	}

}
