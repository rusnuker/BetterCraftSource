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
public @interface XmlElementRef {
    Class type() default DEFAULT.class;
    
    String namespace() default "";
    
    String name() default "##default";
    
    boolean required() default true;
    
    public static final class DEFAULT
    {
    }
}
