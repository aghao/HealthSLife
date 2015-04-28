package com.healthlife.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.healthlife.entity.*;

public class DBManager {
	
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = new ContentValues();
	
	public DBManager(Context context){
		
		dbHelper = new DBHelper(context,"HealthSlife.db",null,1);
		db = dbHelper.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys = ON");
	}
	
	public long insertMusic(Music music){
		
		values.put("PATH", music.getMusicPath());
		values.put("IFACTIVE", music.isIfActive());
		values.put("PACE", music.getPace());

		Cursor cursor = db.query("MUSIC", null, "PATH = ?", new String[] {music.getMusicPath()}, null, null, null);
		if(cursor.getCount()==0)
			music.setMusicId(db.insert("MUSIC", null, values));
		
		values.clear();
		
		return music.getMusicId();
	}
	
	public void removeMusic(long musicId){
		
		db.delete("MUSIC", "MUSICID = ?", new String[] {String.valueOf(musicId)});
	}
	
	public void updateMusic(Music music){
		
		values.put("MUSICNAME", music.getMusicName());
		values.put("PATH", music.getMusicPath());
		values.put("PACE", music.getPace());
		values.put("IFACTIVE", music.isIfActive());
		
		db.update("MUSIC", values, "MusicId = ?", new String[] {String.valueOf(music.getMusicId())});
		
		values.clear();
	}
	
	public long insertBeats(Beats beats){
		
		values.put("DATE",beats.getDate().toString());
		values.put("BEATS",beats.getBeats());
		values.put("TYPE",beats.getType());
		values.put("USERID",1);
		
		beats.setBeatId(db.insert("BEATS", null, values));
		values.clear();
		
		return beats.getBeatId();

	}
	
	public void removeBeat(long beatId){
		
		db.delete("BEATS", "BEATID = ?", new String[] {String.valueOf(beatId)});

	}
	
	public long insertSport(Sports sports){
		
		values.put("DATE", sports.getDate().toString());
		values.put("AVGSPEED", sports.getAVGSpeed());
		values.put("DISTANCE", sports.getDistance());
		values.put("GRADE", sports.getGrade());
		values.put("MAXSPEED", sports.getMaxSpeed());
		values.put("NUM", sports.getNum());
		values.put("PERFECTRATE", sports.getPerfectRate());
		values.put("VALIDRATE", sports.getValidRate());
		values.put("USERID", sports.getUserId());
		values.put("TYPE", sports.getType());
		values.put("DURATION", sports.getDuration().toString());

		sports.setSportsID(db.insert("SPORTS", null, values));
		values.clear();
		
		return sports.getSportsID();	
	}
	
	public void removeSport(long sportsId){
		
		db.delete("SPORTS", "SPORTID = ?", new String[] {String.valueOf(sportsId)});
		
	}

	public ArrayList<Sports> getSportList(){
		
		Sports sports = null;
		ArrayList<Sports> sportsList = null;
		
		Cursor cursor = db.query("SPORTS", null, "USERID = ?", new String [] {String.valueOf(GlobalVariables.CURRENT_USERID)}, null, null, "SPORTSID");
		
		if(cursor!=null)
		{
			sportsList =  new ArrayList<Sports>();
			
			while(cursor.moveToNext()){
				sports = new Sports();
				sports.setSportsID(cursor.getLong(cursor.getColumnIndex("SPORTID")));
				sports.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
				sports.setDuration(cursor.getString(cursor.getColumnIndex("DURATION")));
				sports.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
				sports.setUserId(cursor.getLong(cursor.getColumnIndex("USERID")));
				sports.setNum(cursor.getInt(cursor.getColumnIndex("NUM")));
				sports.setAVGSpeed(cursor.getFloat(cursor.getColumnIndex("AVGSPEED")));
				sports.setValidRate(cursor.getFloat(cursor.getColumnIndex("VALIDRATE")));
				sports.setPerfectRate(cursor.getFloat(cursor.getColumnIndex("PERFECTRATE")));
				sports.setGrade(cursor.getFloat(cursor.getColumnIndex("GRADE")));
				sports.setMaxSpeed(cursor.getFloat(cursor.getColumnIndex("MAXSPEED")));
				sports.setDistance(cursor.getInt(cursor.getColumnIndex("DISTANCE")));
				
				sportsList.add(sports);

			}
		}
		
		cursor.close();

		return sportsList;
	}
	
	public ArrayList<Music> getMusicList(){
		Music music = null;
		ArrayList<Music> musicList = null;
		
		Cursor cursor =  db.query("MUSIC", null, null, null, null, null, null);
		
		if(cursor!=null)
		{
			
			musicList =  new ArrayList<Music>();
			
			while(cursor.moveToNext()){
				music = new Music();
				
				music.setIfActive(cursor.getInt(cursor.getColumnIndex("IFACTIVE")));
				music.setMusicId(cursor.getLong(cursor.getColumnIndex("MUSICID")));
				music.setMusicName(cursor.getString(cursor.getColumnIndex("MUSICNAME")));
				music.setMusicPath(cursor.getString(cursor.getColumnIndex("PATH")));
				music.setPace(cursor.getInt(cursor.getColumnIndex("PACE")));
				
				musicList.add(music);
			}
			
			cursor.close();
		}
		
		return musicList;	
	}
	
