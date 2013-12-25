package com.abctech.abcbroadcast.serviceclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.abctech.abcbroadcast.data.IServiceReceivedData;

public class CommonServiceCall<TResultClass, TCallBack> extends AbsServiceCall<TResultClass> 
{
	private String _path;
	private Boolean _isListResult;
	private Class<TResultClass> _resultClass;
	private ServiceAsyncCallback<TCallBack> _callback;
	
	private boolean _isFinish = false;
	private boolean _isSuccess = false;
	private Exception _failure = null;
	//private boolean _isMakeFailure = false;
	private boolean _isAborted = false;
	private HttpGet _httpGet;
	private String _originalStringData = null;
	
	public CommonServiceCall(String path, Boolean isListResult, Class<TResultClass> resultClass, ServiceAsyncCallback<TCallBack> callback)
	{
		_path = path;
		_resultClass = resultClass;
		_callback = callback;
		_isListResult = isListResult;
		//_isMakeFailure = false;
	}
	
	@Override
	public boolean isFinish() 
	{
		return _isFinish;
	}

	@Override
	public boolean isSuccess() 
	{
		return _isSuccess;
	}

	@Override
	public boolean isFailure() 
	{
		return _failure != null ? true : false;
	}

	@Override
	public boolean isAborted() 
	{
		return _isAborted;
	}
	
	@Override
	public void makeFailure() {
		//ton: to be implement
		
	}

	@Override
	public void abort() 
	{
		if(_httpGet != null) _httpGet.abort();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute() 
	{
		TCallBack result = performExecute();
		if(result != null)
		{
			if(_callback != null)
				_callback.onSuccess(UUID.randomUUID(), (TCallBack)result);
		}
		else
		{
			if(_failure != null)
			{
				if(_callback != null)
					_callback.onFailure(_failure);
			}
			else
			{
				if(_callback != null)
					_callback.onSuccess(UUID.randomUUID(), null);
			}
		}
		
		if(_callback != null)
			_callback.onFinished();
	}
	
	@SuppressWarnings("unchecked")
	public TCallBack performExecute() 
	{
		_originalStringData = null;
		_failure = null;
		_isSuccess = false;
		_isFinish = false;
			
		try 
		{
			HttpClient client = new DefaultHttpClient();
			_httpGet = new HttpGet(_path);
			
			HttpResponse response =  client.execute(_httpGet);
			final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) 
            {
            	_failure = new Exception("response.getStatusLine().getStatusCode() is " + statusCode);
            }
            else
            {
            	HttpEntity getResponseEntity = response.getEntity();

                if (getResponseEntity != null)
                {
                	_originalStringData = EntityUtils.toString(getResponseEntity);
                }
                else
                {
                	_failure = new Exception("response.getEntity() return null");
                }
            }
		} 
		catch (ClientProtocolException exception) 
		{
			_failure = exception;
			_originalStringData = null;
		} 
		catch (IOException exception) 
		{
			_failure = exception;
			_originalStringData = null;
		}

		if(_originalStringData != null)
		{
			if(_isListResult)
			{
				ArrayList<TResultClass> results = getListResult(_originalStringData, _resultClass);
				if(results != null)
				{
					_isSuccess = true;
					return (TCallBack)results;
				}
				else
				{
					_failure = new Exception("serialize failure");
				}
			}
			else
			{
				TResultClass result =  getResult(_originalStringData, _resultClass);
				if(result != null)
				{
					_isSuccess = true;
					
					if(result instanceof IServiceReceivedData)
						((IServiceReceivedData)result).setRefData(_originalStringData);
					
					return (TCallBack)result;
				}
				else
				{
					_failure = new Exception("serialize failure");
				}
			}
		}
		else
		{
			_failure = new Exception("data from the service is null");
		}
		
		_isFinish = true;
		
		return null;
	}
	
	@Override
	public String getOriginalStringData() 
	{
		return _originalStringData;
	}
}

