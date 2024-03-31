// 
// Decompiled by Procyon v0.6.0
// 

package javax.annotation;

import java.lang.annotation.Annotation;
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
import javax.annotation.meta.TypeQualifierNickname;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Documented
@Syntax("RegEx")
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifierNickname
public @interface RegEx {
    When when() default When.ALWAYS;
    
    public static class Checker implements TypeQualifierValidator<RegEx>
    {
        @Override
        public When forConstantValue(final RegEx annotation, final Object value) {
            if (!(value instanceof String)) {
                return When.NEVER;
            }
            try {
                Pattern.compile((String)value);
            }
            catch (final PatternSyntaxException e) {
                return When.NEVER;
            }
            return When.ALWAYS;
        }
    }
}
