package com.healthlife.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.xmlpull.v1.XmlPullParserException;

import com.healthlife.db.DBManager;
import com.healthlife.entity.Beats;
import com.healthlife.entity.IdRefer;
import com.healthlife.entity.Position;
import com.healthlife.entity.Record;
import com.healthlife.entity.Sports;
import com.healthlife.entity.User;
import com.healthlife.util.CurrentUser;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class ClientService extends Service{

	
	private boolean flag=true;
	private int action=0;
	private final int FREE=0;
	private final int REGISTER=1;
	private final int LOGIN=2;
	private final int BACKUPS=3;
	private final int RECOVERY=4;
	
	private final int ACTION_ERROR=-1;
	private final int CONNECT_ERROR=0;
	private final int SUCCESS=1;
	
	private long userid;
	private String username;
	private String password;
	private boolean iflogin=false;
	
	private final String IPAddr="172.28.54.1";
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//Log.i("TEST","create");
		new Thread(new ActionService()).start(); 
		super.onCreate();
	}

	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//Log.i("TEST","start");
		Bundle bundle = intent.getExtras();

	    String Action = bundle.getString("action");
		if(Action.equals("REGISTER"))
			action=REGISTER;
		else if(Action.equals("LOGIN"))
			action=LOGIN;
		else if(Action.equals("BACKUPS"))
			action=BACKUPS;
		else if(Action.equals("RECOVERY"))
			action=RECOVERY;
		
		
		//ע��͵�½ʱ����Ҫ��Activity��ȡ�û��˺���Ϣ
		if(action==REGISTER||action==LOGIN)
		{
			username=bundle.getString("username");
			password=bundle.getString("password");
		}
		//���ݺͻ�ӭʱ����Ҫ��sharedpreference��ȡ�û��˺���Ϣ
		else
		{
			CurrentUser current=new CurrentUser(ClientService.this);
			//current.Login(2, "ssh", "123456");
			User user=current.QueryCurrentUser();
			
			if(user!=null)
			{
				username=user.getUsersName();
				password=user.getPassWord();
				userid=user.getUserId();
				
				Log.i("TEST","username:"+username);
				Log.i("TEST","password:"+password);
				Log.i("TEST","userid:"+userid);
			}
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	class ActionService implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (flag)
			{
			
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					if(action!=FREE)
					{
						Intent intent=new Intent();
						int Ret=ACTION_ERROR;
						
						
						try {
								switch(action){
									case REGISTER:
										//setAction�����ڲ���ǰ�棬���ֲ������ܴ����쳣
										intent.setAction("REGISTER");
										Ret=Register(username,password);
										break;
									case LOGIN:
										intent.setAction("LOGIN");
										Ret=Login(username,password);
										break;
									case BACKUPS:
										intent.setAction("BACKUPS");
										Ret=BackupData(userid,username,password);
										break;
									case RECOVERY:
										intent.setAction("RECOVERY");
										Ret=RecoveryData(userid,username,password);
										break;
									default:break;
							
									}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							
							Ret=CONNECT_ERROR;
							
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						
						
						intent.putExtra("Result", Ret);
						sendBroadcast(intent);
					
						action=FREE;
				

					}
				
			}
			
			
		}
		
	}
	
	
	private byte[] ReadStream(InputStream inStream) throws IOException{
		byte[] buffer=new byte[1024];

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();


        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {

                outStream.write(buffer, 0, len);

        }
        byte[] data;
        data = outStream.toByteArray();
        inStream.close();
        outStream.close();
        
        return data;
	}
	
	
	public long findLoIdByReId(ArrayList<IdRefer> list,long remoteId){
		if(list==null)
			return -1;
		
		for(IdRefer idRefer:list)
		{
			if(idRefer.getRemoteId()==remoteId)
				return idRefer.getLocalId();
		}
		
		
		return -1;
	}
	
	
	private int Register(String username, String password) throws IOException{
		URL url = new URL("http://"+IPAddr+":8080/Server/RegisterServlet"+"?username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200)
		{
			InputStream inStream=conn.getInputStream();
			String Result=new String(ReadStream(inStream));
			Log.i("TEST","ret:"+Result);
            
			if(Result.equals("ok"))
            	return SUCCESS;
            else
            	return ACTION_ERROR;
			
		}
		else
			return CONNECT_ERROR;
		
	}
	
	private int Login(String username, String password) throws IOException
	{
		URL url = new URL("http://"+IPAddr+":8080/Server/LoginServlet"+"?username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200)
		{
			InputStream inStream=conn.getInputStream();
			String Result=new String(ReadStream(inStream));
			Log.i("TEST","ret:"+Result);
			if(Result.equals("error"))
				return ACTION_ERROR;
            else
            {
            	CurrentUser current=new CurrentUser(ClientService.this);
            	long userId=Long.valueOf(Result);
            	current.Login(userId, username, password);
            	
            	return SUCCESS;
            }
            
		}
		else
			return CONNECT_ERROR;
	}
	
	
	private int BackupData(long userId,String username, String password) throws IOException, JSONException{
		
		BackupBeatsData(userId,username,password);
		BackupSportsData(userId,username,password);

		BackupRecordData(userId,username,password);
		return SUCCESS;
	}
	
	
	private int BackupBeatsData(long userId,String username, String password) throws IOException, JSONException{
		
		URL url = new URL("http://"+IPAddr+":8080/Server/BackupsServlet?datatype=beats"+"&username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);

		
		
		conn.setDoOutput(true);//�����������
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", "Fiddler");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setRequestProperty("Charset", "UTF-8");
		
	  
	    //��ȡ�û�ID
	  	DBManager db=new DBManager(ClientService.this);
	  	//��ȡ�����б�
	  	ArrayList<Beats> beatslist=db.getBeatsList();
	  	
	  	JSONArray jsonArray=new JSONArray();
	  	for(Beats beats:beatslist)
	  	{
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("beatId", beats.getBeatId());
	  		jsonObject.put("beats", beats.getBeats());
	  		jsonObject.put("date", beats.getDate());
	  		jsonObject.put("type", beats.getType());
	  		jsonObject.put("userId", beats.getUserId());
	  		jsonArray.put(jsonObject);
	  	}
	  	
	  	
        OutputStream out = conn.getOutputStream();
		out.write(jsonArray.toString().getBytes());
		out.close();
        

		
		return SUCCESS;
	}
	
	private int BackupSportsData(long userId,String username, String password) throws IOException, JSONException{
		
		URL url = new URL("http://"+IPAddr+":8080/Server/BackupsServlet?datatype=sports"+"&username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);
		
		
		
		conn.setDoOutput(true);//�����������
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", "Fiddler");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setRequestProperty("Charset", "UTF-8");
		
	  
	    //��ȡ�˶��б�
	  	DBManager db=new DBManager(ClientService.this);
	  
	  	ArrayList<Sports> sportslist=db.getSportList();
	  		
	  	JSONArray sportsArray=new JSONArray();
	  	for(Sports sports:sportslist)
	  	{
	  			JSONObject jsonObject = new JSONObject();
	  			jsonObject.put("sportsID", sports.getSportsID());
	  			jsonObject.put("Date", sports.getDate());
	  			jsonObject.put("type", sports.getType());
	  			jsonObject.put("userId", sports.getUserId());
	  			jsonObject.put("num", sports.getNum());
	  			jsonObject.put("ValidRate", sports.getValidRate());
	  			jsonObject.put("perfectRate", sports.getPerfectRate());
	  			jsonObject.put("grade", sports.getGrade());
	  			jsonObject.put("MaxSpeed", sports.getMaxSpeed());
	  			jsonObject.put("AVGSpeed", sports.getAVGSpeed());
	  			jsonObject.put("distance", sports.getDistance());
	  			jsonObject.put("duration", sports.getDuration());
	  			sportsArray.put(jsonObject);
	  	}
	  	
	  	
	  	
	  	//Log.i("TEST","json"+sportslist.size());
	  	//Log.i("TEST","json"+jsonArray.toString());
	  	
	  	ArrayList<Position> positionlist=db.getPositionsByUerId(userId);
  		
	  	JSONArray positionArray=new JSONArray();
	  	for(Position position:positionlist)
	  	{
	  			JSONObject jsonObject = new JSONObject();
	  			jsonObject.put("time", position.getTime());
	  			jsonObject.put("latitude", position.getLatitude());
	  			jsonObject.put("longitude", position.getLongitude());
	  			jsonObject.put("sportId", position.getSportId());
	  			jsonObject.put("positionId", position.getPositionId());
	  			positionArray.put(jsonObject);
	  	}
	  	
	  	//Log.i("TEST","json"+jsonArray2.toString());
	  	
	  	String outStr=sportsArray.toString()+"[||]"+positionArray.toString();
	  	
        OutputStream out = conn.getOutputStream();
		out.write(outStr.getBytes());
		out.close();
        

		return SUCCESS;
	}	
	
	
	
	
	
	
	private int BackupRecordData(long userId,String username, String password) throws IOException, JSONException{
		
		URL url = new URL("http://"+IPAddr+":8080/Server/BackupsServlet?datatype=record"+"&username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);

		
		
		conn.setDoOutput(true);//�����������
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", "Fiddler");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setRequestProperty("Charset", "UTF-8");
		
	  
	    //��ȡ��¼
	  	DBManager db=new DBManager(ClientService.this);
	
	  	Record record=db.queryRecord();
	  	JSONArray jsonArray=new JSONArray();
	  	
	  	JSONObject jsonObject = new JSONObject();
	  	jsonObject.put("recordId", record.getUserId());
	  	jsonObject.put("AVGSpeed", record.getAVGSpeed());
	  	jsonObject.put("numPushUp", record.getNumPushUp());
	  	jsonObject.put("numSitUp", record.getNumSitUp());
	  	jsonObject.put("validRateSitUp", record.getValidRateSitUp());
	  	jsonObject.put("validRatePushUp", record.getValidRatePushUp());
	  	jsonObject.put("distance", record.getDistance());
	  	jsonObject.put("AVGPace", record.getAVGPace());
	  	jsonObject.put("perfectRatePushUp", record.getPerfectRatePushUp());
	  	jsonObject.put("perfectRateSitUp", record.getPerfectRateSitUp());
	  	jsonObject.put("gradePushUp", record.getGradePushUp());
	  	jsonObject.put("gradeSitUp", record.getGradeSitUp());
	  	jsonObject.put("TotalDistance", record.getTotalDistance());
	  	jsonObject.put("totalNumPushUp", record.getTotalNumPushUp());
	  	jsonObject.put("totalNumSitUp", record.getTotalNumSitUp());
	  	jsonObject.put("steps", record.getSteps());
	  	jsonObject.put("userId", record.getUserId());
	  	jsonArray.put(jsonObject);
	  	
        OutputStream out = conn.getOutputStream();
		out.write(jsonArray.toString().getBytes());
		out.close();
        

		return SUCCESS;
	}	
		
	
	
	private  int RecoveryData(long userId,String username, String password) throws MalformedURLException, IOException, XmlPullParserException, JSONException {
		// TODO Auto-generated method stub
		
	
	
		RecoveryBeatsData(userId,username,password);
		RecoverySportsData(userId,username,password);

		RecoveryRecordData(userId,username,password);
		return SUCCESS;
	}
	
	
	private  int RecoveryBeatsData(long userId,String username, String password) throws MalformedURLException, IOException, XmlPullParserException, JSONException {
		// TODO Auto-generated method stub
		
	
		URL url = new URL("http://"+IPAddr+":8080/Server/RecoveryServlet?datatype=beats"+"&username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");

		if(conn.getResponseCode()==200)
		{
			InputStream inStream=conn.getInputStream();

            String jsonStr=new String(ReadStream(inStream),"UTF-8");
           // Log.i("TEST","json"+jsonStr);

            
            DBManager db=new DBManager(ClientService.this);
            
            db.removeAllBeats(userId);
            
            JSONArray array = new JSONArray(jsonStr);  
            
            int length = array.length();  
            Log.i("TEST","length"+length);
            for(int i=0;i<length;i++){  
                  	JSONObject object = array.getJSONObject(i); 
	          		
                    Beats beats=new Beats();
	          		beats.setBeatId(object.getLong("beatId"));
	          		beats.setBeats(object.getInt("beats"));
	          		beats.setDate(object.getString("date"));
	          		beats.setType(object.getInt("type"));
	          		beats.setUserId(object.getLong("userId"));
	          		db.insertBeats(beats);
            }  


		}
	
			return SUCCESS;
	}
	
	
	private  int RecoverySportsData(long userId,String username, String password) throws MalformedURLException, IOException, XmlPullParserException, JSONException {
		// TODO Auto-generated method stub
		
	
		URL url = new URL("http://"+IPAddr+":8080/Server/RecoveryServlet?datatype=sports"+"&username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");

		if(conn.getResponseCode()==200)
		{
			InputStream inStream=conn.getInputStream();
			
            String jsonStr=new String(ReadStream(inStream),"UTF-8");
            
            DBManager db=new DBManager(ClientService.this);
            
            db.removeAllSports(userId);
            
            String sportsJson=jsonStr.substring(0, jsonStr.indexOf("[||]"));
            String positionJson=jsonStr.substring(jsonStr.indexOf("[||]")+4,jsonStr.length());
   		 
            ArrayList<IdRefer> IdReferList=new ArrayList<IdRefer>();
           
            Log.i("TEST","json"+sportsJson.toString());
            JSONArray sportsArray = new JSONArray(sportsJson);  
            int length = sportsArray.length();  
            for(int i=0;i<length;i++){  
                JSONObject object = sportsArray.getJSONObject(i); 
        		//����˶���
        		Sports sports=new Sports();
        		sports.setSportsID(object.getLong("sportsID"));
        		sports.setDate(object.getString("Date"));
        		
        		sports.setType(object.getInt("type"));
        		sports.setUserId(object.getLong("userId"));
        		sports.setNum(object.getInt("num"));
        	
        		sports.setAVGSpeed((float)object.getDouble("AVGSpeed"));
        		sports.setValidRate((float)object.getDouble("ValidRate"));
        		sports.setPerfectRate((float)object.getDouble("perfectRate"));
        		sports.setGrade((float)object.getDouble("grade"));
        		sports.setMaxSpeed((float)object.getDouble("MaxSpeed"));
        		sports.setDistance(object.getInt("distance"));
        		sports.setDuration(object.getString("duration"));
        		
        		long remoteId=object.getLong("sportsID");
        		long localId=db.insertSport(sports);
        		
        		IdReferList.add(new IdRefer(localId,remoteId));
                
        	
            }  
            
            Log.i("TEST","json"+positionJson.toString());
            JSONArray positionArray = new JSONArray(positionJson);  
            length = positionArray.length();  
            for(int i=0;i<length;i++){  
                JSONObject object = positionArray.getJSONObject(i); 
        		//����˶���
        		Position position=new Position();
        		
        		position.setTime(object.getLong("time"));
        		
        		position.setLatitude(object.getDouble("latitude"));
        		position.setLongitude(object.getDouble("longitude"));
        		position.setSportId(object.getLong("sportId"));
        		position.setPositionId(object.getLong("positionId"));
        		
        		long remoteId=position.getSportId();
        		long localId=findLoIdByReId(IdReferList, remoteId);
        		
        		position.setSportId(localId);
        		
        		db.insertPosition(position);
        
                
        	
            }  

            


		}
	
			return SUCCESS;
	}
	
	
	
	
	
	
	

	
	
	
	
	
	
	private  int RecoveryRecordData(long userId,String username, String password) throws MalformedURLException, IOException, XmlPullParserException, JSONException {
		// TODO Auto-generated method stub
		
	
		URL url = new URL("http://"+IPAddr+":8080/Server/RecoveryServlet?datatype=record"+"&username="+username+"&password="+password);  
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();

		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");

		if(conn.getResponseCode()==200)
		{
			InputStream inStream=conn.getInputStream();

            String jsonStr=new String(ReadStream(inStream),"UTF-8");
            Log.i("TEST","json"+jsonStr);
            DBManager db=new DBManager(ClientService.this);
           
            db.removeRecord(userId);
            
            
            JSONArray array = new JSONArray(jsonStr);  
            int length = array.length();  
            for(int i=0;i<length;i++){  
                JSONObject object = array.getJSONObject(i); 
        		//��Ӽ�¼��
        		Record record=new Record();
        		record.setRecordId(object.getInt("recordId"));
        		record.setUserId(object.getLong("userId"));
        		record.setAVGPace(object.getInt("AVGPace"));
        		record.setAVGSpeed(object.getInt("AVGSpeed"));
        		record.setDistance(object.getInt("distance"));
        		record.setGradePushUp(object.getInt("gradePushUp"));
        		record.setGradeSitUp(object.getInt("gradeSitUp"));
        		record.setNumPushUp(object.getInt("numPushUp"));
        		record.setNumSitUp(object.getInt("numSitUp"));
        		record.setPerfectRatePushUp(object.getInt("perfectRatePushUp"));
        		record.setPerfectRateSitUp(object.getInt("perfectRateSitUp"));
        		record.setSteps(object.getInt("steps"));
        		record.setTotalDistance(object.getInt("TotalDistance"));
        		record.setTotalNumPushUp(object.getInt("totalNumPushUp"));
        		record.setTotalNumSitUp(object.getInt("totalNumSitUp"));
        		record.setValidRatePushUp(object.getInt("validRatePushUp"));
        		record.setValidRateSitUp(object.getInt("validRateSitUp"));
        	    db.insertRecord(record);
               

            }  


		}
	
			return SUCCESS;
	}
	
	
	

	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	

}
