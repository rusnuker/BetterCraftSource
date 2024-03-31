// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.internal.properties;

import org.jetbrains.annotations.Nullable;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class AdventureProperties
{
    public static final Property<Boolean> DEBUG;
    public static final Property<String> DEFAULT_TRANSLATION_LOCALE;
    public static final Property<Boolean> SERVICE_LOAD_FAILURES_ARE_FATAL;
    public static final Property<Boolean> TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED;
    
    private AdventureProperties() {
    }
    
    @NotNull
    public static <T> Property<T> property(@NotNull final String name, @NotNull final Function<String, T> parser, @Nullable final T defaultValue) {
        return AdventurePropertiesImpl.property(name, parser, defaultValue);
    }
    
    static {
        DEBUG = property("debug", Boolean::parseBoolean, false);
        DEFAULT_TRANSLATION_LOCALE = property("defaultTranslationLocale", Function.identity(), null);
        SERVICE_LOAD_FAILURES_ARE_FATAL = property("serviceLoadFailuresAreFatal", Boolean::parseBoolean, Boolean.TRUE);
        TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED = property("text.warnWhenLegacyFormattingDetected", Boolean::parseBoolean, Boolean.FALSE);
    }
    
    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    public interface Property<T>
    {
        @Nullable
        T value();
    }
}
