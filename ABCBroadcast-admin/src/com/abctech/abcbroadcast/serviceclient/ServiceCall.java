package com.abctech.abcbroadcast.serviceclient;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.HttpException;

import com.abctech.abcbroadcast.helper.EntityStorageManager;
import com.abctech.abcbroadcast.helper.JSONHelper;

import android.util.Log;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ServiceCall<TResult> {
	
	private final Class<?> _resultClass;
	private final boolean _isListResult;
	private boolean _isAborted;
	
	private Class<?> _inputClass;
	private Object _inputData;
	
	private RequestMethod _requestMethod;
	private ServiceManager _serviceManager;
	private ServiceAsyncCallback<TResult> _callback;
	
	private UUID _id;
	
	public ServiceCall(ServiceManager serviceManager, 
			ServiceAsyncCallback<TResult> callback, Class<?> resultClass, 
			boolean isListResult) {
		
		_serviceManager = serviceManager;
		_callback = callback;
		_resultClass = resultClass;
		_isListResult = isListResult;
		_id = UUID.randomUUID();
		
		_isMakeFailure = false;
	}
	
	public RequestMethod getRequestMethod() {
		
		return _requestMethod;
	}
	
	public void setRequestMethod(RequestMethod method) {
		
		_requestMethod = method;
	}
	
	public <TInput> void setInputData(TInput inputData, Class<TInput> inputClass) {
		
		_inputData = inputData;
		_inputClass = inputClass;
	}
	
	private boolean _isFinish = false;
	private boolean _isSuccess = false;
	private boolean _isFailure = false;
	private boolean _isMakeFailure = false;
	
	public boolean isFinish() {
		
		return _isFinish;
	}
	
	public boolean isSuccess() {
		
		return _isSuccess;
	}
	
	public boolean isFailure() {
		
		return _isFailure;
	}
	
	public boolean isAborted() {
		
		return _isAborted;
	}
	
	public String getUrl() {
		
		if(_serviceManager != null)
			return _serviceManager.getUrl();
		return null;
	}
	
	public void makeFailure() {
		
		//dmr -- prevent the timer from causing a failure when the data is being serialized but has been successfully received from the backend
		if(_isSuccess)
			return;
		
		abort();
		_isMakeFailure = true;
		if(_callback != null) {
			
			_callback.onFailure(new MakeCallFailureException("Local service call time out force service call cancelled!", 0));
			_callback.onFinished();
		}
	}
	
	public void execute() throws Exception {
		
		_isFinish = false;
		_isSuccess = false;
		_isFailure = false;
		
		if(_serviceManager instanceof RestServiceManager) {
			
			if(_inputData != null) {
				
				JSONSerializer serializer = 
						JSONHelper.getSerializer(_inputClass, this);
	
				String inputData = serializer.serialize(_inputData);
				((RestServiceManager)_serviceManager).setStringEntity(inputData);
			}
			
			_serviceManager.execute(_requestMethod);
			
		} 
		/*else if(_serviceManager instanceof UploaderServiceManager) {
			
			_serviceManager.execute(RequestMethod.POST);
		}*/
		
		// Do not response if aborted.
		if(_isAborted) return;
		
		//Do not response if MakeFailure.
		if(_isMakeFailure) return;
			
		int responseCode = _serviceManager.getResponseCode();
		
		Log.e("**** URL [Stop] ****", "***** ((( " + _serviceManager.getUrl() + " ))) *****");
		
		if(responseCode == 200) {
			
	
			String responseString = _serviceManager.getResponse();
			
			// In case response is empty string or null, we will just return null as TResult. 
			if(responseString == null || 
				responseString.trim() == "" ||
				responseString.contains("WirelessSUCCESS") ||
				_resultClass == Void.class) {
				
				_isSuccess = true;
				_isFinish = true;
				
				if(!_isMakeFailure) { //ton: no need to callback because MakeFailure already to callback
				
					_callback.onSuccess(_id, null);
					_callback.onFinished();
				}
				
				return;
				
			} else if(responseString.contains("WirelessERROR")) {
				
				HttpException ex = new HttpException("An error occurred on the wireless server.");
				
				Log.e("Zlitter APP", "EXCEPTION: Service: " + ex.getMessage());
				_isFailure = true;
				if(!_isMakeFailure) { //ton: no need to callback because MakeFailure already to callback
				
					_callback.onFailure(ex);
				}
				
				return;
			}
			
			_isSuccess = true;
			
			JSONDeserializer<TResult> deserializer = 
					JSONHelper.getDeserializer(_resultClass, this); 
					
			TResult result = null;
			
			if(!_isListResult) {
				
				try {
					
					if(deserializer == null) {
						deserializer = new JSONDeserializer<TResult>();
					}
					
					result = deserializer.deserialize(responseString, _resultClass);
					result = EntityStorageManager.process(result);
					
				} catch(Exception ex) {
					
					_isSuccess = false;
					_isFailure = true;
					//dmr--I am using _isSuccess to bypass _isMakeFailure so if something happens here we need to call onFailure explicitly
					//dmr--if(!_isMakeFailure) //ton: no need to callback because MakeFailure already to callback
					//{
						_callback.onFailure(ex);
						_callback.onFinished();
					//}
					return;
				}
				
			} else {
				
				try {
					
					if(deserializer == null) {
						deserializer = new JSONDeserializer<TResult>();
					}
					
					deserializer = deserializer.use("values", _resultClass);
					result = deserializer.deserialize(responseString);
					
					if(result instanceof ArrayList<?>) {
						
						@SuppressWarnings("unchecked")
						TResult r = (TResult)EntityStorageManager.process((ArrayList<?>)result);
						
						result = r;
					}

				} catch(Exception ex) {
					
					_isSuccess = false;
					_isFailure = true;
					_callback.onFailure(ex);
					_callback.onFinished();
					
					return;
				}
			}
			
			if(!_isMakeFailure) { //ton: no need to callback because MakeFailure already to callback
			
				_callback.onSuccess(_id, result);
			}
			
		} else {
			
			String message = _serviceManager.getErrorMessage();
			ServiceCallException ex = new ServiceCallException(message, responseCode);
			
			Log.e("Zlitter APP", "EXCEPTION: Service: " + ex.getMessage());
			
			_isFailure = true;
			if(!_isMakeFailure) { //ton: no need to callback because MakeFailure already to callback
			
				_callback.onFailure(ex);
			}
		}
		
		_isFinish = true;
		if(!_isMakeFailure) { //ton: no need to callback because MakeFailure already to callback
		
			_callback.onFinished();
		}
	}
	
	public UUID getServiceCallId() {
		
		return _id;
	}

	public void abort() {
		
		_isAborted = true;
		_serviceManager.abort();
	}
}
