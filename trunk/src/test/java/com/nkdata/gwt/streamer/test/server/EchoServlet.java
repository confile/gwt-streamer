package com.nkdata.gwt.streamer.test.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nkdata.gwt.streamer.client.Streamer;
import com.nkdata.gwt.streamer.test.client.shared.IServerEcho;

public class EchoServlet extends RemoteServiceServlet implements IServerEcho {
	@Override
	public String echo( String message )
	{
		//return message;
		Object o = Streamer.get().fromString( message );
		return Streamer.get().toString( o );
	}
}
