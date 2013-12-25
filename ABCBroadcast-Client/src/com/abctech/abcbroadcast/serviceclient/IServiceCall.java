package com.abctech.abcbroadcast.serviceclient;

public interface IServiceCall
{
	boolean isFinish();
	boolean isSuccess();
	boolean isFailure();
	boolean isAborted();
	void execute();
	void makeFailure();
	void abort();
	String getOriginalStringData();
}