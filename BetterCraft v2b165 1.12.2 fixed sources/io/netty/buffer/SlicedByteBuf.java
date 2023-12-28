// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.io.OutputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.InputStream;
import java.nio.channels.ScatteringByteChannel;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import io.netty.util.ByteProcessor;

@Deprecated
public class SlicedByteBuf extends AbstractUnpooledSlicedByteBuf
{
    private int length;
    
    public SlicedByteBuf(final ByteBuf buffer, final int index, final int length) {
        super(buffer, index, length);
    }
    
    @Override
    final void initLength(final int length) {
        this.length = length;
    }
    
    @Override
    final int length() {
        return this.length;
    }
    
    @Override
    public int capacity() {
        return this.length;
    }
}
