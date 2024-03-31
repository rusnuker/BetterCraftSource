// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsNews extends ValueObject
{
    private static final Logger LOGGER;
    public String newsLink;
    
    public static RealmsNews parse(final String json) {
        final RealmsNews news = new RealmsNews();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(json).getAsJsonObject();
            news.newsLink = JsonUtils.getStringOr("newsLink", object, null);
        }
        catch (final Exception e) {
            RealmsNews.LOGGER.error("Could not parse RealmsNews: " + e.getMessage());
        }
        return news;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
