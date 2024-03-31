// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.party.model;

import java.beans.ConstructorProperties;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import com.google.gson.JsonElement;
import java.net.ProtocolException;
import net.labymod.main.LabyMod;
import com.google.gson.Gson;

public class PartyMessage
{
    private static final Gson GSON;
    private final String action;
    private final PartyMessageData[] data;
    private LabyMod labyMod;
    
    static {
        GSON = new Gson();
    }
    
    public PartyMessage(final LabyMod labyMod, final String action, final PartyMessageData[] data) {
        this.labyMod = labyMod;
        this.action = action;
        this.data = data;
    }
    
    public int getInt(final String key) throws ProtocolException {
        final JsonElement jsonelement = this.getElement(key);
        if (jsonelement == null) {
            throw new ProtocolException(String.format("Malformed JSON in Addon message / INT @ %s:%s", this.getAction(), key));
        }
        return jsonelement.getAsInt();
    }
    
    public UUID getUUID(final String key) throws ProtocolException {
        return UUID.fromString(this.getString(key));
    }
    
    public boolean getBoolean(final String key) throws ProtocolException {
        final JsonElement jsonelement = this.getElement(key);
        if (jsonelement == null) {
            throw new ProtocolException(String.format("Malformed JSON in Addon message / BOOL @ %s:%s", this.getAction(), key));
        }
        return jsonelement.getAsBoolean();
    }
    
    public String getString(final String key) throws ProtocolException {
        final String s = this.getString(key, null);
        if (s == null) {
            throw new ProtocolException(String.format("Malformed JSON in Addon message / String @ %s:%s", this.getAction(), key));
        }
        return s;
    }
    
    public String getString(final String key, final String def) {
        final JsonElement jsonelement = this.getElement(key);
        return (jsonelement == null) ? def : jsonelement.getAsString();
    }
    
    public JsonElement getElement(final String key) {
        PartyMessageData[] data;
        for (int length = (data = this.data).length, i = 0; i < length; ++i) {
            final PartyMessageData partymessage$partymessagedata = data[i];
            if (partymessage$partymessagedata.key.equalsIgnoreCase(key)) {
                return partymessage$partymessagedata.value;
            }
        }
        return null;
    }
    
    public String getAction() {
        return this.action;
    }
    
    public PartyMessageData[] getData() {
        return this.data;
    }
    
    public static class Builder
    {
        private final PartyActionTypes.Client action;
        private List<Pair> data;
        private LabyMod labyMod;
        
        public Builder(final LabyMod labyMod, final PartyActionTypes.Client action) {
            this.data = new LinkedList<Pair>();
            this.labyMod = labyMod;
            this.action = action;
        }
        
        public Builder putUUID(final String key, final UUID content) {
            this.data.add(new Pair(key, new JsonPrimitive(content.toString())));
            return this;
        }
        
        public Builder putString(final String key, final String content) {
            this.data.add(new Pair(key, new JsonPrimitive(content)));
            return this;
        }
        
        public Builder putInt(final String key, final int content) {
            this.data.add(new Pair(key, new JsonPrimitive(content)));
            return this;
        }
        
        public Builder putBoolean(final String key, final boolean content) {
            this.data.add(new Pair(key, new JsonPrimitive(content)));
            return this;
        }
        
        public Builder setResource(final String key, final String... args) {
            this.data.add(new Pair("key", new JsonPrimitive(key)));
            final JsonArray jsonarray = new JsonArray();
            for (final String s : args) {
                jsonarray.add(new JsonPrimitive(s));
            }
            return this.putArray("args", jsonarray);
        }
        
        public Builder putArray(final String key, final JsonArray array) {
            this.data.add(new Pair(key, array));
            return this;
        }
        
        public PartyMessage build() {
            final PartyMessageData[] apartymessage$partymessagedata = new PartyMessageData[this.data.size()];
            int i = 0;
            for (final Pair partymessage$builder$pair : this.data) {
                apartymessage$partymessagedata[i] = new PartyMessageData(partymessage$builder$pair.getKey(), partymessage$builder$pair.getElement());
                ++i;
            }
            return new PartyMessage(this.labyMod, this.action.getKey(), apartymessage$partymessagedata);
        }
        
        public void send() {
            final PartyMessage partymessage = this.build();
            final String s = PartyMessage.GSON.toJson(partymessage);
            System.out.println("[OUT] " + s);
            this.labyMod.getLabyModAPI().sendAddonMessage("party", s);
        }
        
        public static class Pair
        {
            private String key;
            private JsonElement element;
            
            public String getKey() {
                return this.key;
            }
            
            public JsonElement getElement() {
                return this.element;
            }
            
            @ConstructorProperties({ "key", "element" })
            public Pair(final String key, final JsonElement element) {
                this.key = key;
                this.element = element;
            }
        }
    }
    
    public static class PartyMessageData
    {
        private String key;
        private JsonElement value;
        
        public PartyMessageData(final String key, final JsonElement value) {
            this.key = key;
            this.value = value;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public JsonElement getValue() {
            return this.value;
        }
    }
}
