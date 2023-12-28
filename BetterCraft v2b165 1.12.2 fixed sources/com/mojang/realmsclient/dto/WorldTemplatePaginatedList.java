// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class WorldTemplatePaginatedList extends ValueObject
{
    private static final Logger LOGGER;
    public List<WorldTemplate> templates;
    public int page;
    public int size;
    public int total;
    
    public WorldTemplatePaginatedList() {
    }
    
    public WorldTemplatePaginatedList(final WorldTemplatePaginatedList src) {
        this.set(src);
    }
    
    public void set(final WorldTemplatePaginatedList src) {
        this.templates = new ArrayList<WorldTemplate>((src.templates == null) ? new ArrayList<WorldTemplate>() : src.templates);
        this.page = src.page;
        this.size = src.size;
        this.total = src.total;
    }
    
    public boolean isLastPage() {
        final boolean b = this.page * this.size >= this.total && this.page > 0 && this.total > 0 && this.size > 0;
        return b;
    }
    
    public static WorldTemplatePaginatedList parse(final String json) {
        final WorldTemplatePaginatedList list = new WorldTemplatePaginatedList();
        list.templates = new ArrayList<WorldTemplate>();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(json).getAsJsonObject();
            if (object.get("templates").isJsonArray()) {
                final Iterator<JsonElement> it = object.get("templates").getAsJsonArray().iterator();
                while (it.hasNext()) {
                    list.templates.add(WorldTemplate.parse(it.next().getAsJsonObject()));
                }
            }
            list.page = JsonUtils.getIntOr("page", object, 0);
            list.size = JsonUtils.getIntOr("size", object, 0);
            list.total = JsonUtils.getIntOr("total", object, 0);
        }
        catch (final Exception e) {
            WorldTemplatePaginatedList.LOGGER.error("Could not parse WorldTemplatePaginatedList: " + e.getMessage());
        }
        return list;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
