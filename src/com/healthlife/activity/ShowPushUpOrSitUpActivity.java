package com.healthlife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.healthlife.db.DBManager;
import com.healthlife.entity.Sports;
import com.healthslife.R;


public class ShowPushUpOrSitUpActivity extends Activity implements OnClickListener{
	
	private Sports pushUp;
	private DBManager db;
	private Intent intent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_sports);
		db = new DBManager(this);
		pushUp = (Sports) getIntent().getSerializableExtra("walk");
		intent=new Intent(this,MainActivity.class);
		
		Button btnSave = (Button)findViewById(R.id.button_save_sports);
		Button btnDrop = (Button)findViewById(R.id.button_drop_soprts);
		
		btnSave.setOnClickListener(this);
		btnDrop.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.button_save_sports:
			db.insertSport(pushUp);
			startActivity(intent);
			break;
			
		case R.id.button_drop_soprts:
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("É¾³ýÈ·ÈÏ");
			alertDialog.setMessage("ÊÇ·ñÉ¾³ýÔË¶¯¼ÇÂ¼");
			alertDialog.setCancelable(false);
			alertDialog.setPositiveButton("É¾³ý", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					startActivity(intent);		
				}
			});
			alertDialog.setNegativeButton("É¾³ý", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					db.insertSport(pushUp);
					startActivity(intent);
				}
			});
			
			alertDialog.show();	
			break;	
		}
		
		
	}


	
}
