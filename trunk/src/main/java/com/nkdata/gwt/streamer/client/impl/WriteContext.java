package com.nkdata.gwt.streamer.client.impl;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class WriteContext {
	private Map<Object,Integer> refs = new IdentityHashMap<Object,Integer>( 30 );
	private Map<Object,Integer> refsImm = new HashMap<Object, Integer>( 50 );
	
	public WriteContext() {}

	public WriteContext( WriteContext init ) {
		this.refs.putAll( init.refs );
		this.refsImm.putAll( init.refsImm );
	}
	
	public Integer getRefIdx( Object obj )
	{
		if ( obj.getClass().getName().startsWith( "java." ) ) {
			// java core classes are immutable - put them to normal hashmap
			return refsImm.get( obj );
		} else {
			return refs.get( obj );
		}
	}
	
	public void addRef( Object obj )
	{
		int idx = refs.size()+refsImm.size();
		Integer old;
		
		if ( obj.getClass().getName().startsWith( "java." ) ) {
			old = refsImm.put( obj, idx );
		} else {
			old = refs.put( obj, idx );
		}
		
		if ( old != null ) {
			refs.put( obj, old );
			throw new IllegalStateException( "Reference to object already exists: "+obj );
		}
	}
}
