// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.util.ReferenceCounted;

public interface InterfaceHttpData extends Comparable<InterfaceHttpData>, ReferenceCounted
{
    String getName();
    
    HttpDataType getHttpDataType();
    
    public enum HttpDataType
    {
        Attribute, 
        FileUpload, 
        InternalAttribute;
    }
}
