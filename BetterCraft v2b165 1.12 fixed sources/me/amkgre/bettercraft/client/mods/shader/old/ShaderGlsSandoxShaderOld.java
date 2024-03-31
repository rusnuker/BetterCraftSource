// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.old;

import me.amkgre.bettercraft.client.mods.shader.ShaderUtils;

public class ShaderGlsSandoxShaderOld
{
    private String name;
    private String source;
    private ShaderUtils shaderUtils;
    
    public ShaderGlsSandoxShaderOld(final String name, final String source) {
        this.name = name;
        this.source = source;
        this.shaderUtils = new ShaderUtils(source);
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
