package com.googlecode.gwtstreamer.client.std;

import com.googlecode.gwtstreamer.client.impl.ReadContext;
import com.googlecode.gwtstreamer.client.impl.WriteContext;

import java.util.*;

public class CollectionStreamers {
	//ArrayList
	public static class ArrayListStreamer extends CollectionStreamer {
		@Override
		protected Collection<Object> createCollectionInstance(int length) {
			return new ArrayList<Object>( length );
		}
	}
	
	// LinkedList
	public static class LinkedListStreamer extends CollectionStreamer {
		@Override
		protected Collection<Object> createCollectionInstance(int length) {
			return new LinkedList<Object>();
		}
	}
	
	// HashSet
	public static class HashSetStreamer extends CollectionStreamer {
		@Override
		protected Collection<Object> createCollectionInstance(int length) {
			return new HashSet<Object>( length*4/3+1 );
		}
	}
	
	// LinkedHashSet
	public static class LinkedHashSetStreamer extends CollectionStreamer {
		@Override
		protected Collection<Object> createCollectionInstance(int length) {
			return new LinkedHashSet<Object>( length*4/3+1 );
		}
	}
	
	
	// TreeSet
	public static class TreeSetStreamer extends CollectionStreamer
	{
		@Override
		protected void writeObjectData(Collection<?> obj, WriteContext ctx)
		{
			ctx.writeObject(((TreeSet<?>) obj).comparator());
			super.writeObjectData(obj, ctx);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public Object readObject(ReadContext ctx)
		{
			Comparator<Object> cmp = (Comparator<Object>) ctx.readObject();
			int length = ctx.readInt();
			Collection<Object> obj = new TreeSet<Object>( cmp );
			ctx.addObject(obj);
			readObjectData( obj, ctx, length );
			return obj;
		}

		@Override
		protected Collection<Object> createCollectionInstance(int length) {
			throw new RuntimeException();
		}
	}
	
	
	// Vector
	public static class VectorStreamer extends CollectionStreamer {
		@Override
		protected Collection<Object> createCollectionInstance(int length) {
			return new Vector<Object>( length );
		}
	}
	

	// HashMap
	public static class HashMapStreamer extends MapStreamer {
		@Override
		protected Map<Object, Object> createMapInstance( int length ) {
			return new HashMap<Object, Object>( length*4/3+1 );
		}
	}
	
	// IdentityHashMap
	public static class IdentityHashMapStreamer extends MapStreamer {
		@Override
		protected Map<Object, Object> createMapInstance( int length ) {
			return new IdentityHashMap<Object, Object>( length*4/3+1 );
		}
	}
	
	
	// LinkedHashMap
	public static class LinkedHashMapStreamer extends MapStreamer {
		@Override
		protected Map<Object, Object> createMapInstance( int length ) {
			return new LinkedHashMap<Object, Object>( length*4/3+1 );
		}
	}
	
	// TreeMap
	public static class TreeMapStreamer extends MapStreamer 
	{
		@Override
		protected void writeObjectData(Map<?, ?> obj, WriteContext ctx)
		{
			ctx.writeObject(((TreeMap<?, ?>) obj).comparator());
			super.writeObjectData(obj, ctx);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public Object readObject(ReadContext ctx)
		{
			Comparator<Object> cmp = (Comparator<Object>) ctx.readObject();
			int length = ctx.readInt();
			Map<Object,Object> obj = new TreeMap<Object, Object>( cmp );
			ctx.addObject(obj);
			readObjectData( obj, ctx, length );
			return obj;
		}
		
		@Override
		protected Map<Object, Object> createMapInstance( int length ) {
			throw new RuntimeException();
		}
	}
}
