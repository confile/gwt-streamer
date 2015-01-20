package com.nkdata.gwt.streamer.test.client.shared;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.nkdata.gwt.streamer.client.Streamable;


public class BasicTypes implements Streamable
{
	public int fInt;
	public byte fByte;
	public long fLong;
	public short fShort;
	public double fDouble;
	public float fFloat;
	public boolean fBoolean;
	public char fChar;
	public String fString;
	public BigInteger fBigInt;
	public BigDecimal fBigDec;
	public Date fDate;
	
	
	@Override
	public String toString() {
		return ""+fInt+":"+fByte+":"+fLong+":"+fShort+":"+fDouble+":"+fFloat+":"+fBoolean
				+":"+fChar+":"+fString+":"+fBigInt+":"+fBigDec+":"+fDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		return toString().equals( obj.toString() );
	}
}
