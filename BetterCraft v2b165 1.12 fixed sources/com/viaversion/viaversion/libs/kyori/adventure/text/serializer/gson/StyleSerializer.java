// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import java.util.Set;
import java.util.EnumSet;
import com.viaversion.viaversion.libs.gson.JsonNull;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.gson.JsonSerializationContext;
import com.viaversion.viaversion.libs.gson.internal.Streams;
import java.io.Reader;
import java.io.StringReader;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.io.IOException;
import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.gson.JsonDeserializer;

final class StyleSerializer implements JsonDeserializer<Style>, JsonSerializer<Style>
{
    private static final TextDecoration[] DECORATIONS;
    static final String FONT = "font";
    static final String COLOR = "color";
    static final String INSERTION = "insertion";
    static final String CLICK_EVENT = "clickEvent";
    static final String CLICK_EVENT_ACTION = "action";
    static final String CLICK_EVENT_VALUE = "value";
    static final String HOVER_EVENT = "hoverEvent";
    static final String HOVER_EVENT_ACTION = "action";
    static final String HOVER_EVENT_CONTENTS = "contents";
    @Deprecated
    static final String HOVER_EVENT_VALUE = "value";
    private final LegacyHoverEventSerializer legacyHover;
    private final boolean emitLegacyHover;
    
    StyleSerializer(@Nullable final LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover) {
        this.legacyHover = legacyHover;
        this.emitLegacyHover = emitLegacyHover;
    }
    
