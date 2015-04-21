package com.healthlife.util;
 
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
 
public class StepService extends Service {
    public static Boolean flag = false;
    private SensorManager sensorManager;
    private StepDetector stepDetector;
 
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() {
        super.onCreate();
        //���￪����һ���̣߳���Ϊ��̨����Ҳ�������߳��н��У��������԰�ȫ�㣬��ֹ���߳�����
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();
 
    }
 
    private void startStepDetector() {
        flag = true;
        stepDetector = new StepDetector(this);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);//��ȡ��������������ʵ��
        Sensor sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//��ô����������ͣ������õ������Ǽ��ٶȴ�����
        //�˷�������ע�ᣬֻ��ע����Ż���Ч��������SensorEventListener��ʵ����Sensor��ʵ������������
        sensorManager.registerListener(stepDetector, sensor,
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
        if (stepDetector != null) {
            sensorManager.unregisterListener(stepDetector);
        }
    }
}