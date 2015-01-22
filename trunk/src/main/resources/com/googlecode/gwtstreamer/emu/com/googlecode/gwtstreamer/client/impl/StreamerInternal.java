package com.googlecode.gwtstreamer.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.googlecode.gwtstreamer.client.Streamer;

/** 
 * This is a client-side implementation of the class.
 */
public class StreamerInternal
{
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
}
