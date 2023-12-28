// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpContent;
import java.util.List;

public interface InterfaceHttpPostRequestDecoder
{
    boolean isMultipart();
    
    void setDiscardThreshold(final int p0);
    
    int getDiscardThreshold();
    
    List<InterfaceHttpData> getBodyHttpDatas();
    
    List<InterfaceHttpData> getBodyHttpDatas(final String p0);
    
    InterfaceHttpData getBodyHttpData(final String p0);
    
    InterfaceHttpPostRequestDecoder offer(final HttpContent p0);
    
    boolean hasNext();
    
    InterfaceHttpData next();
    
    InterfaceHttpData currentPartialHttpData();
    
    void destroy();
    
    void cleanFiles();
    
    void removeHttpDataFromClean(final InterfaceHttpData p0);
}
