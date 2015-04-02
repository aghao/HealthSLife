package com.healthlife.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorController {

	private SensorManager sensorManager;
	private Sensor linearAcc,rotationVct,gyroscope;
	
	public void initSensors(int type,Context context) {
		
	
		getSensorManager(context);	
		
		//get specific sensors counting on the sportsmode
		switch(type){
		
			case 0 : 	linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); break;
			
			case 1 :    rotationVct = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR); break;
			
			case 2 :	linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); 
						rotationVct = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR); 
						break;
			
			case 3 :	linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); break;
			
			default: System.out.println("unknown mode"); 
		}

	}

	public void record ()
	{
		
	}
	
	private void getSensorManager(Context ctx){
		sensorManager = (SensorManager)ctx.getSystemService(Context .SENSOR_SERVICE);
	}
	
	
}
