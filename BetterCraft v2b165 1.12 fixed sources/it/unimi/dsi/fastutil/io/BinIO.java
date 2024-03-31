// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.io;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteIterator;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterable;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleBigArrays;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.floats.FloatIterable;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatBigArrays;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.longs.LongIterable;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongBigArrays;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.ints.IntIterable;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntBigArrays;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.chars.CharIterable;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharBigArrays;
import it.unimi.dsi.fastutil.chars.CharArrays;
import it.unimi.dsi.fastutil.shorts.ShortIterable;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortBigArrays;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import it.unimi.dsi.fastutil.bytes.ByteIterable;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import java.util.Iterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterable;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.booleans.BooleanBigArrays;
import java.io.DataOutputStream;
import java.io.DataOutput;
import java.io.DataInputStream;
import java.io.EOFException;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import java.io.DataInput;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;

public class BinIO
{
    private static final int MAX_IO_LENGTH = 1048576;
    
    private BinIO() {
    }
    
    public static void storeObject(final Object o, final File file) throws IOException {
        final ObjectOutputStream oos = new ObjectOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        oos.writeObject(o);
        oos.close();
    }
    
    public static void storeObject(final Object o, final CharSequence filename) throws IOException {
        storeObject(o, new File(filename.toString()));
    }
    
    public static Object loadObject(final File file) throws IOException, ClassNotFoundException {
        final ObjectInputStream ois = new ObjectInputStream(new FastBufferedInputStream(new FileInputStream(file)));
        final Object result = ois.readObject();
        ois.close();
        return result;
    }
    
    public static Object loadObject(final CharSequence filename) throws IOException, ClassNotFoundException {
        return loadObject(new File(filename.toString()));
    }
    
    public static void storeObject(final Object o, final OutputStream s) throws IOException {
        final ObjectOutputStream oos = new ObjectOutputStream(new FastBufferedOutputStream(s));
        oos.writeObject(o);
        oos.flush();
    }
    
    public static Object loadObject(final InputStream s) throws IOException, ClassNotFoundException {
        final ObjectInputStream ois = new ObjectInputStream(new FastBufferedInputStream(s));
        final Object result = ois.readObject();
        return result;
    }
    
