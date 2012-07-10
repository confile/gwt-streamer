package com.nkdata.gwt.streamer.test.client.shared;

public class MyEvent extends AbsEvent
{
	public String fieldc = "testC";
	
	@Override public String toString() {
		return super.toString()+",c:"+fieldc;
	}
}
