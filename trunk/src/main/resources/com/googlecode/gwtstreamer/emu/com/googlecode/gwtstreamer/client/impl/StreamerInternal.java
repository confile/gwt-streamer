package com.googlecode.gwtstreamer.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.googlecode.gwtstreamer.client.Streamer;

import java.util.Map;

/** 
 * This is a client-side implementation of the class.
 */
public class StreamerInternal
{
	/**
	 * Initially configured GWT streamers
	 * class name -> class streamer
	 * Do not touch this field!!!
	 */
	public static Map<String,Streamer> INITIAL_STREAMERS;

	private static Streamer rootStreamer = GWT.create( Streamer.class );
	
	public static Streamer getRootStreamer() {
		return rootStreamer;
	}
	
	/**
	 * @return always null. In client mode all streamers must be generated and registered statically 
	 */
	public static Streamer createStreamerFor( String className )
	{
		return null;
	}


	public static String urlEncode( String s ) {
		return URL.encodeQueryString( s );
	}

	
	public static String urlDecode( String s ) {
		return URL.decodeQueryString( s );
	}

	public static void log( String message ) {
		GWT.log( message );
	}

	public static boolean isGWT() { return true; }
}
