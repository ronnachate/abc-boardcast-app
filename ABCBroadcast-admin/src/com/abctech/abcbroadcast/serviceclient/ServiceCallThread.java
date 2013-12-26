package com.abctech.abcbroadcast.serviceclient;

public class ServiceCallThread extends Thread 
{
	private IServiceCall _serviceCalls;
	public ServiceCallThread(IServiceCall serviceCalls)
	{
		_serviceCalls = serviceCalls;
	}
	
	@Override
	public void run() 
	{
		_serviceCalls.execute();
	}
}
