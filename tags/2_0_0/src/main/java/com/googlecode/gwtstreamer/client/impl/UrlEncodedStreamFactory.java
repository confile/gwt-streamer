package com.googlecode.gwtstreamer.client.impl;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.StreamerException;

public class UrlEncodedStreamFactory implements StreamFactory
{
	private final static char DELIMITER = '_';
	
	public static class StrWriter implements Writer
	{
		private StringBuffer buf = new StringBuffer( 256 ); 
		
		
		protected void write( String rawString )
		{
			buf.append( rawString );
		}
		
		protected void writeDelim( String numString ) {
			buf.append( numString ).append( DELIMITER );
		}
		
		
		public String getData() {
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


		@Override
		public int getSizeLimit() {
			return str.length();
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
			if (n > getSizeLimit())
				throw new StreamerException("String length exceeds stream size: "+n);
			String s = read(n);
			return s;
		}
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
