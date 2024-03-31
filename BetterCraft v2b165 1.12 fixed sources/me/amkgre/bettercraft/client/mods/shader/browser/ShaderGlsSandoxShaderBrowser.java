// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.browser;

import me.amkgre.bettercraft.client.mods.shader.ShaderUtils;

public class ShaderGlsSandoxShaderBrowser
{
    private String pictureID;
    private String shaderID;
    private String name;
    private String source;
    private ShaderUtils shaderUtils;
    
    public ShaderGlsSandoxShaderBrowser(final String pictureID, final String shaderID) {
        this.pictureID = pictureID;
        this.shaderID = shaderID;
    }
    
    public String getPictureID() {
        return this.pictureID;
    }
    
    public String getShaderID() {
        return this.shaderID;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(final String source) {
        this.source = source;
    }
    
    public ShaderUtils getShaderUtils() {
        return this.shaderUtils;
    }
}
