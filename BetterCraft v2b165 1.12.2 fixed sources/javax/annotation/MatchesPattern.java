// 
// Decompiled by Procyon v0.6.0
// 

package javax.annotation;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;
import javax.annotation.meta.When;
import javax.annotation.meta.TypeQualifierValidator;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.Documented;

@Documented
@TypeQualifier(applicableTo = String.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchesPattern {
    @RegEx
    String value();
    
    int flags() default 0;
    
    public static class Checker implements TypeQualifierValidator<MatchesPattern>
    {
        @Override
        public When forConstantValue(final MatchesPattern annotation, final Object value) {
            final Pattern p = Pattern.compile(annotation.value(), annotation.flags());
            if (p.matcher((CharSequence)value).matches()) {
                return When.ALWAYS;
            }
            return When.NEVER;
        }
    }
}
