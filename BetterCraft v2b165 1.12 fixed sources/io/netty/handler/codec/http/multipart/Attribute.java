// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

public interface Attribute extends HttpData
{
    String getValue() throws IOException;
    
    void setValue(final String p0) throws IOException;
    
    Attribute copy();
    
    Attribute duplicate();
    
    Attribute retainedDuplicate();
    
    Attribute replace(final ByteBuf p0);
    
    Attribute retain();
    
    Attribute retain(final int p0);
    
    Attribute touch();
    
    Attribute touch(final Object p0);
}
