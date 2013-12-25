package com.abctech.abcbroadcast.serviceclient;


public abstract class AbstractServiceClient {
	 
	//TODO: Implement getting from configuration later.
    protected final int DefaultPageSize = 20;
    
    protected abstract String getServiceBaseUri();
    protected abstract String getServiceName();
    
    protected <T> ServiceCallInfo<T> get(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult) {
    	return get(operationUri, callback, resultClass, isListResult, false);
    }
    
    @SuppressWarnings("unchecked")
	protected <T> ServiceCallInfo<T> get(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult, boolean isAutoAborted) {
    	
    	RestServiceManager client = getRestServiceClient(operationUri, true);
    	ServiceCall<T> serviceCall = new ServiceCall<T>(client, callback, resultClass, isListResult);
    	serviceCall.setRequestMethod(RequestMethod.GET);
    	
    	ServiceCallTask<T> task = new ServiceCallTask<T>();
    	task.setAutoAborted(isAutoAborted);
    	task.execute(serviceCall);
    	
    	return new ServiceCallInfo<T>(serviceCall.getServiceCallId(), task);
    }
    
    protected <T> ServiceCallInfo<T> post(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult) {
    	return post(operationUri, callback, resultClass, isListResult, false);
    }
    
    @SuppressWarnings("unchecked")
	protected <T> ServiceCallInfo<T> post(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult, boolean isAutoAborted) {
    	
    	RestServiceManager client = getRestServiceClient(operationUri, false);
    	
    	ServiceCall<T> serviceCall = new ServiceCall<T>(client, callback, resultClass, isListResult);
    	serviceCall.setRequestMethod(RequestMethod.POST);
    	
    	ServiceCallTask<T> task = new ServiceCallTask<T>();
    	task.setAutoAborted(isAutoAborted);
    	task.execute(serviceCall);
    	
    	return new ServiceCallInfo<T>(serviceCall.getServiceCallId(), task);
    }
    
    protected <TInput, TResult> ServiceCallInfo<TResult> post(
   			String operationUri, TInput input, 
   			ServiceAsyncCallback<TResult> callback,
   			Class<TInput> inputClass,
   			Class<?> resultClass, boolean isListResult) {
    	return post(operationUri, input, callback, inputClass, resultClass, isListResult, false);
    }
    
    @SuppressWarnings("unchecked")
	protected <TInput, TResult> ServiceCallInfo<TResult> post(
			String operationUri, TInput input, 
			ServiceAsyncCallback<TResult> callback,
			Class<TInput> inputClass,
			Class<?> resultClass, boolean isListResult, boolean isAutoAborted) {
    	
    	RestServiceManager client = getRestServiceClient(operationUri, false);
    	
    	ServiceCall<TResult> serviceCall = new ServiceCall<TResult>(client, callback, resultClass, isListResult);
    	serviceCall.setRequestMethod(RequestMethod.POST);
    	serviceCall.setInputData(input, inputClass);
    	
    	ServiceCallTask<TResult> task = new ServiceCallTask<TResult>();
    	task.setAutoAborted(isAutoAborted);
    	task.execute(serviceCall);
    	
    	return new ServiceCallInfo<TResult>(serviceCall.getServiceCallId(), task);
    }
    
    protected <T> ServiceCallInfo<T> put(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult) {
    	return put(operationUri, callback, resultClass, isListResult, false);
    }
    

    @SuppressWarnings("unchecked")
	protected <T> ServiceCallInfo<T> put(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult, boolean isAutoAborted) {
    	
    	RestServiceManager client = getRestServiceClient(operationUri, false);
    	ServiceCall<T> serviceCall = new ServiceCall<T>(client, callback, resultClass, isListResult);
    	serviceCall.setRequestMethod(RequestMethod.PUT);
    	
    	ServiceCallTask<T> task = new ServiceCallTask<T>();
    	task.setAutoAborted(isAutoAborted);
    	task.execute(serviceCall);
    	
    	return new ServiceCallInfo<T>(serviceCall.getServiceCallId(), task);
    }
    
