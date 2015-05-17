package com.healthlife.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

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

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_SPORTS);
		db.execSQL(CREATE_RECORDS);
		db.execSQL(CREATE_BEATS);
		db.execSQL(CREATE_MUSIC);
		db.execSQL(CREATE_POSITIONS);
		db.execSQL(CREATE_CALORIE);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		// TODO Auto-generated method stub
		
	}

}