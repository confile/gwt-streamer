package com.nkdata.gwt.streamer.test.client.shared;


public class ExtSimpleBean extends SimpleBean
{
	// superclass field hiding
	private int a;
	private int b;
	
	public String c = "qwerty";	// null check
	
	public ExtSimpleBean() {
		super( 0, null );
	}
	
	@Override public String toString() {
		return super.toString()+",ExtSimpleBean.a:"+a+",ExtSimpleBean.b:"+b+",ExtSimpleBean.c:"+c;
	}

	public int getNA() {
		return a;
	}

	public void setNA(int a) {
		this.a = a;
	}

	public int getNB() {
		return b;
	}

	public void setNB(int b) {
		this.b = b;
	}
}
