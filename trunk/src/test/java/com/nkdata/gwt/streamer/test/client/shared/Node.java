package com.nkdata.gwt.streamer.test.client.shared;

import com.nkdata.gwt.streamer.client.Streamable;


public class Node implements Streamable {
	public String value;
	
	private Node() {}
	
	public Node( String value ) {
		this.value = value;
	}
	
	public Node left;
	public Node right;
	
	@Override
	public boolean equals( Object o1 ) {
		Node n = (Node) o1;
		
		return n != null && value.equals( n.value );
	}
}
