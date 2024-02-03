// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.internal.properties;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Optional;
import java.nio.file.Paths;
import java.nio.file.Path;
import org.jetbrains.annotations.Nullable;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
import java.util.Properties;

final class AdventurePropertiesImpl
{
    private static final String FILESYSTEM_DIRECTORY_NAME = "config";
    private static final String FILESYSTEM_FILE_NAME = "adventure.properties";
    private static final Properties PROPERTIES;
    
    private static void print(final Throwable ex) {
        ex.printStackTrace();
    }
    
    private AdventurePropertiesImpl() {
    }
    
    @VisibleForTesting
    @NotNull
    static String systemPropertyName(final String name) {
        return String.join(".", "net", "kyori", "adventure", name);
    }
    
    static <T> AdventureProperties.Property<T> property(@NotNull final String name, @NotNull final Function<String, T> parser, @Nullable final T defaultValue) {
        return new PropertyImpl<T>(name, parser, defaultValue);
    }
    
    static {
        PROPERTIES = new Properties();
        final Path path = Optional.ofNullable(System.getProperty(systemPropertyName("config"))).map(x$0 -> Paths.get(x$0, new String[0])).orElseGet(() -> Paths.get("config", "adventure.properties"));
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try (final InputStream is = Files.newInputStream(path, new OpenOption[0])) {
                AdventurePropertiesImpl.PROPERTIES.load(is);
            }
            catch (final IOException e) {
                print(e);
            }
        }
    }
    
    private static final class PropertyImpl<T> implements AdventureProperties.Property<T>
    {
        private final String name;
        private final Function<String, T> parser;
        @Nullable
        private final T defaultValue;
        private boolean valueCalculated;
        @Nullable
        private T value;
        
        PropertyImpl(@NotNull final String name, @NotNull final Function<String, T> parser, @Nullable final T defaultValue) {
            this.name = name;
            this.parser = parser;
            this.defaultValue = defaultValue;
        }
        
        @Nullable
        @Override
        public T value() {
            if (!this.valueCalculated) {
                final String property = AdventurePropertiesImpl.systemPropertyName(this.name);
                final String value = System.getProperty(property, AdventurePropertiesImpl.PROPERTIES.getProperty(this.name));
                if (value != null) {
                    this.value = this.parser.apply(value);
                }
                if (this.value == null) {
                    this.value = this.defaultValue;
                }
                this.valueCalculated = true;
            }
            return this.value;
        }
        
        @Override
        public boolean equals(@Nullable final Object that) {
            return this == that;
        }
        
        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
    }
}
