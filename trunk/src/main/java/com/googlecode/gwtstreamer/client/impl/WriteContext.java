package com.googlecode.gwtstreamer.client.impl;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerException;

import java.util.*;

public final class WriteContext extends Context implements StreamFactory.Writer
{
	private Map<Object,Integer> refs = new IdentityHashMap<Object,Integer>( 30 );
	private Map<Object,Integer> refsImm = new HashMap<Object, Integer>( 70 );

	private final StreamFactory.Writer out;

	public WriteContext( StreamFactory.Writer out ) {
		this.out = out;
	}

	public WriteContext( StreamFactory.Writer out, WriteContext init ) {
		this(out);
		this.refs.putAll(init.refs);
		this.refsImm.putAll( init.refsImm );
		this.classNameStrings.addAll(init.classNameStrings);
	}

	/**
	 * Get identity of serialized object
	 * @param obj registered object
	 * @return object identity or null if object was not serialized before
	 */
	private Integer getObjectIdentity(Object obj)
	{
		if ( obj.getClass().getName().startsWith( "java." ) ) {
			// java core classes are immutable - put them to normal hashMap
			return refsImm.get( obj );
		} else {
			return refs.get( obj );
		}
	}

	/**
	 * Register object within context. In subsequent serializations an object's identity will be
	 * written instead of object data.
	 * @param obj object instance to register
	 * @return object identity
	 * @throws IllegalStateException if object is already registered within the context
	 */
	@Override
	public Integer addObject(Object obj)
	{
		Integer idx = Integer.valueOf( refs.size()+refsImm.size() );
		Integer old;
		
		if ( obj.getClass().getName().startsWith( "java." ) ) {
			old = refsImm.put( obj, idx );
		} else {
			old = refs.put( obj, idx );
		}
		
		if ( old != null ) {
			refs.put( obj, old );
			throw new IllegalStateException( "Object identity already created. " +
					"Possible protocol mismatch. Please review custom streamers implementation. Class: "
					+obj.getClass().getName() );
		}

		return idx;
	}


	public void writeObject( final Object obj )
	{
		if ( obj == null ) {
			// write null (empty string)
			out.writeByte((byte) NULL);
		} else {
			final Integer refIdx = getObjectIdentity(obj);

			if ( refIdx != null ) {
				// write object reference (digits)
				writePacked(REF, refIdx);
			} else {
				// try to obtain class short name or reference
				final Class<?> clazz = obj.getClass();
				final String className = clazz.getName();

				if ( !className.startsWith( "[" ) ) {
					// regular class
					Integer classRef = writeClassName(className);
					writePacked(CLASS_REF, classRef);
				} else {
					// array
					int dim = 1;
					while ( className.charAt( dim ) == '[' )
						dim++;

					if ( className.charAt(dim) == 'L' ) {
						// array of objects
						final String objectClassName = className.substring(dim + 1, className.length() - 1);
						Integer classRef = writeClassName(objectClassName);
						writePacked(OBJ_ARRAY_REF, dim);
						out.writeInt(classRef);
					} else {
						// array of primitives
						final char elem = className.charAt(dim);
						final int elemId = PRIMITIVES.indexOf(elem);

						if (dim <= 3) {
							// pack dimension & type
							int val = elemId | ((dim-1)<<3);
							writePacked(ARRAY_REF, val);
						} else {
							// pack type and write dimension apart
							int val = elemId | (0x3<<3);
							writePacked(ARRAY_REF, val);
							out.writeInt(dim);
						}
					}
				}

				Streamer streamer = Streamer.get(clazz);

				if ( streamer == null ) {
					throw new StreamerException( "Object of this class can not be serialized: " +
							clazz.getName() );
				}

				streamer.writeObject( obj, this );
			}
		}
	}


	private Integer writeClassName(String className) {
		Integer ref = getObjectIdentity(className);

		if (ref == null) {
			// com.googlecode.gwtstreamer.shared
			// find largest prefix of streamed class
			String closestClassName = null; {
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
						closestClassName = s;
						break;
					}
				}

				// add new packages/classes prefixes
				for (int i = n-1; i >= 0; i--) {
					final String s = names[i];
					ref = addObject(s);	// last written ID
					classNameStrings.add(s);
				}
			}

			if (closestClassName != null) {
				Integer closestClassNameRef = getObjectIdentity(closestClassName);
				writePacked(STR_REF, closestClassNameRef);
				// .test.TestClass$MyClass | wordpart.TestClass
				String restString = className.substring(closestClassName.length());
				out.writeString(restString);
			} else {
				out.writeByte((byte) STR_DEF);
				out.writeString(className);
			}
		}

		return ref;
	}


	private void writePacked( int tag, int val ) {
		// 0x1F = 00011111
		if ((val & ~0x1F) != 0 && val != 0x1F) {
			// unpacked value
			// 0x7=00000111
			out.writeByte((byte)((~0x7) | tag));
			out.writeInt(val);
		} else {
			// packed value
			out.writeByte((byte)((val<<3) | tag));
		}
	}

	@Override
	public void writeInt(int val) {
		out.writeInt(val);
	}

	@Override
	public void writeLong(long val) {
		out.writeLong(val);
	}

	@Override
	public void writeShort(short val) {
		out.writeShort(val);
	}

	@Override
	public void writeByte(byte val) {
		out.writeByte(val);
	}

	@Override
	public void writeChar(char val) {
		out.writeChar(val);
	}

	@Override
	public void writeBoolean(boolean val) {
		out.writeBoolean(val);
	}

	@Override
	public void writeDouble(double val) {
		out.writeDouble(val);
	}

	@Override
	public void writeFloat(float val) {
		out.writeFloat(val);
	}

	@Override
	public void writeString(String val) {
		out.writeString(val);
	}

	/**
	 * Get data serialized to String.
	 * @return serialized data.
	 */
	public String getData() {
		return out.toString();
	}
}
