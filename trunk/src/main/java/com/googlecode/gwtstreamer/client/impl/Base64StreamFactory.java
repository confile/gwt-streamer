package com.googlecode.gwtstreamer.client.impl;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.StreamerException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Base64StreamFactory implements StreamFactory 
{
	private static class ByteBufferOutput
	{
		private List<byte[]> seq = new ArrayList<byte[]>();
		private int count;
		private byte[] current;
		
		public ByteBufferOutput() {
			current = new byte[40];
			seq.add( current );
		}
		
		public void write( int b )
		{
			if ( count >= current.length ) {
				current = new byte[current.length*2];
				seq.add( current );
				count = 0;
			}
			
			current[count++] = (byte) b;
		}
		
		
		public byte[] toByteArray()
		{
			int size = 0;
			
			for ( int i = 0; i < seq.size(); i++ ) {
				if ( i < seq.size()-1 )
					size += seq.get(i).length;
				else
					size += count;
			}
			
			byte[] buf = new byte[size];
			
			int n = 0;
			
			for ( int i = 0; i < seq.size(); i++ ) {
				if ( i < seq.size()-1 ) {
					byte[] arr = seq.get( i );
					
					for ( int j = 0; j < arr.length; j++ )
						buf[n++] = arr[j];
				} else
					for ( int j = 0; j < count; j++ )
						buf[n++] = current[j];
			}
			
			return buf;
		}
	}
	
	
	private static class ByteBufferInput 
	{
		private byte[] buf;
		private int count;
		
		public ByteBufferInput( byte[] buf ) {
			this.buf = buf;
		}
		
		public int read() {
			return buf[count++] & 0xFF;
		}
		
		public boolean hasMore() {
			return count < buf.length;
		}
	}
	
	
	public static class Base64Writer implements StreamFactory.Writer
	{
		private ByteBufferOutput out = new ByteBufferOutput();
		
		
		public String getData() {
			byte[] buf = out.toByteArray();
			return Base64Util.encode( buf );
		}
		
		public void writeInt( int v ) {
	        out.write((v >>> 24) & 0xFF);
	        out.write((v >>> 16) & 0xFF);
	        out.write((v >>>  8) & 0xFF);
	        out.write((v >>>  0) & 0xFF);
		}

		public void writeLong( long v ) {
	        out.write(((int)(v >>> 56) & 0xFF));
	        out.write(((int)(v >>> 48) & 0xFF));
	        out.write(((int)(v >>> 40) & 0xFF));
	        out.write(((int)(v >>> 32) & 0xFF));
	        out.write(((int)(v >>> 24) & 0xFF));
	        out.write(((int)(v >>> 16) & 0xFF));
	        out.write(((int)(v >>>  8) & 0xFF));
	        out.write(((int)(v >>>  0) & 0xFF));
		}
		
		public void writeShort( short v ) {
	        out.write((v >>> 8) & 0xFF);
	        out.write((v >>> 0) & 0xFF);
		}
		
		public void writeByte( byte v ) {
			out.write(v);
		}
		
		public void writeChar( char v ) {
	        out.write((v >>> 8) & 0xFF);
	        out.write((v >>> 0) & 0xFF);
		}
		
		public void writeBoolean( boolean v ) {
			out.write(v ? 1 : 0);
		}

		public void writeDouble( double val ) {
			writeLong(Double.doubleToLongBits(val));
		}

		public void writeFloat( float val ) {
			writeInt(Float.floatToIntBits(val));
		}

		/** String will be encoded and may contain any character */
		public void writeString( String val ) {
			try {
				byte[] buf = val.getBytes("UTF-8");
				writeInt(buf.length);
				
				for ( byte b : buf ) {
					out.write( b & 0xFF );
				}
			} catch ( UnsupportedEncodingException e ) {
				throw new StreamerException( e );
			}
		}
	}



	public static class Base64Reader implements Reader
	{
		private ByteBufferInput in;
		
		public Base64Reader( final String str ) {
			byte[] buf = Base64Util.decode( str );
			in = new ByteBufferInput( buf );
		}
		
		
		public boolean hasMore() {
			return in.hasMore();
		}

		public int readInt()
		{
	        int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        int ch4 = in.read();
	        return ((ch1 << 24) | (ch2 << 16) | (ch3 << 8) | (ch4 << 0));
		}

		public long readLong() {
	        long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
			long ch6 = in.read();
			long ch7 = in.read();
			long ch8 = in.read();
	        return ((ch1 << 56) | (ch2 << 48) | (ch3 << 40) | (ch4 << 32) | (ch5 << 24)
					| (ch6 << 16) | (ch7 << 8) | (ch8 << 0));
		}
		
		public short readShort() {
	        int ch1 = in.read();
	        int ch2 = in.read();
	        return (short)((ch1 << 8) | (ch2 << 0));
		}

		
		public byte readByte() {
			int ch = in.read();
			return (byte)(ch);
		}
		
		public char readChar() {
	        int ch1 = in.read();
	        int ch2 = in.read();
	        return (char)((ch1 << 8) | (ch2 << 0));
		}
		
		public boolean readBoolean() {
			int ch = in.read();
			return (ch != 0);
		}

		public double readDouble() {
			return Double.longBitsToDouble(readLong());
		}

		public float readFloat() {
			return Float.intBitsToFloat(readInt());
		}
		
		/** String will be encoded and may contain any character */
		public String readString() {
			int l = readInt();
			byte[] buf = new byte[l];
			for ( int i = 0; i < l; i++ )
				buf[i] = (byte) in.read();
			try {
				return new String( buf, "UTF-8" );
			} catch ( UnsupportedEncodingException e ) {
				throw new StreamerException( e );
			}
		}
	}



	@Override
	public Writer createWriter() {
		return new Base64Writer();
	}



	@Override
	public Reader createReader(String str) {
		return new Base64Reader( str );
	}

}