    @Override
    public Style deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = json.getAsJsonObject();
        return this.deserialize(object, context);
    }
    
    private Style deserialize(final JsonObject json, final JsonDeserializationContext context) throws JsonParseException {
        final Style.Builder style = Style.style();
        if (json.has("font")) {
            style.font(context.deserialize(json.get("font"), Key.class));
        }
        if (json.has("color")) {
            final TextColorWrapper color = context.deserialize(json.get("color"), TextColorWrapper.class);
            if (color.color != null) {
                style.color(color.color);
            }
            else if (color.decoration != null) {
                style.decoration(color.decoration, true);
            }
        }
        for (int i = 0, length = StyleSerializer.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = StyleSerializer.DECORATIONS[i];
            final String name = TextDecoration.NAMES.key(decoration);
            if (json.has(name)) {
                style.decoration(decoration, json.get(name).getAsBoolean());
            }
        }
        if (json.has("insertion")) {
            style.insertion(json.get("insertion").getAsString());
        }
        if (json.has("clickEvent")) {
            final JsonObject clickEvent = json.getAsJsonObject("clickEvent");
            if (clickEvent != null) {
                final ClickEvent.Action action = optionallyDeserialize(clickEvent.getAsJsonPrimitive("action"), context, ClickEvent.Action.class);
                if (action != null && action.readable()) {
                    final JsonPrimitive rawValue = clickEvent.getAsJsonPrimitive("value");
                    final String value = (rawValue == null) ? null : rawValue.getAsString();
                    if (value != null) {
                        style.clickEvent(ClickEvent.clickEvent(action, value));
                    }
                }
            }
        }
        if (json.has("hoverEvent")) {
            final JsonObject hoverEvent = json.getAsJsonObject("hoverEvent");
            if (hoverEvent != null) {
                final HoverEvent.Action action2 = optionallyDeserialize(hoverEvent.getAsJsonPrimitive("action"), context, HoverEvent.Action.class);
                if (action2 != null && action2.readable()) {
                    Object value2;
                    if (hoverEvent.has("contents")) {
                        final JsonElement rawValue2 = hoverEvent.get("contents");
                        value2 = context.deserialize(rawValue2, action2.type());
                    }
                    else if (hoverEvent.has("value")) {
                        final Component rawValue3 = context.deserialize(hoverEvent.get("value"), Component.class);
                        value2 = this.legacyHoverEventContents(action2, rawValue3, context);
                    }
                    else {
                        value2 = null;
                    }
                    if (value2 != null) {
                        style.hoverEvent(HoverEvent.hoverEvent(action2, value2));
                    }
                }
            }
        }
        if (json.has("font")) {
            style.font(context.deserialize(json.get("font"), Key.class));
        }
        return style.build();
    }
    
    private static <T> T optionallyDeserialize(final JsonElement json, final JsonDeserializationContext context, final Class<T> type) {
        return (json == null) ? null : context.deserialize(json, type);
    }
    
    private Object legacyHoverEventContents(final HoverEvent.Action<?> action, final Component rawValue, final JsonDeserializationContext context) {
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return rawValue;
        }
        if (this.legacyHover != null) {
            try {
                if (action == HoverEvent.Action.SHOW_ENTITY) {
                    return this.legacyHover.deserializeShowEntity(rawValue, this.decoder(context));
                }
                if (action == HoverEvent.Action.SHOW_ITEM) {
                    return this.legacyHover.deserializeShowItem(rawValue);
                }
            }
            catch (final IOException ex) {
                throw new JsonParseException(ex);
            }
        }
        throw new UnsupportedOperationException();
    }
    
    private Codec.Decoder<Component, String, JsonParseException> decoder(final JsonDeserializationContext ctx) {
        return (Codec.Decoder<Component, String, JsonParseException>)(string -> {
            new JsonReader(new StringReader(string));
            final JsonReader jsonReader;
            final JsonReader reader = jsonReader;
            return (Component)ctx.deserialize(Streams.parse(reader), Component.class);
        });
    }
    
    @Override
    public JsonElement serialize(final Style src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject json = new JsonObject();
        for (int i = 0, length = StyleSerializer.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = StyleSerializer.DECORATIONS[i];
            final TextDecoration.State state = src.decoration(decoration);
            if (state != TextDecoration.State.NOT_SET) {
                final String name = TextDecoration.NAMES.key(decoration);
                assert name != null;
                json.addProperty(name, state == TextDecoration.State.TRUE);
            }
        }
        final TextColor color = src.color();
        if (color != null) {
            json.add("color", context.serialize(color));
        }
        final String insertion = src.insertion();
        if (insertion != null) {
            json.addProperty("insertion", insertion);
        }
        final ClickEvent clickEvent = src.clickEvent();
        if (clickEvent != null) {
            final JsonObject eventJson = new JsonObject();
            eventJson.add("action", context.serialize(clickEvent.action()));
            eventJson.addProperty("value", clickEvent.value());
            json.add("clickEvent", eventJson);
        }
        final HoverEvent<?> hoverEvent = src.hoverEvent();
        if (hoverEvent != null) {
            final JsonObject eventJson2 = new JsonObject();
            eventJson2.add("action", context.serialize(hoverEvent.action()));
            final JsonElement modernContents = context.serialize(hoverEvent.value());
            eventJson2.add("contents", modernContents);
            if (this.emitLegacyHover) {
                eventJson2.add("value", this.serializeLegacyHoverEvent(hoverEvent, modernContents, context));
            }
            json.add("hoverEvent", eventJson2);
        }
        final Key font = src.font();
        if (font != null) {
            json.add("font", context.serialize(font));
        }
        return json;
    }
    
    private JsonElement serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonElement modernContents, final JsonSerializationContext context) {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
            return modernContents;
        }
        if (this.legacyHover != null) {
            Component serialized = null;
            try {
                if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                    serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder(context));
                }
                else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                    serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
                }
            }
            catch (final IOException ex) {
                throw new JsonSyntaxException(ex);
            }
            return (serialized == null) ? JsonNull.INSTANCE : context.serialize(serialized);
        }
        return JsonNull.INSTANCE;
    }
    
    private Codec.Encoder<Component, String, RuntimeException> encoder(final JsonSerializationContext ctx) {
        return (Codec.Encoder<Component, String, RuntimeException>)(component -> ctx.serialize(component).toString());
    }
    
    static {
        DECORATIONS = new TextDecoration[] { TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED };
        final Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
        for (final TextDecoration decoration : StyleSerializer.DECORATIONS) {
            knownDecorations.remove(decoration);
        }
        if (!knownDecorations.isEmpty()) {
            throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
        }
    }
}
