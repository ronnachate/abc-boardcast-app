package com.abctech.abcbroadcast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "ABC Broadcast";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
               // sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                
            	 try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	/*for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }*/
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                if(extras.getString("message") != null && extras.getString("message").trim().length() > 0)
                	sendNotification(extras.getString("message"));
                
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    
    private void showAlertDlg(String title, String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setMessage(msg)
        .setTitle(title);
    	
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
    	
    	AlertDialog dialog = builder.create();
    	dialog.show();
	}
    
    private void sendNotification(final String msg) 
    {
    	/*if(MainActivity.getInstance() != null && App.isForeground(MainActivity.getInstance()))
    	{
    		MainActivity.getInstance().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					showAlertDlg("New message!", msg);
				}
			});

    	}
    	else
    	{
	       
    	}*/
    	
    	 mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        PackageManager pm = getPackageManager();
			String packageName = getPackageName();
			ApplicationInfo info = null;
			try 
			{
				info = pm.getApplicationInfo(packageName, 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(info != null) 
			{
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setComponent(new ComponentName(packageName, "com.abctech.abcbroadcast.MainActivity"));
				
				Bundle dataPackage = new Bundle();
				dataPackage.putString("message", msg);
				
				intent.putExtras(dataPackage);
				
				intent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
				intent.setAction("actionstring" + System.currentTimeMillis());
				
				PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				
				  NotificationCompat.Builder mBuilder =
			                new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.ic_stat_gcm)
			        .setContentTitle("ABC Tech Notification")
			        .setStyle(new NotificationCompat.BigTextStyle()
			        .bigText(msg))
			        .setContentText(msg);
			        
			        mBuilder.setAutoCancel(true);
			        mBuilder.setLights(Color.BLUE, 500, 500);
			        long[] pattern = {500,500,500,500,500,500,500,500,500};
			        mBuilder.setVibrate(pattern);
			        mBuilder.setStyle(new NotificationCompat.InboxStyle());
			        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			        
			        if(alarmSound == null)
			        {
			            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			            if(alarmSound == null){
			                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			            }
			        }
			       
			        mBuilder.setContentIntent(pIntent);
			        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	
			}
    }
}
