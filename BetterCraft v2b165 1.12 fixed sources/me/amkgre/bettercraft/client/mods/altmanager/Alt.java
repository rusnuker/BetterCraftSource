// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.altmanager;

import com.google.gson.annotations.SerializedName;

public class Alt
{
    @SerializedName("email")
    public String email;
    @SerializedName("name")
    public String name;
    @SerializedName("password")
    public String password;
    @SerializedName("cracked")
    public boolean cracked;
    
    public Alt(final String email, final String password) {
        this.email = email;
        if (password == null || password.isEmpty()) {
            this.name = email;
            this.password = null;
            this.cracked = true;
        }
        else {
            this.name = email;
            this.password = password;
            this.cracked = false;
        }
    }
    
    @Override
    public String toString() {
        return "Alt [email=" + this.email + ", name=" + this.name + ", password=" + this.password + ", cracked=" + this.cracked + "]";
    }
}
