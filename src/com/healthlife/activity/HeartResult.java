package com.healthlife.activity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Beats;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HeartResult extends Activity {
	
	private Beats newBeats = new Beats(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartresult_main);
		//��intent��ȡ����ֵ
		Intent get =getIntent();
		newBeats.setBeats(get.getIntExtra("heartrate", -1));
		TextView resultText = (TextView) findViewById(R.id.showresult);
		Button saveRecord = (Button) findViewById(R.id.savebt);
		Button cancle = (Button) findViewById(R.id.canclebt);
		final RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.hrradiogp);
		//��ȡϵͳʱ��
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm",Locale.getDefault());       
		String date = sDateFormat.format(new java.util.Date());  
		newBeats.setDate(date);
		resultText.setText(date+"���ʽ��"+newBeats.getBeats());
		//���û�Ϊ-1
		newBeats.setUserId(1);
		
		//������������
		newBeats.setType(1);
		
		//��ѡ��
		mRadioGroup.check(R.id.statichr);
		//ע���¼�
		mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			//����¼���ȡ��ѡ�����
				if(checkedId == R.id.statichr)
				{
					newBeats.setType(1);
				};
				if(checkedId == R.id.sporthr)
				{
					newBeats.setType(2);
				};
				if(checkedId == R.id.maxhr)
				{
					newBeats.setType(3);
				};
							
				}
		});
		saveRecord.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DBManager myDB = new DBManager(HeartResult.this);
				long ID = myDB.insertBeats(newBeats);
				Log.v("id",""+ID);
				if(ID == -1)
				{
					//myDB.insertFakeUser();
					ID= myDB.insertBeats(newBeats);
					Log.v("id",""+ID);
				}
				Intent intent = new Intent();
				intent.setClass(HeartResult.this, HeartHistory.class);
				startActivity(intent);
				finish();
			}
		});
		
		cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.heart_result, menu);
		return true;
	}

}
