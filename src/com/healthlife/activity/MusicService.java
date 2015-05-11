package com.healthlife.activity;



import java.util.ArrayList;
import java.util.Random;

import com.healthlife.db.DBManager;
import com.healthlife.entity.Music;
import com.healthlife.util.MusicToPlay;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;



public class MusicService extends Service {
	
	private boolean flag=true;

	private int music_mode;
	private final int ORDER_MODE=0;
	private final int INTEL_MODE=1;
	
	private int music_pace=2;
	
	private ArrayList<Music> music_list=new ArrayList<Music>(); 
	private ArrayList<Integer> index_list=new ArrayList<Integer>();
	private int music_index=0;
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//Log.i("TEST","onCreate");
		super.onCreate();
		
		
		
		RefreshMusicList();
		setMusicmode(INTEL_MODE);
			
		if(MusicToPlay.firstplay)
	    {
			NextMusic();	
	        MusicToPlay.firstplay=false;
	     }
	       
		//Log.i("TEST","onCreate");
    		 
		new Thread(new PlayMusic()).start();  
		
		MusicToPlay.mediaPlayer.setOnCompletionListener(new MusicCompletion());  
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
		else if(action.equals("PaceSetting"))
		{
			Bundle bundle = intent.getExtras();
		    String rateStr = bundle.getString("Pace");
		    if(rateStr.length()>0)
		    {
		    	music_pace=Integer.valueOf(rateStr);
		    	
		    }
		    
		    if(music_mode==INTEL_MODE)
		    {
		    	MusicToPlay.if_music_change=true;
	        	
	        	NextMusic();
		    }
		    	
		    
		}
		else if(action.equals("RefreshMusicList"))
		{
			RefreshMusicList();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	
	/**
	 * ��̨�л�����
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
	 * ����������������¼�
	 * @author Maniger
	 *
	 */
    class MusicCompletion implements MediaPlayer.OnCompletionListener{

   	 @Override  
        public void onCompletion(MediaPlayer mp) {  
        
   		 	MusicToPlay.if_music_change=true;
        	
        	NextMusic();
        }  
   	
    }
	
    
    
    

    /**
     * �������ֲ���ģʽ
     * @param musicmode  ORDER_MODEΪ˳�򲥷ţ�INTEL_MODEΪ���ܲ���
     */
	private void setMusicmode(int musicmode) {
		this.music_mode = musicmode;
	}
    
	
	
	
	
	/**
	 * ��ȡ��һ�׸������
	 * @return
	 */
	private int GetNextMusicIndex(){
		 int index=-1;
		 //�����˳�򲥷�ģʽ
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
	     //��������ܲ���ģʽ
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
		    	 //�������������ѡȡ����һ�׸������ǵ�ǰ����
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
	
	
	
	
	/**
	 * ˢ�¸��������б�
	 */
    private void RefreshMusicList(){
    	
    	
    	music_list.clear();
    	
    	DBManager database=new DBManager(MusicService.this);
    
		music_list=database.getActivedMusicList();

    }
    
 
    
    
    /**
     * �л�����һ�׸�
     */
    private void NextMusic(){
    	
   	   //���� 
       if(!MusicToPlay.if_music_change)
    	   return;
	   
       
       MusicToPlay.if_music_change=false;
	       	
	  int index=GetNextMusicIndex();
	  if(index>=0)
	  {
		   Music music=music_list.get(index);
		
		   MusicToPlay.musicpath=music.filepath; 	   
	       
		   
	       //���ò��������������·��������
	       MusicToPlay.ResetMusic();
		   MusicToPlay.mediaPlayer.start();
	  }
	  else
	  {
		  MusicToPlay.mediaPlayer.reset();
		  MusicToPlay.musicpath="blank";
	  }
	   
	   MusicToPlay.if_lyric_change=true;
	        
 
       
   }
   
    
    
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		//Log.i("TEST","onBind");
		return null;
	}

	
	
	
	


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//Log.i("TEST","onDestroy");
		super.onDestroy();
	}

	

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		//Log.i("TEST","onUnbind");
		return super.onUnbind(intent);
	}
	
	
	

}
