package com.googlecode.gwtstreamer.test.client.shared;

import java.util.HashMap;
import java.util.Map;

public class PrivateHashMap implements Iface1 {
	 private HashMap<String, Integer> clientToVersions = new HashMap<String, Integer>();
	 
	 @Override
	public boolean equals(Object obj) {
		return toString().equals( obj.toString() );
	}
	 
	 @Override
	public String toString() {
		 StringBuffer sb = new StringBuffer();
		 for ( Map.Entry<String, Integer> e : clientToVersions.entrySet() ) {
			 sb.append( e.getKey() ).append( ":" ).append( e.getValue() ).append( "\n" );
		 }
		 
		 return sb.toString();
	}
	 
	 public void add( String key, int val ) {
		 clientToVersions.put( key, val );
	 }
}
