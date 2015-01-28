package com.googlecode.gwtstreamer.test.client;

import com.googlecode.gwtstreamer.client.Streamer;
import com.googlecode.gwtstreamer.client.StreamerConfig;
import com.googlecode.gwtstreamer.client.StreamerException;
import com.googlecode.gwtstreamer.test.client.shared.ContainerClass;
import com.googlecode.gwtstreamer.test.client.shared.SerBean;
import com.googlecode.gwtstreamer.test.client.shared.SimpleBean;
import com.googlecode.gwtstreamer.test.client.shared.TypedArray;
import com.googlecode.gwtstreamer.test.client.shared.TypedArray.MyEnum;

import junit.framework.TestCase;

public class TestPackageServer extends TestCase 
{
	private void log( String s ) {
		System.out.println( s );
	}

	@Override
	public void tearDown() throws Exception {
		Streamer.get().applyConfig(new StreamerConfig());
	}

	public void testPackageNames()
	{
		StreamerConfig cfg = new StreamerConfig();
		cfg.registerName( TypedArray.class.getPackage().getName() );
		Streamer.applyConfig(cfg);

		/*{
			TypedArray t = new TypedArray();
			t.a = new int[]{1, 2, 3, 4, 5, 6, 7};
			t.b = new SimpleBean[]{new SimpleBean(1, "A"), new SimpleBean(2, "B"), new SimpleBean(3, "C")};
			t.c = new SimpleBean[]{new SimpleBean(100, "X"), new SimpleBean(101, "Y")};
			t.d = new Object[]{new SimpleBean(1111, "MIR"), new SerBean()};
			t.e = new TypedArray.MyEnum[]{MyEnum.zero, MyEnum.one, MyEnum.infinite};

			log("original:\t" + t.getClass() + ":" + t);
			String s = Streamer.get().toString(t);
			log("serialized:\t" + s);
			Object o1 = Streamer.get().fromString(s);
			log("copy\t\t:" + o1.getClass() + ":" + o1);
			assertTrue(!s.contains(TypedArray.class.getPackage().getName()));
			assertEquals(t.getClass(), o1.getClass());
			assertEquals(t, o1);
		}*/

		{
			ContainerClass cc = new ContainerClass();
			cc.name = "aaa";
			cc.surname = "bbb";
			ContainerClass.NestedClass nc = new ContainerClass.NestedClass();
			nc.name = "ccc";
			nc.surname = "ddd";
			ContainerClass.NestedClass.NestedClass2 nc2 = new ContainerClass.NestedClass.NestedClass2();
			nc2.name = "eee";
			nc2.surname = "fff";
			{
				Object[] oo = new Object[]{cc, nc, nc2};

				String s = Streamer.get().toString(oo);
				log("serialized:\t" + s);
				Object o1 = Streamer.get().fromString(s);
				assertEquals(Object[].class, o1.getClass());
			}
			{
				Object[] oo = new Object[]{nc2, nc, cc};

				String s = Streamer.get().toString(oo);
				log("serialized:\t" + s);
				Object o1 = Streamer.get().fromString(s);
				assertEquals(Object[].class, o1.getClass());
			}
		}
	}


	public void testStreamVersion() throws StreamerException {
		String b = Streamer.get().toString( new SimpleBean( 1, "A" ) );
		System.out.println(b);
		StreamerConfig cfg = new StreamerConfig();
		cfg.registerName(TypedArray.class.getPackage().getName());
		Streamer.applyConfig(cfg);
		String ba = Streamer.get().toString( new SimpleBean( 1, "A" ) );
		System.out.println(ba);
		try {
			Streamer.get().fromString(b);
			fail("No exception thrown");
		} catch ( StreamerException ex ) {

		}
	}
}
