// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import java.util.Random;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Enumeration;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;

public final class UuidUtil
{
    public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
    private static final Logger LOGGER;
    private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
    private static final AtomicInteger COUNT;
    private static final long TYPE1 = 4096L;
    private static final byte VARIANT = Byte.MIN_VALUE;
    private static final int SEQUENCE_MASK = 16383;
    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
    private static final long INITIAL_UUID_SEQNO;
    private static final long LEAST;
    private static final long LOW_MASK = 4294967295L;
    private static final long MID_MASK = 281470681743360L;
    private static final long HIGH_MASK = 1152640029630136320L;
    private static final int NODE_SIZE = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_4 = 32;
    private static final int SHIFT_6 = 48;
    private static final int HUNDRED_NANOS_PER_MILLI = 10000;
    
    private UuidUtil() {
    }
    
    public static UUID getTimeBasedUuid() {
        final long time = System.currentTimeMillis() * 10000L + 122192928000000000L + UuidUtil.COUNT.incrementAndGet() % 10000;
        final long timeLow = (time & 0xFFFFFFFFL) << 32;
        final long timeMid = (time & 0xFFFF00000000L) >> 16;
        final long timeHi = (time & 0xFFF000000000000L) >> 48;
        final long most = timeLow | timeMid | 0x1000L | timeHi;
        return new UUID(most, UuidUtil.LEAST);
    }
    
    private static byte[] getLocalMacAddress() {
        byte[] mac = null;
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            try {
                final NetworkInterface localInterface = NetworkInterface.getByInetAddress(localHost);
                if (isUpAndNotLoopback(localInterface)) {
                    mac = localInterface.getHardwareAddress();
                }
                if (mac == null) {
                    NetworkInterface nic;
                    for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements() && mac == null; mac = nic.getHardwareAddress()) {
                        nic = networkInterfaces.nextElement();
                        if (isUpAndNotLoopback(nic)) {}
                    }
                }
            }
            catch (final SocketException e) {
                UuidUtil.LOGGER.catching(e);
            }
            if (mac == null || mac.length == 0) {
                mac = localHost.getAddress();
            }
        }
        catch (final UnknownHostException ex) {}
        return mac;
    }
    
    private static boolean isUpAndNotLoopback(final NetworkInterface ni) throws SocketException {
        return ni != null && !ni.isLoopback() && ni.isUp();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        COUNT = new AtomicInteger(0);
        INITIAL_UUID_SEQNO = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
        byte[] mac = getLocalMacAddress();
        final Random randomGenerator = new SecureRandom();
        if (mac == null || mac.length == 0) {
            mac = new byte[6];
            randomGenerator.nextBytes(mac);
        }
        final int length = (mac.length >= 6) ? 6 : mac.length;
        final int index = (mac.length >= 6) ? (mac.length - 6) : 0;
        final byte[] node = new byte[8];
        node[0] = -128;
        node[1] = 0;
        for (int i = 2; i < 8; ++i) {
            node[i] = 0;
        }
        System.arraycopy(mac, index, node, index + 2, length);
        final ByteBuffer buf = ByteBuffer.wrap(node);
        long rand = UuidUtil.INITIAL_UUID_SEQNO;
        String assigned = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
        long[] sequences;
        if (assigned == null) {
            sequences = new long[0];
        }
        else {
            final String[] array = assigned.split(Patterns.COMMA_SEPARATOR);
            sequences = new long[array.length];
            int j = 0;
            for (final String value : array) {
                sequences[j] = Long.parseLong(value);
                ++j;
            }
        }
        if (rand == 0L) {
            rand = randomGenerator.nextLong();
        }
        rand &= 0x3FFFL;
        boolean duplicate;
        do {
            duplicate = false;
            for (final long sequence : sequences) {
                if (sequence == rand) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                rand = (rand + 1L & 0x3FFFL);
            }
        } while (duplicate);
        assigned = ((assigned == null) ? Long.toString(rand) : (assigned + ',' + Long.toString(rand)));
        System.setProperty("org.apache.logging.log4j.assignedSequences", assigned);
        LEAST = (buf.getLong() | rand << 48);
    }
}
