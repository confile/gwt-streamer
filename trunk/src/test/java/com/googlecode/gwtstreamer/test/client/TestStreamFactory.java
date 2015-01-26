package com.googlecode.gwtstreamer.test.client;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.impl.Base64PackedStreamFactory;
import com.googlecode.gwtstreamer.client.impl.Base64StreamFactory;
import com.googlecode.gwtstreamer.client.impl.PrintableStreamFactory;
import com.googlecode.gwtstreamer.client.impl.UrlEncodedStreamFactory;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by akuranov on 26/01/2015.
 */
public class TestStreamFactory extends TestCase
{
    public static class StreamFactoryTest {
        private StreamFactory streamFactory;

        public StreamFactoryTest(StreamFactory streamFactory) {
            this.streamFactory = streamFactory;
        }

        public void execute() {
            // simple int
            check(Arrays.asList(0, 1));

            // advanced ints
            check(Arrays.asList(-1, -13, 15, Integer.MAX_VALUE, Integer.MIN_VALUE,
                    Integer.MAX_VALUE-1, Integer.MIN_VALUE+1,
                    65536, 65535, -65536, 151236721, -198347129));

            // check bytes
            check(Arrays.asList( (byte) 0, (byte) 1, (byte) 15, (byte) 16, (byte) -1,
                    Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE-1, Byte.MIN_VALUE+1,
                    (byte) 100, (byte) -87 ));

            // check shorts
            check(Arrays.asList( (short) 0, (short) 1, (short) 15, (short) 16, (short) -1,
                    Short.MAX_VALUE, Short.MIN_VALUE, (short) 100, (short) -87,
                    (short) 13567, (short) -40000, (short) 64000, (short) 543, (short) 200 ));

            // check longs
            check(Arrays.asList(-1L, -13L, 15L, Long.MAX_VALUE, Long.MIN_VALUE,
                    65536L, 65535L, -65536L, 151236721L, -198347129L, 412345463246234L,
                    -2103498522356737822L, 1000L, 200L, 56L));

            // check booleans
            check(Arrays.asList(true, false, false, true, true, true));

            // check characters
            char[] chars = ("abcABC1230 %!#$%&/()=\\_-*^{}+`¨ç€¿~ºªáóúüñабвАБВ" +
                    "\uFEDA\uFEDB\uFEDC\u2126\u1FF7").toCharArray();
            List<Character> oo = new ArrayList<Character>();
            for (char c:chars)
                oo.add(c);
            check(oo);

            // check doubles
            List<Double> dd = new ArrayList<Double>();
            for (int i=0; i<100; i++)
                dd.add(Math.random());
            dd.add(Double.MAX_VALUE);
            dd.add(Double.MIN_VALUE);
            check(dd);

            // check floats
            List<Float> ff = new ArrayList<Float>();
            for (int i=0; i<100; i++)
                ff.add((float)Math.random());
            ff.add(Float.MAX_VALUE);
            ff.add(Float.MIN_VALUE);
            check(ff);

            // check strings
            check(Arrays.asList("","a","ab","abc","abcd","abcdef","abcdefg",
                    "This is a sample text that must be serialized and deserialized.",
                    "Странные символы кодируются UTF-8.",
                    "Estos son símbolos Unicode. ÑÑÑñññ áé",
                    "abcABC1230 %!#$%&/()=\\_-*^{}+`¨ç€¿~ºªáóúüñабвАБВ" +
                            "\uFEDA\uFEDB\uFEDC\u2126\u1FF7"));
        }

        private void check(List<?> list) {
            StreamFactory.Writer out = streamFactory.createWriter();
            for (Object o : list) {
                if (o.getClass() == Integer.class)
                    out.writeInt((Integer)o);
                else if (o.getClass() == Long.class)
                    out.writeLong((Long) o);
                else if (o.getClass() == Short.class)
                    out.writeShort((Short) o);
                else if (o.getClass() == Byte.class)
                    out.writeByte((Byte) o);
                else if (o.getClass() == Character.class)
                    out.writeChar((Character)o);
                else if (o.getClass() == Boolean.class)
                    out.writeBoolean((Boolean) o);
                else if (o.getClass() == String.class)
                    out.writeString((String)o);
                else if (o.getClass() == Double.class)
                    out.writeDouble((Double) o);
                else if (o.getClass() == Float.class)
                    out.writeFloat((Float)o);
                else
                    fail("Type is not allowed: " + o.getClass());
            }

            String buf = out.getData();
            log(buf);
            List<Object> list1 = new ArrayList<Object>(list.size());
            StreamFactory.Reader in = streamFactory.createReader(buf);

            for (Object o : list) {
                Object o1 = null;
                if (o.getClass() == Integer.class)
                    o1 = in.readInt();
                else if (o.getClass() == Long.class)
                    o1 = in.readLong();
                else if (o.getClass() == Short.class)
                    o1 = in.readShort();
                else if (o.getClass() == Byte.class)
                    o1 = in.readByte();
                else if (o.getClass() == Character.class)
                    o1 = in.readChar();
                else if (o.getClass() == Boolean.class)
                    o1 = in.readBoolean();
                else if (o.getClass() == String.class)
                    o1 = in.readString();
                else if (o.getClass() == Double.class)
                    o1 = in.readDouble();
                else if (o.getClass() == Float.class)
                    o1 = in.readFloat();
                else
                    fail("Type is not allowed: " + o.getClass());
                list1.add(o1);
            }

            assertEquals(list, list1);
        }
    }


    static void log( String s ) {
        System.out.println( s );
    }


    public void testBase64PackedStreamFactory() {
        new StreamFactoryTest(new Base64PackedStreamFactory()).execute();
    }

    public void testPrintablePackedStreamFactory() {
        new StreamFactoryTest(new PrintableStreamFactory()).execute();
    }

    public void testUrlEncodedStreamFactory() {
        new StreamFactoryTest(new UrlEncodedStreamFactory()).execute();
    }

    public void testBase64StreamFactory() {
        new StreamFactoryTest(new Base64StreamFactory()).execute();
    }
}
