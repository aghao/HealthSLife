package com.healthlife.db;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.healthlife.entity.*;

public class DBManager {
	
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private ContentValues values;
	private long userId;
	
	public DBManager(Context context){
		values = new ContentValues();
		dbHelper = new DBHelper(context,"HealthSlife.db",null,1);
		db = dbHelper.getWritableDatabase();
		SharedPreferences pref = context.getSharedPreferences("CurrentUser",Activity.MODE_PRIVATE);
		userId=pref.getLong("userid", 0);
		db.execSQL("PRAGMA foreign_keys = ON");
	}
	
	public long insertMusic(Music music){
		
		values.put("PATH", music.getMusicPath());
		values.put("IFACTIVE", music.isIfActive());
		values.put("PACE", music.getPace());
		values.put("MUSICNAME", music.getMusicName());

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
		values.put("USERID",userId);
		
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
		values.put("GOODNUM", sports.getGoodNum());
		values.put("MAXSPEED", sports.getMaxSpeed());
		values.put("NUM", sports.getNum());
		values.put("PERFECTNUM", sports.getPerfectNum());
		values.put("VALIDNUM", sports.getValidNum());
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
		
		Cursor cursor = db.query("SPORTS", null, "USERID = ?", new String [] {String.valueOf(0)}, null, null, "SPORTID");
		
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
				sports.setValidNum(cursor.getFloat(cursor.getColumnIndex("VALIDNUM")));
				sports.setPerfectNum(cursor.getFloat(cursor.getColumnIndex("PERFECTNUM")));
				sports.setGoodNum(cursor.getFloat(cursor.getColumnIndex("GOODNUM")));
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
		
		Cursor cursor =  db.query("BEATS", null, "USERID = ?",new String [] {String.valueOf(userId)}, null, null, null);
		
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
		
		Cursor cursor = db.query("SPORTS", null, "SPORTID = ?", new String [] {String.valueOf(sportsId)}, null, null, null);
		
		if(cursor.moveToNext()){
			
			sports.setSportsID(cursor.getLong(cursor.getColumnIndex("SPORTID")));
			sports.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
			sports.setDuration(cursor.getString(cursor.getColumnIndex("DURATION")));
			sports.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
			sports.setUserId(cursor.getLong(cursor.getColumnIndex("USERID")));
			sports.setNum(cursor.getInt(cursor.getColumnIndex("NUM")));
			sports.setAVGSpeed(cursor.getFloat(cursor.getColumnIndex("AVGSPEED")));
			sports.setValidNum(cursor.getFloat(cursor.getColumnIndex("VALIDNUM")));
			sports.setPerfectNum(cursor.getFloat(cursor.getColumnIndex("PERFECTNUM")));
			sports.setGoodNum(cursor.getFloat(cursor.getColumnIndex("GOODNUM")));
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
		
		Cursor cursor = db.query("RECORDS", null, "USERID = ?", new String [] {String.valueOf(0)}, null, null, null);
		
		if(cursor.moveToNext())
		{
			record.setAVGPace(cursor.getInt(cursor.getColumnIndex("AVGPACE")));
			record.setAVGSpeed(cursor.getFloat(cursor.getColumnIndex("AVGSPEED")));
			record.setDistance(cursor.getFloat(cursor.getColumnIndex("DISTANCE")));
			record.setGoodNumPushUp(cursor.getFloat(cursor.getColumnIndex("GOODNUMPUSHUP")));
			record.setGoodNumSitUp(cursor.getFloat(cursor.getColumnIndex("GOODNUMSITUP")));
			record.setNumPushUp(cursor.getInt(cursor.getColumnIndex("NUMPUSHUP")));
			record.setNumSitUp(cursor.getInt(cursor.getColumnIndex("NUMSITUP")));
			record.setPerfectNumPushUp(cursor.getFloat(cursor.getColumnIndex("PERFECTNUMPUSHUP")));
			record.setPerfectNumSitUp(cursor.getFloat(cursor.getColumnIndex("PERFECTNUMSITUP")));
			record.setRecordId(cursor.getLong(cursor.getColumnIndex("RECORDID")));
			record.setSteps(cursor.getInt(cursor.getColumnIndex("STEPS")));
			record.setTotalDistance(cursor.getFloat(cursor.getColumnIndex("TOTALDISTANCE")));
			record.setTotalNumPushUp(cursor.getInt(cursor.getColumnIndex("TOTALNUMPUSHUP")));
			record.setTotalNumSitUp(cursor.getInt(cursor.getColumnIndex("TOTALNUMSITUP")));
			record.setUserId(cursor.getLong(cursor.getColumnIndex("USERID")));
			record.setValidNumPushUp(cursor.getFloat(cursor.getColumnIndex("VALIDNUMPUSHUP")));
			record.setValidNumSitUp(cursor.getFloat(cursor.getColumnIndex("VALIDNUMSITUP")));
		}
		cursor.close();
		
		return record;
		
	}
	
	public void updateRecord(Record record){
							
		values.put("AVGSPEED", record.getAVGSpeed());
		values.put("NUMPUSHUP", record.getNumPushUp());
		values.put("NUMSITUP", record.getNumSitUp());
		values.put("VALIDNUMPUSHUP", record.getValidNumPushUp());
		values.put("VALIDNUMSITUP", record.getValidNumSitUp());
		values.put("DISTANCE",record.getDistance());
		values.put("AVGPACE",record.getAVGPace());
		values.put("PERFECTNUMPUSHUP",record.getPerfectNumPushUp());
		values.put("PERFECTNUMSITUP",record.getPerfectNumSitUp());
		values.put("GOODNUMPUSHUP",record.getNumPushUp());
		values.put("GOODNUMSITUP",record.getNumSitUp());
		values.put("TOTALDISTANCE",record.getTotalDistance());
		values.put("TOTALNUMPUSHUP",record.getTotalNumPushUp());
		values.put("TOTALNUMSITUP",record.getTotalNumSitUp());
		values.put("STEPS",record.getSteps());
		
		if(values.size()!=0)
			db.update("RECORDS", values, "RECORDID = ?", new String[] {String.valueOf(record.getRecordId())});
		values.clear();	}
	
	public void clearRecord(long recordId){
		
		Record record = new Record();
		record.setUserId(userId);

		this.updateRecord(record);
	}
	
	public void removeRecord(long userId){
		
		db.delete("RECORDS", "USERID = ?", new String [] {String.valueOf(userId)});
		
	}
	
	public void insertRecord(Record record){
		
		values.put("AVGSPEED", record.getAVGSpeed());
		values.put("NUMPUSHUP", record.getNumPushUp());
		values.put("NUMSITUP", record.getNumSitUp());
		values.put("VALIDNUMPUSHUP", record.getValidNumPushUp());
		values.put("VALIDNUMSITUP", record.getValidNumSitUp());
		values.put("DISTANCE",record.getDistance());
		values.put("AVGPACE",record.getAVGPace());
		values.put("PERFECTNUMPUSHUP",record.getPerfectNumPushUp());
		values.put("PERFECTNUMSITUP",record.getPerfectNumSitUp());
		values.put("GOODNUMPUSHUP",record.getNumPushUp());
		values.put("GOODNUMSITUP",record.getNumSitUp());
		values.put("TOTALDISTANCE",record.getTotalDistance());
		values.put("TOTALNUMPUSHUP",record.getTotalNumPushUp());
		values.put("TOTALNUMSITUP",record.getTotalNumSitUp());
		values.put("STEPS",record.getSteps());
		values.put("USERID", record.getUserId());
		
		db.insert("RECORDS", null, values);
		values.clear();
	}

	public long insertPosition(Position position){
		
		values.put("LATITUDE",position.getLatitude());
		values.put("LONGITUDE", position.getLongitude());
		values.put("SPORTID", position.getSportId());
		values.put("TIME", position.getTime());
		
		position.setPositionId(db.insert("POSITIONS", null, values));
		
		values.clear();
		return position.getPositionId();
		
	}
	
	public ArrayList<Position>getPositionsByUerId(long userId){
		
		ArrayList<Position> positionList=new ArrayList<Position>();
		ArrayList<Long> sportIdList =  new ArrayList<Long>();
		
		Cursor cursor = db.query("SPORTS",null, "USERID = ?", new String [] {String.valueOf(userId)}, null, null, null);
		
		while(cursor.moveToNext()){			
			sportIdList.add(cursor.getLong(cursor.getColumnIndex("SPORTID")));		
		}
		
		cursor.close();
		int i;
		for(i=0;i<sportIdList.size();i++){
			cursor = db.query("POSITIONS", null, "SPORTID = ?", new String [] {String.valueOf(sportIdList.get(i))}, null, null, null);	
			
			while(cursor.moveToNext()){
				
				Position position = new Position();
				
				position.setPositionId(cursor.getLong(cursor.getColumnIndex("POSITIONID")));
				position.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
				position.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
				position.setTime(cursor.getLong(cursor.getColumnIndex("TIME")));
				position.setSportId(cursor.getLong(cursor.getColumnIndex("SPORTID")));
				
				positionList.add(position);
			}
		}
		return positionList;
	}
	
	public ArrayList<Position> getPosList(long jogId){
	
		ArrayList<Position> positionList = new ArrayList<Position>();
		Cursor cursor = db.query("POSITIONS", null, "SPORTID = ?", new String [] {String.valueOf(jogId)}, null, null, null); 
		
		while(cursor.moveToNext()){
			
			Position position = new Position();
			
			position.setPositionId(cursor.getLong(cursor.getColumnIndex("POSITIONID")));
			position.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
			position.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
			position.setTime(cursor.getLong(cursor.getColumnIndex("TIME")));
			position.setSportId(cursor.getLong(cursor.getColumnIndex("SPORTID")));
			
			positionList.add(position);
		}
		cursor.close();
		
		return positionList;
	}

	public void removeAllBeats(long userId){
		db.delete("BEATS", "USERID = ?", new String [] {String.valueOf(userId)});
	}
	
	public void removeAllSports(long userId){
		db.delete("SPORTS", "USERID = ?", new String[] {String.valueOf(userId)});
	}
}
