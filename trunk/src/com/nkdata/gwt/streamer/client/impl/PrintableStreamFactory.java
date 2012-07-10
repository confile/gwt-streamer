package com.nkdata.gwt.streamer.client.impl;

import com.google.gwt.http.client.URL;
import com.nkdata.gwt.streamer.client.StreamFactory;
import com.nkdata.gwt.streamer.client.StreamerException;


/**
 * Stream has a textual representation separated with | (pipe) symbol
 * @author Anton
 */
public class PrintableStreamFactory implements StreamFactory
{
	final static char DELIMITER = '/';
	
	public static class PipeWriter implements com.nkdata.gwt.streamer.client.StreamFactory.Writer
	{
		private StringBuffer buf = new StringBuffer( 256 ); 
		
		protected void write( String rawString )
		{
			buf.append( rawString ).append( DELIMITER );
		}
		
		public String toString() {
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
			write( String.valueOf( val ) );
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
			/*StringBuffer sb = new StringBuffer();
			
			for ( int i = 0; i < val.length(); i++ ) {
				char ch = val.charAt(i);
				
				if ( ch == DELIMITER ) {
					sb.append( DELIMITER );
				}
				
				sb.append( ch );
			}*/
			String s = StreamerInternal.urlEncode( val );
			write( s );
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
				return s.charAt( 0 );
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
			return StreamerInternal.urlDecode( s );
			
			/*StringBuffer sb = new StringBuffer();
			boolean wasDelim = false;
			
			// unescaping double delimiters
			for ( int i = 0; i < s.length(); i++ ) {
				char ch = s.charAt(i);
				
				if ( ch == DELIMITER ) {
					if ( !wasDelim ) {
						wasDelim = true;
					} else {
						// delimiter as normal character
						sb.append( DELIMITER );
					}
				}
			}*/
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
		return new PipeWriter();
	}



	@Override
	public Reader createReader(String str) {
		return new PipeReader( str );
	}
}
