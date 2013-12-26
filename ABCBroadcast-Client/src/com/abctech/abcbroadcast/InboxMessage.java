package com.abctech.abcbroadcast;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class InboxMessage extends Activity {
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_inbox_msg);
	 
	        // get action bar   
	        ActionBar actionBar = getActionBar();
	 
	        // Enabling Up / Back navigation
	        actionBar.setDisplayHomeAsUpEnabled(true);
	    }
}
