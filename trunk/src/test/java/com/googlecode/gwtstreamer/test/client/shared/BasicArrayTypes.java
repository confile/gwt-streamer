package com.googlecode.gwtstreamer.test.client.shared;

import java.util.Arrays;

import com.googlecode.gwtstreamer.client.Streamable;

public class BasicArrayTypes implements Streamable {
	public int[] aInt;
	public byte[] aByte;
	public short[] aShort;
	public long[] aLong;
	public double[] aDouble;
	public float[] aFloat;
	public char[] aChar;
	public boolean[] aBoolean;
	public String[] aString;
	
	@Override
	public String toString() {
		return ""+Arrays.toString(aInt)+":"+Arrays.toString(aByte)+":"+Arrays.toString(aLong)
				+":"+Arrays.toString(aShort)+":"+Arrays.toString(aDouble)+":"+Arrays.toString(aFloat)
				+":"+Arrays.toString(aBoolean)+":"+Arrays.toString(aChar)+":"+
				Arrays.toString(aString);
	}
	
	@Override
	public boolean equals(Object obj) {
		return toString().equals( obj.toString() );
	}
}
