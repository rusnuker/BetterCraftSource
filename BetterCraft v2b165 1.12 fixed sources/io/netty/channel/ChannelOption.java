// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.Constant;
import java.net.NetworkInterface;
import java.net.InetAddress;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ConstantPool;
import io.netty.util.AbstractConstant;

public class ChannelOption<T> extends AbstractConstant<ChannelOption<T>>
{
    private static final ConstantPool<ChannelOption<Object>> pool;
    public static final ChannelOption<ByteBufAllocator> ALLOCATOR;
    public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR;
    public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR;
    public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS;
    @Deprecated
    public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ;
    public static final ChannelOption<Integer> WRITE_SPIN_COUNT;
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK;
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK;
    public static final ChannelOption<WriteBufferWaterMark> WRITE_BUFFER_WATER_MARK;
    public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE;
    public static final ChannelOption<Boolean> AUTO_READ;
    @Deprecated
    public static final ChannelOption<Boolean> AUTO_CLOSE;
    public static final ChannelOption<Boolean> SO_BROADCAST;
    public static final ChannelOption<Boolean> SO_KEEPALIVE;
    public static final ChannelOption<Integer> SO_SNDBUF;
    public static final ChannelOption<Integer> SO_RCVBUF;
    public static final ChannelOption<Boolean> SO_REUSEADDR;
    public static final ChannelOption<Integer> SO_LINGER;
    public static final ChannelOption<Integer> SO_BACKLOG;
    public static final ChannelOption<Integer> SO_TIMEOUT;
    public static final ChannelOption<Integer> IP_TOS;
    public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR;
    public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF;
    public static final ChannelOption<Integer> IP_MULTICAST_TTL;
    public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED;
    public static final ChannelOption<Boolean> TCP_NODELAY;
    @Deprecated
    public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION;
    public static final ChannelOption<Boolean> SINGLE_EVENTEXECUTOR_PER_GROUP;
    
    public static <T> ChannelOption<T> valueOf(final String name) {
        return (ChannelOption)ChannelOption.pool.valueOf(name);
    }
    
    public static <T> ChannelOption<T> valueOf(final Class<?> firstNameComponent, final String secondNameComponent) {
        return (ChannelOption)ChannelOption.pool.valueOf(firstNameComponent, secondNameComponent);
    }
    
    public static boolean exists(final String name) {
        return ChannelOption.pool.exists(name);
    }
    
    public static <T> ChannelOption<T> newInstance(final String name) {
        return (ChannelOption)ChannelOption.pool.newInstance(name);
    }
    
    private ChannelOption(final int id, final String name) {
        super(id, name);
    }
    
    @Deprecated
    protected ChannelOption(final String name) {
        this(ChannelOption.pool.nextId(), name);
    }
    
    public void validate(final T value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
    }
    
    static {
        pool = new ConstantPool<ChannelOption<Object>>() {
            @Override
            protected ChannelOption<Object> newConstant(final int id, final String name) {
                return new ChannelOption<Object>(id, name, null);
            }
        };
        ALLOCATOR = valueOf("ALLOCATOR");
        RCVBUF_ALLOCATOR = valueOf("RCVBUF_ALLOCATOR");
        MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");
        CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");
        MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
        WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");
        WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
        WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");
        WRITE_BUFFER_WATER_MARK = valueOf("WRITE_BUFFER_WATER_MARK");
        ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
        AUTO_READ = valueOf("AUTO_READ");
        AUTO_CLOSE = valueOf("AUTO_CLOSE");
        SO_BROADCAST = valueOf("SO_BROADCAST");
        SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
        SO_SNDBUF = valueOf("SO_SNDBUF");
        SO_RCVBUF = valueOf("SO_RCVBUF");
        SO_REUSEADDR = valueOf("SO_REUSEADDR");
        SO_LINGER = valueOf("SO_LINGER");
        SO_BACKLOG = valueOf("SO_BACKLOG");
        SO_TIMEOUT = valueOf("SO_TIMEOUT");
        IP_TOS = valueOf("IP_TOS");
        IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
        IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
        IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
        IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");
        TCP_NODELAY = valueOf("TCP_NODELAY");
        DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION = valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");
        SINGLE_EVENTEXECUTOR_PER_GROUP = valueOf("SINGLE_EVENTEXECUTOR_PER_GROUP");
    }
}
