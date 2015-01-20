package com.nkdata.gwt.streamer.test.client.shared;

import com.nkdata.gwt.streamer.client.Streamable;

public class ChildBean implements Streamable {
	private ParentBean parent;
	private String name;

	public ParentBean getParent() {
		return parent;
	}

	public void setParent(ParentBean parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		return "name:"+name;
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
}
