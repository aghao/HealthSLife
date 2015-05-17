package com.healthlife.db;

import com.healthlife.entity.GlobalVariables;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler.Value;

public class DBHelper extends SQLiteOpenHelper {
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	/*private static final String CREATE_USERS = " CREATE TABLE USERS ( "
			+ "USERID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "KEYWORD VARCHAR(50)"
		    + ")";
	*/
	
	private static final String CREATE_RECORDS = " CREATE TABLE RECORDS ("
			+"RECORDID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"USERID INTEGER NOT NULL,"
			
			+"AVGSPEED INTEGER,"
			+"DISTANCE INTEGER,"
			
			+"CALOFPUSHUP REAL,"
			+"CALOFSITUP REAL,"
			+"CALOFJOG REAL,"
			+"TOTALCAL REAL,"
			
			+"DURATIONPUSHUP REAL,"
			+"DURATIONJOG REAL,"
			+"DURATIONSITUP REAL,"
			+"DURATIONPERDAY REAL,"		
			+"TOTALDURATION REAL,"
			
			+"TOTALDISTANCE REAL,"
			+"TOTALNUMPUSHUP INTEGER,"
			+"TOTALNUMSITUP INTEGER,"
			+"TOTALSTEPS INTEGER"
			+")";
	
	private static final String CREATE_SPORTS = "CREATE TABLE SPORTS (" 
			+"SPORTID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"DATE VARCHAR(50),"
			+"DURATION VARCHAR(50),"
			+"TYPE INTEGER,"
			+"USERID INTEGER NOT NULL,"
			+"NUM INTEGER,"
			+"AVGSPEED REAL,"
			+"VALIDNUM REAL,"
			+"PERFECTNUM REAL,"
			+"GOODNUM REAL,"
			+"MAXSPEED REAL,"
			+"DISTANCE REAL"
			+")";
	
	private static final String CREATE_BEATS = "CREATE TABLE BEATS ("
			+"BEATID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"USERID INTEGER NOT NULL,"
			+"DATE VARCHAR(50),"
			+"BEATS INTEGER,"
			+"TYPE INTEGER"
			+")";
	private static final String CREATE_POSITIONS ="CREATE TABLE POSITIONS("
			+"POSITIONID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+"SPORTID INTEGER REFERENCES SPORTS(SPORTID) ON DELETE CASCADE,"
			+"LATITUDE REAL,"
			+"LONGITUDE REAL,"
			+"TIME VARCHAR(50)"
			+")";	
	private static final String CREATE_MUSIC = "CREATE TABLE MUSIC ("
			+"MUSICID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"MUSICNAME VARCHAR(50),"
			+"PATH VARCHAR(50),"
			+"PACE INTEGER,"
			+"IFACTIVE INTEGER"
			+")";
	
	private static final String CREATE_CALORIE = "CREATE TABLE CALORIE ("
			+"CALORIEID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"USERID INTEGER,"
			+"CALORIE REAL,"
			+"DATE VARCHAR(50)"
			+")";
	
	private static final String CREATE_FOODS = "CREATE TABLE FOODS ("
			+"FOODID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"CALORIE REAL,"
			+"TYPE INTEGER,"
			+"FOODNAME VARCHAR(50)"
			+")";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		db.execSQL(CREATE_SPORTS);
		db.execSQL(CREATE_RECORDS);
		db.execSQL(CREATE_BEATS);
		db.execSQL(CREATE_MUSIC);
		db.execSQL(CREATE_POSITIONS);
		db.execSQL(CREATE_CALORIE);
		db.execSQL(CREATE_FOODS);
		
		values.put("FOODNAME", "Ã×·¹");	
		values.put("CALORIE",1.5);
		values.put("TYPE",GlobalVariables.MAINFOOD);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "¼å±ý");	
		values.put("CALORIE", 333);
		values.put("TYPE",GlobalVariables.MAINFOOD);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "»¨¾í");	
		values.put("CALORIE", 217);
		values.put("TYPE",GlobalVariables.MAINFOOD);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "ÂøÍ·");	
		values.put("CALORIE", 233);
		values.put("TYPE",GlobalVariables.MAINFOOD);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "ÉÕÂó");	
		values.put("CALORIE", 100);
		values.put("TYPE",GlobalVariables.MAINFOOD);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "¶¹»¨");	
		values.put("CALORIE",47);
		values.put("TYPE",GlobalVariables.SNACKS);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "Æ»¹û");	
		values.put("CALORIE", 80);
		values.put("TYPE",GlobalVariables.FRUITS);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "³´¸Î");	
		values.put("CALORIE", 96);
		values.put("TYPE",GlobalVariables.DISHES);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME", "Í­ÂàÉÕ");	
		values.put("CALORIE",280);
		values.put("TYPE",GlobalVariables.SNACKS);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME","¿É¿Ú¿ÉÀÖ");	
		values.put("CALORIE", 1.12);
		values.put("TYPE",GlobalVariables.DRINKS);
		db.insert("FOODS", null, values);
		values.clear();
				
		values.put("FOODNAME","±ùä¿ÁÜ");	
		values.put("CALORIE", 200);	
		values.put("TYPE",GlobalVariables.SNACKS);
		db.insert("FOODS", null, values);
		values.clear();
		
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		// TODO Auto-generated method stub
		
	}

}