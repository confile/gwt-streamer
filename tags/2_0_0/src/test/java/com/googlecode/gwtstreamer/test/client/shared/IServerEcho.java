package com.googlecode.gwtstreamer.test.client.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("echo")
public interface IServerEcho extends RemoteService {
	String echo( String message );
}
