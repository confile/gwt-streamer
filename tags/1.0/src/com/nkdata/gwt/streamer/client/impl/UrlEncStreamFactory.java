package com.nkdata.gwt.streamer.client.impl;

import com.nkdata.gwt.streamer.client.StreamFactory;
import com.nkdata.gwt.streamer.client.StreamerException;

public class UrlEncStreamFactory implements StreamFactory
{
	private final static char DELIMITER = '_';
	
	public static class StrWriter implements com.nkdata.gwt.streamer.client.StreamFactory.Writer
	{
		private StringBuffer buf = new StringBuffer( 256 ); 
		
		
		protected void write( String rawString )
		{
			buf.append( rawString );
		}
		
		protected void writeDelim( String numString ) {
			buf.append( numString ).append( DELIMITER );
		}
		
		
		public String toString() {
			return StreamerInternal.urlEncode( buf.toString() );
		}
		
		
		public void writeInt( int val ) {
			writeDelim( String.valueOf( val ) );
		}

		public void writeLong( long val ) {
			writeDelim( String.valueOf( val ) );
		}
		
		public void writeShort( short val ) {
			writeDelim( String.valueOf( val ) );
		}
		
		public void writeByte( byte val ) {
			writeDelim( String.valueOf( val ) );
		}
		
		public void writeChar( char val ) {
			write( String.valueOf( val ) );
		}
		
		public void writeBoolean( boolean val ) {
			write( val ? "T" : "F" );
		}
		
		public void writeDouble( double val ) {
			writeDelim( String.valueOf( val ) );
		}
		
		public void writeFloat( float val ) {
			writeDelim( String.valueOf( val ) );
		}
		
		public void writeString( String val )
		{
			writeInt( val.length() );
			write( val );
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



	public static class StrReader implements Reader
	{
		private final String str;
		private int idx;
		
		
		public StrReader( final String str )
		{
			this.str = StreamerInternal.urlDecode( str );
			idx = 0;
		}
		
		
		/** read to delimiter char or to EOLN */
		protected String readDelim()
		{
			if ( idx >= str.length() )
				throw new StreamerException( "Unexpected end of stream" );
			
			int end = str.indexOf( DELIMITER, idx );
			if ( end < 0) end = str.length();
			
			String s = str.substring( idx, end );
			idx = end+1;
			return s;
		}

		
		public boolean hasMore() {
			return idx < str.length();
		}

		
		/** read char */
		protected String read( int n ) 
		{
			if ( idx + n > str.length() )
				throw new StreamerException( "Unexpected end of stream" );
			
			String s = str.substring( idx, idx+n );
			idx += n;
			return s;
		}
		
		
		public int readInt()
		{
			String s = readDelim();
			
			try {
				return Integer.parseInt( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}

		public long readLong() {
			String s = readDelim();
			
			try {
				return Long.parseLong( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public short readShort() {
			String s = readDelim();
			
			try {
				return Short.parseShort( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public byte readByte() {
			String s = readDelim();
			
			try {
				return Byte.parseByte( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public char readChar() {
			String s = read( 1 );
			
			try {
				return s.charAt( 0 );
			} catch ( Exception ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public boolean readBoolean() {
			String s = read( 1 );
			
			try {
				return s.charAt( 0 ) == 'T';
			} catch ( Exception ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public double readDouble() {
			String s = readDelim();
			
			try {
				return Double.parseDouble( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public float readFloat() {
			String s = readDelim();
			
			try {
				return Float.parseFloat( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public String readString()
		{
			int n = readInt();
			String s = read(n);
			return s;
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
		return new StrWriter();
	}



	@Override
	public Reader createReader(String str) {
		return new StrReader( str );
	}
}
