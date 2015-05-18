package com.healthlife.activity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Record;
import com.healthlife.entity.Sports;


@SuppressLint("SimpleDateFormat")
public class ShowPushUpOrSitUpActivity extends Activity implements OnClickListener{
	
	private Sports sports;
	private DBManager db;
	private Intent intentToNextActivity;
	private TextView textNum,textPerfectNum,textDate,textDuration;
	private int showMode,sportsType;
	private RadarChart radarChart; 
	private Record record;
	private float duration;
	private float num;
	private float quality;
	private float calorie;
	private int speed;
	private float durationGrade;
	private float numGrade;
	private float qualityGrade;
	private float calorieGrade;
	private float speedGrade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_sports);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		db = new DBManager(this);
		sportsType = getIntent().getIntExtra("type", 0);
		showMode = getIntent().getIntExtra("showmode",0);
		
		if(sportsType==GlobalVariables.SPORTS_TYPE_PUSHUP)
			sports = (Sports) getIntent().getSerializableExtra("pushup");
		else if(sportsType==GlobalVariables.SPORTS_TYPE_SITUP)
			sports = (Sports) getIntent().getSerializableExtra("situp");
		else 
			Log.e("healthSlife", "sportsType lost");
		
		intentToNextActivity=new Intent(this,MainActivity.class);
		
		SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		num=sports.getNum();

		try {
			long mills =simpleFormatter.parse("1970-1-2"+" "+sports.getDuration()).getTime();
			float timeOffSet=mills-57600000;
			duration= (timeOffSet)/60000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		speed=(int)(sports.getNum()/duration);
		quality=(float)(sports.getPerfectNum()*1+sports.getValidNum()*0.5+sports.getGoodNum()*0.8)/sports.getNum();
		calorie = sports.getCalorie();

		Button btnSave = (Button)findViewById(R.id.button_save_sports);
		btnSave.setOnClickListener(this);
			
		Button btnDrop = (Button)findViewById(R.id.button_drop_sports);
		btnDrop.setOnClickListener(this);
		
		if(GlobalVariables.MODE_SHOW_SAVED==showMode)
		{
			LinearLayout layout = (LinearLayout)findViewById(R.id.btnlayout);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setGravity(Gravity.CENTER_HORIZONTAL);
			btnSave.setVisibility(View.GONE);
			
			btnDrop.setBackgroundResource(R.drawable.pushresult_deletebtn_selector);
		}
		
		textNum = (TextView)findViewById(R.id.text_motion_num);
		textPerfectNum = (TextView)findViewById(R.id.text_perfect_motion);
		textDate = (TextView)findViewById(R.id.text_sports_date);
		textDuration = (TextView)findViewById(R.id.text_sports_duration);
		
		textNum.setText("动作数: "+String.valueOf(sports.getNum()));
		textPerfectNum.setText("消耗卡路里: "+String.valueOf(sports.getCalorie()));
		textDate.setText("日期: "+String.valueOf(sports.getDate()));
		textDuration.setText("持续时间: "+String.valueOf(sports.getDuration()));
		
        radarChart = (RadarChart) findViewById(R.id.radar_chart); 
        radarChart.getYAxis().setAxisLineColor(Color.BLACK);
        radarChart.getYAxis().setAxisLineWidth(5f);
        radarChart.animateY(2000);
        radarChart.getYAxis().setGridColor(Color.RED);
        radarChart.getYAxis().setAxisMaxValue(100);
        
        RadarData radarData = getRadarData(4, sports.getNum());  
        showChart(radarChart, radarData);		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){		
		case R.id.button_save_sports:
			if(showMode==GlobalVariables.MODE_SHOW_UNSAVED){
				db.insertSport(sports);
				updateRecord();
			}
			startActivity(intentToNextActivity);
			finish();
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
					db.removeSport(sports.getSportsID());
				startActivity(intentToNextActivity);	
				finish();
			}
		});
		alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(GlobalVariables.MODE_SHOW_UNSAVED==showMode){
					db.insertSport(sports);
					updateRecord();
				}
				startActivity(intentToNextActivity);
				finish();
			}
		});		
		alertDialog.show();	
	}
	
	private void showChart(RadarChart radarChart, RadarData radarData) {    
    
        radarChart.setDescription("");     
      
        radarChart.setRotationAngle(90); // 初始旋转角度    
        radarChart.setRotationEnabled(true); // 可以手动旋转         
        //设置数据    
        radarChart.setData(radarData);     
        Legend mLegend = radarChart.getLegend();  //设置比例图    
        mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);  //最右边显示    
        mLegend.setForm(LegendForm.CIRCLE);  //设置比例图的形状，默认是方形    
        mLegend.setXEntrySpace(7f);    
        mLegend.setYEntrySpace(5f);               
        radarChart.animateXY(20000, 20000);  //设置动画   
        //radarChart.invalidate(); 
    }    
     
    private RadarData getRadarData(int count, float range) {    
            
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容     

		float cal = new BigDecimal(calorie).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
		
		xValues.add("时间:"+sports.getDuration());
		xValues.add("卡路里:"+cal+"KCAL");
		
		float spd = new BigDecimal(speed).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
		xValues.add("速度:"+spd+"个/分钟");
		xValues.add("数量:"+(int)num+"个");
		
		xValues.add("质量:"+(int)(quality*100)+"%");
   
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据    
        
        calGrades();
        yValues.add(new Entry(durationGrade, 0));    
        yValues.add(new Entry(calorieGrade, 1));    
        yValues.add(new Entry(speedGrade, 2));    
        yValues.add(new Entry(numGrade, 3)); 
        yValues.add(new Entry(qualityGrade, 4)); 
    
        //y轴的集合    
        RadarDataSet radarDataSet = new RadarDataSet(yValues, "运动分析图");        
        ArrayList<Integer> colors = new ArrayList<Integer>();    
    
        // 饼图颜色    
             
        colors.add(Color.RED); 
    
        radarDataSet.setColors(colors);     
        radarDataSet.setDrawValues(false);
        radarDataSet.setDrawFilled(true);
        radarDataSet.setFillColor(Color.RED);
        RadarData radarData = new RadarData(xValues, radarDataSet); 
        return radarData;    
    }

    private void updateRecord(){
    	record = db.queryRecord();
    	
    	if(record.getRecordId()!=0){
	    	if(GlobalVariables.SPORTS_TYPE_PUSHUP==sports.getType()){
	    		record.setCalOfPushUp(record.getCalOfPushUp()+sports.getCalorie());
	    		record.setTotalCal(record.getTotalCal()+sports.getCalorie());
	    		
	    		record.setDurationPushUp(record.getDurationPushUp()+getDurationInFloat(sports.getDuration()));
	    		record.setTotalDuration(record.getTotalDuration()+getDurationInFloat(sports.getDuration()));
	    		
	    		record.setTotalNumPushUp(record.getTotalNumPushUp()+sports.getNum()); 
	    	}
	    	else if(GlobalVariables.SPORTS_TYPE_SITUP==sports.getType()){
	    		record.setCalOfSitUp(record.getCalOfSitUp()+sports.getCalorie());
	    		record.setTotalCal(record.getTotalCal()+sports.getCalorie());
	    		
	    		record.setDurationSitUp(record.getDurationSitUp()+getDurationInFloat(sports.getDuration()));
	    		record.setTotalDuration(record.getTotalDuration()+getDurationInFloat(sports.getDuration()));
	    		
	    		record.setTotalNumSitUp(record.getTotalNumSitUp()+sports.getNum());    	
	    	}
	    	db.updateRecord(record);
    	}
    	else{
    		if(GlobalVariables.SPORTS_TYPE_PUSHUP==sports.getType()){
	    		record.setCalOfPushUp(sports.getCalorie());
	    		record.setTotalCal(sports.getCalorie());
	    		
	    		record.setDurationPushUp(getDurationInFloat(sports.getDuration()));
	    		record.setTotalDuration(getDurationInFloat(sports.getDuration()));
	    		
	    		record.setTotalNumPushUp(sports.getNum()); 
	    	}
	    	else if(GlobalVariables.SPORTS_TYPE_SITUP==sports.getType()){
	    		record.setCalOfSitUp(sports.getCalorie());
	    		record.setTotalCal(sports.getCalorie());
	    		
	    		record.setDurationSitUp(getDurationInFloat(sports.getDuration()));
	    		record.setTotalDuration(getDurationInFloat(sports.getDuration()));
	    		
	    		record.setTotalNumSitUp(sports.getNum());      		
	    	}
    		db.insertRecord(record);
    	}		
    }
    
    private float getDurationInFloat(String duration){

    	float millis=-1;
    	duration="1970-1-1"+" "+duration; 
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			millis = formatter.parse(duration).getTime()-formatter.parse("1970-1-1 00:00:00").getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
		return millis;
    }
	
	private void calGrades(){
		if(duration<1){
			durationGrade = (int)(duration*50);
		}
		else if(duration<=5){
			durationGrade =(int)((duration-1)*5+60);	
		}
		else if(duration<=10){
			durationGrade = (int)((duration-5)*2+70);
		}
		else if(duration<=15){
			durationGrade = (int)((duration-10)*2+80);
		}
		else if(duration<=20){
			durationGrade = (int)((duration-10)*2+90);	
		}
		else if(duration>20){
			durationGrade = 100;	
		}
		
		if(speed<=20){
			speedGrade=speed*2;
		}
		else if(speed>20&&speed<=80){
			speedGrade=(speed-20)+40;
		}
		else if(speed>80){
			speedGrade=100;
		}
		
		qualityGrade = quality*100;
		
		//if(sports.getType()==GlobalVariables.SPORTS_TYPE_PUSHUP){
			if(num<10){
				numGrade = num*4;
			}
			else if(num<=20){
				numGrade = (int)((num-10)*1.5+40);
			}
			else if(num<=40){
				numGrade = (int)((num-20)*0.75+55);
			}
			else if (num<=60){
				numGrade = (int)((num-40)*0.75+70);
			}
			else if (num<=80){
				numGrade = (int)((num-60)*0.75+85);
			}
			else if (num>80){
				numGrade = 100;
			}
		
		
/*		if(sports.getType()==GlobalVariables.SPORTS_TYPE_SITUP){
			if(num<10){
				numGrade = num*40;
			}
			else if(num<=30){
				numGrade = (int)((num-10)*0.75+40);
			}
			else if(num<=50){
				numGrade = (int)((num-20)*0.75+55);
			}
			else if (num<=70){
				numGrade = (int)((num-40)*0.75+70);
			}
			else if (num<=90){
				numGrade = (int)((num-60)*0.75+85);
			}
			else if (num>120){
				numGrade = 100;
			}
		}*/			
			calorieGrade=numGrade*quality;
	}

}
