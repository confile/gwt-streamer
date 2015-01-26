package com.googlecode.gwtstreamer.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.googlecode.gwtstreamer.client.impl.*;
import com.googlecode.gwtstreamer.client.std.CollectionStreamers;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.BooleanArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.ByteArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.CharArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.DoubleArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.FloatArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.IntArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.LongArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.ObjectArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.ShortArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleArrayStreamers.StringArrayStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.BigDecimalStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.BigIntegerStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.BooleanStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.ByteStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.CharStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.DateStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.DoubleStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.FloatStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.IntegerStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.LongStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.ShortStreamer;
import com.googlecode.gwtstreamer.client.std.SimpleStreamers.StringStreamer;


public abstract class Streamer
{
	/**
	 * Registered streamers (initial + configured)
	 * class name -> class streamer
	 */
	private static Map<String,Streamer> streamers;


	private static StreamFactory streamFactory;
	private static WriteContext	initWriteCtx;
	private static ReadContext 	initReadCtx;
	private static int configVersion;


	/**
	 * Return current version of stream. A stream version is hash that reflects current configuration.
	 * @return current version of stream
	 */
	public static int getConfigVersion() {
		return configVersion;
	}


	static {
		// if not in GWT mode apply default configuration. In GWT it will be made by another block.
		if (!StreamerInternal.isGWT())
			applyConfig(new StreamerConfig());
	}


	/**
	 * Apply new streamer configuration. Warning! While GWT Streamer use a singleton instance switching to another
	 * configuration gives unpredictable results on concurrent serializations. The best way is to configure your
	 * GWT streamer at the initialization part of the application.
	 * @param config new configuration to apply
	 */
	public synchronized static void applyConfig(StreamerConfig config)
	{
		streamFactory = config.getStreamFactory();
		streamers = new HashMap<String,Streamer>();
		initWriteCtx = new WriteContext(null);
		initReadCtx = new ReadContext(null);

		// add GWT streamers
		streamers.putAll(StreamerInternal.INITIAL_STREAMERS);

		// add default streamers
		registerStreamer(Integer.class, new IntegerStreamer());
		registerStreamer( Short.class, new ShortStreamer() );
		registerStreamer( Byte.class, new ByteStreamer() );
		registerStreamer( Long.class, new LongStreamer() );
		registerStreamer( Double.class, new DoubleStreamer() );
		registerStreamer( Float.class, new FloatStreamer() );
		
		registerStreamer( Character.class, new CharStreamer() );
		registerStreamer( Boolean.class, new BooleanStreamer() );
		registerStreamer( String.class, new StringStreamer() );
		registerStreamer( Date.class, new DateStreamer() );
		registerStreamer( BigInteger.class, new BigIntegerStreamer() );
		registerStreamer( BigDecimal.class, new BigDecimalStreamer() );
		
		registerStreamer( int[].class, new IntArrayStreamer() );
		registerStreamer( byte[].class, new ByteArrayStreamer() );
		registerStreamer( short[].class, new ShortArrayStreamer() );
		registerStreamer( long[].class, new LongArrayStreamer() );
		registerStreamer( double[].class, new DoubleArrayStreamer() );
		registerStreamer( float[].class, new FloatArrayStreamer() );
		registerStreamer( char[].class, new CharArrayStreamer() );
		registerStreamer( boolean[].class, new BooleanArrayStreamer() );
		registerStreamer( Object[].class, new ObjectArrayStreamer() );
		registerStreamer( String[].class, new StringArrayStreamer() );
		
		registerStreamer( ArrayList.class, new CollectionStreamers.ArrayListStreamer() );
		registerStreamer( LinkedList.class, new CollectionStreamers.LinkedListStreamer() );
		registerStreamer( HashSet.class, new CollectionStreamers.HashSetStreamer() );
		registerStreamer( LinkedHashSet.class, new CollectionStreamers.LinkedHashSetStreamer() );
		registerStreamer( TreeSet.class, new CollectionStreamers.TreeSetStreamer() );
		registerStreamer( Vector.class, new CollectionStreamers.VectorStreamer() );
		registerStreamer( HashMap.class, new CollectionStreamers.HashMapStreamer() );
		registerStreamer( IdentityHashMap.class, new CollectionStreamers.IdentityHashMapStreamer() );
		registerStreamer( LinkedHashMap.class, new CollectionStreamers.LinkedHashMapStreamer() );
		registerStreamer( TreeMap.class, new CollectionStreamers.TreeMapStreamer() );

		initWriteCtx.addClassNameString(Object.class.getName());
		initReadCtx.addClassNameString(Object.class.getName());

		// add custom class names
		for (String s : config.getRegisteredNames()) {
			initWriteCtx.addClassNameString(s);
			initReadCtx.addClassNameString(s);
		}

		// add custom streamers
		for (Map.Entry<Class<?>,Streamer> e : config.getRegisteredStreamers().entrySet())
			registerStreamer(e.getKey(), e.getValue());

		configVersion = config.getVersion();
	}

