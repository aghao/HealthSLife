package com.healthlife.util;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class PushUpService extends Service {
	
	
	private PushUpDetector pushUpDetector;
	private SensorManager sensorManager;
	private Sensor sensor;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		new Thread (new Runnable(){
			public void run(){
				startPushUpDetector();
			}
		}).start();
	}
	
	@SuppressWarnings("static-access")
	public void startPushUpDetector(){
		
		pushUpDetector = new PushUpDetector();
		
		sensorManager = (SensorManager)this.getSystemService(Service.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sensorManager.registerListener(pushUpDetector,sensor,sensorManager.SENSOR_DELAY_FASTEST);

	}
	
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
	
	public void onDestroy() {
        super.onDestroy();
        if (pushUpDetector != null) {
            sensorManager.unregisterListener(pushUpDetector);    
        }
 
    }

}
