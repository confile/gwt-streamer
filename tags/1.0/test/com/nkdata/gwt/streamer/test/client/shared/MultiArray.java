package com.nkdata.gwt.streamer.test.client.shared;

import java.util.Arrays;

import com.nkdata.gwt.streamer.client.Streamable;
import com.nkdata.gwt.streamer.test.client.shared.Enums.Month;


public class MultiArray implements Streamable
{
	public int[][] a;
	public SimpleBean[][] b = new SimpleBean[][] {  };
	public Month[][] c;
	
	@Override public String toString() {
		return "a:"+Arrays.deepToString(a)+",b:"+Arrays.deepToString(b)+",c:"+Arrays.deepToString(c);
	}
	
	@Override public boolean equals( Object o1 ) {
		return this.toString().equals( o1.toString() );
	}
}
