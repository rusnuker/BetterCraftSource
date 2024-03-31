// 
// Decompiled by Procyon v0.6.0
// 

package javax.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Resources.class)
public @interface Resource {
    String name() default "";
    
    String lookup() default "";
    
    Class<?> type() default Object.class;
    
    AuthenticationType authenticationType() default AuthenticationType.CONTAINER;
    
    boolean shareable() default true;
    
    String mappedName() default "";
    
    String description() default "";
    
    public enum AuthenticationType
    {
        CONTAINER("CONTAINER", 0), 
        APPLICATION("APPLICATION", 1);
        
        private AuthenticationType(final String s, final int n) {
        }
    }
}
