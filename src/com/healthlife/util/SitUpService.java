package com.healthlife.util;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

public class SitUpService extends Service {

	private SensorManager sensorManager;
	private Sensor sensor;
	private SitUpDetector sitUpDetector;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate(){
		super.onCreate();
		new Thread (new Runnable(){
			public void run(){

				startSitUpDetector();
				
			}
		}).start(); 
	}
	
	protected void startSitUpDetector() {
		
		sitUpDetector = new SitUpDetector();
		
		sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// TODO Auto-generated method stub
		sensorManager.registerListener(sitUpDetector,sensor,sensorManager.SENSOR_DELAY_FASTEST);
		
	}

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
	}
    
    public void onDestroy() {
        super.onDestroy();
        if (sitUpDetector != null) {
            sensorManager.unregisterListener(sitUpDetector);    
        }
    }

}
