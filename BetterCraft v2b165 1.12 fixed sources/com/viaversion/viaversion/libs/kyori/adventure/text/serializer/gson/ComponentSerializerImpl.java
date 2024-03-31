// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.kyori.adventure.text.KeybindComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.SelectorComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import java.util.Map;
import com.viaversion.viaversion.libs.gson.JsonSerializationContext;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponent;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.ScoreComponent;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.Iterator;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.StorageNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.EntityNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import java.util.List;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.kyori.adventure.text.BuildableComponent;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.gson.JsonDeserializer;

final class ComponentSerializerImpl implements JsonDeserializer<Component>, JsonSerializer<Component>
{
    static final String TEXT = "text";
    static final String TRANSLATE = "translate";
    static final String TRANSLATE_WITH = "with";
    static final String SCORE = "score";
    static final String SCORE_NAME = "name";
    static final String SCORE_OBJECTIVE = "objective";
    static final String SCORE_VALUE = "value";
    static final String SELECTOR = "selector";
    static final String KEYBIND = "keybind";
    static final String EXTRA = "extra";
    static final String NBT = "nbt";
    static final String NBT_INTERPRET = "interpret";
    static final String NBT_BLOCK = "block";
    static final String NBT_ENTITY = "entity";
    static final String NBT_STORAGE = "storage";
    static final String SEPARATOR = "separator";
    
