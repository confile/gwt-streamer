package com.nkdata.gwt.streamer.test.client.shared;

/**
 * Test transient Streamable interface
 */
public class MyClass1 implements Iface1 {
	public String name;
	public int value;
	
	@Override
	public boolean equals(Object obj) {
		return toString().equals( obj.toString() );
	}
	
	@Override
	public String toString() {
		return ""+name+":"+value;
	}
	
}
