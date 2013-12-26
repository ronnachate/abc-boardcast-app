package com.abctech.abcbroadcast.serviceclient;

import java.util.UUID;

public class ServiceCallInfo<T> {

	public UUID id;
	public ServiceCallTask<T> task;
	
	public ServiceCallInfo(UUID id, ServiceCallTask<T> task) {
		
		this.id = id;
		this.task = task;
	}
}