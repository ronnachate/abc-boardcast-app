package com.abctech.abcbroadcast.serviceclient.abc;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.util.Log;

import com.abctech.abcbroadcast.data.MessageDto;
import com.abctech.abcbroadcast.serviceclient.AbstractServiceClient;
import com.abctech.abcbroadcast.serviceclient.ServiceAsyncCallback;
import com.abctech.abcbroadcast.serviceclient.ServiceCallInfo;

public class ABCServiceClient extends AbstractServiceClient
{

	@Override
	protected String getServiceBaseUri() {
		return "/api/";
	}

	@Override
	protected String getServiceName() {
		return "";
	}
	
	public ServiceCallInfo<?> getMessages(int skip, int take, ServiceAsyncCallback<ArrayList<MessageDto>> callback, boolean isAutoAborted) 
	{	
		return get(String.format("/Clients/?skip=%d&take=%d", skip, take),
			callback, MessageDto.class, true, isAutoAborted);
	}
	 
	public void registerDevice(String gcmRegistrationId, ServiceAsyncCallback<Void> callback) 
	{	
		String link = "device?device_id=" + gcmRegistrationId; 
	    put(link, null, callback, String.class, Void.class, false);
	}
	
	public void sendMessage(String message, ServiceAsyncCallback<Void> callback) 
	{	
		String link = "message?message=" + message; 
	    put(link, null, callback, String.class, Void.class, false);
	}
}
