// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.unsupported;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import com.google.common.base.Preconditions;
import java.util.List;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;

public final class UnsupportedServerSoftware implements UnsupportedSoftware
{
    private final String name;
    private final List<String> classNames;
    private final List<UnsupportedMethods> methods;
    private final String reason;
    
    public UnsupportedServerSoftware(final String name, final List<String> classNames, final List<UnsupportedMethods> methods, final String reason) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(reason);
        Preconditions.checkArgument(!classNames.isEmpty() || !methods.isEmpty());
        this.name = name;
        this.classNames = Collections.unmodifiableList((List<? extends String>)classNames);
        this.methods = Collections.unmodifiableList((List<? extends UnsupportedMethods>)methods);
        this.reason = reason;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getReason() {
        return this.reason;
    }
    
    @Override
    public final String match() {
        for (final String className : this.classNames) {
            try {
                Class.forName(className);
                return this.name;
            }
            catch (final ClassNotFoundException ex) {
                continue;
            }
            break;
        }
        for (final UnsupportedMethods method : this.methods) {
            if (method.findMatch()) {
                return this.name;
            }
        }
        return null;
    }
    
    public static final class Builder
    {
        private final List<String> classNames;
        private final List<UnsupportedMethods> methods;
        private String name;
        private String reason;
        
        public Builder() {
            this.classNames = new ArrayList<String>();
            this.methods = new ArrayList<UnsupportedMethods>();
        }
        
        public Builder name(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder reason(final String reason) {
            this.reason = reason;
            return this;
        }
        
        public Builder addMethod(final String className, final String methodName) {
            this.methods.add(new UnsupportedMethods(className, Collections.singleton(methodName)));
            return this;
        }
        
        public Builder addMethods(final String className, final String... methodNames) {
            this.methods.add(new UnsupportedMethods(className, new HashSet<String>(Arrays.asList(methodNames))));
            return this;
        }
        
        public Builder addClassName(final String className) {
            this.classNames.add(className);
            return this;
        }
        
        public UnsupportedSoftware build() {
            return new UnsupportedServerSoftware(this.name, this.classNames, this.methods, this.reason);
        }
    }
    
    public static final class Reason
    {
        public static final String DANGEROUS_SERVER_SOFTWARE = "You are using server software that - outside of possibly breaking ViaVersion - can also cause severe damage to your server's integrity as a whole.";
        public static final String BREAKING_PROXY_SOFTWARE = "You are using proxy software that intentionally breaks ViaVersion. Please use another proxy software or move ViaVersion to each backend server instead of the proxy.";
    }
}
