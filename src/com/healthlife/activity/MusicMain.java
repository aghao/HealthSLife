package com.healthlife.activity;

import com.healthlife.R;
import com.healthlife.util.MusicToPlay;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MusicMain extends Activity {





	//private static int count=0;

	private ImageButton musicbox_bt;
	private ImageButton ordermode_bt;
	private ImageButton intelmode_bt;
	
	private TextView current_mode;
	
	private ImageButton musiclist_bt;
	
	private ImageButton startmusic_bt;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music_main);
        musicbox_bt=(ImageButton)findViewById(R.id.musicbox_bt);
        
        ordermode_bt=(ImageButton)findViewById(R.id.ordermode_bt);
        intelmode_bt=(ImageButton)findViewById(R.id.intelmode_bt);
        
        current_mode=(TextView)findViewById(R.id.current_mode);
        
        musiclist_bt=(ImageButton)findViewById(R.id.musiclist_bt);
       
    
        startmusic_bt=(ImageButton)findViewById(R.id.startmusic_bt);
       
        
        Intent intent = new Intent(MusicMain.this, 
        		MusicService.class); 
        intent.setAction("PlayerControl");
        intent.putExtra("PlayerAction", "Prepare");
        startService(intent); 
      
        
        // Log.i("TEST","MusicMain");
        
        //musicplayer_bt.setText("音乐播放盒");
        musicbox_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(MusicMain.this, MusicPlayer.class);
                startActivity(intent);
			}
        	
        });
        
        //ordermode_bt.setText("顺序播放");
        ordermode_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MusicMain.this, 
		        		MusicService.class); 
				intent.setAction("ModeSetting");
				intent.putExtra("mode", "order_mode");
		        startService(intent);
		        current_mode.setText("顺序模式");
			}
        	
        });
        
        //intelmode_bt.setText("智能播放");
        intelmode_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MusicMain.this, 
		        		MusicService.class); 
			    intent.setAction("ModeSetting");
				intent.putExtra("mode", "intel_mode");
		        startService(intent);
		        current_mode.setText("智能模式");
		        
		        //test
//		        count=count+1;
//		        Intent newintent=new Intent();
//				newintent.setAction("com.healthlife.activity.SitUpActivity.MotionAdd");
//				newintent.putExtra("motionNum", count);
//				sendBroadcast(newintent);
//		        
		        //current_mode.setText("智能模式");
		        //test
			}
        	
        });
        
       // musiclist_bt.setText("音乐列表");
        musiclist_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MusicMain.this, 
		        		MusicList.class); 
				//intent.putExtra("mode", "intel_mode");
		        startActivity(intent);
			}
        	
        });
        
        //startmusic_bt.setText("播放音乐");
        startmusic_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MusicMain.this, 
		        		MusicService.class); 
				intent.setAction("PlayerControl");
				Log.i("TEST","state:"+MusicToPlay.GetMediaState());
				if(MusicToPlay.GetMediaState()==MusicToPlay.PLAYING)
				{
					intent.putExtra("PlayerAction", "Pause");
					startmusic_bt.setBackgroundResource(R.drawable.qstartmusic_bt);
					
				}
				else
				{
					intent.putExtra("PlayerAction", "Play");
					startmusic_bt.setBackgroundResource(R.drawable.qpausemusic_bt);
				}
		        startService(intent);
					
			}
        	
        });
       
    }
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		Log.i("TEST","state:"+MusicToPlay.GetMediaState());
		 if(MusicToPlay.GetMediaState()==MusicToPlay.PLAYING)
	        	startmusic_bt.setBackgroundResource(R.drawable.qpausemusic_bt);
	        else
	        	startmusic_bt.setBackgroundResource(R.drawable.qstartmusic_bt);
		 
		 ShowMusicMode();
	}
    
    
	public void ShowMusicMode(){
		SharedPreferences mySharedPreferences= this.getSharedPreferences("MusicMode",
				Activity.MODE_PRIVATE); 
		// 使用getString方法获得value，注意第2个参数是value的默认值 
		int music_mode =mySharedPreferences.getInt("MusicMode", 0);
		if(music_mode==0)
			current_mode.setText("顺序模式");
		else if(music_mode==1)
			current_mode.setText("智能模式");
	}

	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
