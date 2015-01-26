package com.googlecode.gwtstreamer.test.client;

import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by akuranov on 26/01/2015.
 */
public class GwtAllTestsSuite extends GWTTestSuite {
    public static Test suite()
    {
        TestSuite suite = new TestSuite("All GWT tests");
        suite.addTestSuite(GTestStreamFactory.class);
        suite.addTestSuite(GTestClientStreamer.class);
        suite.addTestSuite(GTestEchoStreamer.class);
        return suite;
    }
}
