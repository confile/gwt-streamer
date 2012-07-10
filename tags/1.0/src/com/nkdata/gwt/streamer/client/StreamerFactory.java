package com.nkdata.gwt.streamer.client;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * This class provides a thread-safe IoC-friendly factory wrapper for Streamer object.
 * Factory must be one-per-application. Factory returns configured singleton instance of Streamer.  
 * @author Anton
 */
public class StreamerFactory
{
	private boolean configured = false;
	
	
	public StreamerFactory()
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			Streamer.get();
			configured = true;
		}
	}
	
	
	public StreamerFactory( Map<Class<?>,Streamer> customStreamers )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			for ( Map.Entry<Class<?>,Streamer> entry : customStreamers.entrySet() )
				Streamer.registerStreamer( entry.getClass(), entry.getValue() );
			
			Streamer.get();
			configured = true;
		}
	}


	public StreamerFactory( Collection<Class<?>> aliasList )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			for ( Class<?> cl : aliasList ) {
				Streamer.createAlias( cl );
			}
			
			Streamer.get();
			configured = true;
		}
	}
	
	
	public StreamerFactory( Collection<Class<?>> aliasList, Map<Class<?>,Streamer> customStreamers )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			for ( Map.Entry<Class<?>,Streamer> entry : customStreamers.entrySet() )
				Streamer.registerStreamer( entry.getClass(), entry.getValue() );
			
			for ( Class<?> cl : aliasList ) {
				Streamer.createAlias( cl );
			}
			
			Streamer.get();
			configured = true;
		}
	}
	
	
	public StreamerFactory( Properties customStreamers )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			for ( Map.Entry<Object,Object> entry : customStreamers.entrySet() ) {
				Class<?> cl;
				try {
					cl = Class.forName( (String) entry.getKey() );
				} catch ( Exception ex ) {
					throw new StreamerException( "Class not found: "+entry.getKey(), ex );
				}
				Class<?> st;
				Streamer iSt;
				try {
					st = Class.forName( (String) entry.getValue() );
					iSt = (Streamer) st.newInstance();
				} catch ( Exception ex ) {
					throw new StreamerException( "Can not instantiate streamer class: "+entry.getValue(), ex );
				}
				
				Streamer.registerStreamer( cl, iSt );
			}
			
			Streamer.get();
			configured = true;
		}
	}


	public StreamerFactory( String[] aliasList )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			for ( String s : aliasList ) {
				Class<?> cl;
				try {
					cl = Class.forName( s );
				} catch ( Exception ex ) {
					throw new StreamerException( "Class not found: "+s, ex );
				}
				
				Streamer.createAlias( cl );
			}
			
			Streamer.get();
			configured = true;
		}
	}
	
	
	public StreamerFactory( String[] aliasList, Properties customStreamers )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			for ( Map.Entry<Object,Object> entry : customStreamers.entrySet() ) {
				Class<?> cl;
				try {
					cl = Class.forName( (String) entry.getKey() );
				} catch ( Exception ex ) {
					throw new StreamerException( "Class not found: "+entry.getKey(), ex );
				}
				Class<?> st;
				Streamer iSt;
				try {
					st = Class.forName( (String) entry.getValue() );
					iSt = (Streamer) st.newInstance();
				} catch ( Exception ex ) {
					throw new StreamerException( "Can not instantiate streamer class: "+entry.getValue(), ex );
				}
				
				Streamer.registerStreamer( cl, iSt );
			}
			
			for ( String s : aliasList ) {
				Class<?> cl;
				try {
					cl = Class.forName( s );
				} catch ( Exception ex ) {
					throw new StreamerException( "Class not found: "+s, ex );
				}
				
				Streamer.createAlias( cl );
			}
			
			Streamer.get();
			configured = true;
		}
	}
	
	
	private void checkConfigured() {
		if ( configured )
			throw new StreamerException( "StreamerFactory must only have one instance per application" );
	}
	
	
	public Streamer getInstance() {
		return Streamer.get();
	}
}
