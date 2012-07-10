package com.nkdata.gwt.streamer.test.client.shared;

import java.util.Arrays;

import com.nkdata.gwt.streamer.client.Streamable;

public class TypedArray implements Streamable {
	public enum MyEnum {
		zero, one, two, three, four, infinite
	}
	
	public int[] a;
	public SimpleBean[] b;
	public Object[] c;
	public Object[] d;
	public MyEnum[] e;
	
	@Override public String toString() {
		return "a:"+Arrays.toString(a)+",b:"+Arrays.toString(b)+",c:"+Arrays.toString(c)
				+",d:"+Arrays.toString(d)+",e:"+Arrays.toString(e);
	}
	
	@Override public boolean equals( Object o1 ) {
		return this.toString().equals( o1.toString() );
	}

}
