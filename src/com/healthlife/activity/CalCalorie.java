package com.healthlife.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.healthlife.R;
import com.healthlife.R.id;
import com.healthlife.R.layout;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Calorie;
import com.healthlife.entity.Food;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.util.FoodsAdapter;

@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class CalCalorie extends Activity{

	private ListView listView;
	private ArrayList<Food> foodList;
	private DBManager db;
	private FoodsAdapter foodsAdapter;
	private Food food;
	private EditText et_accoumt;
	private Calorie calorie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cal_calorie);
		calorie = new Calorie();
		
		db = new DBManager(this);
		foodList=db.queryFoods("");
		
		listView =(ListView)findViewById(R.id.list_foods);
		foodsAdapter = new FoodsAdapter(this,R.layout.list_foods,foodList);
		listView.setAdapter(foodsAdapter);	
		
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				food = foodList.get(position);	
				showDialog(food);
			}
		});		
	}
	
	private void showDialog(Food food){
		if(food.getType()!=GlobalVariables.DRINKS){
			Builder dialog = new AlertDialog.Builder(this);
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_food_account,null);
			
			dialog.setView(layout);
			et_accoumt=(EditText)layout.findViewById(R.id.et_food_account);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			    	insertCalorie();
			    }
			   });
			  
			   dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			    }
			   
			   });
			   dialog.show();
			
		}
		
	}
	
	private void insertCalorie(){
		calorie.setCalorie(Integer.valueOf(et_accoumt.getText().toString())*food.getCalorie());     
		SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
		calorie.setDate(simpleFormatter.format( new java.util.Date()));
	 
		db.addCalorie(calorie);
	}
}
