package com.abctech.abcbroadcast.serviceclient;

import java.util.UUID;

public class ServiceAsyncCallback<TResult> 
{
	public void onSuccess(UUID id, TResult result) { }
	public void onFailure(Exception exception) { }
	public void onFinished() { }
	public void onProgress(int percent) { }
}

