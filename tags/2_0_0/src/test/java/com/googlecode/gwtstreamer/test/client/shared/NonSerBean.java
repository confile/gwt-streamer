package com.googlecode.gwtstreamer.test.client.shared;



// Non serializable bean
public class NonSerBean {
	private int a;
	public String b;
	
	public NonSerBean( int a, String b ) {
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
