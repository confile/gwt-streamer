package com.googlecode.gwtstreamer.client.std;

import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.impl.ReadContext;
import com.googlecode.gwtstreamer.client.impl.WriteContext;

import java.util.Map;

public abstract class MapStreamer extends Streamer {
	@Override
	public void writeObject( Object obj, WriteContext ctx )
	{
		ctx.addObject(obj);
		writeObjectData( (Map<?,?>) obj, ctx );
	}
	
	
	@Override
	public Object readObject( ReadContext ctx )
	{
		int length = ctx.readInt();
		Map<Object,Object> obj = createMapInstance(length);
		ctx.addObject(obj);
		readObjectData( obj, ctx, length );
		return obj;
	}
	
	
	protected void writeObjectData( Map<?,?> obj, WriteContext ctx )
	{
		ctx.writeInt( obj.size() );
		
		for ( Map.Entry<?, ?> o : obj.entrySet() ) {
			ctx.writeObject(o.getKey());
			ctx.writeObject(o.getValue());
		}
	}

	
	protected void readObjectData( Map<Object,Object> obj, ReadContext ctx, int length )
	{
		for ( int i = 0; i < length; i++ ) {
			obj.put( ctx.readObject(), ctx.readObject() );
		}
	}
	
	protected abstract Map<Object,Object> createMapInstance( int length );
}
