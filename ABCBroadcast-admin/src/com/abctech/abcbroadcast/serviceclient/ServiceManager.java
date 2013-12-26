package com.abctech.abcbroadcast.serviceclient;

public interface ServiceManager {
	
	String getResponse();
	String getErrorMessage();
	int getResponseCode();
	void addParam(String name, String value);
	void addHeader(String name, String value);
	void execute(RequestMethod method) throws Exception;
	void abort();
	String getUrl();
}