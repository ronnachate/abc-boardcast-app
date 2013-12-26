package com.abctech.abcbroadcast.helper;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface FieldAssignable 
{

	boolean isAssignable() default true;
	boolean notAssignIfNull() default false;
}
