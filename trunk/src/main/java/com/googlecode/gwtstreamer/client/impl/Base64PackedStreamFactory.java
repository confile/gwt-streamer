package com.googlecode.gwtstreamer.client.impl;

import com.googlecode.gwtstreamer.client.StreamFactory;
import com.googlecode.gwtstreamer.client.StreamerException;

import java.io.UnsupportedEncodingException;

/**
 * Created by akuranov on 26/01/2015.
 */
public class Base64PackedStreamFactory implements StreamFactory
{
    private final static char[] ABC = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_".toCharArray();

    private final static int[] ABC_TO_INT = new int[128];

    static {
        for (int i = 0; i < ABC.length; i++) {
            ABC_TO_INT[ABC[i]] = i;
        }
    }


    private static class Base64Writer implements StreamFactory.Writer
    {
        private StringBuilder out = new StringBuilder(70);

        /**
         * Write a 6-bit unsigned integer value mapped to 64-character set
         * @param sextet a 6-bit unsigned int value (0<= sextet < 64)
         */
        private void write( int sextet ) {
            assert sextet >= 0 && sextet < 64;
            out.append(ABC[sextet]);
        }

        /**
         * Signed integer packed bit format:
         *      - only the used lowest 6 bits to pack value (sextets)
         *      - first sextet:
         *          - bit 5: packed flag:
         *              0 = complete value, 1 = value continued in next sextet
         *          - bit 4: sign
         *          - but 0-3: a 4-bit value (0<= v < 16) of lowest 4 bits
         *      - next sextet:
         *          - bit 5: packed flag:
         *              0 = complete value, 1 = value continued in next sextet
         *          - bit 0-4: a 5-bit value (0<= v < 32) of next 5 bits (from lowest
         *              to highest)
         * @param v integer value to write
         */
        private void writeSignedPackedInt(int v) {
            int sx = 0;
            if (v < 0) {
                sx |= 0x10;         // sign bit, 0x10 = 10000
                v = ~v;             // change sign
                v++;
                v = ~(~v);		    // JS overflow check
            }
            sx |= (v & 0x0F);       // write lowest 4 bits, 0x0F = 1111
            v >>>= 4;               // shift v to the left by 4 bits

            if (v != 0) {
                sx |= 0x20;         // packed flag 0x20 = 100000
                write(sx);
                writeUnsignedPackedInt(v);
            } else
                write(sx);
        }


        /**
         * Unsigned integer packed bit format:
         *      - only the used lowest 6 bits to pack value (sextets)
         *      - sextet:
         *          - bit 5: packed flag:
         *              0 = complete value, 1 = value continued in next sextet
         *          - bit 0-4: a 5-bit value (0<= v < 32) of next 5 bits (from lowest
         *              to highest)
         * @param v integer value to write
         */
        private void writeUnsignedPackedInt(int v) {
            do {
                int sx = 0;
                sx |= (v & 0x1F);   // write lowest 5 bits, 0x1F = 11111
                v >>>= 5;           // shift v to the left by 5 bits
                if (v != 0)
                    sx |= 0x20;     // packed flag 0x20 = 100000
                write(sx);
            } while (v != 0);
        }

        /**
         * Write byte buffer
         * @param buffer buffer to write
         */
        private void writeByteArray(byte[] buffer)
        {
            int size = buffer.length;
            int i = 0;
            while (i < size) {
                byte b0 = buffer[i++];
                write( (b0 >> 2) & 0x3F );  // highest 6 bits

                byte b1;
                if (i < size) {
                    b1 = buffer[i++];
                    // b0 lowest 2 bits + b1 highest 4 bits
                    write( ((b0 << 4) | ((b1 & 0xFF) >> 4)) & 0x3F );

                    byte b2;
                    if (i < size) {
                        b2 = buffer[i++];
                        // b1 lowest 4 bits + b2 highest 2 bits
                        write( ((b1 << 2) | ((b2 & 0xFF) >> 6)) & 0x3F );
                        // b2 lowest 6 bits
                        write( b2 & 0x3F );
                    } else
                        write( (b1 << 2) & 0x3F );  // b1 lowest 4 bits
                } else
                    write((b0 << 4) & 0x3F);    // b0 lowest 2 bits
            }
        }


        public String getData() {
            return out.toString();
        }

        public void writeInt( int v ) {
            writeSignedPackedInt(v);
        }


        /**
         * Long uses the same format signed integer
         * @param v value to write
         */
        public void writeLong( long v ) {
            int sx = 0;
            if (v < 0) {
                sx |= 0x10;         // sign bit, 0x10 = 10000
                v = -v;             // change sign (JS emulates longs)
            }
            sx |= (v & 0x0F);       // write lowest 4 bits, 0x0F = 1111
            v >>>= 4;               // shift v to the left by 4 bits

            if (v != 0L) {
                sx |= 0x20;         // packed flag 0x20 = 100000
                write(sx);

                do {
                    sx = 0;
                    sx |= (v & 0x1F);   // write lowest 5 bits, 0x1F = 11111
                    v >>>= 5;           // shift v to the left by 5 bits
                    if (v != 0L)
                        sx |= 0x20;     // packed flag 0x20 = 100000
                    write(sx);
                } while (v != 0L);
            }
            else
                write(sx);
        }

