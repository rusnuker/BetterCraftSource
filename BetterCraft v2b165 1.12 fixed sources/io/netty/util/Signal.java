// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

public final class Signal extends Error implements Constant<Signal>
{
    private static final long serialVersionUID = -221145131122459977L;
    private static final ConstantPool<Signal> pool;
    private final SignalConstant constant;
    
    public static Signal valueOf(final String name) {
        return Signal.pool.valueOf(name);
    }
    
    public static Signal valueOf(final Class<?> firstNameComponent, final String secondNameComponent) {
        return Signal.pool.valueOf(firstNameComponent, secondNameComponent);
    }
    
    private Signal(final int id, final String name) {
        this.constant = new SignalConstant(id, name);
    }
    
    public void expect(final Signal signal) {
        if (this != signal) {
            throw new IllegalStateException("unexpected signal: " + signal);
        }
    }
    
    @Override
    public Throwable initCause(final Throwable cause) {
        return this;
    }
    
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
    
    @Override
    public int id() {
        return this.constant.id();
    }
    
    @Override
    public String name() {
        return this.constant.name();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }
    
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    @Override
    public int compareTo(final Signal other) {
        if (this == other) {
            return 0;
        }
        return this.constant.compareTo(other.constant);
    }
    
    @Override
    public String toString() {
        return this.name();
    }
    
    static {
        pool = new ConstantPool<Signal>() {
            @Override
            protected Signal newConstant(final int id, final String name) {
                return new Signal(id, name, null);
            }
        };
    }
    
    private static final class SignalConstant extends AbstractConstant<SignalConstant>
    {
        SignalConstant(final int id, final String name) {
            super(id, name);
        }
    }
}
