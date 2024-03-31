// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.impl;

import com.google.gson.JsonObject;

public class Account
{
    private String email;
    private String password;
    private String name;
    private boolean banned;
    
    public Account(final String email, final String password, final String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    
    public Account() {
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isBanned() {
        return this.banned;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setBanned(final boolean banned) {
        this.banned = banned;
    }
    
    public JsonObject toJson() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", this.email);
        jsonObject.addProperty("password", this.password);
        jsonObject.addProperty("name", this.name);
        jsonObject.addProperty("banned", this.banned);
        return jsonObject;
    }
    
    public void fromJson(final JsonObject json) {
        this.email = json.get("email").getAsString();
        this.password = json.get("password").getAsString();
        this.name = json.get("name").getAsString();
        this.banned = json.get("banned").getAsBoolean();
    }
}
