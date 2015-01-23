package com.googlecode.gwtstreamer.server;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerConfig;
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
	
	
	public StreamerFactory(StreamerConfig config)
	{
		synchronized ( StreamerFactory.class ) {
			checkConfigured();
			Streamer.applyConfig(config);
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
