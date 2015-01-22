package com.googlecode.gwtstreamer.test.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.test.client.shared.IServerEcho;

public class EchoServlet extends RemoteServiceServlet implements IServerEcho {
	@Override
	public String echo( String message )
	{
		//return message;
		Object o = Streamer.get().fromString( message );
		return Streamer.get().toString( o );
	}
}