    protected <TInput, TResult> ServiceCallInfo<TResult> put(
   			String operationUri, TInput input, 
   			ServiceAsyncCallback<TResult> callback,
   			Class<TInput> inputClass,
   			Class<?> resultClass, boolean isListResult) {
    	return put(operationUri, input, callback, inputClass, resultClass, isListResult, false);
    }
    
    
    @SuppressWarnings("unchecked")
	protected <TInput, TResult> ServiceCallInfo<TResult> put(
			String operationUri, TInput input, 
			ServiceAsyncCallback<TResult> callback,
			Class<TInput> inputClass,
			Class<?> resultClass, boolean isListResult, boolean isAutoAborted) {
    	
    	RestServiceManager client = getRestServiceClient(operationUri, false);
    	
    	ServiceCall<TResult> serviceCall = new ServiceCall<TResult>(client, callback, resultClass, isListResult);
    	serviceCall.setRequestMethod(RequestMethod.PUT);
    	serviceCall.setInputData(input, inputClass);
    	
    	ServiceCallTask<TResult> task = new ServiceCallTask<TResult>();
    	task.setAutoAborted(isAutoAborted);
    	task.execute(serviceCall);
    	
    	return new ServiceCallInfo<TResult>(serviceCall.getServiceCallId(), task);
    }
    
    protected <T> ServiceCallInfo<T> delete(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult) {
    	return delete(operationUri, callback, resultClass, isListResult, false);
    }
    
    @SuppressWarnings("unchecked")
	protected <T> ServiceCallInfo<T> delete(String operationUri, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult, boolean isAutoAborted) {
    	
    	RestServiceManager client = getRestServiceClient(operationUri, false);
    	ServiceCall<T> serviceCall = new ServiceCall<T>(client, callback, resultClass, isListResult);
    	serviceCall.setRequestMethod(RequestMethod.DELETE);
    	
    	ServiceCallTask<T> task = new ServiceCallTask<T>();
    	task.setAutoAborted(isAutoAborted);
    	task.execute(serviceCall);
    	
    	return new ServiceCallInfo<T>(serviceCall.getServiceCallId(), task);
    }
    
    /*
    protected <T> ServiceCallInfo<T> upload(String operationUri, 
    		File input, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult) {
    	return upload(operationUri, input, callback, resultClass, isListResult, false);
    }*/
    
    /*
    @SuppressWarnings("unchecked")
    protected <T> ServiceCallInfo<T> upload(String operationUri, 
    		File input, ServiceAsyncCallback<T> callback, Class<?> resultClass, boolean isListResult, boolean isAutoAborted) {

    	UploaderServiceManager client = getUploaderServiceClient(operationUri, false);
    	client.setInputFile(input);
    	client.setAsyncCallback(callback);
    	
    	ServiceCall<T> serviceCall = new ServiceCall<T>(client, callback, resultClass, isListResult);
    	serviceCall.setRequestMethod(RequestMethod.POST);
    	
    	ServiceCallTask<T> task = new ServiceCallTask<T>();
    	task.setAutoAborted(isAutoAborted);
    	task.execute(serviceCall);
    	  	
    	return new ServiceCallInfo<T>(serviceCall.getServiceCallId(), task);
    }*/
    
    protected boolean getUseMalerService() {
    	
    	return true;
    }
    
    protected String getServiceUri() {
    	
    	if(getUseMalerService()) {
    	
	    	String serviceBaseUri = getServiceBaseUri();
	
	    	if(!serviceBaseUri.endsWith("/"))
	    		serviceBaseUri += "/";
	
	    	return ServiceClientHelper.getInstance().getServiceUri() + serviceBaseUri;
    	}
    	
    	return "";
    }
    
    private RestServiceManager getRestServiceClient(String operationUri, Boolean isGetMethod) {
    	
    	String serviceUri = getServiceUri() + getServiceName() + operationUri;
    	
    	RestServiceManager client = new RestServiceManager(serviceUri);
    	
        if (!isGetMethod) {
        	
            client.addHeader("Content-type", "application/json");
        }
        
		String base64 =  ServiceClientHelper.getInstance().getBase64Auth();
		
		if(base64 != null)
			//client.addHeader("Authorization", base64);
			client.addHeader("Authorization", base64); 
		 
		return client;
    }
    
    /*
    private UploaderServiceManager getUploaderServiceClient(String operationUri, Boolean isGetMethod) {
    	
    	String serviceUri = getServiceUri() + getServiceName() + operationUri;
    	
    	UploaderServiceManager client = new UploaderServiceManager(serviceUri);
    	
    	return client;
    }*/
}

