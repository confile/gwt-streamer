package com.nkdata.gwt.streamer.test.client.shared;

import com.nkdata.gwt.streamer.client.Streamer;


public class Benchmark
{
	private int treeDepth;
	private String buffer;
	private Node tree;
	private TreeCreator treeCreator;
	
	public Benchmark( int treeDepth )
	{
		this.treeDepth = treeDepth;
		this.treeCreator = new TreeCreator( treeDepth );
		this.tree = treeCreator.createTree();
	}
	
	
	public long serializeBenchmark()
	{
		long t = System.currentTimeMillis();
		this.buffer = Streamer.get().toString( tree );
		return System.currentTimeMillis() - t;
	}


	public long deserializeBenchmark()
	{
		long t = System.currentTimeMillis();
		Streamer.get().fromString( buffer );
		return System.currentTimeMillis() - t;
	}


	public int getTreeDepth() {
		return treeDepth;
	}


	public String getBuffer() {
		return buffer;
	}


	public Node getTree() {
		return tree;
	}


	public TreeCreator getTreeCreator() {
		return treeCreator;
	}
}
