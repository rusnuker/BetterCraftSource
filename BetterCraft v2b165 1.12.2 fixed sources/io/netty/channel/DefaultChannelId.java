// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.internal.MacAddressUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufUtil;
import java.util.Arrays;
import java.lang.reflect.Method;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.util.internal.logging.InternalLogger;

public final class DefaultChannelId implements ChannelId
{
    private static final long serialVersionUID = 3884076183504074063L;
    private static final InternalLogger logger;
    private static final byte[] MACHINE_ID;
    private static final int PROCESS_ID_LEN = 4;
    private static final int PROCESS_ID;
    private static final int SEQUENCE_LEN = 4;
    private static final int TIMESTAMP_LEN = 8;
    private static final int RANDOM_LEN = 4;
    private static final AtomicInteger nextSequence;
    private final byte[] data;
    private final int hashCode;
    private transient String shortValue;
    private transient String longValue;
    
    public static DefaultChannelId newInstance() {
        return new DefaultChannelId();
    }
    
    private static int defaultProcessId() {
        ClassLoader loader = null;
        String value;
        try {
            loader = PlatformDependent.getClassLoader(DefaultChannelId.class);
            final Class<?> mgmtFactoryType = Class.forName("java.lang.management.ManagementFactory", true, loader);
            final Class<?> runtimeMxBeanType = Class.forName("java.lang.management.RuntimeMXBean", true, loader);
            final Method getRuntimeMXBean = mgmtFactoryType.getMethod("getRuntimeMXBean", EmptyArrays.EMPTY_CLASSES);
            final Object bean = getRuntimeMXBean.invoke(null, EmptyArrays.EMPTY_OBJECTS);
            final Method getName = runtimeMxBeanType.getMethod("getName", EmptyArrays.EMPTY_CLASSES);
            value = (String)getName.invoke(bean, EmptyArrays.EMPTY_OBJECTS);
        }
        catch (final Throwable t) {
            DefaultChannelId.logger.debug("Could not invoke ManagementFactory.getRuntimeMXBean().getName(); Android?", t);
            try {
                final Class<?> processType = Class.forName("android.os.Process", true, loader);
                final Method myPid = processType.getMethod("myPid", EmptyArrays.EMPTY_CLASSES);
                value = myPid.invoke(null, EmptyArrays.EMPTY_OBJECTS).toString();
            }
            catch (final Throwable t2) {
                DefaultChannelId.logger.debug("Could not invoke Process.myPid(); not Android?", t2);
                value = "";
            }
        }
        final int atIndex = value.indexOf(64);
        if (atIndex >= 0) {
            value = value.substring(0, atIndex);
        }
        int pid;
        try {
            pid = Integer.parseInt(value);
        }
        catch (final NumberFormatException e) {
            pid = -1;
        }
        if (pid < 0) {
            pid = PlatformDependent.threadLocalRandom().nextInt();
            DefaultChannelId.logger.warn("Failed to find the current process ID from '{}'; using a random value: {}", value, pid);
        }
        return pid;
    }
    
    private DefaultChannelId() {
        this.data = new byte[DefaultChannelId.MACHINE_ID.length + 4 + 4 + 8 + 4];
        int i = 0;
        System.arraycopy(DefaultChannelId.MACHINE_ID, 0, this.data, i, DefaultChannelId.MACHINE_ID.length);
        i += DefaultChannelId.MACHINE_ID.length;
        i = this.writeInt(i, DefaultChannelId.PROCESS_ID);
        i = this.writeInt(i, DefaultChannelId.nextSequence.getAndIncrement());
        i = this.writeLong(i, Long.reverse(System.nanoTime()) ^ System.currentTimeMillis());
        final int random = PlatformDependent.threadLocalRandom().nextInt();
        i = this.writeInt(i, random);
        assert i == this.data.length;
        this.hashCode = Arrays.hashCode(this.data);
    }
    
    private int writeInt(int i, final int value) {
        this.data[i++] = (byte)(value >>> 24);
        this.data[i++] = (byte)(value >>> 16);
        this.data[i++] = (byte)(value >>> 8);
        this.data[i++] = (byte)value;
        return i;
    }
    
