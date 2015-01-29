package com.googlecode.gwtstreamer.test.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerException;
import com.googlecode.gwtstreamer.test.client.shared.*;
import com.googlecode.gwtstreamer.test.client.shared.TypedArray.MyEnum;

public class GTestClientStreamer extends GWTTestCase
{
	@Override
	public String getModuleName() {
		return "com.googlecode.gwtstreamer.test.GWTStreamerJUnit";
	}


	private void checkSer( Object o ) {
		GWT.log( "original:\t"+o.getClass()+":"+o );
		String s = Streamer.get().toString( o );
		GWT.log( "serialized:\t"+s );
		Object o1 = Streamer.get().fromString( s );
		GWT.log( "copy\t\t:"+o1.getClass()+":"+o1 );
		assertEquals( o.getClass(), o1.getClass() );
		assertEquals( o, o1 );
	}
	
	/**
	 * Standard types serialization
	 */
	public void testSimpleSer()
	{
		Integer i = new Integer( 10 );
		checkSer( i );
		String s = "This is a string";
		checkSer( s );
	}
	
	public void testNull() 
	{
		String s = Streamer.get().toString( null );
		Object o1 = Streamer.get().fromString( s );
		assertNull( o1 );
	}
	
	/** 
	 * Simple structure serialization test
	 * 	- private fields
	 * 	- private default constructor 
	 */
	public void testSerSimpleStruct() {
		SimpleBean o = new SimpleBean( 1, "test" );
		checkSer( o );
	}
	
	
	/**
	 * Transient fields
	 */
	public void testSerTransient() {
		TransientBean b = new TransientBean( 20, "TEST" );
		b.temp = "ABCDE";
		String s = Streamer.get().toString( b );
		GWT.log( "serialized:\t"+s );
		TransientBean b1 = (TransientBean) Streamer.get().fromString( s );
		assertEquals( b, b1 );
		assertNull( b1.temp );	// not serialized
	}
	
	
	/** 
	 * Complex structure serialization test:
	 * 	- inheritance from abstract class
	 * 	- inheritance from non-abstract class
	 *  - inner static class
	 */
	public void testSerInherit() {
		MyEvent1 evt = new MyEvent1();
		evt.fielda = "field a";
		evt.fieldb = "field b";
		evt.fieldc = "field c";
		evt.fieldd = 1000;
		evt.prop.a = 10;
		evt.prop.a = 20;
		checkSer( evt );
	}
	
	
	/**
	 * Inheritance:
	 * 	- parent and descendant private fields have the same name
	 * 	- null value 
	 */
	public void testSerFieldsHiding() {
		ExtSimpleBean b = new ExtSimpleBean();
		b.setA( 1001 );
		b.setB( null );
		b.setNA( 12345 );
		b.setNB( 9876 );
		b.c = null;
		checkSer( b );
	}
	
	
	/** Test subclassing from non-Streamable class */
	public void testSerSubclassNonSer() {
		SerBean b = new SerBean();
		b.setA( 10 );
		b.setB( "running" );
		b.setC( "string" );
		checkSer( b );
	}
	
	
	/**
	 * Serialization of identity references.
	 * Every object in a graph must be serialized only once.
	 * The rest contains only references.
	 */
	public void testSerReferenceIdeitity()
	{
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");
		Node n5 = new Node("n5");
		Node n6 = new Node("n6");
		Node n7 = new Node("n7");
		Node n8 = new Node("n8");
		Node n9 = new Node("n9");
		
		// list
		n0.right = n1; n1.right = n2; n2.right = n3; n3.right = n4; n4.right = n5; n5.right = n6; n6.right = n7; n7.right = n8;
		n8.right = n9; n9.right = n0;
		
		// random links
		n0.left = n5;
		n1.left = n9;
		n2.left = n4;
		n3.left = n3;	// link to self
		n4.left = n0;
		n5.left = n7;
		n7.left = n2;
		n8.left = n9;
		n9.left = n5;
		
		// serialize
		String s = Streamer.get().toString( n0 );
		GWT.log( "serialized:\t"+s );
		Node nt = (Node) Streamer.get().fromString( s );
		
		// check structure
		Node ni = nt;
		assertEquals( n0, ni ); ni = ni.right;
		assertEquals( n1, ni ); ni = ni.right;
		assertEquals( n2, ni ); ni = ni.right;
		assertEquals( n3, ni ); ni = ni.right;
		assertEquals( n4, ni ); ni = ni.right;
		assertEquals( n5, ni ); ni = ni.right;
		assertEquals( n6, ni ); ni = ni.right;
		assertEquals( n7, ni ); ni = ni.right;
		assertEquals( n8, ni ); ni = ni.right;
		assertEquals( n9, ni ); ni = ni.right;
		
		// check identity
		assertTrue( nt.left == nt.right.right.right.right.right ); // n0.left == n5
		assertTrue( nt.right.right.left == nt.right.right.right.right ); // n2.left == n4
		assertTrue( nt.right.right.right.left == nt.right.right.right ); // n3.left == n3
	}

	
	public void testBasicTypes()
	{
		BasicTypes t = new BasicTypes();
		t.fInt = -1034534;
		t.fByte = 37;
		t.fShort = 1235;
		t.fLong = System.currentTimeMillis();
		t.fDouble = 0.123542467;
		// WARNING! As JavaScript only supports 64-bit floating points the result of de-serialization
		// may differ. Use /2^n values to generate same results on server and client side.
		t.fFloat = 0.75f;
		t.fChar = 'A';
		t.fString = "Test String";
		t.fBigInt = new BigInteger( "21346475684524464575684543234645768454575" );
		t.fBigDec = new BigDecimal( "-1235123523534563456.235235245634564573435" );
		t.fDate = new Date();
		checkSer( t );
	}
	
	
	public void testBasicArrayTypes()
	{
		BasicArrayTypes t = new BasicArrayTypes();
		t.aInt = new int[] { 1,2,3,4,5 };
		t.aByte = new byte[] { 5,3,4,6,4,7,34,2,2,45,7,7 };
		t.aShort = new short[] { 432,2345,2345,231,423,5345,324,2345,32,3245 };
		t.aLong = new long[] { 2,345,346,2346,456,-756746864,568846845,3457345 };
		t.aDouble = new double[] { 0.23423, 213.23, 23512.2 };
		// WARNING! As JavaScript only supports 64-bit floating points the result of de-serialization
		// may differ. Use /2^n values to generate same results on server and client side.
		t.aFloat = new float[] { 0.5f, 0.25f };
		t.aChar = "TEST CHAR".toCharArray();
		t.aString = new String[] { "Test String", "sadf", "sadfasdf" };
		checkSer( t );
	}
	
	
	public void testEnums()
	{
		Enums e = new Enums();
		e.day = Enums.DayOfWeek.FRIDAY;
		e.month = Enums.Month.OCT;
		e.planet = Enums.Planet.VENUS;
		checkSer( e );
	}
	
