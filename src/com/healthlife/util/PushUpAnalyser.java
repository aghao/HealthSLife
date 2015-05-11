package com.healthlife.util;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.healthlife.activity.MusicService;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;

public class PushUpAnalyser implements SensorEventListener {
	
	private int mNum;
	private int mValidNum;
	private int mGoodNum;
	private int mPerfectNum;
	private float mXOrientation;
	private float mLastXOrientation;
	
	private int mDirection;
	private int mLastDirection;
	
	private boolean mTopValidFlag;
	private boolean mTopGoodFlag;
	private boolean mTopPerfectFlag;
	private boolean mValidFlag;
	private boolean mGoodFlag;
	private boolean mPerfectFLag;
	private boolean mInTop;
	
	private long mStartTime;
	private long mEndTime;
	private boolean mStartFlag;
	private long mCurrentTime;
	
	private int mPace;
	private long mPaceStartTime;
	private int mPaceSamples;
		
	private String mDate;
	private String mDuration;

	private Sports pushUp;
	private Context context;	
	
	private static final float VALID_POINT=30;
	private static final float GOOD_POINT=20;
	private static final float PERFECT_POINT=10;
	
	private static final float TOP_VALID_POINT=60;
	private static final float TOP_GOOD_POINT=70;
	private static final float TOP_PERFECT_POINT=80;
	
	private static final float SENSITIVITY = 9.5f;
					
