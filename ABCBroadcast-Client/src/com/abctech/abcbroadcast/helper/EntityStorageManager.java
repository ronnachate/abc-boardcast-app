package com.abctech.abcbroadcast.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.abctech.abcbroadcast.data.UniqueEntityObject;

public class EntityStorageManager {
	
	private static HashMap<Class<?>, HashMap<Long, UniqueEntityObject>> 
		_uniqueEntityObjects = new HashMap<Class<?>, HashMap<Long, UniqueEntityObject>>();

	@SuppressWarnings("unchecked")
	public static <T> T process(T item) {
		
		if(!(item instanceof UniqueEntityObject)) return item;
		
		T existingItem = null;
		
		if(!_uniqueEntityObjects.containsKey(item.getClass())) {
			
			_uniqueEntityObjects.put(
					item.getClass(), new HashMap<Long, UniqueEntityObject>());
		}
		
		UniqueEntityObject newItem = (UniqueEntityObject)item;
		
		HashMap<Long, UniqueEntityObject> hashMap = _uniqueEntityObjects.get(item.getClass());
		if(!hashMap.containsKey(newItem.getEntityId())) {
			hashMap.put(newItem.getEntityId(), newItem);
		}
			
		existingItem = (T)hashMap.get(newItem.getEntityId());
		
		if(existingItem != newItem)
			assignsAllPublicFields(existingItem, newItem);

		return existingItem;
	}

	public static <T> ArrayList<T> process(ArrayList<T> items) {
		
		ArrayList<T> resultItems = new ArrayList<T>();
		
		for(T item : items) {
			
			resultItems.add(process(item));
		}
		
		return items;
	}
	
	private static <T> T assignsAllPublicFields(T targetItem, T sourceItem) {
		
		Field[] fields = sourceItem.getClass().getFields();
		
		for(Field field : fields) {
			
			FieldAssignable fa = field.getAnnotation(FieldAssignable.class);
			
			if(fa != null) {
				if(!fa.isAssignable())
					continue;
			}
			
			try {
				
				Object value = field.get(sourceItem);

				if(fa == null) {
					
					field.set(targetItem, value);
					
				} else {
					
					if(value == null && fa.notAssignIfNull())
						continue;
					
					field.set(targetItem, value);
				}
				
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
				
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			}
		}
		
		return targetItem;
	}
}

