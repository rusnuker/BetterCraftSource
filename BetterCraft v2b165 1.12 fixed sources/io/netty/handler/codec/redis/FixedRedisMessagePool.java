// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.collection.LongObjectHashMap;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import java.util.HashMap;
import io.netty.util.collection.LongObjectMap;
import io.netty.buffer.ByteBuf;
import java.util.Map;

public final class FixedRedisMessagePool implements RedisMessagePool
{
    private static final String[] DEFAULT_SIMPLE_STRINGS;
    private static final String[] DEFAULT_ERRORS;
    private static final long MIN_CACHED_INTEGER_NUMBER = -1L;
    private static final long MAX_CACHED_INTEGER_NUMBER = 128L;
    private static final int SIZE_CACHED_INTEGER_NUMBER = 129;
    public static final FixedRedisMessagePool INSTANCE;
    private final Map<ByteBuf, SimpleStringRedisMessage> byteBufToSimpleStrings;
    private final Map<String, SimpleStringRedisMessage> stringToSimpleStrings;
    private final Map<ByteBuf, ErrorRedisMessage> byteBufToErrors;
    private final Map<String, ErrorRedisMessage> stringToErrors;
    private final Map<ByteBuf, IntegerRedisMessage> byteBufToIntegers;
    private final LongObjectMap<IntegerRedisMessage> longToIntegers;
    private final LongObjectMap<byte[]> longToByteBufs;
    
    private FixedRedisMessagePool() {
        this.byteBufToSimpleStrings = new HashMap<ByteBuf, SimpleStringRedisMessage>(FixedRedisMessagePool.DEFAULT_SIMPLE_STRINGS.length, 1.0f);
        this.stringToSimpleStrings = new HashMap<String, SimpleStringRedisMessage>(FixedRedisMessagePool.DEFAULT_SIMPLE_STRINGS.length, 1.0f);
        for (final String message : FixedRedisMessagePool.DEFAULT_SIMPLE_STRINGS) {
            final ByteBuf key = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(message.getBytes(CharsetUtil.UTF_8))));
            final SimpleStringRedisMessage cached = new SimpleStringRedisMessage(message);
            this.byteBufToSimpleStrings.put(key, cached);
            this.stringToSimpleStrings.put(message, cached);
        }
        this.byteBufToErrors = new HashMap<ByteBuf, ErrorRedisMessage>(FixedRedisMessagePool.DEFAULT_ERRORS.length, 1.0f);
        this.stringToErrors = new HashMap<String, ErrorRedisMessage>(FixedRedisMessagePool.DEFAULT_ERRORS.length, 1.0f);
        for (final String message : FixedRedisMessagePool.DEFAULT_ERRORS) {
            final ByteBuf key = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(message.getBytes(CharsetUtil.UTF_8))));
            final ErrorRedisMessage cached2 = new ErrorRedisMessage(message);
            this.byteBufToErrors.put(key, cached2);
            this.stringToErrors.put(message, cached2);
        }
        this.byteBufToIntegers = new HashMap<ByteBuf, IntegerRedisMessage>(129, 1.0f);
        this.longToIntegers = new LongObjectHashMap<IntegerRedisMessage>(129, 1.0f);
        this.longToByteBufs = new LongObjectHashMap<byte[]>(129, 1.0f);
        for (long value = -1L; value < 128L; ++value) {
            final byte[] keyBytes = RedisCodecUtil.longToAsciiBytes(value);
            final ByteBuf keyByteBuf = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(keyBytes)));
            final IntegerRedisMessage cached3 = new IntegerRedisMessage(value);
            this.byteBufToIntegers.put(keyByteBuf, cached3);
            this.longToIntegers.put(value, cached3);
            this.longToByteBufs.put(value, keyBytes);
        }
    }
    
    @Override
    public SimpleStringRedisMessage getSimpleString(final String content) {
        return this.stringToSimpleStrings.get(content);
    }
    
    @Override
    public SimpleStringRedisMessage getSimpleString(final ByteBuf content) {
        return this.byteBufToSimpleStrings.get(content);
    }
    
    @Override
    public ErrorRedisMessage getError(final String content) {
        return this.stringToErrors.get(content);
    }
    
    @Override
    public ErrorRedisMessage getError(final ByteBuf content) {
        return this.byteBufToErrors.get(content);
    }
    
    @Override
    public IntegerRedisMessage getInteger(final long value) {
        return this.longToIntegers.get(value);
    }
    
    @Override
    public IntegerRedisMessage getInteger(final ByteBuf content) {
        return this.byteBufToIntegers.get(content);
    }
    
    @Override
    public byte[] getByteBufOfInteger(final long value) {
        return this.longToByteBufs.get(value);
    }
    
    static {
        DEFAULT_SIMPLE_STRINGS = new String[] { "OK", "PONG", "QUEUED" };
        DEFAULT_ERRORS = new String[] { "ERR", "ERR index out of range", "ERR no such key", "ERR source and destination objects are the same", "ERR syntax error", "BUSY Redis is busy running a script. You can only call SCRIPT KILL or SHUTDOWN NOSAVE.", "BUSYKEY Target key name already exists.", "EXECABORT Transaction discarded because of previous errors.", "LOADING Redis is loading the dataset in memory", "MASTERDOWN Link with MASTER is down and slave-serve-stale-data is set to 'no'.", "MISCONF Redis is configured to save RDB snapshots, but is currently not able to persist on disk. Commands that may modify the data set are disabled. Please check Redis logs for details about the error.", "NOAUTH Authentication required.", "NOREPLICAS Not enough good slaves to write.", "NOSCRIPT No matching script. Please use EVAL.", "OOM command not allowed when used memory > 'maxmemory'.", "READONLY You can't write against a read only slave.", "WRONGTYPE Operation against a key holding the wrong kind of value" };
        INSTANCE = new FixedRedisMessagePool();
    }
}
