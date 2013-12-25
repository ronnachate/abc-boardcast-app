package com.abctech.abcbroadcast.serviceclient;

import java.util.ArrayList;

import com.abctech.abcbroadcast.helper.EntityStorageManager;

import flexjson.JSONDeserializer;

public abstract class AbsServiceCall<TResultClass> implements IServiceCall 
{
	protected TResultClass getResult(String jsonString, Class<TResultClass> resultClass)
	{
		TResultClass result = null;
		
		JSONDeserializer<TResultClass> deserializer;
		try 
		{
			deserializer = new JSONDeserializer<TResultClass>();
			
			result = deserializer.deserialize(jsonString, resultClass);
			result = EntityStorageManager.process(result);
			
		} 
		catch(Exception ex) 
		{
			return null;
		}
		
		
		return result;
	}
	
	protected ArrayList<TResultClass> getListResult(String jsonString, Class<TResultClass> resultClass)
	{
	
		ArrayList<TResultClass> results = null;
		
		JSONDeserializer<ArrayList<TResultClass>> deserializer;
		try 
		{
			
			deserializer = new JSONDeserializer<ArrayList<TResultClass>>();
			
			deserializer = deserializer.use("values", resultClass);
			results = deserializer.deserialize(jsonString);
			
			if(results instanceof ArrayList<?>) {
				
				@SuppressWarnings("unchecked")
				ArrayList<TResultClass> r = (ArrayList<TResultClass>)EntityStorageManager.process((ArrayList<?>)results);
				
				results = r;
			}

		} 
		catch(Exception ex) 
		{			
			return null;
		}
		
		return results;
	}
}

