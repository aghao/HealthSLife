package com.healthlife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Record;
import com.healthlife.entity.Sports;


public class ShowWalkActivity extends Activity implements OnClickListener {

	private Sports walk;
	private DBManager db;
	private Intent intentToNextActivity;
	private TextView textStep,textTotalStep,textDuration,textDate;
	private int showMode;
	private Record record;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_walk);
		walk=(Sports) getIntent().getSerializableExtra("walk");
		showMode = getIntent().getIntExtra("showmode", 0);
		db = new DBManager(this); 
		record = db.queryRecord();
		
		Button btnSave = (Button)findViewById(R.id.button_save_sports);
		Button btnDrop = (Button)findViewById(R.id.button_drop_sports);
		
		btnSave.setOnClickListener(this);
		btnDrop.setOnClickListener(this);
		
		if(GlobalVariables.MODE_SHOW_SAVED==showMode)
			btnSave.setVisibility(View.GONE);
		
		textStep=(TextView)findViewById(R.id.text_steps_num);
		textTotalStep=(TextView)findViewById(R.id.text_total_step);
		textDate=(TextView)findViewById(R.id.text_steps_date);
		textDuration=(TextView)findViewById(R.id.text_steps_duration);
		
		walk.getNum();
		textStep.setText("a");
		textStep.setText("Steps: "+String.valueOf(walk.getNum()));
		textTotalStep.setText("TotalSteps: "+String.valueOf(db.queryRecord().getSteps()));
		textDate.setText(String.valueOf("Date: "+walk.getDate()));
		textDuration.setText("Duration: "+String.valueOf(walk.getDuration()));
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		intentToNextActivity=new Intent(this,MainActivity.class);
		switch(v.getId()){
		
		case R.id.button_save_sports:
			if(showMode==GlobalVariables.MODE_SHOW_UNSAVED){
				db.insertSport(walk);
				record.setSteps(record.getSteps()+walk.getNum());
				db.updateRecord(record);
			}
			startActivity(intentToNextActivity);
			break;
			
		case R.id.button_drop_sports:
			
			showDeleteWarning();
			break;	
		}
		
		
	}
	private void showDeleteWarning(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("删除确认");
		alertDialog.setMessage("是否删除运动记录");
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(GlobalVariables.MODE_SHOW_SAVED==showMode)
					db.removeSport(walk.getSportsID());
				startActivity(intentToNextActivity);		
			}
		});
		alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(GlobalVariables.MODE_SHOW_UNSAVED==showMode){
					db.insertSport(walk);
					record.setSteps(record.getSteps()+walk.getNum());
					db.updateRecord(record);
				}
				startActivity(intentToNextActivity);
			}
		});		
		alertDialog.show();	
	}
}