    public static int loadBooleans(final DataInput dataInput, final boolean[] array, final int offset, final int length) throws IOException {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readBoolean();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadBooleans(final DataInput dataInput, final boolean[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readBoolean();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadBooleans(final File file, final boolean[] array, final int offset, final int length) throws IOException {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dis.readBoolean();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadBooleans(final CharSequence filename, final boolean[] array, final int offset, final int length) throws IOException {
        return loadBooleans(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadBooleans(final File file, final boolean[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dis.readBoolean();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadBooleans(final CharSequence filename, final boolean[] array) throws IOException {
        return loadBooleans(new File(filename.toString()), array);
    }
    
    public static boolean[] loadBooleans(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size();
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final boolean[] array = new boolean[(int)length];
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < length; ++i) {
            array[i] = dis.readBoolean();
        }
        dis.close();
        return array;
    }
    
    public static boolean[] loadBooleans(final CharSequence filename) throws IOException {
        return loadBooleans(new File(filename.toString()));
    }
    
    public static void storeBooleans(final boolean[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            dataOutput.writeBoolean(array[offset + i]);
        }
    }
    
    public static void storeBooleans(final boolean[] array, final DataOutput dataOutput) throws IOException {
        for (int length = array.length, i = 0; i < length; ++i) {
            dataOutput.writeBoolean(array[i]);
        }
    }
    
    public static void storeBooleans(final boolean[] array, final int offset, final int length, final File file) throws IOException {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeBoolean(array[offset + i]);
        }
        dos.close();
    }
    
    public static void storeBooleans(final boolean[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeBooleans(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBooleans(final boolean[] array, final File file) throws IOException {
        final int length = array.length;
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeBoolean(array[i]);
        }
        dos.close();
    }
    
    public static void storeBooleans(final boolean[] array, final CharSequence filename) throws IOException {
        storeBooleans(array, new File(filename.toString()));
    }
    
    public static long loadBooleans(final DataInput dataInput, final boolean[][] array, final long offset, final long length) throws IOException {
        BooleanBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final boolean[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readBoolean();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadBooleans(final DataInput dataInput, final boolean[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final boolean[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readBoolean();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadBooleans(final File file, final boolean[][] array, final long offset, final long length) throws IOException {
        BooleanBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final boolean[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dis.readBoolean();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadBooleans(final CharSequence filename, final boolean[][] array, final long offset, final long length) throws IOException {
        return loadBooleans(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadBooleans(final File file, final boolean[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final boolean[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dis.readBoolean();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadBooleans(final CharSequence filename, final boolean[][] array) throws IOException {
        return loadBooleans(new File(filename.toString()), array);
    }
    
    public static boolean[][] loadBooleansBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size();
        final boolean[][] array = BooleanBigArrays.newBigArray(length);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < array.length; ++i) {
            final boolean[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                t[d] = dis.readBoolean();
            }
        }
        dis.close();
        return array;
    }
    
    public static boolean[][] loadBooleansBig(final CharSequence filename) throws IOException {
        return loadBooleansBig(new File(filename.toString()));
    }
    
    public static void storeBooleans(final boolean[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        BooleanBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final boolean[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dataOutput.writeBoolean(t[d]);
            }
        }
    }
    
    public static void storeBooleans(final boolean[][] array, final DataOutput dataOutput) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            final boolean[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dataOutput.writeBoolean(t[d]);
            }
        }
    }
    
    public static void storeBooleans(final boolean[][] array, final long offset, final long length, final File file) throws IOException {
        BooleanBigArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final boolean[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dos.writeBoolean(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeBooleans(final boolean[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeBooleans(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBooleans(final boolean[][] array, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < array.length; ++i) {
            final boolean[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dos.writeBoolean(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeBooleans(final boolean[][] array, final CharSequence filename) throws IOException {
        storeBooleans(array, new File(filename.toString()));
    }
    
    public static void storeBooleans(final BooleanIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeBoolean(i.nextBoolean());
        }
    }
    
    public static void storeBooleans(final BooleanIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeBoolean(i.nextBoolean());
        }
        dos.close();
    }
    
    public static void storeBooleans(final BooleanIterator i, final CharSequence filename) throws IOException {
        storeBooleans(i, new File(filename.toString()));
    }
    
    public static BooleanIterator asBooleanIterator(final DataInput dataInput) {
        return new BooleanDataInputWrapper(dataInput);
    }
    
    public static BooleanIterator asBooleanIterator(final File file) throws IOException {
        return new BooleanDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static BooleanIterator asBooleanIterator(final CharSequence filename) throws IOException {
        return asBooleanIterator(new File(filename.toString()));
    }
    
    public static BooleanIterable asBooleanIterable(final File file) {
        return new BooleanIterable() {
            @Override
            public BooleanIterator iterator() {
                try {
                    return BinIO.asBooleanIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static BooleanIterable asBooleanIterable(final CharSequence filename) {
        return new BooleanIterable() {
            @Override
            public BooleanIterator iterator() {
                try {
                    return BinIO.asBooleanIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    private static int read(final InputStream is, final byte[] a, final int offset, final int length) throws IOException {
        if (length == 0) {
            return 0;
        }
        int read = 0;
        do {
            final int result = is.read(a, offset + read, Math.min(length - read, 1048576));
            if (result < 0) {
                return read;
            }
            read += result;
        } while (read < length);
        return read;
    }
    
    private static void write(final OutputStream outputStream, final byte[] a, final int offset, final int length) throws IOException {
        for (int written = 0; written < length; written += Math.min(length - written, 1048576)) {
            outputStream.write(a, offset + written, Math.min(length - written, 1048576));
        }
    }
    
    private static void write(final DataOutput dataOutput, final byte[] a, final int offset, final int length) throws IOException {
        for (int written = 0; written < length; written += Math.min(length - written, 1048576)) {
            dataOutput.write(a, offset + written, Math.min(length - written, 1048576));
        }
    }
    
    public static int loadBytes(final InputStream inputStream, final byte[] array, final int offset, final int length) throws IOException {
        return read(inputStream, array, offset, length);
    }
    
    public static int loadBytes(final InputStream inputStream, final byte[] array) throws IOException {
        return read(inputStream, array, 0, array.length);
    }
    
    public static void storeBytes(final byte[] array, final int offset, final int length, final OutputStream outputStream) throws IOException {
        write(outputStream, array, offset, length);
    }
    
    public static void storeBytes(final byte[] array, final OutputStream outputStream) throws IOException {
        write(outputStream, array, 0, array.length);
    }
    
    private static long read(final InputStream is, final byte[][] a, final long offset, final long length) throws IOException {
        if (length == 0L) {
            return 0L;
        }
        long read = 0L;
        int segment = BigArrays.segment(offset);
        int displacement = BigArrays.displacement(offset);
        do {
            final int result = is.read(a[segment], displacement, (int)Math.min(a[segment].length - displacement, Math.min(length - read, 1048576L)));
            if (result < 0) {
                return read;
            }
            read += result;
            displacement += result;
            if (displacement != a[segment].length) {
                continue;
            }
            ++segment;
            displacement = 0;
        } while (read < length);
        return read;
    }
    
    private static void write(final OutputStream outputStream, final byte[][] a, final long offset, final long length) throws IOException {
        if (length == 0L) {
            return;
        }
        long written = 0L;
        int segment = BigArrays.segment(offset);
        int displacement = BigArrays.displacement(offset);
        do {
            final int toWrite = (int)Math.min(a[segment].length - displacement, Math.min(length - written, 1048576L));
            outputStream.write(a[segment], displacement, toWrite);
            written += toWrite;
            displacement += toWrite;
            if (displacement == a[segment].length) {
                ++segment;
                displacement = 0;
            }
        } while (written < length);
    }
    
    private static void write(final DataOutput dataOutput, final byte[][] a, final long offset, final long length) throws IOException {
        if (length == 0L) {
            return;
        }
        long written = 0L;
        int segment = BigArrays.segment(offset);
        int displacement = BigArrays.displacement(offset);
        do {
            final int toWrite = (int)Math.min(a[segment].length - displacement, Math.min(length - written, 1048576L));
            dataOutput.write(a[segment], displacement, toWrite);
            written += toWrite;
            displacement += toWrite;
            if (displacement == a[segment].length) {
                ++segment;
                displacement = 0;
            }
        } while (written < length);
    }
    
    public static long loadBytes(final InputStream inputStream, final byte[][] array, final long offset, final long length) throws IOException {
        return read(inputStream, array, offset, length);
    }
    
    public static long loadBytes(final InputStream inputStream, final byte[][] array) throws IOException {
        return read(inputStream, array, 0L, ByteBigArrays.length(array));
    }
    
    public static void storeBytes(final byte[][] array, final long offset, final long length, final OutputStream outputStream) throws IOException {
        write(outputStream, array, offset, length);
    }
    
    public static void storeBytes(final byte[][] array, final OutputStream outputStream) throws IOException {
        write(outputStream, array, 0L, ByteBigArrays.length(array));
    }
    
    public static int loadBytes(final DataInput dataInput, final byte[] array, final int offset, final int length) throws IOException {
        ByteArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readByte();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadBytes(final DataInput dataInput, final byte[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readByte();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadBytes(final File file, final byte[] array, final int offset, final int length) throws IOException {
        ByteArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final int result = read(fis, array, offset, length);
        fis.close();
        return result;
    }
    
    public static int loadBytes(final CharSequence filename, final byte[] array, final int offset, final int length) throws IOException {
        return loadBytes(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadBytes(final File file, final byte[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final int result = read(fis, array, 0, array.length);
        fis.close();
        return result;
    }
    
    public static int loadBytes(final CharSequence filename, final byte[] array) throws IOException {
        return loadBytes(new File(filename.toString()), array);
    }
    
    public static byte[] loadBytes(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 1L;
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final byte[] array = new byte[(int)length];
        if (read(fis, array, 0, (int)length) < length) {
            throw new EOFException();
        }
        fis.close();
        return array;
    }
    
    public static byte[] loadBytes(final CharSequence filename) throws IOException {
        return loadBytes(new File(filename.toString()));
    }
    
    public static void storeBytes(final byte[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        ByteArrays.ensureOffsetLength(array, offset, length);
        write(dataOutput, array, offset, length);
    }
    
    public static void storeBytes(final byte[] array, final DataOutput dataOutput) throws IOException {
        write(dataOutput, array, 0, array.length);
    }
    
    public static void storeBytes(final byte[] array, final int offset, final int length, final File file) throws IOException {
        ByteArrays.ensureOffsetLength(array, offset, length);
        final OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
        write(os, array, offset, length);
        os.close();
    }
    
    public static void storeBytes(final byte[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeBytes(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBytes(final byte[] array, final File file) throws IOException {
        final OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
        write(os, array, 0, array.length);
        os.close();
    }
    
    public static void storeBytes(final byte[] array, final CharSequence filename) throws IOException {
        storeBytes(array, new File(filename.toString()));
    }
    
    public static long loadBytes(final DataInput dataInput, final byte[][] array, final long offset, final long length) throws IOException {
        ByteBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final byte[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readByte();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadBytes(final DataInput dataInput, final byte[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final byte[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readByte();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadBytes(final File file, final byte[][] array, final long offset, final long length) throws IOException {
        ByteBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final long result = read(fis, array, offset, length);
        fis.close();
        return result;
    }
    
    public static long loadBytes(final CharSequence filename, final byte[][] array, final long offset, final long length) throws IOException {
        return loadBytes(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadBytes(final File file, final byte[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long result = read(fis, array, 0L, ByteBigArrays.length(array));
        fis.close();
        return result;
    }
    
    public static long loadBytes(final CharSequence filename, final byte[][] array) throws IOException {
        return loadBytes(new File(filename.toString()), array);
    }
    
    public static byte[][] loadBytesBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 1L;
        final byte[][] array = ByteBigArrays.newBigArray(length);
        if (read(fis, array, 0L, length) < length) {
            throw new EOFException();
        }
        fis.close();
        return array;
    }
    
    public static byte[][] loadBytesBig(final CharSequence filename) throws IOException {
        return loadBytesBig(new File(filename.toString()));
    }
    
    public static void storeBytes(final byte[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        ByteBigArrays.ensureOffsetLength(array, offset, length);
        write(dataOutput, array, offset, length);
    }
    
    public static void storeBytes(final byte[][] array, final DataOutput dataOutput) throws IOException {
        write(dataOutput, array, 0L, ByteBigArrays.length(array));
    }
    
    public static void storeBytes(final byte[][] array, final long offset, final long length, final File file) throws IOException {
        ByteBigArrays.ensureOffsetLength(array, offset, length);
        final OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
        write(os, array, offset, length);
        os.close();
    }
    
    public static void storeBytes(final byte[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeBytes(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBytes(final byte[][] array, final File file) throws IOException {
        final OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
        write(os, array, 0L, ByteBigArrays.length(array));
        os.close();
    }
    
    public static void storeBytes(final byte[][] array, final CharSequence filename) throws IOException {
        storeBytes(array, new File(filename.toString()));
    }
    
    public static void storeBytes(final ByteIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeByte(i.nextByte());
        }
    }
    
    public static void storeBytes(final ByteIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeByte(i.nextByte());
        }
        dos.close();
    }
    
    public static void storeBytes(final ByteIterator i, final CharSequence filename) throws IOException {
        storeBytes(i, new File(filename.toString()));
    }
    
    public static ByteIterator asByteIterator(final DataInput dataInput) {
        return new ByteDataInputWrapper(dataInput);
    }
    
    public static ByteIterator asByteIterator(final File file) throws IOException {
        return new ByteDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static ByteIterator asByteIterator(final CharSequence filename) throws IOException {
        return asByteIterator(new File(filename.toString()));
    }
    
    public static ByteIterable asByteIterable(final File file) {
        return new ByteIterable() {
            @Override
            public ByteIterator iterator() {
                try {
                    return BinIO.asByteIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static ByteIterable asByteIterable(final CharSequence filename) {
        return new ByteIterable() {
            @Override
            public ByteIterator iterator() {
                try {
                    return BinIO.asByteIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadShorts(final DataInput dataInput, final short[] array, final int offset, final int length) throws IOException {
        ShortArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readShort();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadShorts(final DataInput dataInput, final short[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readShort();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadShorts(final File file, final short[] array, final int offset, final int length) throws IOException {
        ShortArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dis.readShort();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadShorts(final CharSequence filename, final short[] array, final int offset, final int length) throws IOException {
        return loadShorts(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadShorts(final File file, final short[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dis.readShort();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadShorts(final CharSequence filename, final short[] array) throws IOException {
        return loadShorts(new File(filename.toString()), array);
    }
    
    public static short[] loadShorts(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 2L;
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final short[] array = new short[(int)length];
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < length; ++i) {
            array[i] = dis.readShort();
        }
        dis.close();
        return array;
    }
    
    public static short[] loadShorts(final CharSequence filename) throws IOException {
        return loadShorts(new File(filename.toString()));
    }
    
    public static void storeShorts(final short[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        ShortArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            dataOutput.writeShort(array[offset + i]);
        }
    }
    
    public static void storeShorts(final short[] array, final DataOutput dataOutput) throws IOException {
        for (int length = array.length, i = 0; i < length; ++i) {
            dataOutput.writeShort(array[i]);
        }
    }
    
    public static void storeShorts(final short[] array, final int offset, final int length, final File file) throws IOException {
        ShortArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeShort(array[offset + i]);
        }
        dos.close();
    }
    
    public static void storeShorts(final short[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeShorts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeShorts(final short[] array, final File file) throws IOException {
        final int length = array.length;
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeShort(array[i]);
        }
        dos.close();
    }
    
    public static void storeShorts(final short[] array, final CharSequence filename) throws IOException {
        storeShorts(array, new File(filename.toString()));
    }
    
    public static long loadShorts(final DataInput dataInput, final short[][] array, final long offset, final long length) throws IOException {
        ShortBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final short[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readShort();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadShorts(final DataInput dataInput, final short[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final short[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readShort();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadShorts(final File file, final short[][] array, final long offset, final long length) throws IOException {
        ShortBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final short[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dis.readShort();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadShorts(final CharSequence filename, final short[][] array, final long offset, final long length) throws IOException {
        return loadShorts(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadShorts(final File file, final short[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final short[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dis.readShort();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadShorts(final CharSequence filename, final short[][] array) throws IOException {
        return loadShorts(new File(filename.toString()), array);
    }
    
    public static short[][] loadShortsBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 2L;
        final short[][] array = ShortBigArrays.newBigArray(length);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < array.length; ++i) {
            final short[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                t[d] = dis.readShort();
            }
        }
        dis.close();
        return array;
    }
    
    public static short[][] loadShortsBig(final CharSequence filename) throws IOException {
        return loadShortsBig(new File(filename.toString()));
    }
    
    public static void storeShorts(final short[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        ShortBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final short[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dataOutput.writeShort(t[d]);
            }
        }
    }
    
    public static void storeShorts(final short[][] array, final DataOutput dataOutput) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            final short[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dataOutput.writeShort(t[d]);
            }
        }
    }
    
    public static void storeShorts(final short[][] array, final long offset, final long length, final File file) throws IOException {
        ShortBigArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final short[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dos.writeShort(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeShorts(final short[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeShorts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeShorts(final short[][] array, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < array.length; ++i) {
            final short[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dos.writeShort(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeShorts(final short[][] array, final CharSequence filename) throws IOException {
        storeShorts(array, new File(filename.toString()));
    }
    
    public static void storeShorts(final ShortIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeShort(i.nextShort());
        }
    }
    
    public static void storeShorts(final ShortIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeShort(i.nextShort());
        }
        dos.close();
    }
    
    public static void storeShorts(final ShortIterator i, final CharSequence filename) throws IOException {
        storeShorts(i, new File(filename.toString()));
    }
    
    public static ShortIterator asShortIterator(final DataInput dataInput) {
        return new ShortDataInputWrapper(dataInput);
    }
    
    public static ShortIterator asShortIterator(final File file) throws IOException {
        return new ShortDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static ShortIterator asShortIterator(final CharSequence filename) throws IOException {
        return asShortIterator(new File(filename.toString()));
    }
    
    public static ShortIterable asShortIterable(final File file) {
        return new ShortIterable() {
            @Override
            public ShortIterator iterator() {
                try {
                    return BinIO.asShortIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static ShortIterable asShortIterable(final CharSequence filename) {
        return new ShortIterable() {
            @Override
            public ShortIterator iterator() {
                try {
                    return BinIO.asShortIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadChars(final DataInput dataInput, final char[] array, final int offset, final int length) throws IOException {
        CharArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readChar();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadChars(final DataInput dataInput, final char[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readChar();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadChars(final File file, final char[] array, final int offset, final int length) throws IOException {
        CharArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dis.readChar();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadChars(final CharSequence filename, final char[] array, final int offset, final int length) throws IOException {
        return loadChars(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadChars(final File file, final char[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dis.readChar();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadChars(final CharSequence filename, final char[] array) throws IOException {
        return loadChars(new File(filename.toString()), array);
    }
    
    public static char[] loadChars(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 2L;
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final char[] array = new char[(int)length];
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < length; ++i) {
            array[i] = dis.readChar();
        }
        dis.close();
        return array;
    }
    
    public static char[] loadChars(final CharSequence filename) throws IOException {
        return loadChars(new File(filename.toString()));
    }
    
    public static void storeChars(final char[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        CharArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            dataOutput.writeChar(array[offset + i]);
        }
    }
    
    public static void storeChars(final char[] array, final DataOutput dataOutput) throws IOException {
        for (int length = array.length, i = 0; i < length; ++i) {
            dataOutput.writeChar(array[i]);
        }
    }
    
    public static void storeChars(final char[] array, final int offset, final int length, final File file) throws IOException {
        CharArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeChar(array[offset + i]);
        }
        dos.close();
    }
    
    public static void storeChars(final char[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeChars(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeChars(final char[] array, final File file) throws IOException {
        final int length = array.length;
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeChar(array[i]);
        }
        dos.close();
    }
    
    public static void storeChars(final char[] array, final CharSequence filename) throws IOException {
        storeChars(array, new File(filename.toString()));
    }
    
    public static long loadChars(final DataInput dataInput, final char[][] array, final long offset, final long length) throws IOException {
        CharBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final char[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readChar();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadChars(final DataInput dataInput, final char[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final char[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readChar();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadChars(final File file, final char[][] array, final long offset, final long length) throws IOException {
        CharBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final char[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dis.readChar();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadChars(final CharSequence filename, final char[][] array, final long offset, final long length) throws IOException {
        return loadChars(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadChars(final File file, final char[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final char[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dis.readChar();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadChars(final CharSequence filename, final char[][] array) throws IOException {
        return loadChars(new File(filename.toString()), array);
    }
    
    public static char[][] loadCharsBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 2L;
        final char[][] array = CharBigArrays.newBigArray(length);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < array.length; ++i) {
            final char[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                t[d] = dis.readChar();
            }
        }
        dis.close();
        return array;
    }
    
    public static char[][] loadCharsBig(final CharSequence filename) throws IOException {
        return loadCharsBig(new File(filename.toString()));
    }
    
    public static void storeChars(final char[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        CharBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final char[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dataOutput.writeChar(t[d]);
            }
        }
    }
    
    public static void storeChars(final char[][] array, final DataOutput dataOutput) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            final char[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dataOutput.writeChar(t[d]);
            }
        }
    }
    
    public static void storeChars(final char[][] array, final long offset, final long length, final File file) throws IOException {
        CharBigArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final char[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dos.writeChar(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeChars(final char[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeChars(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeChars(final char[][] array, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < array.length; ++i) {
            final char[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dos.writeChar(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeChars(final char[][] array, final CharSequence filename) throws IOException {
        storeChars(array, new File(filename.toString()));
    }
    
    public static void storeChars(final CharIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeChar(i.nextChar());
        }
    }
    
    public static void storeChars(final CharIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeChar(i.nextChar());
        }
        dos.close();
    }
    
    public static void storeChars(final CharIterator i, final CharSequence filename) throws IOException {
        storeChars(i, new File(filename.toString()));
    }
    
    public static CharIterator asCharIterator(final DataInput dataInput) {
        return new CharDataInputWrapper(dataInput);
    }
    
    public static CharIterator asCharIterator(final File file) throws IOException {
        return new CharDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static CharIterator asCharIterator(final CharSequence filename) throws IOException {
        return asCharIterator(new File(filename.toString()));
    }
    
    public static CharIterable asCharIterable(final File file) {
        return new CharIterable() {
            @Override
            public CharIterator iterator() {
                try {
                    return BinIO.asCharIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static CharIterable asCharIterable(final CharSequence filename) {
        return new CharIterable() {
            @Override
            public CharIterator iterator() {
                try {
                    return BinIO.asCharIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadInts(final DataInput dataInput, final int[] array, final int offset, final int length) throws IOException {
        IntArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readInt();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadInts(final DataInput dataInput, final int[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readInt();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadInts(final File file, final int[] array, final int offset, final int length) throws IOException {
        IntArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dis.readInt();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadInts(final CharSequence filename, final int[] array, final int offset, final int length) throws IOException {
        return loadInts(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadInts(final File file, final int[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dis.readInt();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadInts(final CharSequence filename, final int[] array) throws IOException {
        return loadInts(new File(filename.toString()), array);
    }
    
    public static int[] loadInts(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 4L;
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final int[] array = new int[(int)length];
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < length; ++i) {
            array[i] = dis.readInt();
        }
        dis.close();
        return array;
    }
    
    public static int[] loadInts(final CharSequence filename) throws IOException {
        return loadInts(new File(filename.toString()));
    }
    
    public static void storeInts(final int[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        IntArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            dataOutput.writeInt(array[offset + i]);
        }
    }
    
    public static void storeInts(final int[] array, final DataOutput dataOutput) throws IOException {
        for (int length = array.length, i = 0; i < length; ++i) {
            dataOutput.writeInt(array[i]);
        }
    }
    
    public static void storeInts(final int[] array, final int offset, final int length, final File file) throws IOException {
        IntArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeInt(array[offset + i]);
        }
        dos.close();
    }
    
    public static void storeInts(final int[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeInts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeInts(final int[] array, final File file) throws IOException {
        final int length = array.length;
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeInt(array[i]);
        }
        dos.close();
    }
    
    public static void storeInts(final int[] array, final CharSequence filename) throws IOException {
        storeInts(array, new File(filename.toString()));
    }
    
    public static long loadInts(final DataInput dataInput, final int[][] array, final long offset, final long length) throws IOException {
        IntBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final int[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readInt();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadInts(final DataInput dataInput, final int[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final int[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readInt();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadInts(final File file, final int[][] array, final long offset, final long length) throws IOException {
        IntBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final int[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dis.readInt();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadInts(final CharSequence filename, final int[][] array, final long offset, final long length) throws IOException {
        return loadInts(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadInts(final File file, final int[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final int[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dis.readInt();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadInts(final CharSequence filename, final int[][] array) throws IOException {
        return loadInts(new File(filename.toString()), array);
    }
    
    public static int[][] loadIntsBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 4L;
        final int[][] array = IntBigArrays.newBigArray(length);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < array.length; ++i) {
            final int[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                t[d] = dis.readInt();
            }
        }
        dis.close();
        return array;
    }
    
    public static int[][] loadIntsBig(final CharSequence filename) throws IOException {
        return loadIntsBig(new File(filename.toString()));
    }
    
    public static void storeInts(final int[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        IntBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final int[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dataOutput.writeInt(t[d]);
            }
        }
    }
    
    public static void storeInts(final int[][] array, final DataOutput dataOutput) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            final int[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dataOutput.writeInt(t[d]);
            }
        }
    }
    
    public static void storeInts(final int[][] array, final long offset, final long length, final File file) throws IOException {
        IntBigArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final int[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dos.writeInt(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeInts(final int[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeInts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeInts(final int[][] array, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < array.length; ++i) {
            final int[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dos.writeInt(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeInts(final int[][] array, final CharSequence filename) throws IOException {
        storeInts(array, new File(filename.toString()));
    }
    
    public static void storeInts(final IntIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeInt(i.nextInt());
        }
    }
    
    public static void storeInts(final IntIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeInt(i.nextInt());
        }
        dos.close();
    }
    
    public static void storeInts(final IntIterator i, final CharSequence filename) throws IOException {
        storeInts(i, new File(filename.toString()));
    }
    
    public static IntIterator asIntIterator(final DataInput dataInput) {
        return new IntDataInputWrapper(dataInput);
    }
    
    public static IntIterator asIntIterator(final File file) throws IOException {
        return new IntDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static IntIterator asIntIterator(final CharSequence filename) throws IOException {
        return asIntIterator(new File(filename.toString()));
    }
    
    public static IntIterable asIntIterable(final File file) {
        return new IntIterable() {
            @Override
            public IntIterator iterator() {
                try {
                    return BinIO.asIntIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static IntIterable asIntIterable(final CharSequence filename) {
        return new IntIterable() {
            @Override
            public IntIterator iterator() {
                try {
                    return BinIO.asIntIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadLongs(final DataInput dataInput, final long[] array, final int offset, final int length) throws IOException {
        LongArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readLong();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadLongs(final DataInput dataInput, final long[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readLong();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadLongs(final File file, final long[] array, final int offset, final int length) throws IOException {
        LongArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dis.readLong();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadLongs(final CharSequence filename, final long[] array, final int offset, final int length) throws IOException {
        return loadLongs(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadLongs(final File file, final long[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dis.readLong();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadLongs(final CharSequence filename, final long[] array) throws IOException {
        return loadLongs(new File(filename.toString()), array);
    }
    
    public static long[] loadLongs(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 8L;
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final long[] array = new long[(int)length];
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < length; ++i) {
            array[i] = dis.readLong();
        }
        dis.close();
        return array;
    }
    
    public static long[] loadLongs(final CharSequence filename) throws IOException {
        return loadLongs(new File(filename.toString()));
    }
    
    public static void storeLongs(final long[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        LongArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            dataOutput.writeLong(array[offset + i]);
        }
    }
    
    public static void storeLongs(final long[] array, final DataOutput dataOutput) throws IOException {
        for (int length = array.length, i = 0; i < length; ++i) {
            dataOutput.writeLong(array[i]);
        }
    }
    
    public static void storeLongs(final long[] array, final int offset, final int length, final File file) throws IOException {
        LongArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeLong(array[offset + i]);
        }
        dos.close();
    }
    
    public static void storeLongs(final long[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeLongs(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeLongs(final long[] array, final File file) throws IOException {
        final int length = array.length;
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeLong(array[i]);
        }
        dos.close();
    }
    
    public static void storeLongs(final long[] array, final CharSequence filename) throws IOException {
        storeLongs(array, new File(filename.toString()));
    }
    
    public static long loadLongs(final DataInput dataInput, final long[][] array, final long offset, final long length) throws IOException {
        LongBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final long[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readLong();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadLongs(final DataInput dataInput, final long[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final long[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readLong();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadLongs(final File file, final long[][] array, final long offset, final long length) throws IOException {
        LongBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final long[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dis.readLong();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadLongs(final CharSequence filename, final long[][] array, final long offset, final long length) throws IOException {
        return loadLongs(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadLongs(final File file, final long[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final long[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dis.readLong();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadLongs(final CharSequence filename, final long[][] array) throws IOException {
        return loadLongs(new File(filename.toString()), array);
    }
    
    public static long[][] loadLongsBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 8L;
        final long[][] array = LongBigArrays.newBigArray(length);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < array.length; ++i) {
            final long[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                t[d] = dis.readLong();
            }
        }
        dis.close();
        return array;
    }
    
    public static long[][] loadLongsBig(final CharSequence filename) throws IOException {
        return loadLongsBig(new File(filename.toString()));
    }
    
    public static void storeLongs(final long[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        LongBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final long[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dataOutput.writeLong(t[d]);
            }
        }
    }
    
    public static void storeLongs(final long[][] array, final DataOutput dataOutput) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            final long[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dataOutput.writeLong(t[d]);
            }
        }
    }
    
    public static void storeLongs(final long[][] array, final long offset, final long length, final File file) throws IOException {
        LongBigArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final long[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dos.writeLong(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeLongs(final long[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeLongs(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeLongs(final long[][] array, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < array.length; ++i) {
            final long[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dos.writeLong(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeLongs(final long[][] array, final CharSequence filename) throws IOException {
        storeLongs(array, new File(filename.toString()));
    }
    
    public static void storeLongs(final LongIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeLong(i.nextLong());
        }
    }
    
    public static void storeLongs(final LongIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeLong(i.nextLong());
        }
        dos.close();
    }
    
    public static void storeLongs(final LongIterator i, final CharSequence filename) throws IOException {
        storeLongs(i, new File(filename.toString()));
    }
    
    public static LongIterator asLongIterator(final DataInput dataInput) {
        return new LongDataInputWrapper(dataInput);
    }
    
    public static LongIterator asLongIterator(final File file) throws IOException {
        return new LongDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static LongIterator asLongIterator(final CharSequence filename) throws IOException {
        return asLongIterator(new File(filename.toString()));
    }
    
    public static LongIterable asLongIterable(final File file) {
        return new LongIterable() {
            @Override
            public LongIterator iterator() {
                try {
                    return BinIO.asLongIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static LongIterable asLongIterable(final CharSequence filename) {
        return new LongIterable() {
            @Override
            public LongIterator iterator() {
                try {
                    return BinIO.asLongIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadFloats(final DataInput dataInput, final float[] array, final int offset, final int length) throws IOException {
        FloatArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readFloat();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadFloats(final DataInput dataInput, final float[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readFloat();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadFloats(final File file, final float[] array, final int offset, final int length) throws IOException {
        FloatArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dis.readFloat();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadFloats(final CharSequence filename, final float[] array, final int offset, final int length) throws IOException {
        return loadFloats(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadFloats(final File file, final float[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dis.readFloat();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadFloats(final CharSequence filename, final float[] array) throws IOException {
        return loadFloats(new File(filename.toString()), array);
    }
    
    public static float[] loadFloats(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 4L;
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final float[] array = new float[(int)length];
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < length; ++i) {
            array[i] = dis.readFloat();
        }
        dis.close();
        return array;
    }
    
    public static float[] loadFloats(final CharSequence filename) throws IOException {
        return loadFloats(new File(filename.toString()));
    }
    
    public static void storeFloats(final float[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        FloatArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            dataOutput.writeFloat(array[offset + i]);
        }
    }
    
    public static void storeFloats(final float[] array, final DataOutput dataOutput) throws IOException {
        for (int length = array.length, i = 0; i < length; ++i) {
            dataOutput.writeFloat(array[i]);
        }
    }
    
    public static void storeFloats(final float[] array, final int offset, final int length, final File file) throws IOException {
        FloatArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeFloat(array[offset + i]);
        }
        dos.close();
    }
    
    public static void storeFloats(final float[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeFloats(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeFloats(final float[] array, final File file) throws IOException {
        final int length = array.length;
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeFloat(array[i]);
        }
        dos.close();
    }
    
    public static void storeFloats(final float[] array, final CharSequence filename) throws IOException {
        storeFloats(array, new File(filename.toString()));
    }
    
    public static long loadFloats(final DataInput dataInput, final float[][] array, final long offset, final long length) throws IOException {
        FloatBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final float[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readFloat();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadFloats(final DataInput dataInput, final float[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final float[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readFloat();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadFloats(final File file, final float[][] array, final long offset, final long length) throws IOException {
        FloatBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final float[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dis.readFloat();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadFloats(final CharSequence filename, final float[][] array, final long offset, final long length) throws IOException {
        return loadFloats(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadFloats(final File file, final float[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final float[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dis.readFloat();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadFloats(final CharSequence filename, final float[][] array) throws IOException {
        return loadFloats(new File(filename.toString()), array);
    }
    
    public static float[][] loadFloatsBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 4L;
        final float[][] array = FloatBigArrays.newBigArray(length);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < array.length; ++i) {
            final float[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                t[d] = dis.readFloat();
            }
        }
        dis.close();
        return array;
    }
    
    public static float[][] loadFloatsBig(final CharSequence filename) throws IOException {
        return loadFloatsBig(new File(filename.toString()));
    }
    
    public static void storeFloats(final float[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        FloatBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final float[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dataOutput.writeFloat(t[d]);
            }
        }
    }
    
    public static void storeFloats(final float[][] array, final DataOutput dataOutput) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            final float[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dataOutput.writeFloat(t[d]);
            }
        }
    }
    
    public static void storeFloats(final float[][] array, final long offset, final long length, final File file) throws IOException {
        FloatBigArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final float[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dos.writeFloat(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeFloats(final float[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeFloats(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeFloats(final float[][] array, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < array.length; ++i) {
            final float[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dos.writeFloat(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeFloats(final float[][] array, final CharSequence filename) throws IOException {
        storeFloats(array, new File(filename.toString()));
    }
    
    public static void storeFloats(final FloatIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeFloat(i.nextFloat());
        }
    }
    
    public static void storeFloats(final FloatIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeFloat(i.nextFloat());
        }
        dos.close();
    }
    
    public static void storeFloats(final FloatIterator i, final CharSequence filename) throws IOException {
        storeFloats(i, new File(filename.toString()));
    }
    
    public static FloatIterator asFloatIterator(final DataInput dataInput) {
        return new FloatDataInputWrapper(dataInput);
    }
    
    public static FloatIterator asFloatIterator(final File file) throws IOException {
        return new FloatDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static FloatIterator asFloatIterator(final CharSequence filename) throws IOException {
        return asFloatIterator(new File(filename.toString()));
    }
    
    public static FloatIterable asFloatIterable(final File file) {
        return new FloatIterable() {
            @Override
            public FloatIterator iterator() {
                try {
                    return BinIO.asFloatIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static FloatIterable asFloatIterable(final CharSequence filename) {
        return new FloatIterable() {
            @Override
            public FloatIterator iterator() {
                try {
                    return BinIO.asFloatIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadDoubles(final DataInput dataInput, final double[] array, final int offset, final int length) throws IOException {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dataInput.readDouble();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadDoubles(final DataInput dataInput, final double[] array) throws IOException {
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dataInput.readDouble();
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadDoubles(final File file, final double[] array, final int offset, final int length) throws IOException {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (i = 0; i < length; ++i) {
                array[i + offset] = dis.readDouble();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadDoubles(final CharSequence filename, final double[] array, final int offset, final int length) throws IOException {
        return loadDoubles(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadDoubles(final File file, final double[] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        int i = 0;
        try {
            for (final int length = array.length, i = 0; i < length; ++i) {
                array[i] = dis.readDouble();
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return i;
    }
    
    public static int loadDoubles(final CharSequence filename, final double[] array) throws IOException {
        return loadDoubles(new File(filename.toString()), array);
    }
    
    public static double[] loadDoubles(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 8L;
        if (length > 2147483647L) {
            fis.close();
            throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
        }
        final double[] array = new double[(int)length];
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < length; ++i) {
            array[i] = dis.readDouble();
        }
        dis.close();
        return array;
    }
    
    public static double[] loadDoubles(final CharSequence filename) throws IOException {
        return loadDoubles(new File(filename.toString()));
    }
    
    public static void storeDoubles(final double[] array, final int offset, final int length, final DataOutput dataOutput) throws IOException {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            dataOutput.writeDouble(array[offset + i]);
        }
    }
    
    public static void storeDoubles(final double[] array, final DataOutput dataOutput) throws IOException {
        for (int length = array.length, i = 0; i < length; ++i) {
            dataOutput.writeDouble(array[i]);
        }
    }
    
    public static void storeDoubles(final double[] array, final int offset, final int length, final File file) throws IOException {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeDouble(array[offset + i]);
        }
        dos.close();
    }
    
    public static void storeDoubles(final double[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeDoubles(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeDoubles(final double[] array, final File file) throws IOException {
        final int length = array.length;
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < length; ++i) {
            dos.writeDouble(array[i]);
        }
        dos.close();
    }
    
    public static void storeDoubles(final double[] array, final CharSequence filename) throws IOException {
        storeDoubles(array, new File(filename.toString()));
    }
    
    public static long loadDoubles(final DataInput dataInput, final double[][] array, final long offset, final long length) throws IOException {
        DoubleBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final double[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dataInput.readDouble();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadDoubles(final DataInput dataInput, final double[][] array) throws IOException {
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final double[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dataInput.readDouble();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadDoubles(final File file, final double[][] array, final long offset, final long length) throws IOException {
        DoubleBigArrays.ensureOffsetLength(array, offset, length);
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final double[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    t[d] = dis.readDouble();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadDoubles(final CharSequence filename, final double[][] array, final long offset, final long length) throws IOException {
        return loadDoubles(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadDoubles(final File file, final double[][] array) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        long c = 0L;
        try {
            for (int i = 0; i < array.length; ++i) {
                final double[] t = array[i];
                for (int l = t.length, d = 0; d < l; ++d) {
                    t[d] = dis.readDouble();
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        dis.close();
        return c;
    }
    
    public static long loadDoubles(final CharSequence filename, final double[][] array) throws IOException {
        return loadDoubles(new File(filename.toString()), array);
    }
    
    public static double[][] loadDoublesBig(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final long length = fis.getChannel().size() / 8L;
        final double[][] array = DoubleBigArrays.newBigArray(length);
        final DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
        for (int i = 0; i < array.length; ++i) {
            final double[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                t[d] = dis.readDouble();
            }
        }
        dis.close();
        return array;
    }
    
    public static double[][] loadDoublesBig(final CharSequence filename) throws IOException {
        return loadDoublesBig(new File(filename.toString()));
    }
    
    public static void storeDoubles(final double[][] array, final long offset, final long length, final DataOutput dataOutput) throws IOException {
        DoubleBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final double[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dataOutput.writeDouble(t[d]);
            }
        }
    }
    
    public static void storeDoubles(final double[][] array, final DataOutput dataOutput) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            final double[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dataOutput.writeDouble(t[d]);
            }
        }
    }
    
    public static void storeDoubles(final double[][] array, final long offset, final long length, final File file) throws IOException {
        DoubleBigArrays.ensureOffsetLength(array, offset, length);
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final double[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                dos.writeDouble(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeDoubles(final double[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeDoubles(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeDoubles(final double[][] array, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        for (int i = 0; i < array.length; ++i) {
            final double[] t = array[i];
            for (int l = t.length, d = 0; d < l; ++d) {
                dos.writeDouble(t[d]);
            }
        }
        dos.close();
    }
    
    public static void storeDoubles(final double[][] array, final CharSequence filename) throws IOException {
        storeDoubles(array, new File(filename.toString()));
    }
    
    public static void storeDoubles(final DoubleIterator i, final DataOutput dataOutput) throws IOException {
        while (i.hasNext()) {
            dataOutput.writeDouble(i.nextDouble());
        }
    }
    
    public static void storeDoubles(final DoubleIterator i, final File file) throws IOException {
        final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        while (i.hasNext()) {
            dos.writeDouble(i.nextDouble());
        }
        dos.close();
    }
    
    public static void storeDoubles(final DoubleIterator i, final CharSequence filename) throws IOException {
        storeDoubles(i, new File(filename.toString()));
    }
    
    public static DoubleIterator asDoubleIterator(final DataInput dataInput) {
        return new DoubleDataInputWrapper(dataInput);
    }
    
    public static DoubleIterator asDoubleIterator(final File file) throws IOException {
        return new DoubleDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
    }
    
    public static DoubleIterator asDoubleIterator(final CharSequence filename) throws IOException {
        return asDoubleIterator(new File(filename.toString()));
    }
    
    public static DoubleIterable asDoubleIterable(final File file) {
        return new DoubleIterable() {
            @Override
            public DoubleIterator iterator() {
                try {
                    return BinIO.asDoubleIterator(file);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static DoubleIterable asDoubleIterable(final CharSequence filename) {
        return new DoubleIterable() {
            @Override
            public DoubleIterator iterator() {
                try {
                    return BinIO.asDoubleIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    private static final class BooleanDataInputWrapper extends AbstractBooleanIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private boolean next;
        
        public BooleanDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readBoolean();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
    
    private static final class ByteDataInputWrapper extends AbstractByteIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private byte next;
        
        public ByteDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readByte();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
    
    private static final class ShortDataInputWrapper extends AbstractShortIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private short next;
        
        public ShortDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readShort();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
    
    private static final class CharDataInputWrapper extends AbstractCharIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private char next;
        
        public CharDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readChar();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
    
    private static final class IntDataInputWrapper extends AbstractIntIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private int next;
        
        public IntDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readInt();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
    
    private static final class LongDataInputWrapper extends AbstractLongIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private long next;
        
        public LongDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readLong();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
    
    private static final class FloatDataInputWrapper extends AbstractFloatIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private float next;
        
        public FloatDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readFloat();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public float nextFloat() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
    
    private static final class DoubleDataInputWrapper extends AbstractDoubleIterator
    {
        private final DataInput dataInput;
        private boolean toAdvance;
        private boolean endOfProcess;
        private double next;
        
        public DoubleDataInputWrapper(final DataInput dataInput) {
            this.toAdvance = true;
            this.endOfProcess = false;
            this.dataInput = dataInput;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return !this.endOfProcess;
            }
            this.toAdvance = false;
            try {
                this.next = this.dataInput.readDouble();
            }
            catch (final EOFException eof) {
                this.endOfProcess = true;
            }
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            return !this.endOfProcess;
        }
        
        @Override
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.toAdvance = true;
            return this.next;
        }
    }
}
