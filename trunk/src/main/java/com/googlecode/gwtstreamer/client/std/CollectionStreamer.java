package com.googlecode.gwtstreamer.client.std;

import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.impl.ReadContext;
import com.googlecode.gwtstreamer.client.impl.WriteContext;

import java.util.Collection;

public abstract class CollectionStreamer extends Streamer
{
	@Override
	public void writeObject( Object obj, WriteContext ctx )
	{
		ctx.addObject(obj);
		writeObjectData( (Collection<?>) obj, ctx );
	}
	
	
	@Override
	public Object readObject( ReadContext ctx )
	{
		int length = ctx.readInt();
		Collection<Object> obj = createCollectionInstance(length);
		ctx.addObject(obj);
		readObjectData( obj, ctx, length );
		return obj;
	}
	
	
	protected void writeObjectData( Collection<?> obj, WriteContext ctx )
	{
		ctx.writeInt( obj.size() );
		
		for ( Object o : obj )
		ctx.writeObject( o );
	}

	
	protected void readObjectData( Collection<Object> obj, ReadContext ctx, int length )
	{
		for ( int i = 0; i < length; i++ ) {
			obj.add( ctx.readObject() );
		}
	}
	

	protected abstract Collection<Object> createCollectionInstance( int length );
}
