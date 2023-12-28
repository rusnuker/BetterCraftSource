// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PACKAGE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER })
public @interface XmlJavaTypeAdapter {
    Class<? extends XmlAdapter> value();
    
    Class type() default DEFAULT.class;
    
    public static final class DEFAULT
    {
    }
}
