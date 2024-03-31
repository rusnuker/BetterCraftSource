// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.nio.ByteBuffer;
import io.netty.util.AsciiString;

public final class EmptyArrays
{
    public static final byte[] EMPTY_BYTES;
    public static final char[] EMPTY_CHARS;
    public static final Object[] EMPTY_OBJECTS;
    public static final Class<?>[] EMPTY_CLASSES;
    public static final String[] EMPTY_STRINGS;
    public static final AsciiString[] EMPTY_ASCII_STRINGS;
    public static final StackTraceElement[] EMPTY_STACK_TRACE;
    public static final ByteBuffer[] EMPTY_BYTE_BUFFERS;
    public static final Certificate[] EMPTY_CERTIFICATES;
    public static final X509Certificate[] EMPTY_X509_CERTIFICATES;
    public static final javax.security.cert.X509Certificate[] EMPTY_JAVAX_X509_CERTIFICATES;
    
    private EmptyArrays() {
    }
    
    static {
        EMPTY_BYTES = new byte[0];
        EMPTY_CHARS = new char[0];
        EMPTY_OBJECTS = new Object[0];
        EMPTY_CLASSES = new Class[0];
        EMPTY_STRINGS = new String[0];
        EMPTY_ASCII_STRINGS = new AsciiString[0];
        EMPTY_STACK_TRACE = new StackTraceElement[0];
        EMPTY_BYTE_BUFFERS = new ByteBuffer[0];
        EMPTY_CERTIFICATES = new Certificate[0];
        EMPTY_X509_CERTIFICATES = new X509Certificate[0];
        EMPTY_JAVAX_X509_CERTIFICATES = new javax.security.cert.X509Certificate[0];
    }
}
