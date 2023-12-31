// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config.plugins;

import org.apache.logging.log4j.core.config.plugins.visitors.PluginBuilderAttributeVisitor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@PluginVisitorStrategy(PluginBuilderAttributeVisitor.class)
public @interface PluginBuilderAttribute {
    String value() default "";
    
    boolean sensitive() default false;
}
