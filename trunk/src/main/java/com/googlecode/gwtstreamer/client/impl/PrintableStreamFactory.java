package com.googlecode.gwtstreamer.client.impl;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.StreamerException;


/**
 * Stream has a textual representation separated with | (pipe) symbol
 * @author Anton
 */
public class PrintableStreamFactory implements StreamFactory
{
	private final static char DELIMITER = ' ';
	
	public static class PipeWriter implements Writer
	{
		private StringBuffer buf = new StringBuffer( 256 ); 
		
		protected void write( String rawString )
		{
			buf.append( rawString ).append( DELIMITER );
		}
		
		public String getData() {
			return buf.toString();
		}
		
		
		public void writeInt( int val ) {
			write( String.valueOf( val ) );
		}

		public void writeLong( long val ) {
			write( String.valueOf( val ) );
		}
		
		public void writeShort( short val ) {
			write( String.valueOf( val ) );
		}
		
		public void writeByte( byte val ) {
			write( String.valueOf( val ) );
		}
		
		public void writeChar( char val ) {
			write( StreamerInternal.urlEncode(String.valueOf( val )) );
		}
		
		public void writeBoolean( boolean val ) {
			write( val ? "T" : "F" );
		}
		
		public void writeDouble( double val ) {
			write( String.valueOf( val ) );
		}
		
		public void writeFloat( float val ) {
			write( String.valueOf( val ) );
		}
		

		public void writeString( String val )
		{
			// escaping delimiter character with double delimiters
			String s = StreamerInternal.urlEncode(val);
			write( s );
		}
	}



	public static class PipeReader implements Reader
	{
		private final String str;
		
		private int start, end;
		
		
		public PipeReader( final String str ) {
			this.str = str;
			start = 0;
			end = str.indexOf( DELIMITER );
			if ( end < 0 ) end = str.length();
		}
		
		
		public boolean hasMore() {
			return start < str.length();
		}

		/**
		 * @return
		 * @throws StreamerException if no more strings available
		 */
		protected String readNext()
		{
			if ( !hasMore() )
				throw new StreamerException( "Unexpected end of stream" );
			
			String s = str.substring( start, end );
			start = end+1;
			end = str.indexOf( DELIMITER, start );
			if ( end < 0 ) end = str.length();
			return s;
		}
		
		
		public int readInt()
		{
			String s = readNext();
			
			try {
				return Integer.parseInt( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}

		public long readLong() {
			String s = readNext();
			
			try {
				return Long.parseLong( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public short readShort() {
			String s = readNext();
			
			try {
				return Short.parseShort( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public byte readByte() {
			String s = readNext();
			
			try {
				return Byte.parseByte( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public char readChar() {
			String s = readNext();
			
			try {
				return StreamerInternal.urlDecode(s).charAt( 0 );
			} catch ( Exception ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public boolean readBoolean() {
			String s = readNext();
			
			try {
				return s.charAt( 0 ) == 'T';
			} catch ( Exception ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public double readDouble() {
			String s = readNext();
			
			try {
				return Double.parseDouble( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public float readFloat() {
			String s = readNext();
			
			try {
				return Float.parseFloat( s );
			} catch ( NumberFormatException ex ) {
				throw new StreamerException( ex );
			}
		}
		
		public String readString()
		{
			String s = readNext();
			return StreamerInternal.urlDecode(s);
		}
	}



	@Override
	public Writer createWriter() {
		return new PipeWriter();
	}



	@Override
	public Reader createReader(String str) {
		return new PipeReader( str );
	}
}
