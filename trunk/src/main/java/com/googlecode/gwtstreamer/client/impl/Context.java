package com.googlecode.gwtstreamer.client.impl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by anton on 23/01/2015.
 */
public abstract class Context
{
    /** Markers */
    protected final static char NULL = '0';			// null value
    protected final static char REF = 'R';			// reference to serialized object
    protected final static char CLASS_REF = 'C';	// reference to serialized class name
    protected final static char ARRAY_REF = 'A';	// array of primitives
    protected final static char OBJ_ARRAY_REF = 'O';// array of objects
    protected final static char STR_REF = 'X';		// string reference
    protected final static char STR_DEF = 'S';		// string definition


    protected Set<String> classNameStrings = new HashSet<String>();

    /**
     * Register object within context. In subsequent serializations an object's identity will be
     * written instead of object data.
     * @param obj object instance to register
     * @return object identity
     * @throws IllegalStateException if object is already registered within the context
     */
    public abstract Integer addObject(Object obj);

    /**
     * Internal use only
     * @param className
     */
    public void addClassNameString( String className ) {
        if (classNameStrings.contains(className))
            return;

        // divide subpackages/classes into lines
        String[] pp = className.split("[\\.\\$]");
        StringBuilder sb = new StringBuilder(className.length());
        String[] names = new String[pp.length];

        for (int i = 0; i < pp.length; i++) {
            final String s = pp[i];
            sb.append(s);
            String sRef = sb.toString();
            names[pp.length-i-1] = sRef;
            if (i < pp.length-1) {
                final char delim = className.charAt(sb.length());
                sb.append(delim);
            }
        }

        // find largest prefix
        int n;
        for (n = 1; n < names.length; n++) {
            final String s = names[n];
            if (classNameStrings.contains(s)) {
                break;
            }
        }

        // add new packages/classes prefixes
        for (int i = n-1; i >= 0; i--) {
            final String s = names[i];
            addObject(s);	// last written ID
            classNameStrings.add(s);
        }
    }
}
