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
			+"AVGSPEED REAL,"
			+"NUMPUSHUP INTEGER,"
			+"NUMSITUP INTEGER,"
			+"VALIDRATEPUSHUP REAL,"
			+"VALIDRATESITUP REAL,"
			+"DISTANCE REAL,"
			+"AVGPACE REAL,"
			+"PERFECTRATEPUSHUP REAL,"
			+"PERFECTRATESITUP REAL,"
			+"GRADEPUSHUP REAL,"
			+"GRADESITUP REAL,"
			+"TOTALDISTANCE REAL,"
			+"TOTALNUMPUSHUP INTEGER,"
			+"TOTALNUMSITUP INTEGER,"
			+"STEPS INTEGER"
			+")";
	private static final String CREATE_SPORTS = "CREATE TABLE SPORTS (" 
			+"SPORTID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"DATE VARCHAR(50),"
			+"DURATION VARCHAR(50),"
			+"TYPE INTEGER,"
			+"USERID INTEGER NOT NULL,"
			+"NUM INTEGER,"
			+"AVGSPEED REAL,"
			+"VALIDRATE REAL,"
			+"PERFECTRATE REAL,"
			+"GRADE REAL,"
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

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_SPORTS);
		db.execSQL(CREATE_RECORDS);
		db.execSQL(CREATE_BEATS);
		db.execSQL(CREATE_MUSIC);
		db.execSQL(CREATE_POSITIONS);
		db.execSQL("PRAGMA foreign_keys = ON");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		// TODO Auto-generated method stub
		
	}

}