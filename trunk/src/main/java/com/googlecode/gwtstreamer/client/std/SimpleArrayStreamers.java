package com.googlecode.gwtstreamer.client.std;

import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.impl.ReadContext;
import com.googlecode.gwtstreamer.client.impl.WriteContext;

public class SimpleArrayStreamers {
	public static class IntArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx)
		{
			ctx.addObject(obj);
			int[] val = (int[]) obj;
			ctx.writeInt(val.length);
			for ( int v : val )
				ctx.writeInt( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			int[] buf = new int[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readInt();
			return buf;
		}
	}

	public static class LongArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			long[] val = (long[]) obj;
			ctx.writeInt(val.length);
			for ( long v : val )
				ctx.writeLong( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			long[] buf = new long[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readLong();
			return buf;
		}
	}

	public static class ShortArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			short[] val = (short[]) obj;
			ctx.writeInt(val.length);
			for ( short v : val )
				ctx.writeShort( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			short[] buf = new short[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readShort();
			return buf;
		}
	}

	public static class ByteArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			byte[] val = (byte[]) obj;
			ctx.writeInt(val.length);
			for ( byte v : val )
				ctx.writeByte( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			byte[] buf = new byte[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readByte();
			return buf;
		}
	}

	public static class DoubleArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			double[] val = (double[]) obj;
			ctx.writeInt( val.length );
			for ( double v : val )
				ctx.writeDouble( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			double[] buf = new double[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readDouble();
			return buf;
		}
	}
	
	public static class FloatArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			float[] val = (float[]) obj;
			ctx.writeInt(val.length);
			for ( float v : val )
				ctx.writeFloat( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			float[] buf = new float[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readFloat();
			return buf;
		}
	}

	public static class CharArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			char[] val = (char[]) obj;
			ctx.writeString(new String(val));
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			char[] buf = ctx.readString().toCharArray();
			ctx.addObject(buf);
			return buf;
		}
	}
	
	public static class BooleanArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			boolean[] val = (boolean[]) obj;
			ctx.writeInt(val.length);
			for ( boolean v : val )
				ctx.writeBoolean( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			boolean[] buf = new boolean[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readBoolean();
			return buf;
		}
	}

	public static class ObjectArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			Object[] val = (Object[]) obj;
			ctx.writeInt(val.length);
			for ( Object v : val )
				ctx.writeObject(v);
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			Object[] buf = new Object[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readObject();
			return buf;
		}
	}
	
	
	public static class StringArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx )
		{
			ctx.addObject(obj);
			String[] val = (String[]) obj;
			ctx.writeInt(val.length);
			for ( String v : val )
				ctx.writeString( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx )
		{
			int n = ctx.readInt();
			String[] buf = new String[n];
			ctx.addObject(buf);
			for ( int i = 0; i < n; i++ )
				buf[i] = ctx.readString();
			return buf;
		}
	}
}
