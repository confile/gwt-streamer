package com.googlecode.gwtstreamer.test.client.shared;

import com.googlecode.gwtstreamer.client.Streamable;

/**
 * Created by akuranov on 21/01/2015.
 */
public class ContainerClass implements Streamable {
    public String name;
    public String surname;

    public static class NestedClass implements Streamable {
        public static class NestedClass2 implements Streamable {
            public String name;
            public String surname;
        }
        public String name;
        public String surname;
    }
}
