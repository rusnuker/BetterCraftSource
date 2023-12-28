// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.rcon;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.io.Closeable;

public class RconClient implements Closeable
{
    private static final int AUTHENTICATION_FAILURE_ID = -1;
    private static final Charset PAYLOAD_CHARSET;
    private static final int TYPE_COMMAND = 2;
    private static final int TYPE_AUTH = 3;
    private final SocketChannel socketChannel;
    private final AtomicInteger currentRequestId;
    
    static {
        PAYLOAD_CHARSET = StandardCharsets.US_ASCII;
    }
    
    private RconClient(final SocketChannel socketChannel) {
        this.socketChannel = Objects.requireNonNull(socketChannel, "socketChannel");
        this.currentRequestId = new AtomicInteger(1);
    }
    
    public static RconClient open(final String host, final int port, final String password) {
        SocketChannel socketChannel;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
        }
        catch (final IOException e) {
            throw new RconClientException("Failed to open socket to " + host + ":" + port, e);
        }
        final RconClient rconClient = new RconClient(socketChannel);
        try {
            rconClient.authenticate(password);
        }
        catch (final Exception authException) {
            try {
                rconClient.close();
            }
            catch (final Exception closingException) {
                authException.addSuppressed(closingException);
            }
            throw authException;
        }
        return rconClient;
    }
    
    public String sendCommand(final String command) {
        return this.send(2, command);
    }
    
    @Override
    public void close() {
        try {
            this.socketChannel.close();
        }
        catch (final IOException e) {
            throw new RconClientException("Failed to close socket channel", e);
        }
    }
    
    private void authenticate(final String password) {
        this.send(3, password);
    }
    
    private String send(final int type, final String payload) {
        final int requestId = this.currentRequestId.getAndIncrement();
        final ByteBuffer buffer = toByteBuffer(requestId, type, payload);
        try {
            this.socketChannel.write(buffer);
        }
        catch (final IOException e) {
            throw new RconClientException("Failed to write " + buffer.capacity() + " bytes", e);
        }
        final ByteBuffer responseBuffer = this.readResponse();
        final int responseId = responseBuffer.getInt();
        if (responseId == -1) {
            throw new AuthFailureException();
        }
        if (responseId != requestId) {
            throw new RconClientException("Sent request id " + requestId + " but received " + responseId);
        }
        final int responseType = responseBuffer.getInt();
        final byte[] bodyBytes = new byte[responseBuffer.remaining()];
        responseBuffer.get(bodyBytes);
        return new String(bodyBytes, RconClient.PAYLOAD_CHARSET);
    }
    
    private ByteBuffer readResponse() {
        final int size = this.readData(4).getInt();
        final ByteBuffer dataBuffer = this.readData(size - 2);
        final ByteBuffer nullsBuffer = this.readData(2);
        final byte null1 = nullsBuffer.get(0);
        final byte null2 = nullsBuffer.get(1);
        if (null1 != 0 || null2 != 0) {
            throw new RconClientException("Expected 2 null bytes but received " + null1 + " and " + null2);
        }
        return dataBuffer;
    }
    
    private ByteBuffer readData(final int size) {
        final ByteBuffer buffer = ByteBuffer.allocate(size);
        int readCount;
        try {
            readCount = this.socketChannel.read(buffer);
        }
        catch (final IOException e) {
            throw new RconClientException("Failed to read " + size + " bytes", e);
        }
        if (readCount != size) {
            throw new RconClientException("Expected " + size + " bytes but received " + readCount);
        }
        buffer.position(0);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }
    
    private static ByteBuffer toByteBuffer(final int requestId, final int type, final String payload) {
        final ByteBuffer buffer = ByteBuffer.allocate(12 + payload.length() + 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(8 + payload.length() + 2);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(payload.getBytes(RconClient.PAYLOAD_CHARSET));
        buffer.put((byte)0);
        buffer.put((byte)0);
        buffer.position(0);
        return buffer;
    }
}