    private int writeLong(int i, final long value) {
        this.data[i++] = (byte)(value >>> 56);
        this.data[i++] = (byte)(value >>> 48);
        this.data[i++] = (byte)(value >>> 40);
        this.data[i++] = (byte)(value >>> 32);
        this.data[i++] = (byte)(value >>> 24);
        this.data[i++] = (byte)(value >>> 16);
        this.data[i++] = (byte)(value >>> 8);
        this.data[i++] = (byte)value;
        return i;
    }
    
    @Override
    public String asShortText() {
        String shortValue = this.shortValue;
        if (shortValue == null) {
            shortValue = (this.shortValue = ByteBufUtil.hexDump(this.data, this.data.length - 4, 4));
        }
        return shortValue;
    }
    
    @Override
    public String asLongText() {
        String longValue = this.longValue;
        if (longValue == null) {
            longValue = (this.longValue = this.newLongValue());
        }
        return longValue;
    }
    
    private String newLongValue() {
        final StringBuilder buf = new StringBuilder(2 * this.data.length + 5);
        int i = 0;
        i = this.appendHexDumpField(buf, i, DefaultChannelId.MACHINE_ID.length);
        i = this.appendHexDumpField(buf, i, 4);
        i = this.appendHexDumpField(buf, i, 4);
        i = this.appendHexDumpField(buf, i, 8);
        i = this.appendHexDumpField(buf, i, 4);
        assert i == this.data.length;
        return buf.substring(0, buf.length() - 1);
    }
    
    private int appendHexDumpField(final StringBuilder buf, int i, final int length) {
        buf.append(ByteBufUtil.hexDump(this.data, i, length));
        buf.append('-');
        i += length;
        return i;
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    @Override
    public int compareTo(final ChannelId o) {
        if (this == o) {
            return 0;
        }
        if (o instanceof DefaultChannelId) {
            final byte[] otherData = ((DefaultChannelId)o).data;
            final int len1 = this.data.length;
            final int len2 = otherData.length;
            for (int len3 = Math.min(len1, len2), k = 0; k < len3; ++k) {
                final byte x = this.data[k];
                final byte y = otherData[k];
                if (x != y) {
                    return (x & 0xFF) - (y & 0xFF);
                }
            }
            return len1 - len2;
        }
        return this.asLongText().compareTo(o.asLongText());
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof DefaultChannelId && Arrays.equals(this.data, ((DefaultChannelId)obj).data));
    }
    
    @Override
    public String toString() {
        return this.asShortText();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultChannelId.class);
        nextSequence = new AtomicInteger();
        int processId = -1;
        final String customProcessId = SystemPropertyUtil.get("io.netty.processId");
        if (customProcessId != null) {
            try {
                processId = Integer.parseInt(customProcessId);
            }
            catch (final NumberFormatException ex) {}
            if (processId < 0) {
                processId = -1;
                DefaultChannelId.logger.warn("-Dio.netty.processId: {} (malformed)", customProcessId);
            }
            else if (DefaultChannelId.logger.isDebugEnabled()) {
                DefaultChannelId.logger.debug("-Dio.netty.processId: {} (user-set)", (Object)processId);
            }
        }
        if (processId < 0) {
            processId = defaultProcessId();
            if (DefaultChannelId.logger.isDebugEnabled()) {
                DefaultChannelId.logger.debug("-Dio.netty.processId: {} (auto-detected)", (Object)processId);
            }
        }
        PROCESS_ID = processId;
        byte[] machineId = null;
        final String customMachineId = SystemPropertyUtil.get("io.netty.machineId");
        if (customMachineId != null) {
            try {
                machineId = MacAddressUtil.parseMAC(customMachineId);
            }
            catch (final Exception e) {
                DefaultChannelId.logger.warn("-Dio.netty.machineId: {} (malformed)", customMachineId, e);
            }
            if (machineId != null) {
                DefaultChannelId.logger.debug("-Dio.netty.machineId: {} (user-set)", customMachineId);
            }
        }
        if (machineId == null) {
            machineId = MacAddressUtil.defaultMachineId();
            if (DefaultChannelId.logger.isDebugEnabled()) {
                DefaultChannelId.logger.debug("-Dio.netty.machineId: {} (auto-detected)", MacAddressUtil.formatAddress(machineId));
            }
        }
        MACHINE_ID = machineId;
    }
}
