package com.googlecode.gwtstreamer.client.impl;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class ReadContext implements StreamFactory.Reader
{
	private List<Object> refs = new ArrayList<Object>( 70 );
	private TreeSet<String> classNameStrings = new TreeSet<String>();

	private final StreamFactory.Reader in;


	public ReadContext( StreamFactory.Reader in ) {
		this.in = in;
	}
	
	public ReadContext( StreamFactory.Reader in, ReadContext init ) {
		this(in);
		this.refs.addAll( init.refs );
		this.classNameStrings.addAll(init.classNameStrings);
	}
	
	private Object getObject(int identity) {
		return refs.get( identity );
	}

	/**
	 * Register object within context. In subsequent serializations an object's identity will be
	 * written instead of object data.
	 * @param obj object instance to register
	 * @return object identity
	 * @throws IllegalStateException if object is already registered within the context
	 */
	public void addObject(Object obj) {
		refs.add( obj );
	}

	// TODO: share with WriteContext
	private final static char NULL = '0';			// null value
	private final static char REF = 'R';			// reference to serialized object
	private final static char CLASS_REF = 'C';		// reference to serialized class name
	private final static char ARRAY_REF = 'A';		// class with package reference
	private final static char STR_REF = 'X';		// string reference
	private final static char STR_DEF = 'S';		// string definition


	public Object readObject()
	{
		char b = in.readChar();

		if ( b == NULL )
			// is null
			return null;
		else {
			if ( b == REF ) {
				// it is an object reference
				try {
					final int refIdx = in.readInt();
					return getObject(refIdx);
				} catch ( Exception ex ) {
					throw new StreamerException( "Protocol mismatch", ex );
				}
			}
			else {
				// parsing class
				final String className;

				try {
					if (b == STR_DEF || b == STR_REF) {
						readClassName(b);
						b = in.readChar();
					}

					if (b == CLASS_REF) {
						int classIdx = in.readInt();
						className = (String) getObject(classIdx);
					} else if (b == ARRAY_REF) {
						int dim = in.readInt();
						int classIdx = in.readInt();
						String objectClassName = (String) getObject(classIdx);
						StringBuilder sb = new StringBuilder();
						for ( int i = 0; i < dim; i++ )
							sb.append( '[' );
						className = sb.toString()+"L"+objectClassName+";";
					} else {
						throw new StreamerException( "Unknown tag" );
					}
				} catch ( Exception ex ) {
					throw new StreamerException( "Protocol mismatch", ex );
				}

				Streamer streamer = Streamer.get(className);

				if ( streamer == null )
					throw new StreamerException( "Could not find streamer for class: " +className );

				return streamer.readObject( this );
			}
		}
	}


	public String readClassName( char b ) {
		final String closestClassName;
		final String restString;
		final String className;

		if (b == STR_DEF) {
			closestClassName = "";
			restString = in.readString();
			className = restString;
		}
		else if (b == STR_REF) {
			int refIdx = in.readInt();
			closestClassName = (String) getObject(refIdx);
			restString = in.readString();
			className = closestClassName + restString;
		} else
			throw new StreamerException( "Unknown tag" );

		// ["","test","TestClass","MyClass"]
		String[] pp = restString.split("[\\.\\$]");
		StringBuilder sb = new StringBuilder(closestClassName);

		if (!"".equals(pp[0])) {
			sb.append(pp[0]);
			String sRef = sb.toString();
			addObject(sRef);
			classNameStrings.add(sRef);
		}

		for (int i = 1; i < pp.length; i++) {
			final String s = pp[i];
			final char delim = className.charAt(sb.length());
			sb.append(delim).append(s);
			String sRef = sb.toString();
			addObject(sRef);	// last written ID
			classNameStrings.add(sRef);
		}

		return className;
	}

	@Override
	public boolean hasMore() {
		return in.hasMore();
	}

	@Override
	public int readInt() {
		return in.readInt();
	}

	@Override
	public long readLong() {
		return in.readLong();
	}

	@Override
	public short readShort() {
		return in.readShort();
	}

	@Override
	public byte readByte() {
		return in.readByte();
	}

	@Override
	public char readChar() {
		return in.readChar();
	}

	@Override
	public boolean readBoolean() {
		return in.readBoolean();
	}

	@Override
	public double readDouble() {
		return in.readDouble();
	}

	@Override
	public float readFloat() {
		return in.readFloat();
	}

	@Override
	public String readString() {
		return in.readString();
	}
}
