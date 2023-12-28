// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Set;

@Deprecated
public class DefaultCookie extends io.netty.handler.codec.http.cookie.DefaultCookie implements Cookie
{
    private String comment;
    private String commentUrl;
    private boolean discard;
    private Set<Integer> ports;
    private Set<Integer> unmodifiablePorts;
    private int version;
    
    public DefaultCookie(final String name, final String value) {
        super(name, value);
        this.ports = Collections.emptySet();
        this.unmodifiablePorts = this.ports;
    }
    
    @Deprecated
    @Override
    public String getName() {
        return this.name();
    }
    
    @Deprecated
    @Override
    public String getValue() {
        return this.value();
    }
    
    @Deprecated
    @Override
    public String getDomain() {
        return this.domain();
    }
    
    @Deprecated
    @Override
    public String getPath() {
        return this.path();
    }
    
    @Deprecated
    @Override
    public String getComment() {
        return this.comment();
    }
    
    @Deprecated
    @Override
    public String comment() {
        return this.comment;
    }
    
    @Deprecated
    @Override
    public void setComment(final String comment) {
        this.comment = this.validateValue("comment", comment);
    }
    
    @Deprecated
    @Override
    public String getCommentUrl() {
        return this.commentUrl();
    }
    
    @Deprecated
    @Override
    public String commentUrl() {
        return this.commentUrl;
    }
    
    @Deprecated
    @Override
    public void setCommentUrl(final String commentUrl) {
        this.commentUrl = this.validateValue("commentUrl", commentUrl);
    }
    
    @Deprecated
    @Override
    public boolean isDiscard() {
        return this.discard;
    }
    
    @Deprecated
    @Override
    public void setDiscard(final boolean discard) {
        this.discard = discard;
    }
    
    @Deprecated
    @Override
    public Set<Integer> getPorts() {
        return this.ports();
    }
    
    @Deprecated
    @Override
    public Set<Integer> ports() {
        if (this.unmodifiablePorts == null) {
            this.unmodifiablePorts = Collections.unmodifiableSet((Set<? extends Integer>)this.ports);
        }
        return this.unmodifiablePorts;
    }
    
    @Deprecated
    @Override
    public void setPorts(final int... ports) {
        if (ports == null) {
            throw new NullPointerException("ports");
        }
        final int[] portsCopy = ports.clone();
        if (portsCopy.length == 0) {
            final Set<Integer> emptySet = Collections.emptySet();
            this.ports = emptySet;
            this.unmodifiablePorts = emptySet;
        }
        else {
            final Set<Integer> newPorts = new TreeSet<Integer>();
            for (final int p : portsCopy) {
                if (p <= 0 || p > 65535) {
                    throw new IllegalArgumentException("port out of range: " + p);
                }
                newPorts.add(p);
            }
            this.ports = newPorts;
            this.unmodifiablePorts = null;
        }
    }
    
    @Deprecated
    @Override
    public void setPorts(final Iterable<Integer> ports) {
        final Set<Integer> newPorts = new TreeSet<Integer>();
        for (final int p : ports) {
            if (p <= 0 || p > 65535) {
                throw new IllegalArgumentException("port out of range: " + p);
            }
            newPorts.add(p);
        }
        if (newPorts.isEmpty()) {
            final Set<Integer> emptySet = Collections.emptySet();
            this.ports = emptySet;
            this.unmodifiablePorts = emptySet;
        }
        else {
            this.ports = newPorts;
            this.unmodifiablePorts = null;
        }
    }
    
    @Deprecated
    @Override
    public long getMaxAge() {
        return this.maxAge();
    }
    
    @Deprecated
    @Override
    public int getVersion() {
        return this.version();
    }
    
    @Deprecated
    @Override
    public int version() {
        return this.version;
    }
    
    @Deprecated
    @Override
    public void setVersion(final int version) {
        this.version = version;
    }
}
