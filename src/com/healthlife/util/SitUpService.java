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
	private Sensor accSensor,magSensor;
	private SitUpAnalyser sitUpAnalyser;
	private Sports sitUp;
	private DBinder dBinder =  new DBinder();
	
	public class DBinder extends Binder{
		
		public Sports getSitUp(){
			
			sitUp=sitUpAnalyser.getSitUp();
			return sitUp;
		}
	}
	

	@Override
	public IBinder onBind(Intent intent) {	
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
		
		sitUpAnalyser = new SitUpAnalyser(this);
		
		sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		// TODO Auto-generated method stub
		sensorManager.registerListener(sitUpAnalyser,accSensor,SensorManager.SENSOR_DELAY_UI);	
		sensorManager.registerListener(sitUpAnalyser,magSensor,SensorManager.SENSOR_DELAY_UI);	
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
