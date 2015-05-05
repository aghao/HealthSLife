package com.healthlife.util;

import com.healthlife.entity.Sports;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SitUpService extends Service {

	private SensorManager sensorManager;
	private Sensor sensor;
	private SitUpAnalyser sitUpAnalyser;
	private Sports sitUp;
	private DBinder dBinder =  new DBinder();
	
	public class DBinder extends Binder{
		
		public Sports getSitUp(){
			
			sitUp = sitUpAnalyser.getSitUp();
			return sitUp;
		}
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return dBinder;
	}
	
	public void onCreate(){
		super.onCreate();
		new Thread (new Runnable(){
			public void run(){
				Log.i("dd", "startService");
				startSitUpDetector();
				
			}
		}).start(); 
	}
	
	protected void startSitUpDetector() {
		
		sitUpAnalyser = new SitUpAnalyser();
		
		sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		// TODO Auto-generated method stub
		sensorManager.registerListener(sitUpAnalyser,sensor,SensorManager.SENSOR_DELAY_FASTEST);
		
	}

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
	}
    
    public void onDestroy() {
        super.onDestroy();
        if (sitUpAnalyser != null) {
            sensorManager.unregisterListener(sitUpAnalyser);    
        }
    }

}
