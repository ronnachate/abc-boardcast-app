package com.abctech.abcbroadcast.serviceclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class RestServiceManager extends AbstractServiceManager {
	
	private HttpGet _httpGet;
	
	protected RestServiceManager(String url) {
		super(url);
	}

	public void execute(RequestMethod method) throws Exception {
		
		Log.i("ServiceCall", "*** " + method + " url(" + _url + ")");
		
		switch(method) {
		
			case GET: {
				
				//add parameters
				String combinedParams = "";
				if(!_params.isEmpty()) {
					
					combinedParams += "?";
					for(NameValuePair p : _params) {
						
						String paramString = p.getName() + "=" + 
								URLEncoder.encode(p.getValue(),"UTF-8");
						
						if(combinedParams.length() > 1) {
							combinedParams += "&" + paramString;
						} else {
							combinedParams += paramString;
						}
					}
				}
				
				HttpGet request = new HttpGet(_url + combinedParams);
				_httpGet = request;
				
				//add headers
				for(NameValuePair h : _headers) {
					
					request.addHeader(h.getName(), h.getValue());
				}
				
				executeRequest(request, _url);
				break;
			} 
			
			case POST: {
				
				HttpPost request = new HttpPost(_url);
				
				//add headers
				for(NameValuePair h : _headers) {
					request.addHeader(h.getName(), h.getValue());
				}
				
				if(!_params.isEmpty()) {
					request.setEntity(new UrlEncodedFormEntity(_params, HTTP.UTF_8));
				}
				
				if(!(_stringEntity == null))
					request.setEntity(new StringEntity(_stringEntity));
				
				Log.i("ServiceCall", "*** With data: " + _stringEntity);
				
				executeRequest(request, _url);
				break;
			} 
			
			case PUT: {
				
				HttpPut request = new HttpPut(_url);
				
				//add headers
				for(NameValuePair h : _headers) {
					request.addHeader(h.getName(), h.getValue());
				}
				
				if(!_params.isEmpty()) {
					request.setEntity(new UrlEncodedFormEntity(_params, HTTP.UTF_8));
				}
				
				if(!(_stringEntity == null))
					request.setEntity(new StringEntity(_stringEntity));
				
				Log.i("ServiceCall", "*** With data: " + _stringEntity);
				
				executeRequest(request, _url);
				break;
			} 
			
			case DELETE: {
				
				HttpDelete request = new HttpDelete(_url);
				
				//add headers
				for(NameValuePair h : _headers) {
					request.addHeader(h.getName(), h.getValue());
				}
				
				executeRequest(request, _url);
				break;
			}
		}
	}
	
	private void executeRequest(HttpUriRequest request, String url) {
	
		HttpParams httpParameters = new BasicHttpParams(); 
		// Set the timeout in milliseconds until a connection is established. 
		// The default value is zero, that means the timeout is not used.  
		int timeoutConnection = 30000; 
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection); 
		// Set the default socket timeout (SO_TIMEOUT)  
		// in milliseconds which is the timeout for waiting for data. 
		int timeoutSocket = 120000; 
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket); 
		
		
		HttpClient client = new DefaultHttpClient(httpParameters);
		//HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		
		try {
			
			httpResponse = client.execute(request);
			_responseCode = httpResponse.getStatusLine().getStatusCode();
			_message = httpResponse.getStatusLine().getReasonPhrase();
			
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
				
				InputStream instream = entity.getContent();
				_response = convertStreamToString(instream);
				
				// Closing the input stream will trigger connection release
				instream.close();
			}
		
		} catch (ClientProtocolException e) {
			
			client.getConnectionManager().shutdown();
			e.printStackTrace();
			
		} catch (IOException e) {
			
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void abort() {
		
		if(_httpGet != null) _httpGet.abort();
	}
}