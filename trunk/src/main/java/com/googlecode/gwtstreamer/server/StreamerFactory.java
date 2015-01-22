package com.googlecode.gwtstreamer.server;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerException;

/**
 * This class provides a thread-safe IoC-friendly factory wrapper for Streamer object.
 * Factory must be one-per-application. Factory returns configured singleton instance of Streamer.  
 * @author Anton
 */
public class StreamerFactory
{
	private static volatile boolean configured = false;
	
	
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


	public StreamerFactory( Collection<Class<?>> classList )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			for ( Class<?> cl : classList ) {
				Streamer.registerClass(cl);
			}
			
			Streamer.get();
			configured = true;
		}
	}
	
	
	public StreamerFactory( Collection<Class<?>> classList, Collection<String> packageList,
							Map<Class<?>,Streamer> customStreamers, StreamFactory streamFactory  )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			if ( customStreamers != null ) {
				for (Map.Entry<Class<?>, Streamer> entry : customStreamers.entrySet())
					Streamer.registerStreamer(entry.getClass(), entry.getValue());
			}

			if ( classList != null ) {
				for (Class<?> cl : classList) {
					Streamer.registerClass(cl);
				}
			}

			if ( packageList != null ) {
				for (String s : packageList) {
					Streamer.registerPackage(s);
				}
			}

			if ( streamFactory != null )
				Streamer.switchToStreamFactory( streamFactory );

			Streamer.get();
			configured = true;
		}
	}
	
	
	public StreamerFactory( String[] classList, String[] packageList )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			if ( classList != null ) {
				for (String s : classList) {
					Class<?> cl;
					try {
						cl = Class.forName(s);
					} catch (Exception ex) {
						throw new StreamerException("Class not found: " + s, ex);
					}

					Streamer.registerClass(cl);
				}
			}

			if ( packageList != null ) {
				for (String s : packageList) {
					Streamer.registerPackage(s);
				}
			}

			Streamer.get();
			configured = true;
		}
	}
	
	
	public StreamerFactory( String[] classList, String[] packageList, Properties customStreamers )
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			if ( customStreamers != null ) {
				for (Map.Entry<Object, Object> entry : customStreamers.entrySet()) {
					Class<?> cl;
					try {
						cl = Class.forName((String) entry.getKey());
					} catch (Exception ex) {
						throw new StreamerException("Class not found: " + entry.getKey(), ex);
					}
					Class<?> st;
					Streamer iSt;
					try {
						st = Class.forName((String) entry.getValue());
						iSt = (Streamer) st.newInstance();
					} catch (Exception ex) {
						throw new StreamerException("Can not instantiate streamer class: " + entry.getValue(), ex);
					}

					Streamer.registerStreamer(cl, iSt);
				}
			}

			if ( classList != null ) {
				for (String s : classList) {
					Class<?> cl;
					try {
						cl = Class.forName(s);
					} catch (Exception ex) {
						throw new StreamerException("Class not found: " + s, ex);
					}

					Streamer.registerClass(cl);
				}
			}

			if ( packageList != null ) {
				for (String s : packageList) {
					Streamer.registerPackage(s);
				}
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
