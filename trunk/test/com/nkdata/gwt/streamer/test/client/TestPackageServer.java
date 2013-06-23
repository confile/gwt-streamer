package com.nkdata.gwt.streamer.test.client;

import com.nkdata.gwt.streamer.client.Streamer;
import com.nkdata.gwt.streamer.test.client.shared.SerBean;
import com.nkdata.gwt.streamer.test.client.shared.SimpleBean;
import com.nkdata.gwt.streamer.test.client.shared.TypedArray;
import com.nkdata.gwt.streamer.test.client.shared.TypedArray.MyEnum;

import junit.framework.TestCase;

public class TestPackageServer extends TestCase 
{
	private void log( String s ) {
		System.out.println( s );
	}
	
	public void testPackageNames()
	{
		TypedArray t = new TypedArray();
		
		Streamer.addPackageHint( TypedArray.class.getPackage().getName() );
		
		t.a = new int[] { 1, 2, 3, 4, 5, 6, 7 };
		t.b = new SimpleBean[] { new SimpleBean( 1, "A" ), new SimpleBean( 2, "B" ), new SimpleBean( 3, "C") };
		t.c = new SimpleBean[] { new SimpleBean( 100, "X" ),  new SimpleBean( 101, "Y" ) };
		t.d = new Object[] { new SimpleBean( 1111, "MIR" ), new SerBean() };
		t.e = new TypedArray.MyEnum[] { MyEnum.zero, MyEnum.one, MyEnum.infinite };
		
		log( "original:\t"+t.getClass()+":"+t );
		String s = Streamer.get().toString( t );
		log( "serialized:\t"+s );
		Object o1 = Streamer.get().fromString( s );
		log( "copy\t\t:"+o1.getClass()+":"+o1 );
		assertTrue( !s.contains( TypedArray.class.getPackage().getName() ) );
		assertEquals( t.getClass(), o1.getClass() );
		assertEquals( t, o1 );
	}
}
