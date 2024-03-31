// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface XmlElementWrapper {
    String name() default "##default";
    
    String namespace() default "##default";
    
    boolean nillable() default false;
    
    boolean required() default false;
}
