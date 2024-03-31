// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PACKAGE })
public @interface XmlJavaTypeAdapters {
    XmlJavaTypeAdapter[] value();
}
