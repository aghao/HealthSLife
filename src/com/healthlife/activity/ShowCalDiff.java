package com.healthlife.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.healthlife.R;
import com.healthlife.R.id;
import com.healthlife.R.layout;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Calorie;
import com.healthlife.entity.GlobalVariables;
import com.healthlife.entity.Sports;

@SuppressLint("SimpleDateFormat")
public class ShowCalDiff extends Activity implements OnClickListener{

	private String day1;
	private String day2;
	private String day3;
	private String day4;
	private String day5;
	private String day6;
	private String day7;
	private DBManager db;
	
	private LineChart lineChart;
	private ArrayList<Calorie> calList,calDiffList,calOffList;
	private TextView txtCalAbs,txtCalOff,txtCalDiff,txtRec;
	private Button btnPushUp,btnSitUp,btnJog;
	
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_caldiff);
		
		db = new DBManager(this);
		
		//insertFake();
		//insertFakeSports();
	
		getDate();
		getCalList();
		getCalOffList();
		getCalDiffList();
		
		initLineChart();
		
		txtCalAbs=(TextView)findViewById(R.id.diff_calAbsord);
		txtCalOff=(TextView)findViewById(R.id.diff_caloff);
		txtCalDiff=(TextView)findViewById(R.id.diff_caldiff);
		txtRec=(TextView)findViewById(R.id.diff_recommends);
		btnPushUp=(Button)findViewById(R.id.diff_pushup);
		btnSitUp=(Button)findViewById(R.id.diff_situp);
		btnJog=(Button)findViewById(R.id.diff_jog);
		
		txtCalAbs.setText(String.valueOf("摄入卡路里 :"+lineChart.getAverage("摄入卡路里")*7));
		txtCalOff.setText(String.valueOf("消耗卡路里 :"+lineChart.getAverage("运动消耗卡路里")*7));
		txtCalDiff.setText(String.valueOf("总卡路里 :"+lineChart.getAverage("总卡路里")*7));
		
		if(lineChart.getAverage("总卡路里")*7>0){
			txtRec.setText("最近又勇敢地朝土肥圆迈进了呢！\n完成以下运动来赎罪吧");
			btnPushUp.setText((int)(lineChart.getAverage("总卡路里")*7/0.4)+"个俯卧撑");
			btnSitUp.setText((int)(lineChart.getAverage("总卡路里")*7/0.4)+"个俯卧撑");
			btnJog.setText("慢跑约"+(int)(lineChart.getAverage("总卡路里")*7/0.5)+"km");
		}
		else{
			txtRec.setText("干得漂亮，七天内减掉了"+(int)lineChart.getAverage("总卡路里")*7/7.7+"kg脂肪，离瘦子已经不远了！");
			btnPushUp.setVisibility(View.GONE);
			btnSitUp.setVisibility(View.GONE);
			btnJog.setVisibility(View.GONE);	
		}
		
		btnPushUp.setOnClickListener(this);
		btnSitUp.setOnClickListener(this);
		btnJog.setOnClickListener(this);
		
		
		
		System.out.println("");	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private  String getDayBefore(String today) throws java.text.ParseException {
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yyyy-MM-dd").parse(today);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c  
                .getTime());  
        return dayBefore;  
    }  
	
	private void insertFake(){
		Calorie cal = new Calorie();
		
		cal.setCalorie(0.2f);
		cal.setUserId(0);
		cal.setDate("2015-05-11");	
		db.addCalorie(cal);

		cal.setCalorie(0.4f);
		cal.setUserId(0);
		cal.setDate("2015-05-13");	
		db.addCalorie(cal);
		
		cal.setCalorie(0.12f);
		cal.setUserId(0);
		cal.setDate("2015-05-13");	
		db.addCalorie(cal);
		
		cal.setCalorie(0.6f);
		cal.setUserId(0);
		cal.setDate("2015-05-14");	
		db.addCalorie(cal);
		
		cal.setCalorie(0.8f);
		cal.setUserId(0);
		cal.setDate("2015-05-15");	
		db.addCalorie(cal);
	}

    private void getCalOffList(){
    	calOffList = new ArrayList<Calorie>();
    	ArrayList<Sports> sportsList;
    	
		float sum = 0;
		Calorie calorie; 
		//第1天消耗的卡路里
		calorie= new Calorie();
		sportsList= db.querySportsByDate(day1);
		for(int i=0;i<sportsList.size();i++){
			sum+=sportsList.get(i).getCalorie();
		}
		calorie.setCalorie(sum);
		calorie.setDate(day1);
		calOffList.add(calorie);
		
		//第2天消耗的卡路里
		calorie= new Calorie();
		sum=0;
		sportsList= db.querySportsByDate(day2);
		for(int i=0;i<sportsList.size();i++){
			sum+=sportsList.get(i).getCalorie();
		}
		calorie.setCalorie(sum);
		calorie.setDate(day2);
		calOffList.add(calorie);
		
		//第3天消耗的卡路里
		calorie= new Calorie();
		sum=0;
		sportsList= db.querySportsByDate(day3);
		for(int i=0;i<sportsList.size();i++){
			sum+=sportsList.get(i).getCalorie();
		}
		calorie.setCalorie(sum);
		calorie.setDate(day3);
		calOffList.add(calorie);
		
		//第4天消耗的卡路里
		calorie= new Calorie();
		sum=0;
		sportsList= db.querySportsByDate(day4);
		for(int i=0;i<sportsList.size();i++){
			sum+=sportsList.get(i).getCalorie();
		}
		calorie.setCalorie(sum);
		calorie.setDate(day4);
		calOffList.add(calorie);
		
		//第5天消耗的卡路里
		calorie= new Calorie();
		sum=0;
		sportsList= db.querySportsByDate(day5);
		for(int i=0;i<sportsList.size();i++){
			sum+=sportsList.get(i).getCalorie();
		}
		calorie.setCalorie(sum);
		calorie.setDate(day5);
		calOffList.add(calorie);
		
		//第6天消耗的卡路里
		calorie= new Calorie();
		sum=0;
		sportsList= db.querySportsByDate(day6);
		for(int i=0;i<sportsList.size();i++){
			sum+=sportsList.get(i).getCalorie();
		}
		calorie.setCalorie(sum);
		calorie.setDate(day6);
		calOffList.add(calorie);
		
		//第7天消耗的卡路里
		calorie= new Calorie();
		sum=0;
		sportsList= db.querySportsByDate(day7);
		for(int i=0;i<sportsList.size();i++){
			sum+=sportsList.get(i).getCalorie();
		}
		calorie.setCalorie(sum);
		calorie.setDate(day7);
		calOffList.add(calorie);
		
		
		
    	
    }

    private void getCalList(){
    	
		calList = new ArrayList<Calorie>();
    	
		calList.add(db.queryCalorieByDate(day1));
		calList.add(db.queryCalorieByDate(day2));
		calList.add(db.queryCalorieByDate(day3));
		calList.add(db.queryCalorieByDate(day4));
		calList.add(db.queryCalorieByDate(day5));
		calList.add(db.queryCalorieByDate(day6));
		calList.add(db.queryCalorieByDate(day7));	
    	
    }

    private void getCalDiffList(){
    	calDiffList = new ArrayList<Calorie>();
    	Calorie calorieDiff;
    	for(int i=0;i<7;i++){
    		calorieDiff = new Calorie();
    		calorieDiff.setDate(calList.get(i).getDate());
    		calorieDiff.setCalorie((calList.get(i).getCalorie()-calOffList.get(i).getCalorie()-1.4f));
    		calDiffList.add(calorieDiff);
    	}
    }
    
	private void getDate(){
    	SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
		day7=simpleFormatter.format(new java.util.Date());
		
		try {
			day6=getDayBefore(day7);
			day5=getDayBefore(day6);
			day4=getDayBefore(day5);
			day3=getDayBefore(day4);
			day2=getDayBefore(day3);
			day1=getDayBefore(day2);		
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

	private void initLineChart(){
		ArrayList<Entry> calOff = new ArrayList<Entry>();
	    ArrayList<Entry> calAbsorb = new ArrayList<Entry>();
	    ArrayList<Entry> calDiff = new ArrayList<Entry>();

	    for(int i=0;i<7;i++){
	    	Entry cal=new Entry(calList.get(i).getCalorie(),i);
	    	calAbsorb.add(cal);
	    	cal = new Entry(calOffList.get(i).getCalorie(),i); 
	    	calOff.add(cal);
	    	cal = new Entry(calDiffList.get(i).getCalorie(),i);
	    	calDiff.add(cal);
	    }
	    	
	    	LineDataSet calAbsorbData = new LineDataSet(calAbsorb, "摄入卡路里");
	    	calAbsorbData.setAxisDependency(AxisDependency.LEFT);
	        LineDataSet calOffData = new LineDataSet(calOff, "运动消耗卡路里");
	        calOffData.setAxisDependency(AxisDependency.LEFT);
	        LineDataSet calDiffData = new LineDataSet(calDiff, "总卡路里");
	        calDiffData.setAxisDependency(AxisDependency.LEFT);
	        
	        calAbsorbData.setCircleColor(Color.RED);
	        calAbsorbData.setCircleColorHole(Color.WHITE);
	        calAbsorbData.setColor(Color.RED);
	        calAbsorbData.setLineWidth(2f);
	        
	        calOffData.setCircleColor(Color.GREEN);
	        calOffData.setCircleColorHole(Color.WHITE);
	        calOffData.setColor(Color.GREEN);
	        calOffData.setLineWidth(2f);
	        
	        calDiffData.setCircleColor(Color.BLACK);
	        calDiffData.setCircleColorHole(Color.WHITE);
	        calDiffData.setColor(Color.BLACK);
	        calDiffData.setLineWidth(2f);

	        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	        dataSets.add(calAbsorbData);
	        dataSets.add(calOffData);
	        dataSets.add(calDiffData);

	        ArrayList<String> xVals = new ArrayList<String>();
	        xVals.add(day1);
	        xVals.add(day2);
	        xVals.add(day3);
	        xVals.add(day4); 
	        xVals.add(day5);
	        xVals.add(day6);
	        xVals.add(day7);	 
	        
	        LineData data = new LineData(xVals, dataSets);
	        
	        lineChart = (LineChart)findViewById(R.id.lineChart);
	        lineChart.setData(data);
	        lineChart.getAxisLeft().setStartAtZero(false);
	        lineChart.getAxisLeft().resetAxisMaxValue();
	        lineChart.getAxisLeft().resetAxisMinValue();
	        lineChart.invalidate();  	
	}
	
	public void insertFakeSports(){
		Sports sports = new Sports();
		sports.setCalorie(2.2f);
		sports.setType(GlobalVariables.SPORTS_TYPE_PUSHUP);
		sports.setDate("2015-05-13 下午 9:20:56");
		sports.setDuration("aaaaa");
		db.insertSport(sports);
		
		sports.setCalorie(10f);
		sports.setType(GlobalVariables.SPORTS_TYPE_JOG);
		sports.setDate("2015-05-11 上午 8:20:56");
		db.insertSport(sports);		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.diff_pushup:
			intent.setClass(this, PushUpActivity.class);
			break;
		case R.id.diff_situp:
			intent.setClass(this, SitUpActivity.class);
			break;
		case R.id.diff_jog:
			intent.setClass(this, GetLocation.class);
			break;
		default:
		}

		startActivity(intent);		
	}

}
