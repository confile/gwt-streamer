package com.googlecode.gwtstreamer.test.client.shared;

import com.googlecode.gwtstreamer.client.Streamable;


public class TransientBean implements Streamable {
	private int a;
	private String b;
	
	public transient String temp;
	public static int cons = 100;
	
	
	private TransientBean() {
		this.a = 5;
		this.b = "another string";
	}
	
	public TransientBean( int a, String b ) {
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
