package com.nkdata.gwt.streamer.client.std;

import com.nkdata.gwt.streamer.client.Streamer;
import com.nkdata.gwt.streamer.client.StreamFactory.Reader;
import com.nkdata.gwt.streamer.client.StreamFactory.Writer;
import com.nkdata.gwt.streamer.client.impl.ReadContext;
import com.nkdata.gwt.streamer.client.impl.WriteContext;

public class SimpleArrayStreamers {
	public static class IntArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			int[] val = (int[]) obj;
			out.writeInt( val.length );
			for ( int v : val )
				out.writeInt( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			int[] buf = new int[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readInt();
			return buf;
		}
	}

	public static class LongArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			long[] val = (long[]) obj;
			out.writeInt( val.length );
			for ( long v : val )
				out.writeLong( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			long[] buf = new long[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readLong();
			return buf;
		}
	}

	public static class ShortArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			short[] val = (short[]) obj;
			out.writeInt( val.length );
			for ( short v : val )
				out.writeShort( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			short[] buf = new short[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readShort();
			return buf;
		}
	}

	public static class ByteArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			byte[] val = (byte[]) obj;
			out.writeInt( val.length );
			for ( byte v : val )
				out.writeByte( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			byte[] buf = new byte[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readByte();
			return buf;
		}
	}

	public static class DoubleArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			double[] val = (double[]) obj;
			out.writeInt( val.length );
			for ( double v : val )
				out.writeDouble( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			double[] buf = new double[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readDouble();
			return buf;
		}
	}
	
	public static class FloatArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			float[] val = (float[]) obj;
			out.writeInt( val.length );
			for ( float v : val )
				out.writeFloat( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			float[] buf = new float[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readFloat();
			return buf;
		}
	}

	public static class CharArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			char[] val = (char[]) obj;
			out.writeString( new String( val ) );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			char[] buf = in.readString().toCharArray();
			ctx.addRef( buf );
			return buf;
		}
	}
	
	public static class BooleanArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			boolean[] val = (boolean[]) obj;
			out.writeInt( val.length );
			for ( boolean v : val )
				out.writeBoolean( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			boolean[] buf = new boolean[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readBoolean();
			return buf;
		}
	}

	public static class ObjectArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			Object[] val = (Object[]) obj;
			out.writeInt( val.length );
			for ( Object v : val )
				get().writeObject(v, ctx, out);
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			Object[] buf = new Object[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = get().readObject( ctx, in );
			return buf;
		}
	}
	
	
	public static class StringArrayStreamer extends Streamer
	{
		@Override
		public void writeObject( Object obj, WriteContext ctx, Writer out )
		{
			ctx.addRef( obj );
			String[] val = (String[]) obj;
			out.writeInt( val.length );
			for ( String v : val )
				out.writeString( v );
		}
		
		
		@Override
		public Object readObject( ReadContext ctx, Reader in )
		{
			int n = in.readInt();
			String[] buf = new String[n];
			ctx.addRef( buf );
			for ( int i = 0; i < n; i++ )
				buf[i] = in.readString();
			return buf;
		}
	}
}
