// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

public final class AttributeKey<T> extends AbstractConstant<AttributeKey<T>>
{
    private static final ConstantPool<AttributeKey<Object>> pool;
    
    public static <T> AttributeKey<T> valueOf(final String name) {
        return (AttributeKey)AttributeKey.pool.valueOf(name);
    }
    
    public static boolean exists(final String name) {
        return AttributeKey.pool.exists(name);
    }
    
    public static <T> AttributeKey<T> newInstance(final String name) {
        return (AttributeKey)AttributeKey.pool.newInstance(name);
    }
    
    public static <T> AttributeKey<T> valueOf(final Class<?> firstNameComponent, final String secondNameComponent) {
        return (AttributeKey)AttributeKey.pool.valueOf(firstNameComponent, secondNameComponent);
    }
    
    private AttributeKey(final int id, final String name) {
        super(id, name);
    }
    
    static {
        pool = new ConstantPool<AttributeKey<Object>>() {
            @Override
            protected AttributeKey<Object> newConstant(final int id, final String name) {
                return new AttributeKey<Object>(id, name, null);
            }
        };
    }
}
