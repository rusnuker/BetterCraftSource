// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.api.model;

import com.google.gson.Gson;

public class Pack
{
    private static final Gson GSON;
    public int rp_id;
    public String website_name;
    public String ingame_name;
    public int size;
    public String thumbnail;
    public Screenshot[] screenshots;
    public int download;
    public String description;
    public int rating;
    public String tags;
    public String creator;
    public String created_at;
    public String updated_at;
    public String category;
    public String[] tagsArray;
    
    static {
        GSON = new Gson();
    }
    
    public String[] getTags() {
        return (this.tagsArray == null) ? (this.tagsArray = Pack.GSON.fromJson(this.tags, String[].class)) : this.tagsArray;
    }
}
