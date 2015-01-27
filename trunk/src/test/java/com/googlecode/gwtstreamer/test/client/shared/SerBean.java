package com.googlecode.gwtstreamer.test.client.shared;

import com.googlecode.gwtstreamer.client.Streamable;

/**
 * Serializable bean
 */
public class SerBean extends NonSerBean implements Streamable
{
	private int b;		// hiding superclass variable
	private String c = null;
	
	public SerBean() {
		super( 0, null );
	}
	
	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public int getNB() {
		return b;
	}

	public void setNB(int b) {
		this.b = b;
	}

	@Override public String toString() {
		return super.toString()+",SerBean.b:"+b+",c:"+c;
	}
}
