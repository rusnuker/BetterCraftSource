// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import java.util.Locale;
import java.lang.reflect.Field;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY {
        @Override
        public String translateName(final Field f) {
            return f.getName();
        }
    }, 
    UPPER_CAMEL_CASE {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.upperCaseFirstLetter(f.getName());
        }
    }, 
    UPPER_CAMEL_CASE_WITH_SPACES {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.upperCaseFirstLetter(FieldNamingPolicy.separateCamelCase(f.getName(), ' '));
        }
    }, 
    UPPER_CASE_WITH_UNDERSCORES {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), '_').toUpperCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_UNDERSCORES {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), '_').toLowerCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_DASHES {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), '-').toLowerCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_DOTS {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), '.').toLowerCase(Locale.ENGLISH);
        }
    };
    
    static String separateCamelCase(final String name, final char separator) {
        final StringBuilder translation = new StringBuilder();
        for (int i = 0, length = name.length(); i < length; ++i) {
            final char character = name.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
        }
        return translation.toString();
    }
    
    static String upperCaseFirstLetter(final String s) {
        final int length = s.length();
        int i = 0;
        while (i < length) {
            final char c = s.charAt(i);
            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c)) {
                    return s;
                }
                final char uppercased = Character.toUpperCase(c);
                if (i == 0) {
                    return uppercased + s.substring(1);
                }
                return s.substring(0, i) + uppercased + s.substring(i + 1);
            }
            else {
                ++i;
            }
        }
        return s;
    }
}
