package com.nkdata.gwt.streamer.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import com.nkdata.gwt.streamer.client.StreamFactory.Reader;
import com.nkdata.gwt.streamer.client.StreamFactory.Writer;
import com.nkdata.gwt.streamer.client.impl.ReadContext;
import com.nkdata.gwt.streamer.client.impl.StreamerInternal;
import com.nkdata.gwt.streamer.client.impl.UrlEncStreamFactory;
import com.nkdata.gwt.streamer.client.impl.WriteContext;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.ArrayListStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.HashMapStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.HashSetStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.IdentityHashMapStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.LinkedHashMapStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.LinkedHashSetStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.LinkedListStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.TreeMapStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.TreeSetStreamer;
import com.nkdata.gwt.streamer.client.std.CollectionStreamers.VectorStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.BooleanArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.ByteArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.CharArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.DoubleArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.FloatArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.IntArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.LongArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.ObjectArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.ShortArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleArrayStreamers.StringArrayStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.BigDecimalStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.BigIntegerStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.BooleanStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.ByteStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.CharStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.DateStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.DoubleStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.FloatStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.IntegerStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.LongStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.ShortStreamer;
import com.nkdata.gwt.streamer.client.std.SimpleStreamers.StringStreamer;


public abstract class Streamer
{
	private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	
	/** class name -> class id. Must be composed only with letters (without digits or other signs). */
	protected final static Map<String,String> classIdMap = new HashMap<String, String>();
	/** reverse class id -> class name mapping */ 
	protected final static Map<String,String> idClassMap = new HashMap<String, String>();
	/** package name hints */
	protected final static List<String> packages = new ArrayList<String>();

	private static WriteContext initWriteCtx = new WriteContext();
	private static ReadContext initReadCtx = new ReadContext(); 
	
	
	private static String generateNewClassId()
    {
		int classNum = classIdMap.size();
		
    	String s = "";
    	do {
    		int d = classNum % ALPHABET.length;
    		s += ALPHABET[d];
    		classNum = classNum / ALPHABET.length;
    	} while ( classNum > 0 );
    	return s;
    }
    
	
	/**
	 * A shorter aliases are assigned to registered classes in order to reduce size of serialized data.
	 * NOTE! You must register the same classes in the same order on client and server stuff.
	 * @param cl
	 */
	public static String createAlias( Class<?> cl ) {
		String id = generateNewClassId();
		classIdMap.put( cl.getName(), id );
		idClassMap.put( id, cl.getName() );
		return id;
	}
	
	
	/** class name -> class streamer */
	protected final static Map<String,Streamer> streamerClassMap = new HashMap<String, Streamer>(); 
	
