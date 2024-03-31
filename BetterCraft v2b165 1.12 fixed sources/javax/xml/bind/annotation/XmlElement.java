// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface XmlElement {
    String name() default "##default";
    
    boolean nillable() default false;
    
    boolean required() default false;
    
    String namespace() default "##default";
    
    String defaultValue() default "\u0000";
    
    Class type() default DEFAULT.class;
    
    public static final class DEFAULT
    {
    }
}
