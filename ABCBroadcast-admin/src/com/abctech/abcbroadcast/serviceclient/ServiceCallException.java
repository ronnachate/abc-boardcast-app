package com.abctech.abcbroadcast.serviceclient;

public class ServiceCallException extends Exception
{

	private static final long serialVersionUID = -5476155375839184319L;
	
	private int _responseCode;
	
	public ServiceCallException(String message, int responseCode) {
		
		super(message);
		
		_responseCode = responseCode;
	}

	public int getResponseCode() {
		
		return _responseCode;
	}
}