	/**
	 * Registers a custom streamer for a target class.
	 * Custom streamer must override writeObject() and readObject() methods to provide custom
	 * implementation.
	 * @param targetClass
	 * @param streamer
	 */
	public static void registerStreamer( Class<?> targetClass, Streamer streamer )
	{
		//registerClass( targetClass );
		streamerClassMap.put( targetClass.getName(), streamer );
	}
	
	
	/**
	 * Add package name to obtain shorter serialized data output for classes of this package and
	 * all super-packages.
	 * @param pack package name
	 */
	public static void addPackageHint( String pack )
	{
		packages.add( pack );
		initWriteCtx = new WriteContext();
		initReadCtx = new ReadContext();
		
		for ( String packName : packages ) {
			String[] pp = packName.split( "[\\.\\$]" );
			StringBuffer sb = new StringBuffer();
			
			for ( int i = 0; i < pp.length-1; i++ ) {
				sb.append( pp[i] );
				String p = sb.toString();
				Integer ref = initWriteCtx.getRefIdx( p );
				
				if ( ref == null ) {
					initWriteCtx.addRef( p );
					initReadCtx.addRef( p );
				}
				
				sb.append( '.' );
			}
		}
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
		
		registerStreamer( ArrayList.class, new ArrayListStreamer() );
		registerStreamer( LinkedList.class, new LinkedListStreamer() );
		registerStreamer( HashSet.class, new HashSetStreamer() );
		registerStreamer( LinkedHashSet.class, new LinkedHashSetStreamer() );
		registerStreamer( TreeSet.class, new TreeSetStreamer() );
		registerStreamer( Vector.class, new VectorStreamer() );
		registerStreamer( HashMap.class, new HashMapStreamer() );
		registerStreamer( IdentityHashMap.class, new IdentityHashMapStreamer() );
		registerStreamer( LinkedHashMap.class, new LinkedHashMapStreamer() );
		registerStreamer( TreeMap.class, new TreeMapStreamer() );
		
		// add default classes to create short alias names
		createAlias( Integer.class );
		createAlias( String.class );
		createAlias( Object.class );
		createAlias( Long.class );
		createAlias( Short.class );
		createAlias( Byte.class );
		createAlias( Character.class );
		createAlias( Double.class );
		createAlias( Float.class );
		createAlias( Boolean.class );
		createAlias( Object[].class );
		createAlias( String[].class );
		createAlias( BigInteger.class );
		createAlias( BigDecimal.class );
		createAlias( ArrayList.class );
		createAlias( LinkedList.class );
		createAlias( HashSet.class );
		createAlias( LinkedHashSet.class );
		createAlias( TreeSet.class );
		createAlias( Vector.class );
		createAlias( HashMap.class );
		createAlias( IdentityHashMap.class );
		createAlias( LinkedHashMap.class );
		createAlias( TreeMap.class );
		createAlias( Date.class );
	}
	
	
	/** Structure streamers are generated by GWT to provide specific access to object's fields  
		Example of code generation:
	static {
		streamerClassMap.put( "com.nkdata.gwt.streamer.client.test.TestBean", new StructStreamer() {
			// Get number of fields.
			@Override protected int getFieldNum() { return 2; }
			
			@Override protected Class<?> getTargetClass() { return com.nkdata.gwt.streamer.client.test.TestBean.class; }
	
			// Get field values. Fields must be ordered in alphanumeric order
			@com.google.gwt.core.client.UnsafeNativeLong
			@Override protected native List<Object> getValues( Object obj ) /*-{
				var values = @java.util.ArrayList::new()();
				values.@java.util.List::add(Ljava/lang/Object;)(
					@java.lang.Integer::valueOf(I)( obj.@com.nkdata.gwt.streamer.client.test.TestBean::a ) );
				values.@java.util.List::add(Ljava/lang/Object;)( obj.@com.nkdata.gwt.streamer.client.test.TestBean::b );
				return values;
			}-/;
		
			// Create new object instance
			@Override protected native Object createObjectInstance() /*-{
				return @com.nkdata.gwt.streamer.client.test.TestBean::new()();
			}-/;
			
			// Set field values. Fields must be ordered in alphanumeric order
			@com.google.gwt.core.client.UnsafeNativeLong
			@Override protected native void setValues( Object obj, List<Object> values ) /*-{
				obj.@com.nkdata.gwt.streamer.client.test.TestBean::a = values.@java.util.List::get(I)(0).@java.lang.Integer::intValue()();
				obj.@com.nkdata.gwt.streamer.client.test.TestBean::b = values.@java.util.List::get(I)(1);
			}-/;
		} );
	}
	*/
	
	
	/** Stream factory is responsible to create Reader and Writer */
	//public static StreamFactory streamFactory = new Base64StreamFactory();
	//public static StreamFactory streamFactory = new PrintableStreamFactory();
	public static StreamFactory streamFactory = new UrlEncStreamFactory();
	
	
	/** Get root streamer. Lazy init */
	public static Streamer get()
	{
		return StreamerInternal.getRootStreamer();
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
			return StreamerInternal.createStreamerFor( className );
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
		final WriteContext ctx = new WriteContext( initWriteCtx );
		final Writer out = streamFactory.createWriter();
		get().writeObject( obj, ctx, out );
		return out.toString();
	}

	
	/**
	 * Deserialize object from string
	 * @param str
	 * @return
	 */
	public Object fromString( final String str ) 
	{
		final ReadContext ctx = new ReadContext( initReadCtx );
		final Reader in = streamFactory.createReader( str );
		return get().readObject( ctx, in );
	}
	

