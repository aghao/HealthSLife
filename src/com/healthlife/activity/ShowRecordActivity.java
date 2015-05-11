package com.healthlife.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Record;


public class ShowRecordActivity extends Activity {
	
	private DBManager db;
	private Record record;
	private Button btnClear;
	private TextView textDistance,textSpeed,textPushUps,textPerfectPushUps,textValidPushUps,textSitUps,textValidSitUps,textPerfectSitUps,textSteps,textTotalDistance,textTotalPushUps,textTotalSitUps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_records);
		
		btnClear = (Button)findViewById(R.id.button_record_clear);
		
		
		textDistance=(TextView)findViewById(R.id.text_record_distance);
		textSpeed=(TextView)findViewById(R.id.text_record_avgspeed);	
		textPushUps=(TextView)findViewById(R.id.text_record_pushups);
		textPerfectPushUps=(TextView)findViewById(R.id.text_record_pushups_perfect);	
		textValidPushUps=(TextView)findViewById(R.id.text_record_pushups_valid);		
		textSitUps=(TextView)findViewById(R.id.text_record_situps);
		textValidSitUps	=(TextView)findViewById(R.id.text_record_situps_valid	);
		textPerfectSitUps=(TextView)findViewById(R.id.text_record_situps_perfect);
		textSteps=(TextView)findViewById(R.id.text_record_steps);
		textTotalDistance=(TextView)findViewById(R.id.text_record_totaldistance);
		textTotalPushUps=(TextView)findViewById(R.id.text_record_totalpushups);
		textTotalSitUps=(TextView)findViewById(R.id.text_record_totalsitups);
		
		db = new DBManager(this);
		record = db.queryRecord();
		
		textDistance.setText("最长距离: "+String.valueOf(""+record.getDistance())+" km");
		textSpeed.setText("平均速度: "+String.valueOf(""+record.getAVGSpeed())+" km/h");
		textPushUps.setText("日均俯卧撑次: "+String.valueOf(""+record.getNumPushUp())+" 次");
		textPerfectPushUps.setText("俯卧撑动作优秀率: "+String.valueOf(record.getPerfectNumPushUp())+" 次");
		textValidPushUps.setText("俯卧撑动作合格率: "+String.valueOf(record.getValidNumPushUp())+" 次");
		textSitUps.setText("日均仰卧起坐: "+String.valueOf(record.getNumSitUp())+" 次");
		textValidSitUps.setText("仰卧起坐优秀率: "+String.valueOf(record.getValidNumSitUp())+" 次");
		textPerfectSitUps.setText("仰卧起坐合格率: "+String.valueOf(record.getPerfectNumSitUp())+" 次");
		textSteps.setText("总步数: "+String.valueOf(record.getSteps())+" 次");
		textTotalDistance.setText("慢跑总距离: "+String.valueOf(record.getTotalDistance())+" km");
		textTotalPushUps.setText("俯卧撑总数: "+String.valueOf(record.getTotalNumPushUp())+" 次");
		textTotalSitUps.setText("仰卧起坐总数: "+String.valueOf(record.getTotalNumSitUp())+" 次");
			
		btnClear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				db.clearRecord(record.getRecordId());
			}		
		});
		Log.i("dd", "aaa");
	}
}
