// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface XmlType {
    String name() default "##default";
    
    String[] propOrder() default { "" };
    
    String namespace() default "##default";
    
    Class factoryClass() default DEFAULT.class;
    
    String factoryMethod() default "";
    
    public static final class DEFAULT
    {
    }
}
