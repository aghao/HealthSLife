package com.healthlife.util;

import java.text.SimpleDateFormat;

import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
 
/**
 * ����һ��ʵ�����źż����ļǲ�����
 * ���Ǵӹȸ�������һ���ǲ����㷨������̫��
 * @author Liyachao Date:2015-1-6
 *
 */
public class StepAnalyser implements SensorEventListener {
 
    private int mStep = 0;
    private float SENSITIVITY = 10; // SENSITIVITY������
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;
    private static long end = 0;
    private static long start = 0;
    private long mStartTime;
    private long mEndTime;
    private String mDate;
    private String mDuration;
    
    private Sports walk;
    /**
     * �����ٶȷ���
     */
    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;
 
    /**
     * ���������ĵĹ��캯��
     * 
     * @param context
     */
    @SuppressLint("SimpleDateFormat")
	public StepAnalyser(Context context) {
        super();
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
        mDate=formatter.format(new java.util.Date());
    }
 
    //����������⵽����ֵ�����仯ʱ�ͻ�����������
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            	
            	if(mStartTime==0) 
					mStartTime=System.currentTimeMillis();	     
				
                float vSum = 0;
                for (int i = 0; i < 3; i++) {
                    final float v = mYOffset + event.values[i] * mScale[1];//���㴹ֱ�ٶ�
                    vSum += v;
                }
                int k = 0;
                float v = vSum / 3;
 
                float direction = (v > mLastValues[k] ? 1
                        : (v < mLastValues[k] ? -1 : 0));//����ǰ�ٶȱ��ϴ��ٶȴ�����Ϊ��������Ϊ����0
                if (direction == -mLastDirections[k]) {
                    // Direction changed
                    int extType = (direction > 0 ? 0 : 1); // minumum or
                                                            // maximum?
                    mLastExtremes[extType][k] = mLastValues[k];
                    float diff = Math.abs(mLastExtremes[extType][k]
                            - mLastExtremes[1 - extType][k]);
 
                    if (diff > SENSITIVITY) {
                        boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                        boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                        boolean isNotContra = (mLastMatch != 1 - extType);
 
                        if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
                                && isNotContra) {
                            end = System.currentTimeMillis();
                            if (end - start > 500) {// ��ʱ�ж�Ϊ����һ��
 
                                mStep++;
                                mLastMatch = extType;
                                start = end;
                                mEndTime = System.currentTimeMillis();
                            }
                        } else {
                            mLastMatch = -1;
                        }
                    }
                    mLastDiff[k] = diff;
                }
                mLastDirections[k] = direction;
                mLastValues[k] = v;
            }
 
        }
    }
    //���������ľ��ȷ����仯ʱ�ͻ�������������������û����
    public void onAccuracyChanged(Sensor arg0, int arg1) {
 
    }
    
    @SuppressLint("SimpleDateFormat")
	public Sports getWalk(){
    	
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		mDuration = formatter.format(mEndTime-mStartTime);
		
    	walk.setType(GlobalVariables.SPORTS_TYPE_WALK);
    	walk.setNum(mStep);
    	walk.setDate(mDate);
    	walk.setDuration(mDuration);
    	return walk;
    }
 
}
