// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;

public interface FileUpload extends HttpData
{
    String getFilename();
    
    void setFilename(final String p0);
    
    void setContentType(final String p0);
    
    String getContentType();
    
    void setContentTransferEncoding(final String p0);
    
    String getContentTransferEncoding();
    
    FileUpload copy();
    
    FileUpload duplicate();
    
    FileUpload retainedDuplicate();
    
    FileUpload replace(final ByteBuf p0);
    
    FileUpload retain();
    
    FileUpload retain(final int p0);
    
    FileUpload touch();
    
    FileUpload touch(final Object p0);
}