        public void writeShort( short v ) {
            writeInt(v);
        }

        public void writeByte( byte v ) {
            writeInt(v);
        }

        /**
         * Pack char value as unsigned packed int shifting table by 32
         * @param v
         */
        public void writeChar( char v ) {
            writeUnsignedPackedInt(((int) v) - 32);
        }

        public void writeBoolean( boolean v ) {
            write(v ? 1:0);
        }

        public void writeDouble( double val ) {
            writeLong(Double.doubleToLongBits(val));
        }

        public void writeFloat( float val ) {
            writeInt(Float.floatToIntBits(val));
        }

        /**
         *
         * @param val
         */
        public void writeString( String val ) {
            try {
                byte[] buf = val.getBytes("UTF-8");
                writeUnsignedPackedInt(buf.length);
                writeByteArray(buf);
            } catch ( UnsupportedEncodingException e ) {
                throw new StreamerException( e );
            }
        }
    }



    private static class Base64Reader implements StreamFactory.Reader
    {
        private final String buf;
        private int idx;

        public Base64Reader( final String str ) {
            this.buf = str;
        }

        /**
         * Read sextet from buffer
         * @return sextet value (0<= sx < 64)
         */
        private int read() {
            return ABC_TO_INT[buf.charAt(idx++)];
        }

        public boolean hasMore() {
            return idx < buf.length();
        }


        private int readSignedPackedInt()
        {
            int v = 0;
            int sx = read();
            // sign bit, 0x10 = 10000
            boolean sign = (sx & 0x10) != 0;
            v |= (sx & 0x0F);       // set lowest 4 bits, 0x0F = 1111
            // packed flag 0x20 = 100000
            boolean fPack = (sx & 0x20) != 0;
            if (fPack) {
                int v1 = readUnsignedPackedInt();
                v |= v1 << 4;
            }

            if ( sign ) {
                v = ~v;             // change sign
                v++;
                v = ~(~v);		    // JS overflow check
            }

            return v;
        }

        private int readUnsignedPackedInt()
        {
            int v = 0;
            int shift = 0;
            for (;;) {
                int sx = read();

                v |= ((sx & 0x1F) << shift);       // set next 5 bits (from low to high), 0x1F = 11111
                // packed flag 0x20 = 100000
                boolean fPack = (sx & 0x20) != 0;
                if ( fPack ) {
                    shift += 5;
                } else
                    break;
            }

            return v;
        }

        private void readByteArray(byte[] buffer)
        {
            int index = 0;
            final int size = buffer.length;

            while(index < size) {
                int c0 = read();
                int c1 = read();
                buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & 0xFF);
                if (index >= size)
                    return;

                int c2 = read();
                buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & 0xFF);
                if (index >= size)
                    return;

                int c3 = read();
                buffer[index++] = (byte) (((c2 << 6) | c3) & 0xFF);
            }
        }

        public int readInt() {
            return readSignedPackedInt();
        }

        public long readLong() {
            long v = 0L;
            int sx = read();
            // sign bit, 0x10 = 10000
            boolean sign = (sx & 0x10) != 0;
            v |= (sx & 0x0F);       // set lowest 4 bits, 0x0F = 1111
            // packed flag 0x20 = 100000
            boolean fPack = (sx & 0x20) != 0;
            int shift = 4;

            while (fPack) {
                sx = read();
                // set next 5 bits (from low to high), 0x1F = 11111
                v |= ((sx & 0x1FL) << shift);
                // packed flag 0x20 = 100000
                fPack = (sx & 0x20) != 0;

                if ( fPack )
                    shift += 5;
            }

            if ( sign )
                v = -v;         // invert value (JS emulates longs)

            return v;
        }

        public short readShort() {
            return (short) readSignedPackedInt();
        }


        public byte readByte() {
            return (byte) readSignedPackedInt();
        }

        public char readChar() {
            return (char) (readUnsignedPackedInt()+32);
        }

        public boolean readBoolean() {
            return read() == 0 ? false : true;
        }

        public double readDouble() {
            return Double.longBitsToDouble(readLong());
        }

        public float readFloat() {
            return Float.intBitsToFloat(readInt());
        }

        /** String will be encoded and may contain any character */
        public String readString() {
            int l = readUnsignedPackedInt();
            byte[] buf = new byte[l];
            readByteArray(buf);
            try {
                return new String( buf, "UTF-8" );
            } catch ( UnsupportedEncodingException e ) {
                throw new StreamerException( e );
            }
        }
    }



    @Override
    public StreamFactory.Writer createWriter() {
        return new Base64Writer();
    }



    @Override
    public StreamFactory.Reader createReader(String str) {
        return new Base64Reader( str );
    }
}