	public void testArrayTypes()
	{
		{
			int[] o = { 2, 3, 6, 8, 3, 8, 2, 6, 9, 0 };
			String s = Streamer.get().toString( o );
			GWT.log( "serialized:\t"+s );
			Object o1 = Streamer.get().fromString( s );
			assertEquals( o.getClass(), o1.getClass() );
			assertTrue( Arrays.equals( (int[]) o, (int[]) o1 ) );
		}
	}
	
	
	public void testTypedArrays()
	{
		TypedArray t = new TypedArray();
		t.a = new int[] { 1, 2, 3, 4, 5, 6, 7 };
		t.b = new SimpleBean[] { new SimpleBean( 1, "A" ), new SimpleBean( 2, "B" ), new SimpleBean( 3, "C") };
		t.c = new SimpleBean[] { new SimpleBean( 100, "X" ),  new SimpleBean( 101, "Y" ) };
		t.d = new Object[] { new SimpleBean( 1111, "MIR" ), new SerBean() };
		t.e = new TypedArray.MyEnum[] { MyEnum.zero, MyEnum.one, MyEnum.infinite };
		checkSer(t);
	}
	
	
	public void testMultiArrays()
	{
		MultiArray t = new MultiArray();
		t.a = new int[][] { {1, 2, 3, 4, 5}, { 6, 7, }, { 8, 9, 0 } };
		t.b = new SimpleBean[][] { { new SimpleBean( 1, "A" ), new SimpleBean( 2, "B" ), new SimpleBean( 3, "C") },
				{ new SimpleBean( 4, "D" ), new SimpleBean( 5, "E" ) } };
		t.c = new Enums.Month[][] { { Enums.Month.APR, Enums.Month.AUG, Enums.Month.FEB }, { Enums.Month.MAR }, {}, { Enums.Month.NOV, Enums.Month.NOV } };
		checkSer(t);
	}
	
	
	/**
	 * Issue 2 BUG
	 */
	public void testRecursiveStructures()
	{
		ParentBean parent = new ParentBean();
		parent.setName( "parent" );
		ChildBean child = new ChildBean();
		child.setName( "child" );
		parent.setChild(child);
		child.setParent(parent);
		checkSer( parent );
	}
	
	/**
	 * Issue ???
	 */
	public void testTransientInterface()
	{
		MyClass1 c = new MyClass1();
		c.name = "Test";
		c.value = 10;
		checkSer( c );
		
		PrivateHashMap hm = new PrivateHashMap();
		hm.add( "val1", 10 );
		hm.add( "val2", 20 );
		hm.add( "val3", 30 );
		checkSer( hm );
	}

	/**
	 * Collections with enum keys of values
	 */
	public void testEnumCollections()
	{
		EnumCollections e = new EnumCollections();
		e.connections.add( EnumCollections.State.connecting );
		e.connections.add( EnumCollections.State.disconnected );
		
		e.personSex.put( "John", EnumCollections.Sex.male );
		e.personSex.put( "Mary", EnumCollections.Sex.female );
		e.personSex.put( "Steve", EnumCollections.Sex.male );
		
		e.music.put( EnumCollections.Music.rock, "so-so" );
		e.music.put( EnumCollections.Music.electronic, "ok" );
		
		List<EnumCollections.Electronic> music = new ArrayList<EnumCollections.Electronic>();
		music.add( EnumCollections.Electronic.breaks );
		music.add( EnumCollections.Electronic.dubstep );
		e.personMusic.put( "Anton", music );
		
		List<EnumCollections.Electronic> music1 = new ArrayList<EnumCollections.Electronic>();
		music1.add( EnumCollections.Electronic.house );
		e.personMusic.put( "Laura", music1 );
		
		checkSer( e );
	}

	public void testBeanWithoutNoArgConstructor()
	{
		try {
			UninitBean b = new UninitBean("Pepito", "Perez");
			checkSer(b);
			fail();
		} catch ( StreamerException ex ) {}
	}
}
