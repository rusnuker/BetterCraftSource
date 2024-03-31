// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import java.util.List;
import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

final class ComponentCompaction
{
    private ComponentCompaction() {
    }
    
    static Component compact(@NotNull final Component self, @Nullable final Style parentStyle) {
        final List<Component> children = self.children();
        Component optimized = self.children(Collections.emptyList());
        if (parentStyle != null) {
            optimized = optimized.style(self.style().unmerge(parentStyle));
        }
        final int childrenSize = children.size();
        if (childrenSize == 0) {
            if (isBlank(optimized)) {
                optimized = optimized.style(simplifyStyleForBlank(optimized.style(), parentStyle));
            }
            return optimized;
        }
        if (childrenSize == 1 && optimized instanceof TextComponent) {
            final TextComponent textComponent = (TextComponent)optimized;
            if (textComponent.content().isEmpty()) {
                final Component child = children.get(0);
                return child.style(child.style().merge(optimized.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET)).compact();
            }
        }
        Style childParentStyle = optimized.style();
        if (parentStyle != null) {
            childParentStyle = childParentStyle.merge(parentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
        }
        final List<Component> childrenToAppend = new ArrayList<Component>(children.size());
        for (int i = 0; i < children.size(); ++i) {
            Component child2 = children.get(i);
            child2 = compact(child2, childParentStyle);
            if (child2.children().isEmpty() && child2 instanceof TextComponent) {
                final TextComponent textComponent2 = (TextComponent)child2;
                if (textComponent2.content().isEmpty()) {
                    continue;
                }
            }
            childrenToAppend.add(child2);
        }
        if (optimized instanceof TextComponent) {
            while (!childrenToAppend.isEmpty()) {
                final Component child3 = childrenToAppend.get(0);
                final Style childStyle = child3.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                if (!(child3 instanceof TextComponent) || !Objects.equals(childStyle, childParentStyle)) {
                    break;
                }
                optimized = joinText((TextComponent)optimized, (TextComponent)child3);
                childrenToAppend.remove(0);
                childrenToAppend.addAll(0, child3.children());
            }
        }
        int i = 0;
        while (i + 1 < childrenToAppend.size()) {
            final Component child2 = childrenToAppend.get(i);
            final Component neighbor = childrenToAppend.get(i + 1);
            if (child2.children().isEmpty() && child2 instanceof TextComponent && neighbor instanceof TextComponent) {
                final Style childStyle2 = child2.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                final Style neighborStyle = neighbor.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                if (childStyle2.equals(neighborStyle)) {
                    final Component combined = joinText((TextComponent)child2, (TextComponent)neighbor);
                    childrenToAppend.set(i, combined);
                    childrenToAppend.remove(i + 1);
                    continue;
                }
            }
            ++i;
        }
        if (childrenToAppend.isEmpty() && isBlank(optimized)) {
            optimized = optimized.style(simplifyStyleForBlank(optimized.style(), parentStyle));
        }
        return optimized.children(childrenToAppend);
    }
    
    private static boolean isBlank(final Component component) {
        if (component instanceof TextComponent) {
            final TextComponent textComponent = (TextComponent)component;
            final String content = textComponent.content();
            for (int i = 0; i < content.length(); ++i) {
                final char c = content.charAt(i);
                if (c != ' ') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @NotNull
    private static Style simplifyStyleForBlank(@NotNull final Style style, @Nullable final Style parentStyle) {
        final Style.Builder builder = style.toBuilder();
        if (!style.hasDecoration(TextDecoration.UNDERLINED) && !style.hasDecoration(TextDecoration.STRIKETHROUGH) && (parentStyle == null || (!parentStyle.hasDecoration(TextDecoration.UNDERLINED) && !parentStyle.hasDecoration(TextDecoration.STRIKETHROUGH)))) {
            builder.color((TextColor)null);
        }
        builder.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
        builder.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
        return builder.build();
    }
    
    private static TextComponent joinText(final TextComponent one, final TextComponent two) {
        return TextComponentImpl.create(two.children(), one.style(), one.content() + two.content());
    }
}
