package com.abctech.abcbroadcast.serviceclient;

public class MakeCallFailureException extends Exception {

	private static final long serialVersionUID = -5476155375839184309L;
	
	private int _responseCode;
	
	public MakeCallFailureException(String message, int responseCode) {
		
		super(message);
		
		_responseCode = responseCode;
	}

	public int getResponseCode() {
		
		return _responseCode;
	}
}
