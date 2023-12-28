// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.io;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteIterator;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterable;
import it.unimi.dsi.fastutil.doubles.DoubleBigArrays;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.floats.FloatIterable;
import it.unimi.dsi.fastutil.floats.FloatBigArrays;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.longs.LongIterable;
import it.unimi.dsi.fastutil.longs.LongBigArrays;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.ints.IntIterable;
import it.unimi.dsi.fastutil.ints.IntBigArrays;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.shorts.ShortIterable;
import it.unimi.dsi.fastutil.shorts.ShortBigArrays;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import it.unimi.dsi.fastutil.bytes.ByteIterable;
import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import java.util.Iterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterable;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.booleans.BooleanBigArrays;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import java.io.BufferedReader;

public class TextIO
{
    public static final int BUFFER_SIZE = 8192;
    
    private TextIO() {
    }
    
    public static int loadBooleans(final BufferedReader reader, final boolean[] array, final int offset, final int length) throws IOException {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            String s;
            for (i = 0; i < length && (s = reader.readLine()) != null; ++i) {
                array[i + offset] = Boolean.parseBoolean(s.trim());
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadBooleans(final BufferedReader reader, final boolean[] array) throws IOException {
        return loadBooleans(reader, array, 0, array.length);
    }
    
    public static int loadBooleans(final File file, final boolean[] array, final int offset, final int length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final int result = loadBooleans(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static int loadBooleans(final CharSequence filename, final boolean[] array, final int offset, final int length) throws IOException {
        return loadBooleans(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadBooleans(final File file, final boolean[] array) throws IOException {
        return loadBooleans(file, array, 0, array.length);
    }
    
    public static int loadBooleans(final CharSequence filename, final boolean[] array) throws IOException {
        return loadBooleans(filename, array, 0, array.length);
    }
    
    public static void storeBooleans(final boolean[] array, final int offset, final int length, final PrintStream stream) {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            stream.println(array[offset + i]);
        }
    }
    
    public static void storeBooleans(final boolean[] array, final PrintStream stream) {
        storeBooleans(array, 0, array.length, stream);
    }
    
    public static void storeBooleans(final boolean[] array, final int offset, final int length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeBooleans(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeBooleans(final boolean[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeBooleans(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBooleans(final boolean[] array, final File file) throws IOException {
        storeBooleans(array, 0, array.length, file);
    }
    
    public static void storeBooleans(final boolean[] array, final CharSequence filename) throws IOException {
        storeBooleans(array, 0, array.length, filename);
    }
    
    public static void storeBooleans(final BooleanIterator i, final PrintStream stream) {
        while (i.hasNext()) {
            stream.println(i.nextBoolean());
        }
    }
    
    public static void storeBooleans(final BooleanIterator i, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeBooleans(i, stream);
        stream.close();
    }
    
    public static void storeBooleans(final BooleanIterator i, final CharSequence filename) throws IOException {
        storeBooleans(i, new File(filename.toString()));
    }
    
    public static long loadBooleans(final BufferedReader reader, final boolean[][] array, final long offset, final long length) throws IOException {
        BooleanBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final boolean[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    final String s;
                    if ((s = reader.readLine()) == null) {
                        return c;
                    }
                    t[d] = Boolean.parseBoolean(s.trim());
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadBooleans(final BufferedReader reader, final boolean[][] array) throws IOException {
        return loadBooleans(reader, array, 0L, BooleanBigArrays.length(array));
    }
    
    public static long loadBooleans(final File file, final boolean[][] array, final long offset, final long length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final long result = loadBooleans(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static long loadBooleans(final CharSequence filename, final boolean[][] array, final long offset, final long length) throws IOException {
        return loadBooleans(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadBooleans(final File file, final boolean[][] array) throws IOException {
        return loadBooleans(file, array, 0L, BooleanBigArrays.length(array));
    }
    
    public static long loadBooleans(final CharSequence filename, final boolean[][] array) throws IOException {
        return loadBooleans(filename, array, 0L, BooleanBigArrays.length(array));
    }
    
    public static void storeBooleans(final boolean[][] array, final long offset, final long length, final PrintStream stream) {
        BooleanBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final boolean[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                stream.println(t[d]);
            }
        }
    }
    
    public static void storeBooleans(final boolean[][] array, final PrintStream stream) {
        storeBooleans(array, 0L, BooleanBigArrays.length(array), stream);
    }
    
    public static void storeBooleans(final boolean[][] array, final long offset, final long length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeBooleans(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeBooleans(final boolean[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeBooleans(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBooleans(final boolean[][] array, final File file) throws IOException {
        storeBooleans(array, 0L, BooleanBigArrays.length(array), file);
    }
    
    public static void storeBooleans(final boolean[][] array, final CharSequence filename) throws IOException {
        storeBooleans(array, 0L, BooleanBigArrays.length(array), filename);
    }
    
    public static BooleanIterator asBooleanIterator(final BufferedReader reader) {
        return new BooleanReaderWrapper(reader);
    }
    
    public static BooleanIterator asBooleanIterator(final File file) throws IOException {
        return new BooleanReaderWrapper(new BufferedReader(new FileReader(file)));
    }
    
    public static BooleanIterator asBooleanIterator(final CharSequence filename) throws IOException {
        return asBooleanIterator(new File(filename.toString()));
    }
    
    public static BooleanIterable asBooleanIterable(final File file) {
        return new BooleanIterable() {
            @Override
            public BooleanIterator iterator() {
                try {
                    return TextIO.asBooleanIterator(file);
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
                    return TextIO.asBooleanIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadBytes(final BufferedReader reader, final byte[] array, final int offset, final int length) throws IOException {
        ByteArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            String s;
            for (i = 0; i < length && (s = reader.readLine()) != null; ++i) {
                array[i + offset] = Byte.parseByte(s.trim());
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadBytes(final BufferedReader reader, final byte[] array) throws IOException {
        return loadBytes(reader, array, 0, array.length);
    }
    
    public static int loadBytes(final File file, final byte[] array, final int offset, final int length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final int result = loadBytes(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static int loadBytes(final CharSequence filename, final byte[] array, final int offset, final int length) throws IOException {
        return loadBytes(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadBytes(final File file, final byte[] array) throws IOException {
        return loadBytes(file, array, 0, array.length);
    }
    
    public static int loadBytes(final CharSequence filename, final byte[] array) throws IOException {
        return loadBytes(filename, array, 0, array.length);
    }
    
    public static void storeBytes(final byte[] array, final int offset, final int length, final PrintStream stream) {
        ByteArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            stream.println(array[offset + i]);
        }
    }
    
    public static void storeBytes(final byte[] array, final PrintStream stream) {
        storeBytes(array, 0, array.length, stream);
    }
    
    public static void storeBytes(final byte[] array, final int offset, final int length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeBytes(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeBytes(final byte[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeBytes(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBytes(final byte[] array, final File file) throws IOException {
        storeBytes(array, 0, array.length, file);
    }
    
    public static void storeBytes(final byte[] array, final CharSequence filename) throws IOException {
        storeBytes(array, 0, array.length, filename);
    }
    
    public static void storeBytes(final ByteIterator i, final PrintStream stream) {
        while (i.hasNext()) {
            stream.println(i.nextByte());
        }
    }
    
    public static void storeBytes(final ByteIterator i, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeBytes(i, stream);
        stream.close();
    }
    
    public static void storeBytes(final ByteIterator i, final CharSequence filename) throws IOException {
        storeBytes(i, new File(filename.toString()));
    }
    
    public static long loadBytes(final BufferedReader reader, final byte[][] array, final long offset, final long length) throws IOException {
        ByteBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final byte[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    final String s;
                    if ((s = reader.readLine()) == null) {
                        return c;
                    }
                    t[d] = Byte.parseByte(s.trim());
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadBytes(final BufferedReader reader, final byte[][] array) throws IOException {
        return loadBytes(reader, array, 0L, ByteBigArrays.length(array));
    }
    
    public static long loadBytes(final File file, final byte[][] array, final long offset, final long length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final long result = loadBytes(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static long loadBytes(final CharSequence filename, final byte[][] array, final long offset, final long length) throws IOException {
        return loadBytes(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadBytes(final File file, final byte[][] array) throws IOException {
        return loadBytes(file, array, 0L, ByteBigArrays.length(array));
    }
    
    public static long loadBytes(final CharSequence filename, final byte[][] array) throws IOException {
        return loadBytes(filename, array, 0L, ByteBigArrays.length(array));
    }
    
    public static void storeBytes(final byte[][] array, final long offset, final long length, final PrintStream stream) {
        ByteBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final byte[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                stream.println(t[d]);
            }
        }
    }
    
    public static void storeBytes(final byte[][] array, final PrintStream stream) {
        storeBytes(array, 0L, ByteBigArrays.length(array), stream);
    }
    
    public static void storeBytes(final byte[][] array, final long offset, final long length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeBytes(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeBytes(final byte[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeBytes(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeBytes(final byte[][] array, final File file) throws IOException {
        storeBytes(array, 0L, ByteBigArrays.length(array), file);
    }
    
    public static void storeBytes(final byte[][] array, final CharSequence filename) throws IOException {
        storeBytes(array, 0L, ByteBigArrays.length(array), filename);
    }
    
    public static ByteIterator asByteIterator(final BufferedReader reader) {
        return new ByteReaderWrapper(reader);
    }
    
    public static ByteIterator asByteIterator(final File file) throws IOException {
        return new ByteReaderWrapper(new BufferedReader(new FileReader(file)));
    }
    
    public static ByteIterator asByteIterator(final CharSequence filename) throws IOException {
        return asByteIterator(new File(filename.toString()));
    }
    
    public static ByteIterable asByteIterable(final File file) {
        return new ByteIterable() {
            @Override
            public ByteIterator iterator() {
                try {
                    return TextIO.asByteIterator(file);
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
                    return TextIO.asByteIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadShorts(final BufferedReader reader, final short[] array, final int offset, final int length) throws IOException {
        ShortArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            String s;
            for (i = 0; i < length && (s = reader.readLine()) != null; ++i) {
                array[i + offset] = Short.parseShort(s.trim());
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadShorts(final BufferedReader reader, final short[] array) throws IOException {
        return loadShorts(reader, array, 0, array.length);
    }
    
    public static int loadShorts(final File file, final short[] array, final int offset, final int length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final int result = loadShorts(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static int loadShorts(final CharSequence filename, final short[] array, final int offset, final int length) throws IOException {
        return loadShorts(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadShorts(final File file, final short[] array) throws IOException {
        return loadShorts(file, array, 0, array.length);
    }
    
    public static int loadShorts(final CharSequence filename, final short[] array) throws IOException {
        return loadShorts(filename, array, 0, array.length);
    }
    
    public static void storeShorts(final short[] array, final int offset, final int length, final PrintStream stream) {
        ShortArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            stream.println(array[offset + i]);
        }
    }
    
    public static void storeShorts(final short[] array, final PrintStream stream) {
        storeShorts(array, 0, array.length, stream);
    }
    
    public static void storeShorts(final short[] array, final int offset, final int length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeShorts(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeShorts(final short[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeShorts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeShorts(final short[] array, final File file) throws IOException {
        storeShorts(array, 0, array.length, file);
    }
    
    public static void storeShorts(final short[] array, final CharSequence filename) throws IOException {
        storeShorts(array, 0, array.length, filename);
    }
    
    public static void storeShorts(final ShortIterator i, final PrintStream stream) {
        while (i.hasNext()) {
            stream.println(i.nextShort());
        }
    }
    
    public static void storeShorts(final ShortIterator i, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeShorts(i, stream);
        stream.close();
    }
    
    public static void storeShorts(final ShortIterator i, final CharSequence filename) throws IOException {
        storeShorts(i, new File(filename.toString()));
    }
    
    public static long loadShorts(final BufferedReader reader, final short[][] array, final long offset, final long length) throws IOException {
        ShortBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final short[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    final String s;
                    if ((s = reader.readLine()) == null) {
                        return c;
                    }
                    t[d] = Short.parseShort(s.trim());
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadShorts(final BufferedReader reader, final short[][] array) throws IOException {
        return loadShorts(reader, array, 0L, ShortBigArrays.length(array));
    }
    
    public static long loadShorts(final File file, final short[][] array, final long offset, final long length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final long result = loadShorts(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static long loadShorts(final CharSequence filename, final short[][] array, final long offset, final long length) throws IOException {
        return loadShorts(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadShorts(final File file, final short[][] array) throws IOException {
        return loadShorts(file, array, 0L, ShortBigArrays.length(array));
    }
    
    public static long loadShorts(final CharSequence filename, final short[][] array) throws IOException {
        return loadShorts(filename, array, 0L, ShortBigArrays.length(array));
    }
    
    public static void storeShorts(final short[][] array, final long offset, final long length, final PrintStream stream) {
        ShortBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final short[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                stream.println(t[d]);
            }
        }
    }
    
    public static void storeShorts(final short[][] array, final PrintStream stream) {
        storeShorts(array, 0L, ShortBigArrays.length(array), stream);
    }
    
    public static void storeShorts(final short[][] array, final long offset, final long length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeShorts(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeShorts(final short[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeShorts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeShorts(final short[][] array, final File file) throws IOException {
        storeShorts(array, 0L, ShortBigArrays.length(array), file);
    }
    
    public static void storeShorts(final short[][] array, final CharSequence filename) throws IOException {
        storeShorts(array, 0L, ShortBigArrays.length(array), filename);
    }
    
    public static ShortIterator asShortIterator(final BufferedReader reader) {
        return new ShortReaderWrapper(reader);
    }
    
    public static ShortIterator asShortIterator(final File file) throws IOException {
        return new ShortReaderWrapper(new BufferedReader(new FileReader(file)));
    }
    
    public static ShortIterator asShortIterator(final CharSequence filename) throws IOException {
        return asShortIterator(new File(filename.toString()));
    }
    
    public static ShortIterable asShortIterable(final File file) {
        return new ShortIterable() {
            @Override
            public ShortIterator iterator() {
                try {
                    return TextIO.asShortIterator(file);
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
                    return TextIO.asShortIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadInts(final BufferedReader reader, final int[] array, final int offset, final int length) throws IOException {
        IntArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            String s;
            for (i = 0; i < length && (s = reader.readLine()) != null; ++i) {
                array[i + offset] = Integer.parseInt(s.trim());
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadInts(final BufferedReader reader, final int[] array) throws IOException {
        return loadInts(reader, array, 0, array.length);
    }
    
    public static int loadInts(final File file, final int[] array, final int offset, final int length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final int result = loadInts(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static int loadInts(final CharSequence filename, final int[] array, final int offset, final int length) throws IOException {
        return loadInts(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadInts(final File file, final int[] array) throws IOException {
        return loadInts(file, array, 0, array.length);
    }
    
    public static int loadInts(final CharSequence filename, final int[] array) throws IOException {
        return loadInts(filename, array, 0, array.length);
    }
    
    public static void storeInts(final int[] array, final int offset, final int length, final PrintStream stream) {
        IntArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            stream.println(array[offset + i]);
        }
    }
    
    public static void storeInts(final int[] array, final PrintStream stream) {
        storeInts(array, 0, array.length, stream);
    }
    
    public static void storeInts(final int[] array, final int offset, final int length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeInts(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeInts(final int[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeInts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeInts(final int[] array, final File file) throws IOException {
        storeInts(array, 0, array.length, file);
    }
    
    public static void storeInts(final int[] array, final CharSequence filename) throws IOException {
        storeInts(array, 0, array.length, filename);
    }
    
    public static void storeInts(final IntIterator i, final PrintStream stream) {
        while (i.hasNext()) {
            stream.println(i.nextInt());
        }
    }
    
    public static void storeInts(final IntIterator i, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeInts(i, stream);
        stream.close();
    }
    
    public static void storeInts(final IntIterator i, final CharSequence filename) throws IOException {
        storeInts(i, new File(filename.toString()));
    }
    
    public static long loadInts(final BufferedReader reader, final int[][] array, final long offset, final long length) throws IOException {
        IntBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final int[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    final String s;
                    if ((s = reader.readLine()) == null) {
                        return c;
                    }
                    t[d] = Integer.parseInt(s.trim());
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadInts(final BufferedReader reader, final int[][] array) throws IOException {
        return loadInts(reader, array, 0L, IntBigArrays.length(array));
    }
    
    public static long loadInts(final File file, final int[][] array, final long offset, final long length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final long result = loadInts(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static long loadInts(final CharSequence filename, final int[][] array, final long offset, final long length) throws IOException {
        return loadInts(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadInts(final File file, final int[][] array) throws IOException {
        return loadInts(file, array, 0L, IntBigArrays.length(array));
    }
    
    public static long loadInts(final CharSequence filename, final int[][] array) throws IOException {
        return loadInts(filename, array, 0L, IntBigArrays.length(array));
    }
    
    public static void storeInts(final int[][] array, final long offset, final long length, final PrintStream stream) {
        IntBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final int[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                stream.println(t[d]);
            }
        }
    }
    
    public static void storeInts(final int[][] array, final PrintStream stream) {
        storeInts(array, 0L, IntBigArrays.length(array), stream);
    }
    
    public static void storeInts(final int[][] array, final long offset, final long length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeInts(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeInts(final int[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeInts(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeInts(final int[][] array, final File file) throws IOException {
        storeInts(array, 0L, IntBigArrays.length(array), file);
    }
    
    public static void storeInts(final int[][] array, final CharSequence filename) throws IOException {
        storeInts(array, 0L, IntBigArrays.length(array), filename);
    }
    
    public static IntIterator asIntIterator(final BufferedReader reader) {
        return new IntReaderWrapper(reader);
    }
    
    public static IntIterator asIntIterator(final File file) throws IOException {
        return new IntReaderWrapper(new BufferedReader(new FileReader(file)));
    }
    
    public static IntIterator asIntIterator(final CharSequence filename) throws IOException {
        return asIntIterator(new File(filename.toString()));
    }
    
    public static IntIterable asIntIterable(final File file) {
        return new IntIterable() {
            @Override
            public IntIterator iterator() {
                try {
                    return TextIO.asIntIterator(file);
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
                    return TextIO.asIntIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadLongs(final BufferedReader reader, final long[] array, final int offset, final int length) throws IOException {
        LongArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            String s;
            for (i = 0; i < length && (s = reader.readLine()) != null; ++i) {
                array[i + offset] = Long.parseLong(s.trim());
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadLongs(final BufferedReader reader, final long[] array) throws IOException {
        return loadLongs(reader, array, 0, array.length);
    }
    
    public static int loadLongs(final File file, final long[] array, final int offset, final int length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final int result = loadLongs(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static int loadLongs(final CharSequence filename, final long[] array, final int offset, final int length) throws IOException {
        return loadLongs(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadLongs(final File file, final long[] array) throws IOException {
        return loadLongs(file, array, 0, array.length);
    }
    
    public static int loadLongs(final CharSequence filename, final long[] array) throws IOException {
        return loadLongs(filename, array, 0, array.length);
    }
    
    public static void storeLongs(final long[] array, final int offset, final int length, final PrintStream stream) {
        LongArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            stream.println(array[offset + i]);
        }
    }
    
    public static void storeLongs(final long[] array, final PrintStream stream) {
        storeLongs(array, 0, array.length, stream);
    }
    
    public static void storeLongs(final long[] array, final int offset, final int length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeLongs(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeLongs(final long[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeLongs(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeLongs(final long[] array, final File file) throws IOException {
        storeLongs(array, 0, array.length, file);
    }
    
    public static void storeLongs(final long[] array, final CharSequence filename) throws IOException {
        storeLongs(array, 0, array.length, filename);
    }
    
    public static void storeLongs(final LongIterator i, final PrintStream stream) {
        while (i.hasNext()) {
            stream.println(i.nextLong());
        }
    }
    
    public static void storeLongs(final LongIterator i, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeLongs(i, stream);
        stream.close();
    }
    
    public static void storeLongs(final LongIterator i, final CharSequence filename) throws IOException {
        storeLongs(i, new File(filename.toString()));
    }
    
    public static long loadLongs(final BufferedReader reader, final long[][] array, final long offset, final long length) throws IOException {
        LongBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final long[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    final String s;
                    if ((s = reader.readLine()) == null) {
                        return c;
                    }
                    t[d] = Long.parseLong(s.trim());
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadLongs(final BufferedReader reader, final long[][] array) throws IOException {
        return loadLongs(reader, array, 0L, LongBigArrays.length(array));
    }
    
    public static long loadLongs(final File file, final long[][] array, final long offset, final long length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final long result = loadLongs(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static long loadLongs(final CharSequence filename, final long[][] array, final long offset, final long length) throws IOException {
        return loadLongs(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadLongs(final File file, final long[][] array) throws IOException {
        return loadLongs(file, array, 0L, LongBigArrays.length(array));
    }
    
    public static long loadLongs(final CharSequence filename, final long[][] array) throws IOException {
        return loadLongs(filename, array, 0L, LongBigArrays.length(array));
    }
    
    public static void storeLongs(final long[][] array, final long offset, final long length, final PrintStream stream) {
        LongBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final long[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                stream.println(t[d]);
            }
        }
    }
    
    public static void storeLongs(final long[][] array, final PrintStream stream) {
        storeLongs(array, 0L, LongBigArrays.length(array), stream);
    }
    
    public static void storeLongs(final long[][] array, final long offset, final long length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeLongs(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeLongs(final long[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeLongs(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeLongs(final long[][] array, final File file) throws IOException {
        storeLongs(array, 0L, LongBigArrays.length(array), file);
    }
    
    public static void storeLongs(final long[][] array, final CharSequence filename) throws IOException {
        storeLongs(array, 0L, LongBigArrays.length(array), filename);
    }
    
    public static LongIterator asLongIterator(final BufferedReader reader) {
        return new LongReaderWrapper(reader);
    }
    
    public static LongIterator asLongIterator(final File file) throws IOException {
        return new LongReaderWrapper(new BufferedReader(new FileReader(file)));
    }
    
    public static LongIterator asLongIterator(final CharSequence filename) throws IOException {
        return asLongIterator(new File(filename.toString()));
    }
    
    public static LongIterable asLongIterable(final File file) {
        return new LongIterable() {
            @Override
            public LongIterator iterator() {
                try {
                    return TextIO.asLongIterator(file);
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
                    return TextIO.asLongIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadFloats(final BufferedReader reader, final float[] array, final int offset, final int length) throws IOException {
        FloatArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            String s;
            for (i = 0; i < length && (s = reader.readLine()) != null; ++i) {
                array[i + offset] = Float.parseFloat(s.trim());
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadFloats(final BufferedReader reader, final float[] array) throws IOException {
        return loadFloats(reader, array, 0, array.length);
    }
    
    public static int loadFloats(final File file, final float[] array, final int offset, final int length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final int result = loadFloats(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static int loadFloats(final CharSequence filename, final float[] array, final int offset, final int length) throws IOException {
        return loadFloats(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadFloats(final File file, final float[] array) throws IOException {
        return loadFloats(file, array, 0, array.length);
    }
    
    public static int loadFloats(final CharSequence filename, final float[] array) throws IOException {
        return loadFloats(filename, array, 0, array.length);
    }
    
    public static void storeFloats(final float[] array, final int offset, final int length, final PrintStream stream) {
        FloatArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            stream.println(array[offset + i]);
        }
    }
    
    public static void storeFloats(final float[] array, final PrintStream stream) {
        storeFloats(array, 0, array.length, stream);
    }
    
    public static void storeFloats(final float[] array, final int offset, final int length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeFloats(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeFloats(final float[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeFloats(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeFloats(final float[] array, final File file) throws IOException {
        storeFloats(array, 0, array.length, file);
    }
    
    public static void storeFloats(final float[] array, final CharSequence filename) throws IOException {
        storeFloats(array, 0, array.length, filename);
    }
    
    public static void storeFloats(final FloatIterator i, final PrintStream stream) {
        while (i.hasNext()) {
            stream.println(i.nextFloat());
        }
    }
    
    public static void storeFloats(final FloatIterator i, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeFloats(i, stream);
        stream.close();
    }
    
    public static void storeFloats(final FloatIterator i, final CharSequence filename) throws IOException {
        storeFloats(i, new File(filename.toString()));
    }
    
    public static long loadFloats(final BufferedReader reader, final float[][] array, final long offset, final long length) throws IOException {
        FloatBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final float[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    final String s;
                    if ((s = reader.readLine()) == null) {
                        return c;
                    }
                    t[d] = Float.parseFloat(s.trim());
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadFloats(final BufferedReader reader, final float[][] array) throws IOException {
        return loadFloats(reader, array, 0L, FloatBigArrays.length(array));
    }
    
    public static long loadFloats(final File file, final float[][] array, final long offset, final long length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final long result = loadFloats(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static long loadFloats(final CharSequence filename, final float[][] array, final long offset, final long length) throws IOException {
        return loadFloats(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadFloats(final File file, final float[][] array) throws IOException {
        return loadFloats(file, array, 0L, FloatBigArrays.length(array));
    }
    
    public static long loadFloats(final CharSequence filename, final float[][] array) throws IOException {
        return loadFloats(filename, array, 0L, FloatBigArrays.length(array));
    }
    
    public static void storeFloats(final float[][] array, final long offset, final long length, final PrintStream stream) {
        FloatBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final float[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                stream.println(t[d]);
            }
        }
    }
    
    public static void storeFloats(final float[][] array, final PrintStream stream) {
        storeFloats(array, 0L, FloatBigArrays.length(array), stream);
    }
    
    public static void storeFloats(final float[][] array, final long offset, final long length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeFloats(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeFloats(final float[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeFloats(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeFloats(final float[][] array, final File file) throws IOException {
        storeFloats(array, 0L, FloatBigArrays.length(array), file);
    }
    
    public static void storeFloats(final float[][] array, final CharSequence filename) throws IOException {
        storeFloats(array, 0L, FloatBigArrays.length(array), filename);
    }
    
    public static FloatIterator asFloatIterator(final BufferedReader reader) {
        return new FloatReaderWrapper(reader);
    }
    
    public static FloatIterator asFloatIterator(final File file) throws IOException {
        return new FloatReaderWrapper(new BufferedReader(new FileReader(file)));
    }
    
    public static FloatIterator asFloatIterator(final CharSequence filename) throws IOException {
        return asFloatIterator(new File(filename.toString()));
    }
    
    public static FloatIterable asFloatIterable(final File file) {
        return new FloatIterable() {
            @Override
            public FloatIterator iterator() {
                try {
                    return TextIO.asFloatIterator(file);
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
                    return TextIO.asFloatIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    public static int loadDoubles(final BufferedReader reader, final double[] array, final int offset, final int length) throws IOException {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        int i = 0;
        try {
            String s;
            for (i = 0; i < length && (s = reader.readLine()) != null; ++i) {
                array[i + offset] = Double.parseDouble(s.trim());
            }
        }
        catch (final EOFException ex) {}
        return i;
    }
    
    public static int loadDoubles(final BufferedReader reader, final double[] array) throws IOException {
        return loadDoubles(reader, array, 0, array.length);
    }
    
    public static int loadDoubles(final File file, final double[] array, final int offset, final int length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final int result = loadDoubles(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static int loadDoubles(final CharSequence filename, final double[] array, final int offset, final int length) throws IOException {
        return loadDoubles(new File(filename.toString()), array, offset, length);
    }
    
    public static int loadDoubles(final File file, final double[] array) throws IOException {
        return loadDoubles(file, array, 0, array.length);
    }
    
    public static int loadDoubles(final CharSequence filename, final double[] array) throws IOException {
        return loadDoubles(filename, array, 0, array.length);
    }
    
    public static void storeDoubles(final double[] array, final int offset, final int length, final PrintStream stream) {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        for (int i = 0; i < length; ++i) {
            stream.println(array[offset + i]);
        }
    }
    
    public static void storeDoubles(final double[] array, final PrintStream stream) {
        storeDoubles(array, 0, array.length, stream);
    }
    
    public static void storeDoubles(final double[] array, final int offset, final int length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeDoubles(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeDoubles(final double[] array, final int offset, final int length, final CharSequence filename) throws IOException {
        storeDoubles(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeDoubles(final double[] array, final File file) throws IOException {
        storeDoubles(array, 0, array.length, file);
    }
    
    public static void storeDoubles(final double[] array, final CharSequence filename) throws IOException {
        storeDoubles(array, 0, array.length, filename);
    }
    
    public static void storeDoubles(final DoubleIterator i, final PrintStream stream) {
        while (i.hasNext()) {
            stream.println(i.nextDouble());
        }
    }
    
    public static void storeDoubles(final DoubleIterator i, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeDoubles(i, stream);
        stream.close();
    }
    
    public static void storeDoubles(final DoubleIterator i, final CharSequence filename) throws IOException {
        storeDoubles(i, new File(filename.toString()));
    }
    
    public static long loadDoubles(final BufferedReader reader, final double[][] array, final long offset, final long length) throws IOException {
        DoubleBigArrays.ensureOffsetLength(array, offset, length);
        long c = 0L;
        try {
            for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
                final double[] t = array[i];
                for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                    final String s;
                    if ((s = reader.readLine()) == null) {
                        return c;
                    }
                    t[d] = Double.parseDouble(s.trim());
                    ++c;
                }
            }
        }
        catch (final EOFException ex) {}
        return c;
    }
    
    public static long loadDoubles(final BufferedReader reader, final double[][] array) throws IOException {
        return loadDoubles(reader, array, 0L, DoubleBigArrays.length(array));
    }
    
    public static long loadDoubles(final File file, final double[][] array, final long offset, final long length) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final long result = loadDoubles(reader, array, offset, length);
        reader.close();
        return result;
    }
    
    public static long loadDoubles(final CharSequence filename, final double[][] array, final long offset, final long length) throws IOException {
        return loadDoubles(new File(filename.toString()), array, offset, length);
    }
    
    public static long loadDoubles(final File file, final double[][] array) throws IOException {
        return loadDoubles(file, array, 0L, DoubleBigArrays.length(array));
    }
    
    public static long loadDoubles(final CharSequence filename, final double[][] array) throws IOException {
        return loadDoubles(filename, array, 0L, DoubleBigArrays.length(array));
    }
    
    public static void storeDoubles(final double[][] array, final long offset, final long length, final PrintStream stream) {
        DoubleBigArrays.ensureOffsetLength(array, offset, length);
        for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); ++i) {
            final double[] t = array[i];
            for (int l = (int)Math.min(t.length, offset + length - BigArrays.start(i)), d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ++d) {
                stream.println(t[d]);
            }
        }
    }
    
    public static void storeDoubles(final double[][] array, final PrintStream stream) {
        storeDoubles(array, 0L, DoubleBigArrays.length(array), stream);
    }
    
    public static void storeDoubles(final double[][] array, final long offset, final long length, final File file) throws IOException {
        final PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
        storeDoubles(array, offset, length, stream);
        stream.close();
    }
    
    public static void storeDoubles(final double[][] array, final long offset, final long length, final CharSequence filename) throws IOException {
        storeDoubles(array, offset, length, new File(filename.toString()));
    }
    
    public static void storeDoubles(final double[][] array, final File file) throws IOException {
        storeDoubles(array, 0L, DoubleBigArrays.length(array), file);
    }
    
    public static void storeDoubles(final double[][] array, final CharSequence filename) throws IOException {
        storeDoubles(array, 0L, DoubleBigArrays.length(array), filename);
    }
    
    public static DoubleIterator asDoubleIterator(final BufferedReader reader) {
        return new DoubleReaderWrapper(reader);
    }
    
    public static DoubleIterator asDoubleIterator(final File file) throws IOException {
        return new DoubleReaderWrapper(new BufferedReader(new FileReader(file)));
    }
    
    public static DoubleIterator asDoubleIterator(final CharSequence filename) throws IOException {
        return asDoubleIterator(new File(filename.toString()));
    }
    
    public static DoubleIterable asDoubleIterable(final File file) {
        return new DoubleIterable() {
            @Override
            public DoubleIterator iterator() {
                try {
                    return TextIO.asDoubleIterator(file);
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
                    return TextIO.asDoubleIterator(filename);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    private static final class BooleanReaderWrapper extends AbstractBooleanIterator
    {
        private final BufferedReader reader;
        private boolean toAdvance;
        private String s;
        private boolean next;
        
        public BooleanReaderWrapper(final BufferedReader reader) {
            this.toAdvance = true;
            this.reader = reader;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return this.s != null;
            }
            this.toAdvance = false;
            try {
                this.s = this.reader.readLine();
            }
            catch (final EOFException ex) {}
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            if (this.s == null) {
                return false;
            }
            this.next = Boolean.parseBoolean(this.s.trim());
            return true;
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
    
    private static final class ByteReaderWrapper extends AbstractByteIterator
    {
        private final BufferedReader reader;
        private boolean toAdvance;
        private String s;
        private byte next;
        
        public ByteReaderWrapper(final BufferedReader reader) {
            this.toAdvance = true;
            this.reader = reader;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return this.s != null;
            }
            this.toAdvance = false;
            try {
                this.s = this.reader.readLine();
            }
            catch (final EOFException ex) {}
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            if (this.s == null) {
                return false;
            }
            this.next = Byte.parseByte(this.s.trim());
            return true;
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
    
    private static final class ShortReaderWrapper extends AbstractShortIterator
    {
        private final BufferedReader reader;
        private boolean toAdvance;
        private String s;
        private short next;
        
        public ShortReaderWrapper(final BufferedReader reader) {
            this.toAdvance = true;
            this.reader = reader;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return this.s != null;
            }
            this.toAdvance = false;
            try {
                this.s = this.reader.readLine();
            }
            catch (final EOFException ex) {}
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            if (this.s == null) {
                return false;
            }
            this.next = Short.parseShort(this.s.trim());
            return true;
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
    
    private static final class IntReaderWrapper extends AbstractIntIterator
    {
        private final BufferedReader reader;
        private boolean toAdvance;
        private String s;
        private int next;
        
        public IntReaderWrapper(final BufferedReader reader) {
            this.toAdvance = true;
            this.reader = reader;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return this.s != null;
            }
            this.toAdvance = false;
            try {
                this.s = this.reader.readLine();
            }
            catch (final EOFException ex) {}
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            if (this.s == null) {
                return false;
            }
            this.next = Integer.parseInt(this.s.trim());
            return true;
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
    
    private static final class LongReaderWrapper extends AbstractLongIterator
    {
        private final BufferedReader reader;
        private boolean toAdvance;
        private String s;
        private long next;
        
        public LongReaderWrapper(final BufferedReader reader) {
            this.toAdvance = true;
            this.reader = reader;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return this.s != null;
            }
            this.toAdvance = false;
            try {
                this.s = this.reader.readLine();
            }
            catch (final EOFException ex) {}
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            if (this.s == null) {
                return false;
            }
            this.next = Long.parseLong(this.s.trim());
            return true;
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
    
    private static final class FloatReaderWrapper extends AbstractFloatIterator
    {
        private final BufferedReader reader;
        private boolean toAdvance;
        private String s;
        private float next;
        
        public FloatReaderWrapper(final BufferedReader reader) {
            this.toAdvance = true;
            this.reader = reader;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return this.s != null;
            }
            this.toAdvance = false;
            try {
                this.s = this.reader.readLine();
            }
            catch (final EOFException ex) {}
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            if (this.s == null) {
                return false;
            }
            this.next = Float.parseFloat(this.s.trim());
            return true;
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
    
    private static final class DoubleReaderWrapper extends AbstractDoubleIterator
    {
        private final BufferedReader reader;
        private boolean toAdvance;
        private String s;
        private double next;
        
        public DoubleReaderWrapper(final BufferedReader reader) {
            this.toAdvance = true;
            this.reader = reader;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.toAdvance) {
                return this.s != null;
            }
            this.toAdvance = false;
            try {
                this.s = this.reader.readLine();
            }
            catch (final EOFException ex) {}
            catch (final IOException rethrow) {
                throw new RuntimeException(rethrow);
            }
            if (this.s == null) {
                return false;
            }
            this.next = Double.parseDouble(this.s.trim());
            return true;
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
