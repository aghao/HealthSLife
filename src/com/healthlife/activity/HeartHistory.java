package com.healthlife.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Beats;

/**
 * This example shows a expandable listview
 * with a more button per list item which expands the expandable area.
 *
 * In the expandable area there are two buttons A and B which can be click.
 *
 * The events for these buttons are handled here in this Activity.
 *
 * @author tjerk
 * @date 6/13/12 7:33 AM
 */
public class HeartHistory extends Activity {
	
	private DBManager myDB = null;
	ArrayList <Beats> hrHistory = null;
	ArrayList <HashMap<String,Object>> heartData = null;
	View mView = null;
	SimpleAdapter adapter = null;
	@Override
	public void onCreate(Bundle savedData) {

		super.onCreate(savedData);
		this.setContentView(R.layout.hearthistory_main);

		mView = new View(this);
		Button clearBt = (Button) findViewById(R.id.hr_clearbt);
		myDB = new DBManager(this);
		hrHistory = myDB.getBeatsList();
		
		ListView hrList = (ListView) findViewById(R.id.hr_list);
		heartData = getHeartRateData();
		adapter = new SimpleAdapter(this,heartData,R.layout.hearthistory_listview,
				new String[]{"history","type"},new int[]{R.id.hr_listhistory,R.id.hr_listtype} );
		hrList.setAdapter(adapter);
		//���
		clearBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for(int i=0;i<hrHistory.size();i++)
				{
					myDB.removeBeat(hrHistory.get(i).getBeatId());
				}
				hrHistory.clear();
				//�ذ�adapterˢ��listviews
				heartData.clear();
				adapter.notifyDataSetChanged();
//				list.setAdapter(buildDummyData());
			}
		});
		 // listviewע�������Ĳ˵�
		this.registerForContextMenu(hrList);
	}
	

	
	ArrayList <HashMap<String,Object>> getHeartRateData(){
		ArrayList <HashMap<String,Object>> data = new ArrayList <HashMap<String,Object>>();
		int n = hrHistory.size();
		for(int i=0;i<n;i++)
		{
			HashMap <String,Object> map = new HashMap <String,Object>();
			map.put("history", hrHistory.get(n-i-1).getBeats()+"\n"+hrHistory.get(n-i-1).getDate());
			switch(hrHistory.get(n-i-1).getType())
			{
			case 1:
				map.put("type", "��Ϣ����");break;
			case 2:
				map.put("type", "�˶�������");break;
			case 3:
				map.put("type", "�������");break;
			default:
				;
			}
			data.add(map);
		}
		return data;
	}
	
	//�����˵�
	public void onCreateContextMenu(ContextMenu menu, View v,  
	        ContextMenuInfo menuInfo) {  
	    // set context menu title  
	    menu.setHeaderTitle("������ʷ");  
	    // add context menu item  
	    menu.add(0, 1, Menu.NONE, "ɾ��");  
	}  
	//�˵�������Ӧ
	public boolean onContextItemSelected(MenuItem item) {  
	    // �õ���ǰ��ѡ�е�item��Ϣ  
	    AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();  
	   // Log.v("TAG", "context item seleted ID="+ menuInfo.id);  
	      
	    switch(item.getItemId()) {  
	    case 1:  
	        // do something  
	    	int n = (int) (hrHistory.size()-menuInfo.id);
	    	myDB.removeBeat(hrHistory.get(n).getBeatId());
	    	hrHistory.remove(n);
	    	heartData.remove(n);
	    	adapter.notifyDataSetChanged();
	    	break;  
	    default:  
	        return super.onContextItemSelected(item);  
	    }  
	    return true;  
	}  
}