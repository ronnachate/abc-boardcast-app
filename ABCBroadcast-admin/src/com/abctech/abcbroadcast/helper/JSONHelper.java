package com.abctech.abcbroadcast.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import android.util.Log;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JSONHelper {

	public static final JSONSerializer getSerializer(Class<?> dataClass, Object receiver) {
		
		JSONSerializer serializer = null;
		Method method = null;
		
		try {
			
			method = dataClass.getMethod("getSerializer");
			
		} catch (SecurityException e) {
			
			Log.e("ABCBroadcast", dataClass.getName() + 
					".getMethod() throw SecurityException: " + e.getMessage());
			
		} catch (NoSuchMethodException e) {
			
			Log.e("ABCBroadcast", "getDeserializer() not found in class: " + dataClass.getName());
		}
		
		try {
			
			if(method != null)
				serializer = (JSONSerializer)method.invoke(receiver);
			
		} catch (IllegalArgumentException e) {

			Log.e("ABCBroadcast", dataClass.getName() + 
					".deserialize() throw IllegalArgumentException: " + e.getMessage());
			
		} catch (IllegalAccessException e) {

			Log.e("ABCBroadcast", dataClass.getName() + 
					".deserialize() throw IllegalAccessException: " + e.getMessage());
			
		} catch (InvocationTargetException e) {

			Log.e("ABCBroadcast", dataClass.getName() + 
					".deserialize() throw InvocationTargetException: " + e.getMessage());
		}
		
		if(serializer == null)
			serializer = getDefaultSerializer();
		
		return serializer;
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> JSONDeserializer<T> getDeserializer(Class<?> dataClass, Object receiver) {

		JSONDeserializer<?> deserializer = null;
		
		if(!dataClass.isPrimitive()) {
			
			Method method = null;
			
			try {
				
				method = dataClass.getMethod("getDeserializer");
				
			} catch (SecurityException e) {
				
				Log.e("ABCBroadcast", dataClass.getName() + 
						".getMethod() throw SecurityException: " + e.getMessage());
				
			} catch (NoSuchMethodException e) {
				
				Log.e("ABCBroadcast", "getDeserializer() not found in class: " + dataClass.getName());
			}
			
			try {
				
				if(method != null)
					deserializer = (JSONDeserializer<?>)method.invoke(receiver);
				
			} catch (IllegalArgumentException e) {

				Log.e("ABCBroadcast", dataClass.getName() + 
						".deserialize() throw IllegalArgumentException: " + e.getMessage());
				
			} catch (IllegalAccessException e) {

				Log.e("ABCBroadcast", dataClass.getName() + 
						".deserialize() throw IllegalAccessException: " + e.getMessage());
				
			} catch (InvocationTargetException e) {

				Log.e("ABCBroadcast", dataClass.getName() + 
						".deserialize() throw InvocationTargetException: " + e.getMessage());
			}
		} else {
			
			deserializer = new JSONDeserializer<T>();
		}
		
		return (JSONDeserializer<T>) deserializer;
	}

	public static JSONSerializer getDefaultSerializer() {
		
		return new JSONSerializer().
				exclude("*.class").
				transform(new ABCBroadcastDateTransformer(), Date.class).
				transform(new ABCBroadcastEnumTransformer(), Enum.class);
	}
}

