package com.abctech.abcbroadcast;

import java.util.List;

import com.abctech.abcbroadcast.data.DatabaseHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Adapter;
import android.widget.ListAdapter;

public class InboxMessage extends Activity {
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_inbox_msg);
	 
	        // get action bar   
	        ActionBar actionBar = getActionBar();
	 
	        // Enabling Up / Back navigation
	        actionBar.setDisplayHomeAsUpEnabled(true);
	        try {
	        	DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	        	List<String> msg = db.getAllMsg();
	        	ListView lv = (ListView)findViewById(R.id.msgList);
	        	lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,msg));
	        	lv.setTextFilterEnabled(true);
	        } catch( SQLException e ){
	        	Log.i("Error", e.toString());
	        }
	    }

}
