package com.abctech.abcbroadcast.serviceclient;

import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.util.Log;

public class ServiceCallTask<TResult> extends AsyncTask<ServiceCall<TResult>, Integer, Void> {
	
	private ServiceCall<TResult>[] _serviceCalls;
	
	private boolean _isAutoAborted = false;
	private int _autoAbortedTime = 30000;
	
	Timer timer = null;
	
	public void setAutoAborted(boolean isAutoAborted) {
		
		_isAutoAborted = isAutoAborted;
	}
	
	@Override
	protected Void doInBackground(ServiceCall<TResult>... serviceCalls) {
		
		_serviceCalls = serviceCalls;
		
        int count = serviceCalls.length;
        
        timer = new Timer(); 

        for (int i = 0; i < count; i++) {
        	
        	try {
        		
        		if(_isAutoAborted) {
        			
	        		final ServiceCall<TResult> sc = serviceCalls[i];
	        		
	        		timer.schedule(new TimerTask() {
	        			
						@Override
						public void run() {
							
							try {
								
								if(!sc.isAborted() && !sc.isFailure() && !sc.isFinish() && !sc.isSuccess()) {
									sc.makeFailure();
								}
								
				        	} catch (Exception e) {
				        		
								Log.d("Zlitter", "ServiceCall Exception (Timer):" + e.getMessage());
								
							} finally {

								destroyServiceCallLocalTimer();
							}
							
							//Log.d("maler.zlitter.serviceclient.ServiceCallTask", "url:" + sc.getUrl());
						}
					}, _autoAbortedTime);
        		}

        		serviceCalls[i].execute();

			} catch (Exception e) {
				
				Log.d("Zlitter", "ServiceCall Exception: " + e.getMessage());
				Log.e("ServiceCall", "ServiceCall Exception: " + e.getMessage());
				
			} finally {
				
				destroyServiceCallLocalTimer();
			}
        	
            publishProgress((int) ((i / (float) count) * 100));
        }
        
        // Release service call.
        _serviceCalls = null;
        
		return null;
	}
	
	public void cancel() {
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				if(_serviceCalls != null) {
					
			        int count = _serviceCalls.length;
			        for (int i = 0; i < count; i++) {
			        	
			        	_serviceCalls[i].abort();
			        }
			        
			        destroyServiceCallLocalTimer();
			    }
			}
		});
		
		thread.start();
	}
	
	private void destroyServiceCallLocalTimer() {
		
		if(timer != null) {
			
			timer.cancel();
			timer.purge();
			
			timer = null;
		}
	}
}

