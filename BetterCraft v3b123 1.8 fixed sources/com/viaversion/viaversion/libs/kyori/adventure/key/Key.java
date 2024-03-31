// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Comparator;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface Key extends Comparable<Key>, Examinable, Namespaced, Keyed
{
    public static final String MINECRAFT_NAMESPACE = "minecraft";
    public static final char DEFAULT_SEPARATOR = ':';
    
    @NotNull
    default Key key(@NotNull @Pattern("([a-z0-9_\\-.]+:)?[a-z0-9_\\-./]+") final String string) {
        return key(string, ':');
    }
    
    @NotNull
    default Key key(@NotNull final String string, final char character) {
        final int index = string.indexOf(character);
        final String namespace = (index >= 1) ? string.substring(0, index) : "minecraft";
        final String value = (index >= 0) ? string.substring(index + 1) : string;
        return key(namespace, value);
    }
    
    @NotNull
    default Key key(@NotNull final Namespaced namespaced, @NotNull @Pattern("[a-z0-9_\\-./]+") final String value) {
        return key(namespaced.namespace(), value);
    }
    
    @NotNull
    default Key key(@NotNull @Pattern("[a-z0-9_\\-.]+") final String namespace, @NotNull @Pattern("[a-z0-9_\\-./]+") final String value) {
        return new KeyImpl(namespace, value);
    }
    
    @NotNull
    default Comparator<? super Key> comparator() {
        return KeyImpl.COMPARATOR;
    }
    
    default boolean parseable(@Nullable final String string) {
        if (string == null) {
            return false;
        }
        final int index = string.indexOf(58);
        final String namespace = (index >= 1) ? string.substring(0, index) : "minecraft";
        final String value = (index >= 0) ? string.substring(index + 1) : string;
        return parseableNamespace(namespace) && parseableValue(value);
    }
    
    default boolean parseableNamespace(@NotNull final String namespace) {
        for (int i = 0, length = namespace.length(); i < length; ++i) {
            if (!allowedInNamespace(namespace.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    default boolean parseableValue(@NotNull final String value) {
        for (int i = 0, length = value.length(); i < length; ++i) {
            if (!allowedInValue(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    default boolean allowedInNamespace(final char character) {
        return KeyImpl.allowedInNamespace(character);
    }
    
    default boolean allowedInValue(final char character) {
        return KeyImpl.allowedInValue(character);
    }
    
    @NotNull
    String namespace();
    
    @NotNull
    String value();
    
    @NotNull
    String asString();
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("namespace", this.namespace()), ExaminableProperty.of("value", this.value()) });
    }
    
    default int compareTo(@NotNull final Key that) {
        return comparator().compare(this, that);
    }
    
    @NotNull
    default Key key() {
        return this;
    }
}
