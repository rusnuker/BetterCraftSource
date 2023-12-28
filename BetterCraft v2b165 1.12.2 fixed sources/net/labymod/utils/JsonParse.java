// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonParse
{
    private static JsonParser jsonParser;
    
    static {
        JsonParse.jsonParser = new JsonParser();
    }
    
    public static String parse(final String input, final String... value) {
        final JsonElement jsonelement = JsonParse.jsonParser.parse(input);
        final JsonObject jsonobject = jsonelement.getAsJsonObject();
        JsonObject jsonobject2 = null;
        String s = "";
        for (int i = 0; i < value.length; ++i) {
            if (jsonobject2 == null) {
                if (value.length == 1) {
                    s = jsonobject.get(value[i]).getAsString();
                }
                else {
                    jsonobject2 = jsonobject.getAsJsonObject(value[i]);
                }
            }
            else if (i == value.length - 1) {
                s = jsonobject2.get(value[i]).getAsString();
            }
            else {
                jsonobject2 = jsonobject2.getAsJsonObject(value[i]);
            }
        }
        return s;
    }
    
    public static JsonElement parse(final String input) {
        return JsonParse.jsonParser.parse(input);
    }
}
