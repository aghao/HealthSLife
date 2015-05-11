package com.healthlife.activity;


import com.healthlife.R;
import com.healthlife.util.LyricView;
import com.healthlife.util.MusicToPlay;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MusicPlayer extends Activity {
	
	 
	 private LyricView lyricView;  
	 
	 
	 private ImageButton play_bt;
	 private ImageButton stop_bt;
	 private TextView musicprogress;
	 private SeekBar seekBar;  
	
	 private int INTERVAL=45;//歌词每行的间隔  
	    
	 private ImageButton changesongs;

	 /**
	  * 标志位：是否实时刷新歌词
	  */
	 private boolean flag=true;
	
	 //处理频繁换歌的冲突
	 private static int change_state=0;
	 private static final int WAIT_CHANGE=0;
	 private static final int FINISH_CHANGE=1;
	 private static final int START_CHANGE=2;
	 //处理换歌和改变播放进度的冲突
	 private boolean changemusic_lock=false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_player);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        
       // MusicToPlay.musicpath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/爱笑的眼睛.mp3"; 
       // mp3Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/爱笑的眼睛.mp3";  
  
        lyricView = (LyricView) findViewById(R.id.mylrc);  
        
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
      
        SearchLrc();  
        
        
            
        play_bt = (ImageButton) findViewById(R.id.play_bt);  
       // play_bt.setText("播放");  
        
        stop_bt=(ImageButton)findViewById(R.id.stop_bt);
        
        changesongs=(ImageButton)findViewById(R.id.changesongs_bt);
       // changesongs.setText("换歌");
        
        musicprogress=(TextView)findViewById(R.id.musicprogress);
        
        seekBar = (SeekBar) findViewById(R.id.seekbarmusic);  
       
        
       
        seekBar.setOnSeekBarChangeListener(new ChangeProcess());  
         
        
        
        play_bt.setOnClickListener(new PlayMusic());  
        
        stop_bt.setOnClickListener(new StopMusic());
        
        
        changesongs.setOnClickListener(new ChangeMusic());
        
        
        seekBar.setMax(getMusicDuration()); 
        
	}
	
	
	
	
	
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
			
	}


	 @Override
	protected void onStart() {
	// TODO Auto-generated method stub
		super.onStart();
		flag=true;
		new Thread(new RollLyric()).start(); 
		
		
		
		if (isMusicPlaying()) {  
          
          	play_bt.setBackgroundResource(R.drawable.pausemusic_bt);
          
          } 
	}
 
	 
	 
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		flag=false;
	}
	
	
	
	
	
	public boolean isMusicPlaying()
	{
		if(!MusicToPlay.musicpath.equals("blank"))
			return MusicToPlay.mediaPlayer.isPlaying();
		else
			return false;
	}
	public void startMusic()
	{
		if(!MusicToPlay.musicpath.equals("blank"))
			MusicToPlay.mediaPlayer.start();
	}
	public void pauseMusic()
	{
		if(!MusicToPlay.musicpath.equals("blank"))
			MusicToPlay.mediaPlayer.pause();  
	}
	public void MusicSeekto(int position)
	{
		if(!MusicToPlay.musicpath.equals("blank"))
			MusicToPlay.mediaPlayer.seekTo(position);
	}
	public int getMusicDuration()
	{
		if(!MusicToPlay.musicpath.equals("blank"))
			return MusicToPlay.mediaPlayer.getDuration();
		else
			return 0;
	}
	public int getMusicCurrentPosition()
	{
		if(!MusicToPlay.musicpath.equals("blank"))
			return MusicToPlay.mediaPlayer.getCurrentPosition();
		else
			return 0;
	}
	
	
    
    class PlayMusic implements OnClickListener{

		@Override
		public void onClick(View v) {
			 // TODO Auto-generated method stub  
            if (isMusicPlaying()) {  
            	//play_bt.setText("播放"); 
            	play_bt.setBackgroundResource(R.drawable.playmusic_bt);
            	
            	pauseMusic(); 
            } else {  
            	//play_bt.setText("暂停");  
            	play_bt.setBackgroundResource(R.drawable.pausemusic_bt);
            	startMusic(); 
              //  lyricView.setOffsetY(320 - lyricView.SelectIndex(getMusicCurrentPosition())  
              //          * (lyricView.getSIZEWORD() + INTERVAL-1));  

            }  
		}
    	
    }
    
    
    
    
    
    class StopMusic implements OnClickListener{

		@Override
		public void onClick(View v) {
			 // TODO Auto-generated method stub
			 pauseMusic();
			 MusicSeekto(0);
			 seekBar.setProgress(0);
			// play_bt.setText("播放");
			 play_bt.setBackgroundResource(R.drawable.playmusic_bt);
			 lyricView.setOffsetY(320 - lyricView.SelectIndex(0)   
                     * (lyricView.getSIZEWORD() + INTERVAL-1)); 
			 
		}
    	
    }
    
    
    
    
    
    class ChangeMusic implements OnClickListener{

    	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
    		if(change_state==WAIT_CHANGE)
    		{
    			changemusic_lock=true;
    			change_state=START_CHANGE;
    			MusicToPlay.if_music_change=true;
    			
    		}
	
		}
    	
    }
    
    
    
    
    
    
    
    class ChangeProcess implements OnSeekBarChangeListener{

    
        @Override  
        public void onProgressChanged(SeekBar seekBar, int progress,  
                boolean fromUser) {  
            // TODO Auto-generated method stub  
        	if(!changemusic_lock)
        	{
            if (fromUser) {  
            	MusicSeekto(progress);  
                lyricView.setOffsetY(320 - lyricView.SelectIndex(progress)   
                        * (lyricView.getSIZEWORD() + INTERVAL-1));  

            }  
            
             int duration=getMusicDuration();
             duration=duration/1000;
			 String durationStr=String.format("%02d:%02d",duration/60,duration%60);
			 
			 
             int current=getMusicCurrentPosition();
             current=current/1000;

			 String currentStr=String.format("%02d:%02d",current/60,current%60);
			 musicprogress.setText(currentStr+"/"+durationStr);
        	}
        }  
    	
    	@Override  
        public void onStopTrackingTouch(SeekBar seekBar) {  
            // TODO Auto-generated method stub  

        }  

        @Override  
        public void onStartTrackingTouch(SeekBar seekBar) {  
            // TODO Auto-generated method stub  

        }  

    }
    
    
    
    
    
    private void SearchLrc() {  
        String lrc = MusicToPlay.musicpath;  
        lrc = lrc.substring(0, lrc.length() - 4).trim() + ".lrc".trim();  
        LyricView.read(lrc);  
        lyricView.SetTextSize();  
        
        lyricView.setOffsetY(320 - lyricView.SelectIndex(getMusicCurrentPosition())   
                    * (lyricView.getSIZEWORD() + INTERVAL-1)*2);
        
        
    }  
    
    
    
    
    private void UpdateLyric(){
    	
    	 if(MusicToPlay.if_lyric_change)
    	 {
	        MusicToPlay.if_lyric_change=false;

	        
	        SearchLrc(); 
	         
	        	
	        lyricView.setOffsetY(320 - lyricView.SelectIndex(getMusicCurrentPosition())  
	                   * (lyricView.getSIZEWORD() + INTERVAL-1)); 
	        	
	        	
	         seekBar.setMax(getMusicDuration());  
	        
	         change_state=FINISH_CHANGE;
	         
    	 }
    }
    
    
    
    
    
    class RollLyric implements Runnable {  
  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            while (flag) {  
  
                try {  
                    Thread.sleep(100);  
                    
                    if(change_state==WAIT_CHANGE)
                    	changemusic_lock=false;
                    
                    //这两条语句必须在UpdateLyric()之前，因为要确保更换音乐的一切操作完成
                    if(change_state==FINISH_CHANGE)
                    	change_state=WAIT_CHANGE;
                    
                    //更新歌词                    
                    UpdateLyric();
                    
                    if(change_state==WAIT_CHANGE)
                    {
	                    // 滚动歌词    
	                    if (isMusicPlaying()) {  
	                        
	                    	lyricView.setOffsetY(lyricView.getOffsetY() - lyricView.SpeedLrc()); 	                      
	                        lyricView.SelectIndex(getMusicCurrentPosition());  
	                   
	                    }  
	                    seekBar.setProgress(getMusicCurrentPosition());  
	                    mHandler.post(mUpdateResults);
	                   
                    }
                    
                   
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } 
                
                
            }  
        }  
    }  
  
    
    
    Handler mHandler = new Handler();  
    Runnable mUpdateResults = new Runnable() {  
        public void run() {  
        	
            lyricView.invalidate(); // 更新视图  
      
        }  
    };  

	
    
    
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_player, menu);
		return true;
	}
	
	


}
