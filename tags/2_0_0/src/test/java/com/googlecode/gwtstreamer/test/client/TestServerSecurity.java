package com.googlecode.gwtstreamer.test.client;

import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerConfig;
import com.googlecode.gwtstreamer.client.StreamerException;
import com.googlecode.gwtstreamer.client.impl.PrintableStreamFactory;
import com.googlecode.gwtstreamer.test.client.shared.SerBean;
import com.googlecode.gwtstreamer.test.client.shared.SimpleBean;
import junit.framework.TestCase;

/**
 * Created by akuranov on 28/01/2015.
 */
public class TestServerSecurity extends TestCase
{
    @Override
    protected void setUp() throws Exception {
        StreamerConfig cfg = new StreamerConfig();
        cfg.setStreamFactory(new PrintableStreamFactory());
        cfg.setClassRestrictionPolicy(
                "^com\\.googlecode\\.gwtstreamer\\.test\\.client\\.shared\\.(SimpleBean|MultiArray)" );
        Streamer.applyConfig(cfg);
    }

    @Override
    public void tearDown() throws Exception {
        Streamer.applyConfig(new StreamerConfig());
    }

    public void testAllowedClassesPolicy() {
        SimpleBean b = new SimpleBean(1,"abc");
        String s = Streamer.get().toString(b);
        System.out.println(s);
        s = Streamer.get().toString("abcd");
        System.out.println(s);
        s = Streamer.get().toString(1);
        System.out.println(s);
        s = Streamer.get().toString(new String[] {"abcd"} );
        System.out.println(s);
        s = Streamer.get().toString(new int[] {0,1,2,3,4} );
        System.out.println(s);
    }

    public void testRestrictedClassError() {
        try {
            Streamer.get().toString(new SerBean());
            fail();
        } catch ( StreamerException ex ) {}
    }


    public void testReadRestrictedClassError() {
        try {
            Streamer.get().fromString("1044854182 6 com.googlecode.gwtstreamer.test.client.shared.SerBean -6 45 26 0 0 26 0 0");
            fail();
        } catch ( StreamerException ex ) {}
    }


    public void testArraySize() {
        try {
            Streamer.get().fromString("1044854182 43 1000 0 1 2 3 4 ");
            fail();
        } catch ( StreamerException ex ) {}
    }
}
