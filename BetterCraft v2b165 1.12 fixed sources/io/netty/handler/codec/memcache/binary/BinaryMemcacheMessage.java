// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.MemcacheMessage;

public interface BinaryMemcacheMessage extends MemcacheMessage
{
    byte magic();
    
    BinaryMemcacheMessage setMagic(final byte p0);
    
    byte opcode();
    
    BinaryMemcacheMessage setOpcode(final byte p0);
    
    short keyLength();
    
    byte extrasLength();
    
    byte dataType();
    
    BinaryMemcacheMessage setDataType(final byte p0);
    
    int totalBodyLength();
    
    BinaryMemcacheMessage setTotalBodyLength(final int p0);
    
    int opaque();
    
    BinaryMemcacheMessage setOpaque(final int p0);
    
    long cas();
    
    BinaryMemcacheMessage setCas(final long p0);
    
    ByteBuf key();
    
    BinaryMemcacheMessage setKey(final ByteBuf p0);
    
    ByteBuf extras();
    
    BinaryMemcacheMessage setExtras(final ByteBuf p0);
    
    BinaryMemcacheMessage retain();
    
    BinaryMemcacheMessage retain(final int p0);
    
    BinaryMemcacheMessage touch();
    
    BinaryMemcacheMessage touch(final Object p0);
}