	private static void registerStreamer(Class<?> clazz, Streamer streamer) {
		streamers.put(clazz.getName(), streamer);
		initWriteCtx.addClassNameString(clazz.getName());
		initReadCtx.addClassNameString(clazz.getName());
	}

	/** Structure streamers are generated by GWT to provide specific access to object's fields
		Example of code generation:
	static {
		INITIAL_STREAMERS.put( "com.googlecode.gwtstreamer.client.test.TestBean", new StructStreamer() {
			// Get number of fields.
			@Override protected int getFieldNum() { return 2; }
			
			@Override protected Class<?> getTargetClass() { return com.googlecode.gwtstreamer.client.test.TestBean.class; }

			// Get field values. Fields must be ordered in alphanumeric order
			@com.google.gwt.core.client.UnsafeNativeLong
			@Override protected native List<Object> getValues( Object obj ) /*-{
				var values = @java.util.ArrayList::new()();
				values.@java.util.List::add(Ljava/lang/Object;)(
					@java.lang.Integer::valueOf(I)( obj.@com.googlecode.gwtstreamer.client.test.TestBean::a ) );
				values.@java.util.List::add(Ljava/lang/Object;)( obj.@com.googlecode.gwtstreamer.client.test.TestBean::b );
				return values;
			}-/;
		
			// Create new object instance
			@Override protected native Object createObjectInstance() /*-{
				return @com.googlecode.gwtstreamer.client.test.TestBean::new()();
			}-/;
			
			// Set field values. Fields must be ordered in alphanumeric order
			@com.google.gwt.core.client.UnsafeNativeLong
			@Override protected native void setValues( Object obj, List<Object> values ) /*-{
				obj.@com.googlecode.gwtstreamer.client.test.TestBean::a = values.@java.util.List::get(I)(0).@java.lang.Integer::intValue()();
				obj.@com.googlecode.gwtstreamer.client.test.TestBean::b = values.@java.util.List::get(I)(1);
			}-/;
		} );
		...
		applyConfig(new StreamerConfig());
	}
	*/
	
	
	/** Get root streamer. Lazy init */
	public static Streamer get()
	{
		return com.googlecode.gwtstreamer.client.impl.StreamerInternal.getRootStreamer();
	}
	
	
	/** Get streamer for class (internal use only) */
	public static Streamer get( Class<?> cl ) {
		return get( cl.getName() );
	}
	
	
	/** Get streamer for class (internal use only) */
	public static Streamer get( String className )
	{
		Streamer streamer = streamers.get( className );
		
		if ( streamer == null )
			// server: delegate for dynamic streamer creation
			return com.googlecode.gwtstreamer.client.impl.StreamerInternal.createStreamerFor(className);
		else
			return streamer;
	}
	
	
	/**
	 * Serialize object to string
	 * @param obj object to serialize
	 * @return string containing serialized object
	 * @throws StreamerException if serialization was unsuccessful
	 */
	public String toString( final Object obj )
	{
		StreamFactory.Writer out = streamFactory.createWriter();
		out.writeInt(configVersion);
		final WriteContext ctx = new WriteContext( out, initWriteCtx );
		get().writeObject( obj, ctx );
		return ctx.getData();
	}

	
	/**
	 * Deserialize object from string
	 * @param str string containing object
	 * @return de-serialized object
	 * @throws StreamerException if de-serialization was unsuccessful
	 */
	public Object fromString( final String str ) 
	{
		final StreamFactory.Reader in = streamFactory.createReader( str );
		final int strVer = in.readInt();
		if ( configVersion != strVer )
			throw new StreamerException( "The message was serialized with another version of configuration. " +
					"This may occur when producer and consumer Streamers were configured differently. " +
					"Please, ensure that both have the same StreamerConfig.getVersion()." );
		final ReadContext ctx = new ReadContext( in, initReadCtx );
		return get().readObject( ctx );
	}
	

	/**
	 * Obtain deep copy of an object.
	 * The copy is made through serializing and de-serializing thus object must be an instance of Streamable type.
	 * @param obj object to clone
	 * @return deeply cloned object
	 * @throws StreamerException if clonning was unsuccessful
	 */
	@SuppressWarnings("unchecked")
	public <T> T deepCopy( final T obj )
	{
		String s = toString( obj );
		return (T) fromString( s );
	}

	/**
	 * Default implementation of object serialization within context (internal use only)
	 * @param obj object to serialize
	 * @param ctx serialization context
	 */
	public void writeObject( final Object obj, final WriteContext ctx )	{
		ctx.writeObject(obj);
	}


	/**
	 * Default implementation of object de-serialization within context (internal use only)
	 * @param ctx serialization context
	 * @return de-serialized object
	 */
	public Object readObject( final ReadContext ctx ) {
		return ctx.readObject();
	}
}
