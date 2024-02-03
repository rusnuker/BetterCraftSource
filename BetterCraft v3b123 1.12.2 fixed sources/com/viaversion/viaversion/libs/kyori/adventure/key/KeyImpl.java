// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import java.util.function.Function;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Comparator;

final class KeyImpl implements Key
{
    static final Comparator<? super Key> COMPARATOR;
    static final String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";
    static final String VALUE_PATTERN = "[a-z0-9_\\-./]+";
    private final String namespace;
    private final String value;
    
    KeyImpl(@NotNull final String namespace, @NotNull final String value) {
        if (!Key.parseableNamespace(namespace)) {
            throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9_.-] character in namespace of Key[%s]", asString(namespace, value)));
        }
        if (!Key.parseableValue(value)) {
            throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9/._-] character in value of Key[%s]", asString(namespace, value)));
        }
        this.namespace = Objects.requireNonNull(namespace, "namespace");
        this.value = Objects.requireNonNull(value, "value");
    }
    
    static boolean allowedInNamespace(final char character) {
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
    }
    
    static boolean allowedInValue(final char character) {
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.' || character == '/';
    }
    
    @NotNull
    @Override
    public String namespace() {
        return this.namespace;
    }
    
    @NotNull
    @Override
    public String value() {
        return this.value;
    }
    
    @NotNull
    @Override
    public String asString() {
        return asString(this.namespace, this.value);
    }
    
    @NotNull
    private static String asString(@NotNull final String namespace, @NotNull final String value) {
        return namespace + ':' + value;
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.asString();
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("namespace", this.namespace), ExaminableProperty.of("value", this.value) });
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Key)) {
            return false;
        }
        final Key that = (Key)other;
        return Objects.equals(this.namespace, that.namespace()) && Objects.equals(this.value, that.value());
    }
    
    @Override
    public int hashCode() {
        int result = this.namespace.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
    
    @Override
    public int compareTo(@NotNull final Key that) {
        return super.compareTo(that);
    }
    
    static {
        COMPARATOR = Comparator.comparing((Function<? super Object, ? extends Comparable>)Key::value).thenComparing((Function<? super Object, ? extends Comparable>)Key::namespace);
    }
}
