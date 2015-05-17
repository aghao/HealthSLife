package com.healthlife.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;
import com.healthlife.util.SportsAdapter;


public class ShowSportsHistoryActivity extends Activity implements OnClickListener{
	
	private ListView listView;
	private ArrayList<Sports> sportsList;
	private DBManager db;
	private Context context;
	private SportsAdapter sportsAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_sports_history);
		context =this;
		db = new DBManager(this);
		sportsList = db.getSportList();	
		
		Button btnJog = (Button)findViewById(R.id.button_show_jog_history);
		Button btnPushUp = (Button)findViewById(R.id.button_show_pushup_history);
		Button btnSitUp = (Button)findViewById(R.id.button_show_situp_history);
		Button btnTotal = (Button)findViewById(R.id.button_show_total_history);
		
		btnJog.setOnClickListener(this);
		btnPushUp.setOnClickListener(this);
		btnSitUp.setOnClickListener(this);
		btnTotal.setOnClickListener(this);
		
		listView = (ListView)findViewById(R.id.list_sports_history);
		
		sportsAdapter = new SportsAdapter(this,R.layout.list_sports,sportsList);
		listView.setAdapter(sportsAdapter);	
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Sports sports = sportsList.get(position);
				
				Intent intent = null;
				
				switch(sports.getType()){
				
				case GlobalVariables.SPORTS_TYPE_JOG:
					//sportsImage.setImageResource(jogId);
					intent = new Intent(context,com.healthlife.activity.LocationResult.class);
					intent.putExtra("type", GlobalVariables.SPORTS_TYPE_JOG);					
					intent.putExtra("showmode", GlobalVariables.MODE_SHOW_SAVED);
					intent.putExtra("jog", sports);
					break;
					
				case GlobalVariables.SPORTS_TYPE_PUSHUP:
					//sportsImage.setImageResource(pushUpId);
					intent = new Intent(context,com.healthlife.activity.ShowPushUpOrSitUpActivity.class);
					intent.putExtra("type", GlobalVariables.SPORTS_TYPE_PUSHUP);
					intent.putExtra("pushup", sports);
					intent.putExtra("showmode", GlobalVariables.MODE_SHOW_SAVED);
					break;
				case GlobalVariables.SPORTS_TYPE_SITUP:
					//sportsImage.setImageResource(sitUpId);
					
					intent = new Intent(context,com.healthlife.activity.ShowPushUpOrSitUpActivity.class);
					intent.putExtra("type", GlobalVariables.SPORTS_TYPE_SITUP);
					intent.putExtra("situp", sports);
					intent.putExtra("showmode", GlobalVariables.MODE_SHOW_SAVED);
					break;
				default:
				}				
				startActivity(intent);
			}
		});
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.button_show_jog_history:
			sportsList = db.getSportsByType(GlobalVariables.SPORTS_TYPE_JOG);
			break;
			
		case R.id.button_show_pushup_history:
			sportsList = db.getSportsByType(GlobalVariables.SPORTS_TYPE_PUSHUP);
			break;
			
		case R.id.button_show_situp_history:
			sportsList = db.getSportsByType(GlobalVariables.SPORTS_TYPE_SITUP);
			break;
			
		case R.id.button_show_total_history:
			sportsList = db.getSportList();
			break;
		default:
			break;		
		}
		
		sportsAdapter = new SportsAdapter(this,R.layout.list_sports,sportsList);
		listView.setAdapter(sportsAdapter);	
	}
}