    @Override
    public Component deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        return this.deserialize0(json, context);
    }
    
    private BuildableComponent<?, ?> deserialize0(final JsonElement element, final JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonPrimitive()) {
            return Component.text(element.getAsString());
        }
        if (element.isJsonArray()) {
            ComponentBuilder<?, ?> parent = null;
            for (final JsonElement childElement : element.getAsJsonArray()) {
                final BuildableComponent<?, ?> child = this.deserialize0(childElement, context);
                if (parent == null) {
                    parent = (ComponentBuilder<?, ?>)child.toBuilder();
                }
                else {
                    parent.append(child);
                }
            }
            if (parent == null) {
                throw notSureHowToDeserialize(element);
            }
            return (BuildableComponent<?, ?>)parent.build();
        }
        else {
            if (!element.isJsonObject()) {
                throw notSureHowToDeserialize(element);
            }
            final JsonObject object = element.getAsJsonObject();
            ComponentBuilder<?, ?> component;
            if (object.has("text")) {
                component = Component.text().content(object.get("text").getAsString());
            }
            else if (object.has("translate")) {
                final String key = object.get("translate").getAsString();
                if (!object.has("with")) {
                    component = Component.translatable().key(key);
                }
                else {
                    final JsonArray with = object.getAsJsonArray("with");
                    final List<Component> args = new ArrayList<Component>(with.size());
                    for (int i = 0, size = with.size(); i < size; ++i) {
                        final JsonElement argElement = with.get(i);
                        args.add(this.deserialize0(argElement, context));
                    }
                    component = Component.translatable().key(key).args(args);
                }
            }
            else if (object.has("score")) {
                final JsonObject score = object.getAsJsonObject("score");
                if (!score.has("name") || !score.has("objective")) {
                    throw new JsonParseException("A score component requires a name and objective");
                }
                final ScoreComponent.Builder builder = Component.score().name(score.get("name").getAsString()).objective(score.get("objective").getAsString());
                if (score.has("value")) {
                    component = builder.value(score.get("value").getAsString());
                }
                else {
                    component = builder;
                }
            }
            else if (object.has("selector")) {
                final Component separator = this.deserializeSeparator(object, context);
                component = Component.selector().pattern(object.get("selector").getAsString()).separator(separator);
            }
            else if (object.has("keybind")) {
                component = Component.keybind().keybind(object.get("keybind").getAsString());
            }
            else {
                if (!object.has("nbt")) {
                    throw notSureHowToDeserialize(element);
                }
                final String nbt = object.get("nbt").getAsString();
                final boolean interpret = object.has("interpret") && object.getAsJsonPrimitive("interpret").getAsBoolean();
                final Component separator2 = this.deserializeSeparator(object, context);
                if (object.has("block")) {
                    final BlockNBTComponent.Pos pos = context.deserialize(object.get("block"), BlockNBTComponent.Pos.class);
                    component = nbt(Component.blockNBT(), nbt, interpret, separator2).pos(pos);
                }
                else if (object.has("entity")) {
                    component = nbt(Component.entityNBT(), nbt, interpret, separator2).selector(object.get("entity").getAsString());
                }
                else {
                    if (!object.has("storage")) {
                        throw notSureHowToDeserialize(element);
                    }
                    component = nbt(Component.storageNBT(), nbt, interpret, separator2).storage(context.deserialize(object.get("storage"), Key.class));
                }
            }
            if (object.has("extra")) {
                final JsonArray extra = object.getAsJsonArray("extra");
                for (int j = 0, size2 = extra.size(); j < size2; ++j) {
                    final JsonElement extraElement = extra.get(j);
                    component.append(this.deserialize0(extraElement, context));
                }
            }
            final Style style = context.deserialize(element, Style.class);
            if (!style.isEmpty()) {
                component.style(style);
            }
            return (BuildableComponent<?, ?>)component.build();
        }
    }
    
    @Nullable
    private Component deserializeSeparator(final JsonObject json, final JsonDeserializationContext context) {
        if (json.has("separator")) {
            return this.deserialize0(json.get("separator"), context);
        }
        return null;
    }
    
    private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final String nbt, final boolean interpret, @Nullable final Component separator) {
        return ((NBTComponentBuilder<C, B>)((NBTComponentBuilder<C, NBTComponentBuilder<C, B>>)builder).nbtPath(nbt).interpret(interpret)).separator(separator);
    }
    
    @Override
    public JsonElement serialize(final Component src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject object = new JsonObject();
        if (src.hasStyling()) {
            final JsonElement style = context.serialize(src.style());
            if (style.isJsonObject()) {
                for (final Map.Entry<String, JsonElement> entry : ((JsonObject)style).entrySet()) {
                    object.add(entry.getKey(), entry.getValue());
                }
            }
        }
        final List<Component> children = src.children();
        if (!children.isEmpty()) {
            final JsonArray extra = new JsonArray();
            for (final Component child : children) {
                extra.add(context.serialize(child));
            }
            object.add("extra", extra);
        }
        if (src instanceof TextComponent) {
            object.addProperty("text", ((TextComponent)src).content());
        }
        else if (src instanceof TranslatableComponent) {
            final TranslatableComponent tc = (TranslatableComponent)src;
            object.addProperty("translate", tc.key());
            if (!tc.args().isEmpty()) {
                final JsonArray with = new JsonArray();
                for (final Component arg : tc.args()) {
                    with.add(context.serialize(arg));
                }
                object.add("with", with);
            }
        }
        else if (src instanceof ScoreComponent) {
            final ScoreComponent sc = (ScoreComponent)src;
            final JsonObject score = new JsonObject();
            score.addProperty("name", sc.name());
            score.addProperty("objective", sc.objective());
            final String value = sc.value();
            if (value != null) {
                score.addProperty("value", value);
            }
            object.add("score", score);
        }
        else if (src instanceof SelectorComponent) {
            final SelectorComponent sc2 = (SelectorComponent)src;
            object.addProperty("selector", sc2.pattern());
            this.serializeSeparator(context, object, sc2.separator());
        }
        else if (src instanceof KeybindComponent) {
            object.addProperty("keybind", ((KeybindComponent)src).keybind());
        }
        else {
            if (!(src instanceof NBTComponent)) {
                throw notSureHowToSerialize(src);
            }
            final NBTComponent<?, ?> nc = (NBTComponent<?, ?>)src;
            object.addProperty("nbt", nc.nbtPath());
            object.addProperty("interpret", nc.interpret());
            if (src instanceof BlockNBTComponent) {
                final JsonElement position = context.serialize(((BlockNBTComponent)nc).pos());
                object.add("block", position);
                this.serializeSeparator(context, object, nc.separator());
            }
            else if (src instanceof EntityNBTComponent) {
                object.addProperty("entity", ((EntityNBTComponent)nc).selector());
            }
            else {
                if (!(src instanceof StorageNBTComponent)) {
                    throw notSureHowToSerialize(src);
                }
                object.add("storage", context.serialize(((StorageNBTComponent)nc).storage()));
            }
        }
        return object;
    }
    
    private void serializeSeparator(final JsonSerializationContext context, final JsonObject json, @Nullable final Component separator) {
        if (separator != null) {
            json.add("separator", context.serialize(separator));
        }
    }
    
    static JsonParseException notSureHowToDeserialize(final Object element) {
        return new JsonParseException("Don't know how to turn " + element + " into a Component");
    }
    
    private static IllegalArgumentException notSureHowToSerialize(final Component component) {
        return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
    }
}
