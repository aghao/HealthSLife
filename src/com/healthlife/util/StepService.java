package com.healthlife.util;
 
import com.healthlife.entity.Sports;
import com.healthlife.util.PushUpService.DBinder;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
 
public class StepService extends Service {
	
	private DBinder dBinder = new DBinder();
    public static Boolean flag = false;
    private SensorManager sensorManager;
    private StepAnalyser stepAnalyser;
    
    private Sports walk;
 
    @Override
    public void onCreate() {
        super.onCreate();
        //���￪����һ���̣߳���Ϊ��̨����Ҳ�������߳��н��У��������԰�ȫ�㣬��ֹ���߳�����
        new Thread(new Runnable() {
            public void run() {
                analyseWalk();
            }
        }).start();
 
    }
 
    private void analyseWalk() {
        flag = true;
        stepAnalyser = new StepAnalyser(this);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);//��ȡ��������������ʵ��
        Sensor sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//��ô����������ͣ������õ������Ǽ��ٶȴ�����
        //�˷�������ע�ᣬֻ��ע����Ż���Ч��������SensorEventListener��ʵ����Sensor��ʵ������������
        sensorManager.registerListener(stepAnalyser, sensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        if (stepAnalyser != null) {
            sensorManager.unregisterListener(stepAnalyser);
        }
    }

	public class DBinder extends Binder{
		
		public Sports getWalk(){
			
			walk=stepAnalyser.getWalk();
			return walk;
		}
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return dBinder;
	}
    
}