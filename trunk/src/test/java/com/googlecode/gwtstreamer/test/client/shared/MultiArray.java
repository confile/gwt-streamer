package com.googlecode.gwtstreamer.test.client.shared;

import java.util.Arrays;

import com.googlecode.gwtstreamer.client.Streamable;


public class MultiArray implements Streamable
{
	public int[][] a;
	public SimpleBean[][] b = new SimpleBean[][] {  };
	public Enums.Month[][] c;
	
	@Override public String toString() {
		return "a:"+Arrays.deepToString(a)+",b:"+Arrays.deepToString(b)+",c:"+Arrays.deepToString(c);
	}
	
	@Override public boolean equals( Object o1 ) {
		return this.toString().equals( o1.toString() );
	}
}
