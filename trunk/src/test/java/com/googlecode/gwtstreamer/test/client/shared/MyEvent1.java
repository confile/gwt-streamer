package com.googlecode.gwtstreamer.test.client.shared;

public class MyEvent1 extends MyEvent 
{
	public int fieldd = 10;
	
	@Override public String toString() {
		return super.toString()+",d:"+fieldd;
	}
}
