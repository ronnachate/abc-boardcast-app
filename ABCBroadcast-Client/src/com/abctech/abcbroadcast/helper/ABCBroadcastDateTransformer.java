package com.abctech.abcbroadcast.helper;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.BasicDateTransformer;

public class ABCBroadcastDateTransformer extends BasicDateTransformer implements ObjectFactory 
{
	public void transform(Object object) 
	{
		// TODO: Implement transform into this: /Date(126521670000-0700)/
		getContext().writeQuoted("/Date(" + ((Date)object).getTime() + ")/");
	}

	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, @SuppressWarnings("rawtypes") Class targetClass) 
	{
		String dateStringFromJSON = value.toString();
		
		if(dateStringFromJSON.startsWith("{"))
			dateStringFromJSON = value.toString();
			

		// Remove prefix and suffix extra string information
		String dateString = dateStringFromJSON.
				replace("\\/Date(", "").replace(")\\/", "").
				replace("/Date(", "").replace(")/", "");
		
		// Split date and timezone parts
		String[] dateParts = dateString.split("[+-]");

		Calendar calendar = Calendar.getInstance();
		if(dateString.startsWith("-") || dateString.startsWith("+"))
		{	
			// The date must be in milliseconds since January 1, 1970 00:00:00 UTC
			// We want to be sure that it is a valid date and time, aka the use of Calendar
			calendar.setTimeInMillis(Long.parseLong(dateParts[1]));
	
			if(dateParts.length > 2)
			{
				// If you want to play with time zone:
				calendar.setTimeZone(TimeZone.getTimeZone(dateParts[2]));
			}
		}
		else
		{
			// The date must be in milliseconds since January 1, 1970 00:00:00 UTC
			// We want to be sure that it is a valid date and time, aka the use of Calendar
			calendar.setTimeInMillis(Long.parseLong(dateParts[0]));
	
			if(dateParts.length > 1)
			{
				// If you want to play with time zone:
				calendar.setTimeZone(TimeZone.getTimeZone(dateParts[1]));
			}
		}

		// Convert it to a Date() object now:
		return calendar.getTime();
	}
}

