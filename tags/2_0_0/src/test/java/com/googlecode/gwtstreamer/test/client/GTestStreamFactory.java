package com.googlecode.gwtstreamer.test.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.impl.Base64PackedStreamFactory;
import com.googlecode.gwtstreamer.client.impl.Base64StreamFactory;
import com.googlecode.gwtstreamer.client.impl.PrintableStreamFactory;
import com.googlecode.gwtstreamer.client.impl.UrlEncodedStreamFactory;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by akuranov on 26/01/2015.
 */
public class GTestStreamFactory extends GWTTestCase
{
    @Override
    public String getModuleName() {
        return "com.googlecode.gwtstreamer.test.GWTStreamerJUnit";
    }

    static void log( String s ) {
        GWT.log(s);
    }


    public void testBase64PackedStreamFactory() {
        new TestStreamFactory.StreamFactoryTest(new Base64PackedStreamFactory()).execute();
    }

    public void testPrintablePackedStreamFactory() {
        new TestStreamFactory.StreamFactoryTest(new PrintableStreamFactory()).execute();
    }

    public void testUrlEncodedStreamFactory() {
        new TestStreamFactory.StreamFactoryTest(new UrlEncodedStreamFactory()).execute();
    }

    public void testBase64StreamFactory() {
        new TestStreamFactory.StreamFactoryTest(new Base64StreamFactory()).execute();
    }
}
