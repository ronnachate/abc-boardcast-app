package com.abctech.abcbroadcast.data;

import flexjson.JSON;

public abstract class EntityObject {

	public abstract long getEntityId();

	@JSON(include=false)
	public void setEntityId() { }
	
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof EntityObject) {
			
			EntityObject ueo = (EntityObject)o;
			return this.getEntityId() == ueo.getEntityId();
		}
		
		return super.equals(o);
	}
}
