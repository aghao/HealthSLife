package com.healthlife.util;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.healthlife.entity.Sports;

public class PushUpService extends Service {
	
	private DBinder dBinder = new DBinder();
	private PushUpAnalyser pushUpAnalyser;
	private SensorManager sensorManager;
	private Sensor sensor;
	private Sports pushUp;

public class DBinder extends Binder{
		
		public Sports getPushUp(){
			
			pushUp = pushUpAnalyser.getPushUp();
			return pushUp;
		}
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return dBinder;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		new Thread (new Runnable(){
			public void run(){
				analysePushUp();
			}
		}).start();
	}
	
	@SuppressWarnings("static-access")
	public void analysePushUp(){
		
		pushUpAnalyser = new PushUpAnalyser(this);
		
		sensorManager = (SensorManager)this.getSystemService(Service.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(pushUpAnalyser,sensor,sensorManager.SENSOR_DELAY_UI);
		Log.i("dd", "aaaaaaaa");
	}
	
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
	
	public void onDestroy() {
        super.onDestroy();
        if (pushUpAnalyser != null) {
            sensorManager.unregisterListener(pushUpAnalyser);    
        }
 
    }

}
