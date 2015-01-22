package com.googlecode.gwtstreamer.client.std;

import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.impl.ReadContext;
import com.googlecode.gwtstreamer.client.impl.WriteContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SimpleStreamers {
	public static class IntegerStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx ) {
			ctx.writeInt((Integer) obj);
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			return ctx.readInt();
		}
	}

	
	public static class ShortStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx ) {
			ctx.writeShort((Short) obj);
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			return ctx.readShort();
		}
	}

	
	public static class ByteStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx ) {
			ctx.writeByte((Byte) obj);
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			return ctx.readByte();
		}
	}

	
	public static class LongStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx ) {
			ctx.writeLong((Long) obj);
		}
		
		@Override
		public Object readObject( ReadContext ctx)
		{
			return ctx.readLong();
		}
	}

	
	public static class DoubleStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx) {
			ctx.writeDouble((Double) obj);
		}
		
		@Override
		public Object readObject( ReadContext ctx )	{ return ctx.readDouble(); }
	}

	
	public static class FloatStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx ) {
			ctx.writeFloat((Float) obj);
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			return ctx.readFloat();
		}
	}

	
	public static class CharStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx ) { ctx.writeChar( (Character) obj ); }
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			return ctx.readChar();
		}
	}

	
	public static class BooleanStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx ) {
			ctx.writeBoolean((Boolean) obj);
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			return ctx.readBoolean();
		}
	}

	
	public static class StringStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			ctx.writeString( (String) obj );
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			String s = ctx.readString();
			ctx.addObject(s);
			return s;
		}
	}

	
	public static class DateStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.writeLong(((Date) obj).getTime());
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			long time = ctx.readLong();
			return new Date( time );
		}
	}
	
	
	public static class BigIntegerStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.writeString(((BigInteger) obj).toString());
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			String s = ctx.readString();
			return new BigInteger( s );
		}
	}
	
	
	public static class BigDecimalStreamer extends Streamer {
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.writeString(((BigDecimal) obj).toString());
		}
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			String s = ctx.readString();
			return new BigDecimal( s );
		}
	}
}
