// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import java.util.Map;
import io.netty.util.collection.CharObjectHashMap;

public final class Http2Settings extends CharObjectHashMap<Long>
{
    private static final int DEFAULT_CAPACITY = 13;
    private static final Long FALSE;
    private static final Long TRUE;
    
    public Http2Settings() {
        this(13);
    }
    
    public Http2Settings(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public Http2Settings(final int initialCapacity) {
        super(initialCapacity);
    }
    
    @Override
    public Long put(final char key, final Long value) {
        verifyStandardSetting(key, value);
        return super.put(key, value);
    }
    
    public Long headerTableSize() {
        return this.get('\u0001');
    }
    
    public Http2Settings headerTableSize(final long value) {
        this.put('\u0001', value);
        return this;
    }
    
    public Boolean pushEnabled() {
        final Long value = this.get('\u0002');
        if (value == null) {
            return null;
        }
        return Http2Settings.TRUE.equals(value);
    }
    
    public Http2Settings pushEnabled(final boolean enabled) {
        this.put('\u0002', enabled ? Http2Settings.TRUE : Http2Settings.FALSE);
        return this;
    }
    
    public Long maxConcurrentStreams() {
        return this.get('\u0003');
    }
    
    public Http2Settings maxConcurrentStreams(final long value) {
        this.put('\u0003', value);
        return this;
    }
    
    public Integer initialWindowSize() {
        return this.getIntValue('\u0004');
    }
    
    public Http2Settings initialWindowSize(final int value) {
        this.put('\u0004', (long)value);
        return this;
    }
    
    public Integer maxFrameSize() {
        return this.getIntValue('\u0005');
    }
    
    public Http2Settings maxFrameSize(final int value) {
        this.put('\u0005', (long)value);
        return this;
    }
    
    public Long maxHeaderListSize() {
        return this.get('\u0006');
    }
    
    public Http2Settings maxHeaderListSize(final long value) {
        this.put('\u0006', value);
        return this;
    }
    
    public Http2Settings copyFrom(final Http2Settings settings) {
        this.clear();
        this.putAll(settings);
        return this;
    }
    
    public Integer getIntValue(final char key) {
        final Long value = this.get(key);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }
    
    private static void verifyStandardSetting(final int key, final Long value) {
        ObjectUtil.checkNotNull(value, "value");
        switch (key) {
            case 1: {
                if (value < 0L || value > 4294967295L) {
                    throw new IllegalArgumentException("Setting HEADER_TABLE_SIZE is invalid: " + value);
                }
                break;
            }
            case 2: {
                if (value != 0L && value != 1L) {
                    throw new IllegalArgumentException("Setting ENABLE_PUSH is invalid: " + value);
                }
                break;
            }
            case 3: {
                if (value < 0L || value > 4294967295L) {
                    throw new IllegalArgumentException("Setting MAX_CONCURRENT_STREAMS is invalid: " + value);
                }
                break;
            }
            case 4: {
                if (value < 0L || value > 2147483647L) {
                    throw new IllegalArgumentException("Setting INITIAL_WINDOW_SIZE is invalid: " + value);
                }
                break;
            }
            case 5: {
                if (!Http2CodecUtil.isMaxFrameSizeValid(value.intValue())) {
                    throw new IllegalArgumentException("Setting MAX_FRAME_SIZE is invalid: " + value);
                }
                break;
            }
            case 6: {
                if (value < 0L || value > 4294967295L) {
                    throw new IllegalArgumentException("Setting MAX_HEADER_LIST_SIZE is invalid: " + value);
                }
                break;
            }
        }
    }
    
    @Override
    protected String keyToString(final char key) {
        switch (key) {
            case '\u0001': {
                return "HEADER_TABLE_SIZE";
            }
            case '\u0002': {
                return "ENABLE_PUSH";
            }
            case '\u0003': {
                return "MAX_CONCURRENT_STREAMS";
            }
            case '\u0004': {
                return "INITIAL_WINDOW_SIZE";
            }
            case '\u0005': {
                return "MAX_FRAME_SIZE";
            }
            case '\u0006': {
                return "MAX_HEADER_LIST_SIZE";
            }
            default: {
                return super.keyToString(key);
            }
        }
    }
    
    static {
        FALSE = 0L;
        TRUE = 1L;
    }
}
