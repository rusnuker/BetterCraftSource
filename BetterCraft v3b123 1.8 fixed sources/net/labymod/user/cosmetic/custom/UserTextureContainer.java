// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.custom;

import java.util.UUID;

public class UserTextureContainer
{
    private String directory;
    private UUID fileName;
    private final boolean globalTexture;
    private String resolvedURL;
    private boolean loaded;
    
    public UserTextureContainer(final String directory) {
        this(directory, null);
    }
    
    public UserTextureContainer(final String directory, final UUID fileName) {
        this.resolvedURL = null;
        this.loaded = false;
        this.directory = directory;
        this.fileName = fileName;
        this.globalTexture = (fileName == null);
    }
    
    public void validateTexture(final CosmeticImageHandler handler) {
        if (this.loaded || this.resolvedURL == null) {
            return;
        }
        this.loaded = true;
        handler.loadUserTexture(this.fileName, this.resolvedURL);
    }
    
    public void unload() {
        this.loaded = false;
    }
    
    public void resolved() {
        if (this.fileName != null) {
            this.resolvedURL = String.format("http://dl.labymod.net/textures/%s", String.valueOf(this.directory) + "/" + this.fileName.toString());
            this.unload();
        }
    }
    
    public boolean isGlobalTexture() {
        return this.globalTexture;
    }
    
    public String getDirectory() {
        return this.directory;
    }
    
    public void setFileName(final UUID fileName) {
        this.fileName = fileName;
    }
    
    public UUID getFileName() {
        return this.fileName;
    }
    
    public String getResolvedURL() {
        return this.resolvedURL;
    }
    
    public boolean isLoaded() {
        return this.loaded;
    }
}
