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
	private static WriteContext initWriteCtx = new WriteContext(null);
	private static ReadContext initReadCtx = new ReadContext(null);

	public volatile static boolean debug = false;
	
	/**
	 * Configuration hash: a value that reflects Streamer's configuration state
	 * included in every message. De-serialization process controls that current
	 * configuration state of Streamer is the same that is indicated in message.
	 * Otherwise exception is thrown.
	 */
	private static int streamVersion;
	/*private static String streamVersionMark;
	private static int aliasHash = 0;
	private static int packageHash = 0;
	private static Set<String> registeredStreamers = new TreeSet<String>();

	private static void recalculateStreamVersion() {
		streamVersion = 0;
		for ( String cls : registeredStreamers )
			streamVersion ^= hashCode(streamerClassMap.get(cls).getClass().getName());
		streamVersion ^= aliasHash;
		streamVersion ^= packageHash;
		if ( streamFactory != null )
			streamVersion ^= hashCode(streamFactory.getClass().getName());
		byte[] buf = new byte[] {
				(byte) ((streamVersion >>>  0) & 0xFF),
				(byte) ((streamVersion >>>  8) & 0xFF),
				(byte) ((streamVersion >>> 16) & 0xFF),
				(byte) ((streamVersion >>> 24) & 0xFF),
		};
		streamVersionMark = Base64Util.encode(buf).substring(0, 6);

		if ( debug )
			printConfig();
	}*/


	private static void printConfig() {
		/*StreamerInternal.log("Current stream version: " + streamVersion);
		StreamerInternal.log("Stream factory: "+streamFactory.getClass().getName());
		StreamerInternal.log("Registered streamers:");
		int v = 0;
		for ( String cls : registeredStreamers ) {
			String s = streamerClassMap.get(cls).getClass().getName();
			v ^= hashCode(s);
			StreamerInternal.log("  " + s+" ("+v+")");
		}
		StreamerInternal.log("Registered classes ("+aliasHash+"):");
		for (Map.Entry<String,String> entry : new TreeMap<String,String>(idClassMap).entrySet() ) {
			StreamerInternal.log("  "+entry.getValue()+" -> "+entry.getKey() );
		}
		StreamerInternal.log("Registered packages ("+packageHash+"):");
		for ( String p : packages )
			StreamerInternal.log("  "+p);*/
	}

	/**
	 * Return current version of stream. A stream version is hash that reflects current configuration.
	 * @return current version of stream
	 */
	public static int getStreamVersion() {
		if ( debug )
			printConfig();
		return streamVersion;
	}

	/**
	 * We do not rely on GWT String.hashCode() implementation while it may differ
	 * from the JVM one.
	 * @param s string
	 * @return hash code
	 */
	private static int hashCode(String s) {
		int h = 0;
		for (int i = 0; i < s.length(); i++) {
			int c = (int) s.charAt(i);
			h = 31 * h + c;
			h = ~(~h);		// JS overflow check
		}
		return h;
	}



	/**
	 * A shorter aliases are assigned to registered classes in order to reduce size of serialized data.
	 * NOTE! You must register the same classes in the same order on client and server stuff.
	 * @param cl
	 * @return generatedId or null if class already registered
	 */
	public synchronized static void registerClass(Class<?> cl) {

	}
	
	
	/** class name -> class streamer */
	protected final static Map<String,Streamer> streamerClassMap = new HashMap<String, Streamer>(); 
	
	/**
	 * Registers a custom streamer for a target class.
	 * Custom streamer must override writeObject() and readObject() methods to provide custom
	 * implementation.
	 * @param targetClass class that the streamer will serialize
	 * @param streamer Streamer implementation for that class
	 */
	public synchronized static void registerStreamer( Class<?> targetClass, Streamer streamer )
	{
		//registerClass( targetClass );
		streamerClassMap.put( targetClass.getName(), streamer );
	}
	
	
	/**
	 * Add package name to obtain shorter serialized data output for classes of this package and
	 * all super-packages.
	 * @param packageName package name
	 */
	public synchronized static void registerPackage(String packageName)
	{
	}
	
	
	static {
		// add default streamers
		registerStreamer( Integer.class, new IntegerStreamer() );
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
		
		// add default classes to create short alias names
		registerClass(Integer.class);
		registerClass(String.class);
		registerClass(Object.class);
		registerClass(Long.class);
		registerClass(Short.class);
		registerClass(Byte.class);
		registerClass(Character.class);
		registerClass(Double.class);
		registerClass(Float.class);
		registerClass(Boolean.class);
		registerClass(Object[].class);
		registerClass(String[].class);
		registerClass(BigInteger.class);
		registerClass(BigDecimal.class);
		registerClass(ArrayList.class);
		registerClass(LinkedList.class);
		registerClass(HashSet.class);
		registerClass(LinkedHashSet.class);
		registerClass(TreeSet.class);
		registerClass(Vector.class);
		registerClass(HashMap.class);
		registerClass(IdentityHashMap.class);
		registerClass(LinkedHashMap.class);
		registerClass(TreeMap.class);
		registerClass(Date.class);
	}
	
	
	/** Structure streamers are generated by GWT to provide specific access to object's fields  
		Example of code generation:
	static {
		streamerClassMap.put( "com.googlecode.gwtstreamer.client.test.TestBean", new StructStreamer() {
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
	}
	*/
	
	
	/** Stream factory is responsible to create Reader and Writer */
	private static StreamFactory streamFactory = new UrlEncStreamFactory();

	/**
	 * SwitchTo
	 * @param streamFactory
	 * @return old StreamFactory object
	 */
	public synchronized static StreamFactory switchToStreamFactory( StreamFactory streamFactory ) {
		StreamFactory old = Streamer.streamFactory;
		Streamer.streamFactory = streamFactory;
		return old;
	}
	
	
	/** Get root streamer. Lazy init */
	public static Streamer get()
	{
		return com.googlecode.gwtstreamer.client.impl.StreamerInternal.getRootStreamer();
	}
	
	
	/** Get streamer for class */
	public static Streamer get( Class<?> cl ) {
		return get( cl.getName() );
	}
	
	
	/** Get streamer for class */
	public static Streamer get( String className )
	{
		Streamer streamer = streamerClassMap.get( className );
		
		if ( streamer == null )
			// server: delegate for dynamic streamer creation
			return com.googlecode.gwtstreamer.client.impl.StreamerInternal.createStreamerFor(className);
		else
			return streamer;
	}
	
	
	/**
	 * Serialize object to string
	 * @param obj
	 * @return
	 */
	public String toString( final Object obj )
	{
		StreamFactory.Writer out = streamFactory.createWriter();
		out.writeInt(streamVersion);
		final WriteContext ctx = new WriteContext( out, initWriteCtx );
		get().writeObject( obj, ctx );
		return ctx.getData();
	}

	
	/**
	 * Deserialize object from string
	 * @param str
	 * @return
	 */
	public Object fromString( final String str ) 
	{
		final ReadContext ctx = new ReadContext( initReadCtx );
		final StreamFactory.Reader in = streamFactory.createReader( str );
		final int strVer = in.readInt();
		if ( streamVersion != strVer )
			throw new StreamerException( "The message was serialized with another version of stream. " +
					"This may occur when producer and consumer Streamers were configured differently. " +
					"Please, ensure that both have followed in the same order registerClass(), registerStreamer(), registerPackage(), " +
					"switchToStreamFactory()." );
		return get().readObject( ctx );
	}
	

	/**
	 * Obtain deep copy of an object.
	 * The copy is made through serializing and de-serializing thus object must be an instance of Streamable type.
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T deepCopy( final T obj )
	{
		String s = toString( obj );
		return (T) fromString( s );
	}
	
	
	public void writeObject( final Object obj, final WriteContext ctx )	{
		ctx.writeObject(obj);
	}
	
	
	public Object readObject( final ReadContext ctx ) {
		return ctx.readObject();
	}
}
