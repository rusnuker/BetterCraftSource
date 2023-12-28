// 
// Decompiled by Procyon v0.6.0
// 

package javax.annotation;

import java.lang.annotation.Annotation;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.Documented;

@Documented
@TypeQualifier(applicableTo = Number.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Nonnegative {
    When when() default When.ALWAYS;
    
    public static class Checker implements TypeQualifierValidator<Nonnegative>
    {
        @Override
        public When forConstantValue(final Nonnegative annotation, final Object v) {
            if (!(v instanceof Number)) {
                return When.NEVER;
            }
            final Number value = (Number)v;
            boolean isNegative;
            if (value instanceof Long) {
                isNegative = (value.longValue() < 0L);
            }
            else if (value instanceof Double) {
                isNegative = (value.doubleValue() < 0.0);
            }
            else if (value instanceof Float) {
                isNegative = (value.floatValue() < 0.0f);
            }
            else {
                isNegative = (value.intValue() < 0);
            }
            if (isNegative) {
                return When.NEVER;
            }
            return When.ALWAYS;
        }
    }
}
