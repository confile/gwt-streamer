package com.nkdata.gwt.streamer.test.client.shared;

import com.nkdata.gwt.streamer.client.Streamable;

public class ParentBean implements Streamable {
	private ChildBean child;
	private String name;

	public ChildBean getChild() {
		return child;
	}

	public void setChild(ChildBean child) {
		this.child = child;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals( obj.toString() );
	}
	
	@Override
	public String toString() {
		return "name:"+name+",child:"+child.toString();
	}
}
