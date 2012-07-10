package com.nkdata.gwt.streamer.test.client.shared;

import com.nkdata.gwt.streamer.client.Streamable;

/**
 * Serializable bean
 */
public class SerBean extends NonSerBean implements Streamable 
{
	private int b;		// hiding superclass variable
	private String c;
	
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