	public PushUpAnalyser(Context context){
		this.context=context;
		mStartFlag = true;
		mPaceStartTime = System.currentTimeMillis();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int arg1) {
		// TODO Auto-generated method stub	
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this){
			Sensor sensor= event.sensor;
			
			if(Sensor.TYPE_ORIENTATION==sensor.getType()){
				mXOrientation = event.values[1];
				//Log.i("dd", "Orientation: "+mXOrientation);
				
				checkKeyPoints();
				judgeMotion();
				//Log.i("dd","----------------------------------------------------------------");
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private void operateTime(){

		mCurrentTime = System.currentTimeMillis();
		
		//若为第一次读数则记录开始时间
		if(mStartFlag){
			SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
			mDate = simpleFormatter.format( new java.util.Date());
			mStartTime = mCurrentTime;
			mStartFlag = false;
		}
		mEndTime = mCurrentTime;
	}
	//用于判断是否达到关键点
	private void checkKeyPoints(){
		if(mXOrientation>=TOP_VALID_POINT){
			mInTop=true;
			if(mXOrientation>=TOP_VALID_POINT)
				mTopValidFlag=true;
			if(mXOrientation>=TOP_GOOD_POINT)
				mTopGoodFlag=true;
			if(mXOrientation>=TOP_PERFECT_POINT)
				mTopPerfectFlag=true;
		}
		
		if(mXOrientation<=TOP_VALID_POINT){
			mInTop=false;
			if(mXOrientation<=VALID_POINT&&mTopValidFlag)
				mValidFlag = true;
			if(mXOrientation<=GOOD_POINT&&mTopGoodFlag)
				mGoodFlag = true;
			if(mXOrientation<=PERFECT_POINT&&mTopPerfectFlag)
				mPerfectFLag = true;			
		}
		//Log.i("dd", "mTopValidFlag:  "+mTopValidFlag+"   mTopGoodFlag:"+mTopGoodFlag+"   mTopPerfectFlag:"+ mTopPerfectFlag);
		//Log.i("dd", "mValidFlag:  "+mValidFlag+"   mGoodFlag:"+mGoodFlag+"   mPerfectFLag:"+ mPerfectFLag);
	}

	//用于判断该次动作是否结束	
	private void judgeMotion(){
		//Log.i("dd", "mLastXOrientation-mXOrientation: "+(mLastXOrientation-mXOrientation));
		if(mLastXOrientation-mXOrientation>=(10-SENSITIVITY))
			mDirection = -1;
		else if(mXOrientation-mLastXOrientation>=(10-SENSITIVITY))
			mDirection = 1;
		//Log.i("dd", "direction: "+mDirection+"     mLastDirection:  "+mLastDirection);
		if(mLastDirection==-1&&mDirection==-1&&mValidFlag&&mInTop){
				mNum +=1;
				paceAnalyse();
				//Log.i("dd", "get motion");
			if(mPerfectFLag)
				mPerfectNum+=1;
			else if (mGoodFlag)
				mGoodNum+=1;
			else if (mValidFlag)
				mValidNum+=1;
				//Log.i("dd", "mValidNum: "+mValidNum+"   mGoodNum: "+mGoodNum+"   mPerfectNum: "+mPerfectNum);
			
			mTopValidFlag=(mXOrientation>=TOP_VALID_POINT?true:false);
			mTopGoodFlag=(mXOrientation>=TOP_GOOD_POINT?true:false);
			mTopPerfectFlag=(mXOrientation>=TOP_PERFECT_POINT?true:false);		
		
			mValidFlag = false;
			mGoodFlag = false;
			mPerfectFLag = false;
			
			sendBroadcast();
		}
		mLastXOrientation = mXOrientation;
		mLastDirection=mDirection;
		operateTime();
	}
	//获得持续时间
	@SuppressLint("SimpleDateFormat")
	public String getDuration(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		mDuration = formatter.format(mEndTime-mStartTime+57600000);
		return mDuration;
	}
	//获取分析结果
	public Sports getPushUp(){
		pushUp = new Sports();
		
		pushUp.setType(GlobalVariables.SPORTS_TYPE_PUSHUP);
		pushUp.setNum(mNum);
		pushUp.setValidNum(mValidNum);
		pushUp.setPerfectNum(mPerfectNum);
		pushUp.setGoodNum(mGoodNum);
		pushUp.setDuration(getDuration());
		pushUp.setDate(mDate);
		
		return pushUp;
	}
	//向UI发送变更广播
	private void sendBroadcast(){
		Intent intent = new Intent("com.healthlife.activity.PushUpActivity.MotionAdd");
		intent.putExtra("motionNum", mNum);
		context.sendBroadcast(intent);
		//Log.i("ss", "mValidNum: "+mValidNum+"   mGoodNum: "+mGoodNum+"   mPerfectNum: "+mPerfectNum);
	}
	
	private void paceAnalyse() {
		// TODO Auto-generated method stub
    	if(mCurrentTime-mPaceStartTime<60000)
    		mPaceSamples+=1;
    	
    	else if(mCurrentTime-mPaceStartTime>=60000){
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
    		Intent intent = new Intent(context,MusicService.class);
    		intent.putExtra("Pace", String.valueOf(mPace));
    		intent.setAction("PaceSetting");
    		context.startService(intent);
    	}
	}
	
	
}






	/*private final long TIMEGAPGATE = 100;
	
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
	private Intent intent;

	private Sports pushUp;
	private Context context;
	
	@SuppressLint("SimpleDateFormat")
	public PushUpAnalyser(Context context){
		mValidMotionAmount = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
		mDate = formatter.format(new java.util.Date());
		this.context=context;
		intent = new Intent("com.healthlife.activity.PushUpActivity.MotionAdd");
	}
	@SuppressLint("SimpleDateFormat")
	public void onSensorChanged(SensorEvent event) {
				
		synchronized (this){

			Sensor sensor = event.sensor;
			if(sensor.getType()== Sensor.TYPE_PROXIMITY);
			{	
				Log.i("dd","proximityX:" + String.valueOf(event.values[0]));
				if(mStartTime==0) 
					mStartTime=System.currentTimeMillis();	     
				
				mTimeGapFlag=isTimeGapEnough();
				mProximity = event.values[0];
				
				if(mProximity<3&&mTimeGapFlag)
				{
					mValidMotionAmount+=1;
					mValidFlag=true;
					mEndTime=System.currentTimeMillis();
					
					intent.putExtra("motionNum", mValidMotionAmount);
					context.sendBroadcast(intent);

				}
				else if(mProximity>=3&&isValid())
				{
					mPerfectMotionAmount+=1;
					mEndTime=System.currentTimeMillis();
				}
				

			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	private boolean isTimeGapEnough(){
		
		mCurrentTime=System.currentTimeMillis();
		mTimeGap=mCurrentTime-mLastTime;
		mLastTime=mCurrentTime;
		
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

	@SuppressLint("SimpleDateFormat")
	public String getDuration(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		mDuration = formatter.format(mEndTime-mStartTime);
		return mDuration;
	}
	
	public Sports getPushUp(){
		pushUp = new Sports();
		
		pushUp.setType(GlobalVariables.SPORTS_TYPE_PUSHUP);
		pushUp.setNum(mValidMotionAmount);
		//pushUp.setValidRate(getValidRate());
		//pushUp.setPerfectRate(getPerfectRate());
		pushUp.setDuration(getDuration());
		pushUp.setDate(mDate);
		
		return pushUp;
	}

}*/
