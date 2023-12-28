// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.Set;

@Deprecated
public interface Cookie extends io.netty.handler.codec.http.cookie.Cookie
{
    @Deprecated
    String getName();
    
    @Deprecated
    String getValue();
    
    @Deprecated
    String getDomain();
    
    @Deprecated
    String getPath();
    
    @Deprecated
    String getComment();
    
    @Deprecated
    String comment();
    
    @Deprecated
    void setComment(final String p0);
    
    @Deprecated
    long getMaxAge();
    
    @Deprecated
    long maxAge();
    
    @Deprecated
    void setMaxAge(final long p0);
    
    @Deprecated
    int getVersion();
    
    @Deprecated
    int version();
    
    @Deprecated
    void setVersion(final int p0);
    
    @Deprecated
    String getCommentUrl();
    
    @Deprecated
    String commentUrl();
    
    @Deprecated
    void setCommentUrl(final String p0);
    
    @Deprecated
    boolean isDiscard();
    
    @Deprecated
    void setDiscard(final boolean p0);
    
    @Deprecated
    Set<Integer> getPorts();
    
    @Deprecated
    Set<Integer> ports();
    
    @Deprecated
    void setPorts(final int... p0);
    
    @Deprecated
    void setPorts(final Iterable<Integer> p0);
}
