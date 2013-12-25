package com.abctech.abcbroadcast.serviceclient;

public class ServiceClientHelper 
{
	
	private static final String TAG = "ServiceClientHelper";
	
	private static ServiceClientHelper _instance;
	public static ServiceClientHelper getInstance() {
		
		if(_instance == null)
			_instance = new ServiceClientHelper();
		
		return _instance;
	}
	
	private String _serviceUri;
	private String _currentUserName;
	private String _currentPassword;
	private String _base64AuthString = null;

	public String getServiceUri() { return _serviceUri; }
	public void setServiceUri(String serviceUri) { _serviceUri = serviceUri; }
	
	public String getCurrentUserName() { return _currentUserName; }
	public void setCurrentUserName(String userName) { _currentUserName = userName; }
	
	public String getCurrentPassword() { return _currentPassword; }
	public void setCurrentPassword(String password) { _currentPassword = password; }

	
	public void setBase64AuthString(String auth) {
		
		_base64AuthString = auth;
	}
	
	public String getBase64Auth()
	{
		return _base64AuthString;
	}
}