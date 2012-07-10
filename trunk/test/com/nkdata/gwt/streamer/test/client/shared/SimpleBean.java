package com.nkdata.gwt.streamer.test.client.shared;

import com.nkdata.gwt.streamer.client.Streamable;


public class SimpleBean implements Streamable {
	private int a;
	private String b;
	
	private SimpleBean() {
		this.a = 5;
		this.b = "another string";
	}
	
	public SimpleBean( int a, String b ) {
		this.a = a;
		this.b = b;
	}
	
	@Override public String toString() {
		return "a:"+a+",b:"+b;
	}
	
	@Override public boolean equals( Object o1 ) {
		return this.toString().equals( o1.toString() );
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}
}
