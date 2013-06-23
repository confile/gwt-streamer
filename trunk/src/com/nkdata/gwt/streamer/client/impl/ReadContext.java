package com.nkdata.gwt.streamer.client.impl;

import java.util.ArrayList;
import java.util.List;

public class ReadContext {
	private List<Object> refs = new ArrayList<Object>( 50 );
	
	public ReadContext() {}
	
	public ReadContext( ReadContext init ) {
		this.refs.addAll( init.refs );
	}
	
	public Object getRef( int idx ) {
		return refs.get( idx );
	}
	
	public void addRef( Object obj ) {
		refs.add( obj );
	}
	
	public boolean containsRef( Object obj ) {
		return refs.contains( obj );
	}
}
