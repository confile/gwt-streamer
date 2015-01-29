package com.googlecode.gwtstreamer.client.impl;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerException;

import java.util.*;

public final class ReadContext extends Context implements StreamFactory.Reader
{
	private List<Object> refs = new ArrayList<Object>( 70 );

	private final StreamFactory.Reader in;


	public ReadContext( StreamFactory.Reader in ) {
		this.in = in;
	}
	
	public ReadContext(StreamFactory.Reader in, ReadContext init) {
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
	public Integer addObject(Object obj) {
		refs.add( obj );
		return Integer.valueOf(refs.size());
	}

	private int[] tagVal = new int[2];	// temporal use only, not tolerant to concurrent calls

	public Object readObject()
	{
		int[] tagVal = readPacked(this.tagVal);
		int tag = tagVal[0];
		int val = tagVal[1];

		if ( tag == NULL )
			// is null
			return null;
		else {
			if ( tag == REF ) {
				// it is an object reference
				try {
					return getObject(val);
				} catch ( Exception ex ) {
					throw new StreamerException( "Protocol mismatch", ex );
				}
			}
			else {
				// parsing class
				final String className;

				try {
					if (tag == STR_DEF || tag == STR_REF) {
						readClassName(tag, val);
						tagVal = readPacked(this.tagVal);
						tag = tagVal[0];
						val = tagVal[1];
					}

					if (tag == CLASS_REF) {
						className = (String) getObject(val);
					} else if (tag == OBJ_ARRAY_REF) {
						int dim = val;
						int classIdx = in.readInt();
						String objectClassName = (String) getObject(classIdx);
						StringBuilder sb = new StringBuilder(dim+objectClassName.length()+3);
						for ( int i = 0; i < dim; i++ )
							sb.append( '[' );
						className = sb.append("L").append(objectClassName).append(";").toString();
					} else if (tag == ARRAY_REF) {
						int dim = (val>>3) & 0xFF;

						if ( dim == 0x3 ) {
							// unpacked value
							dim = in.readInt();
						} else {
							// packed value
							dim++;
						}
						final int elemIdx = val & 0x7;
						char elem = PRIMITIVES.charAt(elemIdx);
						StringBuilder sb = new StringBuilder(dim+2);
						for ( int i = 0; i < dim; i++ )
							sb.append( '[' );
						sb.append(elem);
						className = sb.toString();
					} else {
						throw new StreamerException( "Unknown tag" );
					}
				} catch ( Exception ex ) {
					throw new StreamerException( "Protocol mismatch", ex );
				}

				Streamer streamer = Streamer.get(className);

				if ( streamer == null )
					throw new StreamerException( "Could not find streamer for class: " +className );

				try {
					return streamer.readObject(this);
				} catch (Exception ex) {
					throw new StreamerException("Error reading object: "+className, ex);
				}
			}
		}
	}

	private int[] readPacked( int[] tagVal ) {
		if (tagVal == null || tagVal.length < 2)
			tagVal = new int[2];

		int raw = in.readByte() & 0xFF;

		// 0x7=00000111
		// if (highest 5 bits of a byte = 00000
		if ((raw & ~0x7) == 0) {
			// read unpacked value
			tagVal[0] = raw & 0x7;
			tagVal[1] = in.readInt();
		} else {
			// read packed value
			tagVal[0] = raw & 0x7;
			tagVal[1] = ((raw >> 3) & 0xFF)-1;
		}

		return tagVal;
	}

	public String readClassName( int tag, int val ) {
		final String closestClassName;
		final String restString;
		final String className;

		if (tag == STR_DEF) {
			closestClassName = "";
			restString = in.readString();
			className = restString;
		}
		else if (tag == STR_REF) {
			closestClassName = (String) getObject(val);
			restString = in.readString();
			className = closestClassName + restString;
		} else
			throw new StreamerException( "Unknown tag" );

		// ["","test","TestClass","MyClass"]
		String[] pp = restString.split("[\\.\\$]");
		StringBuilder sb = new StringBuilder(closestClassName.length()+restString.length());
		sb.append(closestClassName);

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

	/**
	 * Get size limit for arrays and strings to protect from OutOfMemory attacks.
	 * By default size limit is a length of input string.
	 * @return size limit
	 */
	@Override
	public int getSizeLimit() { return in.getSizeLimit(); }

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
