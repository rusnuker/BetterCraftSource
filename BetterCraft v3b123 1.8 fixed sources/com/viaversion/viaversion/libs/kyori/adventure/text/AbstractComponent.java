// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.List;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.ApiStatus;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
@Debug.Renderer(text = "this.debuggerString()", childrenArray = "this.children().toArray()", hasChildren = "!this.children().isEmpty()")
public abstract class AbstractComponent implements Component
{
    protected final List<Component> children;
    protected final Style style;
    
    protected AbstractComponent(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style) {
        this.children = ComponentLike.asComponents(children, AbstractComponent.IS_NOT_EMPTY);
        this.style = style;
    }
    
    @NotNull
    @Override
    public final List<Component> children() {
        return this.children;
    }
    
    @NotNull
    @Override
    public final Style style() {
        return this.style;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractComponent)) {
            return false;
        }
        final AbstractComponent that = (AbstractComponent)other;
        return Objects.equals(this.children, that.children) && Objects.equals(this.style, that.style);
    }
    
    @Override
    public int hashCode() {
        int result = this.children.hashCode();
        result = 31 * result + this.style.hashCode();
        return result;
    }
    
    @Override
    public abstract String toString();
    
    private String debuggerString() {
        final Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren = this.examinableProperties().filter(property -> !property.name().equals("children"));
        return StringExaminer.simpleEscaping().examine(this.examinableName(), examinablePropertiesWithoutChildren);
    }
}
