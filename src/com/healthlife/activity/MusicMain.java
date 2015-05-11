package com.healthlife.activity;

import com.healthlife.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MusicMain extends Activity {

	private Button button;
	private Button order_mode;
	private Button intel_mode;
	
	private TextView current_mode;
	
	private Button musiclist_bt;
	
	private EditText music_pace;
	private Button changepace_bt;
	
	private Button clienttest_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music_main);
        button=(Button)findViewById(R.id.button);
        
        order_mode=(Button)findViewById(R.id.ordermode_bt);
        intel_mode=(Button)findViewById(R.id.intelmode_bt);
        
        current_mode=(TextView)findViewById(R.id.current_mode);
        
        musiclist_bt=(Button)findViewById(R.id.musiclist_bt);
        current_mode.setText("顺序模式");
        
        music_pace=(EditText)findViewById(R.id.music_pace);
        changepace_bt=(Button)findViewById(R.id.changepace_bt);
        
        clienttest_bt=(Button)findViewById(R.id.clienttest_bt);
        clienttest_bt.setText("服务器客户端");
        Intent intent = new Intent(MusicMain.this, 
        		MusicService.class); 
        intent.setAction("ModeSetting");
        intent.putExtra("mode", "order_mode");
        startService(intent); 
       
        
        button.setText("音乐播放盒");
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(MusicMain.this, MusicPlayer.class);
                startActivity(intent);
			}
        	
        });
        
        order_mode.setText("顺序播放");
        order_mode.setOnClickListener(new OnClickListener(){

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
        
        intel_mode.setText("智能播放");
        intel_mode.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MusicMain.this, 
		        		MusicService.class); 
				intent.setAction("ModeSetting");
				intent.putExtra("mode", "intel_mode");
		        startService(intent);
		        current_mode.setText("智能模式");
			}
        	
        });
        
        musiclist_bt.setText("音乐列表");
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
        
        
        changepace_bt.setText("变更音乐节奏");
        changepace_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pace=music_pace.getText().toString();
				if(pace.length()>0)
				{
					Intent intent = new Intent(MusicMain.this, 
			        		MusicService.class); 
					intent.setAction("PaceSetting");
					//pace的可选值 1,2,3,4,5
					intent.putExtra("Pace", pace);
			        startService(intent);
				}
				
				
			}
        	
        });
        
        clienttest_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					Intent intent = new Intent(MusicMain.this, 
			        		ClientMain.class); 
					 startActivity(intent);
				
				
				
			}
        	
        });
    }
    

    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
