// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

public interface ByteProcessor
{
    public static final ByteProcessor FIND_NUL = new IndexOfProcessor((byte)0);
    public static final ByteProcessor FIND_NON_NUL = new IndexNotOfProcessor((byte)0);
    public static final ByteProcessor FIND_CR = new IndexOfProcessor((byte)13);
    public static final ByteProcessor FIND_NON_CR = new IndexNotOfProcessor((byte)13);
    public static final ByteProcessor FIND_LF = new IndexOfProcessor((byte)10);
    public static final ByteProcessor FIND_NON_LF = new IndexNotOfProcessor((byte)10);
    public static final ByteProcessor FIND_SEMI_COLON = new IndexOfProcessor((byte)59);
    public static final ByteProcessor FIND_CRLF = new ByteProcessor() {
        @Override
        public boolean process(final byte value) {
            return value != 13 && value != 10;
        }
    };
    public static final ByteProcessor FIND_NON_CRLF = new ByteProcessor() {
        @Override
        public boolean process(final byte value) {
            return value == 13 || value == 10;
        }
    };
    public static final ByteProcessor FIND_LINEAR_WHITESPACE = new ByteProcessor() {
        @Override
        public boolean process(final byte value) {
            return value != 32 && value != 9;
        }
    };
    public static final ByteProcessor FIND_NON_LINEAR_WHITESPACE = new ByteProcessor() {
        @Override
        public boolean process(final byte value) {
            return value == 32 || value == 9;
        }
    };
    
    boolean process(final byte p0) throws Exception;
    
    public static class IndexOfProcessor implements ByteProcessor
    {
        private final byte byteToFind;
        
        public IndexOfProcessor(final byte byteToFind) {
            this.byteToFind = byteToFind;
        }
        
        @Override
        public boolean process(final byte value) {
            return value != this.byteToFind;
        }
    }
    
    public static class IndexNotOfProcessor implements ByteProcessor
    {
        private final byte byteToNotFind;
        
        public IndexNotOfProcessor(final byte byteToNotFind) {
            this.byteToNotFind = byteToNotFind;
        }
        
        @Override
        public boolean process(final byte value) {
            return value == this.byteToNotFind;
        }
    }
}
