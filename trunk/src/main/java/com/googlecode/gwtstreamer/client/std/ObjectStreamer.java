package com.googlecode.gwtstreamer.client.std;

import com.googlecode.gwtstreamer.client.impl.ReadContext;
import com.googlecode.gwtstreamer.client.impl.WriteContext;

import java.util.List;

/**
 * Created by akuranov on 28/01/2015.
 */
public class ObjectStreamer extends StructStreamer {
    @Override
    protected void writeObjectData(Object obj, WriteContext ctx) {
    }

    @Override
    protected void readObjectData(Object obj, ReadContext ctx) {
    }

    @Override
    protected Class<?> getTargetClass() {
        return Object.class;
    }

    @Override
    protected int getFieldNum() {
        return 0;
    }

    @Override
    protected Object createObjectInstance() {
        return new Object();
    }

    @Override
    protected List<Object> getValues(Object obj) {
        return null;
    }

    @Override
    protected void setValues(Object obj, List<Object> values) {
    }
}
