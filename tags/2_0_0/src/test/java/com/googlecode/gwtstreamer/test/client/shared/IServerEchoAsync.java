package com.googlecode.gwtstreamer.test.client.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IServerEchoAsync {

	void echo(String message, AsyncCallback<String> callback);

}
