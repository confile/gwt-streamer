package com.nkdata.gwt.streamer.test.client.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.nkdata.gwt.streamer.client.Streamable;

public class EnumCollections implements Streamable 
{
	public enum State {
		connected, connecting, disconnected, authenticating
	}
	
	public enum Sex {
		male, female
	}
	
	
	public enum Music {
		rock, pop, jazz, electronic
	}
	
	public enum Electronic {
		breaks, dubstep, house, trance
	}
	
	public List<State> connections = new ArrayList<State>();
	public Map<String,Sex> personSex = new TreeMap<String, Sex>();
	public Map<Music,String> music = new TreeMap<EnumCollections.Music, String>();
	public Map<String,List<Electronic>> personMusic = new TreeMap<String,List<Electronic>>();
	
	@Override
	public boolean equals(Object obj) {
		return toString().equals( obj.toString() );
	}
	 
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		writeList( sb, connections );
		writeMap( sb, personSex );
		writeMap( sb, music );
		writeMap( sb, personMusic );
		return sb.toString();
	}
	
	
	private void writeList( StringBuffer sb, List<?> list ) {
		sb.append( '[' );
		for ( Object o : list ) {
			sb.append( o ).append( ", ");
		}
		sb.append( ']' );
	}
	
	
	private void writeMap( StringBuffer sb, Map<?,?> map ) {
		sb.append( '[' );
		 for ( Map.Entry<?, ?> e : map.entrySet() ) {
			 sb.append( e.getKey() ).append( ":" ).append( e.getValue() ).append( ", ");
		 }
			sb.append( ']' );
	}
}
