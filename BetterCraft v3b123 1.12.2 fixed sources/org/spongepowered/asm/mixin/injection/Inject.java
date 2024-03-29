// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection;

import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    String id() default "";
    
    String[] method() default {};
    
    Desc[] target() default {};
    
    Slice[] slice() default {};
    
    At[] at();
    
    boolean cancellable() default false;
    
    LocalCapture locals() default LocalCapture.NO_CAPTURE;
    
    boolean remap() default true;
    
    int require() default -1;
    
    int expect() default 1;
    
    int allow() default -1;
    
    String constraints() default "";
}
