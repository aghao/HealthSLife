package com.healthlife.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;
import com.healthlife.util.SportsAdapter;


public class ShowSportsHistoryActivity extends Activity {
	
	private ListView listView;
	private ArrayList<Sports> sportsList;
	private DBManager db;
	private Context context;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_sports_history);
		context =this;
		db = new DBManager(this);
		sportsList = db.getSportList();		
		listView = (ListView)findViewById(R.id.list_sports_history);
		
		SportsAdapter sportsAdapter = new SportsAdapter(this,R.layout.list_sports,sportsList);
		listView.setAdapter(sportsAdapter);	
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Sports sports = sportsList.get(position);
				
				Intent intent = null;
				
				switch(sports.getType()){
				
				/*case GlobalVariables.SPORTS_TYPE_JOG:
					//sportsImage.setImageResource(jogId);
					intent = new Intent(context,com.healthlife.activity.LocationResultActivity.class);
					intent.putExtra("type", GlobalVariables.SPORTS_TYPE_JOG);					
					intent.putExtra("showmode", GlobalVariables.MODE_SHOW_SAVED);
					intent.putExtra("jog", sports);
					break;
					*/
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
				case GlobalVariables.SPORTS_TYPE_WALK:
					//sportsImage.setImageResource(walkId);

					intent = new Intent(context,com.healthlife.activity.ShowWalkActivity.class);
					intent.putExtra("type", GlobalVariables.SPORTS_TYPE_WALK);
					intent.putExtra("walk", sports);
					intent.putExtra("showmode", GlobalVariables.MODE_SHOW_SAVED);
					break;
				default:
				}				
				startActivity(intent);
			}
		});
	}
}