	public ArrayList<Music> getActivedMusicList(){
		Music music = null;
		ArrayList<Music> musicList = null;
		
		Cursor cursor =  db.query("MUSIC", null, "IFACTIVE = ?",new String [] {String.valueOf(1)}, null, null, null);
		
		if(cursor!=null)
		{
			
		
			musicList =  new ArrayList<Music>();
			
			while(cursor.moveToNext()){
				music = new Music();
				
				music.setIfActive(cursor.getInt(cursor.getColumnIndex("IFACTIVE")));
				music.setMusicId(cursor.getLong(cursor.getColumnIndex("MUSICID")));
				music.setMusicName(cursor.getString(cursor.getColumnIndex("MUSICNAME")));
				music.setMusicPath(cursor.getString(cursor.getColumnIndex("PATH")));
				music.setPace(cursor.getInt(cursor.getColumnIndex("PACE")));
				
				musicList.add(music);
			}
			
			cursor.close();
		}
		
		return musicList;
		
	}
	
	public ArrayList<Music> getMusicByPace(int pace){
		
		Music music = null;
		ArrayList<Music> musicList = null;
		
		Cursor cursor =  db.query("MUSIC", null, "PACE = ?",new String [] {String.valueOf(pace)}, null, null, null);
		
		if(cursor!=null)
		{
			
			musicList =  new ArrayList<Music>();
			
			while(cursor.moveToNext()){
				music = new Music();
				
				music.setIfActive(cursor.getInt(cursor.getColumnIndex("IFACTIVE")));
				music.setMusicId(cursor.getLong(cursor.getColumnIndex("MUSICID")));
				music.setMusicName(cursor.getString(cursor.getColumnIndex("MUSICNAME")));
				music.setMusicPath(cursor.getString(cursor.getColumnIndex("PATH")));
				music.setPace(cursor.getInt(cursor.getColumnIndex("PACE")));
				
				musicList.add(music);
			}
			
			cursor.close();
		}
		
		return musicList;
		
	}
	
	public ArrayList<Beats> getBeatsList(){
		Beats beats = null;
		ArrayList<Beats> beatsList = null;
		
		Cursor cursor =  db.query("BEATS", null, "USERID = ?",new String [] {String.valueOf(GlobalVariables.CURRENT_USERID)}, null, null, null);
		
		if(cursor!=null)
		{
			beatsList = new ArrayList<Beats>();
			
			while(cursor.moveToNext()){
				beats = new Beats();
				
				beats.setBeatId(cursor.getLong(cursor.getColumnIndex("BEATID")));
				beats.setBeats(cursor.getInt(cursor.getColumnIndex("BEATS")));
				beats.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
				beats.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
				beats.setUserId(cursor.getLong(cursor.getColumnIndex("USERID")));
				
				beatsList.add(beats);
			}
			cursor.close();
		}
		
		return beatsList;
	}
	
	public Music queryMusic(){
		
		return null;	
	}
	
	public Sports querySport(long sportsId){
		
		Sports sports = new Sports();
		
		Cursor cursor = db.query("SPORTS", null, "SPORTSID = ?", new String [] {String.valueOf(sportsId)}, null, null, null);
		
		if(cursor.moveToNext()){
			
			sports.setSportsID(cursor.getLong(cursor.getColumnIndex("SPORTID")));
			sports.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
			sports.setDuration(cursor.getString(cursor.getColumnIndex("DURATION")));
			sports.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
			sports.setUserId(cursor.getLong(cursor.getColumnIndex("USERID")));
			sports.setNum(cursor.getInt(cursor.getColumnIndex("NUM")));
			sports.setAVGSpeed(cursor.getFloat(cursor.getColumnIndex("AVGSPEED")));
			sports.setValidRate(cursor.getFloat(cursor.getColumnIndex("VALIDRATE")));
			sports.setPerfectRate(cursor.getFloat(cursor.getColumnIndex("PERFECTRATE")));
			sports.setGrade(cursor.getFloat(cursor.getColumnIndex("GRADE")));
			sports.setMaxSpeed(cursor.getFloat(cursor.getColumnIndex("MAXSPEED")));
			sports.setDistance(cursor.getInt(cursor.getColumnIndex("DISTANCE")));
		}
		cursor.close();
		return sports;	
	}
	
	public Beats queryBeats(){
		
		return null;		
	}
	
