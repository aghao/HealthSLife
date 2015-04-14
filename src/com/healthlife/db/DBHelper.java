package com.healthlife.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public static final String CREATE_USERS = " CREATE TABLE USERS ( "
			+ "USERID INTEGER PRIMARY KEY AUTOINCREMENT"
		    + ")";
		
	public static final String CREATE_RECORDS = " CREATE TABLE RECORDS ("
			+"RECORDID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"USERID INTEGER REFERENCES USER(USERID) ON DELETE CASCADE,"
			+"AVGSPEED REAL,"
			+"NUMPUSHUP INTEGER,"
			+"NUMSITUP INTERGER,"
			+"VALIDRATEPUSHUP REAL,"
			+"VALIDRATESITUP REAL,"
			+"DISTANCE REAL,"
			+"AVGEPACE REAL,"
			+"PERFECTRATEPUSHUP REAL,"
			+"PERFECTRATESITUP REAL,"
			+"GRADEPUSHUP INTEGER,"
			+"GRADESITUP INTEGER,"
			+"TOTALDISTANCE INTEGER,"
			+"TOTALNUMPUSHUP INTEGER,"
			+"TOTALNUMSITUP INTEGER,"
			+"STEPS INTEGER"
			+")";
	public static final String CREATE_SPORTS = "CREATE TABLE SPORTS (" 
			+"SPORTID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"DATE TEXT "
			+"DURATION TEXT,"
			+"TYPE INTEGER,"
			+"USERID INTEGER REFERENCES USER(USERID) ON DELETE CASCADE,"
			+"NUM INTEGER,"
			+"AVGSPEED REAL,"
			+"VALIDRATE REAL,"
			+"PERFECTRATE REAL,"
			+"GRADE REAL,"
			+"MAXSPEED REAL,"
			+"DISTANCE REAL"
			+")";
	public static final String CREATE_BEATS = "CREATE TABLE HEART_BEATS ("
			+"BEATID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"USERID INTEGER REFERENCES USER(USERID) ON DELETE CASCADE,"
			+"DATE TIMESTAMP,"
			+"BEATS INTEGER,"
			+"TYPE INT"
			+")";
	
	public static final String CREATE_MUSIC = "CREATE TABLE MUSIC ("
	
			+"MUSICID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"MUSICNAME BLOB,"
			+"PATH BLOB,"
			+"PACE INTEGER,"
			+"IFACTIVE INTEGER"
			+")";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USERS);
		db.execSQL(CREATE_SPORTS);
		db.execSQL(CREATE_RECORDS);
		db.execSQL(CREATE_BEATS);
		db.execSQL(CREATE_MUSIC);
		db.execSQL("PRAGMA foreign_keys = ON");
		Log.i("db", "foreign key support succeed");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		// TODO Auto-generated method stub
		
	}

}