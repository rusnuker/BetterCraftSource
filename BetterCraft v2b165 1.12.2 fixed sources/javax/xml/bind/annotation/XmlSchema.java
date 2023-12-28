// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PACKAGE })
public @interface XmlSchema {
    public static final String NO_LOCATION = "##generate";
    
    XmlNs[] xmlns() default {};
    
    String namespace() default "";
    
    XmlNsForm elementFormDefault() default XmlNsForm.UNSET;
    
    XmlNsForm attributeFormDefault() default XmlNsForm.UNSET;
    
    String location() default "##generate";
}
