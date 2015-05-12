package com.healthlife.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Music;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MusicList extends Activity {

	//记录数据库里面存在的全部音乐信息
	ArrayList<Music> all_music_data;
	//临时记录需要修改的音乐信息
	ArrayList<Music> temp_music_data;//注意修改前需要清空数据
	//记录数据库里面存在的待播放的音乐信息
	ArrayList<Music> all_activemusic_data;
	//记录搜索到的全部音乐文件路径
	ArrayList<String> all_music_path;
	
	ListView allmusic_list;
	ListView activemusic_list;
	ImageButton searchmusic_bt;
	ImageButton listenmusic_bt;
	boolean ifmultiselect=true;
	
	
	CheckBox checkbox;
	
	boolean if_longclick=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_music_list);
		
		
		
		TabHost tabHost=(TabHost)findViewById(R.id.tabhost);  
		tabHost.setup();  
		  
		TabSpec spec1=tabHost.newTabSpec("tab1");  
		spec1.setContent(R.id.tab1);  
		spec1.setIndicator("全部歌曲");  
		  
		TabSpec spec2=tabHost.newTabSpec("tab2");  
		spec2.setIndicator("播放列表");  
		spec2.setContent(R.id.tab2);  
		  
		  
		tabHost.addTab(spec1);  
		tabHost.addTab(spec2);  
		

		allmusic_list=(ListView)findViewById(R.id.allmusic_list);
		
		allmusic_list.setOnItemClickListener(new ShowMusicItemInfo());
		
		allmusic_list.setOnItemLongClickListener(new ModifyMusicItemInfo());
		
		activemusic_list=(ListView)findViewById(R.id.activemusic_list);
		
		searchmusic_bt=(ImageButton)findViewById(R.id.searchmusic_bt);
		//searchmusic_bt.setText("刷新歌曲");
		
		searchmusic_bt.setOnClickListener(new RefreshMusicList());
		
		listenmusic_bt=(ImageButton)findViewById(R.id.listenmusic_bt);
		//listenmusic_bt.setText("多选");
		listenmusic_bt.setOnClickListener(new MultiSelect());
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		all_music_path=new ArrayList<String>();
		all_music_data=new ArrayList<Music>();
		temp_music_data=new ArrayList<Music>();
		all_activemusic_data=new ArrayList<Music>();
		
		
		ReadMusicData();
		ShowMusicList();
		
		
	}
	
	/**
	 * 从数据库读取音乐文件数据
	 */
	private void ReadMusicData(){
	     DBManager database=new DBManager(MusicList.this);
	
		 all_music_data=database.getMusicList();
		 all_activemusic_data=database.getActivedMusicList();
	}
	
	
	
	/**
	 * 更新数据库里面的音乐文件数据
	 */
	private void UpdateMusicData(){
		DBManager database=new DBManager(MusicList.this);

		for(int i=0;i<temp_music_data.size();i++)
		{
		
			database.updateMusic(temp_music_data.get(i));
		}
		
		ReadMusicData();
		
		Intent intent = new Intent(MusicList.this, 
        		MusicService.class); 
		intent.setAction("RefreshMusicList");
        startService(intent);
	}

	

	/**
	 * 显示单个音乐项的基本信息(在对话框里显示)
	 * @author Maniger
	 *
	 */
	class ShowMusicItemInfo implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new Builder(MusicList.this);
			builder.setTitle("歌曲信息");
			//获取布局
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.dialog_getmusicinfo,
			     (ViewGroup) findViewById(R.id.getmusicinfo_dialog));
			builder.setView(layout);
			
			//获取歌曲项信息
			Music music=new Music(all_music_data.get(arg2));
			
			//获取控件信息
			TextView music_name=(TextView) layout.findViewById(R.id.dialog_musicname);
			TextView music_pace=(TextView) layout.findViewById(R.id.dialog_musicpace);
			TextView ifactice=(TextView) layout.findViewById(R.id.dialog_ifactive);
			//设置控件内容
			music_name.setText(music.getMusicName());
			music_pace.setText(""+music.getPace());
			if(1==music.isIfActive())
				ifactice.setText("是");
			else
				ifactice.setText("否");	
			
				
			builder.setPositiveButton("确认",null);
			builder.setNegativeButton("取消", null);
			//显示
			builder.show();

		}

		
	}
	
	
	/**
	 * 修改音乐项的基本信息(在对话框里面修改)
	 * @author Maniger
	 *
	 */
	class ModifyMusicItemInfo implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new Builder(MusicList.this);
			builder.setTitle("歌曲信息");
			//获取布局
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.dialog_modifymusicinfo,
			     (ViewGroup) findViewById(R.id.modifymusicinfo_dialog));
			
			final Music music=new Music(all_music_data.get(arg2));
			
			
			//显示音乐名
			final EditText music_name=(EditText)layout.findViewById(R.id.dialog_musicname_m);   
			music_name.setText(music.getMusicName());
			
			//显示音乐节奏
			final Spinner music_pace;
			ArrayList<String> list = new ArrayList<String>(); 
			ArrayAdapter<String> adapter;
	    	
	    	//给列表添加音乐节奏
	    	list.add("1");
	    	list.add("2");
	    	list.add("3");
	    	list.add("4");
	    	list.add("5");
	    	
	    	 //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项    
	    	music_pace = (Spinner)layout.findViewById(R.id.dialog_musicpace_m);    
	        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
	        adapter = new ArrayAdapter<String>(MusicList.this,R.layout.style_musicpacelist, list);    
	        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        //第四步：将适配器添加到下拉列表上    
	        music_pace.setAdapter(adapter);      
	        //设置音乐节奏
	        music_pace.setSelection(list.indexOf(""+music.getPace()), true);
			
	        //是否在播放列表
	        final CheckBox ifactive=(CheckBox)layout.findViewById(R.id.dialog_ifactive_m);
	        if(1==music.isIfActive())
	        	ifactive.setChecked(true);
	        else
	        	ifactive.setChecked(false);
	        
	        
	        
			builder.setView(layout);
			builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					music.setMusicName(music_name.getText().toString());
					TextView tempTextView=(TextView)music_pace.getChildAt(0);
					music.setPace(Integer.valueOf((String) tempTextView.getText()));
					if(ifactive.isChecked())
						music.setIfActive(1);
					else
						music.setIfActive(0);
					
					temp_music_data.clear();
					temp_music_data.add(music);
					UpdateMusicData();
					ShowMusicList();
				}
				
			});
			  
			builder.setNegativeButton("取消", null);

			builder.show();

			//下面的返回值很重要，能区分长按和短按
			return true;
		}
		
		
	}
	
	
	
	
	/**
	 * 将全部歌曲列表的音乐推到播放列表(勾选该音乐)
	 * @author Maniger
	 *
	 */
	class PushMusicToPlayList implements OnCheckedChangeListener{
		int position;
		public PushMusicToPlayList(int position)
		{
			this.position=position;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			
			Music music=new Music(all_music_data.get(position));
			
			if(isChecked)
				music.setIfActive(1);
			else
				music.setIfActive(0);
			
			temp_music_data.add(music);
			
			
			
		}
		
	}
	
	
	/**
	 * 
	 */
	class RemoveMusicInPlayList implements OnClickListener{
		int position;
		public RemoveMusicInPlayList(int position)
		{
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			

			
			
			AlertDialog.Builder builder = new Builder(MusicList.this);
			builder.setMessage("确认移除歌曲？");

			builder.setTitle("提示");

			builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
				
						Music music=new Music(all_activemusic_data.get(position));
						
						music.setIfActive(0);
						
						temp_music_data.clear();
						temp_music_data.add(music);
						
						UpdateMusicData();
						
						ShowMusicList();
						
					}
			  });
			  
			  builder.setNegativeButton("取消", null);

			  builder.show();
			
			
			
			
		}
		
	}
	
	
	/**
	 * 自定义多项选择ListView适配器
	 * @author Maniger
	 *
	 */
	class MultiSelectAdapter extends SimpleAdapter{
		
		private int mResource;
	    private List<? extends Map<String, ?>> mData;

		
		public MultiSelectAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
			this.mResource = resource;
            this.mData = data;
            
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater layoutInflater = getLayoutInflater();
			View view = layoutInflater.inflate(mResource, null);
		    
			TextView music_name = (TextView) view.findViewById(R.id.music_name);
		    music_name.setText(mData.get(position).get("music_name").toString());
		    TextView music_path = (TextView) view.findViewById(R.id.music_path);
		    music_path.setText(mData.get(position).get("music_path").toString()); 
		    TextView music_id = (TextView) view.findViewById(R.id.music_id);
		    music_id.setText(mData.get(position).get("music_id").toString()); 
		    TextView music_pace = (TextView) view.findViewById(R.id.music_pace);
		    music_pace.setText(mData.get(position).get("music_pace").toString()); 
		    TextView ifactive = (TextView) view.findViewById(R.id.ifactive);
		    ifactive.setText(mData.get(position).get("ifactive").toString()); 
		    
		    CheckBox check=(CheckBox)view.findViewById(R.id.check);
		    check.setVisibility(View.VISIBLE);
		    if(mData.get(position).get("ifactive").equals(Integer.valueOf(1)))
		        check.setChecked(true);
		    
		    check.setOnCheckedChangeListener(new PushMusicToPlayList(position));
		    
		    
		         
			
			return view;
		}
		
	}
	
	
	
	
	/**
	 * 自定义多项选择ListView适配器
	 * @author Maniger
	 *
	 */
	class PlayListAdapter extends SimpleAdapter{
		
		private int mResource;
	    private List<? extends Map<String, ?>> mData;

		
		public PlayListAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
			this.mResource = resource;
            this.mData = data;
            
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater layoutInflater = getLayoutInflater();
			View view = layoutInflater.inflate(mResource, null);
		    
			TextView music_name = (TextView) view.findViewById(R.id.music_name);
		    music_name.setText(mData.get(position).get("music_name").toString());
		    TextView music_path = (TextView) view.findViewById(R.id.music_path);
		    music_path.setText(mData.get(position).get("music_path").toString()); 
		    TextView music_id = (TextView) view.findViewById(R.id.music_id);
		    music_id.setText(mData.get(position).get("music_id").toString()); 
		    TextView music_pace = (TextView) view.findViewById(R.id.music_pace);
		    music_pace.setText(mData.get(position).get("music_pace").toString()); 
		    TextView ifactive = (TextView) view.findViewById(R.id.ifactive);
		    ifactive.setText(mData.get(position).get("ifactive").toString()); 
		    
		    ImageButton image=(ImageButton)view.findViewById(R.id.delete_bt);
		    image.setVisibility(View.VISIBLE);
		    image.setOnClickListener(new RemoveMusicInPlayList(position));
		    
		    
		         
			
			return view;
		}

	}
	
	
	
	/**
	 * 显示歌曲列表
	 */
	private void ShowMusicList()
	{
		SimpleAdapter adapter1 = new SimpleAdapter(this,getAllMusicList(),R.layout.listview_music_list,
                new String[]{"music_name","music_path","music_id","music_pace","ifactive"},
                new int[]{R.id.music_name,R.id.music_path,R.id.music_id,R.id.music_pace,R.id.ifactive});
		
		allmusic_list.setAdapter(adapter1);
		
		
		PlayListAdapter adapter2 = new PlayListAdapter(this,getActiveMusicList(),R.layout.listview_music_list,
				 new String[]{"music_name","music_path","music_id","music_pace","ifactive"},
	             new int[]{R.id.music_name,R.id.music_path,R.id.music_id,R.id.music_pace,R.id.ifactive});
		
		activemusic_list.setAdapter(adapter2);
		
	}
	
	
	
	
	/**
	 * 获取全部歌曲列表信息
	 * @return 
	 */
	private List<Map<String, Object>> getAllMusicList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        Map<String, Object> map;
  
		Music music;
		

        for(int i=0;i<all_music_data.size();i++)
        {
        	music=all_music_data.get(i);
        	
        	map = new HashMap<String, Object>();
        	map.put("music_name", music.getMusicName());
            map.put("music_path", music.getMusicPath());
            map.put("music_id", music.getMusicId());
            map.put("music_pace", "节奏:"+music.getPace());
            map.put("ifactive",music.isIfActive());
            
          //  Log.i("TEST","name:"+music.getMusicName());
          //  Log.i("TEST","ifactive:"+music.isIfActive());
            //map.put("img", R.drawable.i1);
            list.add(map);
        	
        }
		
  
        return list;
    }
	
	
	
	
	/**
	 * 获取播放列表歌曲信息
	 * @return
	 */
	private List<Map<String, Object>> getActiveMusicList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        Map<String, Object> map;
       
   
		Music music;
		
        for(int i=0;i<all_activemusic_data.size();i++)
        {
        	music=all_activemusic_data.get(i);
        	
        	map = new HashMap<String, Object>();
        	map.put("music_name", music.getMusicName());
            map.put("music_path", music.getMusicPath());
            map.put("music_id", music.getMusicId());
            map.put("music_pace", "节奏:"+music.getPace());
            map.put("ifactive",music.isIfActive());
            //map.put("img", R.drawable.i1);
            list.add(map);
        	
        }
		
  
        return list;
    }
	
	
	
	
	/**
	 * 多选歌曲项
	 * @author Maniger
	 *
	 */
	class MultiSelect implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(ifmultiselect)
			{
				temp_music_data.clear();
				
				MultiSelectAdapter adapter = new MultiSelectAdapter(MusicList.this,getAllMusicList(),R.layout.listview_music_list,
						 new String[]{"music_name","music_path","music_id","music_pace","ifactive"},
			             new int[]{R.id.music_name,R.id.music_path,R.id.music_id,R.id.music_pace,R.id.ifactive});
				
				allmusic_list.setAdapter(adapter);
				//listenmusic_bt.setText("取消多选");
				ifmultiselect=false;
			
			}
			else
			{
			
				  AlertDialog.Builder builder = new Builder(MusicList.this);
				  builder.setMessage("确认把选中的歌曲添加到播放列表？");

				  builder.setTitle("提示");

				  builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							UpdateMusicData();
							ShowMusicList();
							
						}
				  });
				  
				  builder.setNegativeButton("取消", null);
				  builder.show();
				  
				  SimpleAdapter adapter = new SimpleAdapter(MusicList.this,getAllMusicList(),R.layout.listview_music_list,
							 new String[]{"music_name","music_path","music_id","music_pace","ifactive"},
				             new int[]{R.id.music_name,R.id.music_path,R.id.music_id,R.id.music_pace,R.id.ifactive});
					
				  allmusic_list.setAdapter(adapter);
				  ifmultiselect=true;
			}
			
		}
		
	}
	
	
	
	
	
	/**
	 * 重新搜索音乐并刷新音乐列表
	 */
	class RefreshMusicList implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DBManager database=new DBManager(MusicList.this);
			
		
			SearchMusic();
			
			Music music;
			String music_path;
			String music_name;
			long music_id;
			
			//Log.i("TEST","size:"+all_music.size());
			for(int i=0;i<all_music_path.size();i++)
			{
				
				music_path=all_music_path.get(i);
				//Log.i("TEST","path:"+music_path);
				music_name=music_path.substring(music_path.lastIndexOf("/")+1, music_path.lastIndexOf("."));
				
				
				//Log.i("TEST","name:"+music_name);
				
				music=new Music(1,music_name,music_path,3,0);
				music_id=database.insertMusic(music);
				music.setMusicId(music_id);
				
				
			}
			ReadMusicData();
			ShowMusicList();
		}
		
	}
	
	
	
	/**
	 * 从手机存储和SDcard里面搜索音乐文件
	 */
	private void SearchMusic()
    {
		//search_music(Environment.getRootDirectory().getAbsolutePath());
		search_music(Environment.getExternalStorageDirectory().getAbsolutePath());
    }
	
	
	
	/**
	 * 在指定的文件夹里搜索音乐文件
	 * @param DirectoryPath 文件夹路径
	 */
	private void search_music(String DirectoryPath)
    {
    	
    	
    	//long a = System.currentTimeMillis();
    	LinkedList<File> list = new LinkedList<File>();
    	 //Log.i("TEST","Root:"+Environment.getRootDirectory().getAbsolutePath());
        // Log.i("TEST","SD:"+Environment.getExternalStorageDirectory().getAbsolutePath());
    	
    	File dir = new File(DirectoryPath);
    	File file[] = dir.listFiles();
    	//Log.i("TEST","LEN:"+file.length);
    	all_music_path.clear();
    	
    	for (int i = 0; i < file.length; i++) {
    		if (file[i].isDirectory()) 
    			list.add(file[i]);
    		else
    		{
    		
    			//currentpath=file[i].getAbsolutePath();
    			if(file[i].getAbsolutePath().endsWith(".mp3"))
    			{
    				//Log.i("TEST",file[i].getAbsolutePath());
    			
    				all_music_path.add(file[i].getAbsolutePath());
    			}
    		}
    			
    	}

    	File tmp; 
    	while (!list.isEmpty()) {

    	tmp = (File) list.removeFirst();
    	
    	if (tmp.isDirectory()) {

    		file = tmp.listFiles();

    		if (file == null) continue;

    			for (int i = 0; i < file.length; i++) {
    				if (file[i].isDirectory()) 
    					list.add(file[i]);
    				else
    				{
    					if(file[i].getAbsolutePath().endsWith(".mp3"))
    					//currentpath=file[i].getAbsolutePath();
    					{
    					//	Log.i("TEST",file[i].getAbsolutePath());
    						all_music_path.add(file[i].getAbsolutePath());
    					}
    					
    				}
    					
    			}

    		}
    		else   					
    		{ 
    		
    			//currentpath=tmp.getAbsolutePath();
    			if(tmp.getAbsolutePath().endsWith(".mp3"))
    			{
    				
    			//	Log.i("TEST",tmp.getAbsolutePath());
    				all_music_path.add(tmp.getAbsolutePath());
    			}
    		}

    	}
    	//Log.i("TEST","time:"+(System.currentTimeMillis() - a));
    	
    }
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_list, menu);
		return true;
	}
	


}
