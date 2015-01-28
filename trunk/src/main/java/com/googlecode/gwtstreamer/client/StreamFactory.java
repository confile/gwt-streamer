package com.googlecode.gwtstreamer.client;

/**
 * Interface for implementing of different serialization methods.
 * Use Streamer.switchToStreamFactory() to switch to new implementation.
 */
public interface StreamFactory {
	public interface Writer {
		public String getData();
		public void writeInt( int val );
		public void writeLong( long val );
		public void writeShort( short val );
		public void writeByte( byte val );
		public void writeChar( char val );
		public void writeBoolean( boolean val );
		public void writeDouble( double val );
		public void writeFloat( float val );
		public void writeString( String val );
	}
	
	
	public interface Reader {
		public int getSizeLimit();
		public boolean hasMore();
		public int readInt();
		public long readLong();
		public short readShort();
		public byte readByte();
		public char readChar();
		public boolean readBoolean();
		public double readDouble();
		public float readFloat();
		public String readString();
	}
	
	
	Writer createWriter();
	Reader createReader( String buffer );
}
