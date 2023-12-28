// 
// Decompiled by Procyon v0.6.0
// 

package org.lwjgl.util.mapped;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface MappedField {
    long byteOffset() default -1L;
    
    long byteLength() default -1L;
}
