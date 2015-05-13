package com.healthlife.activity;



import java.util.ArrayList;
import java.util.Random;

import com.healthlife.db.DBManager;
import com.healthlife.entity.Music;
import com.healthlife.util.MusicToPlay;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;



public class MusicService extends Service {
	
	private boolean flag=true;

	private int music_mode=0;
	private final int ORDER_MODE=0;
	private final int INTEL_MODE=1;
	
	private String music_name="blank";
	private int music_oldpace=0;
	private int music_pace=0;
	private int motionNum=0;
	
	private ArrayList<Music> music_list=new ArrayList<Music>(); 
	private ArrayList<Integer> index_list=new ArrayList<Integer>();
	private int music_index=-1;
	
	private ArrayList<Integer> pace_list=new ArrayList<Integer>();
	
	private BroadcastReceiver pace_receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			motionNum=arg1.getIntExtra("motionNum", 0);
			//Bundle bundle = arg1.getExtras();
			//String s=bundle.getString("Result");
			Log.i("TEST","receive:"+motionNum);

		}
		
		
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//Log.i("TEST","onCreate");
		super.onCreate();

		RefreshMusicList();
	
    		 
		new Thread(new PlayMusic()).start();  
		new Thread(new ChangePace()).start();  
		
		MusicToPlay.mediaPlayer.setOnCompletionListener(new MusicCompletion());  
		
		GetMusicMode();
		registerReceiver(pace_receiver,new IntentFilter("com.healthlife.activity.SitUpActivity.MotionAdd"));
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//Log.i("TEST","onDestroy");
		super.onDestroy();
		unregisterReceiver(pace_receiver);
	}

	
	 /**
     * 设置音乐播放模式
     * @param musicmode  ORDER_MODE为顺序播放，INTEL_MODE为智能播放
     */
	private void setMusicmode(int musicmode) {
		this.music_mode = musicmode;
		SharedPreferences mySharedPreferences= this.getSharedPreferences("MusicMode",
				Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		editor.putInt("MusicMode", musicmode);
		editor.commit(); 
		Log.i("TEST","mode:"+musicmode);
	}
	
	
	private void GetMusicMode(){
		SharedPreferences mySharedPreferences= this.getSharedPreferences("MusicMode",
				Activity.MODE_PRIVATE); 
		// 使用getString方法获得value，注意第2个参数是value的默认值 
		music_mode =mySharedPreferences.getInt("MusicMode", ORDER_MODE);

		
	}
	
	
	/**
	 * 刷新歌曲播放列表
	 */
    private void RefreshMusicList(){
    	
    	
    	music_list.clear();
    	
    	DBManager database=new DBManager(MusicService.this);
    
		music_list=database.getActivedMusicList();

    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("TEST","onStartCommand");
		
		String action=intent.getAction();
		if(action.equals("ModeSetting"))
		{
			Bundle bundle = intent.getExtras();
		    String mode = bundle.getString("mode");
		   // Log.i("TEST",mode);
		    if(mode.equals("order_mode"))
		    	setMusicmode(ORDER_MODE);
		    else if(mode.equals("intel_mode"))
		    	setMusicmode(INTEL_MODE);
		}
//		else if(action.equals("PaceSetting"))
//		{
//			Bundle bundle = intent.getExtras();
//		    String rateStr = bundle.getString("Pace");
//		    if(rateStr.length()>0)
//		    {
//		    	music_pace=Integer.valueOf(rateStr);
//		    	
//		    }
//		    
//		    if(music_mode==INTEL_MODE)
//		    {
//		    	MusicToPlay.if_music_change=true;
//	        	
//	        	NextMusic();
//		    }
//		    	    
//		}
		else if(action.equals("PlayerControl"))
		{
			Bundle bundle = intent.getExtras();
		    String playerActionStr = bundle.getString("PlayerAction");
		    
		    if(playerActionStr.equals("Prepare"))
		    {
		    	
		    	 if(MusicToPlay.firstplay)
				 {
		    		 PrepareMusic();
				 }
		    }
		    else if(playerActionStr.equals("Play"))
		    {
		    	MusicToPlay.mediaPlayer.start();
		    	MusicToPlay.musicstate=MusicToPlay.PLAYING;
		    }
		    else if(playerActionStr.equals("DirectPlay"))
		    {
		    	if(music_mode==INTEL_MODE)
		    	{
		    		Intent newintent=new Intent();
		    		newintent.setAction("DirectPlayRet");
		    		newintent.putExtra("Ret", "fail");
		    		sendBroadcast(newintent);
		    	}
		    	else
		    	{
		    		String musicpath=bundle.getString("MusicPath");
		    		
		    		DirectPlay(musicpath);
		    		
		    		
		    	}
		    }
		    else if(playerActionStr.equals("Pause"))
		    {
		    	MusicToPlay.mediaPlayer.pause();
		    	MusicToPlay.musicstate=MusicToPlay.PAUSE;
		    }
		    
		}
		else if(action.equals("GetMediaInfo"))
		{
			Bundle bundle = intent.getExtras();
		    String MediaInfoStr = bundle.getString("Type");
		    //Log.i("TEST","MediaInfoStr"+MediaInfoStr);
			if(MediaInfoStr.equals("MusicName"))
		    {
				SendMusicName();
		    }
			
		}
		
		else if(action.equals("RefreshMusicList"))
		{
			RefreshMusicList();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private void DirectPlay(String musicpath)
	{
		Music music;
		for(int i=0;i<music_list.size();i++)
		{
			music=music_list.get(i);
			//Log.i("TEST","musicPath1:"+musicpath);
			//Log.i("TEST","musicPath2:"+music.getMusicPath());
			
			if(musicpath.equals(music.getMusicPath()))
			{
				
				music_index=i;
				break;
			}
			
		}
		MusicToPlay.musicpath=musicpath;
		
		MusicToPlay.ResetMusic();
		MusicToPlay.StartMusic();
		music=music_list.get(music_index);
		
		music_name=music.getMusicName();
		SendMusicName();
	}
	
	private void SendMusicName()
	{
		Intent newintent=new Intent();
		newintent.setAction("MusicName");
		newintent.putExtra("MusicName", music_name);
		sendBroadcast(newintent);
	}
	
	
	
	/**
	 * 根据运动节奏切换音乐
	 */
	private void ChangeMusicByPace()
	{
		if(pace_list.size()>=30)
		{
			pace_list.remove(0);
			pace_list.add(motionNum);
		}
		else
			pace_list.add(motionNum);
		
        
		int size=pace_list.size();
		int start=pace_list.get(0);
		int end=pace_list.get(size-1);
		double average=(double)(end-start)/(double)size;
		Log.i("TEST","count:"+(end-start));
		Log.i("TEST","AVE:"+average);
		//前10秒不做参考
		if(size<10)
		{
			music_pace=0;
			music_oldpace=0;
		}
		else
		{
			if(average<=0.25)
				music_pace=1;
			else if(average>0.25&&average<=0.5)
				music_pace=2;
			else if(average>0.5&&average<=0.75)
				music_pace=3;
			else if(average>0.75&&average<=1.0)
				music_pace=4;
			else if(average>1)
				music_pace=5;
		}
		//节奏改变就换歌
		if(music_pace!=music_oldpace)
		{
			MusicToPlay.if_music_change=true;
        	
        	NextMusic();
        	
        	music_oldpace=music_pace;
		}
	}
	
	/**
	 * 
	 * 根据用户运动节奏切换成符合节奏的音乐
	 *
	 */
	class ChangePace implements Runnable {  
		  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            while (flag) {  
  
                try {  
                    Thread.sleep(1000);  
                    
                    
                    if(music_mode==INTEL_MODE)
                    {
                    	
                    	ChangeMusicByPace();
                    	
                    }
                    
        			
                   
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	
	
	
	/**
	 * 后台切换音乐
	 * @author Maniger
	 *
	 */
	class PlayMusic implements Runnable {  
		  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            while (flag) {  
  
                try {  
                    Thread.sleep(100);  
                    //Log.i("TEST","123");
                    NextMusic();
                    
                   
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	
	
	
	
	/**
	 * 监听歌曲播放完成事件
	 * @author Maniger
	 *
	 */
    class MusicCompletion implements MediaPlayer.OnCompletionListener{

   	 @Override  
   	 	//一进入音乐盒就直接触发
        public void onCompletion(MediaPlayer mp) {  
   		 	
	   		MusicToPlay.if_music_change=true;

   		 	
        }  
   	
    }
	
    
    
    

   
    
	
	
	
	
	/**
	 * 获取下一首歌的索引
	 * @return
	 */
	private int GetNextMusicIndex(){
		 int index=-1;
		 //如果是顺序播放模式
	     if(music_mode==ORDER_MODE)
	     {
	    	if(music_list.size()>0)
	    	{
	    		music_index++;
		 		if(music_index>=music_list.size())
		 			music_index=0;
		 		
		 		index=music_index;
	    	}
	    	
	 		
	 		//Log.i("TEST","SUM:"+music_list.size());
	    	//Log.i("TEST","index:"+music_index);
	     }
	     //如果是智能播放模式
	     else if(music_mode==INTEL_MODE)
	     {
	    	
	    	 index_list.clear();
	    	 for(int i=0;i<music_list.size();i++)
	    	 {
	    		 if(music_list.get(i).pace==music_pace)
	    			 index_list.add(Integer.valueOf(i));
	    	
	    	 }
	    	 
	    	 if(index_list.size()>0)
	    	 {
	    		 Random random = new Random();
		    	 int referindex=random.nextInt(index_list.size());
		    	 //尽可能做到随机选取的下一首歌曲不是当前歌曲
		    	 while(index_list.size()>1&&music_index==index_list.get(referindex))
		    	 {
		    		 referindex=random.nextInt(index_list.size());
		    	 }
		    	 music_index=index_list.get(referindex);
		    	 index=music_index;
	    	 }
	    	
	    	 
	    	// Log.i("TEST","SUM:"+index_list.size());
	    	// Log.i("TEST","index:"+music_index);
	    	 
	     }
	     

		return index;
	}
	
	
	
	
	
    
 
    
    
    private void PrepareMusic(){
    	

 	       	
 	  int index=GetNextMusicIndex();
 	  if(index>=0)
 	  {
 		   Music music=music_list.get(index);
 		   MusicToPlay.musicpath=music.filepath; 	   
 	
 	  }
 	  else
 		  MusicToPlay.musicpath="blank";
 	
 	   
 	  
 
    }
    
    
    /**
     * 切换到下一首歌
     */
    private void NextMusic(){
    	
    	
   	   //换歌 
       if(!MusicToPlay.if_music_change)
    	   return;
	   
       
       MusicToPlay.if_music_change=false;
       MusicToPlay.musicstate=MusicToPlay.PLAYING;    	
       
	  int index=GetNextMusicIndex();
	  if(index>=0)
	  {
		   Music music=music_list.get(index);
		
		   MusicToPlay.musicpath=music.getMusicPath(); 	   
		   music_name=music.getMusicName();
		   
	       //重置播放器里面的音乐路径并播放
	       MusicToPlay.ResetMusic();
	      
		   MusicToPlay.mediaPlayer.start();
	  }
	  else
	  {
		  MusicToPlay.mediaPlayer.reset();
		  MusicToPlay.musicpath="blank";
	  }
	   
	   MusicToPlay.if_lyric_change=true;
	        
	   SendMusicName();
       
   }
   
    
    
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		//Log.i("TEST","onBind");
		return null;
	}

	
	
	
	


	

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		//Log.i("TEST","onUnbind");
		return super.onUnbind(intent);
	}
	
	
	

}