	public Record queryRecord(){
		
		Record record = new Record();
		
		Cursor cursor = db.query("RECORD", null, "USERID = ?", new String [] {String.valueOf(GlobalVariables.CURRENT_USERID)}, null, null, null);
		
		if(cursor.moveToNext())
		{
			record.setAVGPace(cursor.getInt(cursor.getColumnIndex("AVGPACE")));
			record.setAVGSpeed(cursor.getFloat(cursor.getColumnIndex("AVGSPEED")));
			record.setDistance(cursor.getFloat(cursor.getColumnIndex("DISTANCE")));
			record.setGradePushUp(cursor.getFloat(cursor.getColumnIndex("GRADEPUSHUP")));
			record.setGradeSitUp(cursor.getFloat(cursor.getColumnIndex("GRADESITUP")));
			record.setNumPushUp(cursor.getInt(cursor.getColumnIndex("NUMPUSHUP")));
			record.setNumSitUp(cursor.getInt(cursor.getColumnIndex("NUMSITUP")));
			record.setPerfectRatePushUp(cursor.getFloat(cursor.getColumnIndex("PERFECTRATEPUSHUP")));
			record.setPerfectRateSitUp(cursor.getFloat(cursor.getColumnIndex("PERFECTRATESITUP")));
			record.setRecordId(cursor.getLong(cursor.getColumnIndex("RECORDID")));
			record.setSteps(cursor.getInt(cursor.getColumnIndex("STEPS")));
			record.setTotalDistance(cursor.getFloat(cursor.getColumnIndex("TOTALDISTANCE")));
			record.setTotalNumPushUp(cursor.getInt(cursor.getColumnIndex("TOTALNUMPUSHUP")));
			record.setTotalNumSitUp(cursor.getInt(cursor.getColumnIndex("TOTALNUMSITUP")));
			record.setUserId(cursor.getLong(cursor.getColumnIndex("USERID")));
			record.setValidRatePushUp(cursor.getFloat(cursor.getColumnIndex("VALIDRATEPUSHUP")));
			record.setValidRateSitUp(cursor.getFloat(cursor.getColumnIndex("VALIDRATESITUP")));
		}
		cursor.close();
		
		return record;
		
	}
	
	public void updateRecord(Record record,int [] updateMode){
		
		if(updateMode [GlobalVariables.RECORD_TYPE_AVGSPEED]==1)			
			values.put("AVGSPEED", record.getAVGSpeed());
		if(updateMode [GlobalVariables.RECORD_TYPE_NUMPUSHUP]==1)			
			values.put("NUMPUSHUP", record.getNumPushUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_NUMSITUP]==1)			
			values.put("NUMSITUP", record.getNumSitUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_VALIDRATEPUSHUP]==1)		
			values.put("VALIDRATEPUSHUP", record.getValidRatePushUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_VALIDRATESITUP]==1)		
			values.put("VALIDRATESITUP", record.getValidRateSitUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_DISTANCE]==1)			
			values.put("DISTANCE",record.getDistance());
		if(updateMode [GlobalVariables.RECORD_TYPE_AVGPACE]==1)				
			values.put("AVGPACE",record.getAVGPace());
		if(updateMode [GlobalVariables.RECORD_TYPE_PERFECTRATEPUSHUP]==1)	
			values.put("PERFECTRATEPUSHUP",record.getPerfectRatePushUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_PERFECTRATESITUP]==1)	
			values.put("PERFECTRATESITUP",record.getPerfectRateSitUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_GRADEPUSHUP]==1)			
			values.put("GRADEPUSHUP",record.getGradePushUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_GRADESITUP]==1)			
			values.put("GRADESITUP",record.getGradeSitUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_TOTALDISTANCE]==1)		
			values.put("TOTALDISTANCE",record.getTotalDistance());
		if(updateMode [GlobalVariables.RECORD_TYPE_TOTALNUMPUSHUP]==1)		
			values.put("TOTALNUMPUSHUP",record.getTotalNumPushUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_TOTALNUMSITUP]==1)		
			values.put("TOTALNUMSITUP",record.getTotalNumSitUp());
		if(updateMode [GlobalVariables.RECORD_TYPE_STEPS]==1)				
			values.put("STEPS",record.getSteps());
		
		if(values.size()!=0)
		db.update("RECORD", values, "RECORDID = ?", new String[] {String.valueOf(GlobalVariables.CURRENT_USERID)});
		values.clear();	}
	
	public void clearRecord(long recordId){
		
		db.delete("RECORDS", "USERID = ?", new String [] {String.valueOf(GlobalVariables.CURRENT_USERID)});
		values.put("RECORDID", db.insert("RECORDS", null, values));
		db.update("USERS", values, "USERID = ?", new String [] {String.valueOf(GlobalVariables.CURRENT_USERID)});
		values.clear();
	}
	
	public void insertUserForFake(){
		values.put("USERNAME","asdasd");
		long a = db.insert("USERS", null, values);
		values.clear();
		Log.i("dd", "userid:"+a);
	}
}
