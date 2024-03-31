// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.util;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import java.io.File;
import net.minecraft.realms.Realms;

public class RealmsPersistence
{
    private static final String FILE_NAME = "realms_persistence.json";
    
    public static RealmsPersistenceData readFile() {
        final File file = new File(Realms.getGameDirectoryPath(), "realms_persistence.json");
        final Gson gson = new Gson();
        try {
            return gson.fromJson(FileUtils.readFileToString(file), RealmsPersistenceData.class);
        }
        catch (final IOException e) {
            return new RealmsPersistenceData();
        }
    }
    
    public static void writeFile(final RealmsPersistenceData data) {
        final File file = new File(Realms.getGameDirectoryPath(), "realms_persistence.json");
        final Gson gson = new Gson();
        final String json = gson.toJson(data);
        try {
            FileUtils.writeStringToFile(file, json);
        }
        catch (final IOException ex) {}
    }
    
    public static class RealmsPersistenceData
    {
        public String newsLink;
        public boolean hasUnreadNews;
        
        private RealmsPersistenceData() {
            this.hasUnreadNews = false;
        }
        
        private RealmsPersistenceData(final String newsLink, final boolean hasUnreadNews) {
            this.hasUnreadNews = false;
            this.newsLink = newsLink;
            this.hasUnreadNews = hasUnreadNews;
        }
    }
}
