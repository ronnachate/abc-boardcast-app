package com.abctech.abcbroadcast;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class App
{
	public static String getServerUri(Context context) 
	{	
		return "http://192.168.40.112:3000";
	}
	
	public static boolean isSender()
	{
		return true;
	}
	
	public static boolean isForeground(Context context) 
	{
	    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
	    
	    for(RunningAppProcessInfo info : processes) {
	    	
	    	if(info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
	    		
	    		String packageName = context.getPackageName();
	    		if(info.processName.equals(packageName)) 
	    		{
	    			return true;
	    		}
	    	}
	    }
	    
	    return false;
	}
}
