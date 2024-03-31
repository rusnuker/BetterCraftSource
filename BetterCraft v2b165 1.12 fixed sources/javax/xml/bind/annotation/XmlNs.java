// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface XmlNs {
    String prefix();
    
    String namespaceURI();
}
