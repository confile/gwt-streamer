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
		
		
		public String toString() {
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
			// TODO: ...
			writeString( Double.toString( val ) );
		}
		
		public void writeFloat( float val ) {
			// TODO: ...
			writeString( Double.toString( val ) );
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
		
		/*@Override
		public void writeIntArray(int[] val) {
			writeInt( val.length );
			for ( int v : val )
				writeInt( v );
		}

		@Override
		public void writeLongArray(long[] val) {
			writeInt( val.length );
			for ( long v : val )
				writeLong( v );
		}

		@Override
		public void writeShortArray(short[] val) {
			writeInt( val.length );
			for ( short v : val )
				writeShort( v );
		}

		@Override
		public void writeByteArray(byte[] val) {
			writeInt( val.length );
			for ( byte v : val )
				writeByte( v );
		}

		@Override
		public void writeCharArray(char[] val) {
			writeString( new String( val ) );
		}

		@Override
		public void writeBooleanArray(boolean[] val) {
			writeInt( val.length );
			for ( boolean v : val )
				writeBoolean( v );
		}

		@Override
		public void writeDoubleArray(double[] val) {
			writeInt( val.length );
			for ( double v : val )
				writeDouble( v );
		}

		@Override
		public void writeFloatArray(float[] val) {
			writeInt( val.length );
			for ( float v : val )
				writeFloat( v );
		}*/
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
	        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
		}

		public long readLong() {
	        int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        int ch4 = in.read();
	        int ch5 = in.read();
	        int ch6 = in.read();
	        int ch7 = in.read();
	        int ch8 = in.read();
	        return ((ch1 << 56) + (ch2 << 48) + (ch3 << 40) + (ch4 << 32) + (ch5 << 24) + (ch6 << 16) + (ch7 << 8) + (ch8 << 0));
		}
		
		public short readShort() {
	        int ch1 = in.read();
	        int ch2 = in.read();
	        return (short)((ch1 << 8) + (ch2 << 0));
		}

		
		public byte readByte() {
			int ch = in.read();
			return (byte)(ch);
		}
		
		public char readChar() {
	        int ch1 = in.read();
	        int ch2 = in.read();
	        return (char)((ch1 << 8) + (ch2 << 0));
		}
		
		public boolean readBoolean() {
			int ch = in.read();
			return (ch != 0);
		}
		
		public double readDouble() {
			String s = readString();
			
			try {
				return Double.parseDouble( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public float readFloat() {
			String s = readString();
			
			try {
				return Float.parseFloat( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
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
		
		
		/*@Override
		public int[] readIntArray() {
			int n = readInt();
			int[] buf = new int[n];
			for ( int i = 0; i < n; i++ )
				buf[i] = readInt();
			return buf;
		}


		@Override
		public long[] readLongArray() {
			int n = readInt();
			long[] buf = new long[n];
			for ( int i = 0; i < n; i++ )
				buf[i] = readLong();
			return buf;
		}


		@Override
		public short[] readShortArray() {
			int n = readInt();
			short[] buf = new short[n];
			for ( int i = 0; i < n; i++ )
				buf[i] = readShort();
			return buf;
		}


		@Override
		public byte[] readByteArray() {
			int n = readByte();
			byte[] buf = new byte[n];
			for ( int i = 0; i < n; i++ )
				buf[i] = readByte();
			return buf;
		}


		@Override
		public char[] readCharArray() {
			String s = readString();
			return s.toCharArray();
		}


		@Override
		public boolean[] readBooleanArray() {
			int n = readInt();
			boolean[] buf = new boolean[n];
			for ( int i = 0; i < n; i++ )
				buf[i] = readBoolean();
			return buf;
		}


		@Override
		public double[] readDoubleArray() {
			int n = readInt();
			double[] buf = new double[n];
			for ( int i = 0; i < n; i++ )
				buf[i] = readDouble();
			return buf;
		}


		@Override
		public float[] readFloatArray() {
			int n = readInt();
			float[] buf = new float[n];
			for ( int i = 0; i < n; i++ )
				buf[i] = readFloat();
			return buf;
		}*/
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
