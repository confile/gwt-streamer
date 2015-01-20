package com.nkdata.gwt.streamer.test.client.shared;

import com.nkdata.gwt.streamer.client.Streamable;

public abstract class AbsEvent implements Streamable {
	public String fielda = "test";
	public String fieldb = "more test";
	
	public static class Prop implements Streamable {
		public int a = 0;
		public int b = 1;
		
		@Override public String toString() {
			return "a:"+a+",b:"+b;
		}
		
		@Override public boolean equals( Object o1 ) {
			return this.toString().equals( o1.toString() );
		}
	}
	
	public Prop prop = new Prop();

	@Override public String toString() {
		return "fielda:"+fielda+",fieldb:"+fieldb+",prop:"+prop;
	}
	
	@Override public boolean equals( Object o1 ) {
		return this.toString().equals( o1.toString() );
	}
}
