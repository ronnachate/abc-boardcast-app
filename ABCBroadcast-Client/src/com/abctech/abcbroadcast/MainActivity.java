package com.abctech.abcbroadcast;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.abctech.abcbroadcast.data.DatabaseHandler;
import com.abctech.abcbroadcast.serviceclient.ServiceAsyncCallback;
import com.abctech.abcbroadcast.serviceclient.ServiceClientHelper;
import com.abctech.abcbroadcast.serviceclient.abc.ABCServiceClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.view.Menu;

public class MainActivity extends Activity {
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	String SENDER_ID = "418087069711";
	
	static final String TAG = "ABC Broadcast Reciever";
	
	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;
	
	String regid;

	private static MainActivity _instance = null;
	public static MainActivity getInstance() { return _instance; }
	
	public MainActivity() 
	{
		_instance = this;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ServiceClientHelper.getInstance().setServiceUri(App.getServerUri(context));
		
		setContentView(R.layout.activity_main);
		
	    mDisplay = (TextView) findViewById(R.id.display);
		
	    context = getApplicationContext();
	    
	    // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
	    if (checkPlayServices()) 
	    {
	        gcm = GoogleCloudMessaging.getInstance(this);
	    } else 
	    {
	        Log.i(TAG, "No valid Google Play Services APK found.");
	    }
	    
//	    final SharedPreferences prefs = getGcmPreferences(context);
//	    if (prefs.contains(PROPERTY_REG_ID)) {
//	    	Button activeButton = (Button) findViewById(R.id.activate);
//	    	activeButton.setEnabled(false);
//	    	activeButton.setText("Activated");    	
//	    }
	    
	    if(getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString("message") != null && getIntent().getExtras().getString("message").trim().length() > 0)
	    {
	    	final String message = getIntent().getExtras().getString("message").trim();
	    	Log.i("mes",message);
	    	Handler hnd = new Handler();
	    	hnd.postDelayed(new Runnable() {
				
				@Override
				public void run() 
				{
					showAlertDlg("New message!", message);
				}
			}, 200);
	    	try {
	    		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	    		db.insertMsg(message);
	    	} catch( SQLException e) {
	    		showAlertDlg( "Error",e.toString());
	    	}
	    }
	}
	
	//create menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main_action, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	//On select action bar icon
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case R.id.active_account:
		    final SharedPreferences prefs = getGcmPreferences(context);
		    if (prefs.contains(PROPERTY_REG_ID)) {
		    	showAlertDlg("Register Account", "This device is regiested");
		    }else {
		    	registerDevice();
		    	showAlertDlg("Register Account", "Register is succesful!");
		    }
			return true;
		case R.id.action_inbox:
			InboxMessage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void InboxMessage(){
		Intent i = new Intent (MainActivity.this, InboxMessage.class);
		startActivity(i);
	}
	
	private void showAlertDlg(String title, String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    	
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

	@Override
	protected void onResume()
	{
	    super.onResume();
	    // Check device for Play Services APK.
	    checkPlayServices();
	}
	
	private boolean checkPlayServices() 
	{
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

	    if (resultCode != ConnectionResult.SUCCESS)
	    {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) 
	        {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else 
	        {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        
	        return false;
	    }
	    
	    return true;
	}
	
	@Override
	protected void onDestroy() 
	{
	    super.onDestroy();
	}
	
	// Send an upstream message.
	public void onClick(final View view) 
	{
	    if (view == findViewById(R.id.activate)) 
	    {
	    	registerDevice();
	    }
	    else if (view == findViewById(R.id.ok) ) {
	    	moveTaskToBack(true);
	    }
	}
	
	private void registerDevice() 
	{
		
	    new AsyncTask<Void, Void, String>() 
	    {
	    	
	        @Override
	        protected String doInBackground(Void... params) 
	        {
	            String msg = "";
	            try 
	            {
	                if (gcm == null) 
	                {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;
	
	                // You should send the registration ID to your server over HTTP, so it
	                // can use GCM/HTTP or CCS to send messages to your app.
	                sendRegistrationIdToBackend();
	
	                // For this demo: we don't need to send it because the device will send
	                // upstream messages to a server that echo back the message using the
	                // 'from' address in the message.
	
	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, regid);
	                Button activeButton = (Button) findViewById(R.id.activate);
	    	    	activeButton.setEnabled(false);
	    	    	activeButton.setText("Activated");
	            } 
	            catch (IOException ex)
	            {
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	                
	            }
	            return msg;
	        }
	
	        @Override
	        protected void onPostExecute(String msg) 
	        {
	            mDisplay.append(msg + "\n");
	        }
	    }.execute(null, null, null);
	}
	
	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId)
	{
	    final SharedPreferences prefs = getGcmPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	/**
	 * Gets the current registration ID for application on GCM service, if there is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context)
	{
	    final SharedPreferences prefs = getGcmPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty())
	    {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) 
	    {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    
	    return registrationId;
	}
	
	private static int getAppVersion(Context context) 
	{
	    try 
	    {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    }
	    catch (NameNotFoundException e) 
	    {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context)
	{
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}
	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
	 * messages to your app. Not needed for this demo since the device sends upstream messages
	 * to a server that echoes back the message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() 
	{
		ABCServiceClient service = new ABCServiceClient();
		service.registerDevice(regid, new ServiceAsyncCallback<Void>(){
			@Override
			public void onSuccess(UUID id, Void result) {
				// TODO Auto-generated method stub
				super.onSuccess(id, result);
				Log.i(TAG, "Success with "+result);
			}
			
			@Override
			public void onFailure(Exception exception) {
				// TODO Auto-generated method stub
				super.onFailure(exception);
			}
			
			
		});
	}

}
