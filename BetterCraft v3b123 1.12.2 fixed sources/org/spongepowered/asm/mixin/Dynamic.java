// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.CLASS)
public @interface Dynamic {
    String value() default "";
    
    Class<?> mixin() default void.class;
}
