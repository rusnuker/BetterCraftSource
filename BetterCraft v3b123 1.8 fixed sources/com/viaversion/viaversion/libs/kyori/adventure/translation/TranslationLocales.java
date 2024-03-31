// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.translation;

import com.viaversion.viaversion.libs.kyori.adventure.internal.properties.AdventureProperties;
import java.util.Locale;
import java.util.function.Supplier;

final class TranslationLocales
{
    private static final Supplier<Locale> GLOBAL;
    
    private TranslationLocales() {
    }
    
    static Locale global() {
        return TranslationLocales.GLOBAL.get();
    }
    
    static {
        final String property = AdventureProperties.DEFAULT_TRANSLATION_LOCALE.value();
        if (property == null || property.isEmpty()) {
            GLOBAL = (() -> Locale.US);
        }
        else if (property.equals("system")) {
            GLOBAL = Locale::getDefault;
        }
        else {
            final Locale locale = Translator.parseLocale(property);
            GLOBAL = (() -> locale);
        }
    }
}
