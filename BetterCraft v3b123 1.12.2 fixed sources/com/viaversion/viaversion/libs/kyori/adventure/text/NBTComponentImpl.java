// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import org.jetbrains.annotations.Nullable;

abstract class NBTComponentImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponent implements NBTComponent<C, B>
{
    static final boolean INTERPRET_DEFAULT = false;
    final String nbtPath;
    final boolean interpret;
    @Nullable
    final Component separator;
    
    NBTComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final Component separator) {
        super(children, style);
        this.nbtPath = nbtPath;
        this.interpret = interpret;
        this.separator = separator;
    }
    
    @NotNull
    @Override
    public String nbtPath() {
        return this.nbtPath;
    }
    
    @Override
    public boolean interpret() {
        return this.interpret;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NBTComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final NBTComponent<?, ?> that = (NBTComponent<?, ?>)other;
        return Objects.equals(this.nbtPath, that.nbtPath()) && this.interpret == that.interpret() && Objects.equals(this.separator, that.separator());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.nbtPath.hashCode();
        result = 31 * result + Boolean.hashCode(this.interpret);
        result = 31 * result + Objects.hashCode(this.separator);
        return result;
    }
}
