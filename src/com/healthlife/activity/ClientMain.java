package com.healthlife.activity;

import com.healthlife.R;
import com.healthlife.R.id;
import com.healthlife.R.layout;
import com.healthlife.R.menu;
import com.healthlife.db.DBManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ClientMain extends Activity {
	
	
	  Button login_bt;
	  Button register_bt;
	  Button backups_bt;
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        
   
      
        
        login_bt=(Button) findViewById(R.id.login_bt);
        register_bt=(Button) findViewById(R.id.register_bt);
        backups_bt=(Button) findViewById(R.id.backups_bt);
        
        login_bt.setText("登陆");
        register_bt.setText("注册");
        backups_bt.setText("备份还原");
      
        login_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//MyService s=new MyService();
				Intent intent = new Intent(ClientMain.this, 
		        		LoginActivity.class); 
				startActivity(intent);
				//intent.putExtra("action", "RECOVERY");
				//intent.putExtra("action", "REGISTER");
				//intent.putExtra("action", "LOGIN");
				
				//intent.putExtra("action", "BACKUPS");
				
				//intent.putExtra("username", "1121");
				//intent.putExtra("password", "2222");
		        //startService(intent);
				
		     
				
				
			}
        	
        });
        
        register_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ClientMain.this, 
		        		RegisterActivity.class); 
				startActivity(intent);
				DBManager db=new DBManager(ClientMain.this);
			}
        	
        });
        
        backups_bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ClientMain.this, 
		        		BackupsActivity.class); 
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
