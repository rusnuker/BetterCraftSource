// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import io.netty.handler.codec.MessageToByteEncoder;

public class CompatibleObjectEncoder extends MessageToByteEncoder<Serializable>
{
    private final int resetInterval;
    private int writtenObjects;
    
    public CompatibleObjectEncoder() {
        this(16);
    }
    
    public CompatibleObjectEncoder(final int resetInterval) {
        if (resetInterval < 0) {
            throw new IllegalArgumentException("resetInterval: " + resetInterval);
        }
        this.resetInterval = resetInterval;
    }
    
    protected ObjectOutputStream newObjectOutputStream(final OutputStream out) throws Exception {
        return new ObjectOutputStream(out);
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Serializable msg, final ByteBuf out) throws Exception {
        final ObjectOutputStream oos = this.newObjectOutputStream(new ByteBufOutputStream(out));
        try {
            if (this.resetInterval != 0) {
                ++this.writtenObjects;
                if (this.writtenObjects % this.resetInterval == 0) {
                    oos.reset();
                }
            }
            oos.writeObject(msg);
            oos.flush();
        }
        finally {
            oos.close();
        }
    }
}
