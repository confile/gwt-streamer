package com.googlecode.gwtstreamer.test.client.shared;

/**
 * @author anton
 *
 */
public class TreeCreator
{
	private int maxDepth;
	private int nodeCount;
	
	public TreeCreator( int depth ) {
		this.maxDepth = depth;
	}
	
	/**
	 * Create balanced binary tree of N-th deep 
	 * @param deep
	 * @return
	 */
	public Node createTree()
	{
		return createNode( 0 );
	}
	
	
	private Node createNode( int deep )
	{
		Node n = new Node( "Node "+nodeCount++ );
		
		if ( deep < maxDepth ) {
			n.left = createNode( deep+1 );
			n.right = createNode( deep+1 );
		}
		
		return n;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public int getNodeCount() {
		return nodeCount;
	}
}
