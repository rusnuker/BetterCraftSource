// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon.online.info;

import net.labymod.addon.AddonLoader;
import java.io.File;
import java.util.UUID;

public class OnlineAddonInfo extends AddonInfo
{
    protected String hash;
    protected boolean enabled;
    protected boolean includeInJar;
    protected boolean restart;
    protected boolean verified;
    protected int[] sorting;
    
    public OnlineAddonInfo(final UUID uuid, final String name, final int version, final String hash, final String author, final String description, final int category, final boolean enabled, final boolean includeInJar, final boolean restart, final boolean verified, final int[] sorting) {
        super(uuid, name, version, author, description, category);
        this.hash = hash;
        this.enabled = enabled;
        this.includeInJar = includeInJar;
        this.restart = restart;
        this.verified = verified;
        this.sorting = sorting;
    }
    
    @Override
    public String getDownloadURL() {
        return String.format("http://dl.labymod.net/latest/?file=%s&a=1", this.uuid);
    }
    
    @Override
    public String getImageURL() {
        return String.format("http://dl.labymod.net/latest/addons/%s/icon.png", this.uuid);
    }
    
    @Override
    public File getFileDestination() {
        return new File(AddonLoader.getAddonsDirectory(), String.valueOf(this.name) + ".jar");
    }
    
    public String getHash() {
        return this.hash;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public boolean isIncludeInJar() {
        return this.includeInJar;
    }
    
    public boolean isRestart() {
        return this.restart;
    }
    
    public boolean isVerified() {
        return this.verified;
    }
    
    public int[] getSorting() {
        return this.sorting;
    }
    
    public enum AddonActionState
    {
        INSTALL("INSTALL", 0), 
        INSTALL_REVOKE("INSTALL_REVOKE", 1), 
        UNINSTALL("UNINSTALL", 2), 
        UNINSTALL_REVOKE("UNINSTALL_REVOKE", 3), 
        ERROR("ERROR", 4);
        
        private AddonActionState(final String s, final int n) {
        }
    }
}
