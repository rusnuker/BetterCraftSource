// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractNBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B>
{
    @Nullable
    protected String nbtPath;
    protected boolean interpret;
    @Nullable
    protected Component separator;
    
    AbstractNBTComponentBuilder() {
        this.interpret = false;
    }
    
    AbstractNBTComponentBuilder(@NotNull final C component) {
        super(component);
        this.interpret = false;
        this.nbtPath = component.nbtPath();
        this.interpret = component.interpret();
        this.separator = component.separator();
    }
    
    @NotNull
    @Override
    public B nbtPath(@NotNull final String nbtPath) {
        this.nbtPath = Objects.requireNonNull(nbtPath, "nbtPath");
        return (B)this;
    }
    
    @NotNull
    @Override
    public B interpret(final boolean interpret) {
        this.interpret = interpret;
        return (B)this;
    }
    
    @NotNull
    @Override
    public B separator(@Nullable final ComponentLike separator) {
        this.separator = ComponentLike.unbox(separator);
        return (B)this;
    }
}
