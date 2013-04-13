package com.nkdata.gwt.streamer.test.client;

import com.nkdata.gwt.streamer.client.Streamer;
import com.nkdata.gwt.streamer.client.impl.Base64StreamFactory;
import com.nkdata.gwt.streamer.test.client.shared.Benchmark;

import junit.framework.TestCase;

public class ServerBenchmark extends TestCase {
	private void log( String s ) {
		System.out.println( s );
	}
	
	public void setUp() {
		//Streamer.streamFactory = new Base64StreamFactory();
		// warm up VM
		Benchmark bench = new Benchmark( 10 );
		bench.serializeBenchmark();
		bench.deserializeBenchmark();
	}
	
	/**
	 * Simple serialization serialization
	 */
	public void testTask() throws Exception
	{
		log( "Preparing test..." );
		Benchmark bench = new Benchmark( 18 );
		log( "    Tree depth: "+bench.getTreeDepth() );
		log( "    Node count: "+bench.getTreeCreator().getNodeCount() );
		log( "Executing serialization..." );
		long t = bench.serializeBenchmark();
		log( "Serialization time: "+t );
		log( "    Buffer length: "+bench.getBuffer().length() );
		log( "Executing deserialization..." );
		t = bench.deserializeBenchmark();
		log( "Deserialization time: "+t );
	}
}