	/**
	 * Obtain deep copy of an object.
	 * The copy is made through serializing and deserializing thus object must be an instance of Streamable type. 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T deepCopy( final T obj )
	{
		String s = toString( obj );
		return (T) fromString( s );
	}
	
	
	
	/** Markers */
	private final static char NULL = '0';			// null value
	private final static char REF = 'R';			// reference to serialized object
	private final static char CLASS = 'C';			// full class name
	private final static char CLASS_ID = 'I';		// short class name
	private final static char CLASS_REF = 'E';		// reference to serialized class name
	private final static char CLASS_PACK_REF = 'P';	// class with package reference
	private final static char CLASS_PACK_ARR_REF = 'A';	// array of objects with package reference
	
	
	public void writeObject( final Object obj, final WriteContext ctx, final Writer out )
	{
		if ( obj == null ) {
			// write null (empty string)
			out.writeChar( NULL );
		} else {
			final Integer refIdx = ctx.getRefIdx( obj );
			final Class<?> clazz = obj.getClass();
			
			if ( refIdx != null ) {
				// write object reference (digits)
				//out.writeString( String.valueOf( refIdx ) );
				out.writeChar( REF );
				out.writeInt( refIdx );
			} else {
				// try to obtain class short name or reference
				String classId = classIdMap.get( clazz.getName() );
				
				if ( classId == null ) {
					final Integer classRefIdx = ctx.getRefIdx( clazz );
					
					if ( classRefIdx != null ) {
						out.writeChar( CLASS_REF );
						out.writeInt( classRefIdx );
					} else {
						String className = clazz.getName();
						
						if ( className.indexOf( '.' ) > 0 ) {
							// class with package differential references
							if ( !className.startsWith( "[" ) ) {
								String[] pp = className.split( "[\\.\\$]" );
								StringBuffer sb = new StringBuffer();
								
								Integer largestPackageRef = null;
								String largestPackage = "";
								
								for ( int i = 0; i < pp.length-1; i++ ) {
									sb.append( pp[i] );
									String p = sb.toString();
									Integer ref = ctx.getRefIdx( p );
									
									if ( ref != null ) {
										largestPackageRef = ref;
										largestPackage = p;
									} else
										ctx.addRef( p );
									
									sb.append( '.' );
								}
								
								if ( largestPackageRef != null ) {
									out.writeChar( CLASS_PACK_REF );
									out.writeInt( largestPackageRef );
									String classRest = className.substring( largestPackage.length() );
									out.writeString( classRest );
								} else {
									out.writeChar( CLASS );
									out.writeString( clazz.getName() );
								}
							} else {
								// array of objects
								int dim = 1;
								while ( className.charAt( dim ) == '[' )
									dim++;
								String objectClassName = className.substring( dim+1 );
								String[] pp = objectClassName.split( "[\\.\\$]" );
								StringBuffer sb = new StringBuffer();
								
								Integer largestPackageRef = null;
								String largestPackage = "";
								
								for ( int i = 0; i < pp.length-1; i++ ) {
									sb.append( pp[i] );
									String p = sb.toString();
									Integer ref = ctx.getRefIdx( p );
									
									if ( ref != null ) {
										largestPackageRef = ref;
										largestPackage = p;
									} else
										ctx.addRef( p );
									
									sb.append( '.' );
								}
								
								if ( largestPackageRef != null ) {
									out.writeChar( CLASS_PACK_ARR_REF );
									out.writeInt( dim );
									out.writeInt( largestPackageRef );
									String classRest = objectClassName.substring( largestPackage.length() );
									out.writeString( classRest );
								} else {
									out.writeChar( CLASS );
									out.writeString( clazz.getName() );
								}
							}
						} else {
							out.writeChar( CLASS );
							out.writeString( clazz.getName() );
						}
						
						ctx.addRef( clazz );
					}
				} else {
					out.writeChar( CLASS_ID );
					out.writeString( classId );
				}
				
				Streamer streamer = get( clazz );
				
				if ( streamer == null ) {
					throw new StreamerException( "Object of this class can not be serialized: " +
							clazz.getName() );
				}
				
				streamer.writeObject( obj, ctx, out );
			}
		}
	}
	
	
	public Object readObject( final ReadContext ctx, final Reader in )
	{
		final char b = in.readChar(); 
		
		if ( b == NULL )
			// is null
			return null;
		else {
			if ( b == REF ) {
				// it is an object reference
				try {
					//final int refIdx = Integer.parseInt( s );
					final int refIdx = in.readInt();
					return ctx.getRef( refIdx );
				} catch ( Exception ex ) {
					throw new StreamerException( "Error in protocol", ex );
				}
			} else {
				// parsing class
				String className;
				
				try {
					if ( b == CLASS_REF ) {
						int classRefIdx = in.readInt();
						className = ((String) ctx.getRef( classRefIdx )).substring(1);
					} else if ( b == CLASS_ID ) {
						String classId = in.readString();
						className = idClassMap.get( classId );
					} else if ( b == CLASS || b == CLASS_PACK_REF ) {
						String[] pp;
						int packageLen = 0;
						
						if ( b == CLASS ) {
							className = in.readString();
							pp = className.split( "[\\.\\$]" );
						} else {
							int packRefIdx = in.readInt();
							String packName = (String) ctx.getRef( packRefIdx );
							packageLen = packName.length();
							String restClassName = in.readString();
							className = packName+restClassName;
							pp = className.split( "[\\.\\$]" );
						}
						
						if ( !className.startsWith( "[" ) && className.indexOf( '.' ) > 0 ) {
							StringBuffer sb = new StringBuffer();
							
							for ( int i = 0; i < pp.length-1; i++ ) {
								sb.append( pp[i] );
								String p = sb.toString();
								
								if ( p.length() > packageLen )
									if ( !ctx.containsRef( p ) )
										ctx.addRef( p );
								
								sb.append( '.' );
							}
						}
						
						ctx.addRef( "$"+className );
					} else if ( b == CLASS_PACK_ARR_REF ) {
						String[] pp;
						int packageLen = 0;
						int dim = in.readInt();
						int packRefIdx = in.readInt();
						String packName = (String) ctx.getRef( packRefIdx );
						packageLen = packName.length();
						String restClassName = in.readString();
						String objectClassName = packName+restClassName;
						pp = objectClassName.split( "[\\.\\$]" );
						
						if ( !objectClassName.startsWith( "[" ) && objectClassName.indexOf( '.' ) > 0 ) {
							StringBuffer sb = new StringBuffer();
							
							for ( int i = 0; i < pp.length-1; i++ ) {
								sb.append( pp[i] );
								String p = sb.toString();
								
								if ( p.length() > packageLen )
									if ( !ctx.containsRef( p ) )
										ctx.addRef( p );
								
								sb.append( '.' );
							}
						}
						
						StringBuffer sb = new StringBuffer();
						for ( int i = 0; i < dim; i++ )
							sb.append( '[' );
						className = sb.toString()+"L"+objectClassName;
						ctx.addRef( "$"+className );
					} else {
						throw new StreamerException( "Parse error" );
					}
				} catch ( Exception ex ) {
					throw new StreamerException( "Parse error", ex );
				}
				
				Streamer streamer = get( className );
				
				if ( streamer == null )
					throw new StreamerException( "Unknown class Id: " +className );
				
				return streamer.readObject( ctx, in );
			}
		}
	}
}
