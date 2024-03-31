// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface XmlElementDecl {
    Class scope() default GLOBAL.class;
    
    String namespace() default "##default";
    
    String name();
    
    String substitutionHeadNamespace() default "##default";
    
    String substitutionHeadName() default "";
    
    String defaultValue() default "\u0000";
    
    public static final class GLOBAL
    {
    }
}
