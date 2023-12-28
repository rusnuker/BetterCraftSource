// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader;

import me.amkgre.bettercraft.client.mods.shader.old.GuiShaderOld;
import me.amkgre.bettercraft.client.mods.shader.browser.GuiShaderBrowser;
import net.minecraft.client.gui.GuiScreen;

public class ShaderRenderer extends GuiScreen
{
    private static ShaderUtils shaderUtils;
    
    public static void reactiveShader(final String shaderSRC) {
        ShaderRenderer.shaderUtils = new ShaderUtils(shaderSRC);
    }
    
    public static void doShaderStuff() {
        if (GuiShaderBrowser.shader) {
            renderShader(ShaderRenderer.shaderUtils, false);
        }
        else if (GuiShaderOld.shader) {
            renderShader(GuiShaderOld.current.getShaderUtils(), false);
        }
    }
    
    public static void renderShader(final ShaderUtils shader, final boolean DefaultUniforms) {
        try {
            shader.renderFirst();
            shader.addDefaultUniforms(DefaultUniforms);
            shader.renderSecond();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
