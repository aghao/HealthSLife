package com.healthlife.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.healthlife.R;
import com.healthlife.db.DBManager;
import com.healthlife.entity.Beats;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

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
	View mView = null;
	@Override
	public void onCreate(Bundle savedData) {

		super.onCreate(savedData);
		// set the content view for this activity, check the content view xml file
		// to see how it refers to the ActionSlideExpandableListView view.
		this.setContentView(R.layout.hearthistory_main);
		// get a reference to the listview, needed in order
		// to call setItemActionListener on it
		final ActionSlideExpandableListView list = (ActionSlideExpandableListView)this.findViewById(R.id.list);
		mView = new View(this);
		Button clearBt = (Button) findViewById(R.id.hr_clearbt);
		myDB = new DBManager(this);
		hrHistory = myDB.getBeatsList();
//		// fill the list with data
		list.setAdapter(buildDummyData());
		
		// listen for events in the two buttons for every list item.
		// the 'position' var will tell which list item is clicked
		list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

			@Override
			public void onClick(View listView, View buttonview, int position) {

				/**
				 * Normally you would put a switch
				 * statement here, and depending on
				 * view.getId() you would perform a
				 * different action.
				 */
				if(buttonview.getId()==R.id.hr_upload) {
				} 
				//删除
				else {
					myDB.removeBeat(hrHistory.get(hrHistory.size()-1-position).getBeatId());
					hrHistory.remove(hrHistory.size()-1-position);
					//重绑adapter刷新listview
					list.setAdapter(buildDummyData());
				}
				
			}

		// note that we also add 1 or more ids to the setItemActionListener
		// this is needed in order for the listview to discover the buttons
		}, R.id.hr_upload, R.id.hr_delete);
		
		//清空
		clearBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for(int i=0;i<hrHistory.size();i++)
				{
					myDB.removeBeat(hrHistory.get(i).getBeatId());
				}
				hrHistory.clear();
				//重绑adapter刷新listviews
				list.setAdapter(buildDummyData());
			}
		});
	}

	/**
	 * Builds dummy data for the test.
	 * In a real app this would be an adapter
	 * for your data. For example a CursorAdapter
	 */
	//数据库读数据存入hrHistory
	public ListAdapter buildDummyData() {
//		final int Size = 20;
		String[] values = new String[hrHistory.size()];
		for(int i=0;i<hrHistory.size();i++) {
			values[i] = hrHistory.get(hrHistory.size()-1-i).getBeats()+"\n"+hrHistory.get(i).getDate();
		}
		return new ArrayAdapter<String>(
				this,
				R.layout.hrexpand_listview,
				R.id.hr_history,
				values
		);
	}
}
