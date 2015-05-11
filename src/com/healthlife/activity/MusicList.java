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

	//��¼���ݿ�������ڵ�ȫ��������Ϣ
	ArrayList<Music> all_music_data;
	//��ʱ��¼��Ҫ�޸ĵ�������Ϣ
	ArrayList<Music> temp_music_data;//ע���޸�ǰ��Ҫ�������
	//��¼���ݿ�������ڵĴ����ŵ�������Ϣ
	ArrayList<Music> all_activemusic_data;
	//��¼��������ȫ�������ļ�·��
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
		spec1.setIndicator("ȫ������");  
		  
		TabSpec spec2=tabHost.newTabSpec("tab2");  
		spec2.setIndicator("�����б�");  
		spec2.setContent(R.id.tab2);  
		  
		  
		tabHost.addTab(spec1);  
		tabHost.addTab(spec2);  
		

		allmusic_list=(ListView)findViewById(R.id.allmusic_list);
		
		allmusic_list.setOnItemClickListener(new ShowMusicItemInfo());
		
		allmusic_list.setOnItemLongClickListener(new ModifyMusicItemInfo());
		
		activemusic_list=(ListView)findViewById(R.id.activemusic_list);
		
		searchmusic_bt=(ImageButton)findViewById(R.id.searchmusic_bt);
		//searchmusic_bt.setText("ˢ�¸���");
		
		searchmusic_bt.setOnClickListener(new RefreshMusicList());
		
		listenmusic_bt=(ImageButton)findViewById(R.id.listenmusic_bt);
		//listenmusic_bt.setText("��ѡ");
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
	 * �����ݿ��ȡ�����ļ�����
	 */
	private void ReadMusicData(){
	     DBManager database=new DBManager(MusicList.this);
	
		 all_music_data=database.getMusicList();
		 all_activemusic_data=database.getActivedMusicList();
	}
	
	
	
	/**
	 * �������ݿ�����������ļ�����
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
	 * ��ʾ����������Ļ�����Ϣ(�ڶԻ�������ʾ)
	 * @author Maniger
	 *
	 */
	class ShowMusicItemInfo implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new Builder(MusicList.this);
			builder.setTitle("������Ϣ");
			//��ȡ����
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.dialog_getmusicinfo,
			     (ViewGroup) findViewById(R.id.getmusicinfo_dialog));
			builder.setView(layout);
			
			//��ȡ��������Ϣ
			Music music=new Music(all_music_data.get(arg2));
			
			//��ȡ�ؼ���Ϣ
			TextView music_name=(TextView) layout.findViewById(R.id.dialog_musicname);
			TextView music_pace=(TextView) layout.findViewById(R.id.dialog_musicpace);
			TextView ifactice=(TextView) layout.findViewById(R.id.dialog_ifactive);
			//���ÿؼ�����
			music_name.setText(music.getMusicName());
			music_pace.setText(""+music.getPace());
			if(1==music.isIfActive())
				ifactice.setText("��");
			else
				ifactice.setText("��");	
			
				
			builder.setPositiveButton("ȷ��",null);
			builder.setNegativeButton("ȡ��", null);
			//��ʾ
			builder.show();

		}

		
	}
	
	
	/**
	 * �޸�������Ļ�����Ϣ(�ڶԻ��������޸�)
	 * @author Maniger
	 *
	 */
	class ModifyMusicItemInfo implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new Builder(MusicList.this);
			builder.setTitle("������Ϣ");
			//��ȡ����
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.dialog_modifymusicinfo,
			     (ViewGroup) findViewById(R.id.modifymusicinfo_dialog));
			
			final Music music=new Music(all_music_data.get(arg2));
			
			
			//��ʾ������
			final EditText music_name=(EditText)layout.findViewById(R.id.dialog_musicname_m);   
			music_name.setText(music.getMusicName());
			
			//��ʾ���ֽ���
			final Spinner music_pace;
			ArrayList<String> list = new ArrayList<String>(); 
			ArrayAdapter<String> adapter;
	    	
	    	//���б�������ֽ���
	    	list.add("1");
	    	list.add("2");
	    	list.add("3");
	    	list.add("4");
	    	list.add("5");
	    	
	    	 //��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���    
	    	music_pace = (Spinner)layout.findViewById(R.id.dialog_musicpace_m);    
	        //�ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��    
	        adapter = new ArrayAdapter<String>(MusicList.this,R.layout.style_musicpacelist, list);    
	        //��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��    
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        //���Ĳ�������������ӵ������б���    
	        music_pace.setAdapter(adapter);      
	        //�������ֽ���
	        music_pace.setSelection(list.indexOf(""+music.getPace()), true);
			
	        //�Ƿ��ڲ����б�
	        final CheckBox ifactive=(CheckBox)layout.findViewById(R.id.dialog_ifactive_m);
	        if(1==music.isIfActive())
	        	ifactive.setChecked(true);
	        else
	        	ifactive.setChecked(false);
	        
	        
	        
			builder.setView(layout);
			builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
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
			  
			builder.setNegativeButton("ȡ��", null);

			builder.show();

			//����ķ���ֵ����Ҫ�������ֳ����Ͷ̰�
			return true;
		}
		
		
	}
	
	
	
	
	/**
	 * ��ȫ�������б�������Ƶ������б�(��ѡ������)
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
			builder.setMessage("ȷ���Ƴ�������");

			builder.setTitle("��ʾ");

			builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
				
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
			  
			  builder.setNegativeButton("ȡ��", null);

			  builder.show();
			
			
			
			
		}
		
	}
	
	
	/**
	 * �Զ������ѡ��ListView������
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
	 * �Զ������ѡ��ListView������
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
	 * ��ʾ�����б�
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
	 * ��ȡȫ�������б���Ϣ
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
            map.put("music_pace", "����:"+music.getPace());
            map.put("ifactive",music.isIfActive());
            
          //  Log.i("TEST","name:"+music.getMusicName());
          //  Log.i("TEST","ifactive:"+music.isIfActive());
            //map.put("img", R.drawable.i1);
            list.add(map);
        	
        }
		
  
        return list;
    }
	
	
	
	
	/**
	 * ��ȡ�����б������Ϣ
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
            map.put("music_pace", "����:"+music.getPace());
            map.put("ifactive",music.isIfActive());
            //map.put("img", R.drawable.i1);
            list.add(map);
        	
        }
		
  
        return list;
    }
	
	
	
	
	/**
	 * ��ѡ������
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
				//listenmusic_bt.setText("ȡ����ѡ");
				ifmultiselect=false;
			
			}
			else
			{
			
				  AlertDialog.Builder builder = new Builder(MusicList.this);
				  builder.setMessage("ȷ�ϰ�ѡ�еĸ�����ӵ������б�");

				  builder.setTitle("��ʾ");

				  builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							UpdateMusicData();
							ShowMusicList();
							
						}
				  });
				  
				  builder.setNegativeButton("ȡ��", null);
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
	 * �����������ֲ�ˢ�������б�
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
	 * ���ֻ��洢��SDcard�������������ļ�
	 */
	private void SearchMusic()
    {
    	
		search_music(Environment.getRootDirectory().getAbsolutePath());
		search_music(Environment.getExternalStorageDirectory().getAbsolutePath());
    }
	
	
	
	/**
	 * ��ָ�����ļ��������������ļ�
	 * @param DirectoryPath �ļ���·��
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
