package com.abctech.abcbroadcast.serviceclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public abstract class AbstractServiceManager implements ServiceManager {

	protected ArrayList <NameValuePair> _params;
	protected ArrayList <NameValuePair> _headers;
	protected String _stringEntity;

	protected String _url;
	protected int _responseCode;
	protected String _message;
	protected String _response;
	
	protected AbstractServiceManager(String url) {
		
		_url = url;
		_params = new ArrayList<NameValuePair>();
		_headers = new ArrayList<NameValuePair>();
	}
	
	public String getUrl() { return _url; }
	public String getResponse() { return _response; }
	public String getErrorMessage() { return _message; }
	public int getResponseCode() { return _responseCode; }
	public abstract void execute(RequestMethod method) throws Exception;
	public abstract void abort();
	
	public void addParam(String name, String value) {
		
		_params.add(new BasicNameValuePair(name, value));
	}
	
	public void addHeader(String name, String value) {
		
		_headers.add(new BasicNameValuePair(name, value));
	}
	
	public void setStringEntity(String stringEntity) {
		
		_stringEntity = stringEntity;
	}
	
	protected static String convertStreamToString(InputStream is) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		
		try {
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				is.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
}

